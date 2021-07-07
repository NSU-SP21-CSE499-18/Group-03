package study.cse499.socialpostscheduler.other.instagram_page

import kotlinx.serialization.Serializable

@Serializable
data class InstagramPage(
    val id: String,
    val instagram_business_account: InstagramBusinessAccount
)