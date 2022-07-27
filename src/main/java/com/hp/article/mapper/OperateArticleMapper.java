package com.hp.article.mapper;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.article.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import com.hp.article.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import sun.security.krb5.internal.PAData;

/**
 * @Entity com.hp.article.pojo.OperateObject
 */
@Mapper
@Repository
public interface  OperateArticleMapper extends BaseMapper<Article> {


    List<Article> getAllByArticleTitleAndArticleSource(@Param("articleTitle") String articleTitle,
                                                       @Param("articleSource") String articleSource);


    Page<Article> getAll(@Param("page") Page<Article> page);

    List<Article> getByArticleTitle(@Param("articleTitle") String articleTitle);

    List<Article> getByArticleTitleAndId(@Param("articleTitle") String articleTitle, @Param("id") Long id);
//    获得文章所有分类
    List<String> getAllArticleType();
//    获得文章所有领域
    List<String> getAllArticleField();
//    获得文章所有时间
    List<Integer> getAllArticleTime();
//    通过关键词模糊查询所有文章
    Page<Article> getArticlesByKey(@Param("page") Page<Article> page,@Param("keyWord") String keyWord);

//    根据不同标签进行筛选,采取动态sql  Location:搜索位置 time :搜索时间 type:搜索类型
    Page<Article> getArticlesByTags(@Param("page") Page<Article> page,@Param("keyWord") String keyWord,
                                    @Param("location")  String location ,@Param("time") List<Integer> time,
                                    @Param("type") List<String> type,@Param("field") List<String> field,
                                    @Param("source") List<String> source
    );

//    根据文章不同的分类返回信息
//     Page<Article> getArticlesBySource(@Param("page") Page<Article> page,@Param("source") String source);

//根据id删除文章
    int deleteByArticleId(Integer id);


    @Override
    int insert(Article entity);



//    @Override
//    int updateById(Article entity);
    @Select("select * from admin where id=#{id}")
    Admin getAdminById(@Param("id") long id);


//    根据用户名查找用户
    @Select("select * from admin where admin_name=#{adminName}")
    Admin getAdminByName(@Param("adminName") String adminNames);

    @Select("select admin_name from admin where id=#{id}")
    String getAdminNameById(@Param("id") long id);
}




