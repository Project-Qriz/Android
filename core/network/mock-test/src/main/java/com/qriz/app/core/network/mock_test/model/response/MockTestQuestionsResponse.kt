package com.qriz.app.core.network.mock_test.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MockTestQuestionsResponse(
    @SerialName("questions")
    val questions: List<MockTestQuestionResponse>,
    @SerialName("totalTimeLimit")
    val totalTimeLimit: Int
)

@Serializable
data class MockTestQuestionResponse(
    @SerialName("questionId")
    val questionId: Long,
    @SerialName("skillId")
    val skillId: Long,
    @SerialName("category")
    val category: Int,
    @SerialName("question")
    val question: String,
    @SerialName("description")
    val description: String?,
    @SerialName("options")
    val options: List<MockTestOptionResponse>,
    @SerialName("timeLimit")
    val timeLimit: Int,
    @SerialName("difficulty")
    val difficulty: Int
)

@Serializable
data class MockTestOptionResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("content")
    val content: String
)
