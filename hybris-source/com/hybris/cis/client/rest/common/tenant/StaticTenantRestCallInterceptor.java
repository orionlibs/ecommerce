package com.hybris.cis.client.rest.common.tenant;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class StaticTenantRestCallInterceptor implements ClientHttpRequestInterceptor
{
    public static final String TENANT_HEADER = "X-tenantId";
    @Value("${cis.defaultTenant:single}")
    private String headerValue;


    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        if(!request.getHeaders().containsKey("X-tenantId"))
        {
            request.getHeaders().set("X-tenantId", this.headerValue);
        }
        return execution.execute(request, body);
    }


    protected String getHeaderValue()
    {
        return this.headerValue;
    }


    @Required
    public void setHeaderValue(String headerValue)
    {
        this.headerValue = headerValue;
    }
}
