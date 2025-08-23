package de.hybris.platform.servicelayer.model.strategies;

public class DefaultFetchStrategy implements FetchStrategy
{
    public boolean needsFetch(String attributeQualifier)
    {
        return false;
    }


    public boolean isMutable()
    {
        return true;
    }
}
