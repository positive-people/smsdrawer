package org.hoony.test.smsdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class MessagesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
    }
    public class Author implements IUser {

        private String id;
        private String name;
        private String avatar;

        @Override
        public String getId() {
            return id;
        }
        @Override
        public String getName() {
            return name;
        }
        @Override
        public String getAvatar() {
            return avatar;
        }
    }

    public class Message implements IMessage{

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




}
