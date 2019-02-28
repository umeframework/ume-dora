/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.poi;

import org.apache.poi.ss.usermodel.Cell;

/**
 * SimpleCellWriter
 * 
 * @author mayue
 *
 */
public class SimpleCellWriter implements CellWriter<Object> {

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.tool.util.poi.CellWriter#writer(org.apache.poi.ss.usermodel.Cell, java.lang.Object)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void write(Cell cell, Object value) {
        if (value instanceof Number) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(Double.valueOf(String.valueOf(value)));
        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(String.valueOf(value));
        }
    }

}
