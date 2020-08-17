package com.xanjitxarkar.voteu.data.models

data class StudentInfo(val rollNo:String, val name:String, val branchName:String, var semester:String, val collegeId:String,val imgUrl:String) {
   init {
       semester += when(semester) {
           "1"-> "st Semester"
           "2"-> "nd Semester"
           "3"-> "rd Semester"
           else-> "th Semester"

       }
   }
}