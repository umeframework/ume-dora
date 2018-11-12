/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.umeframework.dora.tool.poi.ExcelAccessor;
import org.umeframework.dora.util.ValidatorUtil;

/**
 * ServiceExcelParser
 */
public class ServiceExcelParser extends ExcelAccessor {

	/**
	 * getDeclareDtoList
	 * 
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getDeclareDtoListOfSamePackage(
			File excelFile) throws Exception {
		Map<String, String> dtoList = new HashMap<String, String>();
		String pkg = this.parsePackageName(excelFile);

		Workbook book = loadExcel(excelFile);
		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			Sheet sheet = book.getSheetAt(i);
			String sheetName = sheet.getSheetName().trim();
			String remark = excelFile + "." + sheetName;
			if (sheetName.toUpperCase().startsWith("D") || sheetName.toUpperCase().startsWith("Dto")) {
				String dtoId = getCellValueAsString(sheet, 0, 1).trim();
				String dtoFullName = pkg + ".dto." + dtoId;
				if (dtoList.containsKey(pkg)) {
					throw new RuntimeException("Found duplicate Dto declare in " + dtoList.get(pkg) + " and " + remark + ", " + dtoFullName);
				}
				dtoList.put(dtoFullName, remark);
			}
		}
		return dtoList;
	}

	/**
	 * parseWorkBook
	 * 
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	public DocBean parseWorkBook(
			File excelFile) throws Exception {
		DocBean docBean = new DocBean();
		docBean.setDocName(excelFile.getName());

		Workbook book = loadExcel(excelFile);
		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			String sheetName = book.getSheetName(i).trim();
			if (sheetName.startsWith("#")) {
				continue;
			}

			if (sheetName.toUpperCase().startsWith("F") || sheetName.toUpperCase().startsWith("FUNC") || sheetName.toUpperCase().startsWith("功能")) {
				DocFuncBean bean = parseWorkSheetForFunc(excelFile, sheetName, book.getSheetAt(i));
				bean.setSheetName(sheetName);
				docBean.getFuncBeanList().add(bean);
			} else if (sheetName.toUpperCase().startsWith("D") || sheetName.toUpperCase().startsWith("DTO") || sheetName.toUpperCase().startsWith("数据结构")) {
				DocDtoBean bean = parseWorkSheetForDto(excelFile, sheetName, book.getSheetAt(i));
				bean.setSheetName(sheetName);
				docBean.getDtoBeanList().add(bean);
			} else if (sheetName.toUpperCase().startsWith("C") || sheetName.toUpperCase().startsWith("CMP") ||sheetName.toUpperCase().startsWith("组件")) {
				DocCmpBean bean = parseWorkSheetForCmp(excelFile, sheetName, book.getSheetAt(i));
				bean.setSheetName(sheetName);
				if (docBean.getCmpBean() != null) {
					throw new RuntimeException("Founc duplicate component define in file " + excelFile + ", " + sheetName);
				}
				docBean.setCmpBean(bean);
			}
		}
		return docBean;
	}

	/**
	 * parseWorkSheetForFunc
	 * 
	 * @param excelFile
	 * @param sheetName
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	protected DocFuncBean parseWorkSheetForFunc(
			File excelFile,
			String sheetName,
			Sheet sheet) throws Exception {
		DocFuncBean bean = new DocFuncBean();
		bean.setId(getCellValueAsString(sheet, 0, 1));
		bean.setName(getCellValueAsString(sheet, 1, 1));
		bean.setComment(getCellValueAsString(sheet, 2, 1));
		bean.setAuthor(getCellValueAsString(sheet, 0, 4));
		bean.setUpdateDate(getCellValueAsString(sheet, 1, 4));
		bean.setPkg(parsePackageName(excelFile));

		if (!ValidatorUtil.isAlphaNumeric(bean.getId())) {
			throw new RuntimeException("Invalid function ID define in " + excelFile + ", " + sheetName + ", " + bean.getId());
		}

		Coordinate cor1 = super.lookupDataCell(sheet, "功能参数列表(Input)");
		if (cor1 == null) {
			return null;
		}
		int inParamStartRowNum = cor1.x + 2;
		int[] targetCols = { 0, 1, 2, 3, 4, 5 };

		List<List<Object>> paramRows = readSheetAsObjectList(excelFile, sheetName, targetCols, inParamStartRowNum, -1);
		for (List<Object> row : paramRows) {
			String index = row.get(0) != null ? String.valueOf(row.get(0)).trim() : "";
			String name = row.get(1) != null ? String.valueOf(row.get(1)).trim() : "";
			String id = row.get(2) != null ? String.valueOf(row.get(2)).trim() : "";
			String type = row.get(3) != null ? String.valueOf(row.get(3)).trim() : "";
			String typeFlag = row.get(4) != null ? String.valueOf(row.get(4)).trim() : "";
			String comment = row.get(5) != null ? String.valueOf(row.get(5)).trim() : "";

			if (isEmpty(id) || !ValidatorUtil.isAlphaNumeric(id.replace("_", ""))) {
				throw new RuntimeException("Invalid function parameter ID define in " + excelFile + ", " + sheetName + ", <" + bean.getId() + ">, <" + id + ">");
			}
			DocItemBean item = new DocItemBean();
			item.setIndex(index);
			item.setName(name);
			item.setId(id);
			item.setType(type);
			item.setTypeFlag(typeFlag);
			item.setComment(comment);
			bean.getInParamList().add(item);
		}

		Coordinate cor2 = super.lookupDataCell(sheet, "功能参数列表(Output)");
		if (cor2 != null) {
			int outParamStartRowNum = cor2.x + 2;
			String index = getCellValueAsString(sheet, outParamStartRowNum, 0);
			String name = getCellValueAsString(sheet, outParamStartRowNum, 1);
			String type = getCellValueAsString(sheet, outParamStartRowNum, 3);
			String typeFlag = getCellValueAsString(sheet, outParamStartRowNum, 4);
			String comment = getCellValueAsString(sheet, outParamStartRowNum, 5);

			if (!isEmpty(type)) {
				DocItemBean item = new DocItemBean();
				item.setIndex(index);
				item.setName(name);
				item.setId("result");
				item.setType(type);
				item.setTypeFlag(typeFlag);
				item.setComment(comment);
				bean.getOutParamList().add(item);
			}
		}

		Coordinate cor3 = super.lookupDataCell(sheet, "功能描述");
		if (cor3 != null) {
			int detailStartRowNum = cor3.x + 1;
			int[] detailTargetCols = { 0, 1, 2, 3, 4, 5 };
			List<String> lines = new ArrayList<String>();
			List<List<Object>> detailRows = readSheetAsObjectList(excelFile, sheetName, detailTargetCols, detailStartRowNum, -1);
			for (List<Object> detailRow : detailRows) {
				StringBuilder line = new StringBuilder();
				for (Object cell : detailRow) {
					if (cell != null && !isEmpty(cell.toString())) {
					    line.append(cell);
					}
				}
				if (!line.toString().trim().equals("")) {
				    lines.add(line.toString());
				}
			}
			bean.setDetails(lines);
		}

		return bean;
	}

	/**
	 * parseWorkSheetForDto
	 * 
	 * @param excelFile
	 * @param sheetName
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	protected DocDtoBean parseWorkSheetForDto(
			File excelFile,
			String sheetName,
			Sheet sheet) throws Exception {
		DocDtoBean bean = new DocDtoBean();
		bean.setId(getCellValueAsString(sheet, 0, 1));
		bean.setName(getCellValueAsString(sheet, 1, 1));
		bean.setComment(getCellValueAsString(sheet, 2, 1));
		bean.setAuthor(getCellValueAsString(sheet, 0, 4));
		bean.setUpdateDate(getCellValueAsString(sheet, 1, 4));
		bean.setPkg(parsePackageName(excelFile) + ".dto");

		if (!ValidatorUtil.isAlphaNumeric(bean.getId())) {
			throw new RuntimeException("Invalid func ID define in " + excelFile + ", " + sheetName + ", <" + bean.getId() + ">");
		}

		Coordinate cor1 = super.lookupDataCell(sheet, "属性列表");
		if (cor1 == null) {
			return null;
		}
		int[] targetCols = { 0, 1, 2, 3, 4, 5 };

		List<List<Object>> rows = readSheetAsObjectList(excelFile, sheetName, targetCols, cor1.x + 2, -1);
		for (List<Object> row : rows) {
			String index = String.valueOf(row.get(0)).trim();
			String name = String.valueOf(row.get(1)).trim();
			String id = String.valueOf(row.get(2)).trim();
			String type = String.valueOf(row.get(3)).trim();
			String typeFlag = String.valueOf(row.get(4)).trim();
			String defaultValue = String.valueOf(row.get(5)).trim();

			if (isEmpty(id) || !ValidatorUtil.isAlphaNumeric(id.replace("_", ""))) {
				throw new RuntimeException("Invalid parma ID define in " + excelFile + ", " + sheetName + ", <" + bean.getId() + ", <" + id + ">");
			}
			DocItemBean prop = new DocItemBean();
			prop.setIndex(index);
			prop.setName(name);
			prop.setId(id);
			prop.setType(type);
			prop.setTypeFlag(typeFlag);
			prop.setDefaultValue(defaultValue);
			bean.getPropList().add(prop);
		}

		return bean;
	}

	/**
	 * parseWorkSheetForCmp
	 * 
	 * @param excelFile
	 * @param sheetName
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	protected DocCmpBean parseWorkSheetForCmp(
			File excelFile,
			String sheetName,
			Sheet sheet) throws Exception {
		DocCmpBean bean = new DocCmpBean();
		bean.setId(getCellValueAsString(sheet, 0, 1));
		bean.setName(getCellValueAsString(sheet, 1, 1));
		bean.setComment(getCellValueAsString(sheet, 2, 1));
		bean.setAuthor(getCellValueAsString(sheet, 0, 4));
		bean.setUpdateDate(getCellValueAsString(sheet, 1, 4));
		bean.setPkg(parsePackageName(excelFile));

		if (!ValidatorUtil.isAlphaNumeric(bean.getId())) {
			throw new RuntimeException("Invalid component ID define in " + excelFile + ", " + sheetName + ", <" + bean.getId() + ">");
		}

		Coordinate cor1 = super.lookupDataCell(sheet, "组件功能列表");
		if (cor1 == null) {
			return null;
		}
		int[] targetCols = { 0, 1, 2, 3, 4, 5 };

		List<List<Object>> rows = readSheetAsObjectList(excelFile, sheetName, targetCols, cor1.x + 2, -1);
		for (List<Object> row : rows) {
			String index = String.valueOf(row.get(0)).trim();
			String name = String.valueOf(row.get(1)).trim();
			String id = String.valueOf(row.get(2)).trim();
			String wsFlag = String.valueOf(row.get(3)).trim().toUpperCase();
			String wsId = String.valueOf(row.get(4)).trim().toUpperCase();
			if (!isEmpty(wsFlag) && isEmpty(wsId)) {
				throw new RuntimeException("Invalid component service ID or service public flag value in " + excelFile + ", " + sheetName + ", <" + bean.getId() + ">, <" + id + ">");
			}

			String comment = String.valueOf(row.get(5)).trim();

			if (isEmpty(id) || !ValidatorUtil.isAlphaNumeric(id)) {
				throw new RuntimeException("Invalid component function ID reference in " + excelFile + ", " + sheetName + ", <" + bean.getId() + ">, <" + id + ">");
			}
			DocItemBean item = new DocItemBean();
			item.setIndex(index);
			item.setName(name);
			item.setId(id);
			item.setWsFlag(wsFlag);
			item.setWsId(wsId);
			item.setComment(comment);
			bean.getFuncList().add(item);
		}

		return bean;
	}

	/**
	 * isEndRow
	 * 
	 * @param <E>
	 * @param row
	 * @return
	 */
	@Override
	protected <E> boolean isEndRow(
			List<E> row) {
		if (isEmpty(String.valueOf(row.get(0))) && isEmpty(String.valueOf(row.get(1)))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * parsePackageName
	 * 
	 * @param excelFile
	 * @return
	 */
	protected String parsePackageName(
			File excelFile) {
		String path = excelFile.getParent().toLowerCase();
		path = path.replace("\\", "/");
		path = path.substring(path.lastIndexOf("/") + 1);
		return path;
	}


}