package com.anran.chapter03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareDataTwo {

    private Integer flag = 1; // 线程标识类 区分线程切换
    private final Lock lock = new ReentrantLock();


    private final Condition condition1 = lock.newCondition();
    private final Condition condition2 = lock.newCondition();
    private final Condition condition3 = lock.newCondition();

    public void print5() {
        lock.lock();
        try {

            while(flag != 1) {
                // 等待
                condition1.await();
            }

            for (int i = 0; i < 5; i++) {
                System.out.println("AA");
            }

            // 通知并且唤醒
            flag = 2;
            condition2.signal(); // 唤醒 B

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.lock();
        try {
            // 等待
            while (flag != 2) {
                condition2.await();
            }

            // 干活
            for (int i = 0; i < 10; i++) {
                System.out.println("BB");
            }

            // 通知唤醒
            flag = 3;
            condition3.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void print15() {
        lock.lock();
        try {
            // 等待
            while(flag != 3) {
                condition3.await();
            }
            // 干活
            for (int i = 0; i < 15; i++) {
                System.out.println("CC");
            }
            // 通知唤醒
            flag = 1;
            condition1.signal();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}

public class ThreadOrder {
    public static void main(String[] args) {
        // 线程操作资源类
        ShareDataTwo shareDataTwo = new ShareDataTwo();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDataTwo.print5();
            }
        },"AA").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDataTwo.print10();
            }
        },"BB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDataTwo.print15();
            }
        },"CC").start();
    }
}
