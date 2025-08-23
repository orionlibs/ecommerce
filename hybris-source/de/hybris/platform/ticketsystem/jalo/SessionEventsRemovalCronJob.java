package de.hybris.platform.ticketsystem.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public class SessionEventsRemovalCronJob extends GeneratedSessionEventsRemovalCronJob
{
    private static final Logger LOG = Logger.getLogger(SessionEventsRemovalCronJob.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        return super.createItem(ctx, type, allAttributes);
    }
}
