package com.qriz.app.core.data.onboard.onboard_api.model

enum class PreCheckConcept(val title: String) {
    UNDERSTANDING_THE_DATA_MODEL("데이터 모델의 이해"),
    ENTITY("엔터티"),
    ATTRIBUTION("속성"),
    RELATIONSHIPS("관계"),
    IDENTIFIER("식별자"),
    NORMALIZATION("정규화"),
    UNDERSTANDING_RELATIONSHIPS_AND_JOINS("관계와 조인의 이해"),
    UNDERSTANDING_TRANSACTIONS("모델이 표현하는 트랜잭션의 이해"),
    UNDERSTANDING_NULL_PROPERTIES("Null 속성의 이해"),
    ESSENTIAL_VS_ARTIFICIAL_IN_IDENTIFIER("본질식별자 vs 인조식별자"),
    UNDERSTANDING_RELATIONAL_DATABASES("관계형 데이터베이스 이해"),
    SELECT("SELECT문"),
    FUNCTION("함수"),
    WHERE("WHERE절"),
    GROUP_BY_AND_HAVING("GROUP BY 와 HAVING절"),
    ORDER_BY("ORDER BY절"),
    JOIN("조인"),
    STANDARD_JOIN("표준 조인"),
    SUBQUERY("서브 쿼리"),
    SET_OPERATOR("집합 연산자"),
    GROUP_FUNCTION("그룹 함수"),
    WINDOW_FUNCTION("윈도우 함수"),
    TOP_N_QUERIES("TOP N 쿼리"),
    HIERARCHICAL_QUERIES_AND_SELF_JOINS("계층형 질의와 셀프 조인"),
    PIVOT_AND_UNPIVOT("PIVOT절과 UNPIVOT절"),
    REGULAR_EXPRESSION("정규 표현식"),
    DML("DML"),
    TCL("TCL"),
    DDL("DDL"),
    DCL("DCL"),
}