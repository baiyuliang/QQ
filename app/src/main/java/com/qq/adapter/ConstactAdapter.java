package com.qq.adapter;

import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import com.qq.R;
import com.qq.bean.Child;
import com.qq.bean.Group;
import com.qq.view.CircleImageView;
import com.qq.view.IphoneTreeView;
import com.qq.view.IphoneTreeView.IphoneTreeHeaderAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConstactAdapter extends BaseExpandableListAdapter implements
		IphoneTreeHeaderAdapter {

	private Context mContext;
	private List<Group> groupList;
	private IphoneTreeView mIphoneTreeView;
	private HashMap<Integer, Integer> groupStatusMap;

	@SuppressLint("UseSparseArrays")
	public ConstactAdapter(Context context, List<Group> groupList,IphoneTreeView mIphoneTreeView) {
		this.mContext = context;
		this.groupList = groupList;
		this.mIphoneTreeView = mIphoneTreeView;
		groupStatusMap = new HashMap<Integer, Integer>();
	}

	public Child getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).getChildList().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * 分组中的总人数
	 * @param groupPosition
	 * @return
	 */
	public int getChildrenCount(int groupPosition) {
		return groupList.get(groupPosition).getChildList().size();
	}
	
	/**
	 * 分组中的在线人数
	 * @param groupPosition
	 * @return
	 */
	public int getChildrenOnlineCount(int groupPosition) {
		int count=0;
		List<Child> childList=groupList.get(groupPosition).getChildList();
		for(int i=0;i<childList.size();i++){
			Child child=childList.get(i);
			if(!TextUtils.isEmpty(child.getOnline_status())&&child.getOnline_status().equals("1")){
				count++;
			}
		}
		return count;
	}

	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	public int getGroupCount() {
		return groupList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}
	

	/**
	 * Child
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_constact_child, null);
			holder = new ChildHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.contact_list_item_name);//昵称
			holder.feelView = (TextView) convertView.findViewById(R.id.cpntact_list_item_state);//心情
			holder.iconView = (CircleImageView) convertView.findViewById(R.id.icon);//头像
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		Child child=getChild(groupPosition, childPosition);
		
		if(!TextUtils.isEmpty(child.getOnline_status())&&child.getOnline_status().equals("1")){
			holder.iconView.setImageResource(R.drawable.ic_launcher);
		}else{
			holder.iconView.setImageResource(R.drawable.h001);
		}
		holder.nameView.setText(child.getUsername());
		holder.feelView.setText(child.getMood());
		return convertView;
	}

	/**
	 * Group
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		GroupHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_constact_group, null);
			holder = new GroupHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.group_name);
			holder.onLineView = (TextView) convertView.findViewById(R.id.online_count);
			holder.iconView = (ImageView) convertView.findViewById(R.id.group_indicator);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		holder.nameView.setText(groupList.get(groupPosition).getGroupName());
		holder.onLineView.setText(getChildrenOnlineCount(groupPosition) + "/"+ getChildrenCount(groupPosition));
		if (isExpanded) {
			holder.iconView.setImageResource(R.drawable.qb_down);
		} else {
			holder.iconView.setImageResource(R.drawable.qb_right);
		}
		return convertView;
	}

	@Override
	public int getTreeHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			//mSearchView.setVisibility(View.GONE);
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1&& !mIphoneTreeView.isGroupExpanded(groupPosition)) {
			//mSearchView.setVisibility(View.VISIBLE);
			return PINNED_HEADER_GONE;
		} else {
			//mSearchView.setVisibility(View.GONE);
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureTreeHeader(View header, int groupPosition,int childPosition, int alpha) {
		((TextView) header.findViewById(R.id.group_name)).setText(groupList.get(groupPosition).getGroupName());//组名
		((TextView) header.findViewById(R.id.online_count)).setText(getChildrenOnlineCount(groupPosition) + "/"+ getChildrenCount(groupPosition));//好友上线比例
	}

	@Override
	public void onHeadViewClick(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getHeadViewClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	class GroupHolder {
		TextView nameView;
		TextView onLineView;
		ImageView iconView;
	}

	class ChildHolder {
		TextView nameView;
		TextView feelView;
		CircleImageView iconView;
	}
	

}
