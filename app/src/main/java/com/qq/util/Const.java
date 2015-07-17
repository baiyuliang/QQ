package com.qq.util;

public class Const {
	
	public static final String XMPP_HOST = "192.168.8.229";
	public static final int XMPP_PORT = 5222;
	
	/**
	 * 登录状态广播
	 */
	public static final String ACTION_IS_LOGIN_SUCCESS = "com.android.qq.is_login_success";
	/**
	 * 消息记录操作广播
	 */
	public static final String ACTION_MSG_OPER= "com.android.qq.msgoper";
	/**
	 * 添加好友请求广播
	 */
	public static final String ACTION_ADDFRIEND= "com.android.qq.addfriend";
	/**
	 * 新消息广播
	 */
	public static final String ACTION_NEW_MSG= "com.android.qq.newmsg";
	/**
	 *好友在线状态更新广播
	 */
	public static final String ACTION_FRIENDS_ONLINE_STATUS_CHANGE= "com.android.qq.friends_online_status_change";
	
	//静态地图API
	public static  final String LOCATION_URL_S = "http://api.map.baidu.com/staticimage?width=320&height=240&zoom=17&center=";
	public static  final String LOCATION_URL_L = "http://api.map.baidu.com/staticimage?width=480&height=800&zoom=17&center=";
	
	public static final String MSG_TYPE_TEXT="msg_type_text";//文本消息
	public static final String MSG_TYPE_IMG="msg_type_img";//图片
	public static final String MSG_TYPE_VOICE="msg_type_voice";//语音
	public static final String MSG_TYPE_LOCATION="msg_type_location";//位置
	
	public static final String MSG_TYPE_ADD_FRIEND="msg_type_add_friend";//添加好友
	public static final String MSG_TYPE_ADD_FRIEND_SUCCESS="msg_type_add_friend_success";//同意添加好友
	
	public static final String SPLIT="卍";
	
	public static final int NOTIFY_ID=0x90;
	
	/**
	 * 是否开启声音
	 */
	public static final String MSG_IS_VOICE = "msg_is_voice";
	/**
	 * 是否开启振动
	 */
	public static final String MSG_IS_VIBRATE = "msg_is_vibrate";

}
