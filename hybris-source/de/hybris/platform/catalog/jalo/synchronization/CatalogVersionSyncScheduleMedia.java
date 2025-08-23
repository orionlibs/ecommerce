package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class CatalogVersionSyncScheduleMedia extends GeneratedCatalogVersionSyncScheduleMedia
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("cronjob") == null)
        {
            throw new JaloInvalidParameterException("missing sync cronjob for creating a new " + type.getCode(), 0);
        }
        allAttributes.setAttributeMode("cronjob", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("cronjobPOS", Item.AttributeMode.INITIAL);
        if(!allAttributes.containsKey("folder"))
        {
            allAttributes.put("folder", CatalogManager.getInstance().getCatalogSyncMediaFolder());
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
