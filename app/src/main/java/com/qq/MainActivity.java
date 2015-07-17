package com.qq;

import com.qq.activity.LoginActivity;
import com.qq.db.ChatMsgDao;
import com.qq.fragment.ConstactFragment;
import com.qq.fragment.NewsFragment;
import com.qq.fragment.SettingFragment;
import com.qq.service.MsfService;
import com.qq.util.Const;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends FragmentActivity implements OnClickListener{

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private ImageButton mNews,mConstact,mSetting;
	private View mPopView;
	private View currentButton;
	
	private TextView tv_newmsg;
	private TextView app_cancle;
	private TextView app_exit;
	private TextView app_change;
	
	private PopupWindow mPopupWindow;
	
	NewsFragment newsFatherFragment;
	ConstactFragment constactFatherFragment;
	SettingFragment settingFragment;
	
	boolean isStartService=false;
	private ChatMsgDao chatMsgDao;
	private int msgCount;
	
	private NewMsgReciver newMsgReciver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext=this;
		chatMsgDao=new ChatMsgDao(mContext);
		newMsgReciver=new NewMsgReciver();
		IntentFilter intf=new IntentFilter();
		intf.addAction(Const.ACTION_NEW_MSG);
		registerReceiver(newMsgReciver, intf);
		isStartService=getIntent().getBooleanExtra("isStartService", false);
		if(isStartService){
			Intent intent=new Intent(mContext, MsfService.class);
			startService(intent);
		}
		newsFatherFragment=(NewsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_news);//消息
		constactFatherFragment=(ConstactFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_constact);//联系人
		settingFragment=(SettingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_setting);//我
		
		tv_newmsg=(TextView)findViewById(R.id.tv_newmsg);//消息数目提醒
		
		findView();
		init();
		initMsgCount();
	}
	
	private void initMsgCount() {
		msgCount=chatMsgDao.queryAllNotReadCount();
		if(msgCount>0){
			tv_newmsg.setText(""+msgCount);
			tv_newmsg.setVisibility(View.VISIBLE);
		}else{
			tv_newmsg.setText("");
			tv_newmsg.setVisibility(View.GONE);
		}
	}

	private void findView(){
		mPopView=LayoutInflater.from(mContext).inflate(R.layout.app_exit, null);//pop，手机菜单键弹出
		
		mNews=(ImageButton) findViewById(R.id.buttom_news);//消息
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);//联系人
		mSetting=(ImageButton) findViewById(R.id.buttom_setting);//我
		
		app_cancle=(TextView) mPopView.findViewById(R.id.app_cancle);//取消
		app_change=(TextView) mPopView.findViewById(R.id.app_change_user);//注销用户
		app_exit=(TextView) mPopView.findViewById(R.id.app_exit);//退出
		
	}
	
	private void init(){
		mNews.setOnClickListener(this);
		mConstact.setOnClickListener(this);
		mSetting.setOnClickListener(this);
		mNews.performClick();
		initPop();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initMsgCount();
	}
	
	private void initPop() {
        mPopupWindow=new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		app_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		app_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				((Activity)mContext).overridePendingTransition(R.anim.activity_up, R.anim.fade_out);
				try{
					MsfService.getInstance().stopSelf();
				}catch(Exception e){
					
				}
				finish();
			}
		});
		app_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					MsfService.getInstance().stopSelf();
				}catch(Exception e){
					
				}
				finish();
			}
		});
	}
	
	private void setButton(View v){
		if(currentButton!=null&&currentButton.getId()!=v.getId()){
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton=v;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
			moveTaskToBack(true);
			return false;
		}
		return super.onKeyDown(keyCode, event);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttom_news://消息
			getSupportFragmentManager().beginTransaction().hide(constactFatherFragment).hide(settingFragment).show(newsFatherFragment).commit();
			setButton(v);
			break;
		case R.id.buttom_constact://联系人
			getSupportFragmentManager().beginTransaction().hide(newsFatherFragment).hide(settingFragment).show(constactFatherFragment).commit();
			setButton(v);
			break;
		case R.id.buttom_setting://设置
			getSupportFragmentManager().beginTransaction().hide(constactFatherFragment).hide(newsFatherFragment).show(settingFragment).commit();
			setButton(v);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 新消息广播接收
	 * @author 白玉梁
	 */
	private class NewMsgReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			initMsgCount();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(newMsgReciver);
	}

}
