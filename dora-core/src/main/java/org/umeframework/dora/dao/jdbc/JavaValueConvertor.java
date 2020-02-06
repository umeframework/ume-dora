/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;

public class JavaValueConvertor {

    public static Object convert(Object value,Class<?> toType){
        if(value==null) return null;
       String key=value.getClass().getName()+":"+toType.getName();
       //System.out.println(key);
       switch(key){
           case "java.lang.String:java.sql.Timestamp": return StringUtil.toTimestamp((String) value);
           case "java.lang.String:java.sql.Date": return StringUtil.toDate((String) value);
           case "java.lang.String:java.lang.Integer": return StringUtil.toInteger((String) value);
           case "java.lang.String:java.lang.Float": return Float.parseFloat((String) value);
           case "java.lang.String:java.lang.Double": return Double.parseDouble((String) value);
           case "java.lang.String:java.math.BigInteger": return StringUtil.toBigInteger((String) value);
           case "java.lang.String:java.math.BigDecimal": return StringUtil.toBigDecimal((String) value);
           case "java.sql.Date:java.lang.String": return value.toString();
           case "java.lang.Integer:java.lang.String": return value.toString();
           case "java.lang.Float:java.lang.String": return value.toString();
           case "java.lang.Double:java.lang.String": return value.toString();
           case "java.math.BigInteger:java.lang.String": return value.toString();
           case "java.math.BigDecimal:java.lang.String": return value.toString();
           default:throw new RuntimeException("value type "+value.getClass().toString()+" convert to "+toType.toString()+" failed. No convert mechanism is defined. Please contact admin.");
       }
    }

}
