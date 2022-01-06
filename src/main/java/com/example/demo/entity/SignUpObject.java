package com.example.demo.entity;

public class SignUpObject {
	private String id;
	private String username;
	private String password;
	private String confirm_password;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SignUpObject{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", confirm_password='" + confirm_password + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
