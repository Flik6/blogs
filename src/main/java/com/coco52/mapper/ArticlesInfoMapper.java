package com.coco52.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.ArticlesInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticlesInfoMapper extends BaseMapper<ArticlesInfo> {
    @Select("SELECT * FROM articlesInfo  ORDER BY  RAND() LIMIT #{integer}")
    List<ArticlesInfo> selectArticleByRandom(Integer integer);
}
