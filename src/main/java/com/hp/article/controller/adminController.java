package com.hp.article.controller;


import com.hp.article.pojo.Admin;
import com.hp.article.pojo.Article;
import com.hp.article.service.impl.OperateArticleServiceImpl;
//import com.hp.article.shiro.JwtToken;
//import com.hp.article.utils.JwtUtils;
import com.hp.article.utils.JwtUtils;
import com.hp.article.utils.ResultUtil;
import javafx.beans.binding.ObjectExpression;
import net.minidev.json.JSONObject;
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
import java.util.HashMap;
import java.util.Map;


@RestController
public class adminController {

    Object data=null;

    @Autowired
    OperateArticleServiceImpl opai;

//    @Autowired
//    JwtUtils jwtUtils;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    , Boolean rememberMe
    public ResultUtil doLogin(@RequestBody Admin admin, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getAdminName(), admin.getAdminPassword());
        System.out.println(admin);
        System.out.println("------------"+token);

        try {
            Admin adminObj = opai.getAdminByName(admin.getAdminName());
            if (adminObj == null) {
                return new ResultUtil(false, "用户不存在",401);
            }
            if (!adminObj.getAdminPassword().equals(admin.getAdminPassword())) {
                return new ResultUtil(false, "密码错误",401);
            }
            JwtUtils jwtUtil = new JwtUtils();
            Map<String, Object> chaim = new HashMap<>();
            chaim.put("username", admin.getAdminName());
            String jwtToken = jwtUtil.encode(admin.getAdminName(), 5 * 60 * 1000, chaim);
            return new ResultUtil(true,"登陆成功",jwtToken,200);
        } catch (UnknownAccountException e) {
            return new ResultUtil(false, "用户不存在2",400);
        } catch (IncorrectCredentialsException e) {
            return new ResultUtil(false, "密码不正确2",400);
        }
    }

//    @ApiOperation(value = "对文章进行添加操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)
    @RequiresAuthentication
    @RequestMapping(value = "user/insertArticle",method = RequestMethod.POST)
    public ResultUtil insertArticle(@RequestBody Article article){
            //        如果大于0，则插入成功
            boolean insert = opai.insert(article)>0;
            return new ResultUtil(insert,"添加成功",200);

    }
    //---------------------------
//    @ApiOperation(value = "对文章进行删除操作，传入一个id")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象的id" , required = true)
//    @RequiresAuthentication
    @RequestMapping(value = "user/deleteArticle",method = RequestMethod.DELETE)
    public ResultUtil deleteArticle(@RequestParam("id") Integer id){
            //        如果大于0则插入成功
            boolean deleteById = opai.deleteById(id)>0;
            return new ResultUtil(deleteById,"删除成功",200);


    }
    //--------------------------------------------
//    @ApiOperation(value = "对文章进行更新操作，传入一个对象")
//    @ApiImplicitParam(name = "article",value = "文章实体类对象" , required = true)

    @RequestMapping(value = "user/updateArticle",method = RequestMethod.PUT)
    public ResultUtil updateArticle(@RequestBody Article article){
            boolean updateById = opai.updateById(article);
            return new ResultUtil(updateById,"修改成功",200);

    }
//    ------------

//    @ApiOperation(value = "对文章进行查询操作，传入一个数值")
//    @ApiImplicitParam(name = "id",value = "文章实体类对象" , required = true)

    @RequestMapping(value = "user/getArticle",method = RequestMethod.GET)
    public ResultUtil getArticleById(@RequestParam("id") Integer id){
            Article byId = opai.getById(id);
            return new ResultUtil(true,"登录成功",byId,200);

    }
    @GetMapping("/logout")
    public ResultUtil logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
            return new ResultUtil(true,"退出登陆成功",200);
        }else{
            return new ResultUtil(false,"请先进行登录再注销",401);
        }
    }

    @Value("${file.upload.url}")
    private String uploadFilePath;
    //    @Slf4j
    @RequestMapping(value = "user/upload",method = RequestMethod.POST)
        public ResultUtil httpUpload(@RequestParam("files") MultipartFile files[]) {
        Subject subject = SecurityUtils.getSubject();

//        System.out.println(profile.getId());
//        会请求到logout
        if (!subject.isAuthenticated()) {
            return new ResultUtil(false,"管理员请先进行登录",401);
        } else {
            JSONObject object = new JSONObject();
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getOriginalFilename();  // 文件名
                File dest = new File(uploadFilePath + '/' + fileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    files[i].transferTo(dest);
                } catch (Exception e) {
                    return new ResultUtil(false, "程序错误，请重新上传",401);
                }
            }
            object.put("success", 1);
            object.put("result", "文件上传成功");
            return new ResultUtil(true, "文件上传成功",200);
        }
    }
    @Value("${file.upload.url}")
    private String downloadFilePath;
    @RequestMapping(value = "user/download",method = RequestMethod.GET)
    public ResultUtil fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResultUtil(false, "管理员请先进行登录",401);
        } else {
            File file = new File(downloadFilePath + '/' + fileName);
            if (!file.exists()) {
                return new ResultUtil(false, "文件不存在",401);
            }
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
                byte[] buff = new byte[1024];
                OutputStream os = response.getOutputStream();
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                return new ResultUtil(false, "下载失败",401);
            }
            return new ResultUtil(true, "下载成功",401);
        }
    }

}
