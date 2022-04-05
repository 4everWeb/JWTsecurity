package com.cos.jwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
public class RestApiController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	public RestApiController(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
//		this.userRepository = userRepository;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//	}
	
	@PostMapping("/home")
	public String home() {
		return "<H1>home</H1>";
		
	}
	
//	@GetMapping("admin/users")
//	public List<User> users(){
//		return UserRepository.findAll();
//	}
	
	
	@PostMapping("join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return "회원가입완료";
	}
	
	@GetMapping("/api/vi/user")
	public String user() {
		return "user";
	}
	@GetMapping("/api/vi/manager")
	public String manager() {
		return "manager";
	}
	@GetMapping("/api/vi/admin")
	public String admin() {
		return "admin";
	}
}
	
