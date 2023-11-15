package org.example.Model.Res;

import java.util.List;

public class SCRoom {

    private String roomId;
    private String title;
    private List<String> members;

    public SCRoom(String roomId, String title, List<String> members) {
        this.roomId = roomId;
        this.title = title;
        this.members = members;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getMembers() {
        return members;
    }
}
