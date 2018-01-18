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
public class Credentials {
    
    @JsonProperty("user")
    private String user;
    @JsonProperty("pass")
    private String pass;
    @JsonProperty("credential")
    private String credential;

    @Override
    public String toString() {
        return "Credentials {" + "user:'" + user + "', pass:'" + pass + "', credential:'" + credential + "'}";
    }
}
