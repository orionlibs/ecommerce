package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRemoveItemsCronJob extends CronJob
{
    public static final String ITEMPKS = "itemPKs";
    public static final String ITEMSFOUND = "itemsFound";
    public static final String ITEMSDELETED = "itemsDeleted";
    public static final String ITEMSREFUSED = "itemsRefused";
    public static final String CREATESAVEDVALUES = "createSavedValues";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("itemPKs", Item.AttributeMode.INITIAL);
        tmp.put("itemsFound", Item.AttributeMode.INITIAL);
        tmp.put("itemsDeleted", Item.AttributeMode.INITIAL);
        tmp.put("itemsRefused", Item.AttributeMode.INITIAL);
        tmp.put("createSavedValues", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCreateSavedValues(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "createSavedValues");
    }


    public Boolean isCreateSavedValues()
    {
        return isCreateSavedValues(getSession().getSessionContext());
    }


    public boolean isCreateSavedValuesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCreateSavedValues(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCreateSavedValuesAsPrimitive()
    {
        return isCreateSavedValuesAsPrimitive(getSession().getSessionContext());
    }


    public void setCreateSavedValues(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "createSavedValues", value);
    }


    public void setCreateSavedValues(Boolean value)
    {
        setCreateSavedValues(getSession().getSessionContext(), value);
    }


    public void setCreateSavedValues(SessionContext ctx, boolean value)
    {
        setCreateSavedValues(ctx, Boolean.valueOf(value));
    }


    public void setCreateSavedValues(boolean value)
    {
        setCreateSavedValues(getSession().getSessionContext(), value);
    }


    public Media getItemPKs(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "itemPKs");
    }


    public Media getItemPKs()
    {
        return getItemPKs(getSession().getSessionContext());
    }


    public void setItemPKs(SessionContext ctx, Media value)
    {
        setProperty(ctx, "itemPKs", value);
    }


    public void setItemPKs(Media value)
    {
        setItemPKs(getSession().getSessionContext(), value);
    }


    public Integer getItemsDeleted(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "itemsDeleted");
    }


    public Integer getItemsDeleted()
    {
        return getItemsDeleted(getSession().getSessionContext());
    }


    public int getItemsDeletedAsPrimitive(SessionContext ctx)
    {
        Integer value = getItemsDeleted(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getItemsDeletedAsPrimitive()
    {
        return getItemsDeletedAsPrimitive(getSession().getSessionContext());
    }


    public void setItemsDeleted(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "itemsDeleted", value);
    }


    public void setItemsDeleted(Integer value)
    {
        setItemsDeleted(getSession().getSessionContext(), value);
    }


    public void setItemsDeleted(SessionContext ctx, int value)
    {
        setItemsDeleted(ctx, Integer.valueOf(value));
    }


    public void setItemsDeleted(int value)
    {
        setItemsDeleted(getSession().getSessionContext(), value);
    }


    public Integer getItemsFound(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "itemsFound");
    }


    public Integer getItemsFound()
    {
        return getItemsFound(getSession().getSessionContext());
    }


    public int getItemsFoundAsPrimitive(SessionContext ctx)
    {
        Integer value = getItemsFound(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getItemsFoundAsPrimitive()
    {
        return getItemsFoundAsPrimitive(getSession().getSessionContext());
    }


    public void setItemsFound(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "itemsFound", value);
    }


    public void setItemsFound(Integer value)
    {
        setItemsFound(getSession().getSessionContext(), value);
    }


    public void setItemsFound(SessionContext ctx, int value)
    {
        setItemsFound(ctx, Integer.valueOf(value));
    }


    public void setItemsFound(int value)
    {
        setItemsFound(getSession().getSessionContext(), value);
    }


    public Integer getItemsRefused(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "itemsRefused");
    }


    public Integer getItemsRefused()
    {
        return getItemsRefused(getSession().getSessionContext());
    }


    public int getItemsRefusedAsPrimitive(SessionContext ctx)
    {
        Integer value = getItemsRefused(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getItemsRefusedAsPrimitive()
    {
        return getItemsRefusedAsPrimitive(getSession().getSessionContext());
    }


    public void setItemsRefused(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "itemsRefused", value);
    }


    public void setItemsRefused(Integer value)
    {
        setItemsRefused(getSession().getSessionContext(), value);
    }


    public void setItemsRefused(SessionContext ctx, int value)
    {
        setItemsRefused(ctx, Integer.valueOf(value));
    }


    public void setItemsRefused(int value)
    {
        setItemsRefused(getSession().getSessionContext(), value);
    }
}
