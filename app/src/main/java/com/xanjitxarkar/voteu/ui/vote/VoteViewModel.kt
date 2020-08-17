package com.xanjitxarkar.voteu.ui.vote

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.*
import com.xanjitxarkar.voteu.data.repositories.*
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoteViewModel
@ViewModelInject
constructor(

    private val candidateRepo: CandidateRepository,
private val studentRepo: StudentRepository,
    private val postRepo: PostRepository,
    private val electionRepo: ElectionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val position:MutableLiveData<Int> = MutableLiveData()
    val candidatesLiveData:MutableLiveData<DataState<MutableList<CandidateVote>>> = MutableLiveData()
    val postsLiveData:MutableLiveData<DataState<List<Post>>> = MutableLiveData()
    var electionLiveData: MutableLiveData<DataState<Election>> = MutableLiveData()
    suspend fun electionInfo(electionId:String)
    {
        viewModelScope.launch {
            electionRepo.getElectionInfo(electionId).onEach { dataState -> electionLiveData.value = dataState  }.launchIn(viewModelScope)
        }
    }
    suspend fun getPosts(collegeId:String)
    {
        Log.d("AppDebug","Loading voteviewmodel")
        viewModelScope.launch {
            postRepo.getPostsInfo(collegeId).onEach { dataState ->
                postsLiveData.value = dataState
            }.launchIn(viewModelScope)
        }

    }
    suspend fun voteSuccess()
    {
        studentRepo.voteSuccess()
    }
suspend fun vote(postId: String,rollNo: String,electionId: String)
{
    candidateRepo.vote(postId,electionId,rollNo)

}
    suspend fun getVoteCandidates(electionId:String, postId:String, collegeId: String) {
        viewModelScope.launch {

            candidateRepo.getVoteCandidates(electionId, postId, collegeId).onEach { dataState ->
                candidatesLiveData.value = dataState

            }.launchIn(viewModelScope)
        }
    }
}