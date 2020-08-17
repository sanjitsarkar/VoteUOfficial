//package com.xanjitxarkar.voteu.ui.candidate
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import com.xanjitxarkar.voteu.R
//import com.xanjitxarkar.voteu.data.models.Candidate
//import com.xanjitxarkar.voteu.data.models.ElectionInfo
//import com.xanjitxarkar.voteu.data.models.RollNo
//import com.xanjitxarkar.voteu.data.repositories.AuthRepository
//import com.xanjitxarkar.voteu.data.repositories.CandidateRepository
//import com.xanjitxarkar.voteu.data.repositories.StudentRepository
//import com.xanjitxarkar.voteu.utils.Utils.hide
//import com.xanjitxarkar.voteu.utils.Utils.show
//import com.xanjitxarkar.voteu.utils.Utils.toast
//import kotlinx.android.synthetic.main.activity_candidates.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.Dispatchers.Main
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class CandidatesActivity : AppCompatActivity() {
//    val TAG = "CandidatesActivity"
//    private val adapter by lazy {
//        CandidatesAdapter()
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_candidates)
//
//        var candidates:MutableList<Candidate> = mutableListOf<Candidate>()
//
//        candidates_loader.show()
//        candidatesRecyclerView.adapter = adapter
//        CoroutineScope(IO).launch {
//
//            val candidateRepo =
//                CandidateRepository()
//            val authRepo =
//                AuthRepository()
//            val studentRepo =
//                StudentRepository()
//
//            val datas = candidateRepo.getCandidateIds(
//                    "U5TjcDmKrlCTWP28EjaF","2XEOX7R6ohJFK6E8igWe")
//
//            datas?.forEach { data->
//
//                    val studentData: RollNo = studentRepo.getStudentDataByRollNodAndCollegeId(data,data)!!
//                candidates.add(
//                    Candidate(
//                        "",
//                        electionInfo.postId,
//                        0,
//                        studentData.name,
//                        ""
//                    )
//                )
//                Log.d(TAG,studentData.toString())
//            }
//            Log.d(TAG,datas.toString())
//
//            withContext(Main)
//            {
//                candidates_loader.hide()
//                adapter.addItems(candidates)
//                adapter.listener = {
//                        view,item,position ->
//                    run {
//                       toast(item.name!!)
//                    }
//                }
//            }
//        }
//
//
//
//    }
//}