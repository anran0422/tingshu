package com.anran.tingshu.common.login.aspect;

import com.alibaba.fastjson.JSONObject;
import com.anran.tingshu.common.constant.PublicConstant;
import com.anran.tingshu.common.constant.RedisConstant;
import com.anran.tingshu.common.execption.BusinessException;
import com.anran.tingshu.common.result.ResultCodeEnum;
import com.anran.tingshu.common.util.AuthContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Aspect
@Component
public class LoginAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定义切面逻辑：
     * 判断请求的资源是否登录，如果登录 直接访问，否则去登录
     */

    @Around(value = "@annotation(com.anran.tingshu.common.login.annotation.TingshuLogin)")
    public Object loginCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 1. 获取请求中的令牌
        String tokenFromWeb = getJsonWebToken();

        // 2. 判断是否携带了令牌
        // 如果携带了，还要将载荷中的数据获取到
        Long userId = checkTokenGetUserId(tokenFromWeb);
        // 将认证中心获取到的 userId 放入到 ThreadLocal 中
        AuthContextHolder.setUserId(userId);

        Object returnValue; // 执行目标方法
        try {
            returnValue = proceedingJoinPoint.proceed();
        } finally {
            AuthContextHolder.removeUserId(); // 解决内存泄漏
        }

        // 返回通知
        return returnValue;
    }

    private Long checkTokenGetUserId(String jsonWebTokenFromWeb) {
        // 1. 请求中是否携带了 token
        if(StringUtils.isEmpty(jsonWebTokenFromWeb)) {
            throw new BusinessException(ResultCodeEnum.LOGIN_AUTH, "未携带token");
        }

        // 2. 校验 jsonWebToken 是否被篡改
        Jwt jwt = JwtHelper.decodeAndVerify(jsonWebTokenFromWeb, new RsaVerifier(PublicConstant.PUBLIC_KEY));

        // 3. 校验通过 获得载荷数据
        String claims = jwt.getClaims();
        Map map = JSONObject.parseObject(claims, Map.class);
        Object userId = map.get("id");
        Object openId = map.get("openId");

        // 4. 比对 Redis 中是否存在 jsonWebToken
        String accessTokenKey = RedisConstant.USER_LOGIN_KEY_PREFIX + openId;
        String jsonWebTokenFromRedis = redisTemplate.opsForValue().get(accessTokenKey);

        if(StringUtils.isEmpty(jsonWebTokenFromRedis)) {
            throw new BusinessException(401, "accessToken 已经过期");
        }
        if(!jsonWebTokenFromWeb.equals(jsonWebTokenFromRedis)) {
            throw new BusinessException(401, "Token 不一致");
        }

        return Long.valueOf(userId.toString());
    }

    private static String getJsonWebToken() {
        // 1.  获取用户的身份信息
        // 1.1 获取目标请求属性对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 1.2 获取目标请求对象
        HttpServletRequest request = requestAttributes.getRequest();
        // 1.3 获取请求对象的请求头
        String token = request.getHeader("token");
        return token;
    }
}
