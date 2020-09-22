package com.thinking.machines.chat.WebSockets;
import javax.websocket.*;
import javax.websocket.server.*;
import java.util.*;
import java.util.concurrent.*;
import com.thinking.machines.chat.beans.*;
import com.google.gson.*;
import java.io.*;

@ServerEndpoint("/first/{data}")
public class First
{
 private static Map<String,UserBean> users=new ConcurrentHashMap<>();
 private static Map<String,UserSessionBean> userSessions=new ConcurrentHashMap<>();
 private static Map<String,UserDetailBean> usersDetail=new ConcurrentHashMap<>();
 private UserDetailBean user=new UserDetailBean();
 private static int load=0;
 @OnOpen
 public void onOpen(Session session,@PathParam("data")String data)
 {
  try
  {
   Gson gson=new Gson();
   if(data.split("`").length==3)
   {
    this.user.setUserName(data.split("`")[1]);
   }
   else if(data.split("`").length==2)
   {
    this.user.setUserName(data.split("`")[0]);
   }
   System.out.println(this.user.getUserName());
   if(load==0)
   {
    load=1;
    File file=new File("UserDatabase.json");
    System.out.println(file.exists());
    if(file.exists())
    {
     RandomAccessFile raf=new RandomAccessFile(file,"r");
     System.out.println("Yaha Aaya");
     String response=raf.readLine();
     UsersMapWrapper usersMapWrapper=(UsersMapWrapper)gson.fromJson(response,UsersMapWrapper.class);
     raf.close();
     users=usersMapWrapper.getUsers();
    }
    file=new File("UserDetails.json");
    System.out.println(file.exists());
    if(file.exists())
    {
     RandomAccessFile raf=new RandomAccessFile(file,"r");
     String response=raf.readLine();
     UsersDetailMapWrapper usersDetailMapWrapper=(UsersDetailMapWrapper)gson.fromJson(response,UsersDetailMapWrapper.class);
     raf.close();
     usersDetail=usersDetailMapWrapper.getUserDetail();
     for(Map.Entry<String,UserDetailBean> entry : usersDetail.entrySet())
     {
      UserSessionBean userSession=new UserSessionBean();
      if(this.user.getUserName().equals(entry.getValue().getUserName()))
      {
       userSession.setUserName(this.user.getUserName());
       userSession.setSession(session);
       userSession.setIsOnline(true);
      }
      else
      {
       userSession.setUserName(entry.getValue().getUserName());
       userSession.setSession(null);
       userSession.setIsOnline(false);
      }
      System.out.println("If else k bahar aagya");
      userSessions.put(entry.getValue().getUserName(),userSession);
     }
    }
   }
   System.out.println("Data : "+data);
   String[] dataArray=data.split("`");
   for(String k:dataArray) System.out.print(k+" ");
   if(dataArray.length==3)
   {
    this.user=new UserDetailBean();
    this.user.setUserName(dataArray[1]);
    this.user.setPassword(dataArray[2]);
    this.user.setEmail(dataArray[0]);
    if(usersDetail.containsKey(this.user.getUserName()))
    {
     ErrorBean error=new ErrorBean();
     error.setError("suUsername already used");
     session.getBasicRemote().sendText(gson.toJson(error));
    }
    else
    {
     System.out.println("Open Connection... "+session.getId()+" "+this.user.getUserName());
     UserBean userBean=new UserBean();
     userBean.setUserName(this.user.getUserName());
     userBean.setChatData(new ConcurrentHashMap<>());
     users.put(this.user.getUserName(),userBean);
     UserSessionBean userSession=new UserSessionBean();
     userSession.setUserName(this.user.getUserName());
     userSession.setSession(session);
     userSession.setIsOnline(true);
     userSessions.put(this.user.getUserName(),userSession);
     usersDetail.put(this.user.getUserName(),this.user);
     sendUsersList(session);
    }
   }
   else
   {
    this.user=new UserDetailBean();
    this.user.setUserName(dataArray[0]);
    this.user.setPassword(dataArray[1]);
    if(usersDetail.containsKey(this.user.getUserName()))
    {
     if(usersDetail.get(this.user.getUserName()).getPassword().equals(this.user.getPassword()))
     {
      System.out.println("Open Connection... "+session.getId()+" "+this.user.getUserName());
      this.user.setEmail(usersDetail.get(this.user.getUserName()).getEmail());
      System.out.println("Email fetched");
      UserSessionBean userSession=userSessions.get(this.user.getUserName());
      userSession.setSession(session);
      userSession.setIsOnline(true);
      System.out.println("Session updated");
      sendUsersList(session);
     }
     else
     {
      ErrorBean error=new ErrorBean();
      error.setError("lpWrong Password!");
      session.getBasicRemote().sendText(gson.toJson(error));
     }
    } 
    else
    {
     ErrorBean error=new ErrorBean();
     error.setError("luUsername not found");
     session.getBasicRemote().sendText(gson.toJson(error));
    }
   }
  }catch(Exception e)
  {
   System.out.println(e);
  }
 }
  
 public void sendUsersList(Session session)
 {
  try
  {
   Gson gson=new Gson();
   List<UserSessionBean> ls=new ArrayList<UserSessionBean>(userSessions.values());
   Collections.sort(ls);
   UserListBean userList=new UserListBean(); 
   List<String> l=new ArrayList<>();
   for(UserSessionBean k:ls)
   {
    if(k.getIsOnline()) l.add(0,"1"+k.getUserName());
    else l.add("0"+k.getUserName());
   }
   userList.setUserList(l);
   for(Map.Entry<String,UserSessionBean> entry : userSessions.entrySet())
    if(entry.getValue().getIsOnline()) entry.getValue().getSession().getBasicRemote().sendText(gson.toJson(userList));
  }catch(Exception e){System.out.println(e);}; 
 }

 @OnClose
 public void onClose(Session session)
 {
  System.out.println("Close Connnection..."+this.user.getUserName());
  Gson gson=new Gson();
  if(userSessions.containsKey(this.user.getUserName()))
  {
   UserSessionBean userSession=userSessions.get(this.user.getUserName());
   userSession.setSession(null);
   userSession.setIsOnline(false);
  }
  sendUsersList(session);
  try{session.close();}catch(Exception e){System.out.println(e);}
 }

 @OnMessage
 public String onMessage(String message)
 {
  System.out.println("Message from the client : "+message);
  Gson gson=new Gson();
  if(message.split(":")[0].equals(this.user.getUserName())) //fetch chat data
  {
   if(users.get(this.user.getUserName()).getChatData()==null)
   {
    users.get(this.user.getUserName()).getChatData().put((message.split(":")[1]),new ArrayList<>());
    users.get(message.split(":")[1]).getChatData().put(this.user.getUserName(),new ArrayList<>());
   }
   else if(users.get(this.user.getUserName()).getChatData().containsKey(message.split(":")[1]))
   {
    ChatMessagesBean chatMessages=new ChatMessagesBean();
    chatMessages.setChatMessages(users.get(this.user.getUserName()).getChatData().get(message.split(":")[1]));
    return gson.toJson(chatMessages);
   }
   else
   {
    ChatMessagesBean chatMessages=new ChatMessagesBean();
    chatMessages.setChatMessages(new ArrayList<>());
    return gson.toJson(chatMessages);
   }
  }
  else if(message.equals("Save"))
  {
   try
   {
    File file=new File("UserDatabase.json");
    RandomAccessFile raf=new RandomAccessFile(file,"rw");
    UsersMapWrapper usersMapWrapper=new UsersMapWrapper();
    usersMapWrapper.setUsers(users);
    String json=gson.toJson(usersMapWrapper);
    raf.writeBytes(json);
    raf.close();
    file=new File("UserDetails.json");
    raf=new RandomAccessFile(file,"rw");
    UsersDetailMapWrapper usersDetailMapWrapper=new UsersDetailMapWrapper();
    usersDetailMapWrapper.setUserDetail(usersDetail);
    json=gson.toJson(usersDetailMapWrapper);
    raf.writeBytes(json);
    raf.close();
   }catch(Exception e)
   {
    System.out.println(e);
   }
   return "{}";
  }
  MessageBean messageBean=(MessageBean)gson.fromJson(message,MessageBean.class);
  if(users.get(this.user.getUserName()).getChatData().containsKey(messageBean.getReceiverName()))
  {
  }
  else
  {
   users.get(this.user.getUserName()).getChatData().put(messageBean.getReceiverName(),new ArrayList<>());
   users.get(messageBean.getReceiverName()).getChatData().put(this.user.getUserName(),new ArrayList<>());
  }
  users.get(this.user.getUserName()).getChatData().get(messageBean.getReceiverName()).add(messageBean);
  users.get(messageBean.getReceiverName()).getChatData().get(this.user.getUserName()).add(messageBean);
  try{if(userSessions.get(messageBean.getReceiverName()).getIsOnline()) userSessions.get(messageBean.getReceiverName()).getSession().getBasicRemote().sendText(gson.toJson(messageBean));}catch(Exception e){System.out.println(e);}
  return gson.toJson(messageBean);
 }

 @OnError
 public void onError(Throwable e)
 {
  e.printStackTrace();
 }
}