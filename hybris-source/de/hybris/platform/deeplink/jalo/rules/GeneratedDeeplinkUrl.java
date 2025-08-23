package de.hybris.platform.deeplink.jalo.rules;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.deeplink.jalo.media.BarcodeMedia;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDeeplinkUrl extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String BASEURL = "baseUrl";
    public static final String BARCODEMEDIAS = "barcodeMedias";
    protected static final OneToManyHandler<BarcodeMedia> BARCODEMEDIASHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.BARCODEMEDIA, false, "deeplinkUrl", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("baseUrl", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<BarcodeMedia> getBarcodeMedias(SessionContext ctx)
    {
        return BARCODEMEDIASHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<BarcodeMedia> getBarcodeMedias()
    {
        return getBarcodeMedias(getSession().getSessionContext());
    }


    public void setBarcodeMedias(SessionContext ctx, Collection<BarcodeMedia> value)
    {
        BARCODEMEDIASHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setBarcodeMedias(Collection<BarcodeMedia> value)
    {
        setBarcodeMedias(getSession().getSessionContext(), value);
    }


    public void addToBarcodeMedias(SessionContext ctx, BarcodeMedia value)
    {
        BARCODEMEDIASHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToBarcodeMedias(BarcodeMedia value)
    {
        addToBarcodeMedias(getSession().getSessionContext(), value);
    }


    public void removeFromBarcodeMedias(SessionContext ctx, BarcodeMedia value)
    {
        BARCODEMEDIASHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromBarcodeMedias(BarcodeMedia value)
    {
        removeFromBarcodeMedias(getSession().getSessionContext(), value);
    }


    public String getBaseUrl(SessionContext ctx)
    {
        return (String)getProperty(ctx, "baseUrl");
    }


    public String getBaseUrl()
    {
        return getBaseUrl(getSession().getSessionContext());
    }


    public void setBaseUrl(SessionContext ctx, String value)
    {
        setProperty(ctx, "baseUrl", value);
    }


    public void setBaseUrl(String value)
    {
        setBaseUrl(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }
}
