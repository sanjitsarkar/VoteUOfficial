package com.xanjitxarkar.voteu.ui.vote

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.CandidateVote
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.databinding.CandidateVoteCardBinding
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.candidate_vote_card.*
import kotlinx.android.synthetic.main.result_fragment.*
import kotlinx.android.synthetic.main.vote_fragment.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.util.*

@AndroidEntryPoint
class VoteFragment : Fragment(R.layout.vote_fragment) {

    companion object {
        fun newInstance() = VoteFragment()
    }

    private val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel>()
    private val viewModel: VoteViewModel by activityViewModels<VoteViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: VoteAdapter by lazy {
            VoteAdapter()
        }
        next.gone()
        voteRecyclerView.adapter = adapter

        var collegeId = ""
        var electionId = ""
        var i = 0
        var isClicked:Boolean = false
        var posts: List<Post>? = null
        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {
            collegeId = it.collegeId
            electionId = it.electionId!!
        })
      viewModel.electionLiveData.observe(viewLifecycleOwner, Observer {dataState->
            run {
                when (dataState) {
                    is DataState.Loading -> {
                        candidate_vote_loader.show()
                        voteRecyclerView.gone()
                    }
                    is DataState.Success -> {


                        if (dataState.data.isStarted) {
                            lifecycleScope.launch(Main) {
                                Log.d("AppDebug", "Loading Vote")
                                viewModel.getPosts(collegeId)
                            }
                        } else {
                            candidate_vote_loader.gone()
                            info_vote.show()
                            val start = Date(dataState.data.startTimestamp.seconds * 1000)
                            val end = Date(dataState.data.endTimestamp.seconds * 1000)


                            info_vote.text =
                                "Voting will be started from ${start.toLocaleString()} - ${end.toLocaleString()}"
                        }

                    }



                    is DataState.Error -> {

                    }
                    else -> ""
                }
            }

        })
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.electionInfo(electionId)

        }




        next.setOnClickListener {
            candidate_vote_loader.show()
            adapter.items.clear()
//            binding.candidateVote = null



            isClicked = true
            if (i == posts?.size!!-1) {
                lifecycleScope.launch(IO) {
                    viewModel.voteSuccess()
                    withContext(Main)
                    {
                        it.findNavController().navigate(R.id.action_voteFragment_to_successFragment)
                    }
                }



            } else {
               i++
                post_title_vote.text = "${i + 1}. ${posts?.get(i)?.name}"

                viewModel.position.value = i


                CoroutineScope(IO).launch {
                    viewModel.getVoteCandidates(electionId, posts!![i].id, collegeId)
                }



            }
        }

        viewModel.postsLiveData.observe(viewLifecycleOwner, Observer { dataState ->

            when (dataState) {
                is DataState.Loading -> {
                    info_vote.gone()

                }
                is DataState.Success -> {

                    post_title_vote_card.show()
                    posts = dataState.data
                    post_title_vote.text = "${i + 1}. ${posts?.get(i)?.name}"


                        lifecycleScope.launch(IO) {
                            viewModel.getVoteCandidates(electionId, posts!![i].id, collegeId)

                    }

                }
                is DataState.NoData -> {

                }
                is DataState.Error -> {
                    Log.d("AppDebug", dataState.exception.message)
                }


                else -> activity?.toast("Nothing")
            }
        })

        viewModel.candidatesLiveData.observe(viewLifecycleOwner, Observer { dataState->
            run {
                when (dataState) {
                    is DataState.Loading -> {
                        candidate_vote_loader.show()

                        next.isEnabled = false

                    }
                    is DataState.Success -> {

                        candidate_vote_loader.gone()
                        voteRecyclerView.show()
                        next.show()
                        adapter.addItems(dataState.data)

                        adapter.notifyDataSetChanged()
                        adapter.listener = { view, item, position ->
                            run {
                                if (!item.voted) {
                                    lifecycleScope.launch(IO) {
                                        withContext(Main)
                                        {
                                            item.voted = true
                                            adapter.items.forEach { it.voted=true }
                                            adapter.notifyDataSetChanged()
                                            view.findViewById<CardView>(R.id.vote_card).setCardBackgroundColor(
                                                getColor(requireContext(),R.color.sky_blue_light))
                                        }
                                        viewModel.vote(item.postId, item.rollNo, electionId)
                                        withContext(Main)
                                        {
                                            next.isEnabled = true
                                            activity?.toast("voted to ${item.name}")


                                        }


                                    }
                                }

                            }

                        }
                    }
                    is DataState.NoData -> {

                    }
                    is DataState.Error -> {
                        Log.d("AppDebug", dataState.exception.message.toString())
                    }


                    else -> activity?.toast("Nothing")
                }
            }
        })




//                if(posts!=null) {
//                    lifecycleScope.launch(IO) {
//                        viewModel.getVoteCandidates(electionId, posts!![i].id, collegeId)
//                    }
//                }








    }




}