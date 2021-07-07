package study.cse499.socialpostscheduler.other.facebook_page

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val access_token: String,
    val category: String,
    val category_list: List<Category>,
    val id: String,
    val name: String,
    val tasks: List<String>
)