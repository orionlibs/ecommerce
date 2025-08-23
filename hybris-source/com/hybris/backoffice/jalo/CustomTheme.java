package com.hybris.backoffice.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomTheme extends GeneratedCustomTheme
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomTheme.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }
}
