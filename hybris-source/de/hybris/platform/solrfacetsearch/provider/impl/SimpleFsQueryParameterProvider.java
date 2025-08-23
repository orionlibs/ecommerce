package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;
import java.util.HashMap;
import java.util.Map;

public class SimpleFsQueryParameterProvider implements ParameterProvider
{
    public Map<String, Object> createParameters()
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("price", Double.valueOf(999.99D));
        return parameters;
    }
}
