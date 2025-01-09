package com.qriz.app.core.data.onboard.onboard.repository

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.data.test.test_api.model.Test
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.network.onboard.api.OnBoardApi
import javax.inject.Inject

internal class OnBoardRepositoryImpl @Inject constructor(
    private val onBoardApi: OnBoardApi
) : OnBoardRepository {

    override fun submitSurvey(concepts: Collection<PreCheckConcept>) {
//        onBoardApi.submitSurvey(
//            SurveyRequest(
//                keyConcept = concepts
//            )
//        )
        //TODO: API 연결
    }

    override suspend fun getPreviewTest(): Test {
        //API 연결
        return Test(
            questions = listOf(
                Question(
                    id = 1,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
                    options = listOf(
                        Option("트랜잭션을 더 작은 단위로 분할"),
                        Option("트랜잭션의 타임아웃 시간을 늘림"),
                        Option("모든 데이터를 메모리에 로드"),
                        Option("트랜잭션의 격리 수준을 낮춤"),
                    ),
                    timeLimit = 60,
                ),
                Question(
                    id = 2,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
                    options = listOf(
                        Option("트랜잭션을 더 작은 단위로 분할#2"),
                        Option("트랜잭션의 타임아웃 시간을 늘림#2"),
                        Option("모든 데이터를 메모리에 로드#2"),
                        Option("트랜잭션의 격리 수준을 낮춤#2"),
                    ),
                    timeLimit = 60,
                ),
                Question(
                    id = 3,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
                    options = listOf(
                        Option("트랜잭션을 더 작은 단위로 분할#2"),
                        Option("트랜잭션의 타임아웃 시간을 늘림#2"),
                        Option("모든 데이터를 메모리에 로드#2"),
                        Option("트랜잭션의 격리 수준을 낮춤#2"),
                    ),
                    timeLimit = 60,
                ),
                Question(
                    id = 4,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
                    options = listOf(
                        Option("트랜잭션을 더 작은 단위로 분할#2"),
                        Option("트랜잭션의 타임아웃 시간을 늘림#2"),
                        Option("모든 데이터를 메모리에 로드#2"),
                        Option("트랜잭션의 격리 수준을 낮춤#2"),
                    ),
                    timeLimit = 60,
                )
            ),
            totalTimeLimit = 1800 //30분
        )
    }

    override fun submitPreviewTest(answer: Map<Long, Option>) {
        //TODO : API 호출
    }
}
