package com.qq.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import com.qq.R;
import com.qq.bean.Session;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class XmppUtil {
	
	/** 
     * 注册 
     *  
     * @param account 
     *            注册帐号 
     * @param password 
     *            注册密码 
     * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败 
     */  
    public static int register(XMPPConnection mXMPPConnection,String account, String password) {  
        Registration reg = new Registration();  
        reg.setType(IQ.Type.SET);  
        reg.setTo(mXMPPConnection.getServiceName());  
        // 注意这里createAccount注册时，参数是UserName，不是jid，是"@"前面的部分。  
        reg.setUsername(account);  
        reg.setPassword(password);  
        // 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！  
        reg.addAttribute("android", "geolo_createUser_android");  
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));  
        PacketCollector collector =mXMPPConnection.createPacketCollector(filter);  
        mXMPPConnection.sendPacket(reg);  
        IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());  
        // Stop queuing results停止请求results（是否成功的结果）  
        collector.cancel();  
        if (result == null) {  
            return 0;  
        } else if (result.getType() == IQ.Type.RESULT) {  
            return 1;  
        } else {  
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {  
                return 2;  
            } else {  
                return 3;  
            }  
        }  
    }  
    
    /** 
     * 查询用户 
     *  
     * @param userName 
     * @return 
     * @throws XMPPException 
     */  
    public static List<Session> searchUsers(XMPPConnection mXMPPConnection,String userName) {  
    	List<Session> listUser=new ArrayList<Session>();
        try{
			UserSearchManager search = new UserSearchManager(mXMPPConnection);
			//此处一定要加上 search.
			Form searchForm = search.getSearchForm("search."+mXMPPConnection.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			ReportedData data = search.getSearchResults(answerForm,"search."+mXMPPConnection.getServiceName());					
			Iterator<Row> it = data.getRows();
			Row row=null;
			while(it.hasNext()){
				row=it.next();
				Session session=new Session();
				session.setFrom(row.getValues("Username").next().toString());
				listUser.add(session);
			}
		}catch(Exception e){
			
		}
        return listUser;  
    }  
    
	 /** 
     * 更改用户状态 
     */  
    public static void setPresence(Context context,XMPPConnection con,int code) {  
        if (con == null)  
            return;  
        Presence presence=null;  
        switch (code) {  
        case 0:  
            presence = new Presence(Presence.Type.available);  //在线
            break;  
        case 1:  
            presence = new Presence(Presence.Type.available);  //设置Q我吧
            presence.setMode(Presence.Mode.chat);  
            break;  
        case 2:                                                                                      //隐身
            Roster roster = con.getRoster();  
            Collection<RosterEntry> entries = roster.getEntries();  
            for (RosterEntry entry : entries) {  
                presence = new Presence(Presence.Type.unavailable);  
                presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
                presence.setFrom(con.getUser());  
                presence.setTo(entry.getUser());  
            }  
            // 向同一用户的其他客户端发送隐身状态  
            presence = new Presence(Presence.Type.unavailable);  
            presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
            presence.setFrom(con.getUser());  
            presence.setTo(StringUtils.parseBareAddress(con.getUser()));  
            break;  
        case 3:  
            presence = new Presence(Presence.Type.available);  //设置忙碌
            presence.setMode(Presence.Mode.dnd);  
            break;  
        case 4:  
            presence = new Presence(Presence.Type.available);  //设置离开
            presence.setMode(Presence.Mode.away);  
            break;  
        case 5:  
            presence = new Presence(Presence.Type.unavailable);  //离线
            break;  
        default:  
            break;  
        }  
        if(presence!=null){
        	presence.setStatus(PreferencesUtils.getSharePreStr(context, "sign"));
        	con.sendPacket(presence);  
        }
    }  
	/** 
     * 删除当前用户 
     * @param connection 
     * @return 
     */  
    public static boolean deleteAccount(XMPPConnection connection)  
    {  
        try {  
            connection.getAccountManager().deleteAccount();         
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
    }  
	/**
	 * 返回所有组信息 <RosterGroup>
	 * @return List(RosterGroup)
	 */
	public static List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();
		while (i.hasNext())
			groupsList.add(i.next());
		return groupsList;
	}

	/**
	 * 返回相应(groupName)组里的所有用户<RosterEntry>
	 * @return List(RosterEntry)
	 */
	public static List<RosterEntry> getEntriesByGroup(Roster roster,
			String groupName) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = roster.getGroup(groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}

	/**
	 * 返回所有用户信息 <RosterEntry>
	 * @return List(RosterEntry)
	 */
	public static List<RosterEntry> getAllEntries(Roster roster) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		Collection<RosterEntry> rosterEntry = roster.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext()){
			RosterEntry rosterentry=  (RosterEntry) i.next();
			Log.e("jj", "好友："+rosterentry.getUser()+","+rosterentry.getName()+","+rosterentry.getType().name());
			EntriesList.add(rosterentry);
		}
		return EntriesList;
	}
	
	
	/** 
     * 创建一个组 
     */ 
	public static boolean addGroup(Roster roster,String groupName)  
    {  
        try {  
            roster.createGroup(groupName);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            Log.e("jj", "创建分组异常："+e.getMessage());
            return false;  
        }  
    }  
      
    /** 
     * 删除一个组 
     */  
    public static boolean removeGroup(Roster roster,String groupName)  
    {  
        return false;  
    }
    
    /**
	 * 添加一个好友  无分组
	 */
	public static boolean addUser(Roster roster,String userName,String name)
	{
		try {
			roster.createEntry(userName, name, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * 添加一个好友到分组
	 * @param roster
	 * @param userName
	 * @param name
	 * @return
	 */
	public static boolean addUsers(Roster roster,String userName,String name,String groupName)
	{
		try {
			roster.createEntry(userName, name,new String[]{ groupName});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("jj", "添加好友异常："+e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * 删除一个好友
	 * @param roster
	 * @param userJid
	 * @return
	 */
	public static boolean removeUser(Roster roster,String userJid)
	{
		try {
			RosterEntry entry = roster.getEntry(userJid);
			roster.removeEntry(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
     * 把一个好友添加到一个组中
     * @param userJid
     * @param groupName
     */
    public static void addUserToGroup(final String userJid, final String groupName,
            final XMPPConnection connection) {
            	RosterGroup group = connection.getRoster().getGroup(groupName);
                // 这个组已经存在就添加到这个组，不存在创建一个组
                RosterEntry entry = connection.getRoster().getEntry(userJid);
                try {
                    if (group != null) {
                        if (entry != null)
                            group.addEntry(entry);
                    } else {
                        RosterGroup newGroup = connection.getRoster().createGroup("我的好友");
                        if (entry != null)
                            newGroup.addEntry(entry);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    /**
     * 把一个好友从组中删除
     * @param userJid
     * @param groupName
     */
    public static void removeUserFromGroup(final String userJid,final String groupName, final XMPPConnection connection) {
            RosterGroup group = connection.getRoster().getGroup(groupName);
            if (group != null) {
                try {
                	RosterEntry entry = connection.getRoster().getEntry(userJid);
                    if (entry != null)
                        group.removeEntry(entry);
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
     }
    
    /** 
     * 修改签名
     */  
    public static void changeSign(XMPPConnection connection,int code , String content){  
        Presence presence = getOnlineStatus(code);  
        presence.setStatus(content);  
        connection.sendPacket(presence);      
    }  
    
    
    /**
	 * 发送消息
	 * @param position
	 * @param content
	 * @param touser
	 * @throws XMPPException
	 */
	public static void sendMessage(XMPPConnection mXMPPConnection,String content,String touser) throws XMPPException {
		if(mXMPPConnection==null||!mXMPPConnection.isConnected()){
			throw new XMPPException();
		}
		ChatManager chatmanager = mXMPPConnection.getChatManager();
		Chat chat =chatmanager.createChat(touser + "@" + Const.XMPP_HOST, null);
		if (chat != null) {
			chat.sendMessage(content);
			Log.e("jj", "发送成功");
		}
	}
	
	public static void setOnlineStatus(ImageView iv_stutas,int code,TextView tv_stutas,String[] items ){
		switch (code) {
		case 0://在线
			iv_stutas.setImageResource(R.drawable.evk);
			tv_stutas.setText(items[0]);
			break;
		case 1://q我吧
			iv_stutas.setImageResource(R.drawable.evm);
			tv_stutas.setText(items[1]);
			break;
		case 2://隐身
			iv_stutas.setImageResource(R.drawable.evf);
			tv_stutas.setText(items[2]);
			break;
		case 3://忙碌
			iv_stutas.setImageResource(R.drawable.evd);
			tv_stutas.setText(items[3]);
			break;
		case 4://离开
			iv_stutas.setImageResource(R.drawable.evp);
			tv_stutas.setText(items[4]);
			break;
		default:
			break;
		}
		
	}
	
	public static Presence getOnlineStatus(int code){
		Presence presence=null;
		  switch (code) {  
	        case 0:  
	            presence = new Presence(Presence.Type.available);  //在线
	            break;  
	        case 1:  
	            presence = new Presence(Presence.Type.available);  //设置Q我吧
	            presence.setMode(Presence.Mode.chat);  
	            break;  
	        case 2:                                                                                      //隐身
	            presence = new Presence(Presence.Type.unavailable);  
	            break;  
	        case 3:  
	            presence = new Presence(Presence.Type.available);  //设置忙碌
	            presence.setMode(Presence.Mode.dnd);  
	            break;  
	        case 4:  
	            presence = new Presence(Presence.Type.available);  //设置离开
	            presence.setMode(Presence.Mode.away);  
	            break;  
	        case 5:  
	            presence = new Presence(Presence.Type.unavailable);  //离线
	            break;  
	        default:  
	            break;  
	        }  
		return presence;
	}
    
}
