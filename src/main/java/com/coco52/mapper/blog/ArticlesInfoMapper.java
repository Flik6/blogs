package com.coco52.mapper.blog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coco52.entity.ArticlesInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticlesInfoMapper extends BaseMapper<ArticlesInfo> {
    @Select("SELECT * FROM articlesInfo  ORDER BY  RAND() LIMIT #{integer}")
    List<ArticlesInfo> selectArticleByRandom(Integer integer);
}
