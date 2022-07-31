package com.hp.article.controller;


import com.hp.article.pojo.Admin;
import com.hp.article.pojo.Article;
import com.hp.article.service.OperateArticleService;
import com.hp.article.service.impl.OperateArticleServiceImpl;
import com.hp.article.utils.JwtUtils;
import com.hp.article.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.ibatis.util.MapUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
            subject.login(token);
            System.out.println(subject.isAuthenticated());
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
    @RequiresAuthentication
    @RequestMapping(value = "user/insertArticle",method = RequestMethod.POST)
    public ResultUtil insertArticle(@RequestBody Article article){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            //        如果大于0，则插入成功
            boolean insert = opai.insert(article)>0;
            return new ResultUtil(insert);
        }else {
            return new ResultUtil(false, "管理员请先进行登录");
        }

    }
    //---------------------------
//    @ApiOperation(value = "对文章进行删除操作，传入一个id")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象的id" , required = true)
//    @RequiresAuthentication
    @RequestMapping(value = "user/deleteArticle",method = RequestMethod.DELETE)
    public ResultUtil deleteArticle(@RequestParam("id") Integer id){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            //        如果大于0则插入成功
            boolean deleteById = opai.deleteById(id)>0;
            return new ResultUtil(deleteById);
        }else {
            return new ResultUtil(false, "管理员请先进行登录");
        }

    }
    //--------------------------------------------
//    @ApiOperation(value = "对文章进行更新操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)

    @RequestMapping(value = "user/updateArticle",method = RequestMethod.PUT)
    public ResultUtil updateArticle(@RequestBody Article article){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            boolean updateById = opai.updateById(article);
            return new ResultUtil(updateById);
        }else {
            return new ResultUtil(false, "管理员请先进行登录");
        }
    }
//    ------------

//    @ApiOperation(value = "对文章进行查询操作，传入一个数值")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象" , required = true)

    @RequestMapping(value = "user/getArticle",method = RequestMethod.GET)
    public ResultUtil getArticleById(@RequestParam("id") Integer id){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            Article byId = opai.getById(id);
            return new ResultUtil(true,byId);
        }else{
            return new ResultUtil(false,"管理员请先进行登录");
        }

    }


    @GetMapping("user/logout")
    public ResultUtil logout() {
        Subject subject = SecurityUtils.getSubject();
//        AccountProfile profile = (AccountProfile) subject.getPrincipal();
//        System.out.println(profile.getId());
//        会请求到logout
        if (subject.isAuthenticated()) {
            subject.logout();
            return new ResultUtil(true,"退出登陆成功");
        }else{
            return new ResultUtil(false,"请先进行登录再注销");
        }
    }

    @Value("${file.upload.url}")
    private String uploadFilePath;
    //    @Slf4j
    @RequestMapping(value = "upload",method = RequestMethod.PUT)
        public ResultUtil httpUpload(@RequestParam("files") MultipartFile files[]){

            JSONObject object=new JSONObject();
            for(int i=0;i<files.length;i++){
                String fileName = files[i].getOriginalFilename();  // 文件名
                File dest = new File(uploadFilePath +'/'+ fileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    files[i].transferTo(dest);
                } catch (Exception e) {
////                    log.error("{}",e);
//                    object.put("success",2);
//                    object.put("result","程序错误，请重新上传");
                    return new ResultUtil(false,"程序错误，请重新上传");
                }
            }
            object.put("success",1);
            object.put("result","文件上传成功");
            return new ResultUtil(true,"文件上传成功");
        }

    @Value("${file.upload.url}")
    private String downloadFilePath;
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public ResultUtil fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        File file = new File(downloadFilePath +'/'+ fileName);
        if(!file.exists()){
            return new ResultUtil(false,"文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
//            log.error("{}",e);
//            return "下载失败";
            return new ResultUtil(false,"下载失败");
        }
        return new ResultUtil(true,"下载成功");
    }


}
