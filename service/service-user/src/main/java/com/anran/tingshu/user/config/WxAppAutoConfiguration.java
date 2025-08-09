package com.anran.tingshu.user.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.anran.tingshu.user.config.properties.WxAutoProperties;
import org.apache.ibatis.javassist.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

/**
 * 微信自动配置类
 */
@Configuration
@EnableConfigurationProperties(WxAutoProperties.class)
public class WxAppAutoConfiguration {


    @Autowired
    WxAutoProperties wxAutoProperties;

    /**
     * 定义 WxMaService 的 Bean 对象
     */
    @Bean
    public WxMaService wxMaService() {
        // 1. 创建 接口的对象出来
        WxMaService wxMaService = new WxMaServiceImpl();

        // 2. 创建一个微信配置对象出来
        WxMaDefaultConfigImpl wxMaDefaultConfig = new WxMaDefaultConfigImpl();
        wxMaDefaultConfig.setAppid(wxAutoProperties.getAppId());
        wxMaDefaultConfig.setSecret(wxAutoProperties.getAppSecret());

        // 3. 将配置对象放到 WxMaService 对象中
        wxMaService.setWxMaConfig(wxMaDefaultConfig);

        return wxMaService;
    }

    /**
     * 定义 RSA 签名的 Bean 对象
     * 读取 RSA 证书
     * 将 new 动作当到配置中
     */
    @Bean
    public RsaSigner rsaSigner() {
        // 1. 读取证书
        ClassPathResource classPathResource = new ClassPathResource("tingshu.jks");

        // 2. 创建一个工厂对象 打开证书
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, "tingshu".toCharArray());

        // 3. 从工厂中获取 公钥和私钥
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("tingshu", "tingshu".toCharArray());

        // 4. 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RsaSigner rsaSigner = new RsaSigner(privateKey);
        return rsaSigner;
    }
}
