package com.panilya.redditserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SignUpDto {

    private final String username;
    private final String password;
    private final String email;

}
