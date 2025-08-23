package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CombinedCatsForRuleModel extends ItemModel
{
    public static final String _TYPECODE = "CombinedCatsForRule";
    public static final String RULE = "rule";
    public static final String CONDITIONID = "conditionId";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String PROMOTION = "promotion";


    public CombinedCatsForRuleModel()
    {
    }


    public CombinedCatsForRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CombinedCatsForRuleModel(String _categoryCode, Integer _conditionId, RuleBasedPromotionModel _promotion, PromotionSourceRuleModel _rule)
    {
        setCategoryCode(_categoryCode);
        setConditionId(_conditionId);
        setPromotion(_promotion);
        setRule(_rule);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CombinedCatsForRuleModel(String _categoryCode, Integer _conditionId, ItemModel _owner, RuleBasedPromotionModel _promotion, PromotionSourceRuleModel _rule)
    {
        setCategoryCode(_categoryCode);
        setConditionId(_conditionId);
        setOwner(_owner);
        setPromotion(_promotion);
        setRule(_rule);
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
    public String getCategoryCode()
    {
        return (String)getPersistenceContext().getPropertyValue("categoryCode");
    }


    @Accessor(qualifier = "conditionId", type = Accessor.Type.GETTER)
    public Integer getConditionId()
    {
        return (Integer)getPersistenceContext().getPropertyValue("conditionId");
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


    @Accessor(qualifier = "conditionId", type = Accessor.Type.SETTER)
    public void setConditionId(Integer value)
    {
        getPersistenceContext().setPropertyValue("conditionId", value);
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
