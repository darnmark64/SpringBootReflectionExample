package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.GroundSegment;
import com.bits.assetTranslator.codex.beans.GroundSegments;
import com.bits.assetTranslator.util.CodexUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GroundSegmentReader extends AssetReader<GroundSegment> {
    
    private final static String groundSegmentUrl = "/codex/api/ground-segments";
    private List<GroundSegment> groundSegmentList = new ArrayList<>();
    
    private Map<String, GroundSegment> groundSegmentMap = new HashMap<>();
    
    @Override
    public void generateMap() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of organizations
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<GroundSegments> responseEntity = template.exchange(codexBaseUrl + groundSegmentUrl, HttpMethod.GET, requestEntity, GroundSegments.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                groundSegmentList.addAll(responseEntity.getBody().getGroundSegments());
            } else {
                throw new Exception("Ground Segment query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
            
            if (!groundSegmentList.isEmpty()) {                
                groundSegmentList.forEach((groundSegment) -> {
                    groundSegmentMap.put(groundSegment.getUuid(), groundSegment);
                });
                log.debug("groundSegmentList.size() = " + groundSegmentList.size() + "; groundSegmentMap.size() = " + groundSegmentMap.size());
            } else {
                throw new Exception("Ground Segment list is empty.");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 

    @Override
    @Bean(name = "groundSegmentMap")
    public Map<String, GroundSegment> getMap() throws Exception {
        if (this.groundSegmentMap == null || this.groundSegmentMap.isEmpty()) {
            generateMap();
        }
        return this.groundSegmentMap;
    }
    
    @Override
    public void generateList() throws Exception {
        throw new Exception("Ground Segment list is generated with the Map; use the generateMap method");
    }
    
    @Override
    @Bean(name = "groundSegmentList")
    public List<GroundSegment> getList() throws Exception {
        if (this.groundSegmentList == null || this.groundSegmentList.isEmpty()) {
            generateMap();
        }
        return this.groundSegmentList;
    }
}
