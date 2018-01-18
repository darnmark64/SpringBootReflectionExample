package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.SpaceVehicle;
import com.bits.assetTranslator.codex.beans.SpaceVehicles;
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
public class SpaceVehicleReader extends AssetReader<SpaceVehicle> {
    
    private final static String spaceVehicleUrl = "/codex/api/space-vehicles";
    private List<SpaceVehicle> spaceVehicleList = new ArrayList<>();
    private Map<String, SpaceVehicle> spaceVehicleMap = new HashMap<>();
    
    @Override
    public void generateMap() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of space vehicles
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<SpaceVehicles> responseEntity = template.exchange(codexBaseUrl + spaceVehicleUrl, HttpMethod.GET, requestEntity, SpaceVehicles.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                spaceVehicleList.addAll(responseEntity.getBody().getSpaceVehicles());
            } else {
                throw new Exception("Space Vehicle query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
            
            if (!spaceVehicleList.isEmpty()) {                
                spaceVehicleList.forEach((spaceVehicle) -> {
                    spaceVehicleMap.put(spaceVehicle.getUuid(), spaceVehicle);
                });
                log.debug("spaceVehicleList.size() = " + spaceVehicleList.size() + "; spaceVehicleMap.size() = " + spaceVehicleMap.size());
            } else {
                throw new Exception("Space Vehicle list is empty.");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 

    @Override
    @Bean(name = "spaceVehicleMap")
    public Map<String, SpaceVehicle> getMap() throws Exception {
        if (this.spaceVehicleMap == null || this.spaceVehicleMap.isEmpty()) {
            generateMap();
        }
        return this.spaceVehicleMap;
    }
    
    @Override
    public void generateList() throws Exception {
        throw new Exception("Space Vehicle list is generated with the Map; use the generateMap method");
    }
    
    @Override
    @Bean(name = "spaceVehicleList")
    public List<SpaceVehicle> getList() throws Exception {
        if (this.spaceVehicleList == null || this.spaceVehicleList.isEmpty()) {
            generateMap();
        }
        return this.spaceVehicleList;
    }
}
