package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedIndexTestItem extends GenericItem
{
    public static final String COLUMN1 = "column1";
    public static final String COLUMN2 = "column2";
    public static final String COLUMN3 = "column3";
    public static final String COLUMN4 = "column4";
    public static final String COLUMN5 = "column5";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("column1", Item.AttributeMode.INITIAL);
        tmp.put("column2", Item.AttributeMode.INITIAL);
        tmp.put("column3", Item.AttributeMode.INITIAL);
        tmp.put("column4", Item.AttributeMode.INITIAL);
        tmp.put("column5", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Short getColumn1(SessionContext ctx)
    {
        return (Short)getProperty(ctx, "column1");
    }


    public Short getColumn1()
    {
        return getColumn1(getSession().getSessionContext());
    }


    public short getColumn1AsPrimitive(SessionContext ctx)
    {
        Short value = getColumn1(ctx);
        return (value != null) ? value.shortValue() : 0;
    }


    public short getColumn1AsPrimitive()
    {
        return getColumn1AsPrimitive(getSession().getSessionContext());
    }


    protected void setColumn1(SessionContext ctx, Short value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'column1' is not changeable", 0);
        }
        setProperty(ctx, "column1", value);
    }


    protected void setColumn1(Short value)
    {
        setColumn1(getSession().getSessionContext(), value);
    }


    protected void setColumn1(SessionContext ctx, short value)
    {
        setColumn1(ctx, Short.valueOf(value));
    }


    protected void setColumn1(short value)
    {
        setColumn1(getSession().getSessionContext(), value);
    }


    public Short getColumn2(SessionContext ctx)
    {
        return (Short)getProperty(ctx, "column2");
    }


    public Short getColumn2()
    {
        return getColumn2(getSession().getSessionContext());
    }


    public short getColumn2AsPrimitive(SessionContext ctx)
    {
        Short value = getColumn2(ctx);
        return (value != null) ? value.shortValue() : 0;
    }


    public short getColumn2AsPrimitive()
    {
        return getColumn2AsPrimitive(getSession().getSessionContext());
    }


    protected void setColumn2(SessionContext ctx, Short value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'column2' is not changeable", 0);
        }
        setProperty(ctx, "column2", value);
    }


    protected void setColumn2(Short value)
    {
        setColumn2(getSession().getSessionContext(), value);
    }


    protected void setColumn2(SessionContext ctx, short value)
    {
        setColumn2(ctx, Short.valueOf(value));
    }


    protected void setColumn2(short value)
    {
        setColumn2(getSession().getSessionContext(), value);
    }


    public Short getColumn3(SessionContext ctx)
    {
        return (Short)getProperty(ctx, "column3");
    }


    public Short getColumn3()
    {
        return getColumn3(getSession().getSessionContext());
    }


    public short getColumn3AsPrimitive(SessionContext ctx)
    {
        Short value = getColumn3(ctx);
        return (value != null) ? value.shortValue() : 0;
    }


    public short getColumn3AsPrimitive()
    {
        return getColumn3AsPrimitive(getSession().getSessionContext());
    }


    protected void setColumn3(SessionContext ctx, Short value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'column3' is not changeable", 0);
        }
        setProperty(ctx, "column3", value);
    }


    protected void setColumn3(Short value)
    {
        setColumn3(getSession().getSessionContext(), value);
    }


    protected void setColumn3(SessionContext ctx, short value)
    {
        setColumn3(ctx, Short.valueOf(value));
    }


    protected void setColumn3(short value)
    {
        setColumn3(getSession().getSessionContext(), value);
    }


    public Short getColumn4(SessionContext ctx)
    {
        return (Short)getProperty(ctx, "column4");
    }


    public Short getColumn4()
    {
        return getColumn4(getSession().getSessionContext());
    }


    public short getColumn4AsPrimitive(SessionContext ctx)
    {
        Short value = getColumn4(ctx);
        return (value != null) ? value.shortValue() : 0;
    }


    public short getColumn4AsPrimitive()
    {
        return getColumn4AsPrimitive(getSession().getSessionContext());
    }


    protected void setColumn4(SessionContext ctx, Short value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'column4' is not changeable", 0);
        }
        setProperty(ctx, "column4", value);
    }


    protected void setColumn4(Short value)
    {
        setColumn4(getSession().getSessionContext(), value);
    }


    protected void setColumn4(SessionContext ctx, short value)
    {
        setColumn4(ctx, Short.valueOf(value));
    }


    protected void setColumn4(short value)
    {
        setColumn4(getSession().getSessionContext(), value);
    }


    public Short getColumn5(SessionContext ctx)
    {
        return (Short)getProperty(ctx, "column5");
    }


    public Short getColumn5()
    {
        return getColumn5(getSession().getSessionContext());
    }


    public short getColumn5AsPrimitive(SessionContext ctx)
    {
        Short value = getColumn5(ctx);
        return (value != null) ? value.shortValue() : 0;
    }


    public short getColumn5AsPrimitive()
    {
        return getColumn5AsPrimitive(getSession().getSessionContext());
    }


    protected void setColumn5(SessionContext ctx, Short value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'column5' is not changeable", 0);
        }
        setProperty(ctx, "column5", value);
    }


    protected void setColumn5(Short value)
    {
        setColumn5(getSession().getSessionContext(), value);
    }


    protected void setColumn5(SessionContext ctx, short value)
    {
        setColumn5(ctx, Short.valueOf(value));
    }


    protected void setColumn5(short value)
    {
        setColumn5(getSession().getSessionContext(), value);
    }
}
