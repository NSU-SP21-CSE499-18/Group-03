package study.cse499.socialpostscheduler.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.other.facebook_page.FacebookPageList
import study.cse499.socialpostscheduler.viewmodel.ScheduleViewModel
import java.util.*


class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    lateinit var viewModel : ScheduleViewModel
    private val args: ScheduleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        val callback =  object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        val response = args.accessToken
        val obj = Json.decodeFromString<FacebookPageList>(response)
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

        btSavePost.setOnClickListener {
            viewModel.insertSchedulePost(etPostContent.text.toString(), Calendar.getInstance().time)
            val request = GraphRequest.newPostRequest(
                accessTokenPage,
                "/${objectData.id}/feed",
                JSONObject("{\"message\":\"${etPostContent.text.toString()}\"}")){

            }
            request.executeAsync()
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}