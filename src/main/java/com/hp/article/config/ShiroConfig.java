package com.hp.article.config;

import com.hp.article.shiro.AccountRealm;
import com.hp.article.shiro.JwtFilter;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;


/**
 * shiro启用注解拦截控制器
 */
@Configuration
public class ShiroConfig {


    @Autowired
    JwtFilter jwtFilter;

    /**
     * 1、建立用户realm
     *
     * @return
     */
    @Bean("accountRealm")
    public AccountRealm userRealm() {
        return new AccountRealm();
    }


    /**
     * 2、配置ShiroFilterFactoryBean
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);

//        封装jwt
        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);

        bean.setFilters(filters);

        return bean;
    }
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        // 哪些请求可以匿名访问
          /*
        map 中value 的意义
        * anon: 无需认证就可以访问资源；
        * authc：必须认证后才能访问资源；
        * user：必须拥有“记住我”功能才能访问资源；
        * perms：拥有对某个资源的权限才能访问资源；
        * role：拥有某个角色权限才能访问资源
        * **/
        chain.addPathDefinition("/user/login", "anon");      // 登录接口
        chain.addPathDefinition("/user/notLogin", "anon");   // 未登录错误提示接口
        chain.addPathDefinition("/403", "anon");    // 权限不足错误提示接口
        // 除了以上的请求外，其它请求都需要登录
        chain.addPathDefinition("user/**", "authc");
        return chain;
    }

    /**
     * 3、配置DefaultWebSecurityManager
     *
     * @param
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("accountRealm") AccountRealm accountRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(accountRealm);

        /**
         * 关闭shiro自带的session，
         */
/*        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);*/

        return securityManager;
    }









    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions)
     * @return
     * DefaultAdvisorAutoProxyCreator的顺序必须在shiroFilterFactoryBean之前，不然SecurityUtils.getSubject().getPrincipal()获取不到参数
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        advisorAutoProxyCreator.setUsePrefix(true);
        return advisorAutoProxyCreator;
    }

//    /**
//     * 开启aop注解支持
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }





}

