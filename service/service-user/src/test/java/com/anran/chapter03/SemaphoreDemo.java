package com.anran.chapter03;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

    public static void main(String[] args) {

        // 资源数量
        Semaphore semaphore = new Semaphore(3);

        // 线程争抢资源:6个线程
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    // 争抢资源
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "抢到了一个资源");

                    // 持有资源一段时间
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));

                    // 释放资源
                    // 这两句代码反过来，输出看起来可能错误，只是输出顺序而已
                    System.out.println(Thread.currentThread().getName() + "释放了");
                    semaphore.release();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, String.valueOf(i)).start();
        }

    }
}
