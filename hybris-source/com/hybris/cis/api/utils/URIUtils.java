package com.hybris.cis.api.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class URIUtils
{
    public static URI createNewUri(String protocol, String host, int port, String context)
    {
        try
        {
            return (new URL(protocol, host, port, "/" + context)).toURI();
        }
        catch(MalformedURLException | URISyntaxException e)
        {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }


    public static URI createNewUri(String uri)
    {
        try
        {
            return new URI(uri);
        }
        catch(URISyntaxException e)
        {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
