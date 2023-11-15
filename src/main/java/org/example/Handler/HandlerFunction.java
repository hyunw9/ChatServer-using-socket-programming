package org.example.Handler;

import org.example.Model.ChatRoomManager;

public class HandlerFunction {

  private final ChatRoomManager chatRoomManager;

  public HandlerFunction(ChatRoomManager chatRoomManager) {
    this.chatRoomManager = chatRoomManager;
  }

  //채팅 이름 지정 요청 처리
  public static void on_cs_name(String message){
    System.out.println("/name");
  public static void on_cs_name(MessageTask task) {
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());

    if (userIn) {
      User user = chatRoomManager.findUserInfo(task.getClientSocket());
      System.out.println(user.getName());
      user.setName(task.getName());

    } else {//유저가 방에 없으면
      userManager.setUserName(task.getClientSocket(), task.getName());
    }
    String msg = "이름이" + task.getName() + " 으로 변경되었습니다.";
    JsonMessage StoC = new SCSystemMessageRes(msg);

    if (userIn) {
      Room room = chatRoomManager.findRoomByUserSocket(task.getClientSocket());
      chatRoomManager.broadcastMsgToRoom(room.getId(), StoC);
    } else {
      userManager.sendMessage(task.getClientSocket(), StoC);
    }
  }

  //채팅 방 목록 요청 처리
  public static void on_cs_rooms(String message){
    System.out.println("/rooms");
  }

  //채팅 방 목록 요청 처리
  public static void on_cs_join(String message){
    System.out.println("/join id");
  }

  //채팅 방 만들기 요청 처리
  public static void on_cs_create(String message){
    System.out.println("/create name");
  }

  //채팅 방 나가기 요청 처리
  public static void on_cs_leave(String message){
    System.out.println("/leave id");
  }

  //채팅 메세지 전송 요청 처리
  public static void on_cs_chat(String message){
    System.out.println("chat data");
  }

  //채팅 서버 종료 요청 처리
  public static void on_cs_shutdown(String s) {
    System.out.println("/shutdown ");
  }
}
