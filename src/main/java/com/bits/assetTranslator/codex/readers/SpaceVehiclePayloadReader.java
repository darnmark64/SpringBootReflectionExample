package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.SpaceVehiclePayload;
import com.bits.assetTranslator.codex.beans.SpaceVehiclePayloads;
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
public class SpaceVehiclePayloadReader extends AssetReader<SpaceVehiclePayload> {
    
    private final static String spaceVehiclePayloadUrl = "/codex/api/space-vehicle-payloads";
    private List<SpaceVehiclePayload> spaceVehiclePayloadList = new ArrayList<>();
    private Map<String, SpaceVehiclePayload> spaceVehiclePayloadMap = new HashMap<>();
    
    @Override
    public void generateMap() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of space vehicle payloads
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<SpaceVehiclePayloads> responseEntity = template.exchange(codexBaseUrl + spaceVehiclePayloadUrl, HttpMethod.GET, requestEntity, SpaceVehiclePayloads.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                spaceVehiclePayloadList.addAll(responseEntity.getBody().getSpaceVehiclePayloads());
            } else {
                throw new Exception("Space Vehicle Payload query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
            
            if (!spaceVehiclePayloadList.isEmpty()) {                
                spaceVehiclePayloadList.forEach((spaceVehiclePayload) -> {
                    spaceVehiclePayloadMap.put(spaceVehiclePayload.getUuid(), spaceVehiclePayload);
                });
                log.debug("spaceVehiclePayloadList.size() = " + spaceVehiclePayloadList.size() + "; spaceVehiclePayloadMap.size() = " + spaceVehiclePayloadMap.size());
            } else {
                throw new Exception("Space Vehicle Payload list is empty.");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 

    @Override
    @Bean("spaceVehiclePayloadMap")
    public Map<String, SpaceVehiclePayload> getMap() throws Exception {
        if (this.spaceVehiclePayloadMap == null || this.spaceVehiclePayloadMap.isEmpty()) {
            generateMap();
        }
        return this.spaceVehiclePayloadMap;
    }
    
    @Override
    public void generateList() throws Exception {
        throw new Exception("Space Vehicle Payload list is generated with the Map; use the generateMap method");
    }
    
    @Override
    @Bean("spaceVehiclePayloadList")
    public List<SpaceVehiclePayload> getList() throws Exception {
        if (this.spaceVehiclePayloadList == null || this.spaceVehiclePayloadList.isEmpty()) {
            generateMap();
        }
        return this.spaceVehiclePayloadList;
    }
}
