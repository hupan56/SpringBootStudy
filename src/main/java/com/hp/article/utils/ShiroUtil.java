package com.hp.article.utils;

import com.hp.article.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * shiro工具类
 */
public class ShiroUtil {

    /**
     * 获取到当前用户
     * @return
     */
    public static AccountProfile getProfile(){
        System.out.println("进入工具类");
//        AccountProfile accountProfile = new AccountProfile();
        AccountProfile accountProfile =  (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        System.out.println(accountProfile);
        System.out.println("user.toString()==================");
//        System.out.println(user.toString());
        return accountProfile;
    }

}
