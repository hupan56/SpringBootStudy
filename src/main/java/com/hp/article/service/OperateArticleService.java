package com.hp.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.article.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface OperateArticleService extends IService<Article> {


    List<Article> getAllByArticleTitleAndArticleSource(String articleTitle,String articleSource);

//    这里要设置一个参数读取当前页
    List<Article> getArticlesByKey(String keyWord ,int currentPage);

//    获取所有文章分类，为前台传去标签体
    List<String> getAllArticleType();

//    获得文章所有领域
    List<String> getAllArticleField();
//    获取所有文章发表的年份
    List<Integer> getAllArticleTime();

    List<Article> getArticlesByTags(@Param("keyWord") String keyWord,
                                    @Param("location")  String location ,@Param("time") List<Integer> time,
                                    @Param("type") List<String> type,@Param("field") List<String> field,
                                    @Param("source") List<String> source,int currentPage);
//    添加对象
    int insert(Article article);
//    删除对象
    int deleteById(Article user);

    boolean updateById(Article entity);

}
