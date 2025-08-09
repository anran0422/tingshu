package com.anran.tingshu.album.mapper;

import com.anran.tingshu.model.album.BaseCategory1;
import com.anran.tingshu.vo.category.CategoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {


    List<CategoryVo> getBaseCategoryList();
}
