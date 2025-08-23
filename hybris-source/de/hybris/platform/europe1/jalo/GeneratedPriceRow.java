package de.hybris.platform.europe1.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPriceRow extends PDTRow
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String MATCHVALUE = "matchValue";
    public static final String CURRENCY = "currency";
    public static final String MINQTD = "minqtd";
    public static final String NET = "net";
    public static final String PRICE = "price";
    public static final String UNIT = "unit";
    public static final String UNITFACTOR = "unitFactor";
    public static final String GIVEAWAYPRICE = "giveAwayPrice";
    public static final String CHANNEL = "channel";
    protected static final BidirectionalOneToManyHandler<GeneratedPriceRow> PRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.PRODUCT, false, "product", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(PDTRow.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("matchValue", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("minqtd", Item.AttributeMode.INITIAL);
        tmp.put("net", Item.AttributeMode.INITIAL);
        tmp.put("price", Item.AttributeMode.INITIAL);
        tmp.put("unit", Item.AttributeMode.INITIAL);
        tmp.put("unitFactor", Item.AttributeMode.INITIAL);
        tmp.put("giveAwayPrice", Item.AttributeMode.INITIAL);
        tmp.put("channel", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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


    public EnumerationValue getChannel(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "channel");
    }


    public EnumerationValue getChannel()
    {
        return getChannel(getSession().getSessionContext());
    }


    public void setChannel(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "channel", value);
    }


    public void setChannel(EnumerationValue value)
    {
        setChannel(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PRODUCTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public Boolean isGiveAwayPrice(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "giveAwayPrice");
    }


    public Boolean isGiveAwayPrice()
    {
        return isGiveAwayPrice(getSession().getSessionContext());
    }


    public boolean isGiveAwayPriceAsPrimitive(SessionContext ctx)
    {
        Boolean value = isGiveAwayPrice(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isGiveAwayPriceAsPrimitive()
    {
        return isGiveAwayPriceAsPrimitive(getSession().getSessionContext());
    }


    public void setGiveAwayPrice(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "giveAwayPrice", value);
    }


    public void setGiveAwayPrice(Boolean value)
    {
        setGiveAwayPrice(getSession().getSessionContext(), value);
    }


    public void setGiveAwayPrice(SessionContext ctx, boolean value)
    {
        setGiveAwayPrice(ctx, Boolean.valueOf(value));
    }


    public void setGiveAwayPrice(boolean value)
    {
        setGiveAwayPrice(getSession().getSessionContext(), value);
    }


    public Integer getMatchValue(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "matchValue");
    }


    public Integer getMatchValue()
    {
        return getMatchValue(getSession().getSessionContext());
    }


    public int getMatchValueAsPrimitive(SessionContext ctx)
    {
        Integer value = getMatchValue(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMatchValueAsPrimitive()
    {
        return getMatchValueAsPrimitive(getSession().getSessionContext());
    }


    public void setMatchValue(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "matchValue", value);
    }


    public void setMatchValue(Integer value)
    {
        setMatchValue(getSession().getSessionContext(), value);
    }


    public void setMatchValue(SessionContext ctx, int value)
    {
        setMatchValue(ctx, Integer.valueOf(value));
    }


    public void setMatchValue(int value)
    {
        setMatchValue(getSession().getSessionContext(), value);
    }


    public Long getMinqtd(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "minqtd");
    }


    public Long getMinqtd()
    {
        return getMinqtd(getSession().getSessionContext());
    }


    public long getMinqtdAsPrimitive(SessionContext ctx)
    {
        Long value = getMinqtd(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getMinqtdAsPrimitive()
    {
        return getMinqtdAsPrimitive(getSession().getSessionContext());
    }


    public void setMinqtd(SessionContext ctx, Long value)
    {
        setProperty(ctx, "minqtd", value);
    }


    public void setMinqtd(Long value)
    {
        setMinqtd(getSession().getSessionContext(), value);
    }


    public void setMinqtd(SessionContext ctx, long value)
    {
        setMinqtd(ctx, Long.valueOf(value));
    }


    public void setMinqtd(long value)
    {
        setMinqtd(getSession().getSessionContext(), value);
    }


    public Boolean isNet(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "net");
    }


    public Boolean isNet()
    {
        return isNet(getSession().getSessionContext());
    }


    public boolean isNetAsPrimitive(SessionContext ctx)
    {
        Boolean value = isNet(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isNetAsPrimitive()
    {
        return isNetAsPrimitive(getSession().getSessionContext());
    }


    public void setNet(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "net", value);
    }


    public void setNet(Boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public void setNet(SessionContext ctx, boolean value)
    {
        setNet(ctx, Boolean.valueOf(value));
    }


    public void setNet(boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public Double getPrice(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "price");
    }


    public Double getPrice()
    {
        return getPrice(getSession().getSessionContext());
    }


    public double getPriceAsPrimitive(SessionContext ctx)
    {
        Double value = getPrice(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getPriceAsPrimitive()
    {
        return getPriceAsPrimitive(getSession().getSessionContext());
    }


    public void setPrice(SessionContext ctx, Double value)
    {
        setProperty(ctx, "price", value);
    }


    public void setPrice(Double value)
    {
        setPrice(getSession().getSessionContext(), value);
    }


    public void setPrice(SessionContext ctx, double value)
    {
        setPrice(ctx, Double.valueOf(value));
    }


    public void setPrice(double value)
    {
        setPrice(getSession().getSessionContext(), value);
    }


    public Unit getUnit(SessionContext ctx)
    {
        return (Unit)getProperty(ctx, "unit");
    }


    public Unit getUnit()
    {
        return getUnit(getSession().getSessionContext());
    }


    public void setUnit(SessionContext ctx, Unit value)
    {
        setProperty(ctx, "unit", value);
    }


    public void setUnit(Unit value)
    {
        setUnit(getSession().getSessionContext(), value);
    }


    public Integer getUnitFactor(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "unitFactor");
    }


    public Integer getUnitFactor()
    {
        return getUnitFactor(getSession().getSessionContext());
    }


    public int getUnitFactorAsPrimitive(SessionContext ctx)
    {
        Integer value = getUnitFactor(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getUnitFactorAsPrimitive()
    {
        return getUnitFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setUnitFactor(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "unitFactor", value);
    }


    public void setUnitFactor(Integer value)
    {
        setUnitFactor(getSession().getSessionContext(), value);
    }


    public void setUnitFactor(SessionContext ctx, int value)
    {
        setUnitFactor(ctx, Integer.valueOf(value));
    }


    public void setUnitFactor(int value)
    {
        setUnitFactor(getSession().getSessionContext(), value);
    }
}
