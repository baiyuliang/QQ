package com.qq.activity;

import com.qq.R;
import com.qq.db.ChatMsgDao;
import com.qq.db.SessionDao;
import com.qq.util.Const;
import com.qq.util.PreferencesUtils;
import com.qq.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @author 白玉梁
 */
public class MsgHistroyActivity extends Activity implements OnClickListener{
	private ImageView go_back;
	private RelativeLayout rl_msg_voice,rl_msg_vibrate;
	private ChatMsgDao chatMsgDao;
	private SessionDao sessionDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_msg_histroy);
		chatMsgDao=new ChatMsgDao(this);
		sessionDao=new SessionDao(this);
		initView();
	}


	/**
	 * 
	 */
	private void initView() {
		go_back = (ImageView) findViewById(R.id.img_back);//返回
		
	    rl_msg_voice=(RelativeLayout) findViewById(R.id.rl_msg_voice);//清空消息列表
	    rl_msg_vibrate=(RelativeLayout) findViewById(R.id.rl_msg_vibrate);//清空所有聊天记录
	    
		go_back.setOnClickListener(this);
		rl_msg_voice.setOnClickListener(this);
		rl_msg_vibrate.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {//返回
			case R.id.img_back:
				this.finish();
				break;
			case R.id.rl_msg_voice://清空消息列表
				sessionDao.deleteTableData();
				sendBroadcast(new Intent(Const.ACTION_NEW_MSG));
				ToastUtil.showShortToast(this, "消息列表已清空");
				break;
			case R.id.rl_msg_vibrate://清空所有聊天记录
				ToastUtil.showShortToast(this, "聊天记录已清空");
				sendBroadcast(new Intent(Const.ACTION_NEW_MSG));
				chatMsgDao.deleteTableData();
				break;
		}
	}
	

}
