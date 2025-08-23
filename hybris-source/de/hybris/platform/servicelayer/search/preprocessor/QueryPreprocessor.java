package de.hybris.platform.servicelayer.search.preprocessor;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

public interface QueryPreprocessor
{
    void process(FlexibleSearchQuery paramFlexibleSearchQuery);
}
