package study.cse499.facebookapitest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class MainActivity : AppCompatActivity() {
    private val EMAIL = "email"
    val callbackManager = CallbackManager.Factory.create();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        val loginButton = findViewById<LoginButton>(R.id.login_button);
        val loginText = findViewById<TextView>(R.id.textView);
        val btLogout = findViewById<Button>(R.id.button2);

        loginButton.setPermissions(listOf(EMAIL))


        if(isLoggedIn){
            loginText.visibility = View.VISIBLE
            btLogout.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
            loginText.text = "Logged in: "+ accessToken?.userId
        }

        btLogout.setOnClickListener {
            LoginManager.getInstance().logOut();
            loginText.visibility = View.GONE
            btLogout.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
        }
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Toast.makeText(this@MainActivity, "On Success", Toast.LENGTH_SHORT).show()
                loginText.visibility = View.VISIBLE
                btLogout.visibility = View.VISIBLE
                loginButton.visibility = View.GONE
                loginText.text = "Logged in: "+ loginResult?.accessToken?.userId
            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "On Cancel", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@MainActivity, "On Error", Toast.LENGTH_SHORT).show()
            }
        })

//        LoginManager.getInstance().registerCallback(callbackManager,
//            object : FacebookCallback<LoginResult?> {
//                override fun onSuccess(loginResult: LoginResult?) {
//                    Toast.makeText(applicationContext, "On Success", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onCancel() {
//                    Toast.makeText(applicationContext, "On Cancel", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onError(exception: FacebookException) {
//                    Toast.makeText(applicationContext, "On Error", Toast.LENGTH_SHORT).show()
//                }
//            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}