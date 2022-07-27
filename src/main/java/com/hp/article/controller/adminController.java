package com.hp.article.controller;

import com.hp.article.pojo.Admin;
import com.hp.article.pojo.Article;
import com.hp.article.service.OperateArticleService;
import com.hp.article.service.impl.OperateArticleServiceImpl;
import com.hp.article.utils.JwtUtils;
import com.hp.article.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.util.MapUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayDeque;

@RestController
public class adminController {

    @Autowired
    OperateArticleServiceImpl opai;

    @Autowired
    JwtUtils jwtUtils;


    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    public ResultUtil doLogin(@RequestBody Admin admin, Boolean rememberMe, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 验证帐号和密码
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getAdminName(), admin.getAdminPassword());
        // 记住登录状态
        token.setRememberMe(rememberMe);
        try {
            //            如果验证通过再根据用户名查找到该用户
            Admin adminObj = opai.getAdminByName(admin.getAdminName());
            if (adminObj == null) {
                return new ResultUtil(false, "用户不存在");
            }


            if (!adminObj.getAdminPassword().equals(admin.getAdminPassword())) {
                return new ResultUtil(false, "密码错误");
            }
//            根据用户id生成一个jwt
            String jwt = jwtUtils.getToken(adminObj.getId());

//            将jwt写入
            response.setHeader("authorization", jwt);
            response.setHeader("Access-Control-Expose-Headers", "authorization");

            //            如果正确就返回用户信息

            return new ResultUtil(true, admin);
//                    .success(MapUtil.builder()
//                    .put("id", user.getId())
//                    .put("username", user.getUsername())
//                    .put("avatar", user.getAvatar())
//                    .put("email", user.getEmail())
//                    .map()
//            );
        } catch (UnknownAccountException e) {
            return new ResultUtil(false, "用户不存在2");
        } catch (IncorrectCredentialsException e) {
            return new ResultUtil(false, "密码不正确2");
        }

    }

//    @ApiOperation(value = "对文章进行添加操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)

    @RequestMapping(value = "user/insertArticle",method = RequestMethod.POST)
    public ResultUtil insertArticle(@RequestBody Article article){
//        如果大于0，则插入成功
        System.out.println(article);
        boolean insert = opai.insert(article)>0;
        return new ResultUtil(insert);
    }
    //---------------------------
//    @ApiOperation(value = "对文章进行删除操作，传入一个id")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象的id" , required = true)
    @RequestMapping(value = "user/deleteArticle",method = RequestMethod.DELETE)
    public ResultUtil deleteArticle(@RequestParam("id") Integer id){
//        如果大于0则插入成功
        boolean deleteById = opai.deleteById(id)>0;
        return new ResultUtil(deleteById);
    }
    //--------------------------------------------
//    @ApiOperation(value = "对文章进行更新操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)
    @RequestMapping(value = "user/updateArticle",method = RequestMethod.PUT)
    public ResultUtil updateArticle(@RequestBody Article article){
        System.out.println(article);
        boolean updateById = opai.updateById(article);
        return new ResultUtil(updateById);
    }
//    ------------

//    @ApiOperation(value = "对文章进行查询操作，传入一个数值")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象" , required = true)
    @RequiresAuthentication
    @RequestMapping(value = "user/getArticle",method = RequestMethod.GET)
    public ResultUtil getArticleById(@RequestParam("id") Integer id){

        Article byId = opai.getById(id);
        return new ResultUtil(true,byId);
    }



}
