package com.sl.pmpapp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class Jipush {
	private static final Logger LOGGER = LoggerFactory.getLogger(JPushUtil.class);
	public static final String APPKEY = "b321c388ca774b66c8decc3d";
	public static final String MASTERSECRET = "60966d444dbd2d74506df082";
	public static JPushClient jpushClient = null;
	private final static Boolean ApnsProduction = true;//上线之后要改为true
 
	
	public static void sendPush(String msg) {
		jpushClient = new JPushClient(MASTERSECRET, APPKEY, 3);
		PushPayload payload = buildPushObjectByMessage(msg);
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (Exception e) {
            e.printStackTrace();
		}
	}
 
	public static PushPayload buildPushObjectByMessage(String msg) {
		return PushPayload.newBuilder().setPlatform(Platform.all())// 设置接受的平台
				.setAudience(Audience.all())// Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
				.setMessage(Message.newBuilder()
						.setMsgContent(msg)
						.build())
				.build();
	}
 
	/**
	 * 根据别名发送
	 */
	public static void sendByAlias(String content, Map<String, String> params, String userId) {
		ClientConfig clientConfig = ClientConfig.getInstance();
		JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY, null, clientConfig);
		List<String> users = new ArrayList<>();
		users.add(userId);
		PushPayload payload = buildPushObjectByAlias(content, params, users);
		try {
			PushResult result = jpushClient.sendPush(payload);
			LOGGER.info("Got result - " + result);
		} catch (APIConnectionException e) {
			LOGGER.error("连接错误. 请过会儿重试. ", e);
		} catch (APIRequestException e) {
			LOGGER.error("Error response from JPush server. Should review and fix it. ", e);
			LOGGER.info("HTTP Status: " + e.getStatus());
			LOGGER.info("Error Code: " + e.getErrorCode());
			LOGGER.info("Error Message: " + e.getErrorMessage());
			LOGGER.info("Msg ID: " + e.getMsgId());
		}
	}
 
	private static PushPayload buildPushObjectByAlias(String content, Map<String, String> params, List<String> userIds) {
		PushPayload.Builder builder = PushPayload.newBuilder();
		builder.setPlatform(Platform.all());
		builder.setAudience(Audience.alias(userIds));
		Notification.Builder notificationBuilder = Notification.newBuilder();
		notificationBuilder.setAlert(content);
		notificationBuilder.addPlatformNotification(IosNotification.newBuilder().setAlert(content).setSound("happy").addExtras(params).build());
		notificationBuilder.addPlatformNotification(AndroidNotification.newBuilder().setTitle("系统推送").setAlert(content).addExtras(params).build());
		builder.setNotification(notificationBuilder.build());
		return builder.build();
	}
 
	    /**
	     * 根据别名集合发送
	     */
	    public static void sendByAliasList(String content, Map<String, String> params,List<String> users) {
	        ClientConfig clientConfig = ClientConfig.getInstance();
	        JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY, null, clientConfig);
	        PushPayload payload = buildPushObjectByAlias(content, params, users);
	        try {
	            PushResult result = jpushClient.sendPush(payload);
	            LOGGER.info("Got result - " + result);
	        } catch (APIConnectionException e) {
	        	LOGGER.error("连接错误. 请过会儿重试. ", e);
	        } catch (APIRequestException e) {
	        	LOGGER.error("Error response from JPush server. Should review and fix it. ", e);
	        	LOGGER.info("HTTP Status: " + e.getStatus());
	            LOGGER.info("Error Code: " + e.getErrorCode());
	            LOGGER.info("Error Message: " + e.getErrorMessage());
	            LOGGER.info("Msg ID: " + e.getMsgId());
	        }
	    }
	   //极光推送>>Android
	   //Map<String, String> parm是我自己传过来的参数,同学们可以自定义参数
	   public static void jpushAndroid(Map<String, String> parm) {
	       //创建JPushClient(极光推送的实例)
	       JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY);
	       //推送的关键,构造一个payload 
	       PushPayload payload = PushPayload.newBuilder()
	            .setPlatform(Platform.android())//指定android平台的用户
	            .setAudience(Audience.all())//你项目中的所有用户
	            .setNotification(Notification.android(parm.get("msg"), "这是title", parm))
	            //发送内容,这里不要盲目复制粘贴,这里是我从controller层中拿过来的参数)
	            .setOptions(Options.newBuilder().setApnsProduction(false).build())
	            //这里是指定开发环境,不用设置也没关系
	            .setMessage(Message.content(parm.get("msg")))//自定义信息
	            .build();

	       try {
	            PushResult pu = jpushClient.sendPush(payload);  
	        } catch (APIConnectionException e) {
	            e.printStackTrace();
	        } catch (APIRequestException e) {
	            e.printStackTrace();
	        }    
	   }
	   
	   
	   
	   /**
	     * 推送给Tag参数的用户
	     * @param tagsList Tag或Tag组
	     * @param msg_content 消息内容
	     * @param extrasparam 扩展字段
	     * @return 0推送失败，1推送成功
	     */
	    public static int sendToTagList(List<String> tagsList, String msg_content,String extra, JsonObject extrasparam,String notification_title) {
	    	ClientConfig clientConfig = ClientConfig.getInstance();
	    	JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY, null, clientConfig);
	    	
	    	int result = 0;
	        try {
	            PushPayload pushPayload= buildPushObject_all_tagList_alertWithTitle(tagsList,msg_content,extra,extrasparam,notification_title);
	            LOGGER.info(""+pushPayload);
	            PushResult pushResult=jpushClient.sendPush(pushPayload);
	            LOGGER.info(""+pushResult);
	            if(pushResult.getResponseCode()==200){
	                result=1;
	            }
	        } catch (APIConnectionException e) {
	            e.printStackTrace();

	        } catch (APIRequestException e) {
	            e.printStackTrace();
	        }

	        return result;
	    }

	    
	    
	    /**
	     *向所有平台单个或多个指定Tag用户推送消息
	     * @param tagsList
	     * @param msg_content
	     * @param extrasparam
	     * @return
	     */
	    private static PushPayload buildPushObject_all_tagList_alertWithTitle(List<String> tagsList,  String msg_content,String extraKey, JsonObject extrasparam,String notification_title) {

	        LOGGER.info("----------向所有平台单个或多个指定Tag用户推送消息中.......");
	        //创建一个IosAlert对象，可指定APNs的alert、title等字段
	        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

	        return PushPayload.newBuilder()
	                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
	                .setPlatform(Platform.android())
	                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
	                .setAudience(Audience.tag_and(tagsList))
	                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
	                .setNotification(Notification.newBuilder()
	                        //指定当前推送的android通知
	                        .addPlatformNotification(AndroidNotification.newBuilder()

	                                .setAlert(msg_content)
	                                .setTitle(notification_title)
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                .addExtra(extraKey,extrasparam)

	                                .build())
	                        //指定当前推送的iOS通知
	                        .addPlatformNotification(IosNotification.newBuilder()
	                                //传一个IosAlert对象，指定apns title、title、subtitle等
	                                .setAlert(msg_content)
	                                //直接传alert
	                                //此项是指定此推送的badge自动加1
	                                .incrBadge(1)
	                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
	                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
	                                .setSound("default")
	                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
	                                .addExtra(extraKey,extrasparam)
	                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
	                                //取消此注释，消息推送时ios将无法在锁屏情况接收
	                                // .setContentAvailable(true)

	                                .build())


	                        .build())
	                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
	                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
	                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
	                /*.setMessage(Message.newBuilder()

	                        .setMsgContent(msg_content)

	                        .setTitle(msg_title)

	                        .addExtra("message extras key",extrasparam)

	                        .build())*/

	                .setOptions(Options.newBuilder()
	                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
	                        .setApnsProduction(ApnsProduction)
	                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
	                        .setSendno(1)
	                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
	                        .setTimeToLive(86400)

	                        .build())

	                .build();

	    }
	    
	    
    public static void main(String[] args) {
    	Map<String, String> params = new HashMap<String, String>();
    	//map里面可以放一些你需要展示的或者你需要传递的数据
    	params.put("id", "28");
    	params.put("Atitle", "Atitle");
    	params.put("msg", "1");
    	
    	//推送所有安卓
    	//Jipush.jpushAndroid(params);
    	
    	
    	//通过别名推送
    	//Jipush.sendByAlias("标题？？", params, "28");
    	
    	
    	List<String> tags=new ArrayList<>();
    	tags.add("28");
    	tags.add("29");
    	//安卓+别名集合
    	//Jipush.sendByAliasList("别名集合", params, tags);
    	
    	//Jipush.sendToTagList(tags, "消息内容", "扩展信息", "string", "标题");
    	
    	
    	
	}


	
}