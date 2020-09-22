package com.thinking.machines.chat.beans;
import java.util.*;

public class UsersDetailMapWrapper
{
 private Map<String,UserDetailBean> userDetail;

 public void setUserDetail(Map<String,UserDetailBean> userDetail)
 {
  this.userDetail=userDetail;
 }
 public Map<String,UserDetailBean> getUserDetail()
 {
  return this.userDetail;
 }

}