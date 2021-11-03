package com.coco52.mapper.words;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coco52.entity.words.Books;
import com.coco52.entity.words.Words;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsMapper extends BaseMapper<Words> {
}
