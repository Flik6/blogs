package com.coco52.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyUtils {

    public static Map<String, String> getUrlParam(String url) {
        Map<String, String> map = new HashMap<String, String>();
        int start = url.indexOf("?");
        if (start >= 0) {
            String str = url.substring(start + 1);
            String[] paramsArr = str.split("&");
            for (String param : paramsArr) {
                String[] temp = param.split("=");
                map.put(temp[0], temp[1]);
            }
        }
        return map;
    }

}
