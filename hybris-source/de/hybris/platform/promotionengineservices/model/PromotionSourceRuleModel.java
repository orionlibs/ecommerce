package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionSourceRuleModel extends SourceRuleModel
{
    public static final String _TYPECODE = "PromotionSourceRule";
    public static final String _PROMOTIONGROUP2PROMOTIONSOURCERULERELATION = "PromotionGroup2PromotionSourceRuleRelation";
    public static final String EXCLUDEFROMSTOREFRONTDISPLAY = "excludeFromStorefrontDisplay";
    public static final String WEBSITE = "website";


    public PromotionSourceRuleModel()
    {
    }


    public PromotionSourceRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionSourceRuleModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionSourceRuleModel(String _code, ItemModel _owner, String _uuid)
    {
        setCode(_code);
        setOwner(_owner);
        setUuid(_uuid);
    }


    @Accessor(qualifier = "excludeFromStorefrontDisplay", type = Accessor.Type.GETTER)
    public Boolean getExcludeFromStorefrontDisplay()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("excludeFromStorefrontDisplay");
    }


    @Accessor(qualifier = "website", type = Accessor.Type.GETTER)
    public PromotionGroupModel getWebsite()
    {
        return (PromotionGroupModel)getPersistenceContext().getPropertyValue("website");
    }


    @Accessor(qualifier = "excludeFromStorefrontDisplay", type = Accessor.Type.SETTER)
    public void setExcludeFromStorefrontDisplay(Boolean value)
    {
        getPersistenceContext().setPropertyValue("excludeFromStorefrontDisplay", value);
    }


    @Accessor(qualifier = "website", type = Accessor.Type.SETTER)
    public void setWebsite(PromotionGroupModel value)
    {
        getPersistenceContext().setPropertyValue("website", value);
    }
}
