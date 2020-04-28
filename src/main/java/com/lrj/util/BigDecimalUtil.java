package com.lrj.util;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class BigDecimalUtil {
    public static final int ROUND_HALF_UP = 4;
    public static final int ROUND_HALF_DOWN = 5;
    public static final int ROUND_DOWN = 1;
    public static final int ROUND_UP = 0;
    public static final int DEF_SCALE_0 = 0;
    public static final int DEF_SCALE_1 = 1;
    public static final int DEF_SCALE_2 = 2;
    public static final int DEF_SCALE_3 = 3;
    public static final int DEF_SCALE_4 = 4;

    public BigDecimalUtil() {
    }

    public static double add(double arg0, double arg1) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.add(value2).doubleValue();
    }

    public static double add(double arg0, double arg1, int scale, int roundingMode) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.add(value2).setScale(scale, roundingMode).doubleValue();
    }

    public static double subtract(double arg0, double arg1) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.subtract(value2).doubleValue();
    }

    public static double subtract(double arg0, double arg1, int scale, int roundingMode) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.subtract(value2).setScale(scale, roundingMode).doubleValue();
    }

    public static double multiply(double arg0, double arg1) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.multiply(value2).doubleValue();
    }

    public static double multiply(double arg0, double arg1, int scale, int roundingMode) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.multiply(value2).setScale(scale, roundingMode).doubleValue();
    }

    public static double divide(double arg0, double arg1, int scale, int roundingMode) {
        BigDecimal value1 = new BigDecimal("" + arg0);
        BigDecimal value2 = new BigDecimal("" + arg1);
        return value1.divide(value2, scale, roundingMode).doubleValue();
    }

    public static double round(double v, int scale, int round_mode) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            return b.setScale(scale, round_mode).doubleValue();
        }
    }

    public static String round(String v, int scale) {
        return round(v, scale, 6);
    }

    public static String round(String v, int scale, int round_mode) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(v);
            return b.setScale(scale, round_mode).toString();
        }
    }

    public static double round(double value) throws Exception {
        NumberFormat nfFormat = NumberFormat.getInstance();
        nfFormat.setMaximumFractionDigits(2);
        Number object = null;
        String temp = fomrat(value);

        try {
            object = nfFormat.parse(nfFormat.format(Double.parseDouble(temp)));
        } catch (Exception var6) {
            System.out.println("转换出错:"+var6);
            throw new Exception("转化格式错误!!!!!!");
        }

        return Double.parseDouble(String.valueOf(object));
    }

    public static String fomrat(double value) {
        String temp = String.valueOf(value);
        String[] temps = temp.split("\\.");
        if(temps.length > 1) {
            int index = temp.indexOf(".");
            if(temps[1].length() >= 3) {
                String sub = temp.substring(index + 1, index + 4);
                return temps[0] + "." + sub;
            } else {
                return String.valueOf(value);
            }
        } else {
            return String.valueOf(value);
        }
    }
}
