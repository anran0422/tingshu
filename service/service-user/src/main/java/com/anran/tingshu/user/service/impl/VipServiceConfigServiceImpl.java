package com.anran.tingshu.user.service.impl;

import com.anran.tingshu.model.user.VipServiceConfig;
import com.anran.tingshu.user.mapper.VipServiceConfigMapper;
import com.anran.tingshu.user.service.VipServiceConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class VipServiceConfigServiceImpl extends ServiceImpl<VipServiceConfigMapper, VipServiceConfig> implements VipServiceConfigService {

	@Autowired
	private VipServiceConfigMapper vipServiceConfigMapper;


}
