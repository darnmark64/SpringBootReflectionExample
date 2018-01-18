package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.GroundSegment;
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
public class GroundSegmentTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Ground Segment";
    
    @Autowired
    @Qualifier("groundSegmentList")
    private List<GroundSegment> groundSegmentList;
    
    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (groundSegmentList != null && !groundSegmentList.isEmpty()) {
            log.info("Translating " + groundSegmentList.size() + " CODEX Ground Segment Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass groundSegmentEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(groundSegmentEntityClass);
                groundSegmentList.forEach((GroundSegment groundSegment) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(groundSegment);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(groundSegmentEntityClass.getAttributeKeys(), GroundSegment.class, groundSegment, GroundSegmentTranslator.class, GroundSegmentTranslator.this);

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
            throw new TranslationException("There are no CODEX Ground Segments to translate");
        }
    }
}
