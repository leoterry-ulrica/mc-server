package com.dist.message.server.utils;

import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dist.message.server.conf.WSConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * java web token工具类
 * @author weifj
 *
 */
public class JWTUtil {
	
	private static Logger LOG = LoggerFactory.getLogger(JWTUtil.class);
	
	private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	/**
	 * 解析java web token
	 * @param jsonWebToken
	 * @param base64Security
	 * @return
	 */
	public static Claims parseJWT(String jsonWebToken, String base64Security) {
		try {
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
					.parseClaimsJws(jsonWebToken).getBody();
			return claims;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 生成token
	 * 
	 * @param name
	 *            用户名
	 * @param userId
	 *            用户id
	 * @param audience
	 *            受众，客户端id
	 * @param expirationMillis
	 *            过期时间（毫秒），截至到当前时间过期
	 * @param base64Security
	 *            base64安全码
	 * @return
	 */
	public static String createJWT(String name, String userId, String audience, long expirationMillis,
			String base64Security) {

		Key signingKey = getKey(base64Security);

		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.claim(WSConstants.SESSION_USERNAME, userId)
				.setSubject(name)
				.setAudience(audience).signWith(signatureAlgorithm, signingKey)
				.setExpiration(new Date(expirationMillis));
		// 生成JWT
		return builder.compact();
	}
	/**
	 * 验证token是否有效
	 * @param jsonWebToken
	 * @param base64Security
	 * @return
	 */
	public static boolean isValid(String jsonWebToken, String base64Security) {
        try {
        	Key signingKey = getKey(base64Security);
            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jsonWebToken.trim());
            return true;
        } catch (Exception e) {
        	LOG.error(e.getMessage());
            return false;
        }
    }
	/**
	 * 获取key
	 * @param base64Security
	 * @return
	 */
	private static Key getKey(String base64Security) {
		// 生成签名密钥
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		return signingKey;
	}
	/**
	 * 获取名称
	 * @param jsonWebToken
	 * @param base64Security
	 * @return
	 */
	public static String getName(String jsonWebToken, String base64Security) {
        if (isValid(jsonWebToken, base64Security)) {
        	Key signingKey = getKey(base64Security);
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jsonWebToken);
            return claimsJws.getBody().getSubject();
        }
        return null;
    }
}
