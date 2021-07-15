package study.cse499.socialpostscheduler.repository

import androidx.lifecycle.LiveData
import retrofit2.Response
import study.cse499.socialpostscheduler.data.local.ShoppingItem
import study.cse499.socialpostscheduler.data.remote.ImageResponse
import study.cse499.socialpostscheduler.other.Resource

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>
    fun observeTotalPrice(): LiveData<Float>
    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}