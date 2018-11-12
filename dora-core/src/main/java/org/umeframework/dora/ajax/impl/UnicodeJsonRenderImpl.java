/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax.impl;

/**
 * json render interface implementation.<br>
 * Render all Non-ASCII char as Unicode format.<br>
 * 
 * @author Yue MA
 */
public class UnicodeJsonRenderImpl extends JsonRenderImpl {
	/**
	 * stringToJson
	 * 
	 * @param strValue
	 * @return
	 */
	@Override
	protected String stringToJson(String strValue) {
		if (strValue == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strValue.length(); i++) {
			char ch = strValue.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch <= '\u001F' || ch >= '\u007F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}
