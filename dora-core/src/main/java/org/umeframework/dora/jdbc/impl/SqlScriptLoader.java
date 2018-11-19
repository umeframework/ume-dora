/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.jdbc.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLScriptLoader
 * 
 * @author Yue Ma
 */
public class SqlScriptLoader {
	/**
	 * Line separator of runtime system
	 */
	private String lineSeparator = System.getProperty("line.separator");
	/**
	 * SQL separator in script file
	 */
	private String sqlSeparator = ";";

	/**
	 * load
	 */
	public void load(Connection connection, String sqlInitDataLocation) {
		try {
			Statement statement = connection.createStatement();
			// statement.setQueryTimeout(30);

			List<String> sqls = this.read(sqlInitDataLocation);
			if (sqls != null) {
				for (String sql : sqls) {
					if (sql != null && !"".equals(sql.trim())) {
						System.out.println("@Import script...");
						System.out.println(sql);
						statement.executeUpdate(sql);
					}
				}
			}
			System.out.println("Import script success.");
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}

	/**
	 * read
	 * 
	 * @param fileName
	 * @return
	 */
	public List<String> read(String fileName) {
		return this.read(fileName, "UTF-8");
	}

	/**
	 * read
	 * 
	 * @param fileName
	 * @param charset
	 * @return
	 */
	public List<String> read(String fileName, String charset) {
		List<String> sqls = new ArrayList<String>();
		BufferedReader bufferReader = null;
		try {
			InputStream inputStream = null;
			fileName = fileName.trim();
			if (fileName.startsWith("classpath:")) {
				fileName = fileName.replaceFirst("classpath:", "");
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			} else {
				inputStream = new FileInputStream(new File(fileName));
			}

			bufferReader = new BufferedReader(new InputStreamReader(inputStream, charset));
			StringBuilder sqlBuilder = new StringBuilder();
			String line = bufferReader.readLine();
			while (line != null) {
				line = line.trim();
				if (line.startsWith("//")) {
					line = bufferReader.readLine();
					continue;
				}
				if (line.startsWith("/*") && line.endsWith("*/")) {
					line = bufferReader.readLine();
					continue;
				}
				if (line.startsWith("/*") && !line.endsWith("*/")) {
					line = bufferReader.readLine().trim();
					while (!line.endsWith("*/")) {
						line = bufferReader.readLine().trim();
					}
					line = bufferReader.readLine();
					continue;
				}

				if (line.endsWith(sqlSeparator)) {
					line = line.substring(0, line.lastIndexOf(sqlSeparator));
					sqlBuilder.append(line);
					sqls.add(sqlBuilder.toString());
					sqlBuilder = new StringBuilder();
				} else {
					if (!line.equals("")) {
						sqlBuilder.append(line);
						sqlBuilder.append(lineSeparator);
					}
				}
				line = bufferReader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sqls;
	}

	/**
	 * @return the sqlSeparator
	 */
	public String getSqlSeparator() {
		return sqlSeparator;
	}

	/**
	 * @param sqlSeparator
	 *            the sqlSeparator to set
	 */
	public void setSqlSeparator(String sqlSeparator) {
		this.sqlSeparator = sqlSeparator;
	}

	/**
	 * @return the lineSeparator
	 */
	public String getLineSeparator() {
		return lineSeparator;
	}

	/**
	 * @param lineSeparator
	 *            the lineSeparator to set
	 */
	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

}
