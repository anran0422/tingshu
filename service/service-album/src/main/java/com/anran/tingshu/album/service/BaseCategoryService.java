package com.anran.tingshu.album.service;

import com.alibaba.fastjson.JSONObject;
import com.anran.tingshu.model.album.BaseAttribute;
import com.anran.tingshu.model.album.BaseCategory1;
import com.anran.tingshu.vo.category.CategoryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseCategoryService extends IService<BaseCategory1> {


    List<CategoryVo> getBaseCategoryList();

    List<BaseAttribute> findAttribute(Long category1Id);
}
