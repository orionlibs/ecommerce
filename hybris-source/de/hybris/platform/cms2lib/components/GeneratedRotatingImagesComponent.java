package de.hybris.platform.cms2lib.components;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedRotatingImagesComponent extends SimpleCMSComponent
{
    public static final String TIMEOUT = "timeout";
    public static final String EFFECT = "effect";
    public static final String BANNERS = "banners";
    protected static String BANNERSFORROTATINGCOMPONENT_SRC_ORDERED = "relation.BannersForRotatingComponent.source.ordered";
    protected static String BANNERSFORROTATINGCOMPONENT_TGT_ORDERED = "relation.BannersForRotatingComponent.target.ordered";
    protected static String BANNERSFORROTATINGCOMPONENT_MARKMODIFIED = "relation.BannersForRotatingComponent.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("timeout", Item.AttributeMode.INITIAL);
        tmp.put("effect", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<BannerComponent> getBanners(SessionContext ctx)
    {
        List<BannerComponent> items = getLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, "BannerComponent", null,
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public List<BannerComponent> getBanners()
    {
        return getBanners(getSession().getSessionContext());
    }


    public long getBannersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, "BannerComponent", null);
    }


    public long getBannersCount()
    {
        return getBannersCount(getSession().getSessionContext());
    }


    public void setBanners(SessionContext ctx, List<BannerComponent> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED));
    }


    public void setBanners(List<BannerComponent> value)
    {
        setBanners(getSession().getSessionContext(), value);
    }


    public void addToBanners(SessionContext ctx, BannerComponent value)
    {
        addLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED));
    }


    public void addToBanners(BannerComponent value)
    {
        addToBanners(getSession().getSessionContext(), value);
    }


    public void removeFromBanners(SessionContext ctx, BannerComponent value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.BANNERSFORROTATINGCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(BANNERSFORROTATINGCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED));
    }


    public void removeFromBanners(BannerComponent value)
    {
        removeFromBanners(getSession().getSessionContext(), value);
    }


    public EnumerationValue getEffect(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "effect");
    }


    public EnumerationValue getEffect()
    {
        return getEffect(getSession().getSessionContext());
    }


    public void setEffect(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "effect", value);
    }


    public void setEffect(EnumerationValue value)
    {
        setEffect(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("BannerComponent");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(BANNERSFORROTATINGCOMPONENT_MARKMODIFIED);
        }
        return true;
    }


    public Integer getTimeout(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "timeout");
    }


    public Integer getTimeout()
    {
        return getTimeout(getSession().getSessionContext());
    }


    public int getTimeoutAsPrimitive(SessionContext ctx)
    {
        Integer value = getTimeout(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getTimeoutAsPrimitive()
    {
        return getTimeoutAsPrimitive(getSession().getSessionContext());
    }


    public void setTimeout(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "timeout", value);
    }


    public void setTimeout(Integer value)
    {
        setTimeout(getSession().getSessionContext(), value);
    }


    public void setTimeout(SessionContext ctx, int value)
    {
        setTimeout(ctx, Integer.valueOf(value));
    }


    public void setTimeout(int value)
    {
        setTimeout(getSession().getSessionContext(), value);
    }
}
