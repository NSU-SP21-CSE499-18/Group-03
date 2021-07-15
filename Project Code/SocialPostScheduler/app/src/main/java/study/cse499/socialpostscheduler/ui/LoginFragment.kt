package study.cse499.socialpostscheduler.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.other.facebook_page.FacebookPageList
import study.cse499.socialpostscheduler.viewmodel.LoginViewModel
import timber.log.Timber


class LoginFragment (): Fragment(R.layout.fragment_login) {
    lateinit var viewModel : LoginViewModel
    lateinit var loginManager: LoginManager
    lateinit var callbackManager: CallbackManager
    var facebookLogin: Boolean = false;
    var instagramLogin: Boolean = false;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        loginManager = LoginManager.getInstance()
        val loginBehaviour = LoginBehavior.WEB_ONLY
        loginManager.loginBehavior = loginBehaviour
        callbackManager = CallbackManager.Factory.create()

        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                loginResult?.let {
                    if(facebookLogin){
                        val request = GraphRequest.newGraphPathRequest(
                            loginResult.accessToken,
                            "/${loginResult.accessToken.userId}/accounts") {
                            it.rawResponse?.let { response ->
                                val action  = LoginFragmentDirections.actionLoginFragmentToScheduleFragment(response)
                                findNavController().navigate(action)
                            }

                        }
                        request.executeAsync()
                    }
                    if(instagramLogin){
                        val request = GraphRequest.newGraphPathRequest(
                            loginResult.accessToken,
                            "/${loginResult.accessToken.userId}/accounts") { it ->
                            it.rawResponse?.let { response ->
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
                                val instagramRequest = GraphRequest.newGraphPathRequest(
                                    accessTokenPage,
                                    "/${objectData.id}") { instagramResponse ->
                                    instagramResponse.rawResponse?.let { instagramData ->
                                        val action  = LoginFragmentDirections.actionLoginFragmentToScheduleFragment(instagramData)
                                        findNavController().navigate(action)
                                    }
                                }
                                val parameters = Bundle()
                                parameters.putString("fields", "instagram_business_account")
                                instagramRequest.parameters = parameters
                                instagramRequest.executeAsync()

                            }

                        }
                        request.executeAsync()
                    }



                }
            }

            override fun onCancel() {
                Timber.d("cancel")
            }

            override fun onError(exception: FacebookException) {
                Timber.d("onError $exception")
            }
        })

        loginButton.setOnClickListener {
            instagramLogin = false;
            facebookLogin = true;
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with (sharedPref.edit()) {
                putBoolean("isInstagram", instagramLogin)
                putBoolean("isFacebook", facebookLogin)
                apply()
            }
            initFacebookLogin()
        }

        loginInstagram.setOnClickListener {
            facebookLogin = false;
            instagramLogin = true;
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with (sharedPref.edit()) {
                putBoolean("isInstagram", instagramLogin)
                putBoolean("isFacebook", facebookLogin)
                apply()
            }
            initInstagramLogin()
        }

    }

    private fun initFacebookLogin(){
        val permissions = listOf(
            "public_profile",
            "email",
            "pages_manage_posts",
            "pages_show_list")
        loginManager.logIn(this, permissions)
    }

    private fun initInstagramLogin(){
        val permissions = listOf(
            "public_profile",
            "email",
            "instagram_basic",
            "pages_manage_posts",
            "pages_show_list")
        loginManager.logIn(this, permissions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

}