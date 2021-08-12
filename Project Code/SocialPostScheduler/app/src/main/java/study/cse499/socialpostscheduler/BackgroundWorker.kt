package study.cse499.socialpostscheduler

import android.content.Context
import android.util.Log
import androidx.work.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import study.cse499.socialpostscheduler.other.facebook_page.FacebookPageList
import java.util.concurrent.TimeUnit

class BackgroundWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        val inputData = inputData
        val pageData = inputData.getString("post_data")
        val body = inputData.getString("body")
        Log.d("work_manager", "facebook post started")
        facebookPost(pageData, body)
        return Result.success()
    }

    fun facebookPost(response: String?, body: String?){
        val obj = Json.decodeFromString<FacebookPageList>(response!!)
        val objectData = obj.data[0]
        val accessTokenUser = AccessToken.getCurrentAccessToken()
        var accessTokenPage: AccessToken? = null
        accessTokenUser?.let{
            accessTokenPage = AccessToken(
                objectData.access_token,
                accessTokenUser.applicationId,
                accessTokenUser.userId,
                accessTokenUser.permissions,
                null,
                null,
                accessTokenUser.source,
                accessTokenUser.expires,
                null,
                null,
                null
            )
        }

        val request = GraphRequest.newPostRequest(
            accessTokenPage,
            "/${objectData.id}/feed",
            JSONObject("{\"message\":\"${body}\"}")
        ){

        }
        request.executeAndWait()
    }
}