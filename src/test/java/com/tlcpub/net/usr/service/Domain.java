package com.tlcpub.net.usr.service;

import com.tlcpub.net.usr.userDto.User;

public class Domain {

   
   private User baseUser = new User();
   private User updateUser = new User();
   private User anotherUser = new User();
   
   
   public User getBaseUser() {
      return baseUser;
   }
   
   public void setBaseUser(User baseUser) {
      this.baseUser = baseUser;
   }
   
   public User getUpdateUser() {
      return updateUser;
   }
   
   public void setUpdateUser(User updateUser) {
      this.updateUser = updateUser;
   }
   
   public User getAnotherUser() {
      return anotherUser;
   }
   
   public void setAnotherUser(User anotherUser) {
      this.anotherUser = anotherUser;
   }
}
