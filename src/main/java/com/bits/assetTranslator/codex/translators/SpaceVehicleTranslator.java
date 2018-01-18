package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.SpaceVehicle;
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
public class SpaceVehicleTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Space Vehicle";
    
    @Autowired
    @Qualifier("spaceVehicleList")
    private List<SpaceVehicle> spaceVehicleList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (spaceVehicleList != null && !spaceVehicleList.isEmpty()) {
            log.info("Translating " + spaceVehicleList.size() + " CODEX Space Vehicle Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass spaceVehicleEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(spaceVehicleEntityClass);
                spaceVehicleList.forEach((SpaceVehicle spaceVehicle) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(spaceVehicle);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(spaceVehicleEntityClass.getAttributeKeys(), SpaceVehicle.class, spaceVehicle, SpaceVehicleTranslator.class, SpaceVehicleTranslator.this);

                        if (spaceVehicle.payloadUuidsSet()) {
                            String payloads = flattenSpaceVehiclePayloads(spaceVehicle.getPayloadUuids());
                            if (payloads != null && !payloads.isEmpty()) attributeMap.put("Payloads", payloads);
                        }

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
            throw new TranslationException("There are no CODEX Space Vehicles to translate");
        }
    }
}
