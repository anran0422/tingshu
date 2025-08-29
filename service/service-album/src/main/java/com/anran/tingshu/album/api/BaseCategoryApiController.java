package com.anran.tingshu.album.api;

import com.anran.tingshu.album.service.BaseCategoryService;
import com.anran.tingshu.common.result.Result;
import com.anran.tingshu.model.album.BaseAttribute;
import com.anran.tingshu.vo.category.CategoryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simpleframework.xml.Path;
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



	@GetMapping("/getBaseCategoryList")
	@Operation(summary = "查找首页分类信息")
	public Result getBaseCategoryList() {
		List<CategoryVo> res = baseCategoryService.getBaseCategoryList();

		return Result.ok(res);
	}

	@GetMapping("/findAttribute/{category1Id}")
	@Operation(summary = "根据一级分类查询专辑的标签属性[属性 + 属性值]")
	public Result findAttribute(@Path("category1Id") Long category1Id) {
		List<BaseAttribute> baseAttributes =  baseCategoryService.findAttribute(category1Id);
		return Result.ok(baseAttributes);
	}
}

