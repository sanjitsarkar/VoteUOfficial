package com.xanjitxarkar.voteu.ui.success

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.ui.vote.VoteViewModel
import com.xanjitxarkar.voteu.utils.DataState
import kotlinx.android.synthetic.main.success_fragment.*
import java.util.*

class SuccessFragment : Fragment() {

    companion object {
        fun newInstance() = SuccessFragment()
    }

    private lateinit var viewModel: SuccessViewModel
    private val voteViewModel: VoteViewModel by viewModels<VoteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.success_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        voteViewModel.electionLiveData.observe(viewLifecycleOwner, Observer {dataState->
            run {
                when (dataState) {
                    is DataState.Success -> {
                        val date = Date(dataState.data.endTimestamp.seconds*1000)
                         result_time.text = date.toLocaleString()
                    }
                }
            }

        })
        viewModel = ViewModelProvider(this).get(SuccessViewModel::class.java)
        back_home.setOnClickListener {
            it.findNavController().navigate(R.id.action_successFragment_to_dashboardFragment)
        }
        // TODO: Use the ViewModel
    }

}