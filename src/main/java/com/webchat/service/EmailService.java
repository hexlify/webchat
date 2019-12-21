package com.webchat.service;

import com.webchat.model.User;

public interface EmailService {
    void sendVerificationMail(User user, String token);
}
