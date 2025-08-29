package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.mapper.*;
import com.anran.tingshu.album.service.BaseCategoryService;
import com.anran.tingshu.model.album.BaseAttribute;
import com.anran.tingshu.model.album.BaseCategory1;
import com.anran.tingshu.model.album.BaseCategoryView;
import com.anran.tingshu.vo.category.CategoryVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseCategoryServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1> implements BaseCategoryService {

	@Autowired
	private BaseCategory1Mapper baseCategory1Mapper;

	@Autowired
	private BaseCategory2Mapper baseCategory2Mapper;

	@Autowired
	private BaseCategory3Mapper baseCategory3Mapper;

	@Resource
	private BaseCategoryViewMapper baseCategoryViewMapper;

	@Autowired
	private BaseAttributeMapper baseAttributeMapper;


	/**
	 * 通过 Mybatis 框架自定义映射实现分级查询
	 */
//	@Override
//	public List<CategoryVo> getBaseCategoryList() {
//		return baseCategory1Mapper.getBaseCategoryList();
//	}

	@Override
	public List<BaseAttribute> findAttribute(Long category1Id) {
		return baseAttributeMapper.findAttribute(category1Id);
	}

	/**
	 * 传统方式实现 3级 查询
	 * @return
	 */
    @Override
    public List<CategoryVo> getBaseCategoryList() {

		List<CategoryVo> res = new ArrayList<>();

		// 1. 查询所有的分类信息
		List<BaseCategoryView> categoryViewList = baseCategoryViewMapper.selectList(null);
		// 2. 封装数据（难点）

		// 对 1 级分类进行分组
		Map<Long, List<BaseCategoryView>> firstCategoryMap = categoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));

		for (Map.Entry<Long, List<BaseCategoryView>> entry : firstCategoryMap.entrySet()) {
			CategoryVo firstCategoryVo = new CategoryVo();
			firstCategoryVo.setCategoryId(entry.getKey());
			firstCategoryVo.setCategoryName(entry.getValue().get(0).getCategory1Name());

			List<CategoryVo> firstCategoryChildren = new ArrayList<>();
			// 对 2 级分类进行分组
			Map<Long, List<BaseCategoryView>> secondCategoryMap = entry.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));

			for (Map.Entry<Long, List<BaseCategoryView>> secondEntry : secondCategoryMap.entrySet()) {
				CategoryVo secondCategoryVo = new CategoryVo();
				secondCategoryVo.setCategoryId(secondEntry.getKey());
				secondCategoryVo.setCategoryName(secondEntry.getValue().get(0).getCategory2Name());

				List<CategoryVo> secondCategoryChildren = new ArrayList<>();
				// 3 级 不需要再分组是一一对应的
				for (BaseCategoryView baseCategoryView : secondEntry.getValue()) {
					CategoryVo thirdCategoryVo = new CategoryVo();
					thirdCategoryVo.setCategoryId(baseCategoryView.getCategory3Id());
					thirdCategoryVo.setCategoryName(baseCategoryView.getCategory3Name());
					thirdCategoryVo.setCategoryChild(null);
					secondCategoryChildren.add(thirdCategoryVo);
				}
				secondCategoryVo.setCategoryChild(secondCategoryChildren);
				firstCategoryChildren.add(secondCategoryVo);
			}
			firstCategoryVo.setCategoryChild(firstCategoryChildren);
			res.add(firstCategoryVo);
		}

		// 3. 返回
		return res;
    }
}