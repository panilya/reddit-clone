package com.panilya.redditserver.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JwtTokenResponseDto {

    private final String jwtToken;

    public JwtTokenResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
