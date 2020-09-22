package com.thinking.machines.chat.beans;
import javax.websocket.*;

public class UserSessionBean implements Comparable<UserSessionBean> 
{
 private String userName;
 private boolean isOnline;
 private Session session;

 public void setUserName(String userName)
 {
  this.userName=userName;
 }
 public String getUserName()
 {
  return this.userName;
 }

 public void setIsOnline(boolean isOnline)
 {
  this.isOnline=isOnline;
 }
 public boolean getIsOnline()
 {
  return this.isOnline;
 }

 public void setSession(Session session)
 {
  this.session=session;
 }
 public Session getSession()
 {
  return this.session;
 }

 public int compareTo(UserSessionBean userSession)
 {
  int a=0;
  int b=0;
  if(this.isOnline) a=1;
  if(userSession.isOnline) b=1;
  if(a<b) return 1;
  else if(a>b) return -1;
  return 0;
 }

}