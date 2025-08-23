package com.hybris.datahub.client;

import com.hybris.datahub.DataHubServerResponse;
import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.item.ResultData;
import com.hybris.datahub.log.Log;
import com.hybris.datahub.validation.ValidationFailure;
import java.util.List;
import java.util.Map;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;

public abstract class AbstractJerseyRestClient
{
    private final Logger logger = (Logger)Log.getLogger(getClass());
    private DataHubClientProvider dataHubClientProvider = (DataHubClientProvider)new DefaultDataHubClientProvider();
    private Client client;


    public Invocation.Builder resource(String uri)
    {
        String resourceUrl = getBaseApiUrl() + getBaseApiUrl();
        this.logger.debug("Created resource for {}", resourceUrl);
        return client().target(resourceUrl).request();
    }


    public Invocation.Builder resource(String uri, MultivaluedMap<String, String> queryParams)
    {
        if(queryParams == null)
        {
            throw new IllegalArgumentException("queryParams must not be null");
        }
        String resourceUrl = getBaseApiUrl() + getBaseApiUrl();
        this.logger.debug("Created resource for {}", resourceUrl);
        WebTarget target = client().target(resourceUrl);
        for(Map.Entry<String, List<String>> param : (Iterable<Map.Entry<String, List<String>>>)queryParams.entrySet())
        {
            target = target.queryParam(param.getKey(), ((List)param.getValue()).toArray());
        }
        return target.request();
    }


    public <T> T get(String uri, Class<T> resType)
    {
        return get(uri, getMediaType(), resType);
    }


    public <T> T get(String uri, MediaType dataFormat, Class<T> resType)
    {
        try
        {
            return (T)resource(uri).accept(new MediaType[] {dataFormat}).get(resType);
        }
        catch(ClientErrorException e)
        {
            throw new DataHubClientException(e.getResponse().getStatusInfo().getReasonPhrase(), e);
        }
    }


    public <T> List<T> get(String uri, GenericType<List<T>> type)
    {
        return get(uri, getMediaType(), type);
    }


    public <T> List<T> get(String uri, GenericType<List<T>> type, MultivaluedMap<String, String> queryParams)
    {
        return get(uri, getMediaType(), type, queryParams);
    }


    public <T> PagedDataHubResponse<T> getPaged(String uri, GenericType<List<T>> type, MultivaluedMap<String, String> queryParams)
    {
        DataHubServerResponse<List<T>> response = getResponse(uri, type, queryParams);
        return new PagedDataHubResponse(response.getTotalCount(), (List)response.getEntity());
    }


    public <T> DataHubServerResponse<List<T>> getResponse(String uri, GenericType<List<T>> type, MultivaluedMap<String, String> queryParams)
    {
        return new DataHubServerResponse(resource(uri, queryParams).accept(new MediaType[] {getMediaType()}).get(), type);
    }


    public DataHubServerResponse getResponse(String uri)
    {
        return new DataHubServerResponse(resource(uri).accept(new MediaType[] {getMediaType()}).get());
    }


    public <T> List<T> get(String uri, MediaType dataFormat, GenericType<List<T>> type)
    {
        try
        {
            return (List<T>)resource(uri).accept(new MediaType[] {dataFormat}).get(type);
        }
        catch(ClientErrorException e)
        {
            throw new DataHubClientException(e.getResponse().getStatusInfo().getReasonPhrase(), e);
        }
    }


    public <T> List<T> get(String uri, MediaType dataFormat, GenericType<List<T>> type, MultivaluedMap<String, String> queryParams)
    {
        try
        {
            return (List<T>)resource(uri, queryParams).accept(new MediaType[] {dataFormat}).get(type);
        }
        catch(ClientErrorException e)
        {
            throw new DataHubClientException(e.getResponse().getStatusInfo().getReasonPhrase(), e);
        }
    }


    public Response post(String uri, Object data)
    {
        MediaType mediaType = getMediaType();
        Response response = resource(uri).accept(new MediaType[] {mediaType}).post(Entity.entity(data, mediaType));
        if(isNotAuthorized(response.getStatus()))
        {
            throw new DataHubClientException(response.getStatusInfo().getReasonPhrase());
        }
        return response;
    }


    public Response post(String uri)
    {
        MediaType mediaType = getMediaType();
        Response response = resource(uri).accept(new MediaType[] {mediaType}).post(Entity.entity("", mediaType));
        if(isNotAuthorized(response.getStatus()))
        {
            throw new DataHubClientException(response.getStatusInfo().getReasonPhrase());
        }
        return response;
    }


    public Response put(String uri, Object data)
    {
        MediaType mediaType = getMediaType();
        Response response = resource(uri).accept(new MediaType[] {mediaType}).put(Entity.entity(data, mediaType));
        if(isNotAuthorized(response.getStatus()))
        {
            throw new DataHubClientException(response.getStatusInfo().getReasonPhrase());
        }
        return response;
    }


    public ResultData delete(String uri, MultivaluedMap<String, String> queryParams)
    {
        this.logger.debug("DELETE {}?{}", uri, queryParams);
        return delete(resource(uri, queryParams));
    }


    public ResultData delete(String uri)
    {
        return delete(resource(uri));
    }


    private static ResultData delete(Invocation.Builder resource)
    {
        Response resp = (Response)resource.delete(Response.class);
        if(isNotAuthorized(resp.getStatus()))
        {
            throw new DataHubClientException(resp.getStatusInfo().getReasonPhrase());
        }
        if(resp.getStatus() != Response.Status.OK.getStatusCode())
        {
            throw new DataHubClientException((String)resp.readEntity(String.class));
        }
        return (ResultData)resp.readEntity(ResultData.class);
    }


    protected MultivaluedMap<String, String> createPageParams(int pageNumber, int pageSize)
    {
        MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();
        multivaluedHashMap.putSingle("pageNumber", Integer.toString(pageNumber));
        multivaluedHashMap.putSingle("pageSize", Integer.toString(pageSize));
        return (MultivaluedMap<String, String>)multivaluedHashMap;
    }


    protected Logger log()
    {
        return this.logger;
    }


    private Client client()
    {
        if(this.client == null)
        {
            this.client = createClient();
        }
        return this.client;
    }


    protected Client createClient()
    {
        ClientConfiguration cfg = getConfiguration();
        return this.dataHubClientProvider.createClient(cfg);
    }


    private static boolean isNotAuthorized(int code)
    {
        return (Response.Status.FORBIDDEN.getStatusCode() == code || Response.Status.UNAUTHORIZED
                        .getStatusCode() == code);
    }


    protected void verifyErrorInResponse(Response response)
    {
        if(response.getStatus() != Response.Status.OK.getStatusCode())
        {
            try
            {
                List<ValidationFailure> failures = (List<ValidationFailure>)response.readEntity((GenericType)new Object(this));
                throw new DataHubClientException(((ValidationFailure)failures.get(0)).getMessage());
            }
            catch(DataHubClientException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                this.logger.trace(e.getMessage(), e);
                throw new DataHubClientException(response.getStatusInfo().getReasonPhrase());
            }
        }
    }


    protected abstract String getBaseApiUrl();


    protected abstract MediaType getMediaType();


    protected abstract ClientConfiguration getConfiguration();


    public void setDataHubClientProvider(DataHubClientProvider dataHubClientProvider)
    {
        this.dataHubClientProvider = dataHubClientProvider;
    }
}
