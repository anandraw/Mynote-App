package org.anand.mynoteapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openApi = new OpenAPI();

        Info info = new Info();
        info.setTitle("Enotes API");
        info.setDescription("Enotes Api");
        info.setVersion("1.0.0");
        info.setTermsOfService("http://codewithanand.com");
        info.setContact(
                new Contact().email("anandrawool5624@gmail.com").name("Anand Rawool").url("http://codewithanand.com/contact"));
        info.setLicense(new License().name("Enotes 1.0").url("http://codewithanand.com"));

        List<Server> serverList = List.of(new Server().description("Dev").url("http://localhost:9090"),
                new Server().description("Test").url("http://localhost:9091"),
                new Server().description("Prod").url("http://localhost:9093"));

        // bearer sdbhkj.sdfdvs.sdfvsdc
        SecurityScheme securityScheme = new SecurityScheme().name("Authorization").scheme("bearer").type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT").in(SecurityScheme.In.HEADER.HEADER);

        Components component = new Components().addSecuritySchemes("Token", securityScheme);
        openApi.setServers(serverList);
        openApi.setInfo(info);
        openApi.setComponents(component);
        openApi.setSecurity(List.of(new SecurityRequirement().addList("Token")));
        return openApi;
    }
}
