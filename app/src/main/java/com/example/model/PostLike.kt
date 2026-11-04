package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class PostLike(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userName: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
