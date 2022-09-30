package com.mislab.common.utils;

import com.mislab.common.exception.BusinessException;
import com.mislab.common.result.ResponseEnum;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "krian123456";

    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary(tokenSignKey);
        return new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
    }

    public static String createToken(String uid, String employeeName) {
        String token = Jwts.builder()
                .setSubject("FINANCE-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("uid", uid)
                .claim("employeeName", employeeName)
                .signWith(SignatureAlgorithm.HS512, getKeyInstance())
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    public static boolean checkToken(String token) {
        if (StringUtils.hasLength(token)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEmployeeId(String token) {
        Claims claims = getClaims(token);
        String uid = (String) claims.get("uid");
        return uid;
    }

    public static String getUserName(String token) {
        Claims claims = getClaims(token);
        return (String) claims.get("userName");
    }

    public static void removeToken(String token) {
        //jwt token无需删除，客户端扔掉即可。
    }

    private static Claims getClaims(String token) {
        if (StringUtils.hasLength(token)) {
            // LOGIN_AUTH_ERROR(-211, "未登录"),
            throw new BusinessException(ResponseEnum.LOGIN_AUTH_ERROR);
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return claims;
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }
}

