package study.cse499.socialpostscheduler.other.facebook_page

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String
)