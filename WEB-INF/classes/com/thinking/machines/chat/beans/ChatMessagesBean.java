package com.thinking.machines.chat.beans;
import java.util.*;

public class ChatMessagesBean
{
 private List<MessageBean> chatMessages;
 
 public void setChatMessages(List<MessageBean> chatMessages)
 {
  this.chatMessages=chatMessages;
 }
 public List<MessageBean> getChatMessages()
 {
  return this.chatMessages;
 }

}