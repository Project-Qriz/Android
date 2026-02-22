package com.qriz.app.core.data.test.test_api.model

/** SQLD 과목 */
enum class SQLDSubject(
    val title: String,
    val subParts: List<SQLDSubPart>
) {
    PART_1(
        title = "데이터 모델링의 이해",
        subParts = listOf(
            SQLDSubPart.UNDERSTANDING_DATA_MODELING,
            SQLDSubPart.DATA_MODEL_AND_SQL
        )
    ),
    PART_2(
        title = "SQL 기본 및 활용",
        subParts = listOf(
            SQLDSubPart.SQL_DEFAULT,
            SQLDSubPart.SQL_UTILIZATION,
            SQLDSubPart.MANAGEMENT_SYNTAX
        )
    ),
}

/** SQLD 소과목 */
enum class SQLDSubPart(
    val title: String,
    val concepts: List<SQLDConcept>
) {
    UNDERSTANDING_DATA_MODELING(
        title = "데이터 모델링의 이해",
        concepts = listOf(
            SQLDConcept.UNDERSTANDING_THE_DATA_MODEL,
            SQLDConcept.ENTITY,
            SQLDConcept.ATTRIBUTION,
            SQLDConcept.RELATIONSHIPS,
            SQLDConcept.IDENTIFIER,
        )
    ),
    DATA_MODEL_AND_SQL(
        title = "데이터 모델과 SQL",
        concepts = listOf(
            SQLDConcept.NORMALIZATION,
            SQLDConcept.UNDERSTANDING_RELATIONSHIPS_AND_JOINS,
            SQLDConcept.UNDERSTANDING_TRANSACTIONS,
            SQLDConcept.UNDERSTANDING_NULL_PROPERTIES,
            SQLDConcept.ESSENTIAL_VS_ARTIFICIAL_IN_IDENTIFIER,
        )
    ),

    SQL_DEFAULT(
        title = "SQL 기본",
        concepts = listOf(
            SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
            SQLDConcept.SELECT,
            SQLDConcept.FUNCTION,
            SQLDConcept.WHERE,
            SQLDConcept.GROUP_BY_AND_HAVING,
            SQLDConcept.ORDER_BY,
            SQLDConcept.JOIN,
            SQLDConcept.STANDARD_JOIN,
        )
    ),
    SQL_UTILIZATION(
        title = "SQL 활용",
        concepts = listOf(
            SQLDConcept.SUBQUERY,
            SQLDConcept.SET_OPERATOR,
            SQLDConcept.GROUP_FUNCTION,
            SQLDConcept.WINDOW_FUNCTION,
            SQLDConcept.TOP_N_QUERIES,
            SQLDConcept.HIERARCHICAL_QUERIES_AND_SELF_JOINS,
            SQLDConcept.PIVOT_AND_UNPIVOT,
            SQLDConcept.REGULAR_EXPRESSION,
        )
    ),
    MANAGEMENT_SYNTAX(
        title = "관리 구문",
        concepts = listOf(
            SQLDConcept.DML,
            SQLDConcept.TCL,
            SQLDConcept.DDL,
            SQLDConcept.DCL,
        )
    ),
}

/** SQLD 개념 */
enum class SQLDConcept(val title: String) {
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
    RELATIONAL_DATABASE_OVERVIEW("관계형 데이터베이스 개요"),
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
    TOP_N_QUERIES("Top N 쿼리"),
    HIERARCHICAL_QUERIES_AND_SELF_JOINS("계층형 질의와 셀프 조인"),
    PIVOT_AND_UNPIVOT("PIVOT절과 UNPIVOT절"),
    REGULAR_EXPRESSION("정규 표현식"),
    DML("DML"),
    TCL("TCL"),
    DDL("DDL"),
    DCL("DCL");

    companion object {
        fun find(title: String): SQLDConcept {
            return entries.find { it.title == title }
                ?: throw Exception("title에 부합하는 SQLDConcept가 존재하지 않습니다. title : $title")
        }
    }
}
