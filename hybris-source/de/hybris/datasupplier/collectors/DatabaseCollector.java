package de.hybris.datasupplier.collectors;

import java.net.URI;
import java.net.URISyntaxException;

public interface DatabaseCollector
{
    boolean isApplicable(String paramString);


    URI getHost(String paramString) throws URISyntaxException;


    String getName(String paramString);
}
