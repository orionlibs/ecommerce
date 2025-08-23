package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsFeatureFlag;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderHelper;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProviderHelper implements AsSearchProviderHelper
{
    private AsSearchProviderFactory asSearchProviderFactory;


    public boolean hasFeature(String indexType, String feature)
    {
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        AsFeatureFlag featureFlag = AsFeatureFlag.valueOf(feature);
        return searchProvider.getSupportedFeatures(indexType).contains(featureFlag);
    }


    public AsSearchProviderFactory getAsSearchProviderFactory()
    {
        return this.asSearchProviderFactory;
    }


    @Required
    public void setAsSearchProviderFactory(AsSearchProviderFactory asSearchProviderFactory)
    {
        this.asSearchProviderFactory = asSearchProviderFactory;
    }
}
