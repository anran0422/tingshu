package com.anran.tingshu.order.client;

import com.anran.tingshu.order.client.impl.OrderInfoDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>
 * 产品列表API接口
 * </p>
 *
 * @author qy
 */
@FeignClient(value = "service-order", fallback = OrderInfoDegradeFeignClient.class)
public interface OrderInfoFeignClient {


}