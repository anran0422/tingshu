package com.anran.tingshu.user.api;

import com.anran.tingshu.common.result.Result;
import com.anran.tingshu.common.result.ResultCodeEnum;
import com.anran.tingshu.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("api/user/userInfo")
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    // Request URL: http://localhost:8500/api/user/userInfo/findUserSubscribePage/1/10

    // todo：实现真正的功能（订阅功能）
    @GetMapping("/findUserSubscribePage/{pn}/{pz}")
    public Result findUserSubscribePage(@PathVariable(value = "pn") Long pn,
                                        @PathVariable(value = "pz") Long pz) {

        return Result.build(null, ResultCodeEnum.LOGIN_AUTH);
    }

}

