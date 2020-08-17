package com.xanjitxarkar.voteu.ui.candidate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.Candidate
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.databinding.CandidatesFragmentBinding
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.ui.post.PostsViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.hide
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.candidates_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidatesFragment : Fragment(R.layout.candidates_fragment) {

    companion object {
        fun newInstance() =
            CandidatesFragment()
    }
        private val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel>()
    private val postsViewModel: PostsViewModel by activityViewModels<PostsViewModel>()

    val viewModel: CandidatesViewModel by activityViewModels<CandidatesViewModel>()



    private lateinit var binding:CandidatesFragmentBinding
    lateinit var post:Post
    var electionId = ""
    var collegeId = ""


    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.toast(requireArguments().getString("postId").toString())

        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {

            electionId = it.electionId.toString()

            collegeId = it.collegeId
        })
        postsViewModel.postInfo.observe(viewLifecycleOwner, Observer {
            post = Post(it.code,it.name,it.id)
post_title_name.text = post.name
        })

//        activity?.toast("hhh")


        val adapter by lazy {
            CandidatesAdapter()
        }
        candidatesRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        viewModel.candidatesData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    candidates_loader.show()
                    adapter.items.clear()
                }
                is DataState.Success -> {
                    candidates_loader.hide()
//                activity?.toast(dataState?.data?.get(0)?.name!!)
                    adapter.addItems(dataState?.data!!)
                    adapter.listener = { view, item, position ->
                        run {

                            viewModel.candidateData.value = item
                            view.findNavController().navigate(R.id.action_candidatesFragment_to_candidateDetailsFragment)
                        }
                    }
                }
                is DataState.Info -> {
                    activity?.toast("info")
                    candidates_loader.hide()
                }
                is DataState.Error -> {
                    activity?.toast(dataState.exception.message.toString())
                    candidates_loader.hide()
                }

                else -> {
                    activity?.toast("Nothing")

                }
            }
        })

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.getCandidates(electionId,post.id,collegeId)
        }

        // TODO: Use the ViewModel
    }
    }
