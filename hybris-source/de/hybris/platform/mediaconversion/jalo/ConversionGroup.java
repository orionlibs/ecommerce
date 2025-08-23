package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class ConversionGroup extends GeneratedConversionGroup
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
