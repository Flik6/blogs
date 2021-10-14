package com.coco52.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.coco52.entity.Article;
import com.coco52.entity.ArticlesInfo;
import com.coco52.entity.RespResult;
import com.coco52.enums.ResultCode;
import com.coco52.mapper.ArticleMapper;
import com.coco52.mapper.ArticlesInfoMapper;
import com.coco52.service.ArticleService;
import com.coco52.util.ArticleUtils;
import com.coco52.util.EmojiConverterUtil;
import com.coco52.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

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
        List articleInfoList = articlesInfoMapper.selectList(queryWrapper);
        if (articleInfoList == null) {
            RespResult.fail("未获取到文章");
        }
        return RespResult.success(ResultCode.SUCCESS, articleInfoList);
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
        //获取文章内容
        String content = article.getContent();
        //判断文章内容或标题不为空
        if (ObjectUtils.isEmpty(article.getTitle()) || ObjectUtils.isEmpty(article.getContent())) {
            return RespResult.fail(ResultCode.ARTICLE_NOT_ALLOW_NULL, null);
        }
        //判断文章标题或内容长度是否小于5
        if (article.getTitle().length()<5 ||article.getContent().length()<5){
            return RespResult.fail(ResultCode.ARTICLE_TOO_SHORT, null);
        }
        //如果文章banner为空 设置文章的第一个图片为banner
        if (article.getArticleImage().isEmpty()){
            //将文章中第一个图片设为banner
            List<String> imgSrcList = ArticleUtils.getImgSrc(content);
            if (!imgSrcList.isEmpty()){
                article.setArticleImage(imgSrcList.get(0));
            }else {
                article.setArticleImage("https://api.yimian.xyz/img?type=head?v="+new Date());
            }
        }

        //设置文章id
        String simpleUUID = IdUtil.simpleUUID();
        article.setArticleId(simpleUUID);
        article.setUuid(uuidFromRequest);
        //设置文章简介如果没有文章简介则将文章标题设为简介
        String intro = ObjectUtils.isEmpty(article.getIntro())? article.getTitle():article.getIntro();
        //初始化文章信息
        ArticlesInfo articlesInfo = new ArticlesInfo(
                null,
                uuidFromRequest,
                article.getTitle(),
                intro,
                0L,
                Integer.parseInt(article.getCategory()),
                article.getArticleImage(),
                simpleUUID,
                LocalDateTime.now(),
                article.getExternalLink()
        );
        //将文章内容设置转换为
        article.setContent(EmojiConverterUtil.emojiConvert1(content));
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
        UpdateWrapper<ArticlesInfo> updateWrapper = new UpdateWrapper<>();
        //查询当前阅读量多少
        QueryWrapper<ArticlesInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.select("read_counts").eq("article_id",articleId);
        ArticlesInfo articlesInfo = articlesInfoMapper.selectOne(infoQueryWrapper);
        //使阅读量增加1
        articlesInfo.setReadCounts(articlesInfo.getReadCounts()+1);
        //更新数据库
        updateWrapper.eq("article_id",articleId);
        articlesInfoMapper.update(articlesInfo,updateWrapper);
        //将utf83字符保存的emoji转换为4字符的emoji
        article.setContent(EmojiConverterUtil.emojiRecovery2(article.getContent()));
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
