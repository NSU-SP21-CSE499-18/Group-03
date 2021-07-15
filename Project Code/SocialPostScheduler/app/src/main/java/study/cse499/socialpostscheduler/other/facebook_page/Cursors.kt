package study.cse499.socialpostscheduler.other.facebook_page

import kotlinx.serialization.Serializable

@Serializable
data class Cursors(
    val after: String,
    val before: String
)