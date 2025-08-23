package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class RuleBasedPromotionModel extends AbstractPromotionModel
{
    public static final String _TYPECODE = "RuleBasedPromotion";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String RULE = "rule";
    public static final String PROMOTIONDESCRIPTION = "promotionDescription";
    public static final String RULEVERSION = "ruleVersion";


    public RuleBasedPromotionModel()
    {
    }


    public RuleBasedPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired()
    {
        return getMessageFired(null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageFired", loc);
    }


    @Accessor(qualifier = "promotionDescription", type = Accessor.Type.GETTER)
    public String getPromotionDescription()
    {
        return getPromotionDescription(null);
    }


    @Accessor(qualifier = "promotionDescription", type = Accessor.Type.GETTER)
    public String getPromotionDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("promotionDescription", loc);
    }


    @Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
    public AbstractRuleEngineRuleModel getRule()
    {
        return (AbstractRuleEngineRuleModel)getPersistenceContext().getPropertyValue("rule");
    }


    @Accessor(qualifier = "ruleVersion", type = Accessor.Type.GETTER)
    public Long getRuleVersion()
    {
        return (Long)getPersistenceContext().getPropertyValue("ruleVersion");
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value)
    {
        setMessageFired(value, null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageFired", loc, value);
    }


    @Accessor(qualifier = "promotionDescription", type = Accessor.Type.SETTER)
    public void setPromotionDescription(String value)
    {
        setPromotionDescription(value, null);
    }


    @Accessor(qualifier = "promotionDescription", type = Accessor.Type.SETTER)
    public void setPromotionDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("promotionDescription", loc, value);
    }


    @Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
    public void setRule(AbstractRuleEngineRuleModel value)
    {
        getPersistenceContext().setPropertyValue("rule", value);
    }


    @Accessor(qualifier = "ruleVersion", type = Accessor.Type.SETTER)
    public void setRuleVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("ruleVersion", value);
    }
}
