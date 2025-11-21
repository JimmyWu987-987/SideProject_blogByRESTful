package com.mysideproject.blog.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mysideproject.blog.model.CustomUserDetails; // 引入你自訂的 UserDetails 類別

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders; // 用於 base64 解碼
import io.jsonwebtoken.security.Keys; // 用於產生 Key 物件

@Component
public class JwtUtils {
	
    // 引入 Logger 用於記錄錯誤訊息
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // 密鑰：從 application.properties 注入
	@Value("${mysideproject.app.jwtSecret}") 
    private String jwtSecret;

    // 過期時間：從 application.properties 注入
	@Value("${mysideproject.app.jwtExpirationMs}")
    private long jwtExpirationMs;
    
    // ====================================================
    // 輔助方法：將 String 密鑰轉換為 Key 物件
    // ====================================================
    private Key key() {
        // 使用 Keys.hmacShaKeyFor() 確保密鑰是 HMAC 演算法所需的安全長度 (byte 陣列)
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ====================================================
    // 方法一：產生 Token (已完成)
    // ====================================================
    public String generateJwtToken(Authentication authentication) {

        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                // 設置 Token 的主題 (Subject)，即我們的 username
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                // 設置簽名 (使用新的 signWith(Key))
                .signWith(key(), SignatureAlgorithm.HS512) 
                .compact();
    }
    
    // ====================================================
    // 方法二：解析 Token，取出 Subject (Username)
    // ====================================================
    /**
     * 從 JWT Token 中解析出使用者名稱 (username)
     */
    public String getUserNameFromJwtToken(String token) {
        
        // Jwts.parserBuilder()：建立解析器
    	// parseBuilder() 在 0.12 版本中已更改為 parser()
        return Jwts.parser()
                // .setSigningKey(key())：設定用於驗證簽名的密鑰 (使用我們的方法一產生的 Key)
                .setSigningKey(key())
                // .build()：建構解析器
                .build()
                // .parseClaimsJws(token)：解析 Token，如果 Token 無效 (如簽名錯誤、過期)，這裡會拋出例外
                .parseClaimsJws(token) 
                // .getBody()：取得 Payload 內容 (Claims)
                .getBody()
                // .getSubject()：取得我們在方法一中設定的 Subject (即 username)
                .getSubject();
    }
    
    // ====================================================
    // 方法三：驗證 Token 是否有效
    // ====================================================
    /**
     * 驗證 JWT Token 是否有效 (簽名和過期時間)
     * @return 如果有效則返回 true，否則返回 false
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // 嘗試解析 Token。如果解析成功，表示簽名和格式都正確。
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            // Token 格式錯誤或無效的簽名 (無法解析)
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            // Token 已過期 (Expiration Claim 無效)
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // Token 格式不受支援 (例如 JWS 而非 JWE)
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // Token 為空字串或 null
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) { 
        	// 捕捉其他所有例外 (例如簽名不符)
            logger.error("JWT validation failed: {}", e.getMessage());
        }
        
        return false;
    }
}