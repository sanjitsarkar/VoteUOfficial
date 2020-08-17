package com.xanjitxarkar.voteu.ui.profile

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.xanjitxarkar.voteu.data.models.ToolBarData
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.CollegeRepository
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.AuthState
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel
@ViewModelInject
constructor(
    private val authRepo: AuthRepository,
    private val studentRepo: StudentRepository,
    private val collegeRepo: CollegeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val _toolbarDetails: MutableLiveData<ToolBarData> = MutableLiveData<ToolBarData>()
    val toolbarDetails: LiveData<ToolBarData>
        get() = _toolbarDetails
    val _info: MutableLiveData<String> = MutableLiveData<String>()
    val info: LiveData<String>
        get() = _info
    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isVoted: MutableLiveData<DataState<Boolean>> = MutableLiveData()
suspend fun isVoted() {
    viewModelScope.launch {
        studentRepo.isVoted().collect { dataState ->
            run {
                isVoted.value = dataState
            }
        }
    }
}
    fun isLoggedIn()
    {
        authRepo.isLoggedIn().onEach { authState ->
            when(authState)
            {
                is AuthState.Success->
                {
                    isLoggedIn.value = authState.user
                }
            }
        }
    }
 fun profileInfo(collegeId:String)
{
Log.d("AppDebug","Hello $collegeId")
    _info.value = "Hello $collegeId"
}
    suspend fun logout(context:Context)
    {

authRepo.logout(context)

            isLoggedIn.value = true
        


    }
}