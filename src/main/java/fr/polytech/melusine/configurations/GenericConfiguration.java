package fr.polytech.melusine.configurations;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class GenericConfiguration {

    @Bean
    public PasswordService passwordService() {
        return new DefaultPasswordService();
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
