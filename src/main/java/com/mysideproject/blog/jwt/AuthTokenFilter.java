package com.mysideproject.blog.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mysideproject.blog.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl userDetailService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 1. 從請求中解析出 JWT
		String jwt = parseJwt(request);
		
		if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
			
			// 2. Token 有效，取出用戶名
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			
			// 3. 載入 UserDetails 取得 會員相關資料
			UserDetails userDetails = userDetailService.loadUserByUsername(username);
			
			// 4. 構建並設置 Authentication 物件到 SecurityContext
            UsernamePasswordAuthenticationToken authentication = 
                // TODO: 請填寫構造函式參數。
                // 提示：需要傳入 UserDetails, 密碼(null), 和權限列表(UserDetails.getAuthorities())
                new UsernamePasswordAuthenticationToken(
                    // 參數 1: Principal (使用者主體資訊，即 userDetails)
//                	使用者身份： 這是 Spring Security 識別「誰」在操作的物件。
//                	當驗證成功後，它會被儲存在 SecurityContext 中。
                    userDetails,
                    // 參數 2: Credentials (密碼，JWT 驗證階段不需要密碼，故傳入 null)
//                    憑證： 在登入階段（AuthController），這裡傳入的是密碼。
//                    但在 JWT 過濾器中，我們已經完成了身份驗證，Token 本身就是憑證，密碼不再需要。
//                    因此傳入 null 是標準做法。
                    null,
                    // 參數 3: Authorities (權限列表，從 userDetails 取得)
//                    權限： 包含使用者被授予的角色和權限列表，用於後續的權限檢查 
//                    (e.g., @PreAuthorize("hasRole('ADMIN')"))。
                    userDetails.getAuthorities()
                );
			
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 將 Authentication 設置到 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

			
		}
		
		filterChain.doFilter(request, response);
	}

	// 從 Header 中取得 Token
	private String parseJwt(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
//		取得授權標頭 (Authorization Header): 
//		根據 RESTful API 的業界慣例，客戶端（例如網頁前端或 Postman）在發送受保護的請求時，
//		必須將 JWT Token 放在 HTTP 請求的 Authorization 標頭中。
		String headerAuth = request.getHeader("Authorization");
		
//		Bearer Scheme 檢查： 
//		JWT 通常採用 Bearer Token Scheme
//		意即客戶端會這樣傳送 Token：Authorization: Bearer <你的JWT字串> (注意 Bearer 後面要有有空格)。
		
		if(headerAuth != null && headerAuth.startsWith("Bearer ")) {
			
//			"Bearer " 總共有 7 個字元 (B+e+a+r+e+r+空格 = 7)。
//			substring(7) 從第 7 個索引（即第 8 個字元）開始截取
//			正好跳過 Bearer 這個前綴，將後面的 純淨 JWT 字串 提取出來。
			
			return headerAuth.substring(7);
		}
		
//		如果請求中根本沒有 Authorization 標頭，或者格式不對（例如沒有 Bearer 前綴）
//		則返回 null，表示沒有找到有效的 JWT，後續的驗證流程就會跳過。
		
		return null;
	}

}
