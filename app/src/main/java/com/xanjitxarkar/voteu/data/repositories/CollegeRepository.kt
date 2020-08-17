package com.xanjitxarkar.voteu.data.repositories

import android.util.Log
import com.xanjitxarkar.voteu.data.models.College
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.collegeCollection
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class CollegeRepository
@Inject
    constructor() {
    val TAG = "CollegeRepository"
    suspend fun getCollegeId(search: String): String? {
        try {
            val colleges = collegeCollection.whereEqualTo("name", search).get().await()
            Log.d(TAG, colleges.documents.size.toString())
            if (colleges.documents.size == 0) {
//                emit(DataState.NoData("No College ID available"))
                return ""
                Log.d(TAG, "college id not available")
            }

            return colleges.documents[0].id
        } catch (e: Exception) {
            return ""
        }


    }

    suspend fun getElectionIdByCollegeId(collegeId: String): String?
    {
        return try {

            val college = collegeCollection.document(collegeId).get().await()
            college.get("election_id").toString()
        } catch (e: Exception) {

            Log.d("AppDebug",e.message.toString())
            null
        }


        }

    suspend fun getElectionId(search: String): String? {
        return try {
            val colleges = collegeCollection.whereEqualTo("name", search).get().await()
//            Log.d(TAG, colleges.documents.size.toString())
            if (colleges.documents.size == 0) {
                return null
            }
            colleges.documents[0].data?.get("election_id") as String
        } catch (e: Exception) {
            Log.d("AppDebug",e.message.toString())
            return null
        }


    }

    suspend fun getCollegeDetails(search: String): College? {
        return try {

            val colleges = collegeCollection.whereEqualTo("name", search).get().await()
            if (colleges.documents.size == 0) {
                Log.d("AppDebug","College details not available")
                return null
            }
            Log.d(TAG, colleges.documents.size.toString())
            val college = colleges.documents[0].data
            Log.d(TAG, college.toString())


                    College(
                        college?.get(
                            "election_id"
                        ) as String,
                        college?.get("college_code") as String,
                        college?.get("college_type") as String,
                        college?.get("name") as String,
                        college?.get("post_ids") as MutableList<String>,
                        college?.get("total_students") as Long
                    )


        } catch (e: Exception) {
            Log.d("AppDebug",e.message.toString())
            return null
        }

//        return College(college as String,college.get("election_code") as String,college.get("name") as String,college.get("total_students") as Int,college.get("college_type") as String)


    }

    suspend fun getCollegeDetailsByCollegeId(collegeId: String): College?  {
       return try {

            val college = collegeCollection.document(collegeId).get().await()


                        College(
                            college?.get(
                                "election_id"
                            ) as String,
                            college?.get("college_code") as String,
                            college?.get("college_type") as String,
                            college?.get("name") as String,
                            college?.get("post_ids") as MutableList<String>,
                            college?.get("total_students") as Long
                        )



        } catch (e: Exception) {
           Log.d("AppDebug",e.message.toString())
            return null
        }


//        return College(college as String,college.get("election_code") as String,college.get("name") as String,college.get("total_students") as Int,college.get("college_type") as String)


    }

    suspend fun isEmailMatched(
        rollNo: String,
        email: String,
        collegeId: String
    ): Boolean {
        try {
            Log.d(TAG, rollNo.toString())
            val students =
                FirebaseCollection.collegeCollection.document(collegeId).collection("Students").whereEqualTo("email", email).get().await()
            if(students.documents[0].id==rollNo)
            {
               return true
            }
            return false
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            return false

        }
    }
}