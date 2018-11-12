/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.poi;

import org.apache.poi.ss.usermodel.Cell;

/**
 * CellWriter
 * 
 * @param <E>
 */
public interface CellWriter<E> {
	/**
	 * write cell data to excel
	 * 
	 * @param cell
	 * @return
	 */
	public void write(
			Cell cell,
			E value);
}
