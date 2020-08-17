package com.xanjitxarkar.voteu.ui.result

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.result_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.result_fragment) {

    companion object {
        fun newInstance() = ResultFragment()
    }

    private  val viewModel: ResultViewModel by activityViewModels<ResultViewModel>()
    private  val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


         val adapter:ResultAdapter by lazy {
            ResultAdapter()
        }
        var electionId = ""
        var collegeId = ""
        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {
            electionId = it.electionId.toString()
            collegeId = it.collegeId
        })
        winnersRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        viewModel.winnersLiveData.observe(viewLifecycleOwner, Observer {
                dataState->
            run {
                when (dataState) {
                    is DataState.Loading -> {
                        winners_loader.show()
                    }
                    is DataState.Success -> {
winners_loader.gone()
                        winnersRecyclerView.show()
                     adapter.addItems(dataState.data)
                        adapter.listener = {view, item, position ->
                            run {
activity?.toast(item.name)
                            }
                        }
                    }
                    is DataState.Error -> {

                    }
                    else -> ""
                }
            }

        })
        viewModel.electionLiveData.observe(viewLifecycleOwner, Observer {dataState->
            run {
                when (dataState) {
                    is DataState.Loading -> {
winners_loader.show()
                    }
                    is DataState.Success -> {
winners_loader.gone()
                        when {
                            dataState.data.isResultDeclared -> {
                                CoroutineScope(IO).launch {
                                    viewModel.getWinnersInfo(electionId,collegeId)
                                }
                            }
                            dataState.data.isStarted -> {
                                val d = Date(dataState.data.endTimestamp.seconds*1000)
                                winners_loader.gone()
                                info.show()
info.text = "Result will be declared at ${d.hours} : ${d.minutes}"
                            }

                            dataState.data.isCancelled -> {
                                info.show()
                                info.text = "Election is Cancelled"
                            }
                            else -> ""
                        }
                    }
                    is DataState.Error -> {

                    }
                    else -> ""
                }
            }

        })
        CoroutineScope(Main).launch {
            viewModel.electionInfo(electionId)

        }

        super.onViewCreated(view, savedInstanceState)
    }


}