package com.qriz.app.core.data.onboard.onboard.repository

import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.model.WeakArea
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.data.test.test_api.model.Test
import javax.inject.Inject

/** API 완성되기 전까지 사용할 테스트 Repository */
internal class FakeOnBoardRepository @Inject constructor() : OnBoardRepository {
    override fun submitSurvey(concepts: Collection<SQLDConcept>) {}

    override suspend fun getPreviewTest(): Test {
        val defaultQuestion = Question(
            id = 1,
            question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
            options = listOf(
                Option("트랜잭션을 더 작은 단위로 분할"),
                Option("트랜잭션의 타임아웃 시간을 늘림"),
                Option("모든 데이터를 메모리에 로드"),
                Option("트랜잭션의 격리 수준을 낮춤"),
            ),
            timeLimit = 60,
        )

        return Test(
            questions = List(5) { index ->
                defaultQuestion.copy(
                    id = (index + 1).toLong(),
                    question = "${defaultQuestion.question} #${index + 1}",
                    options = defaultQuestion.options.map { Option("${it.description} #${index + 1}") }
                )
            },
            totalTimeLimit = defaultQuestion.timeLimit * 5
        )
    }

    override suspend fun submitPreviewTest(answer: Map<Long, Option>) {}

    override suspend fun getPreviewTestResult(): PreviewTestResult {
        val dataList = listOf(
            /**프리뷰 테스트 모두 정답*/
            PreviewTestResult(
                estimatedScore = 83.0F,
                totalScore = 100,
                part1Score = 48,
                part2Score = 52,
                totalQuestions = 21,
                weakAreas = listOf(),
                topConceptsToImprove = listOf(SQLDConcept.JOIN, SQLDConcept.SELECT),
            ),
            /**프리뷰 테스트 한가지 개념만 오답*/
            PreviewTestResult(
                estimatedScore = 77.0F,
                totalScore = 96,
                part1Score = 48,
                part2Score = 48,
                totalQuestions = 21,
                weakAreas = listOf(
                    WeakArea(
                        topic = SQLDConcept.SELECT,
                        incorrectCount = 1
                    )
                ),
                topConceptsToImprove = listOf(SQLDConcept.SELECT, SQLDConcept.JOIN),
            ),
            /**여러 개념 오답*/
            PreviewTestResult(
                estimatedScore = 77.0F,
                totalScore = 55,
                part1Score = 25,
                part2Score = 30,
                totalQuestions = 21,
                weakAreas = listOf(
                    WeakArea(
                        topic = SQLDConcept.SELECT,
                        incorrectCount = 3
                    ),
                    WeakArea(
                        topic = SQLDConcept.ATTRIBUTION,
                        incorrectCount = 5
                    )
                ),
                topConceptsToImprove = listOf(SQLDConcept.SELECT, SQLDConcept.JOIN),
            ),
            PreviewTestResult(
                estimatedScore = 78.0F,
                totalScore = 60,
                part1Score = 20,
                part2Score = 40,
                totalQuestions = 21,
                weakAreas = listOf(
                    WeakArea(
                        topic = SQLDConcept.UNDERSTANDING_THE_DATA_MODEL,
                        incorrectCount = 1
                    ),
                    WeakArea(
                        topic = SQLDConcept.GROUP_BY_AND_HAVING,
                        incorrectCount = 1
                    ),
                    WeakArea(
                        topic = SQLDConcept.GROUP_FUNCTION,
                        incorrectCount = 1
                    ),
                ),
                topConceptsToImprove = listOf(
                    SQLDConcept.UNDERSTANDING_THE_DATA_MODEL,
                    SQLDConcept.GROUP_BY_AND_HAVING
                ),
            ),
            PreviewTestResult(
                estimatedScore = 78.0F,
                totalScore = 60,
                part1Score = 20,
                part2Score = 40,
                totalQuestions = 21,
                weakAreas = listOf(
                    WeakArea(
                        topic = SQLDConcept.UNDERSTANDING_THE_DATA_MODEL,
                        incorrectCount = 4
                    ),
                    WeakArea(
                        topic = SQLDConcept.GROUP_BY_AND_HAVING,
                        incorrectCount = 4
                    ),
                    WeakArea(
                        topic = SQLDConcept.GROUP_FUNCTION,
                        incorrectCount = 4
                    ),
                    WeakArea(
                        topic = SQLDConcept.UNDERSTANDING_TRANSACTIONS,
                        incorrectCount = 3
                    ),
                    WeakArea(
                        topic = SQLDConcept.DDL,
                        incorrectCount = 3
                    ),
                    WeakArea(
                        topic = SQLDConcept.ATTRIBUTION,
                        incorrectCount = 2
                    ),
                    WeakArea(
                        topic = SQLDConcept.JOIN,
                        incorrectCount = 1
                    ),
                ),
                topConceptsToImprove = listOf(
                    SQLDConcept.UNDERSTANDING_THE_DATA_MODEL,
                    SQLDConcept.GROUP_BY_AND_HAVING
                ),
            ),
        )
        val index = (0..dataList.lastIndex).random()
        return dataList[index]
    }
}
