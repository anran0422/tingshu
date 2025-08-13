package com.anran.tingshu.user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.anran.tingshu.common.constant.PublicConstant;
import com.anran.tingshu.common.constant.RedisConstant;
import com.anran.tingshu.common.execption.BusinessException;
import com.anran.tingshu.common.rabbit.constant.MqConst;
import com.anran.tingshu.common.rabbit.service.RabbitService;
import com.anran.tingshu.common.util.AuthContextHolder;
import com.anran.tingshu.model.user.UserInfo;
import com.anran.tingshu.user.mapper.UserInfoMapper;
import com.anran.tingshu.user.service.UserInfoService;
import com.anran.tingshu.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

	@Autowired
	private UserInfoMapper userInfoMapper;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private RsaSigner rsaSigner;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitService rabbitService;

    @Override
    public Map<String, Object> wxLogin(String code) {
        Map<String, Object> map = new HashMap<>();

        // 1. 判断 code 是否存在
        if(StringUtils.isEmpty(code)) {
            throw new BusinessException(201, "code码不存在");
        }

        // 2. 调用微信服务端
        WxMaUserService userService = wxMaService.getUserService();
        WxMaJscode2SessionResult sessonInfo = null;
        String openId = "";
        try {
            sessonInfo = userService.getSessionInfo(code);
            openId = sessonInfo.getOpenid();
        } catch (Exception e) {
            log.error("调用微信服务端失败", e.getMessage());
            throw new BusinessException(201, "调用微信服务端失败");
        }

        String refreshTokenKey = RedisConstant.USER_LOGIN_REFRESH_KEY_PREFIX + openId;
        String jsonWebTokenFromRedis = redisTemplate.opsForValue().get(refreshTokenKey);
        if (!StringUtils.isEmpty(jsonWebTokenFromRedis)) {
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("token", jsonWebTokenFromRedis);
            resMap.put("refreshToken", jsonWebTokenFromRedis); // 前端没有写只是象征性
            return resMap;
        }

        // 3. 根据 openId 查询用户信息
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getWxOpenId, openId);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if(userInfo == null) {
            // 1. 向 tingshu_user 的 user_info 表中插入用户 （注册用户）
            userInfo = new UserInfo();
            userInfo.setWxOpenId(openId);
            userInfo.setNickname(System.currentTimeMillis() + "-[htz]-" + UUID.randomUUID().toString().substring(0,4).replace("-",""));
            userInfo.setAvatarUrl("https://cloud-1311088844.cos.ap-beijing.myqcloud.com/public_share/avatar/default_avatar.jpeg");
            userInfo.setIsVip(0); // 不是 VIP
            userInfo.setVipExpireTime(new Date());

            int insert = userInfoMapper.insert(userInfo);
            log.info("注册用户:{}", insert > 0 ? "success" : "fail");

            // todo 2. 向 tingshu_account 的 user_account 表中插入用户账户（初始化用户账户余额）
            /**
             * param 1 交换机
             * param 2 路由键
             * param 3 消息内容
             */
            rabbitService.sendMessage(MqConst.EXCHANGE_USER, MqConst.ROUTING_USER_REGISTER, userInfo.getId().toString());
            log.info("用户微服务发送初始化用户账户余额：{} 成功", userInfo.getId());


        }
        // 4. 生成一个 token 返回给前端
        String token = getJsonWebToken(userInfo.getId(), openId);
        map.put("token", token);
        map.put("refreshToken", token);

        // 5. 将 token 放入到 Redis 中
        String accessTokenKey = RedisConstant.USER_LOGIN_KEY_PREFIX + openId;
        refreshTokenKey = RedisConstant.USER_LOGIN_REFRESH_KEY_PREFIX + openId;
        redisTemplate.opsForValue().set(accessTokenKey, token, 100, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(refreshTokenKey, token, 180, TimeUnit.DAYS);


        // 6. 返回
        return map;
    }

    private String getJsonWebToken(Long userId, String openId) {
        // 定义一个载荷
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userId);
        jsonObject.put("openId", openId);
        // Jwt 方式生成
        Jwt jwt = JwtHelper.encode(jsonObject.toString(), rsaSigner);
        String token = jwt.getEncoded();
        return token;
    }

    @Override
    public Map<String, Object> getNewAccessToken() {

        Map<String, Object> result = new HashMap<>();

        // 1.  获取用户的身份信息
        // 1.1 获取目标请求属性对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 1.2 获取目标请求对象
        HttpServletRequest request = requestAttributes.getRequest();
        // 1.3 获取请求对象的请求头
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)) {
            throw new BusinessException(201, "之前没有登录过");
        }

        // 2. 校验 jsonWebToken 是否被篡改
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(PublicConstant.PUBLIC_KEY));

        // 3. 校验通过 获得载荷数据
        String claims = jwt.getClaims();
        Map map = JSONObject.parseObject(claims, Map.class);
        Object userId = map.get("id");
        Object openId = map.get("openId");


        String accessTokenKey = RedisConstant.USER_LOGIN_KEY_PREFIX + openId;
        String refreshTokenKey = RedisConstant.USER_LOGIN_REFRESH_KEY_PREFIX + openId;

        // 1. 从 Redis 中获取 refreshToken
        String refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);
        // 2. 判断 Redis 中是否存在 refreshToken
        if(!StringUtils.isEmpty(refreshToken)) {
            // 2.1 如果有，生成一个新令牌返回给前端
            // todo 12.00进行的修改
            String jsonWebToken = getJsonWebToken(Long.parseLong(userId.toString()), openId.toString());
            redisTemplate.opsForValue().set(accessTokenKey, jsonWebToken, 100, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(refreshTokenKey, jsonWebToken, 180, TimeUnit.DAYS);

            // 给前端一份
            result.put("token", jsonWebToken);
            return result;
        } else {
            // 2.2 如果没有，refreshToken 也过期了，重新去登录
            result.put("1", "v");
        }
        return result;
    }

    /**
     * 更新用户信息
     * @param userInfoVo
     */
    @Override
    public void updateUser(UserInfoVo userInfoVo) {

        // 得到当前登录用户
        Long userId = AuthContextHolder.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if(userInfo == null) {
            throw new BusinessException(201, "用户不存在");
        }
        userInfo.setNickname(userInfoVo.getNickname());
        userInfo.setAvatarUrl(userInfoVo.getAvatarUrl());
        userInfoMapper.updateById(userInfo);
    }
}
