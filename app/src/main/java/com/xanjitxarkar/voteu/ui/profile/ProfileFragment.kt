package com.xanjitxarkar.voteu.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.databinding.ProfileFragmentBinding
import com.xanjitxarkar.voteu.ui.auth.AuthActivity
import com.xanjitxarkar.voteu.ui.home.HomeViewModel
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }
    private val homeViewModel:HomeViewModel by activityViewModels<HomeViewModel>()
    private  val profileViewModel: ProfileViewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: ProfileFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     binding = DataBindingUtil.inflate(inflater,R.layout.profile_fragment, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.toolbarDetails.observe(viewLifecycleOwner, Observer {


           lifecycleScope.launch(IO)
           {
               profileViewModel.voted()
           }

            binding.profileCardView.show()
            binding.userPicLoader.gone()
            binding.profileLoader.gone()
            profileViewModel._toolbarDetails.value = it
            binding.userName.text = it.userName
            binding.collegeNameProfile.text = it.collegeName
            binding.email.text = it.email
            binding.rollNoProfileValue.text = it.rollNo
            binding.semesterNameProfile.text = it.semester
            binding.branchNameProfile.text = it.branchCode

            if(it.electionDateStart==null)
            {
                binding.electionDate.text = "N/A"
            }
            else
            {
                val start = Date(it.electionDateStart.seconds*1000)
                val end = Date(it.electionDateEnd?.seconds!!*1000)
                binding.electionDate.text = start.toLocaleString()+" - "+end.toLocaleString()
            }

            Glide.with(this).load(it.profilePicUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.ic_baseline_error_24)
                .into(binding.userPhotoProfile)
        })

        profileViewModel.voted.observe(viewLifecycleOwner, Observer {
            when(it)
            {
                is DataState.Success->
                {
                    if(it.data)
                    {
                        binding.voted.text = "Voted"
                    }
                    else
                    {
                        binding.voted.text = "Not Voted"
                    }
                }
            }
        })
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profileViewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                Intent(activity,AuthActivity::class.java).also {intent->
                    startActivity(intent)
                    activity?.finish()

                }
            }
            else
            {

            }
        })

//CoroutineScope(IO).launch {
//    homeViewModel.getToolbarDetails()
//}

binding.logoutButton.setOnClickListener {
    CoroutineScope(Main).launch {
        profileViewModel.logout(requireContext())
    }

}

        // TODO: Use the ViewModel
    }

}