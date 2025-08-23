package de.hybris.platform.promotionengineservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleTemplateModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionSourceRuleTemplateModel extends SourceRuleTemplateModel
{
    public static final String _TYPECODE = "PromotionSourceRuleTemplate";


    public PromotionSourceRuleTemplateModel()
    {
    }


    public PromotionSourceRuleTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionSourceRuleTemplateModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionSourceRuleTemplateModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }
}
