package com.selvaraj.vendorapp.Interface;

import com.selvaraj.vendorapp.model.UserMessage;

public interface ChatListener {
    void onNewMessage(UserMessage message);
}
