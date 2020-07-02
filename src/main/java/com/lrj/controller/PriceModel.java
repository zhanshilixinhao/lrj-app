package com.lrj.controller;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * @Description:
 * @Author： Lxh
 * @Date： 2020/7/1 13:53
 */
public class PriceModel {
    public static void main(String[] args) {
        System.out.println("请输入使用月份");
        Scanner scanner = new Scanner(System.in);
        int month = scanner.nextInt();
        System.out.println("请输入使用次数");
        int frequency = scanner.nextInt();
        System.out.println("请输入个性服务");
        int individualConsumption = scanner.nextInt();
        PriceModel.priceCalculation(month,frequency,individualConsumption);
    }
    public static void priceCalculation(int month,int frequency,int individualConsumption){
        int totalPrice = 100*frequency*month+individualConsumption;
        double monthDiscount = month * 0.03;
        System.out.println(monthDiscount);
        double frequencyDiscount = frequency * 0.03;
        System.out.println(frequencyDiscount);
        double actualPrice = totalPrice - totalPrice * monthDiscount - totalPrice * frequencyDiscount +
                (totalPrice - totalPrice * monthDiscount - totalPrice * frequencyDiscount)*0.2;
        System.out.println("最低总价"+totalPrice);
        BigDecimal bigDecimal = new BigDecimal(actualPrice);
        double value = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println("实际价格"+value);
    }
}
