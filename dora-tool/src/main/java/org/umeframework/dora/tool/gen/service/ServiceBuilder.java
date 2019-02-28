/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.service;

import java.util.List;
import java.util.Set;

import org.umeframework.dora.tool.poi.TypeMapper;

/**
 * DefaultServiceBuilder
 *
 * @author Yue MA
 *
 */
public class ServiceBuilder {
    /**
     * build
     *
     * @param declareDtoList
     * @param docBean
     */
    public void build(DocBean docBean, Set<String> declareDtoList) {

        // Append information for Dto
        List<DocDtoBean> dtoBeanList = docBean.getDtoBeanList();
        for (DocDtoBean dtoBean : dtoBeanList) {
            for (DocItemBean item : dtoBean.getPropList()) {
                String javaType = getJavaType(docBean.getDocName(), dtoBean.getPkg(), dtoBean.getSheetName(), item.getType(), item.getTypeFlag(), declareDtoList);
                String javaOriType = getJavaOriType(docBean.getDocName(), dtoBean.getPkg(), dtoBean.getSheetName(), item.getType(), item.getTypeFlag(), declareDtoList);
                item.setJavaOriType(javaOriType);
                if (javaType.contains(".")) {
                    dtoBean.getClassImportList().add(formatJavaType4Import(javaType));
                    javaType = removeJavaTypePkg(javaType);
                }
                item.setJavaType(javaType);
                item.setJavaId4GetSet(item.getId().substring(0, 1).toUpperCase() + item.getId().substring(1));

                // make default value for java properties init
                String defaultValue = getJavaDefaultValueType(javaType, item.getDefaultValue());
                item.setDefaultValue(defaultValue);
            }
        }

        // Append information for SIF
        DocCmpBean cmpBean = docBean.getCmpBean();
        List<DocFuncBean> funcBeanList = docBean.getFuncBeanList();
        for (DocFuncBean funcBean : funcBeanList) {
            // Process input parameter
            for (DocItemBean item : funcBean.getInParamList()) {
                String javaType = getJavaType(docBean.getDocName(), cmpBean.getPkg(), funcBean.getSheetName(), item.getType(), item.getTypeFlag(), declareDtoList);
                String javaOriType = getJavaOriType(docBean.getDocName(), cmpBean.getPkg(), funcBean.getSheetName(), item.getType(), item.getTypeFlag(), declareDtoList);
                item.setJavaOriType(javaOriType);
                if (javaType.contains(".")) {
                    // Add import type full name to DocCmpBean level
                    cmpBean.getClassImportList().add(formatJavaType4Import(javaType));
                    // Add import type full name to DocFuncBean level
                    funcBean.getClassImportList().add(formatJavaType4Import(javaType));
                    // Add import type full name to DocFuncBean Input Dto level
                    funcBean.getInParamClassImportList().add(formatJavaType4Import(javaType));
                    javaType = removeJavaTypePkg(javaType);
                }
                item.setJavaType(javaType);
                item.setJavaId4GetSet(item.getId().substring(0, 1).toUpperCase() + item.getId().substring(1));
                item.setDefaultValue("");
            }
            // Process output parameter
            for (DocItemBean item : funcBean.getOutParamList()) {
                String javaType = getJavaType(docBean.getDocName(), cmpBean.getPkg(), funcBean.getSheetName(), item.getType(), item.getTypeFlag(), declareDtoList);
                String javaOriType = getJavaOriType(docBean.getDocName(), cmpBean.getPkg(), funcBean.getSheetName(), item.getType(), item.getTypeFlag(), declareDtoList);
                item.setJavaOriType(javaOriType);
                if (javaType.contains(".")) {
                    // Add import type full name to DocCmpBean (Use for ServiceInterface)
                    cmpBean.getClassImportList().add(formatJavaType4Import(javaType));
                    // Add import type full name to DocFuncBean (Use for WebActionClass
                    funcBean.getClassImportList().add(formatJavaType4Import(javaType));
                    // Add import type full name to DocFuncBean Output Dto level
                    funcBean.getOutParamClassImportList().add(formatJavaType4Import(javaType));
                    // Remove package prefix from javaType
                    javaType = removeJavaTypePkg(javaType);
                }
                item.setJavaType(javaType);
                item.setJavaId4GetSet(item.getId().substring(0, 1).toUpperCase() + item.getId().substring(1));
                item.setDefaultValue("");
            }
        }
    }

    /**
     * getJavaType
     *
     * @param type
     * @param typeFlag
     * @return
     */
    protected String getJavaType(String fileName, String pkg, String sheetName, String type, String typeFlag, Set<String> declareDtoList) {
        return appendArrayFlag(getJavaOriType(fileName, pkg, sheetName, type, typeFlag, declareDtoList), typeFlag);
    }

    /**
     * getJavaOriType
     *
     * @param fileName
     * @param pkg
     * @param sheetName
     * @param type
     * @param typeFlag
     * @param declareDtoList
     * @return
     */
    protected String getJavaOriType(String fileName, String pkg, String sheetName, String type, String typeFlag, Set<String> declareDtoList) {
        typeFlag = typeFlag.trim();

        if (TypeMapper.dataTypeMap2Java.containsKey(type)) {
            String javaType = TypeMapper.dataTypeMap2Java.get(type);
            return javaType;
            // return appendArrayFlag(javaType, typeFlag);
        }

        String typeFullName = pkg.endsWith(".dto") ? (pkg + "." + type) : (pkg + ".dto." + type);

        if (!type.contains(".") && declareDtoList.contains(typeFullName)) {
            String javaType = typeFullName;
            return javaType;
        } else if (type.contains(".")) {
            String javaType = type;
            return javaType;
        }

        throw new RuntimeException("Found unsupport data type in " + fileName + ",<" + sheetName + ">,<" + type + ">");
    }

    /**
     * appendArrayFlag
     *
     * @param type
     * @param typeFlag
     * @return
     */
    protected String appendArrayFlag(String type, String typeFlag) {
        if (typeFlag.equals("数组")) {
            return type + "[]";
        } else if (typeFlag.equals("列表")) {
            return "java.util.List<" + type + ">";
        } else {
            return type;
        }
    }

    /**
     * formatJavaType
     *
     * @return
     */
    protected String removeJavaTypePkg(String javaType) {
        if (javaType.contains("<")) {
            String nestPart = javaType.substring(javaType.indexOf("<"));
            String typePart = javaType.substring(0, javaType.indexOf("<"));
            typePart = removeJavaTypePkg(typePart);

            return typePart + nestPart;
        } else {
            return javaType.substring(javaType.lastIndexOf(".") + 1);
        }
    }

    /**
     * formatJavaType4Import
     *
     * @param javaType
     * @return
     */
    protected String formatJavaType4Import(String javaType) {
        if (javaType.contains("[")) {
            return javaType.substring(0, javaType.indexOf("["));
        }
        if (javaType.contains("<")) {
            return javaType.substring(0, javaType.indexOf("<"));
        }
        return javaType;
    }

    /**
     * getJavaDefaultValueType
     *
     * @param javaType
     * @param oriDefaultValue
     * @return
     */
    protected String getJavaDefaultValueType(String javaType, String oriDefaultValue) {
        if (oriDefaultValue == null || "".equals(oriDefaultValue) || "null".equalsIgnoreCase(oriDefaultValue)) {
            return "";
        }
        if (oriDefaultValue.startsWith("\"") && oriDefaultValue.endsWith("\"")) {
            oriDefaultValue = oriDefaultValue.substring(1, oriDefaultValue.lastIndexOf("\"") - 1);
        }

        if ("String".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= \"" + oriDefaultValue + "\"";
        } else if ("Boolean".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= Boolean.valueOf(\"" + oriDefaultValue + "\")";
        } else if ("Integer".equalsIgnoreCase(javaType) || "int".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= " + oriDefaultValue;
        } else if ("Short".equalsIgnoreCase(javaType) || "short".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= " + oriDefaultValue;
        } else if ("Long".equalsIgnoreCase(javaType) || "long".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= " + oriDefaultValue + "l";
        } else if ("Double".equalsIgnoreCase(javaType) || "double".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= " + oriDefaultValue + "d";
        } else if ("Float".equalsIgnoreCase(javaType) || "float".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= " + oriDefaultValue + "f";
        } else if ("java.sql.Date".equalsIgnoreCase(javaType) || "Date".equalsIgnoreCase(javaType)) {
            oriDefaultValue = oriDefaultValue.replaceAll("/", "-");
            oriDefaultValue = "= Date.valueOf(\"" + oriDefaultValue + "\")";
        } else if ("java.sql.Time".equalsIgnoreCase(javaType) || "Time".equalsIgnoreCase(javaType)) {
            oriDefaultValue = oriDefaultValue.replaceAll("/", "-");
            oriDefaultValue = "= Time.valueOf(\"" + oriDefaultValue + "\")";
        } else if ("java.sql.Timestamp".equalsIgnoreCase(javaType) || "Timestamp".equalsIgnoreCase(javaType)) {
            oriDefaultValue = oriDefaultValue.replaceAll("/", "-");
            oriDefaultValue = "= Timestamp.valueOf(\"" + oriDefaultValue + "\")";
        } else if ("java.math.BigInteger".equalsIgnoreCase(javaType) || "BigInteger".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= new BigInteger(\"" + oriDefaultValue + "\")";
        } else if ("java.math.BigDecimal".equalsIgnoreCase(javaType) || "BigDecimal".equalsIgnoreCase(javaType)) {
            oriDefaultValue = "= new BigDecimal(\"" + oriDefaultValue + "\")";
        }
        return oriDefaultValue;
    }

    // /**
    // * JavaTypeMapping
    // */
    // public static Map<String, String> JavaTypeMapping = new HashMap<String, String>();
    // static {
    // JavaTypeMapping.put("文本", "String");
    // JavaTypeMapping.put("文字", "String");
    // JavaTypeMapping.put("整数", "Integer");
    // JavaTypeMapping.put("实数", "Double");
    // JavaTypeMapping.put("日期", "java.sql.Date");
    // JavaTypeMapping.put("时刻", "java.sql.Time");
    // JavaTypeMapping.put("时间戳", "java.sql.Timestamp");
    // JavaTypeMapping.put("布尔", "Boolean");
    // JavaTypeMapping.put("长整数", "Long");
    // JavaTypeMapping.put("大整数", "Long");
    // JavaTypeMapping.put("大实数", "java.math.BigDecimal");
    // JavaTypeMapping.put("字节", "byte");
    // }
}
