package com.xanjitxarkar.voteu.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ElectionInfo(val collegeId:String, val electionId:String,val postId:String) : Parcelable