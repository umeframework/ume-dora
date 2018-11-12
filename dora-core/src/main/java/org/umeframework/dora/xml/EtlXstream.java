/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
/**
 * Filter the node of xml that does'nt exist in javabean.
 *
 * @author LuanDeLong
 * 
 */
public class EtlXstream extends XStream {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.thoughtworks.xstream.XStream#wrapMapper
	 */
	@Override
	protected MapperWrapper wrapMapper(
			MapperWrapper next) {
		return new MapperWrapper(next) {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.thoughtworks.xstream.mapper.MapperWrapper#shouldSerializeMember
			 */
			@Override
			public boolean shouldSerializeMember(
					@SuppressWarnings("rawtypes") Class definedIn,
					String fieldName) {
				if (definedIn == Object.class) {
					return false;
				}
				if (fieldName.equals("fileName")) {
					return false;
				}
				return super.shouldSerializeMember(definedIn, fieldName);
			}
		};
	}
}
