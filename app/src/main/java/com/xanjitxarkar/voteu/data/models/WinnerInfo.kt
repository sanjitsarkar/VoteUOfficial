package com.xanjitxarkar.voteu.data.models

data class WinnerInfo(val name:String, val postTitle:String, val postCode:String, val voteCount:String, val imgUrl:String,
                      var semester:String, val branchCode:String, val branchName:String) {
    init {
        semester += when(semester) {
            "1"-> "st Sem"
            "2"-> "nd Sem"
            "3"-> "rd Sem"
            else-> "th Sem"

        }
    }
}