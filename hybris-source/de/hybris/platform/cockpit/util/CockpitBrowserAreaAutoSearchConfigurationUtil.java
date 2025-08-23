package de.hybris.platform.cockpit.util;

import org.zkoss.zk.ui.Execution;

public class CockpitBrowserAreaAutoSearchConfigurationUtil
{
    private static final String PROPERTY_DISABLE_INITIAL_SEARCH = "default.abstractBrowserArea.disabledInitialSearch";
    private static final String PROPERTY_DISABLE_AUTOMATIC_SEARCH = "default.abstractBrowserArea.disabledAutoSearch";


    public static boolean isInitialSearchDisabled(Execution execution)
    {
        return Boolean.parseBoolean(UITools.getCockpitParameter("default.abstractBrowserArea.disabledInitialSearch", execution));
    }


    public static boolean isAutomaticSearchDisabled(Execution execution)
    {
        return Boolean.parseBoolean(UITools.getCockpitParameter("default.abstractBrowserArea.disabledAutoSearch", execution));
    }
}
