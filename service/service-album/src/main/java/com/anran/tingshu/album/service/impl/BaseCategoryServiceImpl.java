package com.anran.tingshu.album.service.impl;

import com.anran.tingshu.album.mapper.BaseCategory1Mapper;
import com.anran.tingshu.album.mapper.BaseCategory2Mapper;
import com.anran.tingshu.album.mapper.BaseCategory3Mapper;
import com.anran.tingshu.album.mapper.BaseCategoryViewMapper;
import com.anran.tingshu.album.service.BaseCategoryService;
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
import java.util.Set;
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


	@Override
	public List<CategoryVo> getBaseCategoryList() {
		return baseCategory1Mapper.getBaseCategoryList();
	}

	/**
	 * 传统方式实现 3级 查询
	 * @return
	 */
//    @Override
//    public List<CategoryVo> getBaseCategoryList() {
//
//		List<CategoryVo> res = new ArrayList<>();
//
//		// 1. 查询所有的分类信息
//		List<BaseCategoryView> categoryViewList = baseCategoryViewMapper.selectList(null);
//		// 2. 封装数据（难点）
//
//		// 对 1 级分类进行分组
//		Map<Long, List<BaseCategoryView>> firstCategoryMap = categoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
//
//		for (Map.Entry<Long, List<BaseCategoryView>> entry : firstCategoryMap.entrySet()) {
//			CategoryVo firstCategoryVo = new CategoryVo();
//			firstCategoryVo.setId(entry.getKey());
//			firstCategoryVo.setName(entry.getValue().get(0).getCategory1Name());
//
//			List<CategoryVo> firstCategoryChildren = new ArrayList<>();
//			// 对 2 级分类进行分组
//			Map<Long, List<BaseCategoryView>> secondCategoryMap = entry.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
//
//			for (Map.Entry<Long, List<BaseCategoryView>> secondEntry : secondCategoryMap.entrySet()) {
//				CategoryVo secondCategoryVo = new CategoryVo();
//				secondCategoryVo.setId(secondEntry.getKey());
//				secondCategoryVo.setName(secondEntry.getValue().get(0).getCategory2Name());
//
//				List<CategoryVo> secondCategoryChildren = new ArrayList<>();
//				// 3 级 不需要再分组是一一对应的
//				for (BaseCategoryView baseCategoryView : secondEntry.getValue()) {
//					CategoryVo thirdCategoryVo = new CategoryVo();
//					thirdCategoryVo.setId(baseCategoryView.getCategory3Id());
//					thirdCategoryVo.setName(baseCategoryView.getCategory3Name());
//					thirdCategoryVo.setCategoryChildren(null);
//					secondCategoryChildren.add(thirdCategoryVo);
//				}
//				secondCategoryVo.setCategoryChildren(secondCategoryChildren);
//				firstCategoryChildren.add(secondCategoryVo);
//			}
//			firstCategoryVo.setCategoryChildren(firstCategoryChildren);
//			res.add(firstCategoryVo);
//		}
//
//		// 3. 返回
//		return res;
//    }
}
