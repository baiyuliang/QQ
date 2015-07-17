package com.qq.db;


import java.util.ArrayList;
import java.util.List;

import com.qq.bean.Session;
import com.qq.util.Const;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 聊天回话列表的管理
 * 
 * @author Administrator
 * 
 */
public class SessionDao {
	private SQLiteDatabase db;

	public SessionDao(Context context) {
		db = DBHelper.getInstance(context).getWritableDatabase();
	}

	public SessionDao(Context context, int version) {
		db = DBHelper.getInstance(context).getWritableDatabase();
	}

	// 判断是否包含
	public boolean isContent(String belong, String userid) {
		
		Cursor cursor = db.query(DBcolumns.TABLE_SESSION, new String[] { "*" }, DBcolumns.SESSION_FROM + " = ? and " + DBcolumns.SESSION_TO + " = ?", new String[] { belong, userid }, null, null, null);
		boolean flag = false;
		while (cursor.moveToNext()) {
			flag = true;
		}
		cursor.close();
		return flag;
	}

	// 添加一个会话
	public long insertSession(Session session) {
		if(session.getTo().equals(session.getFrom())){
			return 0;
		}
		ContentValues values = new ContentValues();
		values.put(DBcolumns.SESSION_FROM, session.getFrom());
		values.put(DBcolumns.SESSION_TIME, session.getTime());
		values.put(DBcolumns.SESSION_CONTENT, session.getContent());
		values.put(DBcolumns.SESSION_TO, session.getTo());
		values.put(DBcolumns.SESSION_TYPE, session.getType());
		values.put(DBcolumns.SESSION_ISDISPOSE, session.getIsdispose());
		long row = db.insert(DBcolumns.TABLE_SESSION, null, values);
		return row;
	}

	// 返回全部列表
	public List<Session> queryAllSessions(String user_id) {
		List<Session> list = new ArrayList<Session>();
		Cursor cursor = db.query(DBcolumns.TABLE_SESSION, new String[] { "*" }, DBcolumns.SESSION_TO + " = ? order by session_time desc", new String[] { user_id }, null, null, null);
		Session session = null;
		while (cursor.moveToNext()) {
			session = new Session();
			String id = ""+cursor.getInt(cursor.getColumnIndex(DBcolumns.SESSION_id));
			String from = cursor.getString(cursor.getColumnIndex(DBcolumns.SESSION_FROM));
			String time = cursor.getString(cursor.getColumnIndex(DBcolumns.SESSION_TIME));
			String content = cursor.getString(cursor.getColumnIndex(DBcolumns.SESSION_CONTENT));
			String type = cursor.getString(cursor.getColumnIndex(DBcolumns.SESSION_TYPE));
			String to = cursor.getString(cursor.getColumnIndex(DBcolumns.SESSION_TO));
			String isdispose = cursor.getString(cursor.getColumnIndex(DBcolumns.SESSION_ISDISPOSE));
			int unreadCount = 0;
			Cursor countcursor = db.rawQuery("select count(*) from " + DBcolumns.TABLE_MSG + " where " + DBcolumns.MSG_FROM + " = ? and " + DBcolumns.MSG_ISREADED + " = 0" + " AND " + DBcolumns.MSG_TO + " = ?", new String[] { from,  user_id });
			if (countcursor.moveToFirst()) {
				unreadCount = countcursor.getInt(0);
			}
			countcursor.close();
			session.setId(id);
			session.setNotReadCount("" + unreadCount);
			session.setFrom(from);
			session.setTime( time);
			session.setContent(content);
			session.setTo(to);
			session.setType(type);
			session.setIsdispose(isdispose);
			list.add(session);
		}
		return list;
	}

	// 修改一个回话
	public long updateSession(Session session) {
		ContentValues values = new ContentValues();
		values.put(DBcolumns.SESSION_TYPE, session.getType());
		values.put(DBcolumns.SESSION_TIME, session.getTime());
		values.put(DBcolumns.SESSION_CONTENT, session.getContent());
		long row = db.update(DBcolumns.TABLE_SESSION, values, DBcolumns.SESSION_FROM + " = ? and " + DBcolumns.SESSION_TO + " = ?", new String[] { session.getFrom(),session.getTo() });
		return row;
	}
	
	public void updateSessionToDisPose(String sessionid){
		ContentValues values = new ContentValues();
		values.put(DBcolumns.SESSION_ISDISPOSE, "1");
		db.update(DBcolumns.TABLE_SESSION, values, DBcolumns.SESSION_id + " = ? ", new String[] { sessionid });
	}
	

	// 删除一个回话
	public long deleteSession(Session session) {
		long row = db.delete(DBcolumns.TABLE_SESSION, DBcolumns.SESSION_FROM+"=? and "+DBcolumns.SESSION_TO+"=?", new String[] { session.getFrom(),session.getTo() });
		return row;
	}
	
	public void deleteTableData() {
		db.delete(DBcolumns.TABLE_SESSION, null, null);
	}

}
