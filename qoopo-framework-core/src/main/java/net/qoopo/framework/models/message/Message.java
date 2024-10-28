package net.qoopo.framework.models.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message implements Serializable {

    private String text;
    private String user;
    private boolean updateList;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, boolean updateList) {
        this.text = text;
        this.updateList = updateList;
    }

    public Message(String user, String text, boolean updateList) {
        this.text = text;
        this.user = user;
        this.updateList = updateList;
    }

    public Message setText(String text) {
        this.text = text;
        return this;
    }

    public Message setUser(String user) {
        this.user = user;
        return this;
    }

}
