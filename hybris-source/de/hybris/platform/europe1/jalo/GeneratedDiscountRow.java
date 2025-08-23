package de.hybris.platform.europe1.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDiscountRow extends AbstractDiscountRow
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String ASTARGETPRICE = "asTargetPrice";
    protected static final BidirectionalOneToManyHandler<GeneratedDiscountRow> PRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.PRODUCT, false, "product", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractDiscountRow.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("asTargetPrice", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAsTargetPrice(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "asTargetPrice");
    }


    public Boolean isAsTargetPrice()
    {
        return isAsTargetPrice(getSession().getSessionContext());
    }


    public boolean isAsTargetPriceAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAsTargetPrice(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAsTargetPriceAsPrimitive()
    {
        return isAsTargetPriceAsPrimitive(getSession().getSessionContext());
    }


    public void setAsTargetPrice(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "asTargetPrice", value);
    }


    public void setAsTargetPrice(Boolean value)
    {
        setAsTargetPrice(getSession().getSessionContext(), value);
    }


    public void setAsTargetPrice(SessionContext ctx, boolean value)
    {
        setAsTargetPrice(ctx, Boolean.valueOf(value));
    }


    public void setAsTargetPrice(boolean value)
    {
        setAsTargetPrice(getSession().getSessionContext(), value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PRODUCTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }
}
