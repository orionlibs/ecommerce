package de.hybris.platform.personalizationintegration.segment;

public interface CxProvider
{
    public static final String DEFAULT_PROVIDER_ID = "DEFAULT";


    default String getProviderId()
    {
        return "DEFAULT";
    }
}
