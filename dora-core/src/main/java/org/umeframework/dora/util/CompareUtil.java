/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

/**
 * Data compare utilities methods.<br>
 * 
 * @author Yue MA
 */
abstract public class CompareUtil {

    /**
     * Greater than comparing.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean greaterThan(
            E value1,
            E value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        int result = value1.compareTo(value2);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * Less than comparing.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean lessThan(
            E value1,
            E value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        int result = value1.compareTo(value2);
        if (result < 0) {
            return true;
        }
        return false;
    }

    /**
     * Equal comparing.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean equalTo(
            E value1,
            E value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        int result = value1.compareTo(value2);
        if (result == 0) {
            return true;
        }
        return false;
    }

    /**
     * Return true when 2 operands are both zero.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E> boolean zeroZero(
            Long value1,
            Long value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        if (value1.equals(0L) && value2.equals(0L)) {
            return true;
        }
        return false;
    }

    /**
     * Return true when 2 operands are both not zero.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E> boolean zeroNonzero(
            Long value1,
            Long value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        if (value1.equals(0L) && !value2.equals(0L)) {
            return true;
        }
        return false;
    }

    /**
     * Return true when 1st operand <> 0 but 2nd operand = 0.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E> boolean nonzeroZero(
            Long value1,
            Long value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        if (!value1.equals(0L) && value2.equals(0L)) {
            return true;
        }
        return false;
    }

    /**
     * Return true when 1st operand <> 0 and 2nd operand <> 0.<br>
     * 
     * @param value1
     *            - 1st operand
     * @param value2
     *            - 2nd operand
     * @return true/false
     */
    public static <E> boolean nonzeroNonzero(
            Long value1,
            Long value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        if (!value1.equals(0L) && !value2.equals(0L)) {
            return true;
        }
        return false;
    }

    /**
     * Return true when all list 1 values < list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean allLessThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }

        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                return false;
            }
            int result = op1.compareTo(op2);
            if (result >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true when not all list 1 values < list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean allNotLessThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                return false;
            }
            int result = op1.compareTo(op2);
            if (result < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true when all list 1 values > list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean allGreaterThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                return false;
            }
            int result = op1.compareTo(op2);
            if (result <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true when not all list 1 values > list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean allNotGreaterThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                return false;
            }
            int result = op1.compareTo(op2);
            if (result > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true when all list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean allEqual(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                return false;
            }
            int result = op1.compareTo(op2);
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true when not all list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean allNotEqual(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                return false;
            }
            int result = op1.compareTo(op2);
            if (result == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true when any list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyLessThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                continue;
            }
            int result = op1.compareTo(op2);
            if (result < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true when not all list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyNotLessThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                continue;
            }
            int result = op1.compareTo(op2);
            if (result >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true when any list 1 values > list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyGreaterThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                continue;
            }
            int result = op1.compareTo(op2);
            if (result > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true when not all list 1 values > list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyNotGreaterThan(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                continue;
            }
            int result = op1.compareTo(op2);
            if (result <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true when any list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyEqual(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                continue;
            }
            int result = op1.compareTo(op2);
            if (result == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true when not all list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyNotEqual(
            E[] values1,
            E[] values2) {
        if (values1.length != values2.length) {
            return false;
        }
        for (int i = 0; i < values1.length; i++) {
            E op1 = values1[i];
            E op2 = values2[i];

            if (op1 == null || op2 == null) {
                continue;
            }
            int result = op1.compareTo(op2);
            if (result != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true when any list 1 values = list 2 values.<br>
     * 
     * @param values1
     *            - value list 1
     * @param values2
     *            - value list 2
     * @return true/false
     */
    public static <E extends Comparable<? super E>> boolean anyEqual(
            E value,
            E[] values) {
        for (int i = 0; i < values.length; i++) {
            E op = values[i];

            if (value == null || op == null) {
                continue;
            }
            int result = value.compareTo(op);
            if (result == 0) {
                return true;
            }
        }
        return false;
    }

}
