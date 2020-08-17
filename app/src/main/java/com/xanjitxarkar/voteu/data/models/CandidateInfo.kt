package com.xanjitxarkar.voteu.data.models

data class CandidateInfo(val rollNo:String,val name:String,val branchName:String,val branchCode:String,var semester:String,val imgUrl:String)
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