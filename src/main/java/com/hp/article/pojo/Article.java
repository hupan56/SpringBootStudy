package com.hp.article.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName operate_object
 */
@TableName(value ="operate_articles")
@Data
public class Article implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章作者
     */
    private String articleAuthor;

    /**
     * 文章来源
     */
    private String articleSource;

    /**
     * 文章发表日期
     */
    private Date articleTime;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章责编
     */
    private String articleEditor;

    /**
     * 文章类型
     */
    private String articleType;

    /**
     * 文章领域
     */
    private String articleField;

    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}