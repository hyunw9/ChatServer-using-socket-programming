package org.example.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.example.Model.ChatRoomManager;
import org.example.Model.Message.MessageTask;
import org.example.Model.UserManager;

public class HandlerMap {

  private final Map<String, Consumer<MessageTask>>  handlerMap ;

  public HandlerMap(Map<String, Consumer<MessageTask>> handlerMap,HandlerFunction functions) {
    this.handlerMap = handlerMap;
  }

  public static HandlerMap addInitialFuncAndCreateMap(ChatRoomManager chatRoomManager, UserManager userManager){
    Map<String, Consumer<MessageTask>>  handlerMap = new HashMap<>();
    handlerMap.put("CSName",HandlerFunction::on_cs_name);
    handlerMap.put("CSRooms",HandlerFunction::on_cs_rooms);
    handlerMap.put("CSCreateRoom",HandlerFunction::on_cs_create);
    handlerMap.put("CSJoinRoom",HandlerFunction::on_cs_join);
    handlerMap.put("CSLeaveRoom",HandlerFunction::on_cs_leave);
    handlerMap.put("CSChat",HandlerFunction::on_cs_chat);
    HandlerFunction handlerFunction = new HandlerFunction(chatRoomManager,userManager);
    return new HandlerMap(handlerMap,handlerFunction);
  }

  public void put(String name, Consumer<MessageTask> function){
    handlerMap.put(name, function);
  }

  public Consumer<MessageTask> getFunction(String name) {
    return handlerMap.get(name);
  }

}
