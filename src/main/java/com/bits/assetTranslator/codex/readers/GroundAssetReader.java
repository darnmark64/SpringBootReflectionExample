package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.GroundAsset;
import com.bits.assetTranslator.codex.beans.GroundAssets;
import com.bits.assetTranslator.util.CodexUtils;
import java.util.ArrayList;
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
public class GroundAssetReader extends AssetReader<GroundAsset> {
    
    private final static String groundAssetUrl = "/codex/api/ground-assets";
    private List<GroundAsset> groundAssetList = new ArrayList<>();
    
    @Override
    public void generateMap() throws Exception {
        throw new Exception("Ground Asset is not a parent Asset; no lookup Map need be generated");
    }
    
    @Override
    @Bean(name = "groundAssetMap")
    public Map<String, GroundAsset> getMap() throws Exception {
        return null;
    }
    
    @Override
    public void generateList() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of ground assets
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<GroundAssets> responseEntity = template.exchange(codexBaseUrl + groundAssetUrl, HttpMethod.GET, requestEntity, GroundAssets.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                groundAssetList.addAll(responseEntity.getBody().getGroundAssets());
                log.debug(groundAssetList.size() + " ground assets found");
            } else {
                throw new Exception("Ground Asset query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 
    
    @Override
    @Bean(name = "groundAssetList")
    public List<GroundAsset> getList() throws Exception {
        if (this.groundAssetList == null || this.groundAssetList.isEmpty()) {
            generateList();
        }
        return this.groundAssetList;
    }
}
