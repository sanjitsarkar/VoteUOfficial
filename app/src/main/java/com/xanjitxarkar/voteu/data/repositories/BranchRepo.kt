package com.xanjitxarkar.voteu.data.repositories

import com.xanjitxarkar.voteu.data.models.Branch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BranchRepo
@Inject
    constructor() {

    suspend fun getBranch(branch_id:String): Branch?
    {

        var branchDetails = FirebaseCollection.branchCollection.document(branch_id).get().await()
        branchDetails?.let {  return Branch(
            branchDetails?.get("name") as String,
            branchDetails?.get("code") as String
        )
        }
        return null

    }

}