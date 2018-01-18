package com.bits.assetTranslator.codex.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class SpaceVehicle extends Asset {

/*
{"uuid": "string", "parentUuid": "string", "assetId": "string", "classification": "string",
 "shortName": "string", "fullName": "string", "aliases": ["string"],
 "statusReported": true, "moreInfoLink": "string", "countryTrigraph": "string",
 "alignment": "Blue", "assetType": "GroundAsset", "capabilityUuids": ["string"],
 "sscNumber": "string", "vehicleGroupName": "string", "orbit": "LOW_EARTH_ORBIT",
 "payloadUuids": ["string"]
}
*/
    @JsonProperty("sscNumber")
    private String sscNumber;
    @JsonProperty("vehicleGroupName")
    private String vehicleGroupName;
    @JsonProperty("orbit")
    private String orbit;
    @JsonProperty("payloadUuids")
    private List<String> payloadUuids;

    public List<String> getPayloadUuids() {
        synchronized (this) {
            if (payloadUuids == null) {
                payloadUuids = new ArrayList();
            }
        }
        return payloadUuids;
    }

    public boolean payloadUuidsSet() {
        return payloadUuids != null;
    }

    @Override
    public String toString() {
        return "SpaceVehicle {uuid:'" + getUuid() + "', parentUuid:'" + getParentUuid() + "', assetId:'" + getAssetId() + "', "
                + "classification:'" + getClassification() + "', shortName:'" + getShortName() + "', fullName:'" + getFullName() + "', "
                + "aliases:" + getAliases() + ", statusReported:" + isStatusReported() + ", moreInfoLink:'" + getMoreInfoLink() + "', "
                + "countryTrigraph:'" + getCountryTrigraph() + "', alignment:'" + getAlignment() + "', assetType:'" + getAssetType() + "', "
                + "capabilityUuids:" + getCapabilityUuids() + ", sscNumber:" + getSscNumber() + ", vehicleGroupName:'" + getVehicleGroupName() + "', "
                + "orbit:'" + getOrbit() + "', payloadUuids:" + getPayloadUuids() + "}";
    }
}
