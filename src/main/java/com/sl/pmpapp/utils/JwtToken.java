package com.sl.pmpapp.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
 
/**
 * APP登录Token的生成和解析
 * 
 */
public class JwtToken {
 
	/** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
	public static final String SECRET = "JKKLJOoasdlfj";
	/** token 过期时间: 10天 */
	public static final int calendarField = Calendar.DATE;
	public static final int calendarInterval = 10;
 
	public static void main(String[] args) {
		long a=1;
		try {
			System.out.println(JwtToken.createToken(a));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * JWT生成Token.<br/>
	 * 
	 * JWT构成: header, payload, signature
	 * 
	 * @param user_id
	 *            登录成功后用户user_id, 参数user_id不可传空
	 */
	public static String createToken(Object user_id) throws Exception {
		Date iatDate = new Date();
		// expire time
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(calendarField, calendarInterval);
		Date expiresDate = nowTime.getTime();
 
		// header Map
		Map<String, Object> map = new HashMap<>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
 
		// build token
		// param backups {iss:Service, aud:APP}
		String token = JWT.create().withHeader(map) // header
				.withClaim("iss", "Service") // payload
				.withClaim("aud", "APP").withClaim("user_id", null == user_id ? null : user_id.toString())
				.withIssuedAt(iatDate) // sign time
				.withExpiresAt(expiresDate) // expire time
				.sign(Algorithm.HMAC256(SECRET)); // signature
 
		return token;
	}
 
	/**
	 * 解密Token
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Claim> verifyToken(String token) {
		DecodedJWT jwt = null;
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
			jwt = verifier.verify(token);
		} catch (Exception e) {
			// e.printStackTrace();
			// token 校验失败, 抛出Token验证非法异常
			return null;
		}
		return jwt.getClaims();
	}
 
	/**
	 * 根据Token获取user_id
	 * 
	 * @param token
	 * @return user_id
	 */
	public static String getAppUID(String token) {
		Map<String, Claim> claims = verifyToken(token);
		Claim user_id_claim = claims.get("user_id");
		if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
			// token 校验失败, 抛出Token验证非法异常
		}
		return user_id_claim.asString();
	}
	
	
	/**
	 * 通过token获取项目id
	 * @param token
	 * @return
	 */
	public static String getProject_id(String token){
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Gson gson=new Gson();
		HashMap<String,Object> userData = gson.fromJson(JwtToken.getAppUID(token), HashMap.class);
		map2.put("uid",userData.get("uid"));
		map2=CEUtils.pmpEncrypt(map2); //加密传输数据
		String GET_URL = "http://192.168.0.60:8001/user/get_user_project_info";   //获取接口的数据
		HttpClientService hc=new HttpClientService();
		String data = hc.get(GET_URL,map2); //接口数据String类型
		//JSONArray ja=JSONObject.parseArray(data);
		Map<String,Object> userData2 = gson.fromJson(data, Map.class);
		
		return userData2.get("project_id")+"";
	}

	
	
	
	
	
	
	
	
}
