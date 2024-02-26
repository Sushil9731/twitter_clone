package com.example.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse {

	private String message;
	private boolean Status;
	public ApiResponse(String string, boolean b) {
		// TODO Auto-generated constructor stub
	}
	public void setMessage(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setStatus(boolean b) {
		// TODO Auto-generated method stub
		
	}
}

