package com.testapp.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {

    var accessToken: AccessToken? = null
    var callbackManager: CallbackManager? = null

    val fbCallback = object: FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            accessToken = result?.accessToken
            textView.text = "Logged in with Facebook ${result?.accessToken}"
        }

        override fun onCancel() {

        }

        override fun onError(error: FacebookException?) {
            Log.d("MSG", "error ${error.toString()}")
        }

    }

    val vkCallback = object: VKAuthCallback {
        override fun onLogin(token: VKAccessToken) {
            textView.text = "Logged in with VK ${token.accessToken}"
        }

        override fun onLoginFailed(errorCode: Int) {
            Log.d("MSG", "error2 ${errorCode}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vkLoginBtn.setOnClickListener {
            LoginManager.getInstance().logOut()
            VK.login(this, arrayListOf(VKScope.WALL))
        }

        callbackManager = CallbackManager.Factory.create()

        facebookLoginBtn.setOnClickListener {
            VK.logout()
            facebookLoginBtn.registerCallback(callbackManager, fbCallback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, vkCallback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}