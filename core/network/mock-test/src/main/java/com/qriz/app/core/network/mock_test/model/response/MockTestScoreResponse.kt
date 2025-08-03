package com.qriz.app.core.network.mock_test.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MockTestScoreResponse(
    @SerialName("title")
    val title: String,
    @SerialName("total_score")
    val totalScore: Int,
    @SerialName("major_items")
    val majorItems: List<MajorItemScore>,
)

@Serializable
data class MajorItemScore(
    @SerialName("major_item")
    val majorItem: String,
    @SerialName("score")
    val score: Int,
    @SerialName("sub_item_scores")
    val subItemScores: List<SubItemScore>,
)

@Serializable
data class SubItemScore(
    @SerialName("sub_item")
    val subItem: String,
    @SerialName("score")
    val score: Int,
)
