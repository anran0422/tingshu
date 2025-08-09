package com.anran.user;


class T1 extends Thread {
    @Override
    public void run() {
        System.out.println("线程名字：" + Thread.currentThread().getName());
    }
}
public class ThreadDemo {
    public static void main(String[] args) {
        T1 t1 = new T1();
        t1.setName("T1线程");
        t1.run();
        t1.start();
    }
}
