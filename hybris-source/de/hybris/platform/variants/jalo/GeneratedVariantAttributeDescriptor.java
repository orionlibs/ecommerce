package de.hybris.platform.variants.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;

public abstract class GeneratedVariantAttributeDescriptor extends AttributeDescriptor
{
    public static final String POSITION = "position";


    public Integer getPosition(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "position");
    }


    public Integer getPosition()
    {
        return getPosition(getSession().getSessionContext());
    }


    public int getPositionAsPrimitive(SessionContext ctx)
    {
        Integer value = getPosition(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPositionAsPrimitive()
    {
        return getPositionAsPrimitive(getSession().getSessionContext());
    }


    public void setPosition(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "position", value);
    }


    public void setPosition(Integer value)
    {
        setPosition(getSession().getSessionContext(), value);
    }


    public void setPosition(SessionContext ctx, int value)
    {
        setPosition(ctx, Integer.valueOf(value));
    }


    public void setPosition(int value)
    {
        setPosition(getSession().getSessionContext(), value);
    }
}
