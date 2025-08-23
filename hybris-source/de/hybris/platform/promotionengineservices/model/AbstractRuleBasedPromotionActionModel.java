package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class AbstractRuleBasedPromotionActionModel extends AbstractPromotionActionModel
{
    public static final String _TYPECODE = "AbstractRuleBasedPromotionAction";
    public static final String RULE = "rule";
    public static final String STRATEGYID = "strategyId";
    public static final String METADATAHANDLERS = "metadataHandlers";


    public AbstractRuleBasedPromotionActionModel()
    {
    }


    public AbstractRuleBasedPromotionActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleBasedPromotionActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "metadataHandlers", type = Accessor.Type.GETTER)
    public Collection<String> getMetadataHandlers()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("metadataHandlers");
    }


    @Accessor(qualifier = "rule", type = Accessor.Type.GETTER)
    public AbstractRuleEngineRuleModel getRule()
    {
        return (AbstractRuleEngineRuleModel)getPersistenceContext().getPropertyValue("rule");
    }


    @Accessor(qualifier = "strategyId", type = Accessor.Type.GETTER)
    public String getStrategyId()
    {
        return (String)getPersistenceContext().getPropertyValue("strategyId");
    }


    @Accessor(qualifier = "metadataHandlers", type = Accessor.Type.SETTER)
    public void setMetadataHandlers(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("metadataHandlers", value);
    }


    @Accessor(qualifier = "rule", type = Accessor.Type.SETTER)
    public void setRule(AbstractRuleEngineRuleModel value)
    {
        getPersistenceContext().setPropertyValue("rule", value);
    }


    @Accessor(qualifier = "strategyId", type = Accessor.Type.SETTER)
    public void setStrategyId(String value)
    {
        getPersistenceContext().setPropertyValue("strategyId", value);
    }
}
