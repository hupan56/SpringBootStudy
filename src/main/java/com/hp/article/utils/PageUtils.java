package com.hp.article.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//自定义出一个页码方便修改


@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageUtils {

   @Value("${utils.pageSize}")
    private Integer pageSize;
}
