package fr.polytech.melusine.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.xml.bind.DatatypeConverter;

@Data
@ConfigurationProperties(prefix = "melusine.jwt")
public class JwtProperties {

    private String issuer;

    private byte[] signKey;

    private String headerName;

    private int timeToLive;

    public byte[] getSignKey() {
        return signKey.clone();
    }

    public void setSignKey(String signKey) {
        this.signKey = DatatypeConverter.parseBase64Binary(signKey);
    }

}