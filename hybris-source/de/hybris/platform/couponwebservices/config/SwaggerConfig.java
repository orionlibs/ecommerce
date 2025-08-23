package de.hybris.platform.couponwebservices.config;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Component
public class SwaggerConfig
{
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;


    @Bean
    public Docket apiDocumentation()
    {
        return (new Docket(DocumentationType.SWAGGER_2))
                        .apiInfo(apiInfo())
                        .select()
                        .paths(PathSelectors.any())
                        .build()
                        .securitySchemes(Arrays.asList(new OAuth[] {clientCredentialFlow(), passwordFlow()})).securityContexts(Arrays.asList(new SecurityContext[] {oauthSecurityContext()}));
    }


    protected ApiInfo apiInfo()
    {
        return (new ApiInfoBuilder())
                        .title(getPropertyValue("couponwebservices.documentation.title"))
                        .description(getPropertyValue("couponwebservices.documentation.desc"))
                        .termsOfServiceUrl(getPropertyValue("couponwebservices.terms.of.service.url"))
                        .license(getPropertyValue("couponwebservices.licence"))
                        .licenseUrl(getPropertyValue("couponwebservices.license.url"))
                        .version("1.0.0")
                        .build();
    }


    protected OAuth passwordFlow()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope(getPropertyValue("couponwebservices.oauth.scope"), "");
        ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant("/authorizationserver/oauth/token");
        return new OAuth("oauth2_password", Arrays.asList(new AuthorizationScope[] {authorizationScope}, ), Arrays.asList(new GrantType[] {(GrantType)resourceOwnerPasswordCredentialsGrant}));
    }


    protected OAuth clientCredentialFlow()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope(getPropertyValue("couponwebservices.oauth.scope"), "");
        ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant("/authorizationserver/oauth/token");
        return new OAuth("oauth2_client_credentials", Arrays.asList(new AuthorizationScope[] {authorizationScope}, ), Arrays.asList(new GrantType[] {(GrantType)clientCredentialsGrant}));
    }


    protected String getPropertyValue(String propertyName)
    {
        return this.configurationService.getConfiguration().getString(propertyName);
    }


    protected SecurityContext oauthSecurityContext()
    {
        return SecurityContext.builder().securityReferences(oauthSecurityReferences()).forPaths(PathSelectors.any()).build();
    }


    protected List<SecurityReference> oauthSecurityReferences()
    {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[0];
        return Arrays.asList(new SecurityReference[] {new SecurityReference("oauth2_password", authorizationScopes), new SecurityReference("oauth2_client_credentials", authorizationScopes)});
    }
}
