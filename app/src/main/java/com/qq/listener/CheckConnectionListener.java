package com.qq.listener;

import org.jivesoftware.smack.ConnectionListener;

import com.qq.service.MsfService;
import com.qq.util.ToastUtil;


/**
 * @author baiyuliang
 */
public class CheckConnectionListener implements ConnectionListener{
	
	private MsfService context;
	
	public CheckConnectionListener(MsfService context){
		this.context=context;
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectionClosedOnError(Exception e) {  
		if (e.getMessage().equals("stream:error (conflict)")) {
			ToastUtil.showLongToast(context, "您的账号在异地登录");
		}
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		
	}

}
