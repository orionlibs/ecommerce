package de.hybris.platform.deeplink.jalo.media;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.deeplink.jalo.rules.DeeplinkUrl;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBarcodeMedia extends Media
{
    public static final String BARCODETEXT = "barcodeText";
    public static final String BARCODETYPE = "barcodeType";
    public static final String CONTEXTITEM = "contextItem";
    public static final String DEEPLINKURL = "deeplinkUrl";
    protected static final BidirectionalOneToManyHandler<GeneratedBarcodeMedia> DEEPLINKURLHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.BARCODEMEDIA, false, "deeplinkUrl", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("barcodeText", Item.AttributeMode.INITIAL);
        tmp.put("barcodeType", Item.AttributeMode.INITIAL);
        tmp.put("contextItem", Item.AttributeMode.INITIAL);
        tmp.put("deeplinkUrl", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getBarcodeText(SessionContext ctx)
    {
        return (String)getProperty(ctx, "barcodeText");
    }


    public String getBarcodeText()
    {
        return getBarcodeText(getSession().getSessionContext());
    }


    public void setBarcodeText(SessionContext ctx, String value)
    {
        setProperty(ctx, "barcodeText", value);
    }


    public void setBarcodeText(String value)
    {
        setBarcodeText(getSession().getSessionContext(), value);
    }


    public EnumerationValue getBarcodeType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "barcodeType");
    }


    public EnumerationValue getBarcodeType()
    {
        return getBarcodeType(getSession().getSessionContext());
    }


    public void setBarcodeType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "barcodeType", value);
    }


    public void setBarcodeType(EnumerationValue value)
    {
        setBarcodeType(getSession().getSessionContext(), value);
    }


    public Item getContextItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "contextItem");
    }


    public Item getContextItem()
    {
        return getContextItem(getSession().getSessionContext());
    }


    public void setContextItem(SessionContext ctx, Item value)
    {
        setProperty(ctx, "contextItem", value);
    }


    public void setContextItem(Item value)
    {
        setContextItem(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        DEEPLINKURLHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public DeeplinkUrl getDeeplinkUrl(SessionContext ctx)
    {
        return (DeeplinkUrl)getProperty(ctx, "deeplinkUrl");
    }


    public DeeplinkUrl getDeeplinkUrl()
    {
        return getDeeplinkUrl(getSession().getSessionContext());
    }


    public void setDeeplinkUrl(SessionContext ctx, DeeplinkUrl value)
    {
        DEEPLINKURLHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setDeeplinkUrl(DeeplinkUrl value)
    {
        setDeeplinkUrl(getSession().getSessionContext(), value);
    }
}
