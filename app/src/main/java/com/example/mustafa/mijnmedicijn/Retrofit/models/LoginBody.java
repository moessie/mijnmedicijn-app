package com.example.mustafa.mijnmedicijn.Retrofit.models.login;

public class LoginBody {

	private String password;
	private String email;

	public LoginBody(String email,String password) {
		this.password = password;
		this.email = email;
	}

	public String getPassword(){
		return password;
	}

	public String getEmail(){
		return email;
	}
}