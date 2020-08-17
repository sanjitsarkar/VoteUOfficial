package com.xanjitxarkar.voteu.data.repositories

import android.util.Log
import com.xanjitxarkar.voteu.data.models.WinnerInfo
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.winnerCollection
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class WinnerRepository
@Inject
constructor(
    private val studentRepo: StudentRepository,
    private val postRepo: PostRepository,
    private val branchRepo: BranchRepo
){

    suspend fun getWinners(electionId:String,collegeId:String): Flow<DataState<MutableList<WinnerInfo>>> = flow{
        try {
            emit(DataState.Loading)
            var winnerInfo:MutableList<WinnerInfo> = mutableListOf()
            val winners = winnerCollection.document(electionId).get().await()
            var postIds: MutableSet<String>
//            var winnerInfo = mutableListOf<Any>()
//            val winnerInfos:MutableList<MutableList<Any>> = listOf<Any>() as MutableList<MutableList<Any>>
            postIds = winners.data!!.keys

            postIds.forEach { post->
                run {

val info:List<Any> = winners.get(post) as List<Any>
                    val postInfo = postRepo.getPost(post)
                 val studentInfo = studentRepo.getStudentDataByRollNodAndCollegeId(info[0].toString(),collegeId)
                    val studentData = studentRepo.getStudentDetailsByRollNo(info[0].toString())
                    val branchInfo = branchRepo.getBranch(studentInfo?.branch_id!!)
                    Log.d("AppDebug",WinnerInfo(studentInfo.name,postInfo?.name!!,postInfo?.code!!,
                        info[1].toString(),studentData?.imgUrl!!,studentInfo.semester.toString(),branchInfo?.code!!,branchInfo.name
                    ).toString())
                    winnerInfo.add(WinnerInfo(studentInfo.name,postInfo?.name!!,postInfo?.code!!,
                        info[1].toString(),studentData?.imgUrl!!,studentInfo.semester.toString(),branchInfo?.code!!,branchInfo.name
                    ))

//                    winnerInfos.add(winnerInfo)
                }
            }
            emit(DataState.Success(winnerInfo))

//            for (i in 0 until winners.data?.size!!) {
//                Log.d("AppDebug","${winners.data!!.keys.toString()}")
//            }


        }
        catch (e:Exception)
        {
            Log.d("AppDebug",e.message.toString())
emit(DataState.Error(e))
        }
    }


}
