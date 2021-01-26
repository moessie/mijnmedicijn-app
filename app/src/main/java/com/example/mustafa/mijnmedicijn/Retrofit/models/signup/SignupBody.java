package com.example.mustafa.mijnmedicijn.Retrofit.models.signup;

public class SignupBody{
	private String password;
	private String name;
	private String email;

	public SignupBody(String name, String email,String password) {
		this.password = password;
		this.name = name;
		this.email = email;
	}

	public String getPassword(){
		return password;
	}

	public String getName(){
		return name;
	}

	public String getEmail(){
		return email;
	}
}
