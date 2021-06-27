package study.cse499.socialpostscheduler.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_login.*
import study.cse499.socialpostscheduler.R
import study.cse499.socialpostscheduler.viewmodel.LoginViewModel
import timber.log.Timber


class LoginFragment (): Fragment(R.layout.fragment_login) {
    lateinit var viewModel : LoginViewModel
    lateinit var loginManager: LoginManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        loginButton.setOnClickListener {
            initFacebookLogin()
        }

    }

    private fun initFacebookLogin(){
        loginManager = LoginManager.getInstance()
        val callbackManager = CallbackManager.Factory.create()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Timber.d("success")
            }

            override fun onCancel() {
                Timber.d("cancel")
            }

            override fun onError(exception: FacebookException) {
                Timber.d("onError $exception")
            }
        })

        val loginBehaviour = LoginBehavior.WEB_ONLY
        loginManager.loginBehavior = loginBehaviour

        loginManager.logOut()
        val permissions = listOf(
            "public_profile",
            "email",
            "pages_manage_posts")
        loginManager.logIn(activity, permissions)
    }
}