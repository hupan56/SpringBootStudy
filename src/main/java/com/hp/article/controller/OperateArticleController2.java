package com.hp.article.controller;


import com.hp.article.pojo.Article;
import com.hp.article.service.impl.OperateArticleServiceImpl;
import com.hp.article.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OperateArticleController2 {

    @Autowired
    OperateArticleServiceImpl opai;
//    ---------------------------------------------
    @GetMapping("/article")
    public ResultUtil getAll(){
        List<Article> list = opai.list();
        return  new   ResultUtil(true,list);
    }
//
//-------------------------------------------------

//    ----------------------------------------
    @RequestMapping(value = "article/getArticlesByKey",method = RequestMethod.GET)
//    开发文档接口的相关描述
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "keyWord",value = "模糊查找的关键词",required = false),
            @ApiImplicitParam(name ="currentPage",value = "当前页",required = true)
    }
    )
    @ApiOperation(value = "通过关键词来模糊查询文章，模糊查询包括文章内容，文章标题，文章作者")
    public ResultUtil getArticleByKeyWord(@RequestParam(value = "keyWord") String keyWord,
                                             @RequestParam(value = "currentPage")  Integer currentPage){

        List<Article> articlesByKey = opai.getArticlesByKey(keyWord, currentPage);
        return new ResultUtil(true,articlesByKey);
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
    @RequestMapping(value = "article/getAllArticleType",method = RequestMethod.GET)
    public ResultUtil getAllArticleType(){
        List<String> allArticleType = opai.getAllArticleType();
        return new ResultUtil(true,allArticleType);
    }
//----------------------------
    @ApiOperation(value = "获取数据库类的所有文章领域，用以后续的标签体筛选")
    @RequestMapping(value = "article/getAllArticleField",method = RequestMethod.GET)
    public ResultUtil getAllArticleField(){
        List<String> allArticleField = opai.getAllArticleField();
        return new ResultUtil(true,allArticleField);
    }
//    --------------------------
    @ApiOperation(value = "获取数据库类的所有文章时间，用以后续的标签体筛选")
    @RequestMapping(value = "article/getAllArticleTime",method = RequestMethod.GET)
    public ResultUtil getAllArticleTime(){
        List<Integer> allArticleTime = opai.getAllArticleTime();
        return new ResultUtil(true,allArticleTime);
    }
//-------------------------------------
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
    public ResultUtil getArticlesByTags(@RequestParam("keyWord") String keyWord, @RequestParam("location")String location,
                                          @RequestParam("time")List<Integer> time, @RequestParam("type")List<String> type,
                                          @RequestParam("field")List<String> field, @RequestParam("source")List<String> source,
                                          @RequestParam("currentPage")int currentPage) {
        List<Article> articlesByTags = opai.getArticlesByTags(keyWord, location, time, type, field, source,currentPage);
        return new ResultUtil(true,articlesByTags);
    }
//    ----------------------------
    @ApiOperation(value = "对文章进行添加操作，传入一个对象")
    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)

    @RequestMapping(value = "article/insertArticle",method = RequestMethod.POST)
    public ResultUtil insertArticle(@RequestBody Article article){
//        如果大于0，则插入成功
        System.out.println(article);
        boolean insert = opai.insert(article)>0;
        return new ResultUtil(insert);
}
//---------------------------
    @ApiOperation(value = "对文章进行删除操作，传入一个id")
    @ApiImplicitParam(name = "id",value = "文章实体类对象的id" , required = true)
    @RequestMapping(value = "article/deleteArticle",method = RequestMethod.DELETE)
    public ResultUtil deleteArticle(@RequestParam("id") Integer id){
//        如果大于0则插入成功
        boolean deleteById = opai.deleteById(id)>0;
        return new ResultUtil(deleteById);
    }
//--------------------------------------------
    @ApiOperation(value = "对文章进行更新操作，传入一个对象")
    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)
    @RequestMapping(value = "article/updateArticle",method = RequestMethod.PUT)
    public ResultUtil updateArticle(@RequestBody Article article){
        System.out.println(article);
       boolean updateById = opai.updateById(article);
        return new ResultUtil(updateById);
    }
//    ------------

    @ApiOperation(value = "对文章进行查询操作，传入一个数值")
    @ApiImplicitParam(name = "id",value = "文章实体类对象" , required = true)
    @RequestMapping(value = "article/getArticle",method = RequestMethod.GET)
    public ResultUtil getArticleById(@RequestParam("id") Integer id){

        Article byId = opai.getById(id);
        return new ResultUtil(true,byId);
    }

}
