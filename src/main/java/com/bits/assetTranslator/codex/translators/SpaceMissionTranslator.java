package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.SpaceMission;
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
public class SpaceMissionTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Space Mission";
    
    @Autowired
    @Qualifier("spaceMissionList")
    private List<SpaceMission> spaceMissionList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (spaceMissionList != null && !spaceMissionList.isEmpty()) {
            log.info("Translating " + spaceMissionList.size() + " CODEX Space Mission Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass spaceMissionEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(spaceMissionEntityClass);
                spaceMissionList.forEach((SpaceMission spaceMission) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(spaceMission);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(spaceMissionEntityClass.getAttributeKeys(), SpaceMission.class, spaceMission, SpaceMissionTranslator.class, SpaceMissionTranslator.this);

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
            throw new TranslationException("There are no CODEX Space Missions to translate");
        }
    }
}
