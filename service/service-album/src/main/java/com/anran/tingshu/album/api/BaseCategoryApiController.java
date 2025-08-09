package com.anran.tingshu.album.api;

import com.anran.tingshu.album.service.BaseCategoryService;
import com.anran.tingshu.common.result.Result;
import com.anran.tingshu.common.util.ResponseUtil;
import com.anran.tingshu.vo.category.CategoryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "分类管理")
@RestController
@RequestMapping(value="/api/album/category")
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseCategoryApiController {

	@Autowired
	private BaseCategoryService baseCategoryService;

	@GetMapping("getBaseCategoryList")
	@Operation(summary = "查找首页分类信息")
	public Result getBaseCategoryList() {
		List<CategoryVo> res = baseCategoryService.getBaseCategoryList();

		return Result.ok(res);
	}
}

