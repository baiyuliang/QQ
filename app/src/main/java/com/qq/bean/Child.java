package com.qq.bean;

import java.io.Serializable;


import android.text.TextUtils;


@SuppressWarnings("serial")
public class Child implements Serializable{
	private String username;
	private String mood;
	private String online_status;//在线状态
	
	
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
}
	public String getOnline_status() {
		return online_status;
	}
	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}
	
}