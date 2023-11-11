package org.example.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HandlerMap {

  private final Map<String, Consumer<String>>  handlerMap ;

  public HandlerMap(Map<String, Consumer<String>> handlerMap) {
    this.handlerMap = handlerMap;
  }

  public static HandlerMap addInitialFuncAndCreateMap(){
    Map<String, Consumer<String>>  handlerMap = new HashMap<>();
    handlerMap.put("CSRooms",HandlerFunction::on_cs_rooms);
    handlerMap.put("CSJoinRoom",HandlerFunction::on_cs_join);
    handlerMap.put("CSLeaveRoom",HandlerFunction::on_cs_leave);
    handlerMap.put("CSChat",HandlerFunction::on_cs_chat);
    handlerMap.put("CSLeave",HandlerFunction::on_cs_leave);
    handlerMap.put("CSShutdown",HandlerFunction::on_cs_shutdown);
    return new HandlerMap(handlerMap);
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
