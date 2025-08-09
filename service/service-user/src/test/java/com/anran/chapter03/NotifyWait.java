package com.anran.chapter03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通知 等待 唤醒 机制
 */
class ShareDateOne {

    private final ReentrantLock lock = new  ReentrantLock();
    Condition condition = lock.newCondition();

    private Integer number = 0; // 1 0

    public void increment() {
        lock.lock();
        try {
            // 1. 判断：是否轮到我执行，则干活；否则等待
            while(number != 0) {
                condition.await();
            }
            // 2. 干活
            number++;
            System.out.println(Thread.currentThread().getName() + " " + number);

            // 3. 通知：唤醒等待的线程
            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public void decrement() {
        lock.lock();
        try {
            // 1. 判断：是否轮到我执行，则干活；否则等待
            while(number != 1) {
                condition.await();
            }
            // 2. 干活
            number--;
            System.out.println(Thread.currentThread().getName() + " " + number);

            // 3. 通知：唤醒等待的线程
            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
public class NotifyWait {

    public static void main(String[] args) {
        // 要求加法 减法 10轮交替执行
        ShareDateOne shareDateOne = new ShareDateOne();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDateOne.increment();
            }

        }, "加法线程AAA").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDateOne.decrement();
            }
        }, "减法线程CCC").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDateOne.increment();
            }

        }, "加法线程BBB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareDateOne.decrement();
            }
        }, "减法线程DDD").start();
    }
}
