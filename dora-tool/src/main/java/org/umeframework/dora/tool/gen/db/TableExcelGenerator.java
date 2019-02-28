/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.umeframework.dora.tool.gen.EntityGenerator;
import org.umeframework.dora.tool.poi.ExcelAccessor;
import org.umeframework.dora.tool.poi.TypeMapper;
import org.umeframework.dora.util.StringKanaUtil;
import org.umeframework.dora.util.ValidatorUtil;

/**
 * TableExcelParser
 */
public class TableExcelGenerator extends ExcelAccessor {
    // Identify keys define for Chinese (default) document format
    private String[] excelTargetColumnLabel = new String[] { "序号", "项目名", "项目ID", "数据类型", "长度", "主键", "非空", "默认值", "固定值", "最小值", "最大值", "格式", "备注" };
    // Define for English document format
    // private String excelTargetDataFlag = "#";
    // private String[] excelTargetColumnLabel = new String[] { "NO", "NAME", "ID", "DATA TYPE", "LENGTH", "KEY", "NOT NULL", "DEFAULT", "CONST", "MIN", "MAX", "FORMAT", "COMMENT" };

    // Ignore sheet name define
    private Set<String> ignoreSheetNames = new HashSet<String>();
    // Generator instance
    private EntityGenerator entityGenerator;
    // TypeMapper instance
    private TypeMapper typeMapper;

    /**
     * TableExcelParser
     */
    public TableExcelGenerator(TypeMapper typeMapper, String... databaseCategory) throws IOException {
        ignoreSheetNames.add("R");
        ignoreSheetNames.add("备注");
        this.entityGenerator = new EntityGenerator(typeMapper, databaseCategory);
        this.typeMapper = typeMapper;
    }
    /**
     * TableExcelParser
     */
    public TableExcelGenerator(String... databaseCategory) throws IOException {
        this(new TypeMapper(), databaseCategory);
    }
    /**
     * TableExcelParser
     */
    public TableExcelGenerator() throws IOException {
        this("mysql");
    }

    /**
     * execute
     * 
     * @param inputPath
     * @param database
     */
    public void execute(String inputPath) {
        try {
            File[] pathnames = new File(inputPath).listFiles(new FileFilter() {
                @Override
                public boolean accept(File e) {
                    return e.isDirectory();
                }
            });
            int i = 1;
            for (File pathname : pathnames) {
                File[] defines = pathname.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File e) {
                        String name = e.getName().toLowerCase();
                        return name.endsWith(".xls") || name.endsWith(".xlsx");
                    }
                });
                Map<String, TableDescBean> infoMap = this.parseFile(defines);
                Collection<TableDescBean> infoList = infoMap.values();

                String packageName = pathname.getName().toLowerCase();
                entityGenerator.setGenBasePackage(packageName);
                entityGenerator.execute("-" + (i++), infoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * parseFileList
     * 
     * @param inputPath
     * @return
     * @throws Exception
     */
    protected Map<String, TableDescBean> parseFileList(List<File> defFiles) throws Exception {
        return parseFile(defFiles.toArray(new File[defFiles.size()]));
    }

    /**
     * @param defFiles
     * @return
     * @throws Exception
     */
    protected Map<String, TableDescBean> parseFile(File... defFiles) throws Exception {
        Map<String, TableDescBean> result = new LinkedHashMap<String, TableDescBean>();
        for (File file : defFiles) {
            Map<String, TableDescBean> beans = this.parseWorkbook(file);

            for (Map.Entry<String, TableDescBean> e : beans.entrySet()) {
                if (result.containsKey(e.getKey())) {
                    throw new Exception("Duplicated Table ID " + e.getValue().getTblId() + " define in " + e.getValue().getDocName() + " and " + result.get(e.getKey()).getDocName());
                }
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    /**
     * parseWorkBook
     *
     * @param excelFile
     * @return
     * @throws Exception
     */
    protected Map<String, TableDescBean> parseWorkbook(File excelFile) throws Exception {
        Map<String, TableDescBean> result = new LinkedHashMap<String, TableDescBean>();

        String fileName = excelFile.getName();
        Workbook book = loadExcel(excelFile);
        // Set<String> nameSet = new HashSet<String>();
        Map<String, String> nameSet = new HashMap<String, String>();

        for (int i = 0; i < book.getNumberOfSheets(); i++) {
            String sheetName = book.getSheetName(i);
            if (sheetName.startsWith("#") || ignoreSheetNames.contains(sheetName.toUpperCase())) {
                continue;
            }
            // Is target sheet
            Sheet sheet = book.getSheetAt(i);
            TableDescBean dto = parseWorkSheet(excelFile, sheetName, sheet);
            String docName = fileName + ":" + sheetName;
            dto.setDocName(docName);

            String key = fileName + ":" + dto.getTblId();
            if (nameSet.containsKey(dto.getTblId())) {
                throw new Exception("Duplicated Table ID " + dto.getTblId() + " define in " + docName + " and " + nameSet.get(dto.getTblId()));
            }
            nameSet.put(dto.getTblId(), docName);
            result.put(key, dto);
        }
        return result;
    }

    /**
     * parseWorkSheet
     *
     * @param excelFile
     * @param sheetName
     * @param sheet
     * @return
     * @throws Exception
     */
    protected TableDescBean parseWorkSheet(File excelFile, String sheetName, Sheet sheet) throws Exception {
        TableDescBean dto = new TableDescBean();
        String tblName = getCellValueAsString(getCell(sheet, 0, 2));
        String tblId = getCellValueAsString(getCell(sheet, 1, 2));
        if (!ValidatorUtil.isAlphaNumeric(tblId)) {
            tblId = StringKanaUtil.zenkakuToHankaku(tblId);
        }
        String tblDivision = getCellValueAsString(getCell(sheet, 2, 2));
        tblDivision = !isEmpty(tblDivision) ? "${theDivision}" : "";

        String tblSpace1 = getCellValueAsString(getCell(sheet, 0, 8));
        String tblSpace2 = getCellValueAsString(getCell(sheet, 1, 8));
        String tblHistory = getCellValueAsString(getCell(sheet, 2, 8));
        tblHistory = isEmpty(tblHistory) ? "" : tblHistory.trim();
        tblHistory = tblHistory.equals("○") ? "HT" : tblHistory;

        dto.setTblName(tblName);
        dto.setTblId(tblId);
        dto.setTblAlias(sheetName.trim());
        dto.setTblDivision(tblDivision);
        dto.setTblSpace1(tblSpace1);
        dto.setTblSpace2(tblSpace2);
        dto.setTblHistory(tblHistory);

        String excelTargetDataFlag = excelTargetColumnLabel[0];
        int startRowNum = -1;
        for (int rowIdx = 0; rowIdx < 25; rowIdx++) {
            String cellValue = getCellValueAsString(getCell(sheet, rowIdx, 0));
            if (excelTargetDataFlag.equals(cellValue.trim())) {
                startRowNum = rowIdx + 1;
                break;
            }
        }
        if (startRowNum == -1) {
            throw new Exception("*** No Found [" + excelTargetDataFlag + "] in sheet:" + sheetName);
        }

        int endRowNum = -1;
        for (int rowIdx = startRowNum; rowIdx < startRowNum + 2048; rowIdx++) {
            String cellValue = getCellValueAsString(getCell(sheet, rowIdx, 0));
            if (isEmpty(cellValue) || cellValue.trim().startsWith("[NULL]")) {
                endRowNum = rowIdx - 1;
                break;
            }
        }
        if (endRowNum == -1) {
            throw new Exception("*** No Found row EOF in sheet:" + sheetName);
        }

        int endColNum = -1;
        for (int colIdx = 0; colIdx < 2048; colIdx++) {
            String cellValue = getCellValueAsString(getCell(sheet, startRowNum - 1, colIdx));
            if (isEmpty(cellValue) || cellValue.trim().startsWith("[NULL]")) {
                endColNum = colIdx;
                break;
            }
        }
        if (endColNum == -1) {
            throw new Exception("*** No Found column EOF in sheet:" + sheetName);
        }
        int[] colPositions = new int[endColNum];
        for (int i = 0; i < endColNum; i++) {
            colPositions[i] = i;
        }

        List<List<Object>> actTitlesList = readSheetAsObjectList(excelFile, sheetName, colPositions, startRowNum - 1, startRowNum - 1);
        List<Object> actTitles = actTitlesList.get(0);
        int[] targetColumnPostions = new int[excelTargetColumnLabel.length];
        for (int i = 0; i < targetColumnPostions.length; i++) {
            targetColumnPostions[i] = -1;
        }

        // match title accord's position index value
        for (int i = 0; i < excelTargetColumnLabel.length; i++) {
            String expValue = excelTargetColumnLabel[i];
            for (int j = 0; j < actTitles.size(); j++) {
                String actValue = String.valueOf(actTitles.get(j)).trim();
                if (actValue.equals(expValue)) {
                    targetColumnPostions[i] = j;
                }
            }
        }
        for (int i = 0; i < targetColumnPostions.length; i++) {
            if (targetColumnPostions[i] == -1) {
                throw new Exception("Warning: Title data mismatch. No found except title [" + excelTargetColumnLabel[i] + "] in sheet: " + sheetName + "," + excelFile);
            }
        }

        List<List<Object>> rows = readSheetAsObjectList(excelFile, sheetName, targetColumnPostions, startRowNum, endRowNum);

        // Process for table/Dto field level elements
        for (List<Object> row : rows) {
            FieldDescBean field = new FieldDescBean();
            String itemIndex = String.valueOf(row.get(0)).trim();
            String itemName = String.valueOf(row.get(1)).trim();
            if (itemName.contains("\n")) {
                itemName = itemName.replaceAll("\n", " ");
            }

            String itemId = String.valueOf(row.get(2)).trim();
            String dataType = String.valueOf(row.get(3)).trim();
            String length = String.valueOf(row.get(4)).trim();
            String primaryKey = String.valueOf(row.get(5)).trim();
            String notNull = String.valueOf(row.get(6)).trim();
            String defaultValue = String.valueOf(row.get(7)).trim();
            String constValue = String.valueOf(row.get(8)).trim();
            String minValue = String.valueOf(row.get(9)).trim();
            String maxValue = String.valueOf(row.get(10)).trim();
            String textFormat = String.valueOf(row.get(11)).trim();
            String comment = String.valueOf(row.get(12)).trim();

            itemId = ValidatorUtil.isAlphaNumeric(itemId) ? itemId : StringKanaUtil.zenkakuToHankaku(itemId);
            primaryKey = !isEmpty(primaryKey) ? "Y" : "";
            notNull = !isEmpty(notNull) || !isEmpty(primaryKey) ? "NOT NULL" : "";

            char firstCharOfColId = itemId.toUpperCase().charAt(0);
            if (!(firstCharOfColId >= 'A' && firstCharOfColId <= 'Z')) {
                System.out.println("Warning: " + " Ingore non-support field define information in cell[" + itemId + " of sheet[" + sheetName + "]");
                continue;
            }

            field.setColNo(itemIndex);
            field.setColId(itemId);
            field.setColName(itemName);

            if (!typeMapper.checkDataType(dataType)) {
                throw new Exception("Unsupport Data Type define in cell[" + itemId + " , " + dataType + "] of sheet[" + sheetName + "]");
            }

            field.setColDataType(dataType);
            field.setColLength(length);
            field.setColPK(primaryKey);
            field.setColNotNull(notNull);
            field.setColDefaultValue(defaultValue);
            field.setColConstValue(constValue);
            field.setColMinValue(minValue);
            field.setColMaxValue(maxValue);
            field.setColTextFormat(textFormat);
            field.setColComment(comment);

            // process for Java Naming Rules
            field.setFieldId(typeMapper.dbName2JavaName(field.getColId()));
            field.setFieldIdCap(typeMapper.dbName2JavaGetterSetterName(field.getColId()));
            field.setFieldName(field.getColName());

            dto.getFieldList().add(field);
        }
        return dto;
    }

    /**
     * isEndRow
     *
     * @param <E>
     * @param row
     * @return
     */
    @Override
    protected <E> boolean isEndRow(List<E> row) {
        if (row.get(0) == null || "null".equalsIgnoreCase(row.get(0).toString()))
            return true;
        else
            return false;
    }

    /**
     * @return the excelTargetColumnLabel
     */
    public String[] getExcelTargetColumnLabel() {
        return excelTargetColumnLabel;
    }

    /**
     * @param excelTargetColumnLabel
     *            the excelTargetColumnLabel to set
     */
    public void setExcelTargetColumnLabel(String[] excelTargetColumnLabel) {
        this.excelTargetColumnLabel = excelTargetColumnLabel;
    }

    /**
     * @return the entityGenerator
     */
    public EntityGenerator getEntityGenerator() {
        return entityGenerator;
    }

}
