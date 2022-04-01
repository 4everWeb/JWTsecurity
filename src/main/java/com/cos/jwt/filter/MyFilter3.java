package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		//토큰 cos 만들어야함 .  ID PW 정상적으로 넘어와 로그인 완료시 >> 토큰을 만들어주고 그걸 응답
		//요청 시 header에 authorization에 value 값으로 토큰을 가져옴
		// 토큰이 넘어오면 내가 만든 토큰이 맞는지 검증(rsa , hs256)
		if(req.getMethod().equals("POST")) {
			System.out.println("post 요청!");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			System.out.println("필터3 security 필터 before 동작");
		
			if(headerAuth.equals("cos")) {
				chain.doFilter(request, response);
			}else {
				PrintWriter out = res.getWriter();
				out.println("인증안됨");
			}
		}
	}
}
