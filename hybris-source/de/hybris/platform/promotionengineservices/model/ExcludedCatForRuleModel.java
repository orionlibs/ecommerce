package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ExcludedCatForRuleModel extends ItemModel
{
    public static final String _TYPECODE = "ExcludedCatForRule";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String RULE = "rule";


    public ExcludedCatForRuleModel()
    {
    }


    public ExcludedCatForRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExcludedCatForRuleModel(String _categoryCode, PromotionSourceRuleModel _rule)
    {
        setCategoryCode(_categoryCode);
        setRule(_rule);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExcludedCatForRuleModel(String _categoryCode, ItemModel _owner, PromotionSourceRuleModel _rule)
    {
        setCategoryCode(_categoryCode);
        setOwner(_owner);
        setRule(_rule);
    }


    @Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
    public String getCategoryCode()
    {
        return (String)getPersistenceContext().getPropertyValue("categoryCode");
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


    @Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
    public void setRule(PromotionSourceRuleModel value)
    {
        getPersistenceContext().setPropertyValue("rule", value);
    }
}
