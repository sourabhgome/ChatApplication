package com.thinking.machines.chat.beans;
import java.util.*;
public class UserBean
{
 private String email;
 private String password;
 private String userName;
 private Map<String,List<MessageBean>> chatData;//RecieverName,MessageList
 public UserBean()
 {
 }
 
 
 public void setEmail(String email)
 {
  this.email=email;
 }
 public String getEmail()
 {
  return this.email;
 }

 public void setPassword(String password)
 {
  this.password=password;
 }
 public String getPassword()
 {
  return this.password;
 }

 public void setUserName(String userName)
 {
  this.userName=userName;
 }
 public String getUserName()
 {
  return this.userName;
 }

 public void setChatData(Map<String,List<MessageBean>> chatData)
 {
  this.chatData=chatData;
 }
 public Map<String,List<MessageBean>> getChatData()
 {
  return this.chatData;
 }

}