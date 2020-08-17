package com.xanjitxarkar.voteu.ui.candidate

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.data.repositories.*
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CandidatesViewModel @ViewModelInject
constructor(
    private val authRepo: AuthRepository,
    private val collegeRepo: CollegeRepository,
    private val candidateRepo: CandidateRepository,
    private val studentRepo: StudentRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val candidatesData: MutableLiveData<DataState<List<Candidate>>> = MutableLiveData()
    val candidateData:MutableLiveData<Candidate> = MutableLiveData()

    @InternalCoroutinesApi
    suspend fun getCandidates(electionId: String, postId: String, collegeId: String) {
        viewModelScope.launch {
            candidateRepo.getCandidates(electionId, postId, collegeId)?.onEach {
                candidatesData.value = it

            }.launchIn(viewModelScope)
        }




    }
}
