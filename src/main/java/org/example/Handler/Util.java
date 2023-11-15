package org.example.Handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import org.example.Model.Res.JsonMessage;
import org.example.Model.Res.SCSystemMessageRes;

public class Util {

  public static ByteBuffer JsonSerialize(JsonMessage message) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    String json = mapper.writeValueAsString(message);
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
