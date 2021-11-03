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

    //近义词
    @Autowired
    private SynonymsMapper synonymsMapper;

    @Autowired
    private WordsMapper wordsMapper;


    @Autowired
    private ExampleSentenceMapper exampleSentenceMapper;


    @Override
    public RespResult getBookList() {
        QueryWrapper<Books> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<Books> books = booksMapper.selectList(queryWrapper);
        return books.size() == 0 ? RespResult.fail(ResultCode.WORD_BOOKS_GET_FAIL, null) : RespResult.success(ResultCode.WORD_BOOKS_GET_SUCCESS, books);
    }

    @Override
    public RespResult getWordsList(Integer current, String bookId) {
        Page<Words> page = new Page<Words>(current, 50);//参数一是当前页，参数二是每页个数
        QueryWrapper<Words> wordsQueryWrapper = new QueryWrapper<>();
        wordsQueryWrapper.eq("book_id", bookId);
        Page<Words> booksPage = wordsMapper.selectPage(page, wordsQueryWrapper);
        List<Words> records = booksPage.getRecords();
        return records.size() == 0 ? RespResult.fail(ResultCode.WORD_GET_FAIL, null) : RespResult.success(ResultCode.WORD_GET_SUCCESS, records);


    }

    @Override
    public RespResult GetWordInfo(String wordId, String options) {
        QueryWrapper<Words> wordsQueryWrapper = new QueryWrapper<>();
        wordsQueryWrapper.eq("id", wordId);
        Integer count = wordsMapper.selectCount(wordsQueryWrapper);
        if (count == 0) {
            return RespResult.fail(ResultCode.WORD_INFO_GET_FAIL, null);
        }
        switch (options) {
            case "synonyms": {
                QueryWrapper<Synonyms> synonymsQueryWrapper = new QueryWrapper<>();
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
