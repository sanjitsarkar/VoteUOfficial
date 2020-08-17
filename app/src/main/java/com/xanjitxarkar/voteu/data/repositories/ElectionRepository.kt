package com.xanjitxarkar.voteu.data.repositories

import android.util.Log
import com.google.firebase.Timestamp
import com.xanjitxarkar.voteu.data.models.Election
import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.electionCollection
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class ElectionRepository @Inject
constructor(){
suspend fun getElectionInfo(electionId:String): Flow<DataState<Election>> = flow {
    try {
        emit(DataState.Loading)
        val election= electionCollection.document(electionId).get().await()
        Log.d("AppDebug",
            Election(election.get("start_timestamp") as Timestamp,election.get("end_timestamp") as Timestamp,election.get("is_cancelled") as Boolean,election.get("is_started") as Boolean,election.get("is_result_declared") as Boolean,election.get("total_votes") as Long).toString()
        )
        emit(DataState.Success(Election(election.get("start_timestamp") as Timestamp,election.get("end_timestamp") as Timestamp,election.get("is_cancelled") as Boolean,election.get("is_started") as Boolean,election.get("is_result_declared") as Boolean,election.get("total_votes") as Long)))

    }
    catch (e:Exception)
    {
        Log.d("AppDebug",e.message.toString())
emit(DataState.Error(e))
    }
}
    suspend fun getElectionData(electionId:String): Election? {
        return try {

            val election= electionCollection.document(electionId).get().await()
            Log.d("AppDebug",
                Election(election.get("start_timestamp") as Timestamp,election.get("end_timestamp") as Timestamp,election.get("is_cancelled") as Boolean,election.get("is_started") as Boolean,election.get("is_result_declared") as Boolean,election.get("total_votes") as Long).toString()
            )
            Election(election.get("start_timestamp") as Timestamp,election.get("end_timestamp") as Timestamp,election.get("is_cancelled") as Boolean,election.get("is_started") as Boolean,election.get("is_result_declared") as Boolean,election.get("total_votes") as Long)

        } catch (e:Exception) {
            Log.d("AppDebug",e.message.toString())
            null
        }
    }

}