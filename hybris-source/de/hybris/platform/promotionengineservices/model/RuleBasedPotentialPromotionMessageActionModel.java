package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class RuleBasedPotentialPromotionMessageActionModel extends AbstractRuleBasedPromotionActionModel
{
    public static final String _TYPECODE = "RuleBasedPotentialPromotionMessageAction";
    public static final String PARAMETERS = "parameters";


    public RuleBasedPotentialPromotionMessageActionModel()
    {
    }


    public RuleBasedPotentialPromotionMessageActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleBasedPotentialPromotionMessageActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "parameters", type = Accessor.Type.GETTER)
    public Collection<PromotionActionParameterModel> getParameters()
    {
        return (Collection<PromotionActionParameterModel>)getPersistenceContext().getPropertyValue("parameters");
    }


    @Accessor(qualifier = "parameters", type = Accessor.Type.SETTER)
    public void setParameters(Collection<PromotionActionParameterModel> value)
    {
        getPersistenceContext().setPropertyValue("parameters", value);
    }
}
