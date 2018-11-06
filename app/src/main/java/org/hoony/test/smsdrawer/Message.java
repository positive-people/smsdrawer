package org.hoony.test.smsdrawer;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

public class Message implements IMessage {
    private String id;
    private String text;
    private Date createdAt;
    private com.stfalcon.chatkit.commons.models.IUser user;

    @Override
    public String getId() {
        return id;
    }
    @Override
    public String getText() {
        return text;
    }
    @Override
    public com.stfalcon.chatkit.commons.models.IUser getUser() {
        return user;
    }
    //        @Override
//        public Author getUser() {
//            return author;
//        }
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
