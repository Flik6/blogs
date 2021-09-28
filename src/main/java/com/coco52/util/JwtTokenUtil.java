package com.coco52.util;

import com.coco52.entity.MyUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成荷载
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails, MyUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put("uuid",user.getUuid());
        claims.put("nickname",user.getNickname());
        claims.put("avatar",user.getAvatar());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }



    /**
     * 根据用户信息生成荷载
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取用户名
     *
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        Claims claims = getClaimsFormToken(token);
        try {
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;

    }
    /**
     * 根据token获取uuid
     * @param token
     * @return
     */
    public String getUUIDFromToken(String token){
        String uuid;
        Claims claimsFormToken = getClaimsFormToken(token);
        try {
            uuid = (String)claimsFormToken.get("uuid");
        } catch (Exception e) {
            uuid=null;
        }
        return uuid;
    }

    /**
     * 从token中获取载荷
     *
     * @param token
     * @return
     */
    private Claims getClaimsFormToken(String token) {
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 验证token是否有效
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否过期
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 判断token是否可以被刷新
     *
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFormToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取失效时间
     *
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claimsFormToken = getClaimsFormToken(token);
        return claimsFormToken.getExpiration();
    }

    /**
     * 根据荷载生成JWT token
     *
     * @param claim
     * @return
     */
    private String generateToken(Map<String, Object> claim) {

        return Jwts.builder()
                .setClaims(claim)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成Token的失效时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);

    }

}
