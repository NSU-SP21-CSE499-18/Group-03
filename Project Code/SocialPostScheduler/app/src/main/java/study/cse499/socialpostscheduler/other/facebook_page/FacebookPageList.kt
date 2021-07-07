package study.cse499.socialpostscheduler.other.facebook_page

import kotlinx.serialization.Serializable

@Serializable
data class FacebookPageList(
    val `data`: List<Data>,
    val paging: Paging
)