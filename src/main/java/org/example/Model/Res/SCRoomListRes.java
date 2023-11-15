package org.example.Model.Res;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SCRoomListRes implements JsonMessage {

  @JsonProperty
  private String type;

  @JsonProperty
  private List<SCRoom> rooms;

  public SCRoomListRes(List<SCRoom> rooms) {
    this.type = "SCRoomsResult";
    this.rooms = rooms;
  }

  public List<SCRoom> getSCRoomResList(){
    return rooms;
  }

  public void setSCRoomResList(List<SCRoom> rooms){
    this.rooms = rooms;
  }
}
