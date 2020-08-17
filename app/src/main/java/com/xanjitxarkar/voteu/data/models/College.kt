package com.xanjitxarkar.voteu.data.models

data class College(
    val electionId:String,
    val collegeCode:String, val collegeType:String,
    val name: String,
    val postIds:MutableList<String>,
    val totalStudent: Long
)