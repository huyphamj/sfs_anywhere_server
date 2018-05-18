package com.aia.db.bean;

public class UserBean {
	private String name, password, phonenumber, avatar, address = "", static_code = "", dynamic_code = "";

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatic_code() {
		return static_code;
	}

	public void setStatic_code(String static_code) {
		this.static_code = static_code;
	}

	public String getDynamic_code() {
		return dynamic_code;
	}

	public void setDynamic_code(String dynamic_code) {
		this.dynamic_code = dynamic_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
}
