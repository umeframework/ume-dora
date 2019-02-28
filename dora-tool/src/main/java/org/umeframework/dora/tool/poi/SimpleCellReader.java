/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.poi;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.Cell;

/**
 * SimpleCellReader
 */
public class SimpleCellReader implements CellReader<Object> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.cn.evox.tool.common.CellReader#read(org.apache.poi.ss.usermodel .Cell)
     */
    @SuppressWarnings("deprecation")
    @Override
    public Object read(Cell cell) {
        if (cell == null) {
            return null;
        }

        int cellType = cell.getCellType();

        Object value = null;
        switch (cellType) {
        case Cell.CELL_TYPE_BLANK: {
            value = null;
            break;
        }
        case Cell.CELL_TYPE_BOOLEAN: {
            value = cell.getBooleanCellValue();
            break;
        }
        case Cell.CELL_TYPE_NUMERIC: {
            value = new BigDecimal(cell.getNumericCellValue());
            break;
        }
        case Cell.CELL_TYPE_STRING: {
            value = cell.getStringCellValue();
            break;
        }
        case Cell.CELL_TYPE_FORMULA: {
            value = cell.getCellFormula();
            try {
                value = cell.getNumericCellValue();
            } catch (RuntimeException e1) {
                try {
                    value = cell.getStringCellValue();
                } catch (RuntimeException e2) {
                    try {
                        value = cell.getDateCellValue();
                    } catch (RuntimeException e3) {
                        throw e3;
                    }
                }
            }
            break;
        }
        case Cell.CELL_TYPE_ERROR: {
            value = "CELL_TYPE_ERROR";
            break;
        }
        }
        return value;
    }
}
