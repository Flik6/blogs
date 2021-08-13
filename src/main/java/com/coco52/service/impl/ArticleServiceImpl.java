package com.coco52.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.Article;
import com.coco52.entity.ArticlesInfo;
import com.coco52.entity.RespMsg;
import com.coco52.mapper.ArticleMapper;
import com.coco52.mapper.ArticlesInfoMapper;
import com.coco52.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    /**
     * 文章详细信息Mapper
     */
    @Autowired
    private ArticlesInfoMapper articlesInfoMapper;
    /**
     * 注入文章内容Mapper
     */
    @Autowired
    private ArticleMapper articleMapper;




    public RespMsg selectArticleByUUID(String uuid) {
        QueryWrapper<ArticlesInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("uuid", uuid);
        List articleList = articlesInfoMapper.selectList(queryWrapper);
        if (articleList == null) {
            RespMsg.fail("未获取到文章");
        }
        return RespMsg.success("文章获取成功", articleList);
    }

    public RespMsg selectArticleByRandom(Integer num) {

        List<ArticlesInfo> articlesInfos = articlesInfoMapper.selectArticleByRandom(num);
        if (articlesInfos==null|| articlesInfos.size()==0){
            return RespMsg.fail("查询失败，请联系管理员");
        }
        return RespMsg.success("查询成功",articlesInfos);
    }


}
