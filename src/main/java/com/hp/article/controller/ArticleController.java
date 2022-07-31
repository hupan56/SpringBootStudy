package com.hp.article.controller;

import com.hp.article.pojo.Article;
import com.hp.article.service.impl.OperateArticleServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
public class ArticleController {

    @Autowired
    OperateArticleServiceImpl opai;


    @GetMapping("/article")
    public List<Article> getAll(){
        List<Article> list = opai.list();
        return  list;
    }


    @RequestMapping(value = "article/getArticle",method = RequestMethod.POST)
//    开发文档接口的相关描述
    @ApiOperation(value = "通过文章标题来获文章实体类，用于点击标题链接时跳转到文章页面")
    @ApiImplicitParam(name="title",value = "文章标题",required = true)
    public List<Article> getArticleByTitle(@RequestParam(value = "title") String title,
                                           @RequestParam(value = "source") String source){
        List<Article> byArticleTitle = opai.getAllByArticleTitleAndArticleSource(title,source);
        return byArticleTitle;
    }

    @RequestMapping(value = "article/getArticlesByKey",method = RequestMethod.POST)
//    开发文档接口的相关描述
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "keyWord",value = "模糊查找的关键词",required = false),
            @ApiImplicitParam(name ="currentPage",value = "当前页",required = true)
    }
    )
    @ApiOperation(value = "通过关键词来模糊查询文章，模糊查询包括文章内容，文章标题，文章作者")
    public List<Article> getArticleByKeyWord(@RequestParam(value = "keyWord") String keyWord,
                                             @RequestParam(value = "currentPage")  Integer currentPage){

        List<Article> articlesByKey = opai.getArticlesByKey(keyWord, currentPage);
        return articlesByKey;
    }
//
//    @RequestMapping(value = "article/getArticlesBySource",method = RequestMethod.POST)
////    开发文档接口的相关描述
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "source",value = "文章的分类",required = false),
//            @ApiImplicitParam(name ="currentPage",value = "当前页",required = true)
//    }
//    )
//    @ApiOperation(value = "通过来源来获取文章")
//    public List<Article> getArticleBySource(@RequestParam(value = "source") String source,
//                                             @RequestParam(value = "currentPage")  Integer currentPage){
//        List<Article> articlesBySource = opai.getArticlesBySource(source, currentPage);
//        return articlesBySource;
//    }


    @ApiOperation(value = "获取数据库类的所有文章分类，用以后续的标签体筛选")
    @RequestMapping(value = "article/getAllArticleType",method = RequestMethod.POST)
    public List<String>getAllArticleType(){
        List<String> allArticleType = opai.getAllArticleType();
        return allArticleType;
    }

    @ApiOperation(value = "获取数据库类的所有文章领域，用以后续的标签体筛选")
    @RequestMapping(value = "article/getAllArticleField",method = RequestMethod.POST)
    public List<String>getAllArticleField(){
        List<String> allArticleField = opai.getAllArticleField();
        return allArticleField;
    }
    @ApiOperation(value = "获取数据库类的所有文章时间，用以后续的标签体筛选")
    @RequestMapping(value = "article/getAllArticleTime",method = RequestMethod.POST)
    public List<Integer>getAllArticleTime(){
        List<Integer> allArticleTime = opai.getAllArticleTime();
        return allArticleTime;
    }

    @ApiOperation(value = "通过标签体和关键词模糊右查询进行筛选")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "keyWord",value = "模糊查询关键词(字符串)",required = false),
            @ApiImplicitParam(name = "location",value = "搜索位置标签(字符串list集合)",required = false),
            @ApiImplicitParam(name = "time",value = "文章发表时间标签(整数list集合)",required = false,allowMultiple=true),
            @ApiImplicitParam(name = "type",value = "文章类型标签(字符串lis集合)",required = false,allowMultiple=true),
            @ApiImplicitParam(name = "field",value = "文章领域标签(字符串list集合)",required = false,allowMultiple=true),
            @ApiImplicitParam(name = "source",value = "文章来源标签(字符串list集合)",required = false,allowMultiple=true),
            @ApiImplicitParam(name = "currentPage",value = "当前页码,用于进行分页和异步加载(整数)",required = true),
    })

    @RequestMapping(value = "article/getArticlesByTags",method = RequestMethod.POST)
    public List<Article>getArticlesByTags(@RequestParam("keyWord") String keyWord, @RequestParam("location")String location,
                                          @RequestParam("time")List<Integer> time, @RequestParam("type")List<String> type,
                                          @RequestParam("field")List<String> field, @RequestParam("source")List<String> source,
                                          @RequestParam("currentPage")int currentPage
    )
    {
        List<Article> articlesByTags = opai.getArticlesByTags(keyWord, location, time, type, field, source,currentPage);
        return articlesByTags;
    }

//    下面的操作是管理员实现的
//    @ApiOperation(value = "对文章进行添加操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)
//
//    @RequestMapping(value = "article/insertArticle",method = RequestMethod.POST)
//    public int insertArticle(@RequestParam("article") Article article){
//        int insert = opai.insert(article);
//        return insert;
//    }
//
//    @ApiOperation(value = "对文章进行删除操作，传入一个id")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象的id" , required = true)
//
//    @RequestMapping(value = "article/deleteArticle",method = RequestMethod.POST)
//    public int deleteArticle(@RequestParam("id") Integer id){
//        int deleteById = opai.deleteById(id);
//        return deleteById;
//    }
//
//    @ApiOperation(value = "对文章进行更新操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)
//
//    @RequestMapping(value = "article/updateArticle",method = RequestMethod.POST)
//    public boolean updateArticle(@RequestParam("article") Article article){
//       boolean updateById = opai.updateById(article);
//        return updateById;
//    }

}
