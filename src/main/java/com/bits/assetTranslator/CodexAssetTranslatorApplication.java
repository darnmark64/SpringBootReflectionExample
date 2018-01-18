package com.bits.assetTranslator;

import com.bits.assetTranslator.codex.beans.Credentials;
import com.bits.assetTranslator.codex.beans.LoginToken;
import com.bits.assetTranslator.util.ScopeUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CodexAssetTranslatorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CodexAssetTranslatorApplication.class);
    
    //REST endpoints
    private final String codexLoginUrl = "/codex/api/login";
    private final String scopeLogoutUrl = "/logout";
    
    @Autowired
    private ApplicationContext appContext;
    
    @Autowired
    @Qualifier("codexUrl")
    private String codexBaseUrl;
    
    @Autowired
    @Qualifier("scopeUrl")
    private String scopeBaseUrl;
    
    @Autowired
    @Qualifier("codexToken")
    private LoginToken codexLoginToken;
    
    @Autowired
    @Qualifier("scopeCookie")
    private Map<String, String> scopeCookieMap;
    
    @Autowired
    CodexAssetTranslator translator;

    public static void main(String[] args) {
        SpringApplication.run(CodexAssetTranslatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("codexBaseUrl = " + codexBaseUrl);
        log.info("scopeBaseUrl = " + scopeBaseUrl);
        
        log.info("Spring Boot Bean definitions:");
        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            log.info(bean);
        }
        
        if (codexLoginToken != null && (scopeCookieMap != null && !scopeCookieMap.isEmpty())) {
            //query CODEX for assets, translate to SCOPE asset objects, send assets to SCOPE
            translator.run();
            
            try {
                scopeLogout();
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                log.error(sw.toString());
            }
            
            try {
                codexLogout();
            } catch (Exception ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                log.error(sw.toString());
            }
        }
    }
    
    private void codexLogout() throws Exception {
        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> acceptable = new ArrayList<>();
        acceptable.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptable);
        headers.set("X-Auth-Token", codexLoginToken.getToken());
        
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(codexBaseUrl + codexLoginUrl, HttpMethod.DELETE, entity, String.class);
        log.debug("responseEntity = " + responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("CODEX log out successful.");
        }
    }    
    
    public void scopeLogout() throws Exception {
        log.info("Logging out of SCOPE");
        RestTemplate template = new RestTemplate();            
        HttpEntity<String> requestEntity = new HttpEntity<>(ScopeUtils.getRequestHeaders(scopeCookieMap, false));
        ResponseEntity<String> responseEntity = template.exchange(scopeBaseUrl + scopeLogoutUrl, HttpMethod.GET, requestEntity, String.class);
        log.debug("responseEntity = " + responseEntity);
        if (responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getStatusCode().is3xxRedirection()) {
            String body = responseEntity.getBody();
            if (body.contains("You have been logged out")) log.info("SCOPE log out successful.");
        }
    }
}
