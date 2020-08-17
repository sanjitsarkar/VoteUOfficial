package com.xanjitxarkar.voteu.ui.splash_screen

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.AuthState
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
@ExperimentalCoroutinesApi
class SplashScreenViewModel
    @ViewModelInject
    constructor(
        private val authRepository: AuthRepository,
        private val studentRepository: StudentRepository,
        @Assisted private val savedStateHandle: SavedStateHandle
    ) : ViewModel() {
    private val _dataState: MutableLiveData<Boolean> = MutableLiveData()
    val dataState: LiveData<Boolean>
        get() = _dataState
    private val _authState:MutableLiveData<AuthState<Boolean>> = MutableLiveData()
    val authState:LiveData<AuthState<Boolean>>
        get() = _authState

           fun isLoggedInUser()
           {
               viewModelScope.launch {
                 authRepository.isLoggedIn().onEach { authState ->
                     run {
                         _authState.value = authState
                     }


               }.launchIn(viewModelScope)

        }

    }

    fun isStudentDetailsAvailable()
    {
        viewModelScope.launch {

          val isExist = studentRepository.isStudentDetailsExist(getUid())
            _dataState.value = isExist

            }

        }

    }




