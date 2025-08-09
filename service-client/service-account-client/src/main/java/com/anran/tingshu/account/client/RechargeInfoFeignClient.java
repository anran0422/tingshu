package com.anran.tingshu.account.client;

import com.anran.tingshu.account.client.impl.RechargeInfoDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 产品列表API接口
 * </p>
 *
 * @author qy
 */
// todo 改的name
@FeignClient(value = "service-account",
        contextId = "rechargeInfoFeignClient",
        fallback = RechargeInfoDegradeFeignClient.class )
public interface RechargeInfoFeignClient {


}