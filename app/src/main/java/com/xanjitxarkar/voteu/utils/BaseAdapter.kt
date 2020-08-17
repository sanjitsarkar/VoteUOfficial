package com.xanjitxarkar.voteu.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.CandidateVote

abstract class BaseRecyclerViewAdapter<T : Any, VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.Companion.BaseViewHolder<VB>>() {
    var items = mutableListOf<T>()

    fun addItems(items: List<T>?) {
        this.items = items as MutableList<T>
        notifyDataSetChanged()
    }

    var listener: ((view: View, item: T, position: Int) -> Unit)? = null
    abstract fun getLayout(): Int
    override fun getItemCount(): Int = items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        return BaseViewHolder<VB>(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayout(),
                parent,
                false
            )
        )
    }


    companion object {
        class BaseViewHolder<VB : ViewDataBinding>(val binding: VB) :
            RecyclerView.ViewHolder(binding.root) {

        }
    }
}