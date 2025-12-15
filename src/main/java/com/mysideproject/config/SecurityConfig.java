package com.mysideproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mysideproject.blog.jwt.AuthEntryPointJwt;
import com.mysideproject.blog.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	// 自訂是否有Token的過濾器
	@Autowired
	private AuthTokenFilter authTokenFilter;
	
	// JWT 的例外處理入口
	@Autowired
	private AuthEntryPointJwt unauthEntryPointJwt;
	
	// 定義編碼器
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 定義 AuthenticationManager Bean (必須，供 AuthController 注入)
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	
	// JWT 機制的核心配置
	// 設定 API 請求的是否能公開或需要驗證等等設定權限的方法
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
			// JWT 機制：禁用 CSRF
			.csrf(csrf -> csrf.disable())
			// JWT 機制：Session 設為無狀態 (STATELESS)，本專案設定不會使用 Session
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 配置例外處理：設定當用戶未經授權時，由誰來處理
			.exceptionHandling(exception -> exception
					.authenticationEntryPoint(unauthEntryPointJwt))
			// 設定授權規則
			.authorizeHttpRequests(auth -> auth
					// 登入請求: 允許任何人訪問
					.requestMatchers("/api/v1/auth/signin").permitAll()
					// 註冊請求: 允許任何人訪問
					.requestMatchers("/api/v1/auth/signup").permitAll()
					// 所有文章列表 GET(/api/v1/posts) 允許任何人做請求 
					.requestMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
					// GET(/api/v1/posts) 需要身份驗證後，才能請求
					.requestMatchers(HttpMethod.POST, "/api/v1/posts").authenticated()

					// ========== 新增：放行 Swagger UI 相關路徑 ==========
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    // ========== 新增：放行前端頁面與靜態資源 ==========
                    .requestMatchers("/front_end/**").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .requestMatchers("/css/**").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    // ========== 新增：放行 PageController 請求 ==========
                    .requestMatchers("/blog/posts").permitAll()
					
					// 除了上述特定的請求，所有請求都需要身份驗證
					.anyRequest().authenticated()
			);
		
        // 加入 JWT 過濾器
		http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
		
		
		return http.build();
	}

}
