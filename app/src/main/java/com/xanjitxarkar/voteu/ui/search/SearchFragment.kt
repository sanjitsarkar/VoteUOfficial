package com.xanjitxarkar.voteu.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.ui.vote.SearchAdapter
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    companion object {
        fun newInstance() = SearchFragment()
    }
     val searchViewModel:SearchViewModel by activityViewModels()
     val homeViewModel:HomeViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val adapter:SearchAdapter by lazy {
            SearchAdapter()
        }
        searchRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
searchViewModel.searchResultsLiveData.observe(viewLifecycleOwner, Observer {datState->
    run {
        when (datState) {
            is DataState.Loading -> {
                search_info.gone()
                search_loader.show()
            }
            is DataState.NoData -> {
                search_loader.gone()
                search_info.show()
                search_info.text = datState.title

            }
            is DataState.Success -> {
                search_loader.gone()
                search_info.gone()
                adapter.addItems(datState.data)

            }
            is DataState.Error -> {
                Log.d("AppDebug",datState.exception.message.toString())

            }
            else -> ""
        }
    }

})
        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
//                    Log.d("AppDebug",newText)
                    CoroutineScope(IO).launch {
                        searchViewModel.search(newText,it.collegeId)
                    }
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {

                    return false
                }

            }

            )
        })

    }


}