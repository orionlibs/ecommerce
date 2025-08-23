package com.hybris.cis.client.rest.subscription.config;

import com.hybris.cis.api.subscription.mock.impl.SubscriptionServiceMockImpl;
import com.hybris.cis.client.rest.common.tenant.StaticTenantRestCallInterceptor;
import com.hybris.cis.client.rest.subscription.oauth.OauthTokenRestCallInterceptor;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

@Configuration
@ComponentScan({"com.hybris.cis"})
public class SubscriptionConfiguration
{
    @Value("${subscription.client.endpoint.uri}")
    private String endpointUri;
    @Value("${security.oauth2.client.clientId}")
    private String clientId;
    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;
    @Value("${security.oauth2.client.username}")
    private String username;
    @Value("${security.oauth2.client.password:}")
    private String password;


    @Bean
    public HttpClientConnectionManager connectionManager()
    {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.create().register("https", sslConnectionSocketFactory()).build();
        return (HttpClientConnectionManager)new PoolingHttpClientConnectionManager(registry);
    }


    @Bean
    public SSLConnectionSocketFactory sslConnectionSocketFactory()
    {
        SSLConnectionSocketFactory sslsf;
        try
        {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(), (HostnameVerifier)NoopHostnameVerifier.INSTANCE);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalArgumentException(e);
        }
        return sslsf;
    }


    @Bean
    public HttpClient sbgHttpClient()
    {
        return (HttpClient)HttpClients.custom()
                        .setSSLSocketFactory((LayeredConnectionSocketFactory)sslConnectionSocketFactory())
                        .setConnectionManager(connectionManager())
                        .build();
    }


    @Bean
    public StaticTenantRestCallInterceptor cisStaticTenantInterceptor()
    {
        return new StaticTenantRestCallInterceptor();
    }


    @Bean
    public HttpComponentsClientHttpRequestFactory requestFactory()
    {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(sbgHttpClient());
        return requestFactory;
    }


    @Bean
    public RestTemplateBuilder defaultRestTemplateBuilder()
    {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder(new org.springframework.boot.web.client.RestTemplateCustomizer[0]);
        return restTemplateBuilder.requestFactory(this::requestFactory);
    }


    @Bean
    public ResourceOwnerPasswordResourceDetails resourceDetails()
    {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setClientId(this.clientId);
        resourceDetails.setClientSecret(this.clientSecret);
        resourceDetails.setUsername(this.username);
        resourceDetails.setPassword(this.password);
        resourceDetails.setAccessTokenUri(this.accessTokenUri);
        return resourceDetails;
    }


    @Bean
    public RestTemplateBuilder sbgRestTemplateBuilder()
    {
        return defaultRestTemplateBuilder()
                        .rootUri(getEndpointUri())
                        .interceptors(new ClientHttpRequestInterceptor[] {(ClientHttpRequestInterceptor)cisStaticTenantInterceptor(), (ClientHttpRequestInterceptor)oauthTokenRestCallInterceptor()});
    }


    @Bean
    public SubscriptionServiceMockImpl subscriptionServiceMock()
    {
        SubscriptionServiceMockImpl subscriptionServiceMock = new SubscriptionServiceMockImpl("mockPaymentMethods.xml", "mockBillings.xml");
        subscriptionServiceMock.setHpfUrl("http://mockedsopurl");
        return subscriptionServiceMock;
    }


    @Bean
    public OauthTokenRestCallInterceptor oauthTokenRestCallInterceptor()
    {
        return new OauthTokenRestCallInterceptor();
    }


    public String getEndpointUri()
    {
        return this.endpointUri;
    }


    public void setEndpointUri(String endpointUri)
    {
        this.endpointUri = endpointUri;
    }


    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }


    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }


    public void setAccessTokenUri(String accessTokenUri)
    {
        this.accessTokenUri = accessTokenUri;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }
}
