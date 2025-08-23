package de.hybris.platform.platformbackoffice.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class BackofficeSearchCondition extends GeneratedBackofficeSearchCondition
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        return super.createItem(ctx, type, allAttributes);
    }
}
