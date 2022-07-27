package com.hp.article.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
//用这个不报错
import io.jsonwebtoken.SignatureAlgorithm;
//        用下面报错
//import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Date;
@Component
@Slf4j
@ConfigurationProperties(prefix = "admin.jwt")
@Data
public class JwtUtils {
    private String secret;
    private Long   expire;
    private String header;

    /**
     * 生成jwt token
     */
//    public String generateToken(long userId) {
//
//        Date nowDate = new Date();
////        设置获取过期时间
//        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
//
//        return Jwts.builder()
//                .setHeaderParam("typ", "JWT")    //设置头部信息
//                .setSubject(userId + "")    //设置用户id
//                .setIssuedAt(nowDate)       //设置开始时间
//                .setExpiration(expireDate)  //设置过期时间
//                .signWith(SignatureAlgorithm.HS512, secret)  //设置加密算法
//                .compact();//生成token
//    }
    public String getToken(long adminId){
        Date nowDate=new Date();
        Date expireDate= new Date(nowDate.getTime()+expire*1000);

        return Jwts.builder()
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .setHeaderParam("typ","JWY")
                .setSubject(adminId+"")
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }
    /**
     * 获取jwt的信息
     * 解析token获得token的信息
     */
    public Claims getClaimByToken(String token) {

        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("获取token时的异常：：：" + e);
            return null;
        }

    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
