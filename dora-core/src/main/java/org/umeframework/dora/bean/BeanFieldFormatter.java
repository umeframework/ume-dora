/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.lang.annotation.Annotation;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.umeframework.dora.type.DateConvert;
import org.umeframework.dora.type.ExFormatter;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.validation.constraints.ConstValue;
import org.umeframework.dora.validation.constraints.Size;
import org.umeframework.dora.validation.format.LeftChar;
import org.umeframework.dora.validation.format.Padding;
import org.umeframework.dora.validation.format.RightChar;
import org.umeframework.dora.validation.format.SignedNumber;

/**
 * Format bean properties with annotation define.<br>
 * 
 * @author Yue MA
 */
public class BeanFieldFormatter implements BeanFormatUtil.Formatter {

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.util.BeanFormatUtil.Formatter#format(java.lang.String,
     * java.lang.Object, java.lang.Class, java.lang.annotation.Annotation[])
     */
    public Object format(
            String name,
            Object value,
            Class<?> type,
            Annotation[] annotations) {
        if (value == null) {
            return null;
        }
        if (type == null) {
            type = value.getClass();
        }
        if (type.equals(String.class)) {
            return doSringFormat(name, (String) value, type, annotations);
        }

        return StringUtil.toObject(doSringFormat(name, StringUtil.objectToStr(value), type, annotations), type);
    }

    /**
     * Format String type property
     * 
     * @param name
     *            - property name
     * @param value
     *            - property value
     * @param type
     *            - property type
     * @param annotations
     *            - annotations declare on property
     * @return value after format
     */
    private String doSringFormat(
            String name,
            String value,
            Class<?> type,
            Annotation[] annotations) {
        value = value == null ? "" : value;

        // get annotation definition
        String constChar = null;
        String dateFormatFrom = null;
        String dateFormattTo = null;
        int length = -1;
        int precision = -1;
        SignedNumber.Type signedNumber = null;
        Character paddingType = null;
        Character paddingChar = null;
        String leftChar = null;
        boolean leftCharBeforePadding = false;
        String rightChar = null;
        boolean rightCharBeforePadding = false;
        Class<? extends BeanFormatUtil.Formatter> exFormatter = null;

        for (Annotation anno : annotations) {
            // get annotation type
            Class<? extends Annotation> annoType = anno.annotationType();
            if (ConstValue.class.equals(annoType)) {
                // use first element in const define collection
                constChar = ((ConstValue) anno).value()[0];
            } else if (DateConvert.class.equals(annoType)) {
                dateFormatFrom = ((DateConvert) anno).from();
                dateFormattTo = ((DateConvert) anno).to();
            } else if (Size.class.equals(annoType)) {
                length = ((Size) anno).max();
                precision = ((Size) anno).precision();
            } else if (SignedNumber.class.equals(annoType)) {
                signedNumber = ((SignedNumber) anno).value();
            } else if (Padding.class.equals(annoType)) {
                paddingType = ((Padding) anno).type();
                paddingChar = ((Padding) anno).value();
            } else if (LeftChar.class.equals(annoType)) {
                leftChar = ((LeftChar) anno).value();
                leftCharBeforePadding = ((LeftChar) anno).beforePadding();
            } else if (RightChar.class.equals(annoType)) {
                rightChar = ((RightChar) anno).value();
                rightCharBeforePadding = ((RightChar) anno).beforePadding();
            } else if (ExFormatter.class.equals(annoType)) {
                exFormatter = ((ExFormatter) anno).value();
            }
        }

        // do formatting by priority
        if (constChar != null) {
            // do constant char
            return constChar;
        }
        if (dateFormatFrom != null && dateFormattTo != null) {
            value = doDateConvert(value, dateFormatFrom, dateFormattTo);
        }

        if (leftCharBeforePadding) {
            // do left char append before padding
            value = leftChar + value;
        }
        if (rightCharBeforePadding) {
            // do right char append before padding
            value = value + rightChar;
        }

        if (precision != -1 && StringUtil.isNotEmpty(value)) {
            // do decimal padding
            value = doDecimalPadding(value, length, precision);
        }
        if (paddingType != null) {
            // do default padding
            paddingChar = paddingChar == null ? Padding.SPACE : paddingChar;
            value = doPadding(value, length, signedNumber, paddingType, paddingChar);
        } else if (paddingType == null && length != -1) {
            // do normal padding
            paddingType = Padding.LEFT;
            paddingChar = paddingChar == null ? Padding.SPACE : paddingChar;
            value = doPadding(value, length, signedNumber, paddingType, paddingChar);
        }

        if (leftChar != null && rightChar != null) {
            // do left & right appending
            value = leftChar + value + rightChar;
        } else if (leftChar != null) {
            // do left appending
            value = leftChar + value;
        } else if (rightChar != null) {
            // do right appending
            value = value + rightChar;
        }

        if (exFormatter != null && !exFormatter.equals(BeanFieldFormatter.class)) {
            // do extend formatting while there has extend formatter provided
            BeanFormatUtil.Formatter instance = null;
            try {
                instance = exFormatter.newInstance();
                value = StringUtil.objectToStr(instance.format(name, value, type, annotations));
            } catch (Exception e) {
                throw new BeanException("Fail to format bean field ", e);
            }
        }
        return value;
    }

    /**
     * Do padding for Decimal type
     * 
     * @param value
     *            - decimal value as String
     * @param len
     *            - length
     * @param precision
     *            - precision
     * @return value after format
     */
    protected String doDecimalPadding(
            String value,
            int len,
            int precision) {
        String signed = "";
        if (value.startsWith("-")) {
            signed = "-";
            value = value.substring(1);
        } else if (value.startsWith("+")) {
            signed = "+";
            value = value.substring(1);
        }

        if (value.contains(".") && precision != -1) {
            String intValue = value.substring(0, value.indexOf("."));
            String scaleValue = value.substring(value.indexOf(".") + 1);
            scaleValue = doPadding(scaleValue, precision, null, Padding.LEFT, Padding.ZERO);
            value = intValue + "." + scaleValue;
            int paddingLen = len - precision - 1;
            if (value.length() < paddingLen) {
                value = doPadding(value, paddingLen, null, Padding.RIGHT, Padding.ZERO);
            }
        } else if (!value.contains(".") && precision != -1) {
            value = value + ".";
            value = doPadding(value, len, null, Padding.LEFT, Padding.ZERO);
        } else {
            value = doPadding(value, len, null, Padding.RIGHT, Padding.ZERO);
        }

        return signed + value;
    }

    /**
     * Do padding on property
     * 
     * @param value
     *            - property value
     * @param len
     *            - length
     * @param signedNumber
     *            - signed type (+ or -)
     * @param paddingType
     *            - padding category
     * @param paddingChar
     *            - padding use char
     * @return value after padding
     */
    protected String doPadding(
            String value,
            int length,
            SignedNumber.Type signedNumber,
            Character paddingType,
            Character paddingChar) {
        if (length <= 0 || value.length() >= length) {
            return value;
        }
        String signed = "";
        if (signedNumber != null) {
            if (value.startsWith("-")) {
                signed = "-";
                value = value.substring(1);
                length = length - 1;
            } else if (value.startsWith("+") && !SignedNumber.Type.PLUS_REMOVE.equals(signedNumber)) {
                signed = "+";
                value = value.substring(1);
                length = length - 1;
            } else if (value.startsWith("+") && SignedNumber.Type.PLUS_REMOVE.equals(signedNumber)) {
                value = value.substring(1);
            } else if (!value.startsWith("+") && SignedNumber.Type.PLUS_APPEND.equals(signedNumber)) {
                signed = "+";
                length = length - 1;
            }
        }

        int loopCnt = length - value.length();
        StringBuffer buf = !"".endsWith(signed) && StringUtil.isEmpty(value) ? new StringBuffer(" ") : new StringBuffer(signed);

        if (Padding.RIGHT == paddingType) {
            for (int i = 0; i < loopCnt; i++) {
                buf.append(paddingChar);
            }
            buf.append(value);
        } else {
            buf.append(value);
            for (int i = 0; i < loopCnt; i++) {
                buf.append(paddingChar);
            }
        }
        value = buf.toString();

        return value;
    }

    /**
     * Do Date convert on property
     * 
     * @param value
     *            - property value
     * @param fromStrFormat
     *            - source date format
     * @param toStrFormat
     *            - target date format
     * @return value after convert
     */
    protected String doDateConvert(
            String value,
            String fromStrFormat,
            String toStrFormat) {
        SimpleDateFormat fromFormat = new SimpleDateFormat(fromStrFormat);
        fromFormat.setLenient(false);
        SimpleDateFormat toFormat = new SimpleDateFormat(toStrFormat);
        toFormat.setLenient(false);
        java.util.Date fromDate = null;
        try {
            fromDate = fromFormat.parse(value);
        } catch (ParseException e) {
            throw new BeanException("Fail to format bean field ", e);
        }
        return fromDate != null ? toFormat.format(fromDate) : null;
    }
}
