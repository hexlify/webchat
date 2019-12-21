package com.webchat.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {

    private String from;
    private String fromName;
    private String url;
    private SMTP smtp;

    @Data
    public static class SMTP {
        String host;
        String port;
        String username;
        String apiKey;
    }
}
