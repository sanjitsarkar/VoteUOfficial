package com.xanjitxarkar.voteu.ui.result

import com.xanjitxarkar.voteu.data.models.WinnerInfo
import com.xanjitxarkar.voteu.databinding.WinnerCardBinding
import kotlinx.android.synthetic.main.winner_card.view.*

import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.CandidateVote
import com.xanjitxarkar.voteu.databinding.CandidateVoteCardBinding
import com.xanjitxarkar.voteu.utils.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.candidate_vote_card.view.*

class ResultAdapter: BaseRecyclerViewAdapter<WinnerInfo,WinnerCardBinding>() {
    override fun getLayout(): Int {
        return R.layout.winner_card
    }

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<WinnerCardBinding>,
        position: Int
    ) {
        holder.binding.winner = items[position]


        Glide.with(holder.itemView.context).load(items[position].imgUrl)
            .placeholder(R.drawable.user)
            .error(R.drawable.ic_baseline_error_24)
            .into(holder.itemView.candidate_photo_winner)
        holder.binding.root.setOnClickListener {
            listener?.invoke(it,items[position],position)
        }
    }
}