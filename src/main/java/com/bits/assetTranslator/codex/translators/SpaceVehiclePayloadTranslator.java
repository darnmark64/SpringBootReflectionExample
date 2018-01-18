package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.SpaceVehiclePayload;
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
public class SpaceVehiclePayloadTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Space Vehicle Payload";
    
    @Autowired
    @Qualifier("spaceVehiclePayloadList")
    private List<SpaceVehiclePayload> spaceVehiclePayloadList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (spaceVehiclePayloadList != null && !spaceVehiclePayloadList.isEmpty()) {
            log.info("Translating " + spaceVehiclePayloadList.size() + " CODEX Space Vehicle Payload Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass spaceVehiclePayloadEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(spaceVehiclePayloadEntityClass);
                spaceVehiclePayloadList.forEach((SpaceVehiclePayload spaceVehiclePayload) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(spaceVehiclePayload);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(spaceVehiclePayloadEntityClass.getAttributeKeys(), SpaceVehiclePayload.class, spaceVehiclePayload, SpaceVehiclePayloadTranslator.class, SpaceVehiclePayloadTranslator.this);

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
            throw new TranslationException("There are no CODEX Space Vehicle Payloads to translate");
        }
    }
}
