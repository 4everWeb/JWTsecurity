package com.cos.jwt.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

//스프링 시큐리티에 UsernamePasswordAuthenticationFilter 가 있음.
// login 요청해서 useranme, password 전송하면 post.
// UsernamePasswordAuthenticationFilter 동작을 함. 
//config에서 막아서 작동을안함 . >> but 다시 등록해야함
@RequiredArgsConstructor
public class jwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
	
    @Autowired
    public jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("jwtAuthenticationFilter: 로그인시도중");
		
		// 1. username , password 받아서
		try {
//			
//			BufferedReader br =request.getReader();
//			
//			String input = null;
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// 2. 정상인지 로그인 시도 해보기 authenticationManager로 로그인 시도를 하면  principalDetailsService 호출  (loadUserByUsername 함수 실행)  principalDetails (return)
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			// 3. principalDetails 를 세션에 담고 (권한 관리를 위해서.)
			//authentication 객체가 session 영역에 저장됨 . => 로그인이 되었다는 뜻.
			// 리턴받는이유는 권한 관리를 security가 대신 해주기 떄문에 편하려고 리턴.
			// 굳이 jwt토큰을 사용하면서 세션을 만들 이유가 없음 . 근데 단지 권한 처리떄문에 session 넣어 줌.
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal(); 
			System.out.println(principalDetails.getUser().getUsername());
			
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		  return null;
	}
	//attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행 됨.
	//jwt 토큰을 만들어서 request 요청한 사용자에게 jwt토큰을 response 해주면 됨

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었음.");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal(); 
		
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username" , principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX);
		

	}
	
}
