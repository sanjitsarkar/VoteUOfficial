package com.xanjitxarkar.voteu.ui.home

import android.util.Log
import android.view.View
import androidx.databinding.Observable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.messaging.FirebaseMessaging
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.ToolBarData
import com.xanjitxarkar.voteu.data.repositories.*
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.ui.candidate.CandidateListener
import com.xanjitxarkar.voteu.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


class HomeViewModel
@ViewModelInject
constructor(
    private val authRepo: AuthRepository,
    private val studentRepo: StudentRepository,
    private val collegeRepo: CollegeRepository,
    private val branchRepo: BranchRepo,
    private val electionRepo: ElectionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
)
    : ViewModel(), Observable {

val TAG = "AppDebug"
    private val _toolbarDetails:MutableLiveData<ToolBarData> = MutableLiveData<ToolBarData>()
    val toolbarDetails: LiveData<ToolBarData>
            get() = _toolbarDetails
    val _info: MutableLiveData<String> = MutableLiveData<String>()
//    init {
//        viewModelScope.launch {
//            getToolbarDetails()
//        }
//    }
    val info: LiveData<String>
        get() = _info


 fun subscribeToElectionNotification(collegeId:String)
{
    FirebaseMessaging.getInstance().subscribeToTopic(collegeId)
        .addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscription Failed"
            }
            Log.d(TAG, msg+collegeId)

        }
}
suspend fun getToolbarDetails()
{
    viewModelScope.launch {
_info.value = "Hello"
        var rollNo = ""
        var branchCode = ""
        var branchName = ""
        var branchId = ""
        var semester = ""
        var name = ""
        var electionDate: Timestamp? = null
        val studentData = studentRepo.getStudentDetailsWithUid(getUid())
        Log.d(TAG,"uid ${getUid()}")
        var collegeId = ""

                        collegeId = studentData?.collegeId!!
                        rollNo = studentData.rollNo


                       studentRepo.getStudentDetailsByRollNodAndCollegeId(rollNo,collegeId)?.onEach {
                            dataState ->
                          run {
                               when (dataState) {
                                   is DataState.Success -> {
                                       Log.d(TAG, "Data available 1")
                                       semester = dataState.data.semester.toString()
                                       branchId = dataState.data.branch_id
 name = dataState.data.name
var electionDateStart:Timestamp? = null
var electionDateEnd:Timestamp? = null
                                       val collegeData = collegeRepo.getCollegeDetailsByCollegeId(collegeId)
                                       val user = authRepo.getUser()
                                       Log.d(TAG,"user ${user?.photoUrl}")
                                       val branchInfo =  branchRepo.getBranch(branchId)
                                       branchCode = branchInfo?.code!!
                                       branchName= branchInfo?.name!!
                                       if(collegeData?.electionId=="")
                                       {
                                           electionDate = null
                                           electionDateEnd = null
                                       }
                                       else
                                       {
                                           electionDateStart = electionRepo.getElectionData(collegeData?.electionId!!)?.startTimestamp
                                           electionDateEnd = electionRepo.getElectionData(collegeData?.electionId!!)?.endTimestamp
//                                           electionDateStart = Date(startDate.seconds * 1000).toString()
//                                           electionDateEnd = Date(endDate.seconds * 1000).toString()
                                       }


                                                       _toolbarDetails.value = ToolBarData(name,collegeId,collegeData?.name!!,user?.photoUrl!!,user?.email!!,rollNo,collegeData?.electionId,branchName,branchId,branchCode,electionDateStart,electionDateEnd,semester,studentData.voted)
                                                       Log.d(TAG,_toolbarDetails.value.toString())








                                   }
                                   is DataState.NoData -> {
                                       Log.d(TAG, "No data")
                                   }
                                   is DataState.Error -> {
                                       Log.d(TAG, dataState.exception.message.toString())
                                   }
                                   else -> Log.d(TAG, "Nothing")
                               }
                           }
                        }?.launchIn(viewModelScope)


                    }



    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}