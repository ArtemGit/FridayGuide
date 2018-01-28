package com.friday.guide.api.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EncryptedUrlConstructor {

    @Autowired
    private UrlParamsCrypto urlParamsCrypto;

    private String enc(Object value) {
        return urlParamsCrypto.encryptParam(String.valueOf(value));
    }
}
