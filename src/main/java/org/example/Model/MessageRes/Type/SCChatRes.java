package org.example.Model.MessageRes.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import org.example.Model.MessageRes.Serializer.MessageSerializer;

public class SCChatRes implements MessageRes {

  @JsonProperty
  private String type;

  @JsonProperty
  private String text;

  @JsonProperty
  private String member;

  public SCChatRes(String text, String member) {
    this.type = "SCChat";
    this.text = text;
    this.member = member;
  }

  public String getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  public String getMember() {
    return member;
  }

  @Override
  public ByteBuffer accept(MessageSerializer serializer) {
    try {
      return serializer.serialize(this);
    } catch (InvalidProtocolBufferException e) {
      throw new RuntimeException(e);
    }
  }
}
