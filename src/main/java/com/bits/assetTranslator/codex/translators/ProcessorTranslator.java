package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.Processor;
import static com.bits.assetTranslator.codex.translators.Translator.log;
import com.bits.assetTranslator.scope.beans.AssetEntity;
import com.bits.assetTranslator.scope.beans.EntityClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProcessorTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Processor";
    
    @Autowired
    @Qualifier("processorList")
    List<Processor> processorList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (processorList != null && !processorList.isEmpty()) {
            log.info("Translating " + processorList.size() + " CODEX Processor Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass processorEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(processorEntityClass);
                processorList.forEach((Processor processor) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(processor);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(processorEntityClass.getAttributeKeys(), Processor.class, processor, ProcessorTranslator.class, ProcessorTranslator.this);

                        assetEntity.setAttributes(attributeMap);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    scopeAssetList.add(assetEntity);
                }); 
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            return scopeAssetList;
        } else {
            throw new TranslationException("There are no CODEX Processors to translate");
        }
    }    
}
