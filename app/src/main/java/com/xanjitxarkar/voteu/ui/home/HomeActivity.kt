package com.xanjitxarkar.voteu.ui.home


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.xanjitxarkar.voteu.R
import com.xanjitxarkar.voteu.databinding.ActivityHomeBinding
import com.xanjitxarkar.voteu.ui.candidate.CandidatesFragment
import com.xanjitxarkar.voteu.utils.Utils.gone
import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity(){

    private lateinit var bindingHome: ActivityHomeBinding
     val homeViewModel: HomeViewModel by viewModels()

val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        bindingHome = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )
        bindingHome.viewModel = homeViewModel
        bindingHome.lifecycleOwner = this
        homeViewModel.toolbarDetails.observe(this, Observer {
//            if(it==null)
//            {
//                user_loader_home.show()
//                college_icon_home.gone()
//            }
homeViewModel.subscribeToElectionNotification(it.collegeId)
            college_name.text = it.collegeName

            Glide.with(this@HomeActivity).load(it.profilePicUrl)
                .error(R.drawable.ic_baseline_error_24)
                .into(user_photo_profile);
            user_loader_home.gone()
            college_icon_home.show()
        })

        CoroutineScope(IO).launch {

            homeViewModel.getToolbarDetails()

        }
//        val navigationView =
//            nav_view as NavigationView
//        navigationView.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)
//bottom_nav_view.setOnNavigationItemReselectedListener {  }

        val navController = Navigation.findNavController(
            this,
            R.id.fragment
        )

        NavigationUI.setupWithNavController(bottom_nav_view, navController)
//        NavigationUI.setupActionBarWithNavController(this, navController)
//        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        bindingHome.user.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_profileFragment)
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.dashboardFragment -> {
                    toolbar.show()
                    bottom_nav_view.show()

                }
                R.id.profileFragment -> {toolbar.gone()
                    bottom_nav_view.gone()
                }
                R.id.voteFragment -> {toolbar.gone()
                    bottom_nav_view.gone()
                }
                R.id.successFragment -> {toolbar.gone()
                    bottom_nav_view.gone()
                }
                R.id.resultFragment -> {toolbar.gone()
                    bottom_nav_view.gone()
                }
                else -> {
                    toolbar.gone()
                    bottom_nav_view.show()

                }
            }
        }

    }


//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.logout) {
//            Log.d("AppDebug","logout")
//            toast("hello")
////            homeViewModel.logOut()
//
//        }
//        drawer_layout.closeDrawer(GravityCompat.START);
//        return true
//    }


}
