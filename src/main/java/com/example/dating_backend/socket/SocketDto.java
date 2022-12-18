package com.example.dating_backend.socket;

import lombok.Getter;
import lombok.ToString;

public class SocketDto {
    @Getter
    public static class Message{
        private String user;
        private String message;

        @Override
        public String toString() {
            return "Message{" +
                    "user='" + user + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
