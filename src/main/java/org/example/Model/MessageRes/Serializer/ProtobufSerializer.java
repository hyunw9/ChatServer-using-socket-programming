package org.example.Model.MessageRes.Serializer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Collectors;
import org.example.Model.Message.Protobuf.MessageProto;
import org.example.Model.Message.Protobuf.MessageProto.SCSystemMessage;
import org.example.Model.Message.Protobuf.MessageProto.Type;
import org.example.Model.Message.Protobuf.MessageProto.Type.MessageType;
import org.example.Model.MessageRes.Type.SCChatRes;
import org.example.Model.MessageRes.Type.SCRoomListRes;
import org.example.Model.MessageRes.Type.SCSystemMessageRes;

public class ProtobufSerializer implements MessageSerializer {


  @Override
  public ByteBuffer serialize(SCChatRes message) {
    Type type = Type.newBuilder().setType(
        MessageType.forNumber(MessageType.SC_CHAT.getNumber())).build();

    MessageProto.SCChat scChat = MessageProto.SCChat
        .newBuilder()
        .setText(message.getText())
        .setMember(message.getMember())
        .build();

    byte[] typeBytes = type.toByteArray();
    byte[] textBytes = scChat.toByteArray();

    ByteBuffer byteBuffer = ByteBuffer.allocate(2 + typeBytes.length + 2 + textBytes.length)
        .order(ByteOrder.BIG_ENDIAN)
        .putShort((short) typeBytes.length)
        .put(typeBytes)
        .putShort((short) textBytes.length)
        .put(textBytes)
        .flip();
    return byteBuffer;
  }

  @Override
  public ByteBuffer serialize(SCRoomListRes message) {
    Type type = Type.newBuilder().setType(
        MessageType.forNumber(MessageType.SC_ROOMS_RESULT.getNumber())).build();

    MessageProto.SCRoomsResult scRoomsResult =
        MessageProto.SCRoomsResult.newBuilder()
            .addAllRooms(
                message.getSCRoomResList().stream()
                    .map(scRoom ->
                        MessageProto.SCRoomsResult.RoomInfo.newBuilder()
                            .setRoomId(Integer.parseInt(scRoom.getRoomId()))
                            .setTitle(scRoom.getTitle())
                            .addAllMembers(scRoom.getMembers())
                            .build()).collect(Collectors.toList())
            ).build();

    byte[] typeBytes = type.toByteArray();
    byte[] roomListBytes = scRoomsResult.toByteArray();

    ByteBuffer byteBuffer = ByteBuffer.allocate(2 + typeBytes.length + 2 + roomListBytes.length)
        .order(ByteOrder.BIG_ENDIAN)
        .putShort((short) typeBytes.length)
        .put(typeBytes)
        .putShort((short) roomListBytes.length)
        .put(roomListBytes)
        .flip();
    return byteBuffer;
  }

  @Override
  public ByteBuffer serialize(SCSystemMessageRes message) {
    Type type = Type.newBuilder().setType(
            MessageType.forNumber(MessageType.SC_SYSTEM_MESSAGE_VALUE))
        .build();

    SCSystemMessage scSystemMessage = SCSystemMessage
        .newBuilder()
        .setText(message.getText())
        .build();

    byte[] typeBytes = type.toByteArray();
    byte[] textBytes = scSystemMessage.toByteArray();

    ByteBuffer byteBuffer = ByteBuffer.allocate(2 + typeBytes.length + 2 + textBytes.length)
        .order(ByteOrder.BIG_ENDIAN)
        .putShort((short) typeBytes.length)
        .put(typeBytes)
        .putShort((short) textBytes.length)
        .put(textBytes)
        .flip();
    return byteBuffer;
  }
}
