package de.hybris.platform.cms2.jalo.pages;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForTemplate;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageTemplate extends GeneratedPageTemplate
{
    @Deprecated(since = "4.3")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("active", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "4.3")
    public List<ContentSlotForTemplate> getContentSlots(SessionContext ctx)
    {
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedCms2Constants.TC.CONTENTSLOTFORTEMPLATE + "} WHERE {pageTemplate} = ?me";
        return getSession().getFlexibleSearch()
                        .search(ctx, query, Collections.singletonMap("me", this), ContentSlotForTemplate.class).getResult();
    }


    @Deprecated(since = "4.3")
    public List<String> getDefinedContentSlotPositions()
    {
        List<String> result = new ArrayList<>();
        for(ContentSlotForTemplate slot : getContentSlots())
        {
            result.add(slot.getPosition());
        }
        return result;
    }
}
