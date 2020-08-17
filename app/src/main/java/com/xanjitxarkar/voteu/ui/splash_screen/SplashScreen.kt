package com.xanjitxarkar.voteu.ui.splash_screen

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.ui.auth.AuthActivity
import com.xanjitxarkar.voteu.ui.home.HomeActivity
import com.xanjitxarkar.voteu.ui.student_details.StudentInfoActivity
import com.xanjitxarkar.voteu.utils.AuthState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private val TAG: String = "AppDebug"
    private val viewModel: SplashScreenViewModel by viewModels<SplashScreenViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        subscribeObservers()
        viewModel.isLoggedInUser()


    }

    private fun subscribeObservers() {
        viewModel.authState.observe(this, Observer { authState ->
            when (authState) {
                is AuthState.Loading -> {

                    Log.d(TAG,"loading")
                }
                is AuthState.Success<Boolean> -> {
                    Log.d(TAG,"logged ${authState.user}")

                    navigateTo(authState.user)
                }
                is AuthState.Error -> {
                    Log.d(TAG,"logged ${authState.exception.message.toString()}")

                }
            }

        })
    }

    fun displayError(message: String?) {
        if (message != null) {
            toast(message)
        } else {
            toast("Unknown Error")
        }
    }



    private fun navigateTo(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            viewModel.isStudentDetailsAvailable()
            viewModel.dataState.observe(this, Observer { it ->
                if (it)
                {
                    Intent(this, HomeActivity::class.java).also {intent->
                        startActivity(intent)
                        finish()
                    }
                }
                else
                {
                    Intent(this, StudentInfoActivity::class.java).also { intent->
                        startActivity(intent)
                        finish()
                    }
                }
            })

        } else {
            Intent(this, AuthActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }


}