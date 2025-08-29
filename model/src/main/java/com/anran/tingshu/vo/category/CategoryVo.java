package com.anran.tingshu.vo.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVo {

    /**
     * 分类 id
     */
    private Long categoryId;
    /**
     * 分类 名称
     */
    private String categoryName;
    /**
     * 分类 孩子
     */
    private List<CategoryVo> categoryChild;



}
