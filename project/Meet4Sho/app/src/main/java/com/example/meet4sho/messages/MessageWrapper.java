package com.example.meet4sho.messages;

import com.cometchat.pro.models.TextMessage;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class MessageWrapper implements IMessage {
    private TextMessage message;

    public MessageWrapper(TextMessage message) {
        this.message = message;
    }
    @Override
    public String getId() {
        return message.getMuid();
    }

    @Override
    public String getText() {
        return message.getText();
    }

    @Override
    public IUser getUser() {
        return new UserWrapper(message.getSender());
    }

    @Override
    public Date getCreatedAt() {
        return new Date(message.getSentAt() * 1000);
    }
}
