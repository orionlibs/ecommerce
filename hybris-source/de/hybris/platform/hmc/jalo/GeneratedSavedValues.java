package de.hybris.platform.hmc.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedSavedValues extends GenericItem
{
    public static final String MODIFIEDITEMTYPE = "modifiedItemType";
    public static final String MODIFIEDITEMDISPLAYSTRING = "modifiedItemDisplayString";
    public static final String TIMESTAMP = "timestamp";
    public static final String USER = "user";
    public static final String CHANGEDATTRIBUTES = "changedAttributes";
    public static final String NUMBEROFCHANGEDATTRIBUTES = "numberOfChangedAttributes";
    public static final String MODIFICATIONTYPE = "modificationType";
    public static final String SAVEDVALUESENTRIES = "savedValuesEntries";
    public static final String MODIFIEDITEMPOS = "modifiedItemPOS";
    public static final String MODIFIEDITEM = "modifiedItem";
    protected static final OneToManyHandler<SavedValueEntry> SAVEDVALUESENTRIESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.SAVEDVALUEENTRY, true, "parent", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedSavedValues> MODIFIEDITEMHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.SAVEDVALUES, false, "modifiedItem", "modifiedItemPOS", true, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("modifiedItemType", Item.AttributeMode.INITIAL);
        tmp.put("modifiedItemDisplayString", Item.AttributeMode.INITIAL);
        tmp.put("timestamp", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("modificationType", Item.AttributeMode.INITIAL);
        tmp.put("modifiedItemPOS", Item.AttributeMode.INITIAL);
        tmp.put("modifiedItem", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getChangedAttributes()
    {
        return getChangedAttributes(getSession().getSessionContext());
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        MODIFIEDITEMHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public EnumerationValue getModificationType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "modificationType");
    }


    public EnumerationValue getModificationType()
    {
        return getModificationType(getSession().getSessionContext());
    }


    public void setModificationType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "modificationType", value);
    }


    public void setModificationType(EnumerationValue value)
    {
        setModificationType(getSession().getSessionContext(), value);
    }


    public Item getModifiedItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "modifiedItem");
    }


    public Item getModifiedItem()
    {
        return getModifiedItem(getSession().getSessionContext());
    }


    protected void setModifiedItem(SessionContext ctx, Item value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'modifiedItem' is not changeable", 0);
        }
        MODIFIEDITEMHANDLER.addValue(ctx, value, (ExtensibleItem)this);
    }


    protected void setModifiedItem(Item value)
    {
        setModifiedItem(getSession().getSessionContext(), value);
    }


    public String getModifiedItemDisplayString(SessionContext ctx)
    {
        return (String)getProperty(ctx, "modifiedItemDisplayString");
    }


    public String getModifiedItemDisplayString()
    {
        return getModifiedItemDisplayString(getSession().getSessionContext());
    }


    public void setModifiedItemDisplayString(SessionContext ctx, String value)
    {
        setProperty(ctx, "modifiedItemDisplayString", value);
    }


    public void setModifiedItemDisplayString(String value)
    {
        setModifiedItemDisplayString(getSession().getSessionContext(), value);
    }


    Integer getModifiedItemPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "modifiedItemPOS");
    }


    Integer getModifiedItemPOS()
    {
        return getModifiedItemPOS(getSession().getSessionContext());
    }


    int getModifiedItemPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getModifiedItemPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getModifiedItemPOSAsPrimitive()
    {
        return getModifiedItemPOSAsPrimitive(getSession().getSessionContext());
    }


    void setModifiedItemPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "modifiedItemPOS", value);
    }


    void setModifiedItemPOS(Integer value)
    {
        setModifiedItemPOS(getSession().getSessionContext(), value);
    }


    void setModifiedItemPOS(SessionContext ctx, int value)
    {
        setModifiedItemPOS(ctx, Integer.valueOf(value));
    }


    void setModifiedItemPOS(int value)
    {
        setModifiedItemPOS(getSession().getSessionContext(), value);
    }


    public ComposedType getModifiedItemType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "modifiedItemType");
    }


    public ComposedType getModifiedItemType()
    {
        return getModifiedItemType(getSession().getSessionContext());
    }


    public void setModifiedItemType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "modifiedItemType", value);
    }


    public void setModifiedItemType(ComposedType value)
    {
        setModifiedItemType(getSession().getSessionContext(), value);
    }


    public Integer getNumberOfChangedAttributes()
    {
        return getNumberOfChangedAttributes(getSession().getSessionContext());
    }


    public int getNumberOfChangedAttributesAsPrimitive(SessionContext ctx)
    {
        Integer value = getNumberOfChangedAttributes(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNumberOfChangedAttributesAsPrimitive()
    {
        return getNumberOfChangedAttributesAsPrimitive(getSession().getSessionContext());
    }


    public Set<SavedValueEntry> getSavedValuesEntries(SessionContext ctx)
    {
        return (Set<SavedValueEntry>)SAVEDVALUESENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<SavedValueEntry> getSavedValuesEntries()
    {
        return getSavedValuesEntries(getSession().getSessionContext());
    }


    public Date getTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "timestamp");
    }


    public Date getTimestamp()
    {
        return getTimestamp(getSession().getSessionContext());
    }


    public void setTimestamp(SessionContext ctx, Date value)
    {
        setProperty(ctx, "timestamp", value);
    }


    public void setTimestamp(Date value)
    {
        setTimestamp(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    public abstract String getChangedAttributes(SessionContext paramSessionContext);


    public abstract Integer getNumberOfChangedAttributes(SessionContext paramSessionContext);
}
