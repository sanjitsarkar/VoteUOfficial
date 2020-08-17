package com.xanjitxarkar.voteu.data.models

import com.google.firebase.Timestamp


data class Election(
    val startTimestamp: Timestamp,
    val endTimestamp: Timestamp,
    val isCancelled:Boolean, val isStarted:Boolean, val isResultDeclared:Boolean,
    val totalVotes: Long
) {
}