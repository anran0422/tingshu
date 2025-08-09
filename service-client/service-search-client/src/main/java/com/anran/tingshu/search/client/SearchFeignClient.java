package com.anran.tingshu.search.client;

import com.anran.tingshu.search.client.impl.SearchDegradeFeignClient;
import com.anran.tingshu.common.result.Result;
import com.anran.tingshu.vo.account.AccountLockResultVo;
import com.anran.tingshu.vo.account.AccountLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 产品列表API接口
 * </p>
 *
 * @author qy
 */
@FeignClient(value = "service-search", fallback = SearchDegradeFeignClient.class)
public interface SearchFeignClient {

}