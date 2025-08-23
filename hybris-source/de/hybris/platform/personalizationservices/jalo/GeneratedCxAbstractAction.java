package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.servicelayer.jalo.action.AbstractAction;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxAbstractAction extends AbstractAction
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String VARIATIONPOS = "variationPOS";
    public static final String VARIATION = "variation";
    protected static final BidirectionalOneToManyHandler<GeneratedCxAbstractAction> VARIATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXABSTRACTACTION, false, "variation", "variationPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("variationPOS", Item.AttributeMode.INITIAL);
        tmp.put("variation", Item.AttributeMode.INITIAL);
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


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        VARIATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CxVariation getVariation(SessionContext ctx)
    {
        return (CxVariation)getProperty(ctx, "variation");
    }


    public CxVariation getVariation()
    {
        return getVariation(getSession().getSessionContext());
    }


    public void setVariation(SessionContext ctx, CxVariation value)
    {
        VARIATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setVariation(CxVariation value)
    {
        setVariation(getSession().getSessionContext(), value);
    }


    Integer getVariationPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "variationPOS");
    }


    Integer getVariationPOS()
    {
        return getVariationPOS(getSession().getSessionContext());
    }


    int getVariationPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getVariationPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getVariationPOSAsPrimitive()
    {
        return getVariationPOSAsPrimitive(getSession().getSessionContext());
    }


    void setVariationPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "variationPOS", value);
    }


    void setVariationPOS(Integer value)
    {
        setVariationPOS(getSession().getSessionContext(), value);
    }


    void setVariationPOS(SessionContext ctx, int value)
    {
        setVariationPOS(ctx, Integer.valueOf(value));
    }


    void setVariationPOS(int value)
    {
        setVariationPOS(getSession().getSessionContext(), value);
    }
}
