package de.hybris.platform.cms2lib.components;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public abstract class AbstractBannerComponent extends GeneratedAbstractBannerComponent
{
    private static Logger log = Logger.getLogger(AbstractBannerComponent.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }
}
