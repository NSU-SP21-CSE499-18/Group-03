package study.cse499.socialpostscheduler.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import study.cse499.socialpostscheduler.BuildConfig

interface PixabayAPI {
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}