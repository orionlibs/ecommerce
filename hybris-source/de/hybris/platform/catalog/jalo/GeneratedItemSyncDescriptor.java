package de.hybris.platform.catalog.jalo;

import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedItemSyncDescriptor extends ChangeDescriptor
{
    public static final String TARGETITEM = "targetItem";
    public static final String DONE = "done";
    public static final String COPIEDIMPLICITELY = "copiedImplicitely";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ChangeDescriptor.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("targetItem", Item.AttributeMode.INITIAL);
        tmp.put("done", Item.AttributeMode.INITIAL);
        tmp.put("copiedImplicitely", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCopiedImplicitely(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "copiedImplicitely");
    }


    public Boolean isCopiedImplicitely()
    {
        return isCopiedImplicitely(getSession().getSessionContext());
    }


    public boolean isCopiedImplicitelyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCopiedImplicitely(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCopiedImplicitelyAsPrimitive()
    {
        return isCopiedImplicitelyAsPrimitive(getSession().getSessionContext());
    }


    public void setCopiedImplicitely(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "copiedImplicitely", value);
    }


    public void setCopiedImplicitely(Boolean value)
    {
        setCopiedImplicitely(getSession().getSessionContext(), value);
    }


    public void setCopiedImplicitely(SessionContext ctx, boolean value)
    {
        setCopiedImplicitely(ctx, Boolean.valueOf(value));
    }


    public void setCopiedImplicitely(boolean value)
    {
        setCopiedImplicitely(getSession().getSessionContext(), value);
    }


    public Boolean isDone(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "done");
    }


    public Boolean isDone()
    {
        return isDone(getSession().getSessionContext());
    }


    public boolean isDoneAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDone(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDoneAsPrimitive()
    {
        return isDoneAsPrimitive(getSession().getSessionContext());
    }


    public void setDone(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "done", value);
    }


    public void setDone(Boolean value)
    {
        setDone(getSession().getSessionContext(), value);
    }


    public void setDone(SessionContext ctx, boolean value)
    {
        setDone(ctx, Boolean.valueOf(value));
    }


    public void setDone(boolean value)
    {
        setDone(getSession().getSessionContext(), value);
    }


    public Item getTargetItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "targetItem");
    }


    public Item getTargetItem()
    {
        return getTargetItem(getSession().getSessionContext());
    }


    public void setTargetItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "targetItem", value);
    }


    public void setTargetItem(Item value)
    {
        setTargetItem(getSession().getSessionContext(), value);
    }
}
