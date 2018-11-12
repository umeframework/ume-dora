/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ExcelUtil
 */
public class ExcelAccessor {
    /**
     * position coordinate of cell
     */
    public static class Coordinate {
        public int x;
        public int y;

        /**
         * Coordinate
         * 
         * @param x
         * @param y
         */
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * LINE_SEPARATOR
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * defaultCellReader
     */
    private CellReader<Object> defaultCellReader = new SimpleCellReader();
    /**
     * defaultCellWriter
     */
    private CellWriter<Object> defaultCellWriter = new SimpleCellWriter();

    /**
     * readSheetAsObjectList
     * 
     * @param <E>
     * @param excelFile
     * @param sheetName
     * @param targetColIndexes
     * @param startRowNum
     * @param endRowNum
     * @param cellReader
     * @return
     * @throws Exception
     */
    public <E> List<List<E>> readSheetAsObjectList(File excelFile, String sheetName, int[] targetColIndexes, int startRowNum, int endRowNum, CellReader<E> cellReader) throws Exception {
        endRowNum = endRowNum < 0 ? Integer.MAX_VALUE : endRowNum;
        List<List<E>> result = new ArrayList<List<E>>();
        Workbook book = null;
        try {
            // ファイルをオープンします
            // fis = new FileInputStream(excelFile);
            book = loadExcel(excelFile);

            boolean found = false;
            for (int sheetIndex = 0; sheetIndex < book.getNumberOfSheets(); sheetIndex++) {
                // シート名を取得します
                if (!book.getSheetName(sheetIndex).equals(sheetName)) {
                    continue;
                }
                found = true;
                // データシートを処理します
                Sheet sheet = book.getSheetAt(sheetIndex);
                ExcelAccessor.Coordinate range = getSheetSize(sheet);
                for (int i = startRowNum; i <= endRowNum && i <= range.x; i++) {
                    List<E> row = new ArrayList<E>(targetColIndexes.length);
                    for (int j = 0; j < targetColIndexes.length; j++) {
                        Cell cell = getCell(sheet, i, targetColIndexes[j]);
                        E value = cellReader.read(cell);
                        row.add(value);
                    }

                    if (isEndRow(row)) {
                        break;
                    }
                    result.add(row);
                }
            }
            if (!found) {
                throw new Exception("Error: No found sheet of " + sheetName);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    /**
     * readSheetAsMapList
     * 
     * @param excelFile
     * @param sheetName
     * @param targetColumns
     * @param startRowNum
     * @param cellReader
     * @return
     * @throws Exception
     */
    public <E> List<Map<String, E>> readSheetAsMapList(File excelFile, String sheetName, String[] targetColumns, int startRowNum, int endRowNum, CellReader<E> cellReader) throws Exception {
        endRowNum = endRowNum < 0 ? Integer.MAX_VALUE : endRowNum;
        List<Map<String, E>> result = new ArrayList<Map<String, E>>();
        // FileInputStream fis = null;
        Workbook book = null;
        try {
            // ファイルをオープンします
            // fis = new FileInputStream(excelFile);
            book = loadExcel(excelFile);

            boolean found = false;
            for (int sheetIndex = 0; sheetIndex < book.getNumberOfSheets(); sheetIndex++) {
                // シート名を取得します
                if (!book.getSheetName(sheetIndex).equals(sheetName)) {
                    continue;
                }

                found = true;
                // データシートを処理します
                Sheet sheet = book.getSheetAt(sheetIndex);

                int[] positions = new int[targetColumns.length];
                for (int i = 0; i < targetColumns.length; i++) {
                    positions[i] = lookupDataCell(sheet, targetColumns[i]).y;
                }

                // ソートの範囲を取得します
                ExcelAccessor.Coordinate range = getSheetSize(sheet);
                for (int i = startRowNum; i <= endRowNum && i <= range.x; i++) {
                    LinkedHashMap<String, E> row = new LinkedHashMap<String, E>(targetColumns.length);
                    for (int j = 0; j < positions.length; j++) {
                        String key = targetColumns[j];
                        Cell cell = getCell(sheet, i, positions[j]);
                        E value = cellReader.read(cell);
                        row.put(key, value);
                    }
                    if (isEndRow(row)) {
                        break;
                    }
                    result.add(row);
                }
            }

            if (!found) {
                throw new Exception("Error: No found sheet of " + sheetName);
            }
        } finally {
        }
        return result;
    }

    /**
     * readSheetAsMapList
     * 
     * @param excelFile
     * @param sheetName
     * @param targetColumns
     * @param startRowNum
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> readSheetAsMapList(File excelFile, String sheetName, String[] targetColumns, int startRowNum, int endRowNum) throws Exception {
        return readSheetAsMapList(excelFile, sheetName, targetColumns, startRowNum, endRowNum, new SimpleCellReader());
    }

    /**
     * readSheetAsObjectList
     * 
     * @param excelFile
     * @param sheetName
     * @param startRowNum
     * @param targetColumns
     * @return
     * @throws Exception
     */
    public List<List<Object>> readSheetAsObjectList(File excelFile, String sheetName, String[] targetColumns, int startRowNum, int endRowNum) throws Exception {
        List<Map<String, Object>> mapListData = readSheetAsMapList(excelFile, sheetName, targetColumns, startRowNum, endRowNum);
        List<List<Object>> result = new ArrayList<List<Object>>();
        for (Map<String, Object> mapData : mapListData) {
            List<Object> objList = new ArrayList<Object>(mapData.size());
            objList.addAll(mapData.values());
            result.add(objList);
        }
        return result;
    }

    /**
     * readSheetAsObjectList
     * 
     * @param excelFile
     * @param sheetName
     * @param targetColIndexes
     * @param startRowNum
     * @param endRowNum
     * @return
     * @throws Exception
     */
    public List<List<Object>> readSheetAsObjectList(File excelFile, String sheetName, int[] targetColIndexes, int startRowNum, int endRowNum) throws Exception {
        return readSheetAsObjectList(excelFile, sheetName, targetColIndexes, startRowNum, endRowNum, new SimpleCellReader());
    }

    /**
     * @param strDate
     * @return
     */
    protected java.sql.Date parseSqlDate(String strDate) {
        String[] permitFormats = { "MM/dd/yyyy", "dd/MM/yyyy", "yyyy/MM/dd" };
        java.util.Date utilDate = null;
        for (String permitFormat : permitFormats) {
            SimpleDateFormat sdf = new SimpleDateFormat(permitFormat);
            try {
                utilDate = sdf.parse(strDate);
            } catch (ParseException e) {
                utilDate = null;
            }
        }
        if (utilDate == null) {
            throw new RuntimeException("Parse exception during parsing data string: " + strDate);
        }
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    /**
     * シートの範囲を計算します。
     * 
     * @param sheet
     * @return
     */
    protected Coordinate getSheetSize(Sheet sheet) {
        int maxRow = sheet.getLastRowNum();
        int maxCol = 0;
        Iterator<Row> rows = sheet.iterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            int curColNum = row.getLastCellNum();
            if (curColNum > maxCol) {
                maxCol = curColNum;
            }
        }
        return new Coordinate(maxRow, maxCol);
    }

    /**
     * 指定の行番と列番でCellの値を取得します。
     * 
     * @param sheet
     * @param rowIdx
     * @param columnIdx
     * @return
     */
    protected String getCellStringValue(Sheet sheet, int rowNum, int colNum) {
        String cellValue = null;
        Row row = sheet.getRow(rowNum);
        if (row != null) {
            Cell cell = CellUtil.getCell(row, colNum);
            if (cell != null) {
                cellValue = cell.getStringCellValue();
            }
        }
        return cellValue;
    }

    /**
     * 指定の行番と列番でCellの値を取得します。
     * 
     * @param sheet
     * @param rowIdx
     * @param columnIdx
     * @return
     */
    protected java.util.Date getCellDateValue(Sheet sheet, int rowNum, int colNum) {
        java.util.Date cellValue = null;
        Row row = sheet.getRow(rowNum);
        if (row != null) {
            Cell cell = CellUtil.getCell(row, colNum);
            if (cell != null) {
                cellValue = cell.getDateCellValue();
            }
        }
        return cellValue;
    }

    /**
     * 指定の行番と列番でCellの値を取得します。
     * 
     * @param sheet
     * @param rowIdx
     * @param columnIdx
     * @return
     */
    protected BigDecimal getCellNumericValue(Sheet sheet, int rowNum, int colNum) {
        BigDecimal cellValue = null;
        Row row = sheet.getRow(rowNum);
        if (row != null) {
            Cell cell = CellUtil.getCell(row, colNum);
            if (cell != null) {
                cellValue = new BigDecimal(cell.getNumericCellValue());
            }
        }
        return cellValue;
    }

    /**
     * 指定の行番と列番でCellのコメントを取得します。 行番と列番のindexは0から開始します。
     * 
     * @param sheet
     * @param rowIdx
     * @param columnIdx
     * @return
     */
    protected String getCellCommentAsString(Cell cell) {
        String cellComment = null;
        if (cell != null) {
            Comment c = cell.getCellComment();
            cellComment = c == null ? "" : cell.getCellComment().getString().getString();
        }
        return cellComment;
    }

    /**
     * @param value
     * @return
     */
    protected boolean isEmpty(String value) {
        if (null == value || "".equals(value.trim()) || "null".equals(value.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 指定のセル値でセルの行列番号を取得します。
     * 
     * @param sheet
     * @param keyword
     * @return
     */
    protected Coordinate lookupDataCell(Sheet sheet, String keyword) {
        Iterator<Row> rows = sheet.iterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                String value = String.valueOf(defaultCellReader.read(cell));
                if (keyword.equals(value)) {
                    return new Coordinate(cell.getRowIndex(), cell.getColumnIndex());
                }
            }
        }
        System.out.println("Warning: No found value[" + keyword + "] in sheet[" + sheet.getSheetName() + "]");
        return null;
    }

    /**
     * 指定の行番と列番でCellの値を取得します。
     * 
     * @param sheet
     * @param rowIdx
     * @param columnIdx
     * @return
     */
    protected Cell getCell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row != null) {
            return CellUtil.getCell(row, colNum);
        }
        return null;
    }

    /**
     * @param excelname
     * @param sheetname
     * @param rowMapValues
     */
    public void createSheetWithMapList(String excelname, String sheetname, List<Map<String, Object>> rowMapValues) {
        createSheetWithMapList(excelname, sheetname, rowMapValues, null);
    }

    /**
     * @param excelname
     * @param sheetname
     * @param rowMapValues
     * @param cellWriter
     */
    public void createSheetWithMapList(String excelname, String sheetname, List<Map<String, Object>> rowMapValues, CellWriter<Object> cellWriter) {
        List<List<Object>> rowValues = new ArrayList<List<Object>>(rowMapValues.size() + 1);

        // List<LinkedHashMap<String, Object>>をList<List<Object>>型に変換します
        for (int i = 0; i < rowMapValues.size(); i++) {
            Map<String, Object> map = rowMapValues.get(i);
            if (i == 0) {
                // 一番目のタイトル行を構築します
                List<Object> titles = new ArrayList<Object>(map.size());
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    titles.add(entry.getKey());
                }
                rowValues.add(titles);
            }
            List<Object> values = new ArrayList<Object>(map.size());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                values.add(entry.getValue());
            }
            rowValues.add(values);
        }
        // 引数はList<List<Object>>型の処理を呼出します
        createSheet(excelname, sheetname, rowValues, cellWriter);
    }

    /**
     * @param excelname
     * @param sheetname
     * @param rowValues
     */
    public void createSheet(String excelname, String sheetname, List<List<Object>> rowValues) {
        createSheet(excelname, sheetname, rowValues, null);
    }

    /**
     * createSheet
     * 
     * @param excelname
     * @param sheetname
     * @param rowValues
     * @param cellWriter
     */
    public void createSheet(String excelFile, String sheetname, List<List<Object>> rowValues, CellWriter<Object> cellWriter) {
        createSheet(null, excelFile, sheetname, rowValues, cellWriter);
    }
    
    /**
     * createSheet
     * 
     * @param book
     * @param excelFile
     * @param sheetname
     * @param rowValues
     * @param cellWriter
     */
    @SuppressWarnings("deprecation")
    public void createSheet(Workbook book, String excelFile, String sheetname, List<List<Object>> rowValues, CellWriter<Object> cellWriter) {
        FileOutputStream fos = null;
        if (cellWriter == null) {
            cellWriter = defaultCellWriter;
        }
        try {
            if (book == null) {
                book = loadExcel(excelFile);
            }
            for (int i = 0; i < book.getNumberOfSheets(); i++) {
                String name = book.getSheetName(i);
                if (name.equals(sheetname)) {
                    System.out.println("Warning! Input sheet is already existed.");
                    book.removeSheetAt(i);
                    System.out.println("Remove exist sheet.");
                    break;
                } else {
                    continue;
                }
            }

            book.createSheet(sheetname);
            System.gc();

            Sheet sheet = book.getSheet(sheetname);

            CellStyle cellstyle = book.createCellStyle();
            for (int i = 0; i < rowValues.size(); i++) {
                sheet.createRow(i);
                Row row = sheet.getRow(i);
                List<Object> cellValues = rowValues.get(i);

                for (int j = 0; j < cellValues.size(); j++) {
                    Object value = cellValues.get(j);
                    Cell cell = row.createCell(j);
                    cellstyle.setBorderTop(CellStyle.BORDER_THIN);
                    cellstyle.setBorderBottom(CellStyle.BORDER_THIN);
                    cellstyle.setBorderLeft(CellStyle.BORDER_THIN);
                    cellstyle.setBorderRight(CellStyle.BORDER_THIN);
                    cell.setCellStyle(cellstyle);
                    cellWriter.write(cell, value);
                }
            }
            fos = new FileOutputStream(excelFile);
            book.write(fos);
            fos.flush();
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * createCellComment
     * 
     * @param book
     * @param sheet
     * @param cell
     * @param text
     * @param i
     * @param j
     */
    protected void createCellComment(HSSFWorkbook book, Sheet sheet, Cell cell, String text, int i, int j) {
        CreationHelper helper = book.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(helper.createRichTextString(text));
        cell.setCellComment(comment);
    }

    /**
     * Cellの値を文字列として取得します。
     * 
     * @param sheet
     * @param rowIdx
     * @param columnIdx
     * @return
     */
    protected String getCellValueAsString(Cell cell) {
        Object obj = defaultCellReader.read(cell);
        return obj == null ? "" : obj.toString();
    }

    /**
     * getCellValueAsString
     * 
     * @param sheet
     * @param x
     * @param y
     * @return
     */
    protected String getCellValueAsString(Sheet sheet, int x, int y) {
        return getCellValueAsString(getCell(sheet, x, y));
    }

    /**
     * Cellの値を取得します。
     * 
     * @param cell
     * @return
     */
    protected Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return defaultCellReader.read(cell);
    }

    /**
     * capFirstChar
     * 
     * @param name
     * @return
     */
    public String capFirstChar(String name) {
        return String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    /**
     * isEndRow
     * 
     * @param <E>
     * @param row
     * @return
     */
    protected <E> boolean isEndRow(LinkedHashMap<String, E> row) {
        boolean isEnd = true;
        for (Map.Entry<String, E> e : row.entrySet()) {
            if (!isEmpty(e.getKey())) {
                isEnd = false;
                break;
            }
        }
        return isEnd;
    }

    /**
     * isEndRow
     * 
     * @param <E>
     * @param row
     * @return
     */
    protected <E> boolean isEndRow(List<E> row) {
        boolean isEnd = true;
        for (Object e : row) {
            if (!isEmpty(String.valueOf(e))) {
                isEnd = false;
                break;
            }
        }
        return isEnd;
    }

    /**
     * Load Excel as XSSFWorkbook
     * 
     * @param excelFile
     * @return org.apache.poi.ss.usermodel.HSSFWorkbook
     */
    protected Workbook loadExcel(String excelFile) {
        return loadExcel(new File(excelFile));
    }

    /**
     * Load Excel as XSSFWorkbook
     * 
     * @param excelFile
     * @return org.apache.poi.ss.usermodel.HSSFWorkbook
     */
    protected Workbook loadExcel(File excelFile) {
        Workbook book = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(excelFile);
            if (excelFile.getName().toLowerCase().endsWith(".xls")) {
                book = new HSSFWorkbook(fis);
            } else {
                book = new XSSFWorkbook(fis);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return book;
    }

    /**
     * getRowFirstValueAsString
     * 
     * @param sheet
     * @param rowNum
     * @param maxColNumLimix
     * @return
     */
    protected String getRowFirstValueAsString(Sheet sheet, int rowNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return "";
        }
        int firstCellNum = row.getFirstCellNum();
        int lastCellNum = row.getLastCellNum();
        StringBuilder cellValue = new StringBuilder();
        if (row != null) {
            for (int colNum = firstCellNum; colNum < lastCellNum; colNum++) {
                Cell cell = CellUtil.getCell(row, colNum);
                if (cell != null) {
                    String value = this.getCellValueAsString(cell);
                    if (!isEmpty(value)) {
                        return value;
                    }
                }
            }
        }
        return cellValue.toString();
    }

    /**
     * lookupDataCell
     * 
     * @param file
     * @param sheet
     * @param keyword
     * @return
     */
    protected Coordinate lookupDataCell(File file, Sheet sheet, String keyword) {
        Iterator<Row> rows = sheet.iterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                String value = String.valueOf(defaultCellReader.read(cell));
                if (value != null && keyword.trim().equals(value.trim())) {
                    return new Coordinate(cell.getRowIndex(), cell.getColumnIndex());
                }
            }
        }
        System.out.println("Warning: No found keyword [" + keyword + "] in [" + file.getPath() + " - " + sheet.getSheetName() + "]");
        return null;
    }

}
