package de.hybris.platform.advancedsavedquery.jalo;

import de.hybris.platform.core.Registry;
import org.apache.log4j.Logger;

public class ASQManager extends GeneratedASQManager
{
    private static final Logger log = Logger.getLogger(ASQManager.class.getName());


    public static ASQManager getInstance()
    {
        return (ASQManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("advancedsavedquery");
    }


    public AdvancedSavedQuery getAdvancedSavedQuery(String code)
    {
        return (AdvancedSavedQuery)getFirstItemByAttribute(AdvancedSavedQuery.class, "code", code);
    }
}
