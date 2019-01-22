package org.umeframework.dora.tool.exp.db;

public interface DbDescQueryStr {
    /**
     * TABLE_DESC_QUERY_FOR_DB2
     */
    String TABLE_DESC_QUERY_FOR_DB2 = "SELECT"
            + " T1.COLNAME AS \"colId\","
            + " T1.REMARKS AS \"colName\","
            + " T1.TYPENAME AS \"dataType\","
            + " CASE WHEN T1.TYPENAME ='INTEGER' THEN 10 WHEN T1.TYPENAME ='SMALLINT' THEN 5 WHEN T1.TYPENAME ='NUMERIC' THEN 31 ELSE T1.LENGTH END AS \"dataLength\","
            + " T1.LENGTH AS \"dataPrecision\","
            + " T1.SCALE AS \"dataScale\","
            + " T1.NULLS AS \"notNull\","
            + " NULL AS \"defaultValue\","
            + " MAX(CASE WHEN T2.TYPE='P' THEN '1' ELSE '0' end ) as \"pkFlag\""
            + " FROM SYSCAT.COLUMNS AS T1 LEFT JOIN ( SELECT A.TABSCHEMA, A.TABNAME, B.COLNAME, A.TYPE FROM syscat.tabconst A ,SYSCAT.KEYCOLUSE B WHERE A.CONSTNAME = B.CONSTNAME AND A.TYPE IN ('P','U')) AS T2  ON T1.TABSCHEMA=T2.TABSCHEMA AND T1.TABNAME=T2.TABNAME AND T1.COLNAME=T2.COLNAME WHERE T1.TABNAME = {varTableId}"
            + " GROUP BY T1.COLNAME, T1.REMARKS, T1.TYPENAME, T1.LENGTH, T1.SCALE, T1.NULLS ORDER BY T1.COLNAME";

    
    /**
     * TABLE_DESC_QUERY_FOR_MYSQL
     */
    String TABLE_DESC_QUERY_FOR_MYSQL = "select "
            + " COLUMN_NAME as 'colId',"
            + " COLUMN_COMMENT as 'colName',"
            + " DATA_TYPE as 'dataType',"
            + " case when DATA_TYPE='bigint' or DATA_TYPE='tinyint' or DATA_TYPE='smallint' or DATA_TYPE='mediumint' or DATA_TYPE='int' then NUMERIC_PRECISION+1 when DATA_TYPE='decimal' or DATA_TYPE='double' or DATA_TYPE='float' then NUMERIC_PRECISION when DATA_TYPE='varchar' or DATA_TYPE='char' then CHARACTER_MAXIMUM_LENGTH else CHARACTER_OCTET_LENGTH end as 'dataLength',"
            + " NUMERIC_PRECISION as 'dataPrecision',"
            + " NUMERIC_SCALE as 'dataScale',"
            + " case when COLUMN_KEY='PRI' then '1' else '0' end as 'pkFlag',"
            + " case when IS_NULLABLE='NO' then '1' else '0' end as 'notNull',"
            + " COLUMN_DEFAULT as 'defaultValue'"
            + " from INFORMATION_SCHEMA.COLUMNS"
            + " where TABLE_NAME = {varTableId}";

    /**
     * TABLE_DESC_QUERY_FOR_ORACLE
     */
    String TABLE_DESC_QUERY_FOR_ORACLE = "select "
            + "USER_TAB_COLUMNS.COLUMN_NAME AS \"colId\", "
            + "substr(USER_COL_COMMENTS.COMMENTS, 1, 30) AS  \"colName\", "
            + "USER_TAB_COLUMNS.DATA_TYPE AS \"dataType\", "
            + "USER_TAB_COLUMNS.DATA_LENGTH AS \"dataLength\", "
            + "USER_TAB_COLUMNS.DATA_PRECISION AS \"dataPrecision\", "
            + "USER_TAB_COLUMNS.DATA_SCALE AS \"dataScale\", "
            + "case when count(USER_CONSTRAINTS.CONSTRAINT_NAME) > 0 then '1' else '0' end AS \"pkFlag\", "
            + "USER_TAB_COLUMNS.NULLABLE AS \"notNull\" "
          + "from USER_TAB_COLUMNS, USER_CONSTRAINTS, USER_CONS_COLUMNS, USER_COL_COMMENTS "
          + "where USER_TAB_COLUMNS.TABLE_NAME = 'EBAN' "
            + "and USER_TAB_COLUMNS.TABLE_NAME = USER_CONS_COLUMNS.TABLE_NAME(+) "
            + "and USER_TAB_COLUMNS.COLUMN_NAME = USER_CONS_COLUMNS.COLUMN_NAME(+) "
            + "and USER_CONS_COLUMNS.CONSTRAINT_NAME = USER_CONSTRAINTS.CONSTRAINT_NAME(+) "
            + "and USER_CONS_COLUMNS.TABLE_NAME = USER_CONSTRAINTS.TABLE_NAME(+) "
            + "and USER_TAB_COLUMNS.TABLE_NAME = USER_COL_COMMENTS.TABLE_NAME(+) "
            + "and USER_TAB_COLUMNS.COLUMN_NAME = USER_COL_COMMENTS.COLUMN_NAME(+) "
          + "group by "
            + "USER_TAB_COLUMNS.COLUMN_NAME, "
            + "USER_COL_COMMENTS.COMMENTS, "
            + "USER_TAB_COLUMNS.DATA_TYPE, "
            + "USER_TAB_COLUMNS.DATA_LENGTH, "
            + "USER_TAB_COLUMNS.DATA_PRECISION, "
            + "USER_TAB_COLUMNS.DATA_SCALE, "
            + "USER_TAB_COLUMNS.COLUMN_ID, "
            + "USER_TAB_COLUMNS.NULLABLE " 
          + "order by "
            + "USER_TAB_COLUMNS.COLUMN_ID ";

    
}
