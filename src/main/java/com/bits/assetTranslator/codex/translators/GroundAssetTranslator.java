package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.GroundAsset;
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
public class GroundAssetTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Ground Asset";
    
    @Autowired
    @Qualifier("groundAssetList")
    List<GroundAsset> groundAssetList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (groundAssetList != null && !groundAssetList.isEmpty()) {
            log.info("Translating " + groundAssetList.size() + " CODEX Ground Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass groundAssetEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(groundAssetEntityClass);
                groundAssetList.forEach((GroundAsset groundAsset) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(groundAsset);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(groundAssetEntityClass.getAttributeKeys(), GroundAsset.class, groundAsset, GroundAssetTranslator.class, GroundAssetTranslator.this);

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
            throw new TranslationException("There are no CODEX Ground Assets to translate");
        }
    }
}
