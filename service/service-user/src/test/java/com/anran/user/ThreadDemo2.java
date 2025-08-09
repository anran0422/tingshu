package com.anran.user;
public class ThreadDemo2 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程名字：" + Thread.currentThread().getName() + " 匿名内部类 Runnable接口");
            }
        },"T1线程").start();
        new Thread(() -> {
            System.out.println("线程名字：" + Thread.currentThread().getName() + " Lambda表达式 Runnable接口");
        }, "T2线程").start();
    }
}
