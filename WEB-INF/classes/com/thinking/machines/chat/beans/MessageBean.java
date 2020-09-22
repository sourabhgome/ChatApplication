package com.thinking.machines.chat.beans;
import java.util.*;
public class MessageBean
{
 private String senderName;
 private String receiverName;
 private String message;
 private Date date;
 public MessageBean()
 {
 }
 public MessageBean(String senderName,String receiverName,String message,Date date)
 {
  this.senderName=senderName;
  this.receiverName=receiverName;
  this.message=message;
  this.date=date;
 }
 
 public void setSenderName(String senderName)
 {
  this.senderName=senderName;
 }
 public String getSenderName()
 {
  return this.senderName;
 }

 public void setReceiverName(String receiverName)
 {
  this.receiverName=receiverName;
 }
 public String getReceiverName()
 {
  return this.receiverName;
 }

 public void setMessage(String message)
 {
  this.message=message;
 }
 public String getMessage()
 {
  return this.message;
 }

 public void setDate(Date date)
 {
  this.date=date;
 }
 public Date getDate()
 {
  return this.date;
 }

}