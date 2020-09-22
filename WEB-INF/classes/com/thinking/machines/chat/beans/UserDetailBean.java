package com.thinking.machines.chat.beans;
import java.util.*;
public class UserDetailBean
{
 private String email;
 private String password;
 private String userName;
 public UserDetailBean()
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

}