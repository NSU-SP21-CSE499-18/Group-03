package study.cse499.socialpostscheduler.other.facebook_page

import kotlinx.serialization.Serializable

@Serializable
data class Paging(
    val cursors: Cursors
)