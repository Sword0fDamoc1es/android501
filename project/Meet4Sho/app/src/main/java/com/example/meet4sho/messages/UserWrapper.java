package com.example.meet4sho.messages;

import com.cometchat.pro.models.User;
import com.stfalcon.chatkit.commons.models.IUser;

public class UserWrapper implements IUser {
    private User user;

    public UserWrapper(User user) {
        this.user = user;
    }

    @Override
    public String getId() {
        return user.getUid();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getAvatar() {
        return user.getAvatar();
    }
}
