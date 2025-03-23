package com.qriz.app.core.data.conceptbook.conceptbook.data

internal sealed interface ConceptBookData {
    val conceptName: String
    val id: Long
    val fileName: String

    enum class DataModeling(
        override val id: Long,
        override val conceptName: String,
        override val fileName: String,
    ) : ConceptBookData {
        DATA_MODEL(1, "데이터 모델의 이해", "data_model.pdf"),
        ENTITY(2, "엔터티", "entity.pdf"),
        ATTRIBUTE(3, "속성", "attribute.pdf"),
        RELATIONSHIP(4, "관계", "relationship.pdf"),
        IDENTIFIER(5, "식별자", "identifier.pdf"),
    }

    enum class DataModelSQL(
        override val id: Long,
        override val conceptName: String,
        override val fileName: String,
    ) : ConceptBookData {
        NORMALIZATION(6, "정규화", "normalization.pdf"),
        RELATION_JOIN(7, "관계와 조인의 이해", "relation_join.pdf"),
        TRANSACTION(8, "모델이 표현하는 트랜잭션의 이해", "transaction.pdf"),
        NULL_ATTRIBUTE(9, "NULL 속성의 이해", "null_attribute.pdf"),
        IDENTIFIER_TYPE(10, "본질식별자 vs 인조식별자", "identifier_type.pdf"),;
    }

    enum class SqlBasic(
        override val id: Long,
        override val conceptName: String,
        override val fileName: String,
    ) : ConceptBookData {
        RELATIONAL_DB_INTRO(11, "관계형 데이터베이스 개요", "relational_db_intro.pdf"),
        SELECT(12, "SELECT문", "select.pdf"),
        FUNCTION(13, "함수", "function.pdf"),
        WHERE_CLAUSE(14, "WHERE절", "where_clause.pdf"),
        GROUP_BY_HAVING(15, "GROUP BY, HAVING 절", "group_by_having.pdf"),
        ORDER_BY_CLAUSE(16, "ORDER BY절", "order_by_clause.pdf"),
        JOIN(17, "조인", "join.pdf"),
        STANDARD_JOIN(18, "표준 조인", "standard_join.pdf");
    }

    enum class SqlAdvanced(
        override val id: Long,
        override val conceptName: String,
        override val fileName: String,
    ) : ConceptBookData {
        SUBQUERY(19, "서브 쿼리", "subquery.pdf"),
        AGGREGATE_OPERATOR(20, "집합 연산자", "aggregate_operator.pdf"),
        GROUP_FUNCTION(21, "그룹 함수", "group_function.pdf"),
        WINDOW_FUNCTION(22, "윈도우 함수", "window_function.pdf"),
        TOP_N_QUERY(23, "Top N 쿼리", "top_n_query.pdf"),
        HIERARCHICAL_QUERY(24, "계층형 질의와 셀프 조인", "hierarchical_query.pdf"),
        PIVOT_UNPIVOT(25, "PIVOT절과 UNPIVOT절", "pivot_unpivot.pdf"),
        REGEX(26, "정규 표현식", "regex.pdf");
    }

    enum class QueryManagement(
        override val id: Long,
        override val conceptName: String,
        override val fileName: String,
    ) : ConceptBookData {
        DML(27, "DML", "dml.pdf"),
        TCL(28, "TCL", "tcl.pdf"),
        DDL(29, "DDL", "ddl.pdf"),
        DCL(30, "DCL", "dcl.pdf");
    }
}
