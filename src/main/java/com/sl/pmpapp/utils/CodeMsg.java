package com.sl.pmpapp.utils;

public class CodeMsg {
	private String code;
	private String msg;
	// 按照模块定义CodeMsg
	// 通用异常
	public static CodeMsg SUCCESS = new CodeMsg("1000","OK");
	public static CodeMsg TOKEN_FAILS = new CodeMsg("1001","token认证失败");
	public static CodeMsg  AUTH_EXPIRES= new CodeMsg("1002","授权过期");
	public static CodeMsg  MISSING_PARAMETER= new CodeMsg("1003","缺失参数");
	public static CodeMsg SERVER_EXCEPTION = new CodeMsg("1004","网络异常");
	// 业务异常
	public static CodeMsg PW_INCORRECT = new CodeMsg("1005","密码错误"); 
	public static CodeMsg MISSING_LOGIN_AUTH = new CodeMsg("1006","没有登录权限"); 
	public static CodeMsg NOT_FIND_DATA = new CodeMsg("1007","查找不到对应数据");
	public static CodeMsg MISSING_PATH = new CodeMsg("1008","地址路径错误404");
	public static CodeMsg SERVER_ERROR = new CodeMsg("1009","内部服务器错误500");
	
	public static CodeMsg OPERATION_FAIL = new CodeMsg("1010","操作失败");
	
	private CodeMsg(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
