package com.anran.user;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//class T1 extends Thread {
//    @Override
//    public void run() {
//        System.out.println("线程名字：" + Thread.currentThread().getName());
//    }
//}
//
//class T2 implements Runnable {
//
//    @Override
//    public void run() {
//        System.out.println("Runnable线程名字：" + Thread.currentThread().getName());
//    }
//}

public class ApTest {

    @Test
    public void testJwtApi() {
        // 1. 得到 builder 对象
        JwtBuilder builder = Jwts.builder();

        Map<String, Object> map = new HashMap<>();
        map.put("name", "anran");
        map.put("age", "18");
        map.put("id", "10080");

        // 2. 得到 jsonWebToken()
        String jwt = builder.setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(map)
                .setIssuer("anran")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 10))
                .setSubject("测试使用 Jwt")
                .signWith(SignatureAlgorithm.HS256, "anran".getBytes())
                .compact();

        System.out.println(jwt);

        // 解析 jwt 拿到载荷数据
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey("anran".getBytes()).parseClaimsJws(jwt);
        Object name = claimsJws.getBody().get("name");
        System.out.println(name);
    }


//    @Test
//    public void testJUC() {
//        T1 t1 = new T1();
//        t1.setName("子线程 T1 ");
//        t1.start();
//
//        Thread thread = new Thread(new T2(), "子线程 T2");
//        thread.start();
//
//        T2 t2 = new T2();
//        t2.run();
//
//        System.out.println("线程名字：" + Thread.currentThread().getName());
//    }

//    @Test
//    public void testRunnable() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("匿名内部类：" + Thread.currentThread().getName());
//            }
//        }, "匿名内部类-线程");
//        thread.start();
//        System.out.println("线程名字：" + Thread.currentThread().getName());
//    }

//    @Test
//    public void testLambda() {
//        Thread thread = new Thread(() -> {
//                System.out.println("Lambda：" + Thread.currentThread().getName());
//
//        }, "Lambda-线程");
//        thread.start();
//        System.out.println("线程名字：" + Thread.currentThread().getName());
//    }
}