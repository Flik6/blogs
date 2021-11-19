package com.coco52.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coco52.entity.RespResult;
import com.coco52.entity.words.*;
import com.coco52.enums.ResultCode;
import com.coco52.mapper.words.*;
import com.coco52.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@DS("words")
public class WordServiceImpl implements WordService {


    @Autowired
    private BooksMapper booksMapper;

    //同根词mapper
    @Autowired
    private SameRootWordMapper sameRootWordMapper;

    //短语mapper
    @Autowired
    private PhraseMapper phraseMapper;

    //近义词mapper
    @Autowired
    private SynonymsMapper synonymsMapper;

    @Autowired
    private WordsMapper wordsMapper;

    //例句mapper
    @Autowired
    private ExampleSentenceMapper exampleSentenceMapper;

    //获取单词书本列表
    @Override
    public RespResult getBookList() {
        QueryWrapper<Books> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<Books> books = booksMapper.selectList(queryWrapper);
        return books.size() == 0 ? RespResult.fail(ResultCode.WORD_BOOKS_GET_FAIL, null) : RespResult.success(ResultCode.WORD_BOOKS_GET_SUCCESS, books);
    }
    //默认获取50个单词
    @Override
    public RespResult getWordsList(Integer current, String bookId) {
        Page<Words> page = new Page<Words>(current, 50);//参数一是当前页，参数二是每页个数
        QueryWrapper<Words> wordsQueryWrapper = new QueryWrapper<>();
        wordsQueryWrapper.eq("book_id", bookId);
        Page<Words> booksPage = wordsMapper.selectPage(page, wordsQueryWrapper);
        List<Words> records = booksPage.getRecords();
        return records.size() == 0 ? RespResult.fail(ResultCode.WORD_GET_FAIL, null) : RespResult.success(ResultCode.WORD_GET_SUCCESS, records);


    }


    //根据单词id获取自定义（同根词(sameRoot)，例句(exampleSentence),短语，近义词等详细信息）
    @Override
    public RespResult GetWordInfo(String wordId, String options) {
        QueryWrapper<Words> wordsQueryWrapper = new QueryWrapper<>();
        wordsQueryWrapper.eq("id", wordId);
        Integer count = wordsMapper.selectCount(wordsQueryWrapper);
        if (count == 0) {
            return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
        }
        QueryWrapper<Synonyms> synonymsQueryWrapper = new QueryWrapper<>();
        switch (options) {
            case "synonyms": {
                synonymsQueryWrapper.eq("word_id", wordId);
                Synonyms synonyms = synonymsMapper.selectOne(synonymsQueryWrapper);
                if (synonyms.getWord() == null) {
                    return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
                }
                return RespResult.success(ResultCode.WORD_INFO_GET_SUCCESS, synonyms);
            }
            case "sameRoot": {
                QueryWrapper<SameRootWord> sameRootWordQueryWrapper = new QueryWrapper<>();
                sameRootWordQueryWrapper.eq("word_id", wordId);
                List<SameRootWord> sameRootWords = sameRootWordMapper.selectList(sameRootWordQueryWrapper);
                if (sameRootWords.size() == 0) {
                    return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
                }
                return RespResult.success(ResultCode.WORD_INFO_GET_SUCCESS, sameRootWords);

            }
            case "phrase": {
                QueryWrapper<Phrase> phraseQueryWrapper = new QueryWrapper<>();
                phraseQueryWrapper.eq("word_id", wordId);
                List<Phrase> phrases = phraseMapper.selectList(phraseQueryWrapper);
                if (phrases.size() == 0) {
                    return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
                }
                return RespResult.success(ResultCode.WORD_INFO_GET_SUCCESS, phrases);
            }
            case "exampleSentence": {
                QueryWrapper<ExampleSentence> exampleSentenceQueryWrapper = new QueryWrapper<>();
                exampleSentenceQueryWrapper.eq("word_id", wordId);
                List<ExampleSentence> exampleSentences = exampleSentenceMapper.selectList(exampleSentenceQueryWrapper);
                if (exampleSentences.size() == 0) {
                    return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
                }
                return RespResult.success(ResultCode.WORD_INFO_GET_SUCCESS, exampleSentences);

            }
            default: {
                return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
            }
        }



    }
}
