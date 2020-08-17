package com.xanjitxarkar.voteu.data.repositories

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.CandidateInfo
import com.xanjitxarkar.voteu.data.models.CandidateVote
import com.xanjitxarkar.voteu.data.models.RollNo
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.candidateCollection
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.studentCollection
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CandidateRepository
@Inject
constructor(
    private val studentRepo: StudentRepository,
    private val branchRepo: BranchRepo
) {
    suspend fun getVoteCandidates(
        electionId: String,
        postId: String,
        collegeId: String
    ): Flow<DataState<MutableList<CandidateVote>>> = flow {

        try {
            emit(DataState.Loading)
            val candidateIds = getCandidateIds(electionId, postId)

            var candidatesVote = mutableListOf<CandidateVote>()
            candidateIds?.forEach { id ->
                run {

                    val candidateData = getCandidateData(id, collegeId)
                    candidatesVote.add(
                        CandidateVote(
                            candidateData?.rollNo!!,
                            candidateData.name,
                            candidateData.branchName,
                            candidateData.branchCode,
                            candidateData.semester,
                            candidateData.imgUrl,
                            false,
                            postId
                        )
                    )


                }


            }


            Log.d("AppDebug", candidatesVote.toString())

            if (candidatesVote.size != 0) {
                Log.d("AppDebug", candidatesVote.toString())
                emit(DataState.Success(candidatesVote))

            } else {
                Log.d("AppDebug", "No candidates found")
                emit(DataState.NoData("No candidates found"))
            }


        } catch (e: Exception) {
            Log.d("AppDebug", e.message.toString())

            emit(DataState.Error(e))
        }


    }

    suspend fun getCandidateIds(electionId: String, postId: String): List<String>? {

        try {

            var candidateIds: MutableList<String> = mutableListOf<String>()
            var candidatesData =
                candidateCollection.document(electionId).collection(postId).get().await()

            candidatesData.forEach {
                candidateIds.add(it.id)
            }


            return candidateIds
        } catch (e: Exception) {

            Log.d("AppDebug", e.message.toString())
            return null

        }


    }
suspend fun  vote(postId: String,electionId: String,rollNo: String)
{
studentRepo.updateVoteCount()
    candidateCollection.document(electionId).collection(postId).document(rollNo).update("total_votes",FieldValue.increment(1)).await()
}
    @InternalCoroutinesApi
    suspend fun getCandidates(
        electionId: String,
        postId: String,
        collegeId: String
    ): Flow<DataState<List<Candidate>>> =
        flow {

            try {

                emit(DataState.Loading)
                val candidateIds = getCandidateIds(electionId, postId)
                Log.d("AppDebug", candidateIds.toString())
                var candidates: MutableList<Candidate> = mutableListOf()
                var studentData: RollNo
                candidateIds?.forEach { data ->

                    run {
                        studentData =
                            studentRepo.getStudentDataByRollNodAndCollegeId(data, collegeId)!!
                        Log.d("AppDebug", studentData.name)
                        candidates.add(
                            Candidate(
                                studentData.id,

                                postId,
                                0,
                                studentData.name,
                                ""


                            )
                        )
                    }

                }
                emit(DataState.Success(candidates))

            } catch (e: Exception) {
                Log.d("AppDebug", e.message.toString())
                emit(DataState.Error(e))
            }
        }


    suspend fun getCandidateInfo(
        rollNo: String,
        collegeId: String
    ): Flow<DataState<CandidateInfo>> = flow {

        try {
            emit(DataState.Loading)
            val studentInfo = studentRepo.getStudentDataByRollNodAndCollegeId(rollNo, collegeId)
            val branchInfo = branchRepo.getBranch(studentInfo?.branch_id!!)
            val userInfo = studentRepo.getStudentDetailsByRollNo(rollNo)

            emit(
                DataState.Success(
                    CandidateInfo(
                        rollNo,
                        studentInfo.name,
                        branchInfo?.name!!,
                        branchInfo.code,
                        studentInfo.semester.toString(),
                        userInfo?.imgUrl!!
                    )
                )
            )
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }


    }

    suspend fun getCandidateData(rollNo: String, collegeId: String): CandidateInfo? {

        return try {

            val studentInfo = studentRepo.getStudentDataByRollNodAndCollegeId(rollNo, collegeId)
            val branchInfo = branchRepo.getBranch(studentInfo?.branch_id!!)
            val userInfo = studentRepo.getStudentDetailsByRollNo(rollNo)
            Log.d(
                "AppDebug", CandidateInfo(
                    rollNo,
                    studentInfo.name,
                    branchInfo?.name!!,
                    branchInfo.code,
                    studentInfo.semester.toString(),
                    userInfo?.imgUrl!!
                ).toString()
            )
            CandidateInfo(
                rollNo,
                studentInfo.name,
                branchInfo?.name!!,
                branchInfo.code,
                studentInfo.semester.toString(),
                userInfo?.imgUrl!!
            )
        } catch (e: Exception) {
            Log.d("AppDebug", e.message.toString())
            return null
        }


    }
}