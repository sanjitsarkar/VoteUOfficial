package com.xanjitxarkar.voteu.data.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


data class CandidateVote(val rollNo:String, val name:String, val branchName:String, val branchCode:String, var semester:String, val imgUrl:String,
                         var voted:Boolean, val postId:String)
{
    init {
        semester += when(semester) {
            "1"-> "st Sem"
            "2"-> "nd Sem"
            "3"-> "rd Sem"
            else-> "th Sem"

        }
    }

    @BindingAdapter("profileImage")
    fun loadImage(view: ImageView, imgUrl:String ?) {
        Glide.with(view.context)
            .load(imgUrl).apply(RequestOptions().circleCrop())
            .into(view)
    }
}