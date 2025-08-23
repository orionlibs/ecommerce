package de.hybris.platform.cms2.jalo.relations;

import de.hybris.platform.cms2.jalo.pages.PageTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class ContentSlotForTemplate extends GeneratedContentSlotForTemplate
{
    @Deprecated(since = "4.3")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PageTemplate template = (PageTemplate)allAttributes.get("pageTemplate");
        if(allAttributes.get("catalogVersion") == null && template != null)
        {
            allAttributes.put("catalogVersion", template.getCatalogVersion());
        }
        allAttributes.setAttributeMode("allowOverwrite", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("pageTemplate", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("contentSlot", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("position", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
