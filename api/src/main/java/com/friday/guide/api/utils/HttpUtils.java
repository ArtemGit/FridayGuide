package com.friday.guide.api.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUtils {

    public static final String ALL_IP_RANGE = "0.0.0.0/0";
    public static final String PARAM_EQ = "=";
    public static final String PARAM_DELIMETER = "&";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String PARAMS_START_MARK = "?";
    public static final String URL_SLASH = "/";
    public static final String X_REAL_IP_HEADER = "X-Real-IP";

    public static ResponseEntity<byte[]> fileDownload(String fileName, byte[] data) throws IOException {
        return new ResponseEntity<>(data, headers(fileName, "attachment"), HttpStatus.OK);
    }

    public static ResponseEntity<byte[]> fileView(String fileName, byte[] data) throws IOException {
        return new ResponseEntity<>(data, headers(fileName, "inline"), HttpStatus.OK);
    }

    private static HttpHeaders headers(String fileName, String actionType) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", actionType + ";filename=\"" + UriUtils.encodePath(fileName, DEFAULT_ENCODING) + "\"");
        return headers;
    }

    public static Map<String, String> prepareParams(Map<String, String[]> parametersMap) {
        if (parametersMap == null) return null;
        Map<String, String> prepared = new HashMap<>();
        parametersMap.forEach(
                (k, v) -> prepared.put(k, ArrayUtils.isEmpty(v) ? null : v[0]));
        return prepared;
    }

    public static Map<String, String> parseParams(String parametersString) throws Exception {
        if (StringUtils.isEmpty(parametersString)) return Collections.emptyMap();
        final Map<String, String> result = new LinkedHashMap<>();
        for (String param : parametersString.split(PARAM_DELIMETER)) {
            if (param.contains(PARAM_EQ)) {
                result.putIfAbsent(
                        StringUtils.substringBefore(param, PARAM_EQ),
                        URLDecoder.decode(StringUtils.substringAfter(param, PARAM_EQ), DEFAULT_ENCODING)
                );
            }
        }
        return result;
    }

    public static String constructUrlParams(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) return StringUtils.EMPTY;
        StringBuilder builder = new StringBuilder();
        params.forEach((k, v) -> {
            if (builder.length() > 0) {
                builder.append(PARAM_DELIMETER);
            }
            builder.append(k).append(PARAM_EQ).append(v);
        });
        return builder.toString();
    }
}
