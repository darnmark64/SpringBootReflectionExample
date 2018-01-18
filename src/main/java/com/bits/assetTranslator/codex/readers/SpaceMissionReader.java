package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.SpaceMission;
import com.bits.assetTranslator.codex.beans.SpaceMissions;
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

@Service
public class SpaceMissionReader extends AssetReader<SpaceMission> {
    
    private final static String spaceMissionUrl = "/codex/api/space-missions";
    private List<SpaceMission> spaceMissionList = new ArrayList<>();
    private Map<String, SpaceMission> spaceMissionMap = new HashMap<>();
    
    @Override
    public void generateMap() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of space missions
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<SpaceMissions> responseEntity = template.exchange(codexBaseUrl + spaceMissionUrl, HttpMethod.GET, requestEntity, SpaceMissions.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                spaceMissionList.addAll(responseEntity.getBody().getSpaceMissions());
            } else {
                throw new Exception("Space Mission query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
            
            if (!spaceMissionList.isEmpty()) {                
                spaceMissionList.forEach((spaceMission) -> {
                    spaceMissionMap.put(spaceMission.getUuid(), spaceMission);
                });
                log.debug("spaceMissionList.size() = " + spaceMissionList.size() + "; spaceMissionMap.size() = " + spaceMissionMap.size());
            } else {
                throw new Exception("Space Mission list is empty.");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 

    @Override
    @Bean(name = "spaceMissionMap")
    public Map<String, SpaceMission> getMap() throws Exception {
        if (this.spaceMissionMap == null || this.spaceMissionMap.isEmpty()) {
            generateMap();
        }
        return this.spaceMissionMap;
    }
    
    @Override
    public void generateList() throws Exception {
        throw new Exception("Space Mission list is generated with the Map; use the generateMap method");
    }
    
    @Override
    @Bean(name = "spaceMissionList")
    public List<SpaceMission> getList() throws Exception {
        if (this.spaceMissionList == null || this.spaceMissionList.isEmpty()) {
            generateMap();
        }
        return this.spaceMissionList;
    }
}
