package com.anran.tingshu.user.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完成配置文件中的属性和该类的属性绑定
 */
@Data
@ConfigurationProperties(prefix = "wx.login")
public class WxAutoProperties {

    private String appId;

    private String appSecret;
}
