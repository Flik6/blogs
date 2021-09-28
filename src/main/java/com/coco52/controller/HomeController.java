package com.coco52.controller;

import com.coco52.entity.RespResult;
import com.coco52.service.ArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/home")
@Api(tags = "网站页面信息类模块")
public class HomeController {
    @Autowired
    private ArticleService articleService;
    /**
     * 走马灯
     * @param request 通过设置header Access-Control-Allow-Origin 允许跨域
     * @param httpServletResponse
     * @return  RespMsg 全类型的返回值  返回Banner的img地址
     */
    @GetMapping("getCarousel")
    public RespResult getCarousel(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        List<String> list = Arrays.asList(
                "https://i1.mifile.cn/a4/xmad_15535933141925_ulkYv.jpg",
                "https://i1.mifile.cn/a4/xmad_15532384207972_iJXSx.jpg",
                "https://i1.mifile.cn/a4/xmad_15517939170939_oiXCK.jpg",
                "https://i1.mifile.cn/a4/xmad_15338857534213_DaEjU.jpg",
                "https://i1.mifile.cn/a4/xmad_1535103285321_fPlOK.jpg",
                "https://i1.mifile.cn/a4/xmad_15349329950127_PVrya.jpg",
                "https://i1.mifile.cn/a4/xmad_15338982677936_eQTJk.jpg"
        );
        httpServletResponse.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        return RespResult.success("获取成功！", list);
    }

    /**
     * 初始化界面
     * @return
     */
    @GetMapping("init")
    public RespResult init(){
        return RespResult.success("获取成功");
    }
    @GetMapping("getCards")
    public RespResult getCards(HttpServletRequest request, HttpServletResponse httpServletResponse){
        RespResult respResult = articleService.selectArticleByRandom(3);
        return respResult;
    }
}
