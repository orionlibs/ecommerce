package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPackagingInfo extends GenericItem
{
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String LENGTH = "length";
    public static final String GROSSWEIGHT = "grossWeight";
    public static final String DIMENSIONUNIT = "dimensionUnit";
    public static final String WEIGHTUNIT = "weightUnit";
    public static final String INSUREDVALUE = "insuredValue";
    public static final String CONSIGNMENTPOS = "consignmentPOS";
    public static final String CONSIGNMENT = "consignment";
    protected static final BidirectionalOneToManyHandler<GeneratedPackagingInfo> CONSIGNMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.PACKAGINGINFO, false, "consignment", "consignmentPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("width", Item.AttributeMode.INITIAL);
        tmp.put("height", Item.AttributeMode.INITIAL);
        tmp.put("length", Item.AttributeMode.INITIAL);
        tmp.put("grossWeight", Item.AttributeMode.INITIAL);
        tmp.put("dimensionUnit", Item.AttributeMode.INITIAL);
        tmp.put("weightUnit", Item.AttributeMode.INITIAL);
        tmp.put("insuredValue", Item.AttributeMode.INITIAL);
        tmp.put("consignmentPOS", Item.AttributeMode.INITIAL);
        tmp.put("consignment", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Consignment getConsignment(SessionContext ctx)
    {
        return (Consignment)getProperty(ctx, "consignment");
    }


    public Consignment getConsignment()
    {
        return getConsignment(getSession().getSessionContext());
    }


    protected void setConsignment(SessionContext ctx, Consignment value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'consignment' is not changeable", 0);
        }
        CONSIGNMENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setConsignment(Consignment value)
    {
        setConsignment(getSession().getSessionContext(), value);
    }


    Integer getConsignmentPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "consignmentPOS");
    }


    Integer getConsignmentPOS()
    {
        return getConsignmentPOS(getSession().getSessionContext());
    }


    int getConsignmentPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getConsignmentPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getConsignmentPOSAsPrimitive()
    {
        return getConsignmentPOSAsPrimitive(getSession().getSessionContext());
    }


    void setConsignmentPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "consignmentPOS", value);
    }


    void setConsignmentPOS(Integer value)
    {
        setConsignmentPOS(getSession().getSessionContext(), value);
    }


    void setConsignmentPOS(SessionContext ctx, int value)
    {
        setConsignmentPOS(ctx, Integer.valueOf(value));
    }


    void setConsignmentPOS(int value)
    {
        setConsignmentPOS(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONSIGNMENTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDimensionUnit(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dimensionUnit");
    }


    public String getDimensionUnit()
    {
        return getDimensionUnit(getSession().getSessionContext());
    }


    public void setDimensionUnit(SessionContext ctx, String value)
    {
        setProperty(ctx, "dimensionUnit", value);
    }


    public void setDimensionUnit(String value)
    {
        setDimensionUnit(getSession().getSessionContext(), value);
    }


    public String getGrossWeight(SessionContext ctx)
    {
        return (String)getProperty(ctx, "grossWeight");
    }


    public String getGrossWeight()
    {
        return getGrossWeight(getSession().getSessionContext());
    }


    public void setGrossWeight(SessionContext ctx, String value)
    {
        setProperty(ctx, "grossWeight", value);
    }


    public void setGrossWeight(String value)
    {
        setGrossWeight(getSession().getSessionContext(), value);
    }


    public String getHeight(SessionContext ctx)
    {
        return (String)getProperty(ctx, "height");
    }


    public String getHeight()
    {
        return getHeight(getSession().getSessionContext());
    }


    public void setHeight(SessionContext ctx, String value)
    {
        setProperty(ctx, "height", value);
    }


    public void setHeight(String value)
    {
        setHeight(getSession().getSessionContext(), value);
    }


    public String getInsuredValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "insuredValue");
    }


    public String getInsuredValue()
    {
        return getInsuredValue(getSession().getSessionContext());
    }


    public void setInsuredValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "insuredValue", value);
    }


    public void setInsuredValue(String value)
    {
        setInsuredValue(getSession().getSessionContext(), value);
    }


    public String getLength(SessionContext ctx)
    {
        return (String)getProperty(ctx, "length");
    }


    public String getLength()
    {
        return getLength(getSession().getSessionContext());
    }


    public void setLength(SessionContext ctx, String value)
    {
        setProperty(ctx, "length", value);
    }


    public void setLength(String value)
    {
        setLength(getSession().getSessionContext(), value);
    }


    public String getWeightUnit(SessionContext ctx)
    {
        return (String)getProperty(ctx, "weightUnit");
    }


    public String getWeightUnit()
    {
        return getWeightUnit(getSession().getSessionContext());
    }


    public void setWeightUnit(SessionContext ctx, String value)
    {
        setProperty(ctx, "weightUnit", value);
    }


    public void setWeightUnit(String value)
    {
        setWeightUnit(getSession().getSessionContext(), value);
    }


    public String getWidth(SessionContext ctx)
    {
        return (String)getProperty(ctx, "width");
    }


    public String getWidth()
    {
        return getWidth(getSession().getSessionContext());
    }


    public void setWidth(SessionContext ctx, String value)
    {
        setProperty(ctx, "width", value);
    }


    public void setWidth(String value)
    {
        setWidth(getSession().getSessionContext(), value);
    }
}
