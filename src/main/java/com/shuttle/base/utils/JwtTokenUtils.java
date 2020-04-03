package com.shuttle.base.utils;

import com.shuttle.base.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/** @author D&M */
public class JwtTokenUtils {
  private static final String CLAIM_KEY_CREATED = "created";
  /** 生成足够的安全随机密钥，以适合符合规范的签名 */
  private static byte[] apiKeySecretBytes =
      DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);

  private static SecretKey secretKey = Keys.hmacShaKeyFor(apiKeySecretBytes);

  public static String createToken(String username, List<String> roles, boolean isRememberMe) {
    long expiration =
        isRememberMe ? SecurityConstants.EXPIRATION_REMEMBER : SecurityConstants.EXPIRATION;

    String tokenPrefix =
        Jwts.builder()
            .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .claim(SecurityConstants.ROLE_CLAIMS, String.join(",", roles))
            .setIssuer("SnailClimb")
            .setIssuedAt(new Date())
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .compact();
    return SecurityConstants.TOKEN_PREFIX + tokenPrefix;
  }

  private boolean isTokenExpired(String token) {
    Date expiredDate = getTokenBody(token).getExpiration();
    return expiredDate.before(new Date());
  }

  public static String getUsernameByToken(String token) {
    return getTokenBody(token).getSubject();
  }

  /** 获取用户所有角色 */
  public static List<SimpleGrantedAuthority> getUserRolesByToken(String token) {
    String role = (String) getTokenBody(token).get(SecurityConstants.ROLE_CLAIMS);
    return Arrays.stream(role.split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  private static Claims getTokenBody(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public Boolean validateToken(String token) {
    return (!isTokenExpired(token));
  }

  public Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }
}
