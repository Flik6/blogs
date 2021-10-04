package com.coco52.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coco52.entity.Article;
import com.coco52.entity.ArticlesInfo;
import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import com.coco52.mapper.ArticleMapper;
import com.coco52.mapper.ArticlesInfoMapper;
import com.coco52.service.ArticleService;
import com.coco52.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
    @Autowired
    private RequestUtils requestUtils;




    public RespResult selectArticleByUUID(String uuid) {

        QueryWrapper<ArticlesInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("uuid", uuid);
        List articleList = articlesInfoMapper.selectList(queryWrapper);
        if (articleList == null) {
            RespResult.fail("未获取到文章");
        }
        return RespResult.success(ResultCode.SUCCESS, articleList);
    }

    public RespResult selectArticleByRandom(Integer num) {
        List<ArticlesInfo> articlesInfos = articlesInfoMapper.selectArticleByRandom(num);
        if (articlesInfos == null || articlesInfos.size() != num) {
            return RespResult.fail("查询失败，请联系管理员");
        }
        return RespResult.success(ResultCode.SUCCESS, articlesInfos);
    }

    @Override
    @Transactional
    public RespResult publishAnArticle(Article article, HttpServletRequest request) {
        String uuidFromRequest = requestUtils.getUUIDFromRequest(request);
        Assert.notEmpty(uuidFromRequest);
        if (ObjectUtils.isEmpty(article.getTitle()) || ObjectUtils.isEmpty(article.getContent())) {
            return RespResult.fail(ResultCode.ARTICLE_NOT_ALLOW_NULL, null);
        }
        String simpleUUID = IdUtil.simpleUUID();
        article.setArticleId(simpleUUID);

        String intro = ObjectUtils.isEmpty(article.getIntro())? article.getTitle():article.getIntro();
        ArticlesInfo articlesInfo = new ArticlesInfo(
                null,
                uuidFromRequest,
                article.getTitle(),
                intro,
                0L,
                0,
                null,
                simpleUUID,
                LocalDateTime.now(),
                article.getExternalLink()
        );
        int articleInsert = articleMapper.insert(article);
        int articleInfoInsert = articlesInfoMapper.insert(articlesInfo);
        Assert.isTrue(articleInsert == articleInfoInsert && articleInsert == 1);
        return RespResult.success(ResultCode.ARTICLE_SUCCESSFULLY_PUBLISHED, simpleUUID);
    }

    /**
     * 根据文章id查询文章内容
     * @param articleId
     * @return
     */
    @Override
    public RespResult getArticle(String articleId) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("article_id",articleId);
        Article article = articleMapper.selectOne(articleQueryWrapper);
        if (ObjectUtils.isEmpty(article)){
            return RespResult.fail(ResultCode.ARTICLE_NOT_GET_CONTENT,null);
        }
        return RespResult.success(ResultCode.ARTICLE_GET_SUCCESS,article);
    }

    /**
     * 根据文章id查询文章详细信息  info
     * @param articleId 文章id
     * @return
     */
    @Override
    public RespResult getArticleInfo(String articleId) {
        QueryWrapper<ArticlesInfo> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("article_id",articleId);
        ArticlesInfo articlesInfo = articlesInfoMapper.selectOne(articleQueryWrapper);
        if (ObjectUtils.isEmpty(articlesInfo)){
            return RespResult.fail(ResultCode.ARTICLE_INFO_GET_ERROR,null);
        }
        return RespResult.success(ResultCode.ARTICLE_INFO_GET_SUCCESS,articlesInfo);
    }

    @Override
    public RespResult getArticleHasImage() {
        int num = 3;
        QueryWrapper<ArticlesInfo> articlesInfoQueryWrapper = new QueryWrapper<>();
        //数据库内此字段不能为空
        articlesInfoQueryWrapper.isNotNull("article_image");
        articlesInfoQueryWrapper.orderByAsc("Rand()");
        articlesInfoQueryWrapper.last("limit "+num);
        List<ArticlesInfo> articlesInfos = articlesInfoMapper.selectList(articlesInfoQueryWrapper);
        if (articlesInfos.size()<num){
            return RespResult.fail(ResultCode.FAIL,articlesInfos);
        }

        return RespResult.success(ResultCode.SUCCESS,articlesInfos);
    }


}
