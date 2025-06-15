package com.qriz.app.core.network.application.model.response

import kotlinx.serialization.Serializable  
import kotlinx.serialization.SerialName  

@Serializable  
data class UserApplicationInfo(
    @SerialName("examName") val examName: String,  
    @SerialName("period") val period: String,  
    @SerialName("examDate") val examDate: String,
    @SerialName("releaseDate") val releaseDate: String? = null,
)  
