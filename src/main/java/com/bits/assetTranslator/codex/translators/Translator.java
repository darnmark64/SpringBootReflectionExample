package com.bits.assetTranslator.codex.translators;

import com.bits.assetTranslator.codex.beans.Asset;
import com.bits.assetTranslator.codex.beans.Capability;
import com.bits.assetTranslator.codex.beans.GroundSegment;
import com.bits.assetTranslator.codex.beans.Organization;
import com.bits.assetTranslator.codex.beans.SpaceMission;
import com.bits.assetTranslator.codex.beans.SpaceVehicle;
import com.bits.assetTranslator.codex.beans.SpaceVehiclePayload;
import com.bits.assetTranslator.scope.beans.AssetEntity;
import com.bits.assetTranslator.scope.beans.EntityClass;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class Translator implements TranslatorInterface {

    static final Logger log = LoggerFactory.getLogger(Translator.class);
    
    @Autowired
    ScopeAttributeMapper scopeAttributeMap;
    
    @Autowired
    @Qualifier("scopeEntityClasses")
    Map<String, EntityClass> scopeEntityClasses;
    
    @Autowired
    @Qualifier("codexCapabilities")
    private Map<String, Capability> codexCapabilities;
    
    String flattenCapabilities(List<String> capabilityUuidList) {
        StringBuilder sb = new StringBuilder();
        int size = capabilityUuidList.size();
        int count = 0;
        for (String uuid : capabilityUuidList) {
            Capability cap = this.codexCapabilities.get(uuid);
            String capability;
            if (cap != null) {
                capability = cap.getFullName();
            } else {
                log.warn("Unable to find Capability with UUID of " + uuid);
                capability = uuid;
            }
            if (count < (size - 1)) {
                sb.append(capability + ", ");
            } else {
                sb.append(capability);
            }
            count++;
        }
        
        return sb.toString();
    }
    
    @Autowired
    @Qualifier("groundSegmentMap")
    private Map<String, GroundSegment> groundSegmentMap;
    
    public String flattenGroundSegment(String groundSegmentUuid) {
        if (this.groundSegmentMap != null && !this.groundSegmentMap.isEmpty()) {
            GroundSegment gs = this.groundSegmentMap.get(groundSegmentUuid);
            if (gs != null) {
                return gs.getFullName();
            } else {
                log.warn("Unable to find Ground Segment with UUID of " + groundSegmentUuid);
                return null;
            }
        } else {
            return null;
        }
    }
    
    @Autowired
    @Qualifier("organizationMap")
    private Map<String, Organization> organizationMap;
    
    public String flattenOrganization(String organizationUuid) {
        if (this.organizationMap != null && !this.organizationMap.isEmpty()) {
            Organization org = this.organizationMap.get(organizationUuid);
            if (org != null) {
                return org.getFullName();
            } else {
                log.warn("Unable to find Organization with UUID of " + organizationUuid);
                return null;
            }
        } else {
            return null;
        }
    }
    
    @Autowired
    @Qualifier("spaceMissionMap")
    private Map<String, SpaceMission> spaceMissionMap;
    
    public String flattenSpaceMission(String spaceMissionUuid) {
        if (this.spaceMissionMap != null && !this.spaceMissionMap.isEmpty()) {
            SpaceMission sm = this.spaceMissionMap.get(spaceMissionUuid);
            if (sm != null) {
                return sm.getFullName();
            } else {
                log.warn("Unable to find Space Mission with UUID of " + spaceMissionUuid);
                return null;
            }
        } else {
            return null;
        }
    }
    
    @Autowired
    @Qualifier("spaceVehicleMap")
    private Map<String, SpaceVehicle> spaceVehicleMap;
    
    public String flattenSpaceVehicle(String spaceVehicleUuid) {
        if (this.spaceVehicleMap != null && !this.spaceVehicleMap.isEmpty()) {
            SpaceVehicle sv = this.spaceVehicleMap.get(spaceVehicleUuid);
            if (sv != null) {
                return sv.getFullName();
            } else {
                log.warn("Unable to find Space Vehicle with UUID of " + spaceVehicleUuid);
                return null;
            }
        } else {
            return null;
        }
    }
    
    @Autowired
    @Qualifier("spaceVehiclePayloadMap")
    private Map<String, SpaceVehiclePayload> spaceVehiclePayloadMap;
    
    public String flattenSpaceVehiclePayloads(List<String> spaceVehiclePayloadUuidList) {
        if (this.spaceVehiclePayloadMap != null && !this.spaceVehiclePayloadMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int size = spaceVehiclePayloadUuidList.size();
            int count = 0;
            for (String uuid : spaceVehiclePayloadUuidList) {
                SpaceVehiclePayload svp = this.spaceVehiclePayloadMap.get(uuid);
                String payload;
                if (svp != null) {
                    payload = svp.getFullName();
                } else {
                    log.warn("Unable to find Space Vehicle Payload with UUID of " + uuid);
                    payload = uuid;
                }
                if (count < (size - 1)) {
                    sb.append(payload + ", ");
                } else {
                    sb.append(payload);
                }
                count++;
            }

            return sb.toString();
        } else {
            return null;
        }
    }
    
    <T extends Asset> AssetEntity populateBaseScopeEntity(T asset) {
        AssetEntity assetEntity = new AssetEntity();
        if (asset.getShortName() == null || asset.getShortName().isEmpty()) {
            assetEntity.setName(asset.getFullName());
        } else {
            assetEntity.setName(asset.getShortName());
        }
        assetEntity.setDescription(asset.getFullName());
        assetEntity.setOwner(asset.getCountryTrigraph());
        if (asset.capabilityUuidsSet()) {
            assetEntity.setCapabilities(flattenCapabilities(asset.getCapabilityUuids()));
        }
        
        return assetEntity;
    }
    
    Map<String, String> populateScopeAttributes(List<String> attributeKeys, Class objClass, Object object, 
                                                Class transClass, Object translator) 
                                                       throws Exception {
        Map<String, String> attributeMap = new HashMap<>();
        attributeKeys.forEach((attribute) -> {
            String methodName = scopeAttributeMap.getAssetMethodName(attribute);
            if (methodName != null && !methodName.isEmpty()) {
                try {
                    Class noparams[] = {};
                    Method method = objClass.getMethod(methodName, noparams);
                    Object obj = method.invoke(object, null);
                    if (obj != null) {
                        //check if object needs to be flatten for scope
                        ScopeAttributeMapper.UtilityMethod utilityMethod = scopeAttributeMap.getUtilityMethod(attribute);
                        if (utilityMethod != null) {
                            //assume first object to pass is the data value just found
                            int size = (utilityMethod.areParametersSet() ? utilityMethod.getParameters().size() + 1 : 1);
                            Class utilityParamClasses[] = new Class[size];
                            utilityParamClasses[0] = String.class;
                            Object parameterObjects[] = new Object[size];
                            parameterObjects[0] = obj.toString();
                            if (utilityMethod.areParametersSet()) {
                                int i = 1;
                                for (Class c : utilityMethod.getParameters().values()) {
                                    utilityParamClasses[i] = c;
                                    i++;
                                }
                                Field utilityFields[] = new Field[size - 1];
                                i = 0;
                                for (String fieldName : utilityMethod.getParameters().keySet()) {
                                    utilityFields[i] = transClass.getField(fieldName);
                                    i++;
                                }
                                i = 1;
                                for (Field utilityField : utilityFields) {
                                    parameterObjects[i] = utilityField.get(translator);
                                    i++;
                                }
                            }
                            Method utilMethod = Translator.class.getMethod(utilityMethod.getName(), utilityParamClasses);
                            Object obj1 = utilMethod.invoke(Translator.this, parameterObjects);
                            if (obj1 != null) obj = obj1;
                        }
                    }
                    attributeMap.put(attribute, (obj != null ? obj.toString() : ""));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException nsme) {
                    throw new RuntimeException(nsme);
                }
            }
        });
        
        return attributeMap;
    }
}
