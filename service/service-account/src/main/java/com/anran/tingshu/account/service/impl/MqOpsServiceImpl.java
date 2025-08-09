package com.anran.tingshu.account.service.impl;

import com.anran.tingshu.account.mapper.UserAccountMapper;
import com.anran.tingshu.account.service.MqOpsService;
import com.anran.tingshu.common.execption.BusinessException;
import com.anran.tingshu.model.account.UserAccount;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MqOpsServiceImpl implements MqOpsService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    public void userAccountRegister(String content) {

        try {
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(Long.parseLong(content));

            int res = userAccountMapper.insert(userAccount);

            log.info("初始化用户账户:{}", res > 0 ? "success" : "fail");
        } catch (Exception e) {
            throw new BusinessException(201, "服务内部处理数据失败");
        }
    }
}
