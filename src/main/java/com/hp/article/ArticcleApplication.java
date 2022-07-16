package com.hp.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableWebMvc


public class ArticcleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticcleApplication.class, args);
        System.out.println("master");
        System.out.println(3);
    }

}
