package org.example.Handler;

import com.google.protobuf.Message;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import org.example.Model.Manager.ChatRoomManager;
import org.example.Model.MessageRes.Type.MessageRes;
import org.example.Model.MessageRes.Type.SCChatRes;
import org.example.Model.MessageRes.Type.SCRoom;
import org.example.Model.MessageRes.Type.SCRoomListRes;
import org.example.Model.MessageRes.Type.SCSystemMessageRes;
import org.example.Model.Message.MessageTask;
import org.example.Model.Room;
import org.example.Model.User;
import org.example.Model.Manager.UserManager;
import org.example.ServerController;

public class HandlerFunction {

  private static ChatRoomManager chatRoomManager;
  private static UserManager userManager;

  public HandlerFunction(ChatRoomManager chatRoomManager, UserManager userManager) {
    HandlerFunction.chatRoomManager = chatRoomManager;
    HandlerFunction.userManager = userManager;
  }

  //채팅 이름 지정 요청 처리
  public static void on_cs_name(MessageTask task) {
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());
    String beforeName = "";
    if (userIn) {
      User user = chatRoomManager.findUser(task.getClientSocket());
      beforeName += user.getName();
      System.out.println(user.getName());
      user.setName(task.getName());

    } else {//유저가 방에 없으면
      User user = userManager.findUser(task.getClientSocket())
          .orElseThrow(NoSuchElementException::new);
      beforeName += user.getName();
      userManager.setUserName(task.getClientSocket(), task.getName());
    }
    String msg = beforeName + " 의 이름이" + task.getName() + " 으로 변경되었습니다.";
    MessageRes StoC = new SCSystemMessageRes(msg);

    if (userIn) {
      Room room = chatRoomManager.findRoomByUserSocket(task.getClientSocket());
      chatRoomManager.broadcastMsgToRoom(room.getId(), StoC);
    } else {
      userManager.sendMessage(task.getClientSocket(), StoC);
    }
  }

  //채팅 방 목록 요청 처리
  public static void on_cs_rooms(MessageTask task) {
    List<SCRoom> roomList = chatRoomManager.getRoomInfoList();
    MessageRes SCRoomListRes = new SCRoomListRes(roomList);
    userManager.sendMessage(task.getClientSocket(), SCRoomListRes);
  }

  //채팅 방 참여 요청 처리
  public static void on_cs_join(MessageTask task) {
    int roomId = Integer.parseInt(task.getRoomId());
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());
    boolean userIsNotInRoom = chatRoomManager.isRoom(roomId);

    if (userIn) { //이미 유저가 방에 속한 경우
      MessageRes SCSystemMessageRes = new SCSystemMessageRes("이미 방에 속한 유저입니다! ");
      chatRoomManager.sendMessage(task.getClientSocket(), SCSystemMessageRes);
    } else if (!userIsNotInRoom) { //유저가 방에 속해있지 않은 경우
      MessageRes SCSystemMessageRes = new SCSystemMessageRes("존재하지 않는 방입니다.");
      userManager.sendMessage(task.getClientSocket(), SCSystemMessageRes);
    } else {
      User user = userManager.findUser(task.getClientSocket())
          .orElseThrow(NoSuchElementException::new);

      userManager.removeUser(user.getSocketChannel());
      Room room = chatRoomManager.findRoomByRoomId(roomId).orElseThrow(NoSuchElementException::new);
      chatRoomManager.addUserToChatRoom(room, user);
      MessageRes SCSystemMessageRes = new SCSystemMessageRes(user.getName() + " 님이 채팅방에 입장하셨습니다. ");
      chatRoomManager.broadcastMsgToRoom(room.getId(), SCSystemMessageRes);
    }
  }

  //채팅 방 만들기 요청 처리
  public static void on_cs_create(MessageTask task) {
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());

    if (userIn) {//방 내부에서 방 생성할경우
      MessageRes SCSystemMessageResRes = new SCSystemMessageRes("이미 방에 속한 유저입니다!");
      chatRoomManager.sendMessage(task.getClientSocket(), SCSystemMessageResRes);
    } else {
      //방에 속해있지 않으면
      User user = userManager.findUser(task.getClientSocket())
          .orElseThrow(NoSuchElementException::new);
      userManager.removeUser(user.getSocketChannel());

      Room newRoom = new Room(task.getTitle());
      chatRoomManager.addUserToChatRoom(newRoom, user);

      System.out.println("id: " + newRoom.getId() + " title: " + newRoom.getTitle() + " 방 생성");
      MessageRes SCSystemMessageResRes = new SCSystemMessageRes(task.getTitle() + " 방을 생성했습니다.");
      userManager.sendMessage(user.getSocketChannel(), SCSystemMessageResRes);
    }
  }

  //채팅 방 나가기 요청 처리
  public static void on_cs_leave(MessageTask task) {
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());

    //방이 없다면
    if (!userIn) {
      MessageRes SCSystemMessageResRes = new SCSystemMessageRes("방에 속해있지 않습니다.");
      userManager.sendMessage(task.getClientSocket(), SCSystemMessageResRes);

    } else { //방에 속해있다면
      Room userRoom = chatRoomManager.findRoomByUserSocket(task.getClientSocket());
      User user = chatRoomManager.findUser(task.getClientSocket());

      //만약 User가 떠났을 때, 방에 아무도 남지 않는다면 방 삭제
      if(chatRoomManager.checkRoomIsEmpty(userRoom)){
        chatRoomManager.deleteRoom(userRoom);
      }

      chatRoomManager.deleteUser(userRoom, user.getSocketChannel());
      userManager.addUser(user);
      MessageRes broadCastingMsg = new SCSystemMessageRes(
          user.getName() + " 님이 " + userRoom.getTitle() + " 방에서 나갔습니다. ");
      MessageRes userMsg = new SCSystemMessageRes(
          userRoom.getTitle() + " 방에서 나갔습니다. ");

      chatRoomManager.broadcastMsgToRoom(userRoom.getId(), broadCastingMsg);
      userManager.sendMessage(user.getSocketChannel(), userMsg);
    }
  }

  //채팅 메세지 전송 요청 처리
  public static void on_cs_chat(MessageTask task) {
    boolean userIn = chatRoomManager.checkUserInRoom(task.getClientSocket());

    if (!userIn) {
      String msg = "현재 대화방에 들어가 있지 않습니다.";
      MessageRes scSystemMessageRes = new SCSystemMessageRes(msg);
      chatRoomManager.sendMessage(task.getClientSocket(), scSystemMessageRes);
    } else {
      Room room = chatRoomManager.findRoomByUserSocket(task.getClientSocket());
      String msg = task.getText();
      User user = chatRoomManager.findUser(task.getClientSocket());
      MessageRes scChatRes = new SCChatRes(msg, user.getName());
      chatRoomManager.broadcastMsgToRoom(room.getId(), scChatRes);
    }
  }

  public static void on_cs_shutdown(MessageTask task) throws IOException {
    ServerController.shutdownServer();
  }
}
