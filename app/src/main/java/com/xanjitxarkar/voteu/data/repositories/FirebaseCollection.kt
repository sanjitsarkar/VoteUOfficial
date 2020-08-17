package com.xanjitxarkar.voteu.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton


object FirebaseCollection {

        var auth:FirebaseAuth = FirebaseAuth.getInstance()
        var firestore: FirebaseFirestore = Firebase.firestore
        var collegeCollection: CollectionReference = firestore.collection("Colleges")
        var studentCollection: CollectionReference = firestore.collection("Students")
        var electionCollection: CollectionReference = firestore.collection("Elections")
        var winnerCollection: CollectionReference = firestore.collection("Winners")
        var branchCollection: CollectionReference = firestore.collection("Branches")
        var postCollection: CollectionReference = firestore.collection("Posts")
        var candidateCollection: CollectionReference = firestore.collection("Candidates")

         var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    fun getUid(): String
    {
        return firebaseAuth.uid as String
    }

}