package org.example.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.example.Model.ChatRoomManager;

public class HandlerMap {

  private final Map<String, Consumer<String>>  handlerMap ;
  private HandlerFunction handlerFunctions;


  public HandlerMap(Map<String, Consumer<String>> handlerMap,HandlerFunction functions) {
    this.handlerMap = handlerMap;
    this.handlerFunctions = functions;
  }

  public static HandlerMap addInitialFuncAndCreateMap(ChatRoomManager chatRoomManager){
    Map<String, Consumer<String>>  handlerMap = new HashMap<>();
    handlerMap.put("CSName",HandlerFunction::on_cs_name);
    handlerMap.put("CSRooms",HandlerFunction::on_cs_rooms);
    handlerMap.put("CSCreateRoom",HandlerFunction::on_cs_create);
    handlerMap.put("CSJoinRoom",HandlerFunction::on_cs_join);
    handlerMap.put("CSLeaveRoom",HandlerFunction::on_cs_leave);
    handlerMap.put("CSChat",HandlerFunction::on_cs_chat);
    handlerMap.put("CSShutdown",HandlerFunction::on_cs_shutdown);
    HandlerFunction handlerFunction = new HandlerFunction(chatRoomManager);
    return new HandlerMap(handlerMap,handlerFunction);
  }

  public void put(String name, Consumer<String> function){
    handlerMap.put(name, function);
  }

  public Consumer<String> getFunction(String name) {
    return handlerMap.get(name);
  }

  public Map<String, Consumer<String>> getHandlerMap() {
    return handlerMap;
  }
}
