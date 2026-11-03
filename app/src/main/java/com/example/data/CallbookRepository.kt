package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.model.Comment
import com.example.model.Post
import com.example.model.UserProfile
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CallbookRepository(private val context: Context) {

    private val firestore: FirebaseFirestore by lazy {
        val databaseId = context.getString(R.string.firestore_database_id)
        FirebaseFirestore.getInstance(FirebaseApp.getInstance(), databaseId)
    }

    private val usersCollection get() = firestore.collection("users")
    private val postsCollection get() = firestore.collection("posts")

    // ==================== User Profile ====================

    fun observeUserProfile(uid: String): Flow<UserProfile?> = callbackFlow {
        val listener = usersCollection.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("CallbookRepository", "Error observing user profile: ${error.message}")
                trySend(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val profile = snapshot.toObject(UserProfile::class.java)
                trySend(profile)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            val doc = usersCollection.document(uid).get().await()
            if (doc.exists()) doc.toObject(UserProfile::class.java) else null
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to get user profile: ${e.message}")
            null
        }
    }

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            usersCollection.document(profile.uid).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to save user profile: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updatePhoneNumber(uid: String, phone: String): Result<Unit> {
        return try {
            usersCollection.document(uid).update(
                mapOf(
                    "phone" to phone,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to update phone number: ${e.message}")
            Result.failure(e)
        }
    }

    // ==================== Posts ====================

    fun observePosts(): Flow<List<Post>> = callbackFlow {
        val listener = postsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CallbookRepository", "Error observing posts: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val posts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(posts)
            }
        awaitClose { listener.remove() }
    }

    fun observeUserPosts(userId: String): Flow<List<Post>> = callbackFlow {
        val listener = postsCollection
            .whereEqualTo("authorId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CallbookRepository", "Error observing user posts: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val posts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(posts)
            }
        awaitClose { listener.remove() }
    }

    suspend fun createPost(post: Post): Result<String> {
        return try {
            val docRef = postsCollection.document()
            val newPost = post.copy(id = docRef.id)
            docRef.set(newPost).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to create post: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deletePost(postId: String): Result<Unit> {
        return try {
            postsCollection.document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to delete post: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun toggleLike(postId: String, userId: String): Result<Unit> {
        return try {
            val postRef = postsCollection.document(postId)
            val snapshot = postRef.get().await()
            val post = snapshot.toObject(Post::class.java) ?: return Result.failure(Exception("Post not found"))

            val isLiked = post.likedUserIds.contains(userId)
            if (isLiked) {
                postRef.update(
                    mapOf(
                        "likedUserIds" to FieldValue.arrayRemove(userId),
                        "likeCount" to FieldValue.increment(-1)
                    )
                ).await()
            } else {
                postRef.update(
                    mapOf(
                        "likedUserIds" to FieldValue.arrayUnion(userId),
                        "likeCount" to FieldValue.increment(1)
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to toggle like: ${e.message}")
            Result.failure(e)
        }
    }

    // ==================== Comments ====================

    fun observeComments(postId: String): Flow<List<Comment>> = callbackFlow {
        val listener = postsCollection.document(postId)
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CallbookRepository", "Error observing comments: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val comments = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(comments)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addComment(postId: String, comment: Comment): Result<String> {
        return try {
            val commentsCollection = postsCollection.document(postId).collection("comments")
            val docRef = commentsCollection.document()
            val newComment = comment.copy(id = docRef.id, postId = postId)

            // Add comment and increment post commentCount atomically
            val batch = firestore.batch()
            batch.set(docRef, newComment)
            batch.update(postsCollection.document(postId), "commentCount", FieldValue.increment(1))
            batch.commit().await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e("CallbookRepository", "Failed to add comment: ${e.message}")
            Result.failure(e)
        }
    }
}
