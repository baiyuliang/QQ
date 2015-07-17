package com.qq.bean;

public class Notice {
	
	private String noticeid;
	private String noticetype;
	private String noticefrom;
	private String noticefrom_head;
	private String noticecontent;
	private String isDispose;//是否已处理 0未处理，1已处理
	
	
	public String getNoticeid() {
		return noticeid;
	}
	public String getNoticetype() {
		return noticetype;
	}
	public String getNoticefrom() {
		return noticefrom;
	}
	public String getNoticefrom_head() {
		return noticefrom_head;
	}
	public String getNoticecontent() {
		return noticecontent;
	}
	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}
	public void setNoticetype(String noticetype) {
		this.noticetype = noticetype;
	}
	public void setNoticefrom(String noticefrom) {
		this.noticefrom = noticefrom;
	}
	public void setNoticefrom_head(String noticefrom_head) {
		this.noticefrom_head = noticefrom_head;
	}
	public void setNoticecontent(String noticecontent) {
		this.noticecontent = noticecontent;
	}
	public String getIsDispose() {
		return isDispose;
	}
	public void setIsDispose(String isDispose) {
		this.isDispose = isDispose;
	}

}
