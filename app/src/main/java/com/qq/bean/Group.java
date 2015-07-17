package com.qq.bean;

import java.util.List;

public class Group {
	private String groupName;
	private List<Child> childList;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<Child> getChildList() {
		return childList;
	}
	public void setChildList(List<Child> childList) {
		this.childList = childList;
	}
	
}
