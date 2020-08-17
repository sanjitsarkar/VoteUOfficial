package com.xanjitxarkar.voteu.ui.candidate

import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.databinding.CandidateCardBinding
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.utils.BaseRecyclerViewAdapter

class CandidatesAdapter : BaseRecyclerViewAdapter<Candidate,CandidateCardBinding>() {
    override fun getLayout(): Int  =
        R.layout.candidate_card
    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<CandidateCardBinding>,
        position: Int
    ) {
        holder.binding.candidate = items[position]
        holder.binding.root.setOnClickListener {
            listener?.invoke(it,items[position],position)
        }
    }


}