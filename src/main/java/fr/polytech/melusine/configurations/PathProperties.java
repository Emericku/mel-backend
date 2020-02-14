package fr.polytech.melusine.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "melusine.path")
public class PathProperties {

    String base;

}
