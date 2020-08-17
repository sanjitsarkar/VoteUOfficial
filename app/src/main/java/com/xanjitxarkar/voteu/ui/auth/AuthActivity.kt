package com.xanjitxarkar.voteu.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.databinding.ActivityAuthBinding
import com.xanjitxarkar.voteu.ui.home.HomeActivity
import com.xanjitxarkar.voteu.ui.student_details.StudentInfoActivity
import com.xanjitxarkar.voteu.utils.AuthState
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.hide
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import java.lang.Exception
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AuthActivity : AppCompatActivity() {

     val TAG = "AuthActivity"
    val REQUEST_CODE_SIGN_IN = 0
    private lateinit var binding:ActivityAuthBinding
    private val viewModel:AuthViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth)
        binding.viewModel = viewModel

        subscribeObservers()
        binding.signIn.setOnClickListener()
        {

            it.hide()

            auth_loader.show()

            val signInClient = viewModel.signIn(this,getString(R.string.client_id))

            signInClient?.signInIntent.also {
               startActivityForResult(it, REQUEST_CODE_SIGN_IN )
            }
        }

            Log.d(TAG,"hello")




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_SIGN_IN )

        {
            CoroutineScope(IO).launch {

            viewModel.addToFirebaseAuth(data!!)

        }



        }
    }

    private fun subscribeObservers()
    {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {

                    auth_loader.show()
                    Log.d(TAG,"loading")
                }
                is DataState.Success<Boolean> -> {

                    Log.d(TAG,"logged")
                    if(dataState.data)
                    {
                        auth_loader.gone()

                        Intent(this,HomeActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    else
                    {

                        Intent(this,StudentInfoActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    auth_loader.gone()

                }
                is DataState.Error -> {

                    Log.d(TAG,"${dataState.exception.message.toString()}")
                    auth_loader.gone()
                }
                else -> toast("Nothing Bro")
            }

        })
    }

}

