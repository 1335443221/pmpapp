package com.sl.pmpapp.entity;

import java.io.Serializable;


public class User implements Serializable{
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private int id; //
	private String uname;
	private String name; //中文姓名
	private String pwd; 
	private String oper_pwd;//操作密码
	private String email; //邮箱
	private int type; //0：缴费用户 1：管理员
	private String reg_time;
	private String phone; //用户电话号码
	private String last_login_time;  //最后登录时间
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getOper_pwd() {
		return oper_pwd;
	}
	public void setOper_pwd(String oper_pwd) {
		this.oper_pwd = oper_pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	
	
	
	
	
	
	
	
	
	
}
