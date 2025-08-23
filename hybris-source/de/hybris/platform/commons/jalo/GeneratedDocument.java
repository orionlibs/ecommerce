package de.hybris.platform.commons.jalo;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDocument extends Media
{
    public static final String ITEMTIMESTAMP = "itemTimestamp";
    public static final String FORMAT = "format";
    public static final String SOURCEITEM = "sourceItem";
    protected static final BidirectionalOneToManyHandler<GeneratedDocument> SOURCEITEMHANDLER = new BidirectionalOneToManyHandler(GeneratedCommonsConstants.TC.DOCUMENT, false, "sourceItem", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("itemTimestamp", Item.AttributeMode.INITIAL);
        tmp.put("format", Item.AttributeMode.INITIAL);
        tmp.put("sourceItem", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOURCEITEMHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Format getFormat(SessionContext ctx)
    {
        return (Format)getProperty(ctx, "format");
    }


    public Format getFormat()
    {
        return getFormat(getSession().getSessionContext());
    }


    protected void setFormat(SessionContext ctx, Format value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'format' is not changeable", 0);
        }
        setProperty(ctx, "format", value);
    }


    protected void setFormat(Format value)
    {
        setFormat(getSession().getSessionContext(), value);
    }


    public Date getItemTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "itemTimestamp");
    }


    public Date getItemTimestamp()
    {
        return getItemTimestamp(getSession().getSessionContext());
    }


    public void setItemTimestamp(SessionContext ctx, Date value)
    {
        setProperty(ctx, "itemTimestamp", value);
    }


    public void setItemTimestamp(Date value)
    {
        setItemTimestamp(getSession().getSessionContext(), value);
    }


    public Item getSourceItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "sourceItem");
    }


    public Item getSourceItem()
    {
        return getSourceItem(getSession().getSessionContext());
    }


    protected void setSourceItem(SessionContext ctx, Item value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceItem' is not changeable", 0);
        }
        SOURCEITEMHANDLER.addValue(ctx, value, (ExtensibleItem)this);
    }


    protected void setSourceItem(Item value)
    {
        setSourceItem(getSession().getSessionContext(), value);
    }
}
