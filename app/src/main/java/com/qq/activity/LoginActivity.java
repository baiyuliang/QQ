package com.qq.activity;

import com.qq.MainActivity;
import com.qq.R;
import com.qq.service.MsfService;
import com.qq.util.Const;
import com.qq.util.PreferencesUtils;
import com.qq.util.ToastUtil;
import com.qq.view.LoadingDialog;
import com.qq.view.TextURLView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class LoginActivity extends Activity{

	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;
	private TextURLView mTextViewURL;
	
	private EditText account,password;
	private BroadcastReceiver receiver;
	private LoadingDialog loadDialog;
	
	String username;//用户名
	String pwd;//密码
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext=this;
		loadDialog=new LoadingDialog(this);
		loadDialog.setTitle("正在登录...");
		findView();
		initTvUrl();
		init();
		initReceiver();
	}
	
	private void initReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Const.ACTION_IS_LOGIN_SUCCESS)){
					if(loadDialog.isShowing()){
						loadDialog.dismiss();
					}
					boolean isLoginSuccess=intent.getBooleanExtra("isLoginSuccess", false);
					if(isLoginSuccess){//登录成功
						//默认开启声音和震动提醒
						PreferencesUtils.putSharePre(LoginActivity.this, Const.MSG_IS_VOICE, true);
						PreferencesUtils.putSharePre(LoginActivity.this, Const.MSG_IS_VIBRATE, true);
						Intent intent2=new Intent(mContext,MainActivity.class);
						startActivity(intent2);
						finish();
					}else{
						ToastUtil.showShortToast(mContext, "登录失败，请检您的网络是否正常以及用户名和密码是否正确");
					}
				}
			}
		};
		//注册广播接收者
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Const.ACTION_IS_LOGIN_SUCCESS);
		registerReceiver(receiver, mFilter);
	}

	private void findView(){
		rl_user=(RelativeLayout) findViewById(R.id.rl_user);
		mLogin=(Button) findViewById(R.id.login);
		register=(Button) findViewById(R.id.register);
		mTextViewURL=(TextURLView) findViewById(R.id.tv_forget_password);
		
		account=(EditText) findViewById(R.id.account);
		password=(EditText) findViewById(R.id.password);
		
	}

	private void init(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
		mLogin.setOnClickListener(loginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
	}
	
	private void initTvUrl(){
		mTextViewURL.setText(R.string.forget_password);
	}
	
	/**
	 * 登录
	 */
	private OnClickListener loginOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			doLogin();
		}
	};
	
	/**
	 * 注册
	 */
	private OnClickListener registerOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(mContext, RegisterActivity.class);
			startActivity(intent);
			
		}
	};
	
	protected void onStart() {
		super.onStart();
		String username=PreferencesUtils.getSharePreStr(this, "username");//用户名
		String pwd=PreferencesUtils.getSharePreStr(this, "pwd");//密码
		if(!TextUtils.isEmpty(username)){
			account.setText(username);
		}
		if(!TextUtils.isEmpty(pwd)){
			password.setText(pwd);
		}
	};
	
	void doLogin(){
		username=account.getText().toString();//用户名
		pwd=password.getText().toString();//密码
		if(TextUtils.isEmpty(username)){
			ToastUtil.showShortToast(mContext, "请输入您的账号");
			return;
		}
		if(TextUtils.isEmpty(pwd)){
			ToastUtil.showShortToast(mContext, "请输入您的密码");
			return;
		}
		PreferencesUtils.putSharePre(mContext, "username", username);
		PreferencesUtils.putSharePre(mContext, "pwd", pwd);
		loadDialog.show();
		//启动核心Service
		Intent intent=new Intent(this,MsfService.class);
		startService(intent);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
}
