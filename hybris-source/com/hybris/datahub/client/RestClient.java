package com.hybris.datahub.client;

import javax.ws.rs.core.MediaType;

public class RestClient extends AbstractJerseyRestClient
{
    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;
    private final String baseApiUrl;
    private final MediaType mediaType;
    private final ClientConfiguration configuration;


    public RestClient(String baseUrl)
    {
        this(null, baseUrl);
    }


    public RestClient(ClientConfiguration cfg, String url)
    {
        this.baseApiUrl = url;
        this.configuration = cfg;
        this.mediaType = toMediaType();
    }


    private MediaType toMediaType()
    {
        String mimeType = getConfiguration().getContentType();
        try
        {
            return (mimeType != null) ? MediaType.valueOf(mimeType) : getDefaultMediaType();
        }
        catch(Exception e)
        {
            log().warn("Invalid Content-Type configured {}. Using {} by default.", new Object[] {mimeType, getDefaultMediaType(), e});
            return getDefaultMediaType();
        }
    }


    protected MediaType getDefaultMediaType()
    {
        return DEFAULT_MEDIA_TYPE;
    }


    public String getBaseApiUrl()
    {
        return this.baseApiUrl;
    }


    public String getContentType()
    {
        return this.mediaType.toString();
    }


    protected MediaType getMediaType()
    {
        return this.mediaType;
    }


    protected ClientConfiguration getConfiguration()
    {
        return (this.configuration != null) ? this.configuration : defaultConfiguration();
    }


    protected ClientConfiguration defaultConfiguration()
    {
        return (new ClientConfiguration())
                        .setContentType(getDefaultMediaType().toString());
    }
}
