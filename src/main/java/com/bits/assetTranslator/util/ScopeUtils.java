package com.bits.assetTranslator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ScopeUtils {
    
    public static HttpHeaders getRequestHeaders(Map<String, String> cookies, boolean addContentType) {
        HttpHeaders requestHeader = new HttpHeaders();
        if (addContentType) requestHeader.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptType = new ArrayList<>();
        acceptType.add(MediaType.APPLICATION_JSON);
        acceptType.add(MediaType.TEXT_PLAIN);
        acceptType.add(MediaType.ALL);
        requestHeader.setAccept(acceptType);
        for (String key : cookies.keySet()) {
            requestHeader.add(HttpHeaders.COOKIE, key + "=" + cookies.get(key));
        }

        return requestHeader;
    }
    
}
