package com.qriz.app.feature.concept_book.model

sealed interface Category {
    val categoryName: String
    val concept: List<Concept>

    enum class FirstSubjectCategory(
        override val categoryName: String,
        override val concept: List<Concept>,
    ) : Category {
        DATA_MODELING("데이터 모델링의 이해", Concept.DataModeling.entries),
        DATA_MODEL_SQL("데이터 모델과 SQL", Concept.DataModelSQL.entries),;
    }

    enum class SecondSubjectCategory(
        override val categoryName: String,
        override val concept: List<Concept>,
    ) : Category {
        SQL_BASIC("SQL 기본", Concept.SqlBasic.entries),
        SQL_ADVANCED("SQL 활용", Concept.SqlAdvanced.entries),
        QUERY_MANAGEMENT("관리 구문", Concept.QueryManagement.entries);
    }
}

