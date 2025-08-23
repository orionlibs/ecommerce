package com.hybris.datahub.client.extension;

import com.hybris.datahub.client.ClientConfiguration;
import com.hybris.datahub.client.DataHubBlockedException;
import com.hybris.datahub.client.RestClient;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ExtensionClient extends RestClient
{
    private static final String EXTENSION_URI = "/extensions";


    public ExtensionClient(String uri)
    {
        super(uri);
    }


    public ExtensionClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public void deployExtension(ExtensionSource source) throws IOException
    {
        log().info("Deploying extension {}", source);
        InputStream in = null;
        try
        {
            in = source.inputStream();
            deployExtension(in);
        }
        finally
        {
            if(in != null)
            {
                closeExtensionStream(in);
            }
        }
    }


    private void deployExtension(InputStream in)
    {
        Response resp = post("/extensions", in);
        if(resp.getStatus() == Response.Status.SERVICE_UNAVAILABLE.getStatusCode())
        {
            throw new DataHubBlockedException();
        }
        if(resp.getStatus() == Response.Status.BAD_REQUEST.getStatusCode())
        {
            throw new InvalidDataHubExtensionException();
        }
    }


    private void closeExtensionStream(InputStream in)
    {
        assert in != null : "Should not be attempted to close if null";
        try
        {
            in.close();
        }
        catch(IOException e)
        {
            log().warn("Failed to close extension source input stream", e);
        }
    }


    protected MediaType getDefaultMediaType()
    {
        return MediaType.APPLICATION_XML_TYPE;
    }
}
