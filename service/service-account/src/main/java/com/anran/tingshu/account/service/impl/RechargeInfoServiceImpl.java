package com.anran.tingshu.account.service.impl;

import com.anran.tingshu.account.mapper.RechargeInfoMapper;
import com.anran.tingshu.account.service.RechargeInfoService;
import com.anran.tingshu.model.account.RechargeInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class RechargeInfoServiceImpl extends ServiceImpl<RechargeInfoMapper, RechargeInfo> implements RechargeInfoService {

	@Autowired
	private RechargeInfoMapper rechargeInfoMapper;

}
