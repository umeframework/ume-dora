/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.exp.db;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.umeframework.dora.ajax.impl.JsonRenderImpl;
import org.umeframework.dora.dao.impl.JdbcDaoImpl;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.log.impl.Log4j2Impl;
import org.umeframework.dora.tool.poi.CellWriter;
import org.umeframework.dora.tool.poi.ExcelAccessor;
import org.umeframework.dora.tool.poi.SimpleCellWriter;
import org.umeframework.dora.util.StringUtil;

/**
 * 1.根据Excel输入的Table列表信息及数据库连接信息，查询数据字典获取表定义的结构， 以此结构创建Excel的页并导出数据库中的既存数据至Excel文件。 2.根据上述模版结构编辑数据，将编辑后的数据生成可更新至数据库的SQL。
 * 
 * @author mayue
 */
public class Excel2DbExporter extends ExcelAccessor implements DbDescQueryStr {
    /**
     * 访问的Excel HSSF 对象
     */
    private Workbook book;
    /**
     * 访问的Excel文件名
     */
    private String excelFile;
    /**
     * 数据库种类
     */
    private String databaseType;
    /**
     * Dao实例
     */
    private JdbcDaoImpl dao;
    /**
     * 数据字典查询SQL语句
     */
    private String tableDescQueryStr;

    // private String tableDescQueryStr = "select" + " COLUMN_NAME as 'colId'," + " COLUMN_COMMENT as 'colName'," + " DATA_TYPE as 'dataType'," + " case" + " when DATA_TYPE='bigint' or DATA_TYPE='tinyint' or DATA_TYPE='smallint' or DATA_TYPE='mediumint' or DATA_TYPE='int' then NUMERIC_PRECISION+1" +
    // " when DATA_TYPE='decimal' or DATA_TYPE='double' or DATA_TYPE='float' then NUMERIC_PRECISION" + " when DATA_TYPE='varchar' or DATA_TYPE='char' then CHARACTER_MAXIMUM_LENGTH" + " else CHARACTER_OCTET_LENGTH" + " end as 'dataLength'," + " NUMERIC_PRECISION as 'dataPrecision'," + " NUMERIC_SCALE
    // as 'dataScale'," + " case when COLUMN_KEY='PRI' then '1' else '0' end as 'pkFlag'," + " case when IS_NULLABLE='NO' then '1' else '0' end as 'notNull'," + " COLUMN_DEFAULT as 'defaultValue'" + " from INFORMATION_SCHEMA.COLUMNS" + " where TABLE_NAME = {varTableId} AND TABLE_SCHEMA =
    // {varSchema};";
    /**
     * @return the tableDescQueryStr
     */
    public String getTableDescQueryStr() {
        if (tableDescQueryStr != null && !tableDescQueryStr.trim().equals("")) {
            return tableDescQueryStr;
        }

        String type = databaseType.toUpperCase();
        if (type.equals("ORACLE")) {
            return TABLE_DESC_QUERY_FOR_ORACLE;
        } else if (type.equals("DB2")) {
            return TABLE_DESC_QUERY_FOR_DB2;
        }
        return TABLE_DESC_QUERY_FOR_MYSQL;
    }

    /**
     * @return the currentTimestampStr
     */
    public String getCurrentTimestampStr() {
        String type = databaseType.toUpperCase();
        if (type.equals("ORACLE")) {
            return "current_timestamp";
        } else if (type.equals("DB2")) {
            return "current timestamp";
        }
        return "current_timestamp()";
    }

    /**
     * logger
     */
    private Logger logger = new Log4j2Impl();

    /**
     * 构造函数
     * 
     * @param excelFile
     * @param tableDescQueryStr
     * @throws Exception
     */
    public void init(String excelFile) throws Exception {
        this.excelFile = excelFile;
        File file = new File(excelFile);
        book = loadExcel(file);
        dao = new JdbcDaoImpl();
        DataSource ds = getDataSource();
        ((JdbcDaoImpl) dao).setDataSource(ds);
        ((JdbcDaoImpl) dao).setLogger(logger);
    }

    // /**
    // * 构造函数
    // */
    // public Excel2DbExporter() throws Exception {
    // }

    /**
     * expTableData
     * 
     * @param inputPath
     * @throws Throwable
     */
    public void expTableData(String inputPath) throws Throwable {
        for (File file : getFiles(inputPath)) {
            init(file.getAbsolutePath());
            this.createTableDescPages(true);
            System.out.println(file.getName() + " has been prcoessd.");
        }
    }

    /**
     * expTableDataGenInsertSql
     * 
     * @param inputPath
     * @throws Throwable
     */
    public void expTableDataGenInsertSql(String inputPath) throws Throwable {
        for (File file : getFiles(inputPath)) {
            init(file.getAbsolutePath());
            this.createInsertSqls();
            System.out.println(file.getName() + " has been prcoessd.");
        }
    }

    /**
     * expTableDataGenJson
     * 
     * @param inputPath
     * @param useCamelCase
     * @throws Throwable
     */
    public void expTableDataGenJson(String inputPath, Boolean useCamelCase) throws Throwable {
        for (File file : getFiles(inputPath)) {
            init(file.getAbsolutePath());
            this.createJsons(useCamelCase);
            System.out.println(file.getName() + " has been prcoessd.");
        }
    }

    /**
     * expTableDefine
     * 
     * @param inputPath
     * @throws Throwable
     */
    public void expTableDefine(String inputPath) throws Throwable {
        for (File file : getFiles(inputPath)) {
            init(file.getAbsolutePath());
            this.createTableDocPages();
            System.out.println(file.getName() + " has been prcoessd.");
        }
    }

    /**
     * @param inputPath
     * @return
     * @throws Exception
     */
    protected File[] getFiles(String inputPath) throws Exception {
        File[] files = new File(inputPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File e) {
                String name = e.getName().toLowerCase();
                return name.endsWith(".xls") || name.endsWith(".xlsx");
            }
        });
        return files;
    }

    /**
     * 创建数据Insert用的SQL语句
     * 
     * @throws Exception
     */
    protected void createInsertSqls() throws Exception {
        String lineSeparator = System.getProperty("line.separator");
        String fileName = excelFile.substring(0, excelFile.lastIndexOf("."));
        String charset = "UTF-8";
        OutputStreamWriter sqlFilePrinter = null;
        try {
            sqlFilePrinter = new OutputStreamWriter(new FileOutputStream(fileName + ".sql"), charset);
            for (int i = 0; i < book.getNumberOfSheets(); i++) {
                Sheet sheet = book.getSheetAt(i);
                if (sheet.getSheetName().startsWith("#")) {
                    continue;
                }
                List<String> sqlList = createTableInsertSQL(this.excelFile, sheet.getSheetName());

                for (String e : sqlList) {
                    sqlFilePrinter.write(e);
                    sqlFilePrinter.write(lineSeparator);
                }
                sqlFilePrinter.write(lineSeparator);
                sqlFilePrinter.flush();
            }
        } finally {
            sqlFilePrinter.close();
        }
    }

    /**
     * 创建数据Insert用的SQL语句
     * 
     * @throws Exception
     */
    protected void createJsons(Boolean useCamelCase) throws Exception {
        String lineSeparator = System.getProperty("line.separator");
        String fileName = excelFile.substring(0, excelFile.lastIndexOf("."));
        String charset = "UTF-8";
        OutputStreamWriter jsonFilePrinter = null;
        try {
            jsonFilePrinter = new OutputStreamWriter(new FileOutputStream(fileName + ".json"), charset);
            for (int i = 0; i < book.getNumberOfSheets(); i++) {
                Sheet sheet = book.getSheetAt(i);
                if (sheet.getSheetName().startsWith("#")) {
                    continue;
                }
                List<String> list = new ArrayList<String>();

                if (useCamelCase == null || useCamelCase) {
                    list.add("// [" + toCamelCase(sheet.getSheetName()) + "]");
                    List<String> l = createTableJsonData(this.excelFile, sheet.getSheetName(), true);
                    list.addAll(l);
                }
                if (useCamelCase == null || !useCamelCase) {
                    list.add("// [" + sheet.getSheetName() + "]");
                    List<String> l = createTableJsonData(this.excelFile, sheet.getSheetName(), false);
                    list.addAll(l);
                }
                for (String e : list) {
                    jsonFilePrinter.write(e);
                    jsonFilePrinter.write(lineSeparator);
                }
                jsonFilePrinter.write(lineSeparator);
                jsonFilePrinter.flush();
            }
        } finally {
            jsonFilePrinter.close();
        }
    }

    /**
     * 创建数据Insert用的SQL语句
     * 
     * @throws Exception
     */
    protected List<String> createTableInsertSQL(String excelFile, String sheetName) throws Exception {
        List<String> sqls = new ArrayList<String>();
        Sheet sheet = book.getSheet(sheetName);

        int x = 1;
        int y = 0;
        String value = getCellValueAsString(sheet, x, y);
        while (!isEmpty(value)) {
            y++;
            value = getCellValueAsString(sheet, x, y);
        }

        int[] targetColIndexes = new int[y];
        for (int c = 0; c < targetColIndexes.length; c++) {
            targetColIndexes[c] = c;
        }

        StringBuilder sql = new StringBuilder();
        String table = sheet.getSheetName();
        sql.append("DELETE FROM " + table + " ;");
        sqls.add(sql.toString());

        List<List<Object>> rows = super.readSheetAsObjectList(new File(excelFile), sheet.getSheetName(), targetColIndexes, 0, 65535);
        List<Object> cols = rows.get(1);
        List<Object> types = rows.get(2);
        for (int rowIndex = 3; rowIndex < rows.size(); rowIndex++) {
            List<Object> datas = rows.get(rowIndex);
            sql = new StringBuilder();
            sql.append("INSERT INTO " + table + "(");
            for (int k = 0; k < cols.size(); k++) {
                if (k > 0) {
                    sql.append(",");
                }
                sql.append(cols.get(k));
            }
            sql.append(") VALUES (");
            for (int k = 0; k < datas.size(); k++) {
                if (k > 0) {
                    sql.append(",");
                }
                sql.append(convertInsertValue(cols.get(k).toString(), types.get(k).toString(), datas.get(k) == null ? "" : datas.get(k).toString()));
            }
            sql.append(") ;");
            sqls.add(sql.toString());
        }
        System.out.println("Generate SQL for " + table);
        return sqls;
    }

    /**
     * convertInsertValue
     * 
     * @param type
     * @param value
     * @return
     */
    protected String convertInsertValue(String col, String typeStr, String value) {
        String result = null;
        typeStr = typeStr.toUpperCase();
        typeStr = typeStr.contains("(") ? typeStr.substring(0, typeStr.indexOf("(")) : typeStr;
        if (typeStr.equals("CHARACTER") || typeStr.equals("CHAR") || typeStr.equals("VARCHAR") || typeStr.equals("VARCHAR2") || typeStr.equals("LONGVARCHAR")) {
            if (isEmpty(value)) {
                result = "null";
            } else if (value.contains("'")) {
                value = value.replace("'", "''");
                result = "'" + value + "'";
            } else {
                result = "'" + value + "'";
            }
        } else if (typeStr.equals("TIMESTAMP") || typeStr.equals("TIME") || typeStr.equals("DATE") || typeStr.equals("DATETIME")) {
            if (col.toUpperCase().equals("UPDATE_DATETIME")) {
                result = getCurrentTimestampStr();
            } else if (col.toUpperCase().equals("CREATE_DATETIME") && isEmpty(value)) {
                result = getCurrentTimestampStr();
            }
        } else {
            result = value.toString();
        }
        return isEmpty(result) ? "null" : result;
    }

    /**
     * 创建数据Insert用的SQL语句
     * 
     * @throws Exception
     */
    protected List<String> createTableJsonData(String excelFile, String sheetName, boolean useCamelCase) throws Exception {
        List<String> jsons = new ArrayList<String>();
        Sheet sheet = book.getSheet(sheetName);
        int x = 1;
        int y = 0;
        String value = getCellValueAsString(sheet, x, y);
        while (!isEmpty(value)) {
            y++;
            value = getCellValueAsString(sheet, x, y);
        }
        int[] targetColIndexes = new int[y];
        for (int c = 0; c < targetColIndexes.length; c++) {
            targetColIndexes[c] = c;
        }
        String table = sheet.getSheetName();

        List<List<Object>> rows = super.readSheetAsObjectList(new File(excelFile), sheet.getSheetName(), targetColIndexes, 0, 65535);
        JsonRenderImpl render = new JsonRenderImpl();

        List<Object> cols = rows.get(1);
        List<Object> types = rows.get(2);
        for (int rowIndex = 3; rowIndex < rows.size(); rowIndex++) {
            List<Object> datas = rows.get(rowIndex);
            LinkedHashMap<String, Object> bean = new LinkedHashMap<String, Object>();
            for (int k = 0; k < datas.size(); k++) {
                String colName = cols.get(k).toString();
                String dataStr = datas.get(k).toString();
                Object dataObj = null;
                String typeStr = types.get(k).toString().toUpperCase();
                typeStr = typeStr.contains("(") ? typeStr.substring(0, typeStr.indexOf("(")) : typeStr;

                if (StringUtil.isEmpty(dataStr)) {
                    dataObj = null;
                } else if (typeStr.equals("CHARACTER") || typeStr.equals("CHAR") || typeStr.equals("VARCHAR") || typeStr.equals("VARCHAR2") || typeStr.equals("LONGVARCHAR") || typeStr.equals("TEXT") || typeStr.equals("TINYTEXT")) {
                    dataObj = dataStr.contains("'") ? dataStr.replace("'", "''") : dataStr;
                } else if (typeStr.equals("TIMESTAMP") || typeStr.equals("DATETIME")) {
                    dataObj = Timestamp.valueOf(dataStr);
                } else if (typeStr.equals("TIME")) {
                    dataObj = Time.valueOf(dataStr);
                } else if (typeStr.equals("DATE")) {
                    dataObj = Date.valueOf(dataStr);
                } else if (typeStr.equals("TINYINT") || typeStr.equals("SMALLINT") || typeStr.equals("MEDIUMINT") || typeStr.equals("INTEGER") || typeStr.equals("INT") || typeStr.equals("BIGINT")) {
                    dataObj = Long.valueOf(dataStr);
                } else if (typeStr.equals("FLOAT") || typeStr.equals("DOUBLE") || typeStr.equals("DECIMAL")) {
                    dataObj = Double.valueOf(dataStr);
                } else if (typeStr.equals("BOOLEAN")) {
                    dataObj = Boolean.valueOf(dataStr);
                } else {
                    dataObj = dataStr;
                }

                if (useCamelCase) {
                    colName = toCamelCase(colName);
                }
                bean.put(colName, dataObj);
            }
            String json = render.render(bean);
            jsons.add(json);
        }
        System.out.println("Generate json data for " + table);
        return jsons;
    }

    /**
     * toCamelCase
     * 
     * @param name
     * @return
     */
    protected String toCamelCase(String name) {
        if (name.contains("_")) {
            String[] words = name.split("_");
            StringBuilder newName = new StringBuilder();
            for (int p = 0; p < words.length; p++) {
                if (p == 0) {
                    newName.append(words[p].toLowerCase());
                } else {
                    newName.append(words[p].toUpperCase().charAt(0) + words[p].toLowerCase().substring(1));
                }
            }
            name = newName.toString();
        } else {
            name = name.toLowerCase();
        }
        return name;
    }

    /**
     * 创建表定义的Excel页
     * 
     * @throws Throwable
     */
    protected void createTableDocPages() throws Throwable {
        List<String[]> tblConfigInfo = getTblConfigInfo();
        for (String[] e : tblConfigInfo) {
            String schemaName = e[0];
            String tableName = e[1];
            Map<String, String> param = new HashMap<String, String>();
            if (schemaName != null && !schemaName.trim().equals("") && !schemaName.trim().equals("*")) {
                param.put("varSchema", schemaName);
            }
            param.put("varTableId", tableName);
            @SuppressWarnings("rawtypes")
            List<LinkedHashMap> tableDesc = dao.queryForObjectList(getTableDescQueryStr(), param, LinkedHashMap.class);
            if (tableDesc == null || tableDesc.size() == 0) {
                System.out.println("No found table [" + tableName + "] in schema [" + schemaName + "]!");
                continue;
            }
            tableName = tableName.length() > 30 ? tableName.substring(0, 29) : tableName;
            createTableDocPage(tableName, tableDesc);
            System.out.println("Table [" + tableName + "] information create.");
        }
    }

    /**
     * 创建表定义的Excel页
     * 
     * @param tableName
     * @param tableDesc
     */
    protected List<Object> createTitleRow1(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc) {
        return createList(new Object[] { "表描述", "", tableName, "", "", "", "TABLE SPACE", "", "", "", "", "", "" });
    }

    protected List<Object> createTitleRow2(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc) {
        return createList(new Object[] { "表名称", "", tableName, "", "", "", "TABLE SPACE (IDX)", "", "", "", "", "", "" });
    }

    protected List<Object> createTitleRow3(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc) {
        return createList(new Object[] { "分表设定", "", "", "", "", "", "TABLE SPACE (LOB)", "", "", "", "", "", "" });
    }

    protected List<Object> createTitleRow4(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc) {
        return createList(new Object[] { "", "", "", "", "", "", "", "", "", "", "", "", "" });
    }

    protected List<Object> createTitleRow5(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc) {
        return createList(new Object[] { "序号", "项目名", "项目ID", "数据类型", "长度", "主键", "非空", "默认值", "固定值", "最小值", "最大值", "格式", "备注" });
    }

    protected List<Object> createDataRow(String tableName, int colIndex, LinkedHashMap<?, ?> e) {
        Object colName = e.get("colName");
        Object colId = e.get("colId");
        if (isEmpty(String.valueOf(colName))) {
            colName = colId;
        }
        String remark = String.valueOf(e.get("dataType"));
        String type = this.getTextDescFromType(remark);
        String length = "";
        if (databaseType.equalsIgnoreCase("ORACLE")) {
            if (e.get("dataPrecision") != null && e.get("dataScale") != null) {
                if (e.get("dataScale").toString().equals("0")) {
                    length = e.get("dataPrecision").toString();
                    type = this.getTextDescFromType("INTEGER");
                } else {
                    length = e.get("dataPrecision") + "," + e.get("dataScale");
                }
            } else if (e.get("dataPrecision") != null && e.get("dataScale") == null) {
                length = e.get("dataPrecision").toString();
                type = this.getTextDescFromType("INTEGER");
            } else if (e.get("dataLength") != null) {
                length = e.get("dataLength").toString();
            }
        } else {
            if (e.get("dataPrecision") != null && e.get("dataScale") != null) {
                length = e.get("dataPrecision") + "," + e.get("dataScale");
            } else if (e.get("dataPrecision") != null) {
                length = e.get("dataPrecision").toString();
            } else if (e.get("dataLength") != null) {
                length = e.get("dataLength").toString();
            }
        }
        String pkFlag = e.get("pkFlag").toString().equals("1") ? "○" : "";
        String notNull = e.get("notNull").toString().equals("1") || e.get("notNull").toString().toUpperCase().equals("Y") ? "○" : "";

        return createList(new Object[] { colIndex, colName, colId, type, length, pkFlag, notNull, "", "", "", "", "", remark });
    }

    protected void createTableDocPage(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        List<Object> row1 = createTitleRow1(tableName, tableDesc);
        List<Object> row2 = createTitleRow2(tableName, tableDesc);
        List<Object> row3 = createTitleRow3(tableName, tableDesc);
        List<Object> row4 = createTitleRow4(tableName, tableDesc);
        List<Object> row5 = createTitleRow5(tableName, tableDesc);
        if (row1 != null) {
            rows.add(row1);
        }
        if (row2 != null) {
            rows.add(row2);
        }
        if (row3 != null) {
            rows.add(row3);
        }
        if (row4 != null) {
            rows.add(row4);
        }
        if (row5 != null) {
            rows.add(row5);
        }

        int colIndex = 1;
        for (LinkedHashMap<?, ?> e : tableDesc) {
            List<Object> row = createDataRow(tableName, colIndex, e);// new Object[] { colIndex, colName, colId, type, length, pkFlag, notNull, "", "", "", "", "", remark });
            colIndex++;
            rows.add(row);
        }

        super.createSheet(book, excelFile, tableName, rows, new CellWriter<Object>() {
            // Title cell style
            CellStyle titleCellstyle = book.createCellStyle();

            @SuppressWarnings("deprecation")
            @Override
            public void write(Cell cell, Object value) {
                int rowIndex = cell.getRowIndex();
                if (rowIndex < 5) {
                    titleCellstyle.setBorderTop(CellStyle.BORDER_THIN);
                    titleCellstyle.setBorderBottom(CellStyle.BORDER_THIN);
                    titleCellstyle.setBorderLeft(CellStyle.BORDER_THIN);
                    titleCellstyle.setBorderRight(CellStyle.BORDER_THIN);
                    titleCellstyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
                    titleCellstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    cell.setCellStyle(titleCellstyle);
                }
                if (value instanceof Number) {
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Double.valueOf(String.valueOf(value)));
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(String.valueOf(value));
                }
            }
        });
    }

    /**
     * createList
     * 
     * @param arr
     * @return
     */
    protected List<Object> createList(Object[] arr) {
        List<Object> list = new ArrayList<Object>(arr.length);
        for (Object e : arr) {
            list.add(e);
        }
        return list;
    }

    /**
     * 导出表数据
     * 
     * @throws Throwable
     */
    protected void createTableDescPages(boolean selectExistData) throws Throwable {
        List<String[]> tblConfigInfo = getTblConfigInfo();
        for (String[] e : tblConfigInfo) {

            String schemaName = e[0];
            String tableName = e[1];
            String whereCondition = e[2];
            Map<String, String> param = new HashMap<String, String>();
            param.put("varSchema", schemaName);
            param.put("varTableId", tableName);

            System.out.println("Start create table structure template of " + tableName);
            @SuppressWarnings("rawtypes")
            List<LinkedHashMap> tableDesc = dao.queryForObjectList(getTableDescQueryStr(), param, LinkedHashMap.class);

            System.out.println("Start extract table data of " + tableName + (isEmpty(whereCondition) ? "" : ",condition by " + whereCondition));
            String tableDataQueryStr = createTableDataQueryStr(tableDesc, schemaName, tableName, whereCondition);
            @SuppressWarnings("rawtypes")
            List<LinkedHashMap> tableData = dao.queryForObjectList(tableDataQueryStr, null, LinkedHashMap.class);
            System.out.println("Extracted " + (tableData != null ? tableData.size() : "0") + " record of " + tableName);

            tableName = tableName.length() > 30 ? tableName.substring(0, 29) : tableName;
            createTableDescPage(tableName, tableDesc, tableData);
            System.out.println("Finish process of " + tableName);
        }
    }

    /**
     * 创建导出表数据的Excel页
     * 
     * @param tableName
     * @param tableDesc
     */
    protected void createTableDescPage(String tableName, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableDesc, @SuppressWarnings("rawtypes") List<LinkedHashMap> tableData) {

        List<List<Object>> rows = new ArrayList<List<Object>>();
        List<Object> row1 = new ArrayList<Object>();
        List<Object> row2 = new ArrayList<Object>();
        List<Object> row3 = new ArrayList<Object>();

        int colIndex = 0;
        for (LinkedHashMap<?, ?> e : tableDesc) {
            int pkFlag = e.get("pkFlag").toString().equals("1") ? 1 : 0;
            int notNull = e.get("notNull").toString().equals("1") ? 1 : 0;

            Map<String, Object> map = new HashMap<String, Object>();
            if (super.isEmpty(String.valueOf(e.get("colName")))) {
                map.put("value", "[" + e.get("colId") + "]");
            } else {
                map.put("value", e.get("colName"));
            }
            map.put("rowIndex", 0);
            map.put("colIndex", colIndex);
            map.put("pkFlag", pkFlag);
            map.put("notNull", notNull);
            row1.add(map);

            map = new HashMap<String, Object>();
            map.put("value", e.get("colId"));
            map.put("rowIndex", 1);
            map.put("colIndex", colIndex);
            map.put("pkFlag", pkFlag);
            map.put("notNull", notNull);
            row2.add(map);

            String type = String.valueOf(e.get("dataType"));
            String length = "";
            if (e.get("dataPrecision") != null && e.get("dataScale") != null) {
                length = "(" + e.get("dataPrecision") + "," + e.get("dataScale") + ")";
            } else if (e.get("dataPrecision") != null) {
                length = "(" + e.get("dataPrecision") + ")";
            } else if (e.get("dataLength") != null) {
                length = "(" + e.get("dataLength") + ")";
            }
            map = new HashMap<String, Object>();
            map.put("value", type + length);
            map.put("rowIndex", 2);
            map.put("colIndex", colIndex);
            map.put("pkFlag", pkFlag);
            map.put("notNull", notNull);
            row3.add(map);

            colIndex++;
        }
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        if (tableData != null) {
            for (LinkedHashMap<?, ?> e : tableData) {
                List<Object> row = new ArrayList<Object>();
                Iterator<?> itr = e.values().iterator();
                while (itr.hasNext()) {
                    Object ee = itr.next();
                    ee = ee == null ? "" : ee;
                    row.add(ee);
                }
                rows.add(row);
            }
        }

        super.createSheet(excelFile, tableName, rows, new CellWriter<Object>() {
            @SuppressWarnings({ "rawtypes", "deprecation" })
            @Override
            public void write(Cell cell, Object value) {
                if (value instanceof Map) {
                    Object actValue = ((Map) value).get("value");
                    new SimpleCellWriter().write(cell, actValue);

                    int rowIndex = (Integer) ((Map) value).get("rowIndex");
                    int pkFlag = (Integer) ((Map) value).get("pkFlag");
                    int notNull = (Integer) ((Map) value).get("notNull");

                    CellStyle cellstyle = cell.getCellStyle();
                    if (rowIndex < 3) {
                        if (pkFlag == 1) {
                            cellstyle.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
                        } else if (notNull == 1) {
                            cellstyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
                        } else {
                            cellstyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
                        }
                        cellstyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        cell.setCellStyle(cellstyle);

                    }
                } else {
                    new SimpleCellWriter().write(cell, value);
                }
            }
        });
    }

    /**
     * 读取表名列表
     * 
     * @return
     */
    protected List<String[]> getTblConfigInfo() {
        List<String[]> configs = new ArrayList<String[]>();
        Sheet sheet = book.getSheet("#TBLCONFIG");
        Coordinate coordinate = lookupDataCell(sheet, "SET SCHEMA");
        int row = coordinate.x + 1;
        int col = coordinate.y;
        String schema = getCellValueAsString(sheet, row, col);
        String table = getCellValueAsString(sheet, row, col + 1);
        String condition = getCellValueAsString(sheet, row, col + 2);
        while (!isEmpty(schema)) {
            String[] config = new String[] { schema.trim(), table.trim(), condition.trim() };
            configs.add(config);
            row++;
            schema = getCellValueAsString(sheet, row, col);
            table = getCellValueAsString(sheet, row, col + 1);
            condition = getCellValueAsString(sheet, row, col + 2);
        }
        return configs;
    }

    /**
     * 创建表数据查询SQL语句
     * 
     * @param desc
     * @param schemaName
     * @param tableName
     * @param whereCondition
     * @return
     */
    protected String createTableDataQueryStr(@SuppressWarnings("rawtypes") List<LinkedHashMap> desc, String schemaName, String tableName, String whereCondition) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        for (int i = 0; i < desc.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(desc.get(i).get("colId"));
        }
        builder.append(" FROM ");
        builder.append(schemaName);
        builder.append(".");
        builder.append(tableName);
        if (whereCondition != null && !whereCondition.trim().equals("")) {
            builder.append(" WHERE ");
            builder.append(whereCondition);
        }
        // builder.append(" ORDER BY UPDATE_DATETIME");
        return builder.toString();
    }

    /**
     * 取得数据库数据源
     * 
     * @return
     * @throws Exception
     */
    protected DataSource getDataSource() throws Exception {
        Sheet sheet = book.getSheet("#DBCONFIG");
        Coordinate coordinate = lookupDataCell(sheet, "DATABASE DRIVER");
        String driver = getCellValueAsString(sheet, coordinate.x, coordinate.y + 1);
        coordinate = lookupDataCell(sheet, "DATABASE URL");
        String url = getCellValueAsString(sheet, coordinate.x, coordinate.y + 1);
        coordinate = lookupDataCell(sheet, "DATABASE USER");
        String user = getCellValueAsString(sheet, coordinate.x, coordinate.y + 1);
        coordinate = lookupDataCell(sheet, "DATABASE PASSWORD");
        String password = getCellValueAsString(sheet, coordinate.x, coordinate.y + 1);

        Properties dsProp = new Properties();
        dsProp.setProperty("driverClassName", driver);
        dsProp.setProperty("url", url);
        dsProp.setProperty("username", user);
        dsProp.setProperty("password", password);

        DataSource ds = BasicDataSourceFactory.createDataSource(dsProp);
        System.out.println("Connected to:" + url);
        return ds;
    }

    /**
     * @return the databaseType
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * @param databaseType
     *            the databaseType to set
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * @param tableDescQueryStr
     *            the tableDescQueryStr to set
     */
    public void setTableDescQueryStr(String tableDescQueryStr) {
        this.tableDescQueryStr = tableDescQueryStr;
    }

    // /**
    // * @param tableDescQueryStr
    // * the tableDescQueryStr to set
    // */
    // public void setTableDescQueryStr(String tableDescQueryStr) {
    // this.tableDescQueryStr = tableDescQueryStr;
    // }
    //
    // /**
    // * @param currentTimestampStr
    // * the currentTimestampStr to set
    // */
    // public void setCurrentTimestampStr(String currentTimestampStr) {
    // this.currentTimestampStr = currentTimestampStr;
    // }

}
