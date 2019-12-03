package fr.polytech.melusine.configurations;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private String host = "localhost:8080";
    private String basePath = "";

    @SuppressWarnings("Guava")
    @Bean
    public Docket api(ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(Collections.singletonList(new ApiKey("Authorization", "Access token", "header")))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                // Configure path for Swagger UI API calls
                .host(host)
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        return basePath;
                    }
                });
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Melusine",
                "Melusine Backend API.",
                "",
                "",
                new Contact("Emeric Hoerner", "", "hoerner.emeric@gmail.com"),
                "",
                "",
                Collections.emptyList());
    }

}
