/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.poi;

import org.apache.poi.ss.usermodel.Cell;

/**
 * CellReader
 * 
 * @param <E>
 */
public interface CellReader<E> {
    /**
     * read excel's cell data
     * 
     * @param cell
     * @return
     */
    public E read(
            Cell cell);
}
