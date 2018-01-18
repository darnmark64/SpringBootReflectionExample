package com.bits.assetTranslator;

import com.bits.assetTranslator.codex.beans.Credentials;
import com.bits.assetTranslator.codex.beans.LoginToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author darnellm
 */
@Configuration
public class CodexAssetTranslatorConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CodexAssetTranslatorConfiguration.class);
    
    //REST endpoints
    private final String codexLoginUrl = "/codex/api/login";
    private final String scopeLoginUrl = "/login";
    
    @Bean(name = "codexUrl")
    public String getCodexBaseUrl(@Value("${codex.secure}") boolean codexSecure, @Value("${codex.base.address}") String codexBaseAddress, @Value("${codex.port}") int codexPort) {
        String url = (codexSecure ? "https://" : "http://") + codexBaseAddress + ":" + codexPort;
        
        return url;
    }
    
    @Bean(name = "scopeUrl")
    public String getScopeBaseUrl(@Value("${scope.secure}") boolean scopeSecure, @Value("${scope.base.address}") String scopeBaseAddress, @Value("${scope.port}") int scopePort) {
        String url = (scopeSecure ? "https://" : "http://") + scopeBaseAddress + ":" + scopePort;
        
        return url;
    }
    
    @Bean(name = "codexToken")
    public LoginToken getCodexLoginToken(@Value("${codex.secure}") boolean codexSecure, @Value("${codex.base.address}") String codexBaseAddress, @Value("${codex.port}") int codexPort,
                                         @Value("${codex.username}") String cUsername, @Value("${codex.password}") String cPassword) throws Exception {
        String codexBaseUrl = (codexSecure ? "https://" : "http://") + codexBaseAddress + ":" + codexPort;
        
        Credentials creds = new Credentials();
        creds.setUser(cUsername);
        creds.setPass(cPassword);

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptable = new ArrayList<>();
        acceptable.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptable);
        HttpEntity<Credentials> entity = new HttpEntity<>(creds, headers);

        // send request and parse result
        log.info("Logging into CODEX using account " + cUsername);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LoginToken> loginResponse = restTemplate.exchange(codexBaseUrl + codexLoginUrl, HttpMethod.POST, entity, LoginToken.class);
        if (loginResponse.getStatusCode().is2xxSuccessful()) {
            LoginToken token = loginResponse.getBody();
            log.debug("token = " + token.toString());
            return token;
        } else {
            throw new Exception(loginResponse.getStatusCode().getReasonPhrase());
        }
    }
    
    @Bean(name = "scopeCookie")
    public Map<String, String> getScopeCookieMap(@Value("${scope.secure}") boolean scopeSecure, @Value("${scope.base.address}") String scopeBaseAddress, @Value("${scope.port}") int scopePort, 
                                                 @Value("${scope.username}") String sUsername, @Value("${scope.password}") String sPassword) throws Exception {
        String scopeBaseUrl = (scopeSecure ? "https://" : "http://") + scopeBaseAddress + ":" + scopePort;
        List<String> cookies = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", sUsername);
        map.add("password", sPassword);
        map.add("remember-me", "on");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        log.info("Logging into SCOPE using account " + sUsername);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.postForEntity(scopeBaseUrl + scopeLoginUrl, request, String.class);
        log.debug("response = " + response);
        if (response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()) {
            HttpHeaders responseHeaders = response.getHeaders();
            cookies = new ArrayList<>();
            cookies.addAll(responseHeaders.get("Set-Cookie"));
        }
        
        return parseCookies(cookies);
    }
    
    private Map<String, String> parseCookies(List<String> cookies) {
        Map<String, String> cookieMap = new HashMap<>();
        cookies.forEach((cookie) -> {
            int equal = cookie.indexOf("=");
            int semicolon = cookie.indexOf(";");
            String key = cookie.substring(0, equal);
            String value = cookie.substring(equal + 1, semicolon + 1);
            cookieMap.put(key, value);
        });
        
        return cookieMap;
    }
}
