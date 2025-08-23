package de.hybris.platform.cms2.jalo.relations;

import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public final class ContentSlotForPage extends GeneratedContentSlotForPage
{
    @Deprecated(since = "4.3")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        AbstractPage page = (AbstractPage)allAttributes.get("page");
        if(allAttributes.get("catalogVersion") == null && page != null)
        {
            allAttributes.put("catalogVersion", page.getCatalogVersion());
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
