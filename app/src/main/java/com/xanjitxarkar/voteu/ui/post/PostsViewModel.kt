package com.xanjitxarkar.voteu.ui.post

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.CollegeRepository
import com.xanjitxarkar.voteu.data.repositories.PostRepository
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception

class PostsViewModel @ViewModelInject
constructor(
    private val authRepo: AuthRepository,
    private val collegeRepo: CollegeRepository,
    private val postRepo: PostRepository,
    private val studentRepo: StudentRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
 val postsData:MutableLiveData<DataState<List<Post>?>> = MutableLiveData()
    var postInfo: MutableLiveData<Post> = MutableLiveData<Post>()

     fun getPosts(collegeId:String){
    viewModelScope.launch {
        postRepo.getPosts(collegeId)?.onEach {dataState->
          postsData.value = dataState
        }?.launchIn(viewModelScope)
    }


}
}