package com.hp.article.shiro;
import com.hp.article.utils.JwtUtils;


import com.hp.article.pojo.Admin;
import com.hp.article.service.OperateArticleService;
//import com.hp.article.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户realm，用于在SecurityUtils.login()时进行登录验证信息
 @Slf4j
 public class JwtRealm extends AuthorizingRealm {
 /*
  * 多重写一个support
  * 标识这个Realm是专门用来验证JwtToken
  * 不负责验证其他的token（UsernamePasswordToken）
  * */
@Slf4j
public class JwtRealm extends AuthorizingRealm {
 /*
  * 多重写一个support
  * 标识这个Realm是专门用来验证JwtToken
  * 不负责验证其他的token（UsernamePasswordToken）
  * */
 @Override
 public boolean supports(AuthenticationToken token) {
  //这个token就是从过滤器中传入的jwtToken
  return token instanceof JwtToken;
 }

 //授权
 @Override
 protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
  return null;
 }

 //认证
 //这个token就是从过滤器中传入的jwtToken
 @Override
 protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

  String jwt = (String) token.getPrincipal();
  if (jwt == null) {
   throw new NullPointerException("jwtToken 不允许为空");
  }
  //判断
  JwtUtils jwtUtil = new JwtUtils();
  if (!jwtUtil.isVerify(jwt)) {
   throw new UnknownAccountException();
  }
  //下面是验证这个user是否是真实存在的
  String username = (String) jwtUtil.decode(jwt).get("username");//判断数据库中username是否存在
  log.info("在使用token登录"+username);
  return new SimpleAuthenticationInfo(jwt,jwt,"JwtRealm");
  //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名

 }

}




