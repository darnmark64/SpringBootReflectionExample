package com.bits.assetTranslator.codex.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class LoginToken {
    
    @JsonProperty("token")
    private String token;
    @JsonProperty("expires")
    private long expires;
    @JsonProperty("userUuid")
    private String userUuid;

    @Override
    public String toString() {
        return "LoginToken {" + "token:'" + token + "', expires:" + expires + ", userUuid:'" + userUuid + "'}";
    }
}
