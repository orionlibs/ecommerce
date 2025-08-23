package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ExcludedProductForRuleModel extends ItemModel
{
    public static final String _TYPECODE = "ExcludedProductForRule";
    public static final String PRODUCTCODE = "productCode";
    public static final String RULE = "rule";


    public ExcludedProductForRuleModel()
    {
    }


    public ExcludedProductForRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExcludedProductForRuleModel(String _productCode, PromotionSourceRuleModel _rule)
    {
        setProductCode(_productCode);
        setRule(_rule);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExcludedProductForRuleModel(ItemModel _owner, String _productCode, PromotionSourceRuleModel _rule)
    {
        setOwner(_owner);
        setProductCode(_productCode);
        setRule(_rule);
    }


    @Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
    public String getProductCode()
    {
        return (String)getPersistenceContext().getPropertyValue("productCode");
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


    @Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
    public void setRule(PromotionSourceRuleModel value)
    {
        getPersistenceContext().setPropertyValue("rule", value);
    }
}
