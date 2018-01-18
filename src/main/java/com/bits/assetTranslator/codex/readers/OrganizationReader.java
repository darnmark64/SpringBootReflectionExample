package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.Organization;
import com.bits.assetTranslator.codex.beans.Organizations;
import com.bits.assetTranslator.util.CodexUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author darnellm
 */
@Service
public class OrganizationReader extends AssetReader<Organization> {
    
    private final static String organizationUrl = "/codex/api/organizations";
    private List<Organization> organizationList = new ArrayList<>();
    private Map<String, Organization> organizationMap = new HashMap<>();
    
    @Override
    public void generateMap() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of organizations
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Organizations> responseEntity = template.exchange(codexBaseUrl + organizationUrl, HttpMethod.GET, requestEntity, Organizations.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                organizationList.addAll(responseEntity.getBody().getOrganizations());
            } else {
                throw new Exception("Organization query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
            
            if (!organizationList.isEmpty()) {                
                organizationList.forEach((organization) -> {
                    organizationMap.put(organization.getUuid(), organization);
                });
                log.debug("organizationList.size() = " + organizationList.size() + "; organizationMap.size() = " + organizationMap.size());
            } else {
                throw new Exception("Organization list is empty.");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 

    @Override
    @Bean(name = "organizationMap")
    public Map<String, Organization> getMap() throws Exception {
        if (this.organizationMap == null || this.organizationMap.isEmpty()) {
            generateMap();
        }
        return this.organizationMap;
    }
    
    @Override
    public void generateList() throws Exception {
        throw new Exception("Organization list is generated with the Map; use the generateMap method");
    }
    
    @Override
    @Bean(name = "organizationList")
    public List<Organization> getList() throws Exception {
        if (this.organizationList == null || this.organizationList.isEmpty()) {
            generateMap();
        }
        return this.organizationList;
    }
}
