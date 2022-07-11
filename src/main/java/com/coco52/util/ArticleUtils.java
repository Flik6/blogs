package com.coco52.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author flik
 */
@Component
public class ArticleUtils {
    private static final Pattern P_IMG = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
    private static final Pattern P_SRC = Pattern.compile("(src|SRC)=([\"'])(.*?)([\"'])");
    /**
     * 获取img标签中的src值
     */
    public static List<String> getImgSrc(String content){

        List<String> list = new ArrayList<>();
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签

        Matcher mImg = P_IMG.matcher(content);
        boolean resultImg = mImg.find();
        if (resultImg) {
            while (resultImg) {
                //获取到匹配的<img />标签中的内容
                String strImg = mImg.group(2);

                //开始匹配<img />标签中的src

                Matcher mSrc = P_SRC.matcher(strImg);
                if (mSrc.find()) {
                    String strSrc = mSrc.group(3);
                    list.add(strSrc);
                }
                //结束匹配<img />标签中的src
                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                resultImg = mImg.find();
            }
        }
        return list;
    }
}
