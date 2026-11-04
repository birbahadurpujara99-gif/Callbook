package com.example.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.localization.AppLanguage
import com.example.localization.CallbookStrings
import com.example.model.Post
import com.google.firebase.Timestamp

@Composable
fun PostCard(
    post: Post,
    currentUserId: String?,
    language: AppLanguage,
    isLiked: Boolean,
    onLikeClicked: () -> Unit,
    onCommentClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Author info & actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    photoUrl = post.authorPhotoUrl,
                    name = post.authorName,
                    size = 44.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    UserNameWithBadges(
                        name = post.authorName,
                        isVerified = post.authorIsVerified,
                        role = post.authorRole,
                        language = language
                    )
                    Text(
                        text = formatRelativeTime(post.createdAt, language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (currentUserId != null && currentUserId == post.authorId) {
                    IconButton(
                        onClick = onDeleteClicked,
                        modifier = Modifier.testTag("delete_post_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = CallbookStrings.delete(language),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Post Text Content
            if (post.text.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = post.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Post Photo Content
            if (!post.photoData.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                PostImage(
                    photoData = post.photoData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Likes & Comments
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Like Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onLikeClicked() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("like_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = CallbookStrings.like(language),
                        tint = if (isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.likesCount}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isLiked) FontWeight.Bold else FontWeight.Medium,
                        color = if (isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onCommentClicked() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("comment_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = CallbookStrings.comments(language),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${post.commentsCount}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun UserAvatar(
    photoUrl: String?,
    name: String,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUrl.isNullOrBlank()) {
            if (photoUrl.startsWith("data:image")) {
                val bitmap = remember(photoUrl) {
                    try {
                        val base64Data = photoUrl.substringAfter("base64,")
                        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                    } catch (e: Exception) {
                        null
                    }
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(size)
                    )
                } else {
                    AvatarInitials(name)
                }
            } else {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(size)
                )
            }
        } else {
            AvatarInitials(name)
        }
    }
}

@Composable
private fun AvatarInitials(name: String) {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "U"
    Text(
        text = initial,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun PostImage(
    photoData: String,
    modifier: Modifier = Modifier
) {
    if (photoData.startsWith("data:image")) {
        val bitmap = remember(photoData) {
            try {
                val base64Data = photoData.substringAfter("base64,")
                val bytes = Base64.decode(base64Data, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Post photo",
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    } else {
        AsyncImage(
            model = photoData,
            contentDescription = "Post photo",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

fun formatRelativeTime(timestamp: Timestamp?, language: AppLanguage): String {
    if (timestamp == null) return CallbookStrings.justNow(language)
    val now = System.currentTimeMillis()
    val timeMillis = timestamp.toDate().time
    val diffSeconds = (now - timeMillis) / 1000

    return when {
        diffSeconds < 60 -> CallbookStrings.justNow(language)
        diffSeconds < 3600 -> CallbookStrings.minutesAgo(diffSeconds / 60, language)
        diffSeconds < 86400 -> CallbookStrings.hoursAgo(diffSeconds / 3600, language)
        else -> CallbookStrings.daysAgo(diffSeconds / 86400, language)
    }
}
