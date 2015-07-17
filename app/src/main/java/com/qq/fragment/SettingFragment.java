package com.qq.fragment;

import org.jivesoftware.smack.XMPPConnection;

import com.qq.QQApplication;
import com.qq.R;
import com.qq.activity.LoginActivity;
import com.qq.activity.MsgAlertActivity;
import com.qq.activity.MsgHistroyActivity;
import com.qq.activity.SignActivity;
import com.qq.service.MsfService;
import com.qq.util.PreferencesUtils;
import com.qq.util.ToastUtil;
import com.qq.util.XmppUtil;
import com.qq.view.TitleBarView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingFragment extends Fragment implements OnClickListener{

	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private TextView name,tv_sign;
	private RelativeLayout online_status,rl_sign,rl_msg,rl_histroy,login_out;
	private ImageView iv_stutas;//在线状态
	private TextView tv_stutas;
	
	String[] items;
	
	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private TextView loginout;
	private LinearLayout cancel;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_mine, null);
		layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items=  new String[]{"在线","Q我吧","隐身","忙碌","离开"};
		init();
		return mBaseView;
	}
	
	private void init(){
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.mime);
		
		name=(TextView) mBaseView.findViewById(R.id.name);
		name.setText(PreferencesUtils.getSharePreStr(mContext, "username"));
		tv_sign=(TextView) mBaseView.findViewById(R.id.tv_sign);
		online_status=(RelativeLayout) mBaseView.findViewById(R.id.online_status);//在线状态
		rl_sign=(RelativeLayout) mBaseView.findViewById(R.id.rl_sign);//签名
		rl_msg=(RelativeLayout) mBaseView.findViewById(R.id.rl_msg);//消息通知
		rl_histroy=(RelativeLayout) mBaseView.findViewById(R.id.rl_histroy);//消息记录
		login_out=(RelativeLayout) mBaseView.findViewById(R.id.login_out);//退出
		
		iv_stutas=(ImageView) mBaseView.findViewById(R.id.iv_stutas);
		tv_stutas=(TextView) mBaseView.findViewById(R.id.tv_stutas);
		XmppUtil.setOnlineStatus(iv_stutas,PreferencesUtils.getSharePreInt(mContext, "online_status"),tv_stutas,items);
		
		online_status.setOnClickListener(this);
		rl_sign.setOnClickListener(this);
		rl_msg.setOnClickListener(this);
		rl_histroy.setOnClickListener(this);
		login_out.setOnClickListener(this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		tv_sign.setText(PreferencesUtils.getSharePreStr(mContext, "sign"));
	}

	@Override
	public void onClick(View arg0) {
		Intent intent=null;
		switch (arg0.getId()) {
		case R.id.online_status:
			Builder bd = new AlertDialog.Builder(mContext);
			bd.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					XMPPConnection mXMPPConnection=QQApplication.xmppConnection;
					if(mXMPPConnection==null){
						ToastUtil.showShortToast(mContext, "设置失败，请检查您的网络连接");
						return;
					}
					XmppUtil.setPresence(mContext,mXMPPConnection, arg1);
					XmppUtil.setOnlineStatus(iv_stutas,arg1,tv_stutas,items);
					PreferencesUtils.putSharePre(mContext, "online_status", arg1);//将状态保存
				}
			});
			bd.create().show();
			break;
		case R.id.rl_sign:
			intent=new Intent(mContext, SignActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_msg:
			intent=new Intent(mContext, MsgAlertActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_histroy:
			intent=new Intent(mContext, MsgHistroyActivity.class);
			startActivity(intent);
			break;
		case R.id.login_out:
			showPopupWindow(login_out);
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent){
		if (popWindow == null) {		
			View view = layoutInflater.inflate(R.layout.pop_loginout,null);
			popWindow = new PopupWindow(view,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
			initPop(view);
		}	
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());	
		popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
	public void initPop(View view){
		loginout = (TextView) view.findViewById(R.id.loginout);//退出
		cancel= (LinearLayout) view.findViewById(R.id.cancel);//取消
		loginout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try{
					MsfService.getInstance().stopSelf();
				}catch (Exception e) {
					
				}
				Intent intent=new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
				getActivity().finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
			}
		});
	}
	
}
