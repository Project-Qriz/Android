package com.quiz.app.core.data.user.user_api.model

/**
 * 1. 이메일 아이디 부분 : 영문 대소문자와 숫자만 허용, 최소 2자 최대 10자까지 가능
 * 2. 도메인 이름 부분 : 영문 대소문자와 숫자만 허용, 최소 2자 최대 6자까지 가능
 * 3. 최상위 도메인 부분 : 영문 대소문자만 허용, 최소 2자 최대 3자까지 가능 (예: com, net, org)
 */
val EMAIL_REGEX =
    "^[a-zA-Z0-9._%+-]{2,64}@[a-zA-Z0-9.-]{2,255}\\.[a-zA-Z]{2,10}".toRegex()

/**
 * 1. 길이 6자 이상 20자 이하
 * 2. 영문과 숫자를 반드시 둘 다 포함
 * 3. 공백 불포함
 * 4. 특수문자 불포함
 */
const val ID_MAX_LENGTH = 20
val ID_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,$ID_MAX_LENGTH}\$".toRegex()

/**
 * 1. 대문자 포함 : 최소 한 개의 대문자를 포함
 * 2. 소문자 포함 : 최소 한 개의 소문자를 포함
 * 3. 숫자 포함 : 하나 이상의 숫자 포함
 * 4. 특수 문자 포함 : 하나 이상의 특수 문자 포함
 */
const val PW_MIN_LENGTH = 8
const val PW_MAX_LENGTH = 16
val PW_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=*!])(?=\\S+\$).{$PW_MIN_LENGTH,$PW_MAX_LENGTH}".toRegex()

/** 한글/영문 1~20자 이내 */
const val USER_NAME_MAX_LENGTH = 20
val USER_NAME_REGEX = "^[a-zA-Z가-힣]{1,$USER_NAME_MAX_LENGTH}\$".toRegex()

/** 인증번호 */
const val AUTH_NUMBER_MAX_LENGTH = 6
