package com.hp.article.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hp.article.pojo.Admin;
import com.hp.article.pojo.Article;
import com.hp.article.utils.PageUtils;
import com.hp.article.service.OperateArticleService;
import com.hp.article.mapper.OperateArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class OperateArticleServiceImpl extends ServiceImpl<OperateArticleMapper, Article>
    implements OperateArticleService {
    @Autowired
    OperateArticleMapper opam;
//    通过标题来查询文章 就是点击某链接跳转到具体页面

    @Autowired
    PageUtils pageUtils;
//    获取配置类中的页码




    @Override
    public List<Article> getAllByArticleTitleAndArticleSource(String articleTitle,String articleSource) {
        List<Article> byArticleTitle = opam.getAllByArticleTitleAndArticleSource(articleTitle,articleSource);
        return  byArticleTitle;
    }

//    通过模糊查询关键词获取相关文章，使用的是右模糊
    @Override
    public List<Article> getArticlesByKey(String keyWord, int currentPage) {
//        这里需要进行一个判断都keyWord为空或者空字符串时，进入全部搜索
        if(keyWord==" "||keyWord==null||keyWord==""){

                Page<Article> page = new Page<>(1, pageUtils.getPageSize());
                opam.getAll(page);
                return  page.getRecords();


        }else {
            Page<Article> page = new Page<>(currentPage, pageUtils.getPageSize());
            opam.getArticlesByKey(page, keyWord);
            return page.getRecords();
        }
    }

//    获取文章所有分类
    @Override
    public List<String> getAllArticleType() {
        List<String> articleType = opam.getAllArticleType();
        return articleType;
    }
//    获取文章所有领域
    @Override
    public List<String> getAllArticleField() {
        List<String> allArticleField = opam.getAllArticleField();
        return allArticleField;
    }

    @Override
    public List<Integer> getAllArticleTime() {
        List<Integer> allArticleTime = opam.getAllArticleTime();
        return allArticleTime;
    }

    @Override
    public List<Article> getArticlesByTags( String keyWord, String location, List<Integer> time, List<String> type,
                                            List<String> field, List<String> source,int currentPage) {
//        pageSize是配置文件中配置的
        Page<Article> page = new Page<>(currentPage, pageUtils.getPageSize());
         opam.getArticlesByTags(page,keyWord, location, time, type, field,source);
        return page.getRecords();
    }

//    @Override
//    public List<Article> getArticlesBySource(String source, int currentPage) {
//        Page<Article> page = new Page<>(currentPage, utils.getPageSize());
//        opam.getArticlesBySource(page,source);
//        return page.getRecords();
//    }

    @Override
    public int insert(Article article) {
        return opam.insert(article);
    }

    @Override
    public int deleteById(Integer id) {
        return opam.deleteById(id);
    }

    @Override
    public Admin getAdminById(long id) {
        return opam.getAdminById(id);
    }

    @Override
    public Admin getAdminByName(String name) {
        return opam.getAdminByName(name);
    }

    @Override
    public boolean updateById(Article article) {
        return opam.updateById(article)>0;
    }

    @Override
    public String getAdminNameById(long id) {
        return opam.getAdminNameById(id);
    }
}




