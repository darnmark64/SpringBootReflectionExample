package com.bits.assetTranslator;

import com.bits.assetTranslator.scope.beans.AssetEntity;
import com.bits.assetTranslator.util.ScopeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author darnellm
 */
@Service
public class ScopeAssetSender {

    private static final Logger log = LoggerFactory.getLogger(ScopeAssetSender.class);
    
    @Autowired
    @Qualifier("scopeCookie")
    private Map<String, String> scopeCookieMap;
    
    @Autowired
    @Qualifier("scopeUrl")
    private String scopeBaseUrl;
    
    private final String entityUrl = "/api/entity";
    
    public int sendAssetsToScope(List<AssetEntity> assetList) throws Exception {
        int assetsSent = 0;
        
        for (AssetEntity asset : assetList) {
            String json = convertAssetToJson(asset);

            if (json != null) {
                //try posting a the asset to scope
                RestTemplate template = new RestTemplate();            
                HttpEntity<String> requestEntity = new HttpEntity<>(json, ScopeUtils.getRequestHeaders(scopeCookieMap, true));
                ResponseEntity<String> responseEntity = template.exchange(scopeBaseUrl + entityUrl, HttpMethod.POST, requestEntity, String.class);
                log.debug("responseEntity = " + responseEntity);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    assetsSent++;
                } else {
                    throw new Exception("POST Asset Entity FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
                }
            }
        }
        
        return assetsSent;
    }
    
    private String convertAssetToJson(AssetEntity ae) {
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(ae);
        }  catch (JsonProcessingException jpe) {
            StringWriter sw = new StringWriter();
            jpe.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());                    
        }
        
        return json;
    }
}
