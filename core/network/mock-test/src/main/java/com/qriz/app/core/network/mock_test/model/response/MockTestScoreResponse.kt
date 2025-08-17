package com.qriz.app.core.network.mock_test.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MockTestScoreResponse(
    @SerialName("title")
    val title: String,
    @SerialName("totalScore")
    val totalScore: Double,
    @SerialName("majorItems")
    val majorItems: List<MajorItemScore>,
)

@Serializable
data class MajorItemScore(
    @SerialName("majorItem")
    val majorItem: String,
    @SerialName("score")
    val score: Double,
    @SerialName("subItemScores")
    val subItemScores: List<SubItemScore>,
)

@Serializable
data class SubItemScore(
    @SerialName("subItem")
    val subItem: String,
    @SerialName("score")
    val score: Double,
)
