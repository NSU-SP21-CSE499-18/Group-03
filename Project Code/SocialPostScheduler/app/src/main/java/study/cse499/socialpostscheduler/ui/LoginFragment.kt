package study.cse499.socialpostscheduler.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    lateinit var callbackManager: CallbackManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        loginManager = LoginManager.getInstance()
        val loginBehaviour = LoginBehavior.WEB_ONLY
        loginManager.loginBehavior = loginBehaviour
        callbackManager = CallbackManager.Factory.create()

        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
            }

            override fun onCancel() {
            }

            override fun onError(exception: FacebookException) {
            }
        })

        loginButton.setOnClickListener {
            initFacebookLogin()
        }

    }

    private fun initFacebookLogin(){
        val permissions = listOf(
            "public_profile",
            "email")
        loginManager.logIn(this, permissions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

}