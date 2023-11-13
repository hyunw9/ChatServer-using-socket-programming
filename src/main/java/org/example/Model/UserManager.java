/*
package org.example.Model;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

  private final Map<SocketChannel, String> userMap;

  public UserManager(Map<SocketChannel, String> map) {
    this.userMap = new HashMap<>();
  }

  public static UserManager createUserManager(SocketChannel socketChannel, String name){
    Map<SocketChannel,String> map = new HashMap<>();
    map.put(socketChannel,name);
    return new UserManager(map);
  }

  public void setUserName(SocketChannel clientSocket,String name){
    if(userMap.containsKey(clientSocket)){
      userMap.put(clientSocket,name);
    }else{
      System.out.println("올바르지 않은 유저");
    }
  }

  public void addUser(SocketChannel user,String userName) {
    this.userMap.put(user,userName);
  }

  public void removeUser(SocketChannel user) {
    this.userMap.remove(user);
  }

  public Map<SocketChannel, String> getUserMap() {
    return userMap;
  }
  public String getUserMapId(String id){
    return userMap.get(id);
  }
}
*/
