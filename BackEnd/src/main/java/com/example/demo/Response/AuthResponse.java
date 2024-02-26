package com.example.demo.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	
	private String jwt;
	private boolean status;
	public void setStatus(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public void setJwt(String token) {
		// TODO Auto-generated method stub
		
	}

}
