/**
 * 作者：pjp
 * 邮箱：vippjp@163.com
 */
package com.sl.pmpapp.utils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
 
import javax.crypto.Cipher;
 
public class Rsa {
	
	private String priKey;
	private String pubKey;
	
	public static void main(String[] args) {
		Rsa rsa = new Rsa();
		String str = "我要加密这段文字。";
		System.out.println("原文:"+"我要加密这段文字。");
		String crypt = rsa.encryptByPrivateKey(str);
		System.out.println("私钥加密密文:"+crypt);
		String result = rsa.decryptByPublicKey(crypt);
		System.out.println("原文:"+result);
		
		System.out.println("---");
		
		str = "我要加密这段文字。";
		System.out.println("原文:"+"我要加密这段文字。");
		crypt = rsa.encryptByPublicKey(str);
		System.out.println("公钥加密密文:"+crypt);
		result = rsa.decryptByPrivateKey(crypt);
		System.out.println("原文:"+result);
		
		System.out.println("---");
		
		str = "我要签名这段文字。";
		System.out.println("原文："+str);
		String str1 = rsa.signByPrivateKey(str);
		System.out.println("签名结果："+str1);
		if(rsa.verifyByPublicKey(str1, str)){
			System.out.println("成功");
		} else {
			System.out.println("失败");
		}
	}
 
	public Rsa(){
		priKey = readStringFromFile("E://rsa_private_key.pem");
		pubKey = readStringFromFile("E://rsa_public_key.pem");
	}
	
	  /**
	   * 使用私钥加密
	   * @see decByPriKey
	   */
	  public String encryptByPrivateKey(String data) {
	    // 加密
	    String str = "";
	    try {
			byte[] pribyte = base64decode(priKey.trim());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
	    	Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	c1.init(Cipher.ENCRYPT_MODE, privateKey);
	    	str = base64encode(c1.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			
	    }
	    return str;
	  }
	  
	  /**
	   * 使用私钥解密
	   * @see decByPriKey
	   */
	  public String decryptByPrivateKey(String data) {
	    // 加密
	    String str = "";
	    try {
			byte[] pribyte = base64decode(priKey.trim());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
	    	Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	c1.init(Cipher.DECRYPT_MODE, privateKey);
	    	byte[] temp = c1.doFinal(base64decode(data));
	    	str = new String(temp);
		} catch (Exception e) {
			e.printStackTrace();
			
	    }
	    return str;
	  }
 
	  
	  /**
	   * 使用公钥加密
	   * @see decByPriKey
	   */
	  public String encryptByPublicKey(String data) {
	    // 加密
	    String str = "";
	    try {
	    	byte[] pubbyte = base64decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZsiPMXh0NgV/RmFtBbWu11Pyr"+
	    			"P+jFr/F/fwdZFOeI+VL51PvpY+eW5NDuI7fCeNXePKYBE122zMpU8RqmXjjaGyr1"+
	    			"JRjt3sS+yMC3pDcANnooDJh9cNURNyUc6saTcIvuLnqaK7xsQHLY4L2nrthBpzhJ"+
	    			"6nOx7CRQ8o3hSphjEQIDAQAB");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
	    	Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	c1.init(Cipher.ENCRYPT_MODE, rsaPubKey);
	    	str = base64encode(c1.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			
	    }
	    return str;
	  }
	  
	  
	  
	
	  /**
	   * 使用公钥解密
	   * @see decByPriKey
	   */
	  public String decryptByPublicKey(String data) {
	    // 加密
	    String str = "";
	    try {
			byte[] pubbyte = base64decode(pubKey.trim());
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
	    	Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	c1.init(Cipher.DECRYPT_MODE, rsaPubKey);
	    	byte[] temp = c1.doFinal(base64decode(data));
	    	str = new String(temp);
		} catch (Exception e) {
			e.printStackTrace();
			
	    }
	    return str;
	  }
	/**
	 * 本方法使用SHA1withRSA签名算法产生签名
	 * @param String src 签名的原字符串
	 * @return String 签名的返回结果(16进制编码)。当产生签名出错的时候，返回null。
	 */
	public String signByPrivateKey(String src) {
		try {
			Signature sigEng = Signature.getInstance("SHA1withRSA");
			byte[] pribyte = base64decode(priKey.trim());
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
			sigEng.initSign(privateKey);
			sigEng.update(src.getBytes());
			byte[] signature = sigEng.sign();
			return base64encode(signature);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	/**
	 * 使用共钥验证签名
	 * @param sign
	 * @param src
	 * @return
	 */
	public boolean verifyByPublicKey(String sign, String src) {
		try {
			Signature sigEng = Signature.getInstance("SHA1withRSA");
			byte[] pubbyte = base64decode(pubKey.trim());
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
			sigEng.initVerify(rsaPubKey);
			sigEng.update(src.getBytes());
			byte[] sign1 = base64decode(sign);
			return sigEng.verify(sign1);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
 
	/**
	 *  base64加密
	 * @param bstr
	 * @return
	 */
	@SuppressWarnings("restriction")
	private String base64encode(byte[] bstr) {
		String str =  new sun.misc.BASE64Encoder().encode(bstr);
		str = str.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
		return str;
	}
 
	/**
	 * base64解密
	 * @param str
	 * @return byte[]
	 */
	@SuppressWarnings("restriction")
	private byte[] base64decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return bt;
	}
 
	/**
	 * 从文件中读取所有字符串
	 * @param fileName
	 * @return	String
	 */
	private String readStringFromFile(String fileName){
		StringBuffer str = new StringBuffer();
		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			char[] temp = new char[1024];
			while (fr.read(temp) != -1) {
				str.append(temp);
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
 
		}
		return str.toString();
	}
}
