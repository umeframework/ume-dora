/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.lob.ex;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.umeframework.dora.type.Blob;

/**
 * BlobTypeHandler
 * 
 * @author Yue MA
 */
public class BlobTypeHandler implements TypeHandler<Blob> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
	 */
	@Override
	public Blob getResult(ResultSet resultSet, String name) throws SQLException {
		Blob result = null;
		try {
			java.sql.Blob blob = resultSet.getBlob(name);
			if (blob != null) {
				InputStream is = blob.getBinaryStream();
				byte[] content = new byte[is.available()];
				int temp;
				int index = 0;
				while ((temp = is.read()) != -1) {
					content[index++] = Byte.valueOf((byte) temp);
				}
				result = new Blob(content);
			}
		} catch (IOException e) {
			throw new SQLException("Failed to access BLOB data in BlobTypeHandler.", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, int)
	 */
	@Override
	public Blob getResult(ResultSet resultSet, int i) throws SQLException {
		Blob result = null;
		try {
			java.sql.Blob blob = resultSet.getBlob(i);
			if (blob != null) {
				InputStream is = blob.getBinaryStream();
				byte[] content = new byte[is.available()];
				int temp;
				int index = 0;
				while ((temp = is.read()) != -1) {
					content[index++] = Byte.valueOf((byte) temp);
				}
				result = new Blob(content);
			}
		} catch (IOException e) {
			throw new SQLException("Failed to access BLOB data in BlobTypeHandler.", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
	 */
	@Override
	public Blob getResult(CallableStatement statement, int i) throws SQLException {
		Blob result = null;
		try {
			java.sql.Blob blob = statement.getBlob(i);
			if (blob != null) {
				InputStream is = blob.getBinaryStream();
				byte[] content = new byte[is.available()];
				int temp;
				int index = 0;
				while ((temp = is.read()) != -1) {
					content[index++] = Byte.valueOf((byte) temp);
				}
				result = new Blob(content);
			}
		} catch (IOException e) {
			throw new SQLException("Failed to access BLOB data in BlobTypeHandler.", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement , int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setParameter(PreparedStatement statement, int paramIndex, Blob value, JdbcType jdbcType) throws SQLException {
		//statement.setBlob(paramIndex, value);
	    statement.setBytes(paramIndex, value.getBytes());
	}

}
