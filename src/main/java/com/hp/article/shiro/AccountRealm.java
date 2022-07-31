package com.hp.article.shiro;



import com.hp.article.pojo.Admin;
import com.hp.article.service.OperateArticleService;
import com.hp.article.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户realm，用于在SecurityUtils.login()时进行登录验证信息
 */
//@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    OperateArticleService opai;


    /**
     * token支持
     * @param token
     * @return
     */
/*    @Override
    public boolean supports(AuthenticationToken token) {
//        判断这个token是不是jwtToken
        return token instanceof JwtToken;
    }*/


    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
/*        JwtToken jwtToken = (JwtToken) token;

//       根据token来获取到jwt
        Claims claim = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal());
        String userID = claim.getSubject();

        User user = userService.getById(Long.parseLong(userID));*/

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        System.out.println("转换成功！");
//        根据用户名查找用户
        Admin user = opai.getAdminByName(userToken.getUsername());

        System.out.println(user.toString());


        if (user == null){
            throw new UnknownAccountException("账户不存在！");
        }
//        如果被封号了

//        if (user.getStatus() == -1){
//            throw new LockedAccountException("账户被锁定！");
//        }
//        根据id生成token
        String jwtToken = jwtUtils.getToken(user.getId());

        AccountProfile profile = new AccountProfile();
//        将获取到的用户信息copy到新的映射中
//        BeanUtil.copyProperties(user,profile);

        BeanUtils.copyProperties(user,profile);
//        System.out.println("最后一步了" + SecurityUtils.getSubject());
        System.out.println("最后一步了" + SecurityUtils.getSubject().getPrincipal());
        System.out.println("获取到的jwtToken" + jwtToken);
//        返回用户信息
        return new SimpleAuthenticationInfo(profile,user.getAdminPassword(),"");
    }
}
