package com.xanjitxarkar.voteu.ui.vote

import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.CandidateVote
import com.xanjitxarkar.voteu.data.models.SearchResult
import com.xanjitxarkar.voteu.databinding.CandidateVoteCardBinding
import com.xanjitxarkar.voteu.databinding.SearchCardBinding
import com.xanjitxarkar.voteu.utils.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.candidate_vote_card.view.*

class SearchAdapter: BaseRecyclerViewAdapter<SearchResult,SearchCardBinding>() {
    override fun getLayout(): Int {
        return R.layout.search_card
    }

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<SearchCardBinding>,
        position: Int
    ) {
        holder.binding.search = items[position]


        holder.binding.root.setOnClickListener {
            listener?.invoke(it,items[position],position)
        }
    }
}