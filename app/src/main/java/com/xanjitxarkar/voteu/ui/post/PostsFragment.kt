package com.xanjitxarkar.voteu.ui.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.data.models.Post
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.hide
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.posts_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFragment : Fragment(R.layout.posts_fragment) {

    companion object {
        fun newInstance() = PostsFragment()
    }
    private val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel>()
    private val viewModel: PostsViewModel by activityViewModels<PostsViewModel>()

    var collegeId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {

            collegeId = it.collegeId
        })
        val adapter by lazy {
            PostsAdapter()
        }

        postsRecyclerView.adapter = adapter


        adapter.notifyDataSetChanged()
        adapter.items.clear()
        viewModel.postsData.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {

                    posts_loader.show()

                }
                is DataState.Success -> {
                    posts_loader.hide()
//                activity?.toast(dataState?.data?.get(0)?.name!!)
                    adapter.addItems(dataState?.data!!)


                    adapter.listener = { view, item, position ->
                        viewModel.postInfo.value = Post(item.code,item.name,item.id)


                            view.findNavController().navigate(R.id.action_postsFragment_to_candidatesFragment)

                    }
                }
                is DataState.Info -> {
                    activity?.toast("info")
                    posts_loader.hide()
                }
                is DataState.Error -> {
                    activity?.toast(dataState.exception.message.toString())
                    posts_loader.hide()
                }

                else -> {
                    activity?.toast("Nothing")

                }
            }
        })

        CoroutineScope(Main).launch {
            viewModel.getPosts(collegeId)
        }

    }

}