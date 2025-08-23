package de.hybris.platform.persistence;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;

public class MyTestLanguage extends Language
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("myCountry", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
