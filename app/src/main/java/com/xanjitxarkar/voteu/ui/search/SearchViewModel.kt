package com.xanjitxarkar.voteu.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.SearchResult
import com.xanjitxarkar.voteu.data.repositories.CandidateRepository
import com.xanjitxarkar.voteu.data.repositories.ElectionRepository
import com.xanjitxarkar.voteu.data.repositories.PostRepository
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject
constructor(
    private val studentRepo: StudentRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val searchResultsLiveData:MutableLiveData<DataState<MutableList<SearchResult>>> = MutableLiveData()

    suspend fun search(search_key:String,collegeId:String)
    {  viewModelScope.launch {
        studentRepo.search(search_key, collegeId)
            .onEach { dataState -> searchResultsLiveData.value = dataState }.launchIn(viewModelScope)
    }
    }
}