package com.bits.assetTranslator;

import com.bits.assetTranslator.scope.beans.EntityClass;
import com.bits.assetTranslator.util.ScopeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScopeEntityClassRetriever {

    private static final Logger log = LoggerFactory.getLogger(ScopeEntityClassRetriever.class);
    
    @Autowired
    @Qualifier("scopeCookie")
    private Map<String, String> scopeCookieMap;
    
    @Autowired
    @Qualifier("scopeUrl")
    private String scopeBaseUrl;
    
    private final String entityUrl = "/api/entityclass";

    @Bean(name = "scopeEntityClasses")
    public Map<String, EntityClass> getScopeEntityClasses() throws Exception {
        List<EntityClass> entityClassesList = null;
        if (scopeCookieMap != null && !scopeCookieMap.isEmpty() && scopeBaseUrl != null && !scopeBaseUrl.isEmpty()) {
            //try getting a list of entity classes
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(ScopeUtils.getRequestHeaders(scopeCookieMap, false));
            ResponseEntity<String> responseEntity = template.exchange(scopeBaseUrl + entityUrl, HttpMethod.GET, requestEntity, String.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                ObjectMapper mapper = new ObjectMapper();
                entityClassesList = Arrays.asList(mapper.readValue(responseEntity.getBody(), EntityClass[].class));
            } else {
                throw new Exception("Entity Class query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
        } else {
            throw new Exception("Either the SCOPE cookie map is missing or the SCOPE base URL is null");
        }
        
        if (entityClassesList != null && !entityClassesList.isEmpty()) {
            Map<String, EntityClass> entityClassMap = new HashMap<>();
            for (EntityClass entityClass : entityClassesList) {
                log.debug("Adding to the map: " + entityClass.toString());
                entityClassMap.put(entityClass.getName(), entityClass);
            }
            
            return entityClassMap;
        } else {
            throw new Exception("No SCOPE Entity Classes found.");
        }
    }
}
