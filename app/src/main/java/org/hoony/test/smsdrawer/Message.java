package org.hoony.test.smsdrawer;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {
    private String id;
    private String text;
    private IUser user;
    private Date createdAt;

    public Message(String id, String text, IUser user, Date created) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = created;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
