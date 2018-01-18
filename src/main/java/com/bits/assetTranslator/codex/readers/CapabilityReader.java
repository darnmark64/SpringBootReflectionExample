package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.Capabilities;
import com.bits.assetTranslator.codex.beans.Capability;
import com.bits.assetTranslator.codex.beans.LoginToken;
import com.bits.assetTranslator.util.CodexUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CapabilityReader {

    private static final Logger log = LoggerFactory.getLogger(CapabilityReader.class);
    
    private final static String capabilityUrl = "/codex/api/capabilities";
    
    private Map<String, Capability> capabilityMap = new HashMap<>();
    
    @Autowired
    @Qualifier("codexToken")
    private LoginToken codexLoginToken;
    
    @Autowired
    @Qualifier("codexUrl")
    private String codexBaseUrl;
    
    public Map<String, Capability> getCapabilities() throws Exception {
        if (this.codexLoginToken != null && this.codexBaseUrl != null) {
            // set headers
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            
            List<Capability> capabilityList;
            //try getting a list of capabilities
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Capabilities> responseEntity = template.exchange(codexBaseUrl + capabilityUrl, HttpMethod.GET, requestEntity, Capabilities.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                capabilityList = responseEntity.getBody().getCapabilities();
            } else {
                throw new Exception("Capability query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
            
            if (!capabilityList.isEmpty()) {                
                capabilityList.forEach((capability) -> {
                    capabilityMap.put(capability.getUuid(), capability);
                });
                log.debug("capabilityList.size() = " + capabilityList.size() + "; capabilityMap.size() = " + capabilityMap.size());
                
                return capabilityMap;
            } else {
                throw new Exception("Capability list is empty.");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    }
    
    @Bean(name = "codexCapabilities")
    public Map<String, Capability> getCapabilityMap() throws Exception {
        if (this.capabilityMap == null || this.capabilityMap.isEmpty()) {
            getCapabilities();
        }
        return capabilityMap;
    }
}
