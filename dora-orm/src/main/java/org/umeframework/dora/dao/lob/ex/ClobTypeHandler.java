/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.lob.ex;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.umeframework.dora.type.Clob;

/**
 * ClobTypeHandler
 * 
 * @author Yue MA
 */
public class ClobTypeHandler implements TypeHandler<Clob> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
	 */
	@Override
	public Clob getResult(ResultSet resultSet, String name) throws SQLException {
		Clob result = null;
		try {
			java.sql.Clob clob = resultSet.getClob(name);
			if (clob != null) {
				BufferedReader reader = new BufferedReader(clob.getCharacterStream());
				StringBuilder contentStr = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					contentStr.append(line);
				}
				char[] content = contentStr.toString().toCharArray();
				result = new Clob(content);
			}
		} catch (IOException e) {
			throw new SQLException("Failed to access CLOB data in ClobTypeHandler.", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, int)
	 */
	@Override
	public Clob getResult(ResultSet resultSet, int i) throws SQLException {
		Clob result = null;
		try {
			java.sql.Clob clob = resultSet.getClob(i);
			if (clob != null) {
				BufferedReader reader = new BufferedReader(clob.getCharacterStream());
				StringBuilder contentStr = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					contentStr.append(line);
				}
				char[] content = contentStr.toString().toCharArray();
				result = new Clob(content);
			}
		} catch (IOException e) {
			throw new SQLException("Failed to access CLOB data in ClobTypeHandler.", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
	 */
	@Override
	public Clob getResult(CallableStatement statement, int i) throws SQLException {
		Clob result = null;
		try {
			java.sql.Clob clob = statement.getClob(i);
			if (clob != null) {
				BufferedReader reader = new BufferedReader(clob.getCharacterStream());
				StringBuilder contentStr = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					contentStr.append(line);
				}
				char[] content = contentStr.toString().toCharArray();
				result = new Clob(content);
			}
		} catch (IOException e) {
			throw new SQLException("Failed to access CLOB data in ClobTypeHandler.", e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement , int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setParameter(PreparedStatement statement, int index, Clob value, JdbcType jdbcType) throws SQLException {
		statement.setClob(index, value);
	}

}
