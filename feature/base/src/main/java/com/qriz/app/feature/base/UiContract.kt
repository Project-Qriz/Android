package com.qriz.app.feature.base

/** Model         (ViewModel -> View)
 *
 * UI의 현재 상태
 *
 * 화면에 표시되는 데이터와 관련된 모든 정보를 포함
 */
interface UiState

/** Intent        (View -> ViewModel) or (ViewModel 내부 발생 및 처리)
 *
 * 사용자 Or 시스템의 특정 동작
 *
 * 상태 변경 혹은 SideEffect를 발생을 유발
 */
interface UiAction

/** SideEffect    (ViewModel -> View)
 *
 * 상태 변경이 필요하지 않은 일회성 UI 이벤트
 *
 * 화면 이동, 토스트 메시지, 스낵바 등
 */
interface UiEffect
