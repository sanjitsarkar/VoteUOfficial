package com.xanjitxarkar.voteu.ui.post

import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.databinding.PostCardBinding

import com.xanjitxarkar.voteu.utils.BaseRecyclerViewAdapter

class PostsAdapter : BaseRecyclerViewAdapter<Post,PostCardBinding>() {
    override fun getLayout(): Int  = R.layout.post_card
    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<PostCardBinding>,
        position: Int
    ) {
        holder.binding.post  =  items[position]
   holder.binding.root.setOnClickListener {
       listener?.invoke(it,items[position],position)
   }
    }


}