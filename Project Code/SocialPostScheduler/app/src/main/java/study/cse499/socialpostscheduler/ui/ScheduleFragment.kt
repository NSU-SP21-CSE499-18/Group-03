package study.cse499.socialpostscheduler.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import study.cse499.socialpostscheduler.BackgroundWorker
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.other.facebook_page.FacebookPageList
import study.cse499.socialpostscheduler.other.instagram_page.InstagramPage
import study.cse499.socialpostscheduler.other.instagram_page.MediaContainer
import study.cse499.socialpostscheduler.viewmodel.ScheduleViewModel
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min


class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    lateinit var viewModel : ScheduleViewModel
    var facebookLogin: Boolean = false;
    var instagramLogin: Boolean = false;

    var year_picker: Int = 0;
    var month_picker: Int = 0;
    var day_picker: Int = 0;
    var hour_picker: Int = 0;
    var min_picker: Int = 0;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        val callback =  object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }


        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPref?.let {
            val response = "";
            instagramLogin = it.getBoolean("isInstagram",false)
            facebookLogin = it.getBoolean("isFacebook",false)

            if(facebookLogin){
                checkboxFacebook.visibility = View.VISIBLE
            }

            if(instagramLogin){
                checkboxInstagram.visibility = View.VISIBLE
            }


            checkboxFacebook.setOnCheckedChangeListener { _, isChecked ->
                if(checkboxFacebook.isChecked || checkboxInstagram.isChecked){
                    textInputLayout.visibility = View.VISIBLE
                    llPostTypeContainer.visibility = View.VISIBLE
                }else{
                    textInputLayout.visibility = View.GONE
                    llPostTypeContainer.visibility = View.GONE
                }
            }

            checkboxInstagram.setOnCheckedChangeListener { _, isChecked ->
                if(checkboxFacebook.isChecked || checkboxInstagram.isChecked){
                    textInputLayout.visibility = View.VISIBLE
                    llPostTypeContainer.visibility = View.VISIBLE
                }else{
                    textInputLayout.visibility = View.GONE
                    llPostTypeContainer.visibility = View.GONE
                }

                if(isChecked){
                    textInputLayoutUrl.visibility = View.VISIBLE
                }else{
                    textInputLayoutUrl.visibility = View.GONE
                }
            }

            checkboxScheduleNow.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    checkboxScheduleLater.isChecked = false;
                }
                if(checkboxScheduleNow.isChecked || checkboxScheduleLater.isChecked){
                    llStatus.visibility = View.VISIBLE
                }
            }

            checkboxScheduleLater.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    checkboxScheduleNow.isChecked = false;
                    scheduleTimeContainer.visibility = View.VISIBLE;
                }else{
                    scheduleTimeContainer.visibility = View.GONE;
                }

                if(checkboxScheduleNow.isChecked || checkboxScheduleLater.isChecked){
                    llStatus.visibility = View.VISIBLE
                }
            }

            sendButtonAction();

//            if(facebookLogin){
//                facebookPost(response)
//            }else{
//                instagramPost(response)
//            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        setDate();
    }

    fun setDate(){
        tvDate.setOnClickListener {
            val c = Calendar.getInstance()
            year_picker = c.get(Calendar.YEAR)
            month_picker = c.get(Calendar.MONTH)
            day_picker = c.get(Calendar.DAY_OF_MONTH)

            val picker = DatePickerDialog(requireContext(),
                { datePicker, year, month, day ->
                    tvDate.setText(day.toString() + "/" + (month + 1) + "/" + year)
                    year_picker = year
                    month_picker = month
                    day_picker = day
                }, year_picker, month_picker, day_picker
            )

            picker.show()
        }

        tvTime.setOnClickListener {
            val c = Calendar.getInstance()
            hour_picker = c.get(Calendar.HOUR_OF_DAY)
            min_picker = c.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(requireContext(),
                { timePicker, hour, min ->
                    tvTime.setText("$hour:$min")
                    hour_picker = hour
                    min_picker = min
                }, hour_picker, min_picker, false
            )

            timePickerDialog.show()
        }

    }

    fun sendButtonAction(){
        btSavePost.setOnClickListener {
            if(checkboxScheduleNow.isChecked){
                if(checkboxFacebook.isChecked){
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.let {pref ->
                        val response = pref.getString("facebook_token", "");
                        response?.let {
                            if(checkboxScheduleNow.isChecked){
                                facebookPost(it, etPostContent.text.toString());
                            }
                        }
                    }

                }else{
                    if(checkboxInstagram.isChecked){
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                        sharedPref?.let { pref ->
                            val response = pref.getString("instagram_token", "");
                            response?.let {
                                if (checkboxScheduleNow.isChecked) {
                                    instagramPost(it);
                                }
                            }
                        }
                    }
                }
//
            }else {

                val scheduleCal = Calendar.getInstance()
                scheduleCal.set(Calendar.YEAR, year_picker)
                scheduleCal.set(Calendar.MONTH, month_picker)
                scheduleCal.set(Calendar.DAY_OF_MONTH, day_picker)
                scheduleCal.set(Calendar.HOUR_OF_DAY, hour_picker)
                scheduleCal.set(Calendar.MINUTE, min_picker)

                val currentCal = Calendar.getInstance()

                Log.d("minute", "year: " + year_picker)
                Log.d("minute", "month: " + month_picker)
                Log.d("minute", "day: " + day_picker)
                Log.d("minute", "hour: " + hour_picker)
                Log.d("minute", "minute: " + min_picker)

                Log.d("minute", "currentcal: " + currentCal.timeInMillis)
                Log.d("minute", "schedule: " + scheduleCal.timeInMillis)
                if (currentCal.timeInMillis < scheduleCal.timeInMillis) {

                    val diff = scheduleCal.timeInMillis - currentCal.timeInMillis
                    val minute = TimeUnit.MILLISECONDS.toMinutes(diff)

                    var facebookData = "";
                    var instagramData = "";

                    if (checkboxFacebook.isChecked) {
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                        sharedPref?.let { pref ->
                            val response = pref.getString("facebook_token", "");
                            response?.let {
                                facebookData = it;
                            }
                        }

                    } else {
                        if (checkboxInstagram.isChecked) {
                            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                            sharedPref?.let { pref ->
                                val response = pref.getString("instagram_token", "");
                                response?.let {
                                    instagramData = it;
                                }
                            }
                        }
                    }

                    val data = Data.Builder()
                        .putString("post_data_facebook", facebookData)
                        .putString("post_data_instagram", instagramData)
                        .putString("body", etPostContent.text.toString())
                        .putString("image", etPostContentUrl.text.toString())
                        .putBoolean("isFacebook", checkboxFacebook.isChecked)
                        .putBoolean("isInstagram", checkboxInstagram.isChecked)
                        .build()
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresCharging(false)
                        .build();
                    val oneTimeWorkRequest =
                        OneTimeWorkRequest.Builder(BackgroundWorker::class.java)
                            .setInputData(data)
                            .setConstraints(constraints)
                            .setInitialDelay(minute, TimeUnit.MINUTES)
                            .addTag("postdata")
                            .build()

                    WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)

                    Toast.makeText(context, "Post Scheduled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Wrong Data Input", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun instagramPost(response: String){
        tvStatus.text = "Instagram Post Uploading"
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
                JSONObject("{\"image_url\":\"${etPostContentUrl.text.toString()}\"}")){ it ->
                it.rawResponse?.let { response ->
                    val mediaContainerObj = Json.decodeFromString<MediaContainer>(response)
                    publishInstagramContent(
                        obj.instagram_business_account.id,
                        mediaContainerObj.id,
                        accessTokenUser
                    )
                }

            }
            request.executeAsync()
        }
//        uploadImageContainer.visibility = View.VISIBLE
//        btUploadImage.setOnClickListener {
//            ImagePicker.with(this) //Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                .start()
//        }

    }

    fun publishInstagramContent(accountId: String , containerId: String, access_token: AccessToken?){
        val request = GraphRequest.newPostRequest(
            access_token,
            "/${accountId}/media_publish",
            JSONObject("{\"creation_id\":\"${containerId}\"}")){
            Toast.makeText(context, "Content Uploaded", Toast.LENGTH_SHORT).show()
            tvStatus.text = "Instagram Upload Completed"
        }
        request.executeAsync()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val file = ImagePicker.getFile(data)
            if (file != null) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                // Use Uri object instead of File to avoid storage permissions
                ivUploadImage.setImageBitmap(bitmap)
                //Do my work
            }

        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    fun facebookPost(response: String?, body: String?) {

        tvStatus.text = "Facebook Post Sending"
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
            var jsonObject = JSONObject();
            if(checkboxInstagram.isChecked){
                url = "/${objectData.id}/photos"
                jsonObject = JSONObject("{\"message\":\"${body}\", \"url\":\"${etPostContentUrl.text.toString()}\"}")
            }else{
                url = "/${objectData.id}/feed"
                jsonObject = JSONObject("{\"message\":\"${body}\"}");
            }
            val request = GraphRequest.newPostRequest(
                accessTokenPage,
                url,
                jsonObject

            ) {
                tvStatus.text = "Facebook Post Completed"
                if(checkboxInstagram.isChecked){
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    sharedPref?.let { pref ->
                        val response = pref.getString("instagram_token", "");
                        response?.let {
                            if (checkboxScheduleNow.isChecked) {
                                instagramPost(it);
                            }
                        }
                    }
                }
            }
            request.executeAsync()
        }
    }
}
