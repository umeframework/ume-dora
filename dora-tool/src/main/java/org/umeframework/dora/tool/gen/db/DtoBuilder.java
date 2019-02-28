/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.umeframework.dora.tool.gen.EntityGenerator;
import org.umeframework.dora.tool.poi.TypeMapper;

/**
 * DtoBuilder
 */
public class DtoBuilder {
    /**
     * entityGenerator
     */
    private EntityGenerator entityGenerator;
    /**
     * configuration properties
     */
    private String builderProperties;

    /**
     * generate Dto extension name
     */
    private String genDtoExtension = "";
    /**
     * generate Dto Condition extension name
     */
    private String genDtoConditionExtension = "Condition";
    /**
     * generate Dto Criteria extension name
     */
    private String genDtoCriteriaExtension = "Criteria";
    /**
     * generate Dto Mapper extension name
     */
    private String genDtoMapperExtension = "Mapper";
    /**
     * generate Crud interface extension name
     */
    private String genCrudInterfaceExtension = "CrudService";
    /**
     * generate Crud class extension name
     */
    private String genCrudClassExtension = "CrudServiceImpl";
    /**
     * generate Crud class extension name
     */
    private String genCrudApiExtension = "CrudController";
    /**
     * generate Crud package extension name
     */
    private String genCrudPackageExtension = "service";
    /**
     * generate Crud Api package extension name
     */
    private String genCrudApiPackageExtension = "api";
    /**
     * generate Mapper package extension name
     */
    private String genMapperPackageExtension = "";
    /**
     * generate DTO package extension name
     */
    private String genDtoPackageExtension = "entity";

    /**
     * dtoBuildProperties
     */
    private String dtoBuildProperties = "template/dto-builder.properties";

    /**
     * builderProperties
     */
    private Properties buildProperties = new Properties();

    /**
     * TypeMapper instance
     */
    private TypeMapper typeMapper;

    /**
     * DefaultDtoBuilder
     * 
     * @throws IOException
     */
    public DtoBuilder(TypeMapper typeMapper, EntityGenerator entityGenerator) throws IOException {
        this.entityGenerator = entityGenerator;
        this.typeMapper = typeMapper;
        buildProperties.load(new FileInputStream(new File(dtoBuildProperties)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.tool.gen.db.DtoBuilder#build(org.umeframework.dora.tool.gen.db.DtoBean)
     */
    public EntityDescBean build(TableDescBean dto, String databaseCategory) {
        EntityDescBean ex = this.copyFrom(dto);
        ex.setClassName(ex.getTblName());
        String javaStyleName = typeMapper.upperCaseFirstChar(typeMapper.dbName2JavaName(ex.getTblAlias()));
        ex.setClassId(javaStyleName + this.getGenDtoExtension());
        ex.setClassOriId(javaStyleName);
        ex.setClassOriIdInLowCase(javaStyleName.toLowerCase());
        ex.setClassOriIdInUpperCase(javaStyleName.toUpperCase());
        ex.setTableCrudServiceInterface(javaStyleName + this.getGenCrudInterfaceExtension());
        ex.setTableCrudServiceClass(javaStyleName + this.getGenCrudClassExtension());
        ex.setTableCrudApiClass(javaStyleName + this.getGenCrudApiExtension());
        ex.setTableMapperClass(javaStyleName + this.getGenDtoMapperExtension());
        ex.setTableDtoClass(javaStyleName + this.getGenDtoExtension());
        ex.setTableDtoConditionClass(javaStyleName + this.getGenDtoConditionExtension());
        ex.setTableDtoCriteriaClass(javaStyleName + this.getGenDtoCriteriaExtension());

        ex.setCurrentDate(new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date()));
        ex.setBasePackage(entityGenerator.getGenBasePackage());
        if (!this.getGenCrudPackageExtension().equals("")) {
            ex.setTableCrudServiceInterfacePackage(entityGenerator.getGenBasePackage() + "." + this.getGenCrudPackageExtension());
        } else {
            ex.setTableCrudServiceInterfacePackage(entityGenerator.getGenBasePackage());
        }

        if (!this.getGenCrudApiPackageExtension().equals("")) {
            ex.setTableCrudApiPackage(entityGenerator.getGenBasePackage() + "." + this.getGenCrudApiPackageExtension());
            ex.setTableCrudServicePackage(entityGenerator.getGenBasePackage() + "." + this.getGenCrudPackageExtension() + ".impl");
        } else {
            ex.setTableCrudApiPackage(entityGenerator.getGenBasePackage());
            ex.setTableCrudServicePackage(entityGenerator.getGenBasePackage() + ".impl");
        }

        if (!this.getGenMapperPackageExtension().equals("")) {
            ex.setTableMapperPackage(entityGenerator.getGenBasePackage() + "." + this.getGenMapperPackageExtension());
        } else {
            ex.setTableMapperPackage(entityGenerator.getGenBasePackage());
        }
        if (!this.getGenDtoPackageExtension().equals("")) {
            ex.setTableDtoPackage(entityGenerator.getGenBasePackage() + "." + this.getGenDtoPackageExtension());
        } else {
            ex.setTableDtoPackage(entityGenerator.getGenBasePackage());
        }

        for (FieldDescBean field : ex.getFieldList()) {
            this.buildDtoField(field, databaseCategory);

            // Grouping PL fields
            if (field.getColPK().equalsIgnoreCase("Y") || field.getColPK().equalsIgnoreCase("YES") || field.getColPK().equalsIgnoreCase("Y") || field.getColPK().equalsIgnoreCase("TRUE")) {
                ex.getPrimaryKeyFieldList().add(field);
            } else {
                ex.getNonPrimaryKeyFieldList().add(field);
            }
            if (typeMapper.isNotEmpty(field.getColDataType()) && field.getColDataType().equalsIgnoreCase("AUTO_INCREMENT")) {
                field.setColDataTypeWithLength("INT AUTO_INCREMENT PRIMARY KEY");
                field.setColPK(null);
                ex.getAutoIncrementColumnList().add(field.getColId());
            }
        }

        buildDtoAnnotation(ex);
        return ex;
    }

    /**
     * copyFrom
     * 
     * @param tableDesc
     * @return
     */
    EntityDescBean copyFrom(TableDescBean tableDesc) {
        EntityDescBean entityDesc = new EntityDescBean();
        entityDesc.setCurrentDate(tableDesc.getCurrentDate());
        entityDesc.setTblId(tableDesc.getTblId());
        entityDesc.setTblAlias(tableDesc.getTblAlias());
        entityDesc.setTblName(tableDesc.getTblName());
        entityDesc.setTblDivision(tableDesc.getTblDivision());
        entityDesc.setTblSpace1(tableDesc.getTblSpace1());
        entityDesc.setTblSpace2(tableDesc.getTblSpace2());
        entityDesc.setTblHistory(tableDesc.getTblHistory());

        entityDesc.setFieldList(tableDesc.getFieldList());

        entityDesc.setPrimaryKeyFieldList(new ArrayList<FieldDescBean>());
        entityDesc.setNonPrimaryKeyFieldList(new ArrayList<FieldDescBean>());
        entityDesc.setClassAnnotationList(new ArrayList<String>());
        entityDesc.setAutoIncrementColumnList(new ArrayList<String>());
        entityDesc.setClassImportList(new HashSet<String>());
        return entityDesc;
    }

    /**
     * buildDtoField
     * 
     * @param field
     */
    void buildDtoField(FieldDescBean field, String databaseCategory) {
        String dbDataType = typeMapper.getDBDataType(field.getColDataType(), field, databaseCategory);
        String javaDataType = typeMapper.getJavaType(field.getColDataType(), field, databaseCategory);
        field.setColDataType(dbDataType);
        field.setFieldType(javaDataType);
        String length = field.getColLength();
        if (typeMapper.isNotEmpty(length)) {
            length = length.trim();
            length = length.contains("(") ? length.replace("(", "") : length;
            length = length.contains(")") ? length.replace(")", "") : length;

            if (length.contains("-") || length.contains("~")) {
                length = length.contains("~") ? length.replace("~", "-") : length;
                String[] arr = length.split("-");
                field.setColMinLength(arr[0].trim());
                field.setColMaxLength(arr[1].trim());
                field.setColLength(arr[1].trim());
                field.setColDataTypeWithLength(dbDataType + "(" + arr[1].trim() + ")");
            } else if (length.contains(",")) {
                String[] arr = length.split(",");
                field.setColMinLength(arr[0].trim());
                field.setColMaxLength(arr[0].trim());
                field.setColLength(arr[0].trim());
                field.setColPrecisionLength(arr[1].trim());
                field.setColDataTypeWithLength(dbDataType + "(" + arr[0].trim() + "," + arr[1].trim() + ")");
            } else {
                field.setColMinLength(length);
                field.setColMaxLength(length);
                field.setColLength(length);
                field.setColDataTypeWithLength(dbDataType + "(" + length + ")");
            }
        } else {
            field.setColDataTypeWithLength(dbDataType);
        }

        if (dbDataType.equalsIgnoreCase("LONG VARCHAR") && field.getColDataTypeWithLength().contains("(")) {
            field.setColDataTypeWithLength(field.getColDataTypeWithLength().substring(0, field.getColDataTypeWithLength().indexOf("(")));
        }

        String defValStr = "";
        if (typeMapper.isNotEmpty(field.getColDefaultValue())) {
            String val = field.getColDefaultValue().trim();
            if (typeMapper.isStrType(field.getColDataType())) {
                val = val.charAt(0) == '\"' && val.charAt(val.length() - 1) == '\"' ? val.substring(1, val.length() - 1) : val;
                val = typeMapper.isStrType(field.getColDataType()) && !val.startsWith("\"") && !val.startsWith("'") ? val = "'" + val + "'" : val;
                defValStr = "DEFAULT " + val;
            } else {
                defValStr = "DEFAULT " + val;
            }
        }

        if (databaseCategory.toLowerCase().equals("mysql")) {
            if (field.getColDataType().trim().toUpperCase().startsWith("TIMESTAMP") && field.getColNotNull().equals("")) {
                defValStr = "NULL DEFAULT NULL";
            } else if (field.getColDataType().trim().toUpperCase().startsWith("TIMESTAMP") && field.getColNotNull().equals("NOT NULL")) {
                defValStr = "DEFAULT '0000-00-00 00:00:00'";
            }
        }

        field.setColDefaultValue(defValStr);

        // process for Java Naming Rules
        field.setFieldId(typeMapper.dbName2JavaName(field.getColId()));
        field.setFieldIdCap(typeMapper.dbName2JavaGetterSetterName(field.getColId()));
        field.setFieldName(field.getColName());
    }

    /**
     * buildDtoAnnotation
     * 
     * @param dto
     */
    void buildDtoAnnotation(EntityDescBean dto) {
        dto.getClassAnnotationList().add(createAnnotation("JPA-Entity"));
        dto.getClassAnnotationList().add(createAnnotation("JPA-Table", dto.getTblId()));
        dto.getClassAnnotationList().add(createAnnotation("TableDesc", dto.getTblId(), dto.getTblName()));

        for (FieldDescBean field : dto.getFieldList()) {
            StringBuilder jpaColumnBuilder = new StringBuilder();
            jpaColumnBuilder.append("name=\"" + field.getColId() + "\"");

            String index = field.getColNo();
            index = index.contains(".") ? index.substring(0, index.indexOf(".")) : index;
            field.getFieldAnnotationList().add(createAnnotation("ColumnLabel", field.getColName()));
            field.getFieldAnnotationList().add(createAnnotation("ColumnSequence", index));

            // process for NotNull
            if (typeMapper.isNotEmpty(field.getColNotNull())) {
                field.getFieldAnnotationList().add(createAnnotation("ColumnNotEmpty"));
                jpaColumnBuilder.append(", nullable=false");
            } else {
                jpaColumnBuilder.append(", nullable=true");
            }
            // process for Length
            String colMaxLength = field.getColMaxLength();
            String colMinLength = field.getColMinLength();
            if (typeMapper.isNotEmpty(colMinLength) && typeMapper.isNotEmpty(colMaxLength)) {
                Long colMaxLengthIntValue = Long.valueOf(colMaxLength);
                String size = "max=" + colMaxLength;
                if (String.valueOf(field.getColDataType()).equals("CHAR")) {
                    size = "min=" + colMinLength + ", max=" + colMaxLength;
                }
                if (field.getColPrecisionLength() != null) {
                    size += ", precision=" + field.getColPrecisionLength();
                    jpaColumnBuilder.append(", length=" + colMaxLength);
                    jpaColumnBuilder.append(", scale=" + colMaxLength);
                    jpaColumnBuilder.append(", precision=" + field.getColPrecisionLength());
                } else {
                    if (colMaxLengthIntValue <= Integer.MAX_VALUE) {
                        jpaColumnBuilder.append(", length=" + colMaxLength);
                    }
                }
                if (colMaxLengthIntValue <= Integer.MAX_VALUE) {
                    field.getFieldAnnotationList().add(createAnnotation("ColumnLength", size));
                }
            }
            jpaColumnBuilder.append(", columnDefinition=\"" + field.getColDataTypeWithLength());
            if (field.getColNotNull() != null && !field.getColNotNull().trim().equals("")) {
                jpaColumnBuilder.append(" " + field.getColNotNull());
            }
            jpaColumnBuilder.append("\"");
            jpaColumnBuilder.append(", table=\"" + dto.getTblId() + "\"");

            // process for PK
            String isPrimaryKey = "false";
            if (typeMapper.isNotEmpty(field.getColPK())) {
                isPrimaryKey = "true";
                field.getFieldAnnotationList().add(createAnnotation("JPA-Id"));
            }
            // process for ConstValue
            if (typeMapper.isNotEmpty(field.getColConstValue())) {
                String str = field.getColConstValue();
                str = str.startsWith("\"") ? str : "\"" + str + "\"";
                field.getFieldAnnotationList().add(createAnnotation("ColumnConst", str));
            }
            // process for Range
            String range = null;
            if (typeMapper.isNotEmpty(field.getColMinValue())) {
                String str = field.getColMinValue();
                str = str.startsWith("\"") ? str : "\"" + str + "\"";
                range = "min=" + str;
            }
            if (typeMapper.isNotEmpty(field.getColMaxValue())) {
                String str = field.getColMaxValue();
                str = str.startsWith("\"") ? str : "\"" + str + "\"";
                if (range != null) {
                    range += ", ";
                }
                range += "max=" + str;
            }
            if (range != null) {
                field.getFieldAnnotationList().add(createAnnotation("ColumnRange", range));
            }
            // process for TextFormat
            String textFormat = null;
            if (typeMapper.isNotEmpty(field.getColTextFormat())) {
                String tf = field.getColTextFormat();
                switch (tf) {
                // 日时,日期,时间,英字,数字,英数,货币,电话,手机,邮编,EMAIL,ASCII,ASCII-NS
                case "日时": {
                    textFormat = "TextFormat.Category.Datetime";
                    break;
                }
                case "日期": {
                    textFormat = "TextFormat.Category.Date";
                    break;
                }
                case "时间": {
                    textFormat = "TextFormat.Category.Time";
                    break;
                }
                case "英字": {
                    textFormat = "TextFormat.Category.Alpha";
                    break;
                }
                case "数字": {
                    textFormat = "TextFormat.Category.Numeric";
                    break;
                }
                case "英数": {
                    textFormat = "TextFormat.Category.AlphaNumeric";
                    break;
                }
                case "货币": {
                    textFormat = "TextFormat.Category.Currency";
                    break;
                }
                case "电话": {
                    textFormat = "TextFormat.Category.TelNumber";
                    break;
                }
                case "手机": {
                    textFormat = "TextFormat.Category.MobileNumber";
                    break;
                }
                case "邮编": {
                    textFormat = "TextFormat.Category.ZipCode";
                    break;
                }
                case "EMAIL": {
                    textFormat = "TextFormat.Category.Email";
                    break;
                }
                case "ASCII": {
                    textFormat = "TextFormat.Category.Ascii";
                    break;
                }
                case "ASCII-NS": {
                    textFormat = "TextFormat.Category.AsciiNoneSpace";
                    break;
                }
                }
                if (textFormat != null) {
                    field.getFieldAnnotationList().add(createAnnotation("ColumnTextFormat", textFormat));
                }
            }

            String label = field.getColName();
            label = StringEscapeUtils.unescapeXml(label);
            label = label.contains("\n") ? label.replace("\n", " ") : label;
            field.getFieldAnnotationList().add(createAnnotation("ColumnDesc", index, field.getColId(), field.getColDataType(), isPrimaryKey, label));
            // jpaColumnBuilder.append(", columnDefinition=\"" + field.getColDataTypeWithLength() + "\"");
            field.getFieldAnnotationList().add(createAnnotation("JPA-Column", jpaColumnBuilder.toString()));
        }

        // move annotation's package define part to class import part
        Set<String> classImport = new HashSet<String>();
        List<String> annoList = dto.getClassAnnotationList();
        List<String> newAnnoList = new ArrayList<String>();
        for (String anno : annoList) {
            if (anno == null) {
                continue;
            }
            String impPart;
            String clspart;
            if (anno.contains("(")) {
                String s1 = anno.substring(0, anno.indexOf("("));
                String s2 = anno.substring(anno.indexOf("("));
                impPart = "import " + s1.substring(1) + ";";
                clspart = "@" + s1.substring(s1.lastIndexOf(".") + 1) + s2;
            } else {
                impPart = "import " + anno.substring(1) + ";";
                clspart = "@" + anno.substring(anno.lastIndexOf(".") + 1);
            }
            classImport.add(impPart);
            newAnnoList.add(clspart);
        }
        dto.setClassAnnotationList(newAnnoList);

        for (FieldDescBean field : dto.getFieldList()) {
            annoList = field.getFieldAnnotationList();
            newAnnoList = new ArrayList<String>();
            for (String anno : annoList) {
                if (anno == null) {
                    continue;
                }

                String impPart;
                String clspart;
                if (anno.contains("(")) {
                    String s1 = anno.substring(0, anno.indexOf("("));
                    String s2 = anno.substring(anno.indexOf("("));
                    impPart = "import " + s1.substring(1) + ";";
                    clspart = "@" + s1.substring(s1.lastIndexOf(".") + 1) + s2;
                } else {
                    impPart = "import " + anno.substring(1) + ";";
                    clspart = "@" + anno.substring(anno.lastIndexOf(".") + 1);
                }
                classImport.add(impPart);
                newAnnoList.add(clspart);
            }
            field.setFieldAnnotationList(newAnnoList);
        }
        dto.setClassImportList(classImport);
    }

    /**
     * createAnnotation
     * 
     * @param key
     * @param params
     * @return
     */
    protected String createAnnotation(String key, String... params) {
        String value = buildProperties.getProperty(key);
        if (value == null) {
            return null;
        }
        if (params != null) {
            for (int i = 1; i <= params.length; i++) {
                String param = params[i - 1];
                if (param == null || param.equals("null")) {
                    return null;
                    // value = null;
                }
                String match = "{$" + i + "}";
                if (value.contains(match)) {
                    value = value.replace(match, param);
                }
            }
        }
        return value;
    }

    /**
     * @return the genDtoExtension
     */
    public String getGenDtoExtension() {
        return genDtoExtension;
    }

    /**
     * @param genDtoExtension
     *            the genDtoExtension to set
     */
    public void setGenDtoExtension(String genDtoExtension) {
        this.genDtoExtension = genDtoExtension;
    }

    /**
     * @return the genCrudInterfaceExtension
     */
    public String getGenCrudInterfaceExtension() {
        return genCrudInterfaceExtension;
    }

    /**
     * @param genCrudInterfaceExtension
     *            the genCrudInterfaceExtension to set
     */
    public void setGenCrudInterfaceExtension(String genCrudInterfaceExtension) {
        this.genCrudInterfaceExtension = genCrudInterfaceExtension;
    }

    /**
     * @return the genCrudClassExtension
     */
    public String getGenCrudClassExtension() {
        return genCrudClassExtension;
    }

    /**
     * @param genCrudClassExtension
     *            the genCrudClassExtension to set
     */
    public void setGenCrudClassExtension(String genCrudClassExtension) {
        this.genCrudClassExtension = genCrudClassExtension;
    }

    /**
     * @return the builderProperties
     */
    public String getBuilderProperties() {
        return builderProperties;
    }

    /**
     * @param builderProperties
     *            the builderProperties to set
     */
    public void setBuilderProperties(String builderProperties) {
        this.builderProperties = builderProperties;
    }

    /**
     * @return the genCrudApiExtension
     */
    public String getGenCrudApiExtension() {
        return genCrudApiExtension;
    }

    /**
     * @param genCrudApiExtension
     *            the genCrudApiExtension to set
     */
    public void setGenCrudApiExtension(String genCrudApiExtension) {
        this.genCrudApiExtension = genCrudApiExtension;
    }

    /**
     * @return the genCrudApiPackageExtension
     */
    public String getGenCrudApiPackageExtension() {
        return genCrudApiPackageExtension;
    }

    /**
     * @param genCrudApiPackageExtension
     *            the genCrudApiPackageExtension to set
     */
    public void setGenCrudApiPackageExtension(String genCrudApiPackageExtension) {
        this.genCrudApiPackageExtension = genCrudApiPackageExtension;
    }

    /**
     * @return the genCrudPackageExtension
     */
    public String getGenCrudPackageExtension() {
        return genCrudPackageExtension;
    }

    /**
     * @param genCrudPackageExtension
     *            the genCrudPackageExtension to set
     */
    public void setGenCrudPackageExtension(String genCrudPackageExtension) {
        this.genCrudPackageExtension = genCrudPackageExtension;
    }

    /**
     * @return the genDtoConditionExtension
     */
    public String getGenDtoConditionExtension() {
        return genDtoConditionExtension;
    }

    /**
     * @param genDtoConditionExtension the genDtoConditionExtension to set
     */
    public void setGenDtoConditionExtension(String genDtoConditionExtension) {
        this.genDtoConditionExtension = genDtoConditionExtension;
    }

    /**
     * @return the genDtoCriteriaExtension
     */
    public String getGenDtoCriteriaExtension() {
        return genDtoCriteriaExtension;
    }

    /**
     * @param genDtoCriteriaExtension the genDtoCriteriaExtension to set
     */
    public void setGenDtoCriteriaExtension(String genDtoCriteriaExtension) {
        this.genDtoCriteriaExtension = genDtoCriteriaExtension;
    }

    /**
     * @return the genDtoMapperExtension
     */
    public String getGenDtoMapperExtension() {
        return genDtoMapperExtension;
    }

    /**
     * @param genDtoMapperExtension the genDtoMapperExtension to set
     */
    public void setGenDtoMapperExtension(String genDtoMapperExtension) {
        this.genDtoMapperExtension = genDtoMapperExtension;
    }

    /**
     * @return the genMapperPackageExtension
     */
    public String getGenMapperPackageExtension() {
        return genMapperPackageExtension;
    }

    /**
     * @param genMapperPackageExtension the genMapperPackageExtension to set
     */
    public void setGenMapperPackageExtension(String genMapperPackageExtension) {
        this.genMapperPackageExtension = genMapperPackageExtension;
    }

    /**
     * @return the genDtoPackageExtension
     */
    public String getGenDtoPackageExtension() {
        return genDtoPackageExtension;
    }

    /**
     * @param genDtoPackageExtension the genDtoPackageExtension to set
     */
    public void setGenDtoPackageExtension(String genDtoPackageExtension) {
        this.genDtoPackageExtension = genDtoPackageExtension;
    }

}
