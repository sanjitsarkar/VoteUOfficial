package com.xanjitxarkar.voteu.ui.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.databinding.ActivityPostsBinding
import com.xanjitxarkar.voteu.data.models.ElectionInfo
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.data.models.Student
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.CollegeRepository
import com.xanjitxarkar.voteu.data.repositories.PostRepository
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
//import com.xanjitxarkar.voteu.ui.candidate.CandidatesActivity
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show

import kotlinx.android.synthetic.main.activity_posts.*

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class PostsActivity : AppCompatActivity() {
    val TAG = "PostsActivity"
lateinit var binding:ActivityPostsBinding
    lateinit var postIds: MutableList<String>
    private val adapter by lazy {
        PostsAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
         var collegeId:String = ""
        var electionId:String = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
//val postRepo = PostRepository()
//        val studentRepo =
//            StudentRepository()
//        posts_loader.show()
//
//
//        postsRecyclerView.adapter = adapter
//        CoroutineScope(IO).launch {
//val authRepo = AuthRepository()
////            val student: Student = studentRepo.getStudentDetailsWithUid(getUid())!!
//            val collegeRepo =
//                CollegeRepository()
////             electionId = collegeRepo.getElectionIdByCollegeId(student.collegeId)!!
//
////            collegeId = student.collegeId
////            postIds = postRepo.getPostIds(collegeId)!!
//
//            val posts:MutableList<Post> = mutableListOf<Post>()
//
//
//                   postIds.forEach { post ->
//                       Log.d(TAG,post.toString())
//                     val postData =  postRepo.getPost(post)
//
////                       posts.add(postData!!)
//
//                   }
//
//
//            withContext(Main)
//            {
//                Log.d(TAG,posts.size.toString())
//                posts_loader.gone()
//
//                adapter.addItems(posts)
//                adapter.listener = {
//                        view,item,position ->
//                    run {
//                        var electionInfo: ElectionInfo =
//                            ElectionInfo(
//                                collegeId,
//                                electionId,
//                                item.id
//                            )
//                        Log.d(TAG,electionInfo.toString())
//                       Intent(this@PostsActivity,
//                           CandidatesActivity::class.java).also {
//                           it.putExtra("electionInfo",electionInfo)
//                           startActivity(it)
//                       }
//                    }
//                }
//            }
//        }
//
//
//
//
//
    }
}