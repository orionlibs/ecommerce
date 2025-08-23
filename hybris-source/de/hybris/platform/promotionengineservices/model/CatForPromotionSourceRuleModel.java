package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CatForPromotionSourceRuleModel extends ItemModel
{
    public static final String _TYPECODE = "CatForPromotionSourceRule";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String RULE = "rule";
    public static final String PROMOTION = "promotion";


    public CatForPromotionSourceRuleModel()
    {
    }


    public CatForPromotionSourceRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatForPromotionSourceRuleModel(String _categoryCode, RuleBasedPromotionModel _promotion, PromotionSourceRuleModel _rule)
    {
        setCategoryCode(_categoryCode);
        setPromotion(_promotion);
        setRule(_rule);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatForPromotionSourceRuleModel(String _categoryCode, ItemModel _owner, RuleBasedPromotionModel _promotion, PromotionSourceRuleModel _rule)
    {
        setCategoryCode(_categoryCode);
        setOwner(_owner);
        setPromotion(_promotion);
        setRule(_rule);
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
    public String getCategoryCode()
    {
        return (String)getPersistenceContext().getPropertyValue("categoryCode");
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
    public RuleBasedPromotionModel getPromotion()
    {
        return (RuleBasedPromotionModel)getPersistenceContext().getPropertyValue("promotion");
    }


    @Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
    public PromotionSourceRuleModel getRule()
    {
        return (PromotionSourceRuleModel)getPersistenceContext().getPropertyValue("rule");
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.SETTER)
    public void setCategoryCode(String value)
    {
        getPersistenceContext().setPropertyValue("categoryCode", value);
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
    public void setPromotion(RuleBasedPromotionModel value)
    {
        getPersistenceContext().setPropertyValue("promotion", value);
    }


    @Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
    public void setRule(PromotionSourceRuleModel value)
    {
        getPersistenceContext().setPropertyValue("rule", value);
    }
}
