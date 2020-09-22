package com.thinking.machines.chat.beans;
import java.util.*;

public class UsersMapWrapper
{
 private Map<String,UserBean> users;

 public void setUsers(Map<String,UserBean> users)
 {
  this.users=users;
 }
 public Map<String,UserBean> getUsers()
 {
  return this.users;
 }

}