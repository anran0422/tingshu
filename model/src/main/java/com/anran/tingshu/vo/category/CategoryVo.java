package com.anran.tingshu.vo.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVo {

    /**
     * 分类 id
     */
    private Long id;
    /**
     * 分类 名称
     */
    private String name;
    /**
     * 分类 孩子
     */
    private List<CategoryVo> categoryChildren;



}
