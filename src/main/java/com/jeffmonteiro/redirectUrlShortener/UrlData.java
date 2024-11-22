package com.jeffmonteiro.redirectUrlShortener;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UrlData {

    private String originalUrl;
    private long expirationTime;

}
