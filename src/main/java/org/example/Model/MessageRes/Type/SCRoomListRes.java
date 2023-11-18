package org.example.Model.MessageRes.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.nio.ByteBuffer;
import java.util.List;
import org.example.Model.MessageRes.Serializer.MessageSerializer;

public class SCRoomListRes implements MessageRes {

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

  @Override
  public String getType() {
    return type;
  }

  @Override
  public ByteBuffer accept(MessageSerializer serializer) {
    return serializer.serialize(this);
  }
}
