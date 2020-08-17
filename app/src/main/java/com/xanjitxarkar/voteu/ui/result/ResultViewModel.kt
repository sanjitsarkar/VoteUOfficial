package com.xanjitxarkar.voteu.ui.result

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.Election
import com.xanjitxarkar.voteu.data.models.WinnerInfo
import com.xanjitxarkar.voteu.data.repositories.*
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ResultViewModel @ViewModelInject
constructor(


    private val winnerRepo: WinnerRepository,
    private val electionRepo: ElectionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val winnersLiveData: MutableLiveData<DataState<MutableList<WinnerInfo>>> = MutableLiveData()
    var electionLiveData: MutableLiveData<DataState<Election>> = MutableLiveData()
    suspend fun electionInfo(electionId:String)
    {
        viewModelScope.launch {
            electionRepo.getElectionInfo(electionId).onEach { dataState -> electionLiveData.value = dataState  }.launchIn(viewModelScope)
        }
    }
    suspend fun getWinnersInfo(electionId:String,collegeId:String)
    {
        viewModelScope.launch {
            winnerRepo.getWinners(electionId,collegeId).onEach { dataState -> winnersLiveData.value = dataState  }.launchIn(viewModelScope)
        }
    }
}