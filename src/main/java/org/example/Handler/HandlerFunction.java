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
  //TODO Create,Join 만든 후 구현할것
  public static void on_cs_rooms(MessageTask task) {
    List<SCRoom> roomList = chatRoomManager.getRoomInfoList();
    JsonMessage SCRoomListRes = new SCRoomListRes(roomList);
    userManager.sendMessage(task.getClientSocket(),SCRoomListRes);
  }

  //채팅 방 참여 요청 처리
  public static void on_cs_join(MessageTask task) {
    int roomId = Integer.parseInt(task.getRoomId());
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());
    boolean userIsNotInRoom = chatRoomManager.isRoom(roomId);

    if (userIn) { //이미 유저가 방에 속한 경우
      JsonMessage SCSystemMessage = new SCSystemMessageRes("이미 방에 속한 유저입니다! ");
      chatRoomManager.sendMessage(task.getClientSocket(), SCSystemMessage);
    } else if (!userIsNotInRoom) { //유저가 방에 속해있지 않은 경우
      JsonMessage SCSystemMessage = new SCSystemMessageRes("존재하지 않는 방입니다.");
      userManager.sendMessage(task.getClientSocket(), SCSystemMessage);
    } else {
      User user = userManager.findUser(task.getClientSocket())
          .orElseThrow(NoSuchElementException::new);

      userManager.removeUser(user.getSocketChannel());
      Room room = chatRoomManager.findRoomByRoomId(roomId).orElseThrow(NoSuchElementException::new);
      chatRoomManager.addUserToChatRoom(room, user);
      JsonMessage SCSystemMessage = new SCSystemMessageRes(user.getName() + " 님이 채팅방에 입장하셨습니다. ");
      chatRoomManager.broadcastMsgToRoom(room.getId(), SCSystemMessage);
    }
  }


  //채팅 방 만들기 요청 처리
  public static void on_cs_create(MessageTask task) {
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());

    if (userIn) {//방 내부에서 방 생성할경우
      JsonMessage SCSystemMessageRes = new SCSystemMessageRes("이미 방에 속한 유저입니다!");
      chatRoomManager.sendMessage(task.getClientSocket(), SCSystemMessageRes);
    } else {
      //방에 속해있지 않으면
      User user = userManager.findUser(task.getClientSocket())
          .orElseThrow(NoSuchElementException::new);
      userManager.removeUser(user.getSocketChannel());

      Room newRoom = new Room(task.getTitle());
      chatRoomManager.addUserToChatRoom(newRoom, user);

      System.out.println("id: " + newRoom.getId() + " title: " + newRoom.getTitle() + " 방 생성");
      JsonMessage SCSystemMessageRes = new SCSystemMessageRes(task.getTitle() + " 방을 생성했습니다.");
      chatRoomManager.sendMessage(user.getSocketChannel(), SCSystemMessageRes);
    }
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
