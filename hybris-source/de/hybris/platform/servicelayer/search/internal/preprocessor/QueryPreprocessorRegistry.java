package de.hybris.platform.servicelayer.search.internal.preprocessor;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

public interface QueryPreprocessorRegistry
{
    void executeAllPreprocessors(FlexibleSearchQuery paramFlexibleSearchQuery);
}
