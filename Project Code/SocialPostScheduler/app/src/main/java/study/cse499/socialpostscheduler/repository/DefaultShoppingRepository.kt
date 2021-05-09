package study.cse499.socialpostscheduler.repository

import androidx.lifecycle.LiveData
import study.cse499.socialpostscheduler.data.local.ShoppingDao
import study.cse499.socialpostscheduler.data.local.ShoppingItem
import study.cse499.socialpostscheduler.data.remote.ImageResponse
import study.cse499.socialpostscheduler.data.remote.PixabayAPI
import study.cse499.socialpostscheduler.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItem()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("An unkown error", null)
            }else{
                Resource.error("An unkown error", null)
            }
        }catch (e: Exception){
            Resource.error("Couldn't reach the server", null)
        }
    }
}