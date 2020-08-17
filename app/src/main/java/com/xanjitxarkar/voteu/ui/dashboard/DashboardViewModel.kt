package com.xanjitxarkar.voteu.ui.dashboard

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.Election
import com.xanjitxarkar.voteu.data.repositories.CandidateRepository
import com.xanjitxarkar.voteu.data.repositories.ElectionRepository
import com.xanjitxarkar.voteu.data.repositories.PostRepository
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DashboardViewModel @ViewModelInject
constructor(

    private val studentRepo: StudentRepository,
    private val electionRepo: ElectionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val votedLiveData:MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val electionLiveData:MutableLiveData<DataState<Election>> = MutableLiveData()

suspend fun electionInfo(electionId:String)
{ viewModelScope.launch {
    electionRepo.getElectionInfo(electionId).onEach { dataState -> electionLiveData.value = dataState }.launchIn(viewModelScope)
}
}


    suspend fun voted()
    {
        viewModelScope.launch {
            studentRepo.voted().onEach { dataState -> votedLiveData.value = dataState }.launchIn(viewModelScope)
        }
    }
}