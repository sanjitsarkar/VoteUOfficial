package com.xanjitxarkar.voteu.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.databinding.DashboardFragmentBinding
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.hide
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.result_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() =
            DashboardFragment()
    }

    private val viewModel: DashboardViewModel by activityViewModels<DashboardViewModel>()
    private val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var binding:DashboardFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.dashboard_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vote.gone()
        binding.candidates.gone()
        binding.result.gone()

      homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {
          lifecycleScope.launch(IO) {
              viewModel.voted()
          }
          if(it.electionId=="")
          {
              binding.candidates.gone()
              binding.vote.gone()
              binding.result.gone()
              dashboard_loader.gone()
              info_dashboard.show()
              info_dashboard.text = "Election Date is not yet fixed"
          }
else {

              viewModel.electionLiveData.observe(viewLifecycleOwner, Observer {
                      dataState->
                  run {
                      when (dataState) {
                          is DataState.Loading -> {

                          }
                          is DataState.Success -> {
                              dashboard_loader.gone()

                                  if(dataState.data.isResultDeclared) {
                                      binding.vote.gone()
                                     binding.result.show()
                                      binding.candidates.show()
                                  }


                                  else if(dataState.data.isCancelled) {
                                      binding.vote.gone()
                                      binding.result.gone()
                                      binding.candidates.gone()
                                      info_dashboard.show()
                                      info_dashboard.text = "Election is Cancelled"
                                  }
                              else
                                  {
                                   if(!it.voted)
                                   {

                                       binding.result.gone()
                                       binding.candidates.show()
                                       binding.vote.show()
                                       lifecycleScope.launch {
                                           viewModel.electionInfo(it.electionId!!)
                                       }

                                       viewModel.votedLiveData.observe(viewLifecycleOwner,
                                           Observer { dataState->
                                               when(dataState)
                                               {
                                                   is DataState.Loading->
                                                   {

                                                   }
                                                   is DataState.Success->
                                                   {
                                                       dashboard_loader.gone()
                                                       if(!dataState.data)
                                                       {
                                                           binding.result.gone()
                                                           binding.candidates.show()
                                                           binding.vote.show()
                                                       }
                                                       else
                                                       {
                                                           binding.result.show()
                                                           binding.candidates.show()
                                                           binding.vote.gone()
                                                       }
                                                   }
                                               }

                                           })
                                   }
                                      else
                                   {
                                       binding.result.show()
                                       binding.candidates.show()
                                       binding.vote.gone()
                                   }
                                  }


                          }
                          is DataState.Error -> {

                          }
                          else -> ""
                      }
                  }

              })

              lifecycleScope.launch(IO)
              {
                  viewModel.electionInfo(it.electionId!!)
              }





          }

      })

        binding.viewModel = viewModel
        binding.vote.setOnClickListener {
            it.findNavController().navigate(R.id.action_dashboardFragment_to_voteFragment)
        }
        binding.candidates.setOnClickListener {
            val bundle:Bundle = bundleOf("name" to "Sanjit")
            it.findNavController().navigate(R.id.action_dashboardFragment_to_postsFragment,bundle)
        }
        binding.result.setOnClickListener {

            it.findNavController().navigate(R.id.action_dashboardFragment_to_resultFragment)
        }
        // TODO: Use the ViewModel
    }

}