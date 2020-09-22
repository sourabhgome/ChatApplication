package com.thinking.machines.chat.beans;
import java.util.*;

public class UserListBean
{
 private List<String> userList;

 public void setUserList(List<String> userList)
 {
  this.userList=userList;
 }
 public List<String> getUserList()
 {
  return this.userList;
 }

}