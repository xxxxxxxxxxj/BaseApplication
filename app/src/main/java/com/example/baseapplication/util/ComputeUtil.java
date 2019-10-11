package com.example.baseapplication.util;

import java.math.BigDecimal;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 确的浮点数运算，包括加减乘除和四舍五入。</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2017/12/8 17:07
 */
public class ComputeUtil {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 2;

    //这个类不能实例化
    private ComputeUtil() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return formatDouble(b1.add(b2).doubleValue(), 2);
    }

    /**
     * 提供精确的加法运算。
     *
     * @return 多个个参数的和
     */
    public static double add(double... v) {
        double sum = 0;
        for (int i = 0; i < v.length; i++) {
            sum = add(sum, v[i]);
        }
        return sum;
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return formatDouble(b1.subtract(b2).doubleValue(), 2);
    }

    /**
     * 提供精确的减法运算。
     *
     * @return 多个参数的差
     */
    public static double sub(double... v) {
        double subNum = 0;
        subNum = sub(v[0], v[1]);
        if (v.length > 2) {
            for (int i = 2; i < v.length; i++) {
                subNum = sub(subNum, v[i]);
            }
        }
        return subNum;
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
        //return Utils.formatDouble(b1.multiply(b2).doubleValue(), 2);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void main(String[] args) {
        double a = 0.123;
        double b = 0.456;
        System.out.println(a * b);
        System.out.println(ComputeUtil.mul(a, b));

    }

    /**
     * 使double类型保留points位小数
     *
     * @param num
     * @return
     */
    public static double formatDouble(double num, int points) {
        BigDecimal bigDecimal = new BigDecimal(new Double(num).toString());
        return bigDecimal.setScale(points, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * double类型转换为int类型
     *
     * @param num
     * @return
     */
    public static int formatDouble(double num) {
        return Integer.parseInt(new java.text.DecimalFormat("0").format(num));
    }
}
