package de.hybris.platform.hmc.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSavedValueEntry extends GenericItem
{
    public static final String MODIFIEDATTRIBUTE = "modifiedAttribute";
    public static final String OLDVALUEATTRIBUTEDESCRIPTOR = "OldValueAttributeDescriptor";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";
    public static final String PARENT = "parent";
    protected static final BidirectionalOneToManyHandler<GeneratedSavedValueEntry> PARENTHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.SAVEDVALUEENTRY, false, "parent", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("modifiedAttribute", Item.AttributeMode.INITIAL);
        tmp.put("OldValueAttributeDescriptor", Item.AttributeMode.INITIAL);
        tmp.put("oldValue", Item.AttributeMode.INITIAL);
        tmp.put("newValue", Item.AttributeMode.INITIAL);
        tmp.put("parent", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PARENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getModifiedAttribute(SessionContext ctx)
    {
        return (String)getProperty(ctx, "modifiedAttribute");
    }


    public String getModifiedAttribute()
    {
        return getModifiedAttribute(getSession().getSessionContext());
    }


    public void setModifiedAttribute(SessionContext ctx, String value)
    {
        setProperty(ctx, "modifiedAttribute", value);
    }


    public void setModifiedAttribute(String value)
    {
        setModifiedAttribute(getSession().getSessionContext(), value);
    }


    public Object getNewValue(SessionContext ctx)
    {
        return getProperty(ctx, "newValue");
    }


    public Object getNewValue()
    {
        return getNewValue(getSession().getSessionContext());
    }


    public void setNewValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "newValue", value);
    }


    public void setNewValue(Object value)
    {
        setNewValue(getSession().getSessionContext(), value);
    }


    public Object getOldValue(SessionContext ctx)
    {
        return getProperty(ctx, "oldValue");
    }


    public Object getOldValue()
    {
        return getOldValue(getSession().getSessionContext());
    }


    public void setOldValue(SessionContext ctx, Object value)
    {
        setProperty(ctx, "oldValue", value);
    }


    public void setOldValue(Object value)
    {
        setOldValue(getSession().getSessionContext(), value);
    }


    public AttributeDescriptor getOldValueAttributeDescriptor(SessionContext ctx)
    {
        return (AttributeDescriptor)getProperty(ctx, "OldValueAttributeDescriptor");
    }


    public AttributeDescriptor getOldValueAttributeDescriptor()
    {
        return getOldValueAttributeDescriptor(getSession().getSessionContext());
    }


    public void setOldValueAttributeDescriptor(SessionContext ctx, AttributeDescriptor value)
    {
        setProperty(ctx, "OldValueAttributeDescriptor", value);
    }


    public void setOldValueAttributeDescriptor(AttributeDescriptor value)
    {
        setOldValueAttributeDescriptor(getSession().getSessionContext(), value);
    }


    public SavedValues getParent(SessionContext ctx)
    {
        return (SavedValues)getProperty(ctx, "parent");
    }


    public SavedValues getParent()
    {
        return getParent(getSession().getSessionContext());
    }


    protected void setParent(SessionContext ctx, SavedValues value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'parent' is not changeable", 0);
        }
        PARENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setParent(SavedValues value)
    {
        setParent(getSession().getSessionContext(), value);
    }
}
