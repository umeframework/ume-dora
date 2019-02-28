/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.poi;

import org.apache.poi.ss.usermodel.Cell;

/**
 * CommentCellReader
 */
public class CommentCellReader implements CellReader<CommentCellReader.CellValue> {
    /**
     * contain cell's data with comment
     */
    public static class CellValue {
        public String value;
        public String comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.cn.evox.tool.common.CellReader#read(org.apache.poi.ss.usermodel .Cell)
     */
    @Override
    public CommentCellReader.CellValue read(Cell cell) {
        CellValue cv = new CellValue();
        cv.value = new ExcelAccessor().getCellValueAsString(cell);
        cv.comment = new ExcelAccessor().getCellCommentAsString(cell);
        return cv;
    }
}
