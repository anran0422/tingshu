package com.anran.tingshu.user.service;

import com.anran.tingshu.model.user.UserInfo;
import com.anran.tingshu.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.Objects;

public interface UserInfoService extends IService<UserInfo> {

    /**
     * 微信小程序登录
     * @param code
     * @return
     */
    Map<String, Object> wxLogin(String code);

    Map<String, Object> getNewAccessToken();

    void updateUser(UserInfoVo userInfoVo);
}
