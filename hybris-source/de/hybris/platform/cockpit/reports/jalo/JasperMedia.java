package de.hybris.platform.cockpit.reports.jalo;

import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

@Deprecated
public class JasperMedia extends GeneratedJasperMedia
{
    @Deprecated
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("folder"))
        {
            allAttributes.put("folder", CockpitManager.getInstance().getJasperReportsMediaFolder());
        }
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }
}
