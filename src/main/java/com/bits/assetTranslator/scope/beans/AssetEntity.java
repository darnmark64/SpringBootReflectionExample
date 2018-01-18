package com.bits.assetTranslator.scope.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
public class AssetEntity extends Entity {

    @JsonProperty("type")
    private final String type = "Asset";

    @Override
    public String toString() {
        return "AssetEntity{" + "id:'" + getId() + "', name:'" + getName() + "', description:'" + getDescription() + "', owner:'" + getOwner() + "',"
                + " type:'" + getType() + "' capabilities:'" + getCapabilities() + "', vulnerabilities:'" + getVulnerabilities() + "',"
                + " susceptibilities:'" + getSusceptibilities() + "', classes:" + getClasses() + ", attributes:" + getAttributes() + "}";
    }
}
