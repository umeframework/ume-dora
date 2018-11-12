/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * NumberUtil
 * 
 * @author Yue MA
 * 
 */
abstract public class NumberUtil {
    /**
     * Text "0"
     */
    private static final String ZERO_STR = "0";
    /**
     * Text "+"
     */
    private static final String PLUS_STR = "+";
    /**
     * Text "-"
     */
    private static final String MINUS_STR = "-";
    /**
     * Numeric format "#.###"
     */
    private static final String NUMBER_FORMAT = "#.###";

    /**
     * sumAsLong
     * 
     * @param values
     * @return
     */
    public static long sum(
            Integer... values) {
        long result = 0L;
        if (values == null) {
            return result;
        }
        for (Integer value : values) {
            if (value != null) {
                result += value;
            }
        }
        return result;
    }

    /**
     * sumAsLong
     * 
     * @param values
     * @return
     */
    public static long sum(
            Long... values) {
        long result = 0L;
        if (values == null) {
            return result;
        }
        for (Long value : values) {
            if (value != null) {
                result += value;
            }
        }
        return result;
    }

    /**
     * sumAsLong
     * 
     * @param values
     * @return
     */
    public static long sum(
            String... values) {
        long result = 0L;
        if (values == null) {
            return result;
        }
        for (String value : values) {
            if (StringUtil.isNotEmpty(value)) {
                result += StringUtil.toLong(value);
            }
        }
        return result;
    }

    /**
     * sumAsLong
     * 
     * @param values
     * @return
     */
    public static long sum(
            List<?> values) {
        long result = 0L;
        if (values == null) {
            return result;
        }
        Iterator<?> it = values.iterator();
        while (it.hasNext()) {
            Object ob = it.next();
            if (ob != null) {
                result += StringUtil.toLong(ob.toString());
            }
        }
        return result;
    }

    /**
     * sumAsInteger
     * 
     * @param values
     * @return
     */
    public static Integer sumAsInteger(
            Integer... values) {
        if (values == null) {
            return null;
        }
        boolean isNullFlg = true;
        for (Integer value : values) {
            if (value != null) {
                isNullFlg = false;
                break;
            }
        }
        if (isNullFlg) {
            return null;
        }

        int result = 0;
        for (Integer value : values) {
            if (value != null) {
                result += value;
            }
        }
        return result;
    }

    /**
     * sumAsInteger
     * 
     * @param values
     * @return
     */
    public static Integer sumAsInteger(
            String... values) {
        if (values == null) {
            return null;
        }
        boolean isNullFlg = true;
        for (String value : values) {
            if (StringUtil.isNotEmpty(value)) {
                isNullFlg = false;
                break;
            }
        }
        if (isNullFlg) {
            return null;
        }

        int result = 0;
        for (String value : values) {
            if (StringUtil.isNotEmpty(value)) {
                result += toInt(value);
            }
        }
        return result;
    }

    /**
     * sumAsInteger
     * 
     * @param values
     * @return
     */
    public static Integer sumAsInteger(
            List<?> values) {
        if (values == null) {
            return null;
        }
        boolean isNullFlg = true;
        Iterator<?> it1 = values.iterator();
        Iterator<?> it2 = values.iterator();
        while (it1.hasNext()) {
            if (it1.next() != null) {
                isNullFlg = false;
                break;
            }
        }
        if (isNullFlg) {
            return null;
        }

        int result = 0;
        while (it2.hasNext()) {
            Object ob = it2.next();
            if (ob != null) {
                result += toInt(ob.toString());
            }
        }
        return result;
    }

    /**
     * sumAsBigDecimal
     * 
     * @param values
     * @return
     */
    public static BigDecimal sumAsBigDecimal(
            String... values) {
        BigDecimal result = BigDecimal.valueOf(0);
        if (values == null) {
            return result;
        }
        for (String value : values) {
            if (StringUtil.isNotEmpty(value)) {
                result = result.add(toBigDecimal(value));
            }
        }
        return result;
    }

    /**
     * sumAsBigDecimal
     * 
     * @param values
     * @return
     */
    public static BigDecimal sumAsBigDecimal(
            BigDecimal... values) {
        BigDecimal result = BigDecimal.valueOf(0);
        if (values == null) {
            return result;
        }
        for (BigDecimal value : values) {
            if (value != null) {
                result = result.add(value);
            }
        }
        return result;
    }

    /**
     * sumAsBigInteger
     * 
     * @param values
     * @return
     */
    public static BigInteger sumAsBigInteger(
            BigInteger... values) {
        BigInteger result = BigInteger.valueOf(0);
        if (values == null) {
            return result;
        }
        for (BigInteger value : values) {
            if (value != null) {
                result = result.add(value);
            }
        }
        return result;
    }

    /**
     * toInt
     * 
     * @param value
     * @return
     */
    public static int toInt(
            BigDecimal value) {
        return value.intValue();
    }

    /**
     * toInt
     * 
     * @param value
     * @return
     */
    public static int toInt(
            BigInteger value) {
        return value.intValue();
    }

    /**
     * toInt
     * 
     * @param value
     * @return
     */
    public static int toInt(
            String value) {
        if (StringUtil.isEmpty(value)) {
            value = ZERO_STR;
        }
        return StringUtil.toInteger(value);
    }

    /**
     * toLong
     * 
     * @param value
     * @return
     */
    public static long toLong(
            BigDecimal value) {
        return value.longValue();
    }

    /**
     * toLong
     * 
     * @param value
     * @return
     */
    public static long toLong(
            BigInteger value) {
        return value.longValue();
    }

    /**
     * toLong
     * 
     * @param value
     * @return
     */
    public static long toLong(
            String value) {
        if (StringUtil.isEmpty(value)) {
            value = ZERO_STR;
        }
        return StringUtil.toLong(value);
    }

    /**
     * toBigInteger
     * 
     * @param value
     * @return
     */
    public static BigInteger toBigInteger(
            Number value) {
        return value == null ? null : BigInteger.valueOf(value.longValue());
    }

    /**
     * toBigInteger
     * 
     * @param value
     * @return
     */
    public static BigInteger toBigInteger(
            String value) {
        if (StringUtil.isEmpty(value)) {
            value = ZERO_STR;
        }
        return StringUtil.toBigInteger(value);
    }

    /**
     * toFloat
     * 
     * @param value
     * @return
     */
    public static float toFloat(
            String value) {
        if (StringUtil.isEmpty(value)) {
            value = ZERO_STR;
        }
        return StringUtil.toFloat(value);
    }

    /**
     * toDouble
     * 
     * @param value
     * @return
     */
    public static double toDouble(
            String value) {
        if (StringUtil.isEmpty(value)) {
            value = ZERO_STR;
        }
        return StringUtil.toDouble(value);
    }

    /**
     * toBigDecimal
     * 
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(
            Number value) {
        return value == null ? null : new BigDecimal(value.toString());
    }

    /**
     * toBigDecimal
     * 
     * @param value
     * @return
     */
    public static BigDecimal toBigDecimal(
            String value) {
        if (StringUtil.isEmpty(value)) {
            value = ZERO_STR;
        }
        return StringUtil.toBigDecimal(value);
    }

    /**
     * round
     * 
     * @param value
     * @param scale
     * @param roundingMode
     * @return
     */
    public static BigDecimal round(
            BigDecimal value,
            int scale,
            RoundingMode roundingMode) {
        MathContext mathContext = new MathContext(value.precision() - value.scale() + scale);
        if (roundingMode instanceof RoundingMode) {
            mathContext = new MathContext(value.precision() - value.scale() + scale, roundingMode);
        }
        value = value.round(mathContext);
        return value;
    }

    /**
     * round
     * 
     * @param value
     * @param scale
     * @param roundingMode
     * @return
     */
    public static BigDecimal round(
            String value,
            int scale,
            RoundingMode roundingMode) {
        return round(StringUtil.toBigDecimal(value), scale, roundingMode);
    }

    /**
     * add
     * 
     * @param operand1
     * @param operand2
     * @return
     */
    public static BigDecimal add(
            Object operand1,
            Object operand2) {
        BigDecimal op1 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand1));
        BigDecimal op2 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand2));
        return op1.add(op2);
    }

    /**
     * sub
     * 
     * @param operand1
     * @param operand2
     * @return
     */
    public static BigDecimal sub(
            Object operand1,
            Object operand2) {
        BigDecimal op1 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand1));
        BigDecimal op2 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand2));
        return op1.subtract(op2);
    }

    /**
     * mul
     * 
     * @param operand1
     * @param operand2
     * @return
     */
    public static BigDecimal mul(
            Object operand1,
            Object operand2) {
        BigDecimal op1 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand1));
        BigDecimal op2 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand2));
        return op1.multiply(op2);
    }

    /**
     * div
     * 
     * @param operand1
     * @param operand2
     * @param scale
     * @return
     */
    public static BigDecimal div(
            Object operand1,
            Object operand2,
            int scale) {
        return div(operand1, operand2, scale, RoundingMode.HALF_UP);
    }

    /**
     * div
     * 
     * @param operand1
     * @param operand2
     * @param scale
     * @param roundMode
     * @return
     */
    public static BigDecimal div(
            Object operand1,
            Object operand2,
            int scale,
            RoundingMode roundMode) {
        BigDecimal op1 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand1));
        BigDecimal op2 = StringUtil.toBigDecimal(StringUtil.objectToStr(operand2));
        return op1.divide(op2, scale, roundMode);
    }

    /**
     * div
     * 
     * @param operand1
     * @param operand2
     * @param scale
     * @param roundMode
     * @param mode
     * @return
     */
    public static String div(
            Object operand1,
            Object operand2,
            int scale,
            RoundingMode roundMode,
            int mode) {
        DecimalFormat format = new DecimalFormat(NUMBER_FORMAT);
        return format.format(div(operand1, operand2, scale, roundMode));
    }

    /**
     * getSignum
     * 
     * @param numStr
     * @return
     */
    public static String getSignum(
            Number numStr) {
        String result = null;
        if (numStr != null) {
            if (new BigDecimal(numStr.toString()).compareTo(BigDecimal.ZERO) >= 0) {
                result = PLUS_STR;
            } else {
                result = MINUS_STR;
            }
        }
        return result;
    }

    /**
     * getAbsValue
     * 
     * @param numStr
     * @return
     */
    public static String getAbsValue(
            Number numStr) {
        return numStr == null ? null : StringUtil.objectToStr((new BigDecimal(numStr.toString()).abs()));
    }

}
