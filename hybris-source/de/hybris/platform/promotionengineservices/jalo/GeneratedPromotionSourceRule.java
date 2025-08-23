package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotionengineservices.constants.GeneratedPromotionEngineServicesConstants;
import de.hybris.platform.promotions.jalo.PromotionGroup;
import de.hybris.platform.ruleengineservices.jalo.SourceRule;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionSourceRule extends SourceRule
{
    public static final String EXCLUDEFROMSTOREFRONTDISPLAY = "excludeFromStorefrontDisplay";
    public static final String WEBSITE = "website";
    protected static final BidirectionalOneToManyHandler<GeneratedPromotionSourceRule> WEBSITEHANDLER = new BidirectionalOneToManyHandler(GeneratedPromotionEngineServicesConstants.TC.PROMOTIONSOURCERULE, false, "website", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SourceRule.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("excludeFromStorefrontDisplay", Item.AttributeMode.INITIAL);
        tmp.put("website", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WEBSITEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isExcludeFromStorefrontDisplay(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "excludeFromStorefrontDisplay");
    }


    public Boolean isExcludeFromStorefrontDisplay()
    {
        return isExcludeFromStorefrontDisplay(getSession().getSessionContext());
    }


    public boolean isExcludeFromStorefrontDisplayAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExcludeFromStorefrontDisplay(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExcludeFromStorefrontDisplayAsPrimitive()
    {
        return isExcludeFromStorefrontDisplayAsPrimitive(getSession().getSessionContext());
    }


    public void setExcludeFromStorefrontDisplay(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "excludeFromStorefrontDisplay", value);
    }


    public void setExcludeFromStorefrontDisplay(Boolean value)
    {
        setExcludeFromStorefrontDisplay(getSession().getSessionContext(), value);
    }


    public void setExcludeFromStorefrontDisplay(SessionContext ctx, boolean value)
    {
        setExcludeFromStorefrontDisplay(ctx, Boolean.valueOf(value));
    }


    public void setExcludeFromStorefrontDisplay(boolean value)
    {
        setExcludeFromStorefrontDisplay(getSession().getSessionContext(), value);
    }


    public PromotionGroup getWebsite(SessionContext ctx)
    {
        return (PromotionGroup)getProperty(ctx, "website");
    }


    public PromotionGroup getWebsite()
    {
        return getWebsite(getSession().getSessionContext());
    }


    public void setWebsite(SessionContext ctx, PromotionGroup value)
    {
        WEBSITEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWebsite(PromotionGroup value)
    {
        setWebsite(getSession().getSessionContext(), value);
    }
}
