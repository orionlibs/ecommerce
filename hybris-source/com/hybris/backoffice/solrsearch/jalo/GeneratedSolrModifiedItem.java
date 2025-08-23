package com.hybris.backoffice.solrsearch.jalo;

import com.hybris.backoffice.solrsearch.constants.GeneratedBackofficesolrsearchConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrModifiedItem extends GenericItem
{
    public static final String MODIFIEDTYPECODE = "modifiedTypeCode";
    public static final String MODIFIEDPK = "modifiedPk";
    public static final String MODIFICATIONTYPE = "modificationType";
    public static final String PARENT = "parent";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrModifiedItem> PARENTHANDLER = new BidirectionalOneToManyHandler(GeneratedBackofficesolrsearchConstants.TC.SOLRMODIFIEDITEM, false, "parent", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("modifiedTypeCode", Item.AttributeMode.INITIAL);
        tmp.put("modifiedPk", Item.AttributeMode.INITIAL);
        tmp.put("modificationType", Item.AttributeMode.INITIAL);
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


    public Long getModifiedPk(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "modifiedPk");
    }


    public Long getModifiedPk()
    {
        return getModifiedPk(getSession().getSessionContext());
    }


    public long getModifiedPkAsPrimitive(SessionContext ctx)
    {
        Long value = getModifiedPk(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getModifiedPkAsPrimitive()
    {
        return getModifiedPkAsPrimitive(getSession().getSessionContext());
    }


    public void setModifiedPk(SessionContext ctx, Long value)
    {
        setProperty(ctx, "modifiedPk", value);
    }


    public void setModifiedPk(Long value)
    {
        setModifiedPk(getSession().getSessionContext(), value);
    }


    public void setModifiedPk(SessionContext ctx, long value)
    {
        setModifiedPk(ctx, Long.valueOf(value));
    }


    public void setModifiedPk(long value)
    {
        setModifiedPk(getSession().getSessionContext(), value);
    }


    public String getModifiedTypeCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "modifiedTypeCode");
    }


    public String getModifiedTypeCode()
    {
        return getModifiedTypeCode(getSession().getSessionContext());
    }


    public void setModifiedTypeCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "modifiedTypeCode", value);
    }


    public void setModifiedTypeCode(String value)
    {
        setModifiedTypeCode(getSession().getSessionContext(), value);
    }


    public BackofficeSolrIndexerCronJob getParent(SessionContext ctx)
    {
        return (BackofficeSolrIndexerCronJob)getProperty(ctx, "parent");
    }


    public BackofficeSolrIndexerCronJob getParent()
    {
        return getParent(getSession().getSessionContext());
    }


    public void setParent(SessionContext ctx, BackofficeSolrIndexerCronJob value)
    {
        PARENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setParent(BackofficeSolrIndexerCronJob value)
    {
        setParent(getSession().getSessionContext(), value);
    }
}
