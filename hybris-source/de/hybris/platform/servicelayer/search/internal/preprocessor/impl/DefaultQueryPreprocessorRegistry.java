package de.hybris.platform.servicelayer.search.internal.preprocessor.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.internal.preprocessor.QueryPreprocessorRegistry;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultQueryPreprocessorRegistry implements QueryPreprocessorRegistry
{
    private static final Logger LOG = Logger.getLogger(DefaultQueryPreprocessorRegistry.class);
    @Autowired
    private Map<String, QueryPreprocessor> queryPreprocessors;


    public void executeAllPreprocessors(FlexibleSearchQuery query)
    {
        if(query == null)
        {
            throw new IllegalArgumentException("FlexibleSearchQuery object is required to perform this operation, null given");
        }
        for(Map.Entry<String, QueryPreprocessor> entry : this.queryPreprocessors.entrySet())
        {
            if(entry.getValue() != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Query preprocessor: " + (String)entry.getKey());
                }
                ((QueryPreprocessor)entry.getValue()).process(query);
            }
        }
    }


    public void setQueryPreprocessors(Map<String, QueryPreprocessor> queryPreprocessors)
    {
        this.queryPreprocessors = queryPreprocessors;
    }
}
