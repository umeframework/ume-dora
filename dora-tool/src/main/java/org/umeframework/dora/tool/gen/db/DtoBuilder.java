/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

/**
 * DtoBuilder
 */
public interface DtoBuilder { 
	/**
	 * build
	 * 
	 * @param dto
	 */
	EntityDescBean build(TableDescBean dto, String databaseCategory);
	/**
	 * @return the genDtoExtension
	 */
	String getGenDtoExtension();

	/**
	 * @param genDtoExtension
	 *            the genDtoExtension to set
	 */
	void setGenDtoExtension(String genDtoExtension);

	/**
	 * @return the genCrudInterfaceExtension
	 */
	String getGenCrudInterfaceExtension();

	/**
	 * @param genCrudInterfaceExtension
	 *            the genCrudInterfaceExtension to set
	 */
	void setGenCrudInterfaceExtension(String genCrudInterfaceExtension);

	/**
	 * @return the genCrudClassExtension
	 */
	String getGenCrudClassExtension();

	/**
	 * @param genCrudClassExtension
	 *            the genCrudClassExtension to set
	 */
	void setGenCrudClassExtension(String genCrudClassExtension);
	
    /**
     * @return the genCrudApiExtension
     */
    String getGenCrudApiExtension();

    /**
     * @param genCrudApiExtension
     *            the genCrudApiExtension to set
     */
	void setGenCrudApiExtension(String genCrudApiExtension);
	
	/**
	 * @return
	 */
	String getGenCrudApiPackageExtension();
	
	/**
	 * @param genCrudApiPackageExtension
	 */
	void setGenCrudApiPackageExtension(String genCrudApiPackageExtension);
	
	/**
	 * @return
	 */
	String getGenCrudPackageExtension();
	
	/**
	 * @param genCrudPackageExtension
	 */
	void setGenCrudPackageExtension(String genCrudPackageExtension);
}
