package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.Constellation;
import com.bits.assetTranslator.scope.beans.AssetEntity;
import com.bits.assetTranslator.scope.beans.EntityClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ConstellationTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Constellation";
    
    @Autowired
    @Qualifier("constellationList")
    List<Constellation> constellationList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (constellationList != null && !constellationList.isEmpty()) {
            log.info("Translating " + constellationList.size() + " CODEX Constellation Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass constellationEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(constellationEntityClass);
                constellationList.forEach((Constellation constellation) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(constellation);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(constellationEntityClass.getAttributeKeys(), Constellation.class, constellation, ConstellationTranslator.class, ConstellationTranslator.this);

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
            throw new TranslationException("There are no CODEX Constellations to translate");
        }
    }
}
