package com.qq.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;

import com.qq.QQApplication;
import com.qq.R;
import com.qq.adapter.AddFriendAdapter;
import com.qq.bean.Session;
import com.qq.service.MsfService;
import com.qq.util.Const;
import com.qq.util.PreferencesUtils;
import com.qq.util.ToastUtil;
import com.qq.util.XmppUtil;
import com.qq.view.LoadingDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


/**
 * 
 * @author 白玉梁
 */
public class SignActivity extends Activity implements OnClickListener{
	
	private ImageView go_back;
	private Button btn_ok;
	private EditText sign_content;
	private LoadingDialog loadingDialog;
	
	
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(loadingDialog.isShowing()){
				loadingDialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				ToastUtil.showShortToast(SignActivity.this, "设置签名成功");
				finish();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		loadingDialog=new LoadingDialog(this);
		loadingDialog.setTitle("请稍后...");
		initView();
	}



	/**
	 * 初始化控件
	 */
	private void initView() {
		go_back = (ImageView) findViewById(R.id.img_back);//返回
		btn_ok=(Button) findViewById(R.id.btn_ok);
		sign_content=(EditText) findViewById(R.id.sign_content);
		
		go_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {//返回
		case R.id.img_back:
			this.finish();
			break;
		case R.id.btn_ok:
			if(QQApplication.xmppConnection==null){
				ToastUtil.showLongToast(SignActivity.this, "请检查您的网络");
				return;
			}
			if(TextUtils.isEmpty(sign_content.getText().toString().trim())){
				return;
			}
			loadingDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					int code=PreferencesUtils.getSharePreInt(SignActivity.this, "online_status");
					try{
						XmppUtil.changeSign(QQApplication.xmppConnection, code, sign_content.getText().toString());
						PreferencesUtils.putSharePre(SignActivity.this, "sign", sign_content.getText().toString());//保存个性签名
					}catch(Exception e){
						ToastUtil.showLongToast(SignActivity.this, "设置签名失败："+e.getMessage());
					}
					mHandler.sendEmptyMessage(1);
				}
			}).start();
			break;
		}
	}
	
	
}
