package org.example.Model.Manager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.Model.MessageRes.Type.MessageRes;
import org.example.Model.MessageRes.Serializer.MessageSerializer;
import org.example.Model.User;

public class UserManager {

  private final List<User> userManager;
  private final MessageSerializer serializer;

  public UserManager(List<User> userManager,MessageSerializer serializer) {
    this.userManager = userManager;
    this.serializer = serializer;
  }

  //정적 팩토리 메서드로 생성
  public static UserManager createUserManager(MessageSerializer serializer) {
    List<User> userManager = new ArrayList<>();
    return new UserManager(userManager,serializer);
  }

  public void setUserName(SocketChannel clientSocket, String name) {
    for (User user : userManager) {
      if (user.getSocketChannel().equals(clientSocket) ) {
        user.setName(name);
        System.out.println("이름 변경 완료: "+name);
      }
    }
  }

  public Optional<User> findUser(SocketChannel clientSocket){
    for (User user : userManager) {
      if(user.getSocketChannel() == clientSocket) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  public void addUser(User user) {
    this.userManager.add(user);
  }

