package com.xanjitxarkar.voteu.ui.candidate_details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.ui.candidate.CandidatesViewModel
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.ui.post.PostsViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.candidate_details_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateDetailsFragment : Fragment(R.layout.candidate_details_fragment) {

    companion object {
        fun newInstance() =
            CandidateDetailsFragment()
    }


    private val homeViewModel:HomeViewModel by activityViewModels<HomeViewModel>()
    private val candidatesViewModel:CandidatesViewModel by activityViewModels<CandidatesViewModel>()
    private val postsViewModel:PostsViewModel by activityViewModels<PostsViewModel>()
    private val viewModel:CandidateDetailsViewModel by viewModels<CandidateDetailsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.candidate_details_fragment, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var collegeId = ""
        var rollNo = ""
        var postTitle = ""

        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {
            collegeId = it.collegeId
        })
        candidatesViewModel.candidateData.observe(viewLifecycleOwner, Observer {
            rollNo = it.rollNo

        })
        postsViewModel.postInfo.observe(viewLifecycleOwner, Observer {
            postTitle = it.name
        })
        viewModel.candidateData.observe(viewLifecycleOwner, Observer {
            dataState->
            run {
                when (dataState) {
                    is DataState.Loading -> {
candidate_pic_loader.show()

                    }
                    is DataState.Success -> {
candidate_data_loader.gone()
                        candidate_post_title.text = postTitle
                        candidate_pic_loader.gone()
                        candidate_cardView.show()
                        candidate_name.text = dataState.data.name
                        roll_no_candidate_value.text = dataState.data.rollNo
                        branch_name_candidate.text = dataState.data.branchCode
                        semester_name_candidate.text  = dataState.data.semester

                        candidate_name.text = dataState.data.name
                        Glide.with(this).load(dataState.data.imgUrl)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.ic_baseline_error_24)
                            .into(candidate_photo_candidate);
                    }
                    is DataState.Error -> {

                    }
                    else -> ""
                }
            }
        })
        CoroutineScope(Main).launch {

            viewModel.getCandidateDetails(rollNo,collegeId)

        }
    }
}