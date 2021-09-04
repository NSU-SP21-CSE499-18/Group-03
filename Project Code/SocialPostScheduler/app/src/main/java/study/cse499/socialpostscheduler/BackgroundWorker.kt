package study.cse499.socialpostscheduler

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import study.cse499.socialpostscheduler.other.facebook_page.FacebookPageList
import study.cse499.socialpostscheduler.other.instagram_page.InstagramPage
import study.cse499.socialpostscheduler.other.instagram_page.MediaContainer
import java.util.concurrent.TimeUnit

class BackgroundWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    var isFacebook = false;
    var isInstagram = false;
    var facebookData = "";
    var instagramData = "";
    var image = "";
    var body = "";

    override fun doWork(): Result {
        val inputData = inputData
        inputData.getString("body")?.let {
            body = it;
        }

        isFacebook = inputData.getBoolean("isFacebook",false);
        isInstagram = inputData.getBoolean("isInstagram",false);
        inputData.getString("post_data_facebook")?.let {
            facebookData = it;
        };

        inputData.getString("post_data_instagram")?.let {
            instagramData = it;
        };

        inputData.getString("image")?.let {
            image = it;
        };

        inputData.getString("image")?.let {
            image = it;
        };

        if(isFacebook){
            facebookPost(facebookData, body);
        }else{
            instagramPost(instagramData);
        }
        return Result.success()
    }

    fun facebookPost(response: String?, body: String?) {

        val obj = Json.decodeFromString<FacebookPageList>(response!!)
        val objectData = obj.data[0]
        val accessTokenUser = AccessToken.getCurrentAccessToken()
        var accessTokenPage: AccessToken? = null
        val permissions = listOf(
            "public_profile",
            "email",
            "pages_manage_posts",
            "pages_show_list"
        )
        accessTokenUser?.let {
            accessTokenPage = AccessToken(
                objectData.access_token,
                accessTokenUser.applicationId,
                accessTokenUser.userId,
                permissions,
                null,
                null,
                accessTokenUser.source,
                accessTokenUser.expires,
                null,
                null,
                null
            )
            var url = "";
            var jsonObject: JSONObject;
            if(isInstagram){
                url = "/${objectData.id}/photos"
                jsonObject = JSONObject("{\"message\":\"${body}\", \"url\":\"${image}\"}")
            }else{
                url = "/${objectData.id}/feed"
                jsonObject = JSONObject("{\"message\":\"${body}\"}");
            }
            val request = GraphRequest.newPostRequest(
                accessTokenPage,
                url,
                jsonObject

            ) {
                if(isInstagram){
                    instagramPost(instagramData);
                }
            }
            request.executeAndWait()
        }
    }

    fun instagramPost(response: String){
        val obj = Json.decodeFromString<InstagramPage>(response)
        var accessTokenUser = AccessToken.getCurrentAccessToken()
        val permissions = listOf(
            "public_profile",
            "email",
            "business_management",
            "instagram_basic",
            "pages_manage_posts",
            "instagram_content_publish",
            "pages_read_engagement",
            "pages_show_list")

        accessTokenUser?.let {
            val newToken = AccessToken(
                accessTokenUser.token,
                accessTokenUser.applicationId,
                accessTokenUser.userId,
                permissions,
                null,
                null,
                accessTokenUser.source,
                accessTokenUser.expires,
                null,
                null,
                null
            )

            val request = GraphRequest.newPostRequest(
                newToken,
                "/${obj.instagram_business_account.id}/media",
                JSONObject("{\"image_url\":\"${image}\"}")){ it ->
                it.rawResponse?.let { response ->
                    val mediaContainerObj = Json.decodeFromString<MediaContainer>(response)
                    publishInstagramContent(
                        obj.instagram_business_account.id,
                        mediaContainerObj.id,
                        accessTokenUser
                    )
                }

            }
            request.executeAndWait()
        }
    }

    fun publishInstagramContent(accountId: String , containerId: String, access_token: AccessToken?){
        val request = GraphRequest.newPostRequest(
            access_token,
            "/${accountId}/media_publish",
            JSONObject("{\"creation_id\":\"${containerId}\"}")){
        }
        request.executeAndWait()
    }
}