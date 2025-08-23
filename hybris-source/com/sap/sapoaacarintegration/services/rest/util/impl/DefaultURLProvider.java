/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapoaacarintegration.services.rest.util.impl;

import com.sap.sapoaacarintegration.services.rest.util.URLProvider;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Default implementation of URLProvider.
 */
public class DefaultURLProvider implements URLProvider
{
    @Override
    public URI compileURI(final String urlString, final String path, final String client) throws URISyntaxException
    {
        URI url;
        if(path.contains("?"))
        {
            url = new URI(urlString + path + "&sap-client=" + client);
        }
        else
        {
            url = new URI(urlString + path + "?sap-client=" + client);
        }
        return url;
    }
}
