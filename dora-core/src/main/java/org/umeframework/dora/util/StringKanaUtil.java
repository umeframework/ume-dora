package org.umeframework.dora.util;

/**
 * String common process tool for KANA charSet
 * 
 * @author Yue MA
 */
abstract public class StringKanaUtil extends StringUtil {
    /**
     * ZenKaku Space char
     */
    private static final char ZENKAKU_SPACE_CHR = '　';
    /**
     * Half width characters
     */
    private static final String HANKAKU_LST = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGH" + "IJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnop" + "qrstuvwxyz{|}~｡｢｣､･ｧｨｩｪｫｬｭｮｯｰｱｲｴｵﾅﾆﾇﾈﾉ" + "ﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝﾞﾟ ";
    /**
     * Half-Kana width characters
     */
    private static final String HANKAKU_KASATAHA_LST = "ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ";
    /**
     * Half-Kana width characters of Ha set
     */
    private static final String HANKAKU_HA_LST = "ﾊﾋﾌﾍﾎ";
    /**
     * ZenKaku width characters
     */
    private static final String ZENKAKU_LST = "！”＃＄％＆’（）＊＋，－．／０１２３４" + "５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨ" + "ＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥" + "］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐ" + "ｑｒｓｔｕｖｗｘｙｚ｛｜｝￣。「」、・" + "ァィゥェォャュョッーアイエオナニヌネノ" + "マミムメモヤユヨラリルレロン゛゜　";
    /**
     * ZenKaku (Ka,Sa,Ta,Ha lines) width characters
     */
    private static final String ZENKAKU_KASATAHA_LST = "カキクケコサシスセソタチツテトハヒフヘホウ";
    /**
     * ZenKaku (Ga,Za,Da,Ba lines) width characters
     */
    private static final String ZENKAKU_GAZADABA_LST = "ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ";
    /**
     * ZenKaku (Pa line) width characters
     */
    private static final String ZENKAKU_PA_LST = "パピプペポ";
    /**
     * Low case Kana characters
     */
    private static final Character[] KANA_LOWER = { 'ｧ', 'ｨ', 'ｩ', 'ｪ', 'ｫ', 'ｯ', 'ｬ', 'ｭ', 'ｮ' };
    /**
     * Up case Kana characters
     */
    private static final Character[] KANA_UPPER = { 'ｱ', 'ｲ', 'ｳ', 'ｴ', 'ｵ', 'ﾂ', 'ﾔ', 'ﾕ', 'ﾖ' };
    /**
     * ZenKaku (ワ"[&yen;30f7])
     */
    private static final Character ZENKAKU_WA_DAKUTEN_CHR = new Character('\u30f7');
    /**
     * ZenKaku (ヲ"[&yen;30fa])
     */
    private static final Character ZENKAKU_WO_DAKUTEN_CHR = new Character('\u30fa');

    /**
     * isZenHankakuSpace
     * 
     * @param value
     * @return
     */
    public static boolean isZenHankakuSpace(
            char value) {
        return (value == ZENKAKU_SPACE_CHR);
    }

    /**
     * trimZ
     * 
     * @param value
     * @return
     */
    public static String trimZ(
            String value) {
        return trimLeftZ(trimRightZ(value));
    }

    /**
     * trimLeftZ
     * 
     * @param value
     * @return
     */
    public static String trimLeftZ(
            String value) {
        if (value == null) {
            return null;
        }

        int start = 0;
        int length = value.length();
        while ((start < length) && isZenHankakuSpace(value.charAt(start))) {
            start++;
        }

        return start > 0 ? value.substring(start, length) : value;
    }

    /**
     * trimRightZ
     * 
     * @param value
     * @return
     */
    public static String trimRightZ(
            String value) {
        if (value == null) {
            return null;
        }

        int length = value.length();
        while ((0 < length) && isZenHankakuSpace(value.charAt(length - 1))) {
            length--;
        }

        return length < value.length() ? value.substring(0, length) : value;
    }

    /**
     * isEmptyHankakuAndZenkaku
     * 
     * @param value
     * @return
     */
    public static boolean isEmptyHankakuAndZenkaku(
            String value) {
        if (value == null || trimZ(value).equals(EMPTY)) {
            return true;
        }
        return false;
    }

    /**
     * hankakuToZenkaku
     * 
     * @param value
     * @return
     */
    public static String hankakuToZenkaku(
            String value) {

        if (value == null || EMPTY.equals(value)) {
            return value;
        }

        char[] chars = value.toCharArray();
        StringBuilder returnValue = new StringBuilder();
        String getValue = null;
        Character nextvalue = null;

        for (int i = 0; i < chars.length; i++) {

            getValue = getZenkakuMoji(chars[i]);

            if (getValue != null) {
                returnValue.append(getValue);
            } else if (i == (chars.length - 1)) {
                getValue = getZenkakuKasatahaMoji(chars[i]);
                if (getValue != null) {
                    returnValue.append(getValue);
                } else if (Character.valueOf(chars[i]).equals(new Character('ﾜ'))) {
                    returnValue.append("ワ");
                } else if (Character.valueOf(chars[i]).equals(new Character('ｦ'))) {
                    returnValue.append("ヲ");
                } else {
                    returnValue.append(String.valueOf(chars[i]));
                }
            } else {
                nextvalue = Character.valueOf(chars[i + 1]);
                if (nextvalue.equals(new Character('ﾞ'))) {
                    getValue = getZenkakuDakuMoji(chars[i]);
                    if (getValue != null) {
                        returnValue.append(getValue);
                        i++;
                    } else if (Character.valueOf(chars[i]).equals(new Character('ﾜ'))) {
                        // ﾜﾞ
                        returnValue.append(ZENKAKU_WA_DAKUTEN_CHR);
                        i++;
                    } else if (Character.valueOf(chars[i]).equals(new Character('ｦ'))) {
                        // ｦﾞ
                        returnValue.append(ZENKAKU_WO_DAKUTEN_CHR);
                        i++;
                    } else {
                        returnValue.append((String.valueOf(chars[i]) + "゛"));
                        i++;
                    }
                } else if (nextvalue.equals(new Character('ﾟ'))) {
                    getValue = getZenkakuHandakuMoji(chars[i]);
                    if (getValue != null) {
                        returnValue.append(getValue);
                        i++;
                    } else {
                        getValue = getZenkakuKasatahaMoji(chars[i]);
                        returnValue.append((String.valueOf(getValue) + "゜"));
                        i++;
                    }
                } else {
                    getValue = getZenkakuKasatahaMoji(chars[i]);
                    if (getValue != null) {
                        returnValue.append(getValue);
                    } else if (Character.valueOf(chars[i]).equals(new Character('ﾜ'))) {
                        returnValue.append("ワ");
                    } else if (Character.valueOf(chars[i]).equals(new Character('ｦ'))) {
                        returnValue.append("ヲ");
                    } else {
                        returnValue.append(String.valueOf(chars[i]));
                    }
                }
            }
        }
        return returnValue.toString();
    }

    /**
     * zenkakuToHankaku
     * 
     * @param value
     * @return
     */
    public static String zenkakuToHankaku(
            String value) {

        if (value == null || EMPTY.equals(value)) {
            return value;
        }

        char[] chars = value.toCharArray();
        StringBuilder returnValue = new StringBuilder();
        String getValue = null;

        for (int i = 0; i < chars.length; i++) {

            getValue = getHankakuMoji(chars[i]);

            if (getValue != null) {
                returnValue.append(getValue);
            } else {
                returnValue.append(String.valueOf(chars[i]));
            }
        }
        return returnValue.toString();
    }

    /**
     * getZenkakuMoji
     * 
     * @param value
     * @return
     */
    private static String getZenkakuMoji(
            char value) {

        int index = HANKAKU_LST.indexOf(value);

        if (index >= 0) {
            return String.valueOf(ZENKAKU_LST.charAt(index));
        }
        return null;
    }

    /**
     * getZenkakuDakuMoji
     * 
     * @param value
     * @return
     */
    private static String getZenkakuDakuMoji(
            char value) {

        int index = HANKAKU_KASATAHA_LST.indexOf(value);
        if (index >= 0) {
            return String.valueOf(ZENKAKU_GAZADABA_LST.charAt(index));
        }
        return null;
    }

    /**
     * getZenkakuHandakuMoji
     * 
     * @param value
     * @return
     */
    private static String getZenkakuHandakuMoji(
            char value) {

        int index = HANKAKU_HA_LST.indexOf(value);
        if (index >= 0) {
            return String.valueOf(ZENKAKU_PA_LST.charAt(index));
        }
        return null;
    }

    /**
     * getZenkakuKasatahaMoji
     * 
     * @param value
     * @return
     */
    private static String getZenkakuKasatahaMoji(
            char value) {

        int index = HANKAKU_KASATAHA_LST.indexOf(value);
        if (index >= 0) {
            return String.valueOf(ZENKAKU_KASATAHA_LST.charAt(index));
        }
        return null;
    }

    /**
     * getHankakuMoji
     * 
     * @param value
     * @return
     */
    private static String getHankakuMoji(
            char value) {
        int index = 0;
        String tmpValue = null;
        index = ZENKAKU_LST.indexOf(value);
        if (index >= 0) {
            return String.valueOf(HANKAKU_LST.charAt(index));
        }

        index = ZENKAKU_KASATAHA_LST.indexOf(value);
        if (index >= 0) {
            return String.valueOf(HANKAKU_KASATAHA_LST.charAt(index));
        }

        index = ZENKAKU_GAZADABA_LST.indexOf(value);
        if (index >= 0) {
            tmpValue = String.valueOf(HANKAKU_KASATAHA_LST.charAt(index));
            return tmpValue + "ﾞ";
        }

        index = ZENKAKU_PA_LST.indexOf(value);
        if (index >= 0) {
            tmpValue = String.valueOf(HANKAKU_HA_LST.charAt(index));
            return tmpValue + "ﾟ";
        } else if ((Character.valueOf(value)).equals(new Character('ワ'))) {
            return "ﾜ";
        } else if ((Character.valueOf(value)).equals(new Character('ヲ'))) {
            return "ｦ";
        } else if ((Character.valueOf(value)).equals(ZENKAKU_WA_DAKUTEN_CHR)) {
            return "ﾜﾞ";
        } else if ((Character.valueOf(value)).equals(ZENKAKU_WO_DAKUTEN_CHR)) {
            return "ｦﾞ";
        } else {
            return null;
        }
    }

    /**
     * hasZenkaku
     * 
     * @param input
     * @return
     */
    public static boolean hasZenkaku(
            String input) {
        boolean result = false;
        String fieldValueStr = input.toString();
        for (int i = 0; i < fieldValueStr.length(); i++) {
            char checkChar = fieldValueStr.charAt(i);
            if (HANKAKU_LST.indexOf(checkChar) < 0 && HANKAKU_KASATAHA_LST.indexOf(checkChar) < 0 && HANKAKU_HA_LST.indexOf(checkChar) < 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * isZenkaku
     * 
     * @param input
     * @return
     */
    public static boolean isZenkaku(
            String input) {
        boolean result = true;
        String fieldValueStr = input.toString();
        for (int i = 0; i < fieldValueStr.length(); i++) {
            char checkChar = fieldValueStr.charAt(i);
            if (HANKAKU_LST.indexOf(checkChar) < 0 && HANKAKU_KASATAHA_LST.indexOf(checkChar) < 0 && HANKAKU_HA_LST.indexOf(checkChar) < 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * getKanaUpper
     * 
     * @param input
     * @return
     */
    public static String getKanaUpperCase(
            String input) {
        String outText = input;
        for (int i = 0; i < KANA_LOWER.length; i++) {
            outText = outText.replace(KANA_LOWER[i], KANA_UPPER[i]);
        }
        return outText;
    }

}
