package com.cos.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CorsConfig corsConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);  //스프링 필터 이전에 필터 커스텀필터등록 
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 만들지 않음
		.and()
		.addFilter(corsConfig.corsFilter())    // @CrossOrigin(인증 x),시큐리티 필터에 등록 인증(O)
		.formLogin().disable()  // 폼 로그인을 하지 않음
		.httpBasic().disable()  // 기본인증 방식 사용하지 않음
		.authorizeRequests()
		.antMatchers("api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("api/v1/manager/**")
		.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("api/v1/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll();
		
		
	}



	
	
}
