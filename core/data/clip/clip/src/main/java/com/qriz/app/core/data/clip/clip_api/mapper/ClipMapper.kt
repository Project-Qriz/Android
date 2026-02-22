package com.qriz.app.core.data.clip.clip_api.mapper

import com.qriz.app.core.data.clip.clip_api.model.Clip
import com.qriz.app.core.data.clip.clip_api.model.ClipDetail
import com.qriz.app.core.network.clip.model.ClipDetailResponse
import com.qriz.app.core.network.clip.model.ClipFilterResponse

/**
 * ClipFilterResponse를 Clip domain 모델로 변환
 */
fun ClipFilterResponse.toDomain(): Clip =
    Clip(
        id = id,
        questionNum = questionNum,
        question = question,
        correction = correction,
        keyConcepts = keyConcepts,
        date = date
    )

/**
 * ClipDetailResponse를 ClipDetail domain 모델로 변환
 */
fun ClipDetailResponse.toDomain(): ClipDetail =
    ClipDetail(
        skillName = skillName,
        questionText = questionText,
        questionNum = questionNum,
        description = description,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        answer = answer,
        solution = solution,
        checked = checked,
        correction = correction,
        testInfo = testInfo,
        skillId = skillId,
        title = title,
        keyConcepts = keyConcepts
    )
