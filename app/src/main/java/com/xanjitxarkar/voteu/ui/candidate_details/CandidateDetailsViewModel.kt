package com.xanjitxarkar.voteu.ui.candidate_details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.CandidateInfo
import com.xanjitxarkar.voteu.data.models.RollNo
import com.xanjitxarkar.voteu.data.repositories.*
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CandidateDetailsViewModel @ViewModelInject
constructor(

    private val candidateRepo: CandidateRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val candidateData:MutableLiveData<DataState<CandidateInfo>> = MutableLiveData()
@InternalCoroutinesApi
suspend fun getCandidateDetails(rollNo: String, collegeId:String)
{   viewModelScope.launch {
    candidateRepo.getCandidateInfo(rollNo, collegeId).onEach { dataState ->
        candidateData.value = dataState
    }.launchIn(viewModelScope)
}
}

}