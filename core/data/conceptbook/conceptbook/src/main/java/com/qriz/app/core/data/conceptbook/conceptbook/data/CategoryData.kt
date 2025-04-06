package com.qriz.app.core.data.conceptbook.conceptbook.data

internal sealed interface CategoryData {
    val categoryName: String
    val concept: List<ConceptBookData>

    enum class FirstSubjectCategory(
        override val categoryName: String,
        override val concept: List<ConceptBookData>,
    ) : CategoryData {
        DATA_MODELING("데이터 모델링의 이해", ConceptBookData.DataModeling.entries),
        DATA_MODEL_SQL("데이터 모델과 SQL", ConceptBookData.DataModelSQL.entries),;
    }

    enum class SecondSubjectCategory(
        override val categoryName: String,
        override val concept: List<ConceptBookData>,
    ) : CategoryData {
        SQL_BASIC("SQL 기본", ConceptBookData.SqlBasic.entries),
        SQL_ADVANCED("SQL 활용", ConceptBookData.SqlAdvanced.entries),
        QUERY_MANAGEMENT("관리 구문", ConceptBookData.QueryManagement.entries);
    }
}

