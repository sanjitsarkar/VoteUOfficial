package com.xanjitxarkar.voteu.data.repositories

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.RollNo
import com.xanjitxarkar.voteu.data.models.SearchResult
import com.xanjitxarkar.voteu.data.models.Student
import com.xanjitxarkar.voteu.data.models.StudentInfo
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.collegeCollection
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.studentCollection
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.capitalizeWords
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class StudentRepository
@Inject
constructor(
    private val branchRepo: BranchRepo,
    private val collegeRepo: CollegeRepository,
    private val authRepository: AuthRepository
) {
    var TAG: String = "StudentRepository"


    suspend fun search(search_key:String,collegeId: String):Flow<DataState<MutableList<SearchResult>>> = flow {

       try {
           emit(DataState.Loading)
           val results:MutableList<SearchResult> = mutableListOf()
//           val datas = collegeCollection.document(collegeId).collection("Students").orderBy("name").startAfter(search_key).endAt("$search_key\uf8ff").get().await()

           val datas = collegeCollection.document(collegeId).collection("Students").orderBy("search_key").whereArrayContainsAny("search_key",
               listOf(search_key.toLowerCase())).get().await()
           datas.documents.forEach { data->
               run {
                   Log.d("AppDebug",data.get("name").toString())
                   results.add(SearchResult(data.get("name").toString(),data.get("semester").toString(),data.get("email").toString(),data.id,data.get("branch_id").toString(),data.get("branchCode").toString()))
               }
           }
           if(datas.isEmpty)
           {
               emit(DataState.NoData("No results found..."))
           }
           else
           {
               emit(DataState.Success(results))
           }

       }
       catch (e:Exception)
       {
          emit(DataState.Error(e))
       }


    }

    suspend fun voted():Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.Loading)
            val studentData = studentCollection.document(getUid()).get().await()
            if(studentData.data?.get("voted") as Boolean)
            {
                emit(DataState.Success(true))
            }
            else
            {
                emit(DataState.Success(false))
            }
        }
        catch (e:Exception)
        {
            emit(DataState.Error(e))
        }
    }

    suspend fun voteSuccess()
    {
        studentCollection.document(getUid()).update("voted",true).await()
    }
    suspend fun getStudentDetails(search: String, rollNo: String): RollNo?{
        try {

            val collegeRepo =
                CollegeRepository()

            var collegesId = collegeRepo.getCollegeId(search)


            val details = FirebaseCollection.collegeCollection.document(collegesId!!)
                .collection("Students").document(rollNo!!).get().await()
            if (details.data == null) {
                return null
                Log.d("AppDebug","No data available studentInfo")
            } else {

                return RollNo(
                            details?.get("branch_id") as String,
                            details?.get("email") as String,
                            details?.get("name") as String,
                            details?.get("semester") as Long,
                    collegesId,
                    details.id
                        )


            }


        } catch (e: Exception) {
            Log.d("AppDebug",e.message.toString())
            return null
        }
    }



    suspend fun getStudentDataByRollNodAndCollegeId(
        rollNo: String,
        collegeId: String
    ): RollNo?{

        try {


            val details = FirebaseCollection.collegeCollection.document(collegeId!!)
                .collection("Students").document(rollNo).get().await()



                       return RollNo(
                            details?.get("branch_id") as String,
                            details?.get("email") as String,
                            details?.get("name") as String,
                            details?.get("semester") as Long,
                            collegeId,
                           details.id
                        )


//            } else {
//                emit(DataState.NoData(R.string.student_details_not_available.toString()))
//            }
        } catch (e: Exception) {
            Log.d("AppDebu2g",e.message.toString())
            return null
        }


    }
    suspend fun getStudentDetailsByRollNo(rollNo: String):Student?
    {
        return try {
            val details = studentCollection.whereEqualTo("rollNo",rollNo).get().await()
            Student(details.documents[0].get("rollNo") as String,details.documents[0].get("collegeId") as String,details.documents[0].get("voted") as Boolean,details.documents[0].get("voteCount") as Long,details.documents[0].get("imgUrl") as String)
        }
        catch (e:Exception)
        {
            null
        }

    }
    suspend fun getStudentDetailsByRollNodAndCollegeId(
        rollNo: String,
        collegeId: String
    ): Flow<DataState<RollNo>>? = flow {
        emit(DataState.Loading)
        try {


            val details = FirebaseCollection.collegeCollection.document(collegeId!!)
                .collection("Students").document(rollNo).get().await()


            emit(
                DataState.Success(
                    RollNo(
                        details?.get("branch_id") as String,
                        details?.get("email") as String,
                        details?.get("name") as String,
                        details?.get("semester") as Long,
                        collegeId,
                        details.id
                    )
                )
            )
//            } else {
//                emit(DataState.NoData(R.string.student_details_not_available.toString()))
//            }
        } catch (e: Exception) {
            Log.d("AppDebu2g",e.message.toString())
            emit(DataState.Error(e))
        }


    }
    suspend fun addStudentDetails(student: Student, uid: String): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading)
            val success = studentCollection.document(uid!!).set(student!!).await()
            emit(DataState.Success(true))
            Log.d(TAG, "added")
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }


    }

    suspend fun isStudentDetailsExist(uid: String): Boolean {
        try {

            val student = studentCollection.document(uid!!).get().await()
            if (student.data != null) {
                return true
            }
            return false
        } catch (e: Exception) {
            return false
            Log.d(TAG, e.message.toString())
        }


    }






    suspend fun getStudentDetailsWithUid(uid: String): Student? {
       return try {
            val studentData = studentCollection.document(uid!!).get().await()

           val imgUrl = authRepository.getUser()?.photoUrl

                        Student(
                            studentData.get("rollNo") as String,
                            studentData.get("collegeId") as String,
                            studentData.get("voted") as Boolean,
                            studentData.get("voteCount") as Long,
                            imgUrl.toString()

                        )



        } catch (e: Exception) {
            Log.d("AppDebug",e.message.toString())
           null
        }

    }

suspend fun updateVoteCount()
{
    studentCollection.document(getUid()).update("voteCount",FieldValue.increment(1)).await()

}

    suspend fun getStudentInfo(collegeSearch:String,rollNo:String): Flow<DataState<StudentInfo>>
            = flow{
        Log.d("AppDebug","hello")
        if(TextUtils.isEmpty(collegeSearch) || TextUtils.isEmpty(rollNo))
        {
//                toast("Please input something")
//                collegeName.requestFocus()
            Log.d("AppDebug","Please input something")
            emit(DataState.Info("Please input something"))

        }
        else
        {

            Log.d("AppDebug","${collegeSearch} ${rollNo}")




                emit(DataState.Loading)
//                val uid = FirebaseCollection.getUid()
                val userInfo = authRepository.getUser()
//            emit(DataState.Info(userInfo?.email!!))


//Log.d("AppDebug",collegeSearch.capitalize())

            val data = getStudentDetails(
                collegeSearch.toLowerCase().capitalizeWords(),
                rollNo.toString()
            )

            if (data == null) {
                emit(DataState.Info("Roll no or College name is invalid"))
            } else {

                val result = collegeRepo.isEmailMatched(rollNo,userInfo?.email!!,data?.collegeId!!)
//            emit(DataState.Info(result.toString()))
                Log.d(TAG,"Result ${result.toString()}")
                if(!result)
                {
                    emit(DataState.Info("Email is not registered in College with this roll no"))
                }
                else {


//                            val collegeIdData = collegeRepo.getCollegeId(collegeSearch.toString())


//                                collegeId = collegeIdData


//                            }


                        val rollNoInfo:RollNo = data
                        val branchId = rollNoInfo.branch_id
                        val branchData = branchRepo.getBranch(branchId)
                        if (branchData == null) {
                            emit(DataState.Info("No Branch available"))
                        }
                    val imgUrl = authRepository.getUser()?.photoUrl
                        emit(DataState.Success(StudentInfo(rollNo.toString(),rollNoInfo.name,branchData?.name!!,rollNoInfo.semester.toString(),data.collegeId,imgUrl.toString())))
//
                    }
                }
            }

    }
}