package com.study.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HashMapInitSizeTest
 * @Description TODO
 * @Author 网易云课堂微专业-java高级开发工程师
 * @Date 2019/11/5 21:45
 * @Version 1.0
 */
public class HashMapInitSizeTest {

    /**
     * 如果我们没有设置初始容量大小，随着元素的不断增加，HashMap会发生多次扩容，
     * 而HashMap中的扩容机制决定了每次扩容都需要重建hash表，是非常影响性能的。
     */
    public static void main(String[] args) {
        int aHundredMillion = 10000000;

        Map<Integer, Integer> map = new HashMap<>();
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < aHundredMillion; i++) {
            map.put(i, i);
        }
        long s2 = System.currentTimeMillis();
        System.out.println("未初始化容量，耗时 ： " + (s2 - s1));
        Map<Integer, Integer> map1 = new HashMap<>(aHundredMillion / 2);
        long s5 = System.currentTimeMillis();
        for (int i = 0; i < aHundredMillion; i++) {
            map1.put(i, i);
        }
        long s6 = System.currentTimeMillis();
        System.out.println("初始化容量" + aHundredMillion / 2 + "，耗时 ： " + (s6 - s5));
        Map<Integer, Integer> map2 = new HashMap<>(aHundredMillion);
        long s3 = System.currentTimeMillis();
        for (int i = 0; i < aHundredMillion; i++) {
            map2.put(i, i);
        }
        long s4 = System.currentTimeMillis();
        System.out.println("初始化容量为" + aHundredMillion + "，耗时 ： " + (s4 - s3));
        Map<Integer, Integer> map3 = new HashMap<>(16);
        long s7 = System.currentTimeMillis();
        for (int i = 0; i < aHundredMillion; i++) {
            map3.put(i, i);
        }
        long s8 = System.currentTimeMillis();
        System.out.println("初始化容量为16，耗时 ： " + (s8 - s7));
    }

}
