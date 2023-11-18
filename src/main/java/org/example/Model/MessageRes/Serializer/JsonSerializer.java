package org.example.Model.MessageRes.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import org.example.Model.MessageRes.Type.SCChatRes;
import org.example.Model.MessageRes.Type.SCRoomListRes;
import org.example.Model.MessageRes.Type.SCSystemMessageRes;

public class JsonSerializer implements MessageSerializer {

  private final ObjectMapper mapper;

  public JsonSerializer() {
    this.mapper = new ObjectMapper();
  }

  @Override
  public ByteBuffer serialize(SCChatRes message) {

    String json;
    try {
      json = mapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    byte[] jsonData = json.getBytes(StandardCharsets.UTF_8);
    int messageLength = jsonData.length;

    ByteBuffer byteBuffer = ByteBuffer.allocate(2 + messageLength)
        .order(ByteOrder.BIG_ENDIAN)
        .putShort((short) messageLength)
        .put(jsonData)
        .flip();

    return byteBuffer;
  }

  @Override
  public ByteBuffer serialize(SCRoomListRes message) {

    String json;
    try {
      json = mapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    byte[] jsonData = json.getBytes(StandardCharsets.UTF_8);
    int messageLength = jsonData.length;

    return ByteBuffer.allocate(2 + messageLength)
        .order(ByteOrder.BIG_ENDIAN)
        .putShort((short) messageLength)
        .put(jsonData)
        .flip();
  }

  @Override
  public ByteBuffer serialize(SCSystemMessageRes message) {

    String json;
    try {
      json = mapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    byte[] jsonData = json.getBytes(StandardCharsets.UTF_8);
    int messageLength = jsonData.length;

    ByteBuffer byteBuffer = ByteBuffer.allocate(2 + messageLength)
        .order(ByteOrder.BIG_ENDIAN)
        .putShort((short) messageLength)
        .put(jsonData)
        .flip();

    return byteBuffer;
  }

}
