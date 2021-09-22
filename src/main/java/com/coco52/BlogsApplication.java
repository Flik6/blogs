package com.coco52;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@MapperScan("com.coco52.mapper")
public class BlogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
