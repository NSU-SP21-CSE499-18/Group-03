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

            if(instagramLogin){
                checkboxFacebook.visibility = View.VISIBLE
            }

            if(facebookLogin){
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
                }else{
                    textInputLayout.visibility = View.GONE
                }

                if(isChecked){
                    uploadImageContainer.visibility = View.VISIBLE
                }else{
                    uploadImageContainer.visibility = View.GONE
                }
            }

            if(checkboxFacebook.isChecked){
                val response = sharedPref.getString("facebook_token", "");
                response?.let {
                    if(checkboxScheduleNow.isChecked){
                        facebookPost(it);
                    }
                }
            }

            checkboxScheduleNow.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    checkboxScheduleLater.isChecked = false;
                }
            }

            checkboxScheduleLater.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    checkboxScheduleNow.isChecked = false;
                }
            }

//            if(facebookLogin){
//                facebookPost(response)
//            }else{
//                instagramPost(response)
//            }

        }
//        requireActivity().onBackPressedDispatcher.addCallback(callback)
//        setDate()
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

    fun facebookPost(response: String){
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


            facebookPost(response, etPostContent.text.toString());
//            val scheduleCal = Calendar.getInstance()
//
//            scheduleCal.set(Calendar.YEAR, year_picker)
//            scheduleCal.set(Calendar.MONTH, month_picker)
//            scheduleCal.set(Calendar.DAY_OF_MONTH, day_picker)
//            scheduleCal.set(Calendar.HOUR_OF_DAY, hour_picker)
//            scheduleCal.set(Calendar.MINUTE, min_picker)
//
//            val currentCal = Calendar.getInstance()
//
//            Log.d("minute", "year: " + year_picker)
//            Log.d("minute", "month: " + month_picker)
//            Log.d("minute", "day: " + day_picker)
//            Log.d("minute", "hour: " + hour_picker)
//            Log.d("minute", "minute: " + min_picker)
//
//            Log.d("minute", "currentcal: " + currentCal.timeInMillis)
//            Log.d("minute", "schedule: " + scheduleCal.timeInMillis)
//            if (currentCal.timeInMillis < scheduleCal.timeInMillis) {
//
//                val diff = scheduleCal.timeInMillis - currentCal.timeInMillis
//                val minute = TimeUnit.MILLISECONDS.toMinutes(diff)
//                Log.d("minute", "minute: " + minute)
//                Log.d("minute", "response: " + response)
//                Log.d("minute", "body: " + etPostContent.text.toString())
//                val data = Data.Builder()
//                    .putString("post_data",response)
//                    .putString("body", etPostContent.text.toString())
//                    .build()
//                val constraints = Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .setRequiresCharging(false)
//                    .build();
//                val oneTimeWorkRequest = OneTimeWorkRequest.Builder(BackgroundWorker::class.java)
//                    .setInputData(data)
//                    .setConstraints(constraints)
//                    .setInitialDelay(minute, TimeUnit.MINUTES)
//                    .addTag("postdata")
//                    .build()
//
//                WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)
//
//                Toast.makeText(context, "Post Scheduled", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(context, "Wrong Data Input", Toast.LENGTH_SHORT).show()
//            }


//            viewModel.insertSchedulePost(etPostContent.text.toString(), Calendar.getInstance().time)
//            val request = GraphRequest.newPostRequest(
//                accessTokenPage,
//                "/${objectData.id}/feed",
//                JSONObject("{\"message\":\"${etPostContent.text.toString()}\"}")){
//
//            }
//            request.executeAsync()
        }
    }

    fun instagramPost(response: String){
        val obj = Json.decodeFromString<InstagramPage>(response)
        val accessTokenUser = AccessToken.getCurrentAccessToken()
        uploadImageContainer.visibility = View.VISIBLE
        btUploadImage.setOnClickListener {
            ImagePicker.with(this) //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        btSavePost.setOnClickListener {
            viewModel.insertSchedulePost(etPostContent.text.toString(), Calendar.getInstance().time)

            val request = GraphRequest.newPostRequest(
                accessTokenUser,
                "/${obj.instagram_business_account.id}/media",
                JSONObject("{\"image_url\":\"https://fastly.4sqi.net/img/general/200x200/zNv7bbjkQQNgGArHKtAeb0O9BMJ1zdFrE-EpUx3-YU0.jpg\"}")){ it ->
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
    }

    fun publishInstagramContent(accountId: String , containerId: String, access_token: AccessToken?){
        val request = GraphRequest.newPostRequest(
            access_token,
            "/${accountId}/media_publish",
            JSONObject("{\"creation_id\":\"${containerId}\"}")){
            Toast.makeText(context, "Content Uploaded", Toast.LENGTH_SHORT).show()
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
