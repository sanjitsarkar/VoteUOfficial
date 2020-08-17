package com.xanjitxarkar.voteu.ui.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.AuthState
import com.xanjitxarkar.voteu.utils.DataState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception



@ExperimentalCoroutinesApi
class AuthViewModel
@ViewModelInject
constructor(
    private val authRepository: AuthRepository,
    private val studentRepo: StudentRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(

) {

    private val _dataState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val dataState: LiveData<DataState<Boolean>>
        get() = _dataState

    fun signIn(activity: Activity, client_id: String): GoogleSignInClient? {
        try {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(client_id)
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(activity, options)

            Log.d("AppDebug", "Hello google")
        } catch (e: Exception) {
            return null
            Log.d("AppDebug", e.message.toString())
        }


    }

    suspend fun addToFirebaseAuth(data: Intent) {
        try
        {val account: GoogleSignInAccount? = GoogleSignIn.getSignedInAccountFromIntent(data).result



            account?.let {
                viewModelScope.launch {
                    authRepository.googleAuthForFirebase(it,studentRepo).onEach { datState ->

                        _dataState.value = datState
                    }.launchIn(viewModelScope)
                }

            }
            Log.d("AppDebug","addtofirebase")
        }
        catch (e:Exception)
        {
            Log.d("AppDebug",e.message.toString())
        }


    }
}