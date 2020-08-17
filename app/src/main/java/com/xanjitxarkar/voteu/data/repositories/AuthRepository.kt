package com.xanjitxarkar.voteu.data.repositories

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.User
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.auth
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.firebaseAuth
import com.xanjitxarkar.voteu.ui.home.HomeActivity
import com.xanjitxarkar.voteu.ui.student_details.StudentInfoActivity
import com.xanjitxarkar.voteu.utils.AuthState
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.internal.InjectedFieldSignature
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AuthRepository
@Inject
constructor(


) {


    suspend fun googleAuthForFirebase(
        account: GoogleSignInAccount,
        studentRepo: StudentRepository
    ): Flow<DataState<Boolean>> = flow {


        try {
            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
            emit(DataState.Loading)
            auth.signInWithCredential(credentials).await()

            val uid = FirebaseCollection.getUid()
            Log.d("AppDebug", uid)
            if (studentRepo.isStudentDetailsExist(uid)) {
                Log.d("AppDebug", "exist")
                emit(DataState.Success(true))
            } else {
                emit(DataState.Success(false))
            }

        } catch (e: Exception) {
//emit(DataState.Error(e))
            Log.d("AppDebug", e.message.toString())
        }

    }

    @ExperimentalCoroutinesApi
    fun isLoggedIn(): Flow<AuthState<Boolean>> = flow {
        try {


            emit(AuthState.Loading)

            var state: Boolean = firebaseAuth.currentUser != null
            emit(AuthState.Success(state))
        } catch (e: Exception) {
            emit(AuthState.Error(e))
        }
    }

    fun getUser(): User? {
        firebaseAuth.currentUser?.let {

            return User(
                it.displayName!!,
                it.photoUrl.toString()!!,
                it.email!!,
                it.uid!!
            );

        }
        return null
    }
suspend fun logout(context: Context)
{
    AuthUI.getInstance().signOut(context).await()

}
    suspend fun deleteAccount(context: Context)
    {
        AuthUI.getInstance().delete(context).await()

    }

}