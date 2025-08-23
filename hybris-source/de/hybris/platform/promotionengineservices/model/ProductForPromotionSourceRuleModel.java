package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProductForPromotionSourceRuleModel extends ItemModel
{
    public static final String _TYPECODE = "ProductForPromotionSourceRule";
    public static final String PRODUCTCODE = "productCode";
    public static final String RULE = "rule";
    public static final String PROMOTION = "promotion";


    public ProductForPromotionSourceRuleModel()
    {
    }


    public ProductForPromotionSourceRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductForPromotionSourceRuleModel(String _productCode, RuleBasedPromotionModel _promotion, PromotionSourceRuleModel _rule)
    {
        setProductCode(_productCode);
        setPromotion(_promotion);
        setRule(_rule);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductForPromotionSourceRuleModel(ItemModel _owner, String _productCode, RuleBasedPromotionModel _promotion, PromotionSourceRuleModel _rule)
    {
        setOwner(_owner);
        setProductCode(_productCode);
        setPromotion(_promotion);
        setRule(_rule);
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
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


    @Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
    public void setProductCode(String value)
    {
        getPersistenceContext().setPropertyValue("productCode", value);
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
