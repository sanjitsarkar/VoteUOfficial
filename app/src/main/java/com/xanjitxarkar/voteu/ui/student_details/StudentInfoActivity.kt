package com.xanjitxarkar.voteu.ui.student_details

import android.content.Intent
import android.content.pm.ActivityInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.xanjitxarkar.voteu.R

import com.xanjitxarkar.voteu.data.models.Branch
import com.xanjitxarkar.voteu.data.models.RollNo
import com.xanjitxarkar.voteu.data.models.Student
import com.xanjitxarkar.voteu.data.repositories.AuthRepository
import com.xanjitxarkar.voteu.data.repositories.BranchRepo
import com.xanjitxarkar.voteu.data.repositories.CollegeRepository

import com.xanjitxarkar.voteu.data.repositories.FirebaseCollection.getUid
import com.xanjitxarkar.voteu.data.repositories.StudentRepository
import com.xanjitxarkar.voteu.databinding.ActivityStudentInfoBinding
import com.xanjitxarkar.voteu.ui.auth.AuthActivity
import com.xanjitxarkar.voteu.ui.home.HomeActivity
import com.xanjitxarkar.voteu.utils.DataState
import com.xanjitxarkar.voteu.utils.Utils.gone

import com.xanjitxarkar.voteu.utils.Utils.show
import com.xanjitxarkar.voteu.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.android.synthetic.main.activity_student_info.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StudentInfoActivity : AppCompatActivity() {
    val TAG:String = "AppDebug"
    val viewModel:StudentDetailsViewModel by viewModels()
    lateinit var binding:ActivityStudentInfoBinding

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_info)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        update.isEnabled = false
        viewModel.isLoggedIn.observe(this, Observer {
            if(it)
            {
                Intent(this,AuthActivity::class.java).also {intent->
                    startActivity(intent)
                    finish()
                }
            }
        })
        update.setOnClickListener()
        {
            it.gone()
            CoroutineScope(IO).launch {
                viewModel.addStudentInfo()
            }
                viewModel.studentInfoAdded.observe(this, Observer {dataState->
                    run {
                        when (dataState) {
                            is DataState.Loading -> {
                                update_loader.show()
                            }
                            is DataState.Info -> {
                                   toast(dataState.info)
                            }
                            is DataState.Success -> {
                                 Intent(this,HomeActivity::class.java).also {
                                     startActivity(it)
                                     finish()
                                 }
                            }
                            is DataState.Error -> {
toast(dataState.exception.message.toString())
                            }
                            else -> toast("Nothing")
                        }
                    }

                })


        }

        binding.changeGmail.setOnClickListener {
            CoroutineScope(Main).launch {
                viewModel.logout(this@StudentInfoActivity)
            }
        }

        binding.getDetails.setOnClickListener{

            binding.studentDetialsCard.gone()

CoroutineScope(IO).launch {
    viewModel.getStudentInfo(binding.collegeName.text.toString(),binding.rollNo.text.toString())
}


            viewModel.studentInfoData.observe(this, Observer { dataState->
                run {
                    when (dataState) {
                        is DataState.Loading -> {
                            binding.studentDetialsCard.gone()
                            loader_student.show()
                        }
                        is DataState.Info -> {
                            binding.studentDetialsCard.gone()
                            toast(dataState.info)
                            loader_student.gone()
                        }
                        is DataState.Success -> {
                            loader_student.gone()
                            binding.studentDetialsCard.show()
                            viewModel.studentData.value  = dataState.data
                            viewModel.studentInfo.value = Student(dataState.data.rollNo,dataState.data.collegeId,false,0,dataState.data.imgUrl)
                            update.isEnabled = true

                        }
                        is DataState.Error -> {
                            binding.studentDetialsCard.gone()
                            toast(dataState.exception.message.toString())

                            loader_student.gone()
                        }
                        else -> {
                            toast("Nothing")
                        }
                    }

                }
            })
            }
        }

    }
