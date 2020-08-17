package com.xanjitxarkar.voteu.ui.student_details

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.InverseMethod
import androidx.databinding.Observable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.xanjitxarkar.voteu.data.models.RollNo
import com.xanjitxarkar.voteu.data.models.Student
import com.xanjitxarkar.voteu.data.models.StudentInfo
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.BranchRepo
import com.xanjitxarkar.voteu.data.repositories.CollegeRepository
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi

class StudentDetailsViewModel
@ViewModelInject
    constructor(
    private val studentRepo: StudentRepository,
    private val authRepo: AuthRepository,

    @Assisted private val savedStateHandle: SavedStateHandle
    )
    : ViewModel(),Observable {
val TAG = "AppDebug"
     var studentData: MutableLiveData<StudentInfo> = MutableLiveData()
    var studentInfo: MutableLiveData<Student> = MutableLiveData()
    private var _studentInfoData: MutableLiveData<DataState<StudentInfo>> = MutableLiveData()
    val studentInfoData: LiveData<DataState<StudentInfo>>
        get() = _studentInfoData
    private var _studentInfoAdded: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val studentInfoAdded: LiveData<DataState<Boolean>>
        get() = _studentInfoAdded
    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

//    @Bindable
//     var collegeSearch: MutableLiveData<String> = MutableLiveData<String>()
//
//    @Bindable
//     var rollNo: MutableLiveData<String> = MutableLiveData<String>()

suspend fun getStudentInfo(collegeSearch:String,rollNo:String)
{
    Log.d(TAG,"$collegeSearch $rollNo")
    viewModelScope.launch {
        studentRepo.getStudentInfo(collegeSearch,rollNo).onEach {
            dataState ->  _studentInfoData.value = dataState


        }.launchIn(viewModelScope)
    }
}

    suspend fun addStudentInfo()
    {

        viewModelScope.launch {
            studentRepo.addStudentDetails(studentInfo.value!!, getUid()).onEach {
                    dataState ->  _studentInfoAdded.value = dataState


            }.launchIn(viewModelScope)
        }
    }
suspend fun logout(context: Context)
{

        authRepo.logout(context)
//    authRepo.deleteAccount(context)
    isLoggedIn.value = true

}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}