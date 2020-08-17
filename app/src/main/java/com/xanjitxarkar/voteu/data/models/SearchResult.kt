package com.xanjitxarkar.voteu.data.models

data class SearchResult(val name:String,
                        var semester:String, val email:String, val rollNo:String, val branchId:String,val branchCode:String) {
    init {
        semester += when(semester) {
            "1"-> "st Sem"
            "2"-> "nd Sem"
            "3"-> "rd Sem"
            else-> "th Sem"

        }
    }
}