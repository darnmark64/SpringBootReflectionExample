package com.bits.assetTranslator.codex.readers;

import com.bits.assetTranslator.codex.beans.Processor;
import com.bits.assetTranslator.codex.beans.Processors;
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
public class ProcessorReader extends AssetReader<Processor> {
    
    private final static String processorUrl = "/codex/api/processors";
    private List<Processor> processorList = new ArrayList<>();
    
    @Override
    public void generateMap() throws Exception {
        throw new Exception("Processor is not a parent Asset; no lookup Map need be generated");
    }
    
    @Override
    @Bean(name = "processorMap")
    public Map<String, Processor> getMap() throws Exception {
        return null;
    }
    
    @Override
    public void generateList() throws Exception {
        if (codexLoginToken != null && codexBaseUrl != null) {
            //try getting a list of ground assets
            HttpHeaders headers = CodexUtils.getHeaders(codexLoginToken);
            RestTemplate template = new RestTemplate();            
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Processors> responseEntity = template.exchange(codexBaseUrl + processorUrl, HttpMethod.GET, requestEntity, Processors.class);
            log.debug("responseEntity = " + responseEntity);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                processorList.addAll(responseEntity.getBody().getProcessors());
                log.debug(processorList.size() + " processors found");
            } else {
                throw new Exception("Processor query FAILED with response reason (code): " + responseEntity.getStatusCode().getReasonPhrase() + " (" + responseEntity.getStatusCodeValue() + ")");
            }
        } else {
            throw new Exception("Either the CODEX login token is missing or the CODEX base URL is null");
        }
    } 
    
    @Override
    @Bean(name = "processorList")
    public List<Processor> getList() throws Exception {
        if (this.processorList == null || this.processorList.isEmpty()) {
            generateList();
        }
        return this.processorList;
    }
}
