package com.example.ui

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.auth.AuthManager
import com.example.data.CallbookRepository
import com.example.model.Comment
import com.example.model.Post
import com.example.model.UserProfile
import com.example.util.AppLanguage
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallbookViewModel(
    private val authManager: AuthManager,
    private val repository: CallbookRepository
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    val currentUser: StateFlow<FirebaseUser?> = authManager.currentUserFlow

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList)
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _activeCommentsPost = MutableStateFlow<Post?>(null)
    val activeCommentsPost: StateFlow<Post?> = _activeCommentsPost.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private var profileJob: Job? = null
    private var postsJob: Job? = null
    private var commentsJob: Job? = null

    init {
        // Observe auth state changes to start/stop user data observation
        viewModelScope.launch {
            authManager.currentUserFlow.collect { user ->
                profileJob?.cancel()
                if (user != null) {
                    observeUserProfile(user.uid)
                } else {
                    _currentUserProfile.value = null
                }
            }
        }

        // Observe feed posts
        observeFeed()
    }

    private fun observeUserProfile(uid: String) {
        profileJob = viewModelScope.launch {
            repository.observeUserProfile(uid).collect { profile ->
                _currentUserProfile.value = profile
            }
        }
    }

    private fun observeFeed() {
        postsJob?.cancel()
        postsJob = viewModelScope.launch {
            repository.observePosts().collect { postList ->
                _posts.value = postList
            }
        }
    }

    fun switchLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ENGLISH) {
            AppLanguage.NEPALI
        } else {
            AppLanguage.ENGLISH
        }
    }

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = authManager.signInWithGoogle(activity)
            result.onSuccess { user ->
                // Ensure user profile document exists in Firestore
                repository.createOrUpdateUserProfile(
                    uid = user.uid,
                    displayName = user.displayName ?: "",
                    email = user.email ?: "",
                    photoUrl = user.photoUrl?.toString() ?: "",
                    phone = "",
                    bio = ""
                )
            }.onFailure { ex ->
                _errorMessage.value = ex.localizedMessage ?: "Sign-in failed"
            }
            _isLoading.value = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
            _currentUserProfile.value = null
        }
    }

    fun savePhoneNumber(phoneNumber: String, onSuccess: () -> Unit = {}) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val current = _currentUserProfile.value
            val result = repository.createOrUpdateUserProfile(
                uid = user.uid,
                displayName = current?.displayName ?: user.displayName ?: "",
                email = current?.email ?: user.email ?: "",
                photoUrl = current?.photoUrl ?: user.photoUrl?.toString() ?: "",
                phone = phoneNumber.trim(),
                bio = current?.bio ?: ""
            )
            _isLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure { ex ->
                _errorMessage.value = ex.localizedMessage ?: "Failed to save phone number"
            }
        }
    }

    fun updateProfile(displayName: String, bio: String, phone: String, photoBase64OrUrl: String?, onSuccess: () -> Unit = {}) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val current = _currentUserProfile.value
            val targetPhoto = photoBase64OrUrl ?: current?.photoUrl ?: user.photoUrl?.toString() ?: ""
            val result = repository.createOrUpdateUserProfile(
                uid = user.uid,
                displayName = displayName.trim(),
                email = current?.email ?: user.email ?: "",
                photoUrl = targetPhoto,
                phone = phone.trim(),
                bio = bio.trim()
            )
            _isLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure { ex ->
                _errorMessage.value = ex.localizedMessage ?: "Failed to update profile"
            }
        }
    }

    fun createPost(text: String, photoBase64: String?, onSuccess: () -> Unit) {
        val user = currentUser.value ?: return
        val profile = _currentUserProfile.value
        val authorName = profile?.displayName?.ifEmpty { null } ?: user.displayName ?: "User"
        val authorPhoto = profile?.photoUrl?.ifEmpty { null } ?: user.photoUrl?.toString() ?: ""

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.createPost(
                authorId = user.uid,
                authorName = authorName,
                authorPhotoUrl = authorPhoto,
                text = text.trim(),
                photoBase64 = photoBase64
            )
            _isLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure { ex ->
                _errorMessage.value = ex.localizedMessage ?: "Failed to publish post"
            }
        }
    }

    fun toggleLike(postId: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleLike(postId, user.uid)
        }
    }

    fun deletePost(postId: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deletePost(postId, user.uid)
            _isLoading.value = false
            result.onFailure { ex ->
                _errorMessage.value = ex.localizedMessage ?: "Failed to delete post"
            }
        }
    }

    fun openComments(post: Post) {
        _activeCommentsPost.value = post
        commentsJob?.cancel()
        commentsJob = viewModelScope.launch {
            repository.observeComments(post.id).collect { commentList ->
                _comments.value = commentList
            }
        }
    }

    fun closeComments() {
        commentsJob?.cancel()
        _activeCommentsPost.value = null
        _comments.value = emptyList()
    }

    fun addComment(postId: String, text: String, onComplete: () -> Unit = {}) {
        val user = currentUser.value ?: return
        if (text.isBlank()) return
        val profile = _currentUserProfile.value
        val authorName = profile?.displayName?.ifEmpty { null } ?: user.displayName ?: "User"
        val authorPhoto = profile?.photoUrl?.ifEmpty { null } ?: user.photoUrl?.toString() ?: ""

        viewModelScope.launch {
            val result = repository.addComment(
                postId = postId,
                authorId = user.uid,
                authorName = authorName,
                authorPhotoUrl = authorPhoto,
                text = text.trim()
            )
            result.onSuccess {
                onComplete()
            }.onFailure { ex ->
                _errorMessage.value = ex.localizedMessage ?: "Failed to post comment"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val authManager = AuthManager(context)
            val repository = CallbookRepository(context)
            return CallbookViewModel(authManager, repository) as T
        }
    }
}
