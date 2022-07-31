package com.hp.article.shiro;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hp.article.utils.JwtUtils;
import com.hp.article.utils.ResultUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 配置jwt的过滤器，可以自动登录方法的的过滤器
 */

@Component
public class JwtFilter extends AuthenticatingFilter {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//        将servletRequest强转为HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        从request的头部中获取到jwt
        String jwt = request.getHeader("authorization");
        System.out.println("JwtFilter获取到的token===" + jwt);
//        判断如果jwt为空
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }
//      返回jwt
        return new JwtToken(jwt);
    }



//    /**
//     * 执行认证
//     *
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     */
//    public boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        JwtToken jwtToken = (JwtToken) createToken(request, response);
//
//        try {
//            SecurityUtils.getSubject().login(jwtToken);
//        } catch (Exception e) {
//            if (e.getClass().getName().equalsIgnoreCase(AuthenticationException.class.getName())) {
//                responseToken(response, "Token无效，您无权访问该接口!", 444);
//            } else if (e.getClass().getName().equalsIgnoreCase(UnknownAccountException.class.getName())) {
//                responseToken(response, "用户名不存在!", 444);
//            } else if (e.getClass().getName().equalsIgnoreCase(IncorrectCredentialsException.class.getName())) {
//                responseToken(response, "密码错误!", 444);
//            } else if (e.getClass().getName().equalsIgnoreCase(ExpiredCredentialsException.class.getName())) {
//                responseToken(response, "Token已过期!",444);
//            } else {
//                responseToken(response, "其他错误!", 444);
//            }
//            return false;
//        }
//        return true;
//    }
    /**
     * 无需转发，直接返回Response信息 Token认证错误
     */
    private void responseToken(ServletResponse response, String msg, int status) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(status);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            out.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /**
     * 拦截校验，当头部没有Authorization时候，我们直接通过，不需要自动登录；
     * 当带有的时候，首先我们校验jwt的有效性，没问题我们就直接执行executeLogin方法实现自动登录
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //        将servletRequest强转为HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        从request的头部中获取到jwt
        String jwt = request.getHeader("authorization");
//        判断如果jwt为空,说明没登录信息，直接交给注解进行拦截
        if (StringUtils.isEmpty(jwt)) {
            return true;
        } else {
//            校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
//            判断如果获取到的token不存在或者已经失效
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("token不存在或已失效！请重新登录");
            }

//            执行登录
            return executeLogin(servletRequest, servletResponse);
        }

    }

    /**
     * 重写登录失败的方法
     * 登录异常时候进入的方法，我们直接把异常信息封装然后抛出
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            //        获取到错误信息并返回
            Throwable cause = e.getCause() == null ? e : e.getCause();

//            ResultUtil result = ResultUtil.fail(cause.getMessage());
            ResultUtil resultUtil = new ResultUtil(false, cause.getMessage());

//        将信息转为json串
            String jsonStr = JSON.toJSONString(resultUtil);
//            String jsonStr = JSONUtil.toJsonStr(result);


//            将json串输出
            httpServletResponse.getWriter().print(jsonStr);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }

//    /**
//     * 对跨域提供支持
//     * 拦截器的前置拦截，因为我们是前后端分析项目，
//     * 项目中除了需要跨域全局配置之外，我们再拦截器中也需要提供跨域支持。
//     * 这样，拦截器才不会在进入Controller之前就被限制了。
//     *
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     */
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
//        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }
    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletRequest req, ServletResponse resp) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
