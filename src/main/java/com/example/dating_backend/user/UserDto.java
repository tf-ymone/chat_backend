package com.example.dating_backend.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserDto {

    @Getter
    public static class userSignUpReq {
        private String email;
        private String password;

    }

    @Getter
    @AllArgsConstructor
    public static class userSignUpRes {
        private String email;
    }

    @Getter
    public static class userSignInReq {
        private String email;
        private String password;
    }

    @Getter
    public static class userSignInRes {
        private String email;

        public userSignInRes(User user) {
            this.email = user.getUserEmail();
        }
    }
}
