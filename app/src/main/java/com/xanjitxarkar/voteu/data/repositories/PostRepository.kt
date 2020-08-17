package com.xanjitxarkar.voteu.data.repositories


import android.util.Log
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.collegeCollection
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.postCollection
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class PostRepository
@Inject
constructor() {
    val TAG = "PostRepository"

    suspend fun getPostIds(collegeId: String): List<String>? {

        return try {

            val collegeDetails = collegeCollection.document(collegeId).get().await()
            val postIds = collegeDetails.get("post_ids")
            postIds as List<String>


        } catch (e: Exception) {
            Log.d("AppDebug", e.message.toString())
            null

        }
    }


    suspend fun getPost(postId: String): Post? {

        try {
            val postDetails =
                postCollection.document(postId!!).get().await()


            return Post(
                postDetails.get("code") as String, postDetails.get("name") as String,
                postDetails.id
            ) as Post


        } catch (e: Exception) {

            Log.d("AppDebug", "getPost ${e.message.toString()}")
            return null
        }
    }



     fun getPosts(collegeId: String): Flow<DataState<List<Post>>>? =  flow{

         try {

           var posts:MutableList<Post> = mutableListOf()
val postIds = getPostIds(collegeId)
             postIds?.forEach { postId ->

                     run {
                         var post = getPost(postId)
                         Log.d("AppDebug",post.toString())


                         posts.add(post!!)
                     }
                 emit(DataState.Loading)

             }
             if(posts.size==0)
             {
                 emit(DataState.NoData("No Posts Available"))
             }

             emit(DataState.Success(posts))

        } catch (e: Exception) {
             emit(DataState.Error(e))
            Log.d("AppDebug", e.message.toString())

        }


    }

    suspend fun getPostsInfo(collegeId: String):Flow<DataState<List<Post>>> = flow {
        try {
            emit(DataState.Loading)
            val postIds = getPostIds(collegeId)
            Log.d("AppDebug",postIds.toString())

            var posts = mutableListOf<Post>()
            postIds?.forEach { postId ->
                run {
                    val post = getPost(postId)
                    posts.add(post!!)
                }
            }
            if (posts.size != 0) {
                Log.d("AppDebug",posts.size.toString())

                emit(DataState.Success(posts))
            } else {
                Log.d("AppDebug","No Post Available")
                emit(DataState.NoData("No Post Available"))
            }
        }
        catch (e:Exception)
        {
            Log.d("AppDebug",e.message.toString())
            emit(DataState.Error(e))
        }
    }


}
