package com.example.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	public AuthResponse(String token, boolean b) {
		// TODO Auto-generated constructor stub
	}
	private String jwt;
	private String message;
}
