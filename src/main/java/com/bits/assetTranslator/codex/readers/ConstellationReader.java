package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.Constellation;
import com.bits.assetTranslator.codex.beans.Constellations;
import com.bits.assetTranslator.util.CodexUtils;
import java.util.ArrayList;
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
public class ConstellationReader extends AssetReader<Constellation> {
    
    private final static String constellationUrl = "/codex/api/constellations";
    private List<Constellation> constellationList = new ArrayList<>();
    
    @Override
    public void generateMap() throws Exception {
        throw new Exception("Constellation is not a parent Asset; no lookup Map need be generated");
    }
    
    @Override
    @Bean(name = "constellationMap")
    public Map<String, Constellation> getMap() throws Exception {
        return null;
    }
    
    @Override
    public void generateList() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of organizations
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Constellations> responseEntity = template.exchange(codexBaseUrl + constellationUrl, HttpMethod.GET, requestEntity, Constellations.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                constellationList.addAll(responseEntity.getBody().getConstellations());
                log.debug(constellationList.size() + " constellations found");
            } else {
                throw new Exception("Constellation query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 
    
    @Override
    @Bean(name = "constellationList")
    public List<Constellation> getList() throws Exception {
        if (this.constellationList == null || this.constellationList.isEmpty()) {
            generateList();
        }
        return this.constellationList;
    }
}
