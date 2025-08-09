package com.anran.tingshu.user.api;

import com.anran.tingshu.common.execption.BusinessException;
import com.anran.tingshu.common.login.annotation.TingshuLogin;
import com.anran.tingshu.common.result.Result;
import com.anran.tingshu.common.result.ResultCodeEnum;
import com.anran.tingshu.common.util.AuthContextHolder;
import com.anran.tingshu.model.user.UserInfo;
import com.anran.tingshu.user.service.UserInfoService;
import com.anran.tingshu.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@Tag(name = "微信授权登录接口")
@RestController
@RequestMapping("/api/user/wxLogin")
@Slf4j
public class WxLoginApiController {

//    Request URL: http://localhost:8500/api/user/wxLogin/wxLogin/0a3Iae1007ceoU1pIQ30043RDb3Iae1w

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/wxLogin/{code}")
    @Operation(summary = "微信小程序登录")
    public Result wxLogin(@PathVariable(value = "code")String code) {
        // 前端要的 == 实现的功能
        Map<String, Object> map = userInfoService.wxLogin(code);

        return Result.ok(map);
    }

    // Request URL: http://localhost:8500/api/user/wxLogin/getUserInfo
    // todo 什么都没带，我们怎么拿呢？ 这里先写死，后面再去动态获取
    @GetMapping("/getUserInfo")
    @Operation(summary = "获取用户信息")
    @TingshuLogin
    public Result getUserInfo() {
        // 从 ThreadLocal 中获取 userId
        Long userId = AuthContextHolder.getUserId();

        // 前端要的 == 实现的功能
        UserInfo userInfo = userInfoService.getById(userId);
        if(userInfo == null) {
            throw new BusinessException(201, "用户不存在");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);

        return Result.ok(userInfoVo);
    }

    @GetMapping("/refreshToken/getNewAccessToken")
    @Operation(summary = "获取新令牌")
    @TingshuLogin
    public Result getNewAccessToken() {

        Map<String, Object> map =  userInfoService.getNewAccessToken();
        String flag = (String) map.get("1");
        if(map != null && !StringUtils.isEmpty(flag)) { // 需要登录就是
            return Result.build(null, ResultCodeEnum.LOGIN_AUTH);
        }
        return Result.ok(map);
    }
}
