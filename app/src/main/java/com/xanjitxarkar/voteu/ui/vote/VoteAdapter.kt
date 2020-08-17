package com.xanjitxarkar.voteu.ui.vote

import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.CandidateVote
import com.xanjitxarkar.voteu.databinding.CandidateVoteCardBinding
import com.xanjitxarkar.voteu.utils.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.candidate_vote_card.view.*

class VoteAdapter: BaseRecyclerViewAdapter<CandidateVote,CandidateVoteCardBinding>() {
    override fun getLayout(): Int {
        return R.layout.candidate_vote_card
    }

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<CandidateVoteCardBinding>,
        position: Int
    ) {
        holder.binding.candidateVote = items[position]
        if(!items[position].isVoted) {
            holder.binding.voteCard.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
        Glide.with(holder.itemView.context).load(items[position].imgUrl)
            .placeholder(R.drawable.user)
            .error(R.drawable.ic_baseline_error_24)
            .into(holder.itemView.candidate_photo_vote)
        holder.binding.root.setOnClickListener {
            listener?.invoke(it,items[position],position)
        }
    }
}