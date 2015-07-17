package com.qq.adapter;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import com.qq.R;
import com.qq.bean.Session;
import com.qq.util.Const;
import com.qq.util.ExpressionUtil;
import com.qq.util.ImgUtil;
import com.qq.util.ImgUtil.OnLoadBitmapListener;
import com.qq.util.SystemMethod;
import com.qq.view.CircleImageView;
import com.qq.view.CustomListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SessionAdapter extends BaseAdapter {
	private Context mContext;
	private List<Session> lists;

	public SessionAdapter(Context context, List<Session> lists) {
		this.mContext = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_news_item,null);
			holder = new Holder();
			holder.iv = (CircleImageView) convertView.findViewById(R.id.user_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
			holder.tv_tips = (TextView) convertView.findViewById(R.id.tips);
			holder.tv_content = (TextView) convertView.findViewById(R.id.content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_newmsg= (TextView) convertView.findViewById(R.id.tv_newmsg);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		Session session = lists.get(position);
		if(session.getType().equals(Const.MSG_TYPE_ADD_FRIEND)){
			holder.tv_tips .setVisibility(View.VISIBLE);
			holder.iv.setImageResource(R.drawable.ibl);
		}else{
			holder.tv_tips .setVisibility(View.GONE);
			holder.iv.setImageResource(R.drawable.ic_launcher);
		}

		holder.tv_name.setText(session.getFrom());
		holder.tv_content.setText(ExpressionUtil.prase(mContext, holder.tv_content, session.getContent()==null?"":session.getContent()));
		holder.tv_time.setText(session.getTime());
		if(!TextUtils.isEmpty(session.getNotReadCount())&&Integer.parseInt(session.getNotReadCount())>0){
			holder.tv_newmsg.setVisibility(View.VISIBLE);
			holder.tv_newmsg.setText(session.getNotReadCount());
		}else{
			holder.tv_newmsg.setVisibility(View.GONE);
			holder.tv_newmsg.setText("");
		}
		return convertView;
	}

	class Holder {
		CircleImageView iv;
		TextView tv_name,tv_tips;
		TextView tv_content;
		TextView tv_time,tv_newmsg;
	}

}
