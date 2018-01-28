package com.friday.guide.api.service.security;


import com.friday.guide.api.utils.CryptUtils;
import com.friday.guide.api.utils.HttpUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


@Component
public class UrlParamsCrypto {

    private static final Logger logger = LoggerFactory.getLogger(UrlParamsCrypto.class);
    private static final String ENCODING = "utf-8";

    @Value("#{'${app.crypt.key}'.getBytes(T(java.nio.charset.StandardCharsets).UTF_8)}")
    private byte[] urlCryptKey;

    public String encryptParam(String param) {
        return encrypt(param);
    }

    public String decryptParam(String param) {
        return decrypt(param);
    }

    public String getEncryptedParams(Map<String, String> params) {
        return HttpUtils.constructUrlParams(encrypt(params));
    }

    public Map<String, String> encrypt(Map<String, String> params) {
        return act(params, this::encrypt);
    }

    public Map<String, String> decrypt(Map<String, String> params) {
        return act(params, this::decrypt);
    }

    private Map<String, String> act(Map<String, String> params, Function<String, String> actor) {
        if (MapUtils.isEmpty(params)) return Collections.emptyMap();
        Map<String, String> result = new ConcurrentHashMap<>();
        params.forEach((k, v) -> result.put(actor.apply(k), actor.apply(v)));
        return result;
    }

    private String encrypt(String val) {
        if (StringUtils.isEmpty(val)) return val;
        try {
            return CryptUtils.encrypt(val, urlCryptKey, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error occurred while encrypting url parameter " + val, e);
        }
        return null;
    }

    private String decrypt(String val) {
        if (StringUtils.isEmpty(val)) return val;
        try {
            return CryptUtils.decrypt(val, urlCryptKey, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error occurred while decrypting url parameter " + val, e);
        }
        return null;
    }
}
