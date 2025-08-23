package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.cms2.jalo.restrictions.AbstractRestriction;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.List;
import java.util.Map;

public abstract class AbstractCMSComponent extends GeneratedAbstractCMSComponent
{
    @Deprecated(since = "4.3")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.setAttributeMode("visible", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "4.3")
    public String getType(SessionContext ctx)
    {
        return getComposedType().getName(ctx);
    }


    @Deprecated(since = "4.3")
    public Map<Language, String> getAllType(SessionContext ctx)
    {
        return getComposedType().getAllNames(ctx);
    }


    @Deprecated(since = "4.3")
    public String getTypeCode(SessionContext ctx)
    {
        return getComposedType().getCode();
    }


    @Deprecated(since = "4.3")
    public Boolean isRestricted(SessionContext ctx)
    {
        List<AbstractRestriction> res = getRestrictions();
        return Boolean.valueOf((res != null && !res.isEmpty()));
    }


    @Deprecated(since = "4.3")
    public Boolean isVisible(SessionContext ctx)
    {
        Boolean bool = super.isVisible(ctx);
        return (bool != null) ? bool : Boolean.TRUE;
    }
}
