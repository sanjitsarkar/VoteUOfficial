package com.xanjitxarkar.voteu.ui.student_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xanjitxarkar.voteu.R

class StudentDetailsFragment : Fragment() {

    companion object {
        fun newInstance() =
            StudentDetailsFragment()
    }

    private lateinit var viewModel: StudentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.student_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StudentDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}