package com.hybris.datahub.rest.filter;

import com.hybris.datahub.log.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientResponse;
import org.slf4j.Logger;

public class FollowRedirectFilter implements ClientResponseFilter
{
    private static final Logger LOGGER = (Logger)Log.getLogger(FollowRedirectFilter.class);


    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext)
    {
        Invocation invocation;
        if(responseContext.getStatusInfo().getFamily() != Response.Status.Family.REDIRECTION)
        {
            return;
        }
        String location = responseContext.getLocation().toString();
        LOGGER.warn("\n*************************************************************************************\n* The server is sending a redirect response\n* from: {}\n*   to: {}\n* The application is going to follow the redirect but please update your endpoint url\n* accordingly since it affects the overall performance\n*************************************************************************************",
                        requestContext
                                        .getUri(), location);
        Object content = requestContext.getEntity();
        WebTarget target = requestContext.getClient().target(location);
        Invocation.Builder builder = target.request();
        builder.headers(requestContext.getHeaders());
        Objects.requireNonNull(builder);
        requestContext.getCookies().values().forEach(builder::cookie);
        requestContext.getPropertyNames().forEach(n -> builder.property(n, requestContext.getProperty(n)));
        Objects.requireNonNull(builder);
        requestContext.getAcceptableMediaTypes().forEach(xva$0 -> rec$.accept(new MediaType[] {xva$0}));
        Objects.requireNonNull(builder);
        requestContext.getAcceptableLanguages().forEach(xva$0 -> rec$.acceptLanguage(new Locale[] {xva$0}));
        if(content != null)
        {
            if(content instanceof InputStream)
            {
                try
                {
                    ((InputStream)content).reset();
                }
                catch(IOException e)
                {
                    LOGGER.error("Redirection was not possible because of payload type is InputStream an not resettable.", e);
                    return;
                }
            }
            Entity entity = Entity.entity(content, requestContext.getMediaType());
            invocation = builder.build(requestContext.getMethod(), entity);
        }
        else
        {
            invocation = builder.build(requestContext.getMethod());
        }
        Response resp = invocation.invoke();
        ClientResponse clientResponse = (ClientResponse)responseContext;
        clientResponse.getHeaders().clear();
        resp.getHeaders().forEach((k, v) -> {
            List<String> list = (List<String>)v.stream().map(Object::toString).collect(Collectors.toList());
            clientResponse.getHeaders().addAll(k, list);
        });
        responseContext.setEntityStream((InputStream)resp.getEntity());
        responseContext.setStatusInfo(resp.getStatusInfo());
        responseContext.setStatus(resp.getStatus());
    }
}
