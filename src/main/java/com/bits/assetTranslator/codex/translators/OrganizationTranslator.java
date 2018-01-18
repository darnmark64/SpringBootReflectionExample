package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.Organization;
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
public class OrganizationTranslator extends Translator {
    
    private final static String scopeEntityClassName = "Organization";
    
    @Autowired
    @Qualifier("organizationList")
    private List<Organization> organizationList;

    @Override
    public List<AssetEntity> translate() throws TranslationException, RuntimeException {
        if (scopeEntityClasses == null || scopeEntityClasses.isEmpty()) {
            throw new TranslationException("SCOPE Entity Class map is not available; cannot translate without it");
        }
        
        if (organizationList != null && !organizationList.isEmpty()) {
            log.info("Translating " + organizationList.size() + " CODEX Organization Assets to SCOPE Asset Entities.");
            List<AssetEntity> scopeAssetList = new ArrayList<>();
            try {
                EntityClass organizationEntityClass = scopeEntityClasses.get(scopeEntityClassName);
                List<EntityClass> entityClassList = new ArrayList<>();
                entityClassList.add(organizationEntityClass);
                organizationList.forEach((Organization organization) -> {
                    AssetEntity assetEntity = populateBaseScopeEntity(organization);
                    assetEntity.setClasses(entityClassList);
                    try {
                        Map<String, String> attributeMap = populateScopeAttributes(organizationEntityClass.getAttributeKeys(), Organization.class, organization, OrganizationTranslator.class, OrganizationTranslator.this);

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
            throw new TranslationException("There are no CODEX Organizations to translate");
        }
    }
}
