package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.util.Config;

public class FlexibleSearchHintsProviderFactory
{
    private final DefaultHintsProvider defaultHintsProvider = new DefaultHintsProvider();
    private SQLServerHintProvider sqlServerHintProvider;
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper;


    public FlexibleSearchHintsProviderFactory(ReadOnlyConditionsHelper readOnlyConditionsHelper)
    {
        this.readOnlyConditionsHelper = readOnlyConditionsHelper;
    }


    public FlexibleSearchHintsProvider getFlexibleSearchProvider()
    {
        switch(null.$SwitchMap$de$hybris$platform$util$Config$DatabaseName[Config.getDatabaseName().ordinal()])
        {
            case 1:
                return getSqlServerHintProvider();
        }
        return (FlexibleSearchHintsProvider)this.defaultHintsProvider;
    }


    private FlexibleSearchHintsProvider getSqlServerHintProvider()
    {
        if(this.sqlServerHintProvider == null)
        {
            this.sqlServerHintProvider = new SQLServerHintProvider(this.readOnlyConditionsHelper);
        }
        return (FlexibleSearchHintsProvider)this.sqlServerHintProvider;
    }
}
