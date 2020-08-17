package com.xanjitxarkar.voteu.data.models

import com.google.firebase.Timestamp

data class ToolBarData(val userName:String, val collegeId:String, val collegeName:String, val profilePicUrl:String, val email:String, val rollNo:String, val electionId:String?, val branchName:String, val branchId:String, val branchCode:String, val electionDateStart:Timestamp?,
                       val electionDateEnd:Timestamp?,var semester:String,val isVoted:Boolean)
{
    init {
        semester += when(semester) {
            "1"-> "st Semester"
            "2"-> "nd Semester"
            "3"-> "rd Semester"
            else-> "th Semester"

        }
    }
}