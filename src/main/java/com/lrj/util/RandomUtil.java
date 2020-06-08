package com.lrj.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    private static final String[] strArray = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b",
            "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u",
            "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    /**
     * <b>generateRandomString</b>：(生产随机字符串)<br>
     * @param n
     *            字符串位数<br>
     * @param<br>
     * @return String<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static String generateRandomString(int n) {

        if (n <= 0) {
            return "n must >0";
        }
        String rString = "";
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int rd = rand.nextInt(strArray.length);
            rString += strArray[rd];
        }
        return rString;
    }

    /**
     * <b>generateRandomNumberString</b>：(生产随机数字)<br>
     * @return String<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static String generateRandomNumberString(int n) {

        String[] array = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        if (n <= 0) {
            return "n must >0";
        }
        String rString = "";
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int rd = rand.nextInt(array.length);
            rString += array[rd];
        }
        return rString;
    }

    /**
     * 从指定数组中随机选取一个数（红包赠送）
     */
    public static String getRandomCopuon() {

        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9","10"};
        String rString = "";
        Random rand = new Random();
        rString = array[rand.nextInt(array.length)];
        return rString;
    }
    /**
     * 从指定数组中随机选取一个数（红包类型选择）
     */
    public static String getRandomCopuonType() {

        String[] array = {"2", "3", "4", "5"};
        String rString = "";
        Random rand = new Random();
        rString = array[rand.nextInt(array.length)];
        return rString;
    }
    /**
     * 随机形成指定位数的密码
     * @return
     * @throws Exception
     */
    public static String generatePassword(int passwordLength) throws Exception {

        int length = strArray.length;
        if (passwordLength <= 0 || passwordLength > length) {
            throw new Exception("system.err.unkown");
        }
        Random random = new Random();
        Integer n = null;
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            n = random.nextInt(length);
            password.append(strArray[n]);
        }
        return password.toString();
    }

    /**
     * 产生随机数
     * 
     * @param n
     * @return
     * @throws Exception
     */
    public static int generateRandom(int n) throws Exception {

        if (n < 1) {
            throw new Exception("system.err.unkown");
        }
        Random rd = new Random();
        return rd.nextInt(n);
    }

    /**
     * 生成6位随机数+前辍
     * @param prefix
     * @return
     */
    public static String generateRandom(String prefix) {

        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 9; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i <= 5; i++) {
            result = result * 10 + array[i];
        }
        return prefix + result;
    }

    /**
     * <b>generateOrderId</b>：(生成订单号)<br>
     * @return long<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static long generateOrderId() {

        String sorderId = DateUtils.getCurrentDateToNum() + RandomUtil.generateRandomNumber(8);
        return Long.parseLong(sorderId);
    }

    /**
     * <b>generateRandomNumber</b>：(产生随机数)<br>
     * @param number
     *            number只能小于等于9
     *            随机数位数<br>
     * @param<br>
     * @return Integer<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static Integer generateRandomNumber(int number) {

        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 9; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < number; i++) {
            result = result * 10 + array[i];
        }
        return result;
    }

    /**
     * <b>generateOrder</b>：(生成订单号)<br>
     * @return Integer<br>
     * @Exception<br>
     * @author SAM QZL
     */
    public static Integer generateOrder(int number) {

        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 9; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < number; i++) {
            result = result * 10 + array[i];
        }
        return result;
    }

    public static void main(String[] args) {

        System.out.println(generateRandomNumberString(13));
        /* System.out.println(generateRandom(10000)); */
        /* System.out.println(generateRandom("201409")); */
    }

    public static String getUUID() {

        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23)
                + s.substring(24);
    }
}
