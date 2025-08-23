package com.hybris.backoffice.solrsearch.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class SolrModifiedItem extends GeneratedSolrModifiedItem
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        return super.createItem(ctx, type, allAttributes);
    }
}
