package com.qq.activity;

import com.qq.R;
import com.qq.util.Const;
import com.qq.util.PreferencesUtils;

import android.app.Activity;
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
public class MsgAlertActivity extends Activity implements OnClickListener{
	private ImageView go_back;
	private RelativeLayout rl_msg_voice,rl_msg_vibrate;
    private CheckBox cb_msg_voice,cb_msg_vibrate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_msg_alert);
		initView();
	}


	/**
	 * 
	 */
	private void initView() {
		go_back = (ImageView) findViewById(R.id.img_back);//返回
		
	    rl_msg_voice=(RelativeLayout) findViewById(R.id.rl_msg_voice);//声音
	    rl_msg_vibrate=(RelativeLayout) findViewById(R.id.rl_msg_vibrate);//振动
		
	    cb_msg_voice=(CheckBox) findViewById(R.id.cb_msg_voice);
	    cb_msg_vibrate=(CheckBox) findViewById(R.id.cb_msg_vibrate);
	    
	    if(PreferencesUtils.getSharePreBoolean(this, Const.MSG_IS_VOICE)){//声音
	    	cb_msg_voice.setChecked(true);
	    }
	    if(PreferencesUtils.getSharePreBoolean(this, Const.MSG_IS_VIBRATE)){//振动.
	    	cb_msg_vibrate.setChecked(true);
	    }
	    
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
			case R.id.rl_msg_voice://声音
				if(cb_msg_voice.isChecked()){
					cb_msg_voice.setChecked(false);
					PreferencesUtils.putSharePre(this, Const.MSG_IS_VOICE, false);
				}else{
					cb_msg_voice.setChecked(true);
					PreferencesUtils.putSharePre(this, Const.MSG_IS_VOICE, true);
				}
				break;
			case R.id.rl_msg_vibrate://振动
				if(cb_msg_vibrate.isChecked()){
					cb_msg_vibrate.setChecked(false);
					PreferencesUtils.putSharePre(this, Const.MSG_IS_VIBRATE, false);
				}else{
					cb_msg_vibrate.setChecked(true);
					PreferencesUtils.putSharePre(this, Const.MSG_IS_VIBRATE, true);
				}
				break;
		}
	}
	

}
