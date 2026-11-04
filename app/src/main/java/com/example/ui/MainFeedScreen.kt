package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.CallbookStrings
import com.example.ui.components.AdminVerificationDialog
import com.example.ui.components.CommentsSheet
import com.example.ui.components.CreatePostSheet
import com.example.ui.components.GreenAdminColor
import com.example.ui.components.InstallAppDialog
import com.example.ui.components.PostCard
import com.example.ui.components.ProfileDialog
import com.example.ui.components.UserAvatar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFeedScreen(
    viewModel: CallbookViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isCreatePostOpen by remember { mutableStateOf(false) }
    var isProfileOpen by remember { mutableStateOf(false) }
    var isAdminDialogOpen by remember { mutableStateOf(false) }
    var isInstallDialogOpen by remember { mutableStateOf(false) }

    val createSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val commentsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentUserId = uiState.currentUser?.uid
    val language = uiState.language

    // Toast feedback
    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.callbook_icon_1793649905239),
                            contentDescription = "CALLBOOK Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = CallbookStrings.appName(language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // Install App / Chrome PWA / Public Link action button
                    FilledTonalIconButton(
                        onClick = { isInstallDialogOpen = true },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .testTag("topbar_install_app_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.InstallMobile,
                            contentDescription = CallbookStrings.installApp(language),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Admin Shield Action Button (for Bir Pujara)
                    if (uiState.isAdmin) {
                        FilledTonalIconButton(
                            onClick = {
                                viewModel.loadUsersForAdmin()
                                isAdminDialogOpen = true
                            },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = GreenAdminColor.copy(alpha = 0.15f),
                                contentColor = GreenAdminColor
                            ),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .testTag("topbar_admin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = CallbookStrings.adminPanel(language),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Language Switcher button
                    OutlinedButton(
                        onClick = { viewModel.toggleLanguage() },
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("topbar_language_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = CallbookStrings.changeLanguage(language),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "नेपाली" else "EN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Profile Avatar Icon / Button
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(CircleShape)
                            .clickable { isProfileOpen = true }
                            .testTag("topbar_profile_button")
                    ) {
                        UserAvatar(
                            photoUrl = uiState.userProfile?.photoUrl,
                            name = uiState.userProfile?.name ?: "User",
                            size = 38.dp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isCreatePostOpen = true },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = CallbookStrings.createPost(language)
                    )
                },
                text = {
                    Text(
                        text = CallbookStrings.createPost(language),
                        fontWeight = FontWeight.Bold
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("create_post_fab")
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isFeedLoading && uiState.feedPosts.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Loading CALLBOOK feed..." else "फिड लोड हुँदैछ...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                uiState.feedErrorMessage != null && uiState.feedPosts.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.feedErrorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.startObservingFeed() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retry")
                        }
                    }
                }

                uiState.feedPosts.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.callbook_icon_1793649905239),
                            contentDescription = null,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = CallbookStrings.noPostsYet(language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { isCreatePostOpen = true },
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.testTag("empty_feed_create_post_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(CallbookStrings.createPost(language))
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("posts_feed_list"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(uiState.feedPosts, key = { it.id }) { post ->
                            var isLiked by remember(post.id, currentUserId) {
                                mutableStateOf(false)
                            }
                            PostCard(
                                post = post,
                                currentUserId = currentUserId,
                                language = language,
                                isLiked = isLiked,
                                onLikeClicked = {
                                    isLiked = !isLiked
                                    viewModel.toggleLike(post.id)
                                },
                                onCommentClicked = {
                                    viewModel.openComments(post)
                                },
                                onDeleteClicked = {
                                    viewModel.deletePost(post.id)
                                }
                            )
                        }
                        // Bottom spacing for FAB
                        item {
                            Spacer(modifier = Modifier.height(72.dp))
                        }
                    }
                }
            }
        }
    }

    // Create Post Modal Sheet
    if (isCreatePostOpen && uiState.userProfile != null) {
        CreatePostSheet(
            profile = uiState.userProfile!!,
            language = language,
            sheetState = createSheetState,
            isPublishing = uiState.isPublishingPost,
            onDismiss = {
                scope.launch { createSheetState.hide() }.invokeOnCompletion {
                    isCreatePostOpen = false
                }
            },
            onPublish = { text, photoData ->
                viewModel.createPost(text, photoData) {
                    scope.launch { createSheetState.hide() }.invokeOnCompletion {
                        isCreatePostOpen = false
                    }
                }
            },
            onProcessImage = { uri ->
                viewModel.processImageUri(viewModel.getApplication(), uri)
            }
        )
    }

    // Comments Modal Sheet
    if (uiState.activeCommentsPost != null) {
        CommentsSheet(
            post = uiState.activeCommentsPost!!,
            comments = uiState.activeComments,
            currentUserId = currentUserId,
            language = language,
            sheetState = commentsSheetState,
            isSubmitting = uiState.isSubmittingComment,
            onDismiss = {
                scope.launch { commentsSheetState.hide() }.invokeOnCompletion {
                    viewModel.closeComments()
                }
            },
            onSubmitComment = { text ->
                viewModel.addComment(uiState.activeCommentsPost!!.id, text)
            },
            onDeleteComment = { commentId ->
                viewModel.deleteComment(uiState.activeCommentsPost!!.id, commentId)
            }
        )
    }

    // Profile View & Edit Dialog
    if (isProfileOpen && uiState.userProfile != null) {
        ProfileDialog(
            profile = uiState.userProfile!!,
            isAdmin = uiState.isAdmin,
            language = language,
            isSaving = uiState.isSavingProfile,
            isRequestingVerification = uiState.isRequestingVerification,
            onDismiss = { isProfileOpen = false },
            onSaveProfile = { name, phone, photoUrl, bio ->
                viewModel.saveProfile(name, phone, photoUrl, bio)
            },
            onOpenAdminPanel = {
                isProfileOpen = false
                viewModel.loadUsersForAdmin()
                isAdminDialogOpen = true
            },
            onOpenInstallDialog = {
                isProfileOpen = false
                isInstallDialogOpen = true
            },
            onRequestVerification = {
                viewModel.requestVerification()
            },
            onSignOut = {
                isProfileOpen = false
                Firebase.auth.signOut()
            },
            onToggleLanguage = {
                viewModel.toggleLanguage()
            },
            onProcessImage = { uri ->
                viewModel.processImageUri(viewModel.getApplication(), uri)
            }
        )
    }

    // Admin Verification Dialog (Only for Bir Pujara)
    if (isAdminDialogOpen && uiState.isAdmin) {
        AdminVerificationDialog(
            users = uiState.allUsersForAdmin,
            isLoading = uiState.isLoadingUsers,
            language = language,
            onDismiss = { isAdminDialogOpen = false },
            onSetVerification = { targetUserId, isVerified ->
                viewModel.setVerificationStatus(targetUserId, isVerified)
            }
        )
    }

    // Install CALLBOOK App Dialog (Google Chrome PWA + Public Link + Offline info)
    if (isInstallDialogOpen) {
        InstallAppDialog(
            lang = language,
            onDismiss = { isInstallDialogOpen = false }
        )
    }
}
