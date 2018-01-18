package com.bits.assetTranslator.codex.translators;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ScopeAttributeMapper {
    Map<String, Methods> attributeMap = new HashMap<>();
    
    public ScopeAttributeMapper() {
        attributeMap.put("CODEX UUID", new Methods("getUuid", null));
        UtilityMethod gsMethod = new UtilityMethod();
        gsMethod.setName("flattenGroundSegment");
        attributeMap.put("Ground Segment", new Methods("getParentUuid", gsMethod));
        attributeMap.put("Asset ID", new Methods("getAssetId", null));
        attributeMap.put("Classification", new Methods("getClassification", null));
        attributeMap.put("Aliases", new Methods("getAliasesAsString", null));
        attributeMap.put("Status Reported", new Methods("isStatusReported", null));
        attributeMap.put("More Info Link", new Methods("getMoreInfoLink", null));
        attributeMap.put("Alignment", new Methods("getAlignment", null));
        UtilityMethod orgMethod = new UtilityMethod();
        orgMethod.setName("flattenOrganization");
        attributeMap.put("Organization", new Methods("getParentUuid", orgMethod));
        attributeMap.put("Parent Organization", new Methods("getParentUuid", orgMethod));
        attributeMap.put("Description", new Methods("getDescription", null));
        UtilityMethod smMethod = new UtilityMethod();
        smMethod.setName("flattenSpaceMission");
        attributeMap.put("Mission", new Methods("getMissionUuid", smMethod));
        UtilityMethod svMethod = new UtilityMethod();
        svMethod.setName("flattenSpaceVehicle");
        attributeMap.put("Vehicle", new Methods("getSpaceVehicleUuid", svMethod));
        attributeMap.put("SSC Number", new Methods("getSscNumber", null));
        attributeMap.put("Vehicle Group Name", new Methods("getVehicleGroupName", null));
        attributeMap.put("Orbit", new Methods("getOrbit", null));
        attributeMap.put("Series Name", new Methods("getSeriesName", null));
    }
    
    public String getAssetMethodName (String attribute) {
        Methods method = attributeMap.get(attribute);
        return (method != null ? method.getAssetMethodName() : null);
    }
    
    public UtilityMethod getUtilityMethod(String attribute) {
        Methods method = attributeMap.get(attribute);
        return (method != null ? method.getUtilityMethod() : null);
    }
    
    public class Methods {
        private String assetMethodName;
        private UtilityMethod utilityMethod;
        
        public Methods(String name, UtilityMethod utility) {
            this.assetMethodName = name;
            this.utilityMethod = utility;
        }

        public String getAssetMethodName() {
            return assetMethodName;
        }

        public void setAssetMethodName(String assetMethodName) {
            this.assetMethodName = assetMethodName;
        }

        public UtilityMethod getUtilityMethod() {
            return utilityMethod;
        }

        public void setUtilityMethods(UtilityMethod utilityMethod) {
            this.utilityMethod = utilityMethod;
        }
    }
    
    public class UtilityMethod {
        private String name;
        private Map<String, Class> parameters;
        
        public UtilityMethod() {
            
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Class> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Class> parameters) {
            if (this.parameters == null) this.parameters = new LinkedHashMap<>();
            this.parameters.putAll(parameters);
        }
        
        public boolean areParametersSet() {
            return !(this.parameters == null || this.parameters.isEmpty());
        }
    }
}
