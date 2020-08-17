package com.xanjitxarkar.voteu.ui.success

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.xanjitxarkar.voteu.R
import kotlinx.android.synthetic.main.success_fragment.*

class SuccessFragment : Fragment() {

    companion object {
        fun newInstance() = SuccessFragment()
    }

    private lateinit var viewModel: SuccessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.success_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SuccessViewModel::class.java)
        back_home.setOnClickListener {
            it.findNavController().navigate(R.id.action_successFragment_to_dashboardFragment)
        }
        // TODO: Use the ViewModel
    }

}