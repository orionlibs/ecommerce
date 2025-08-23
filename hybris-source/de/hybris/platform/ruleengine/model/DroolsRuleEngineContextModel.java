package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DroolsRuleEngineContextModel extends AbstractRuleEngineContextModel
{
    public static final String _TYPECODE = "DroolsRuleEngineContext";
    public static final String KIESESSION = "kieSession";
    public static final String RULEFIRINGLIMIT = "ruleFiringLimit";


    public DroolsRuleEngineContextModel()
    {
    }


    public DroolsRuleEngineContextModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsRuleEngineContextModel(DroolsKIESessionModel _kieSession, String _name)
    {
        setKieSession(_kieSession);
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsRuleEngineContextModel(DroolsKIESessionModel _kieSession, String _name, ItemModel _owner)
    {
        setKieSession(_kieSession);
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "kieSession", type = Accessor.Type.GETTER)
    public DroolsKIESessionModel getKieSession()
    {
        return (DroolsKIESessionModel)getPersistenceContext().getPropertyValue("kieSession");
    }


    @Accessor(qualifier = "ruleFiringLimit", type = Accessor.Type.GETTER)
    public Long getRuleFiringLimit()
    {
        return (Long)getPersistenceContext().getPropertyValue("ruleFiringLimit");
    }


    @Accessor(qualifier = "kieSession", type = Accessor.Type.SETTER)
    public void setKieSession(DroolsKIESessionModel value)
    {
        getPersistenceContext().setPropertyValue("kieSession", value);
    }


    @Accessor(qualifier = "ruleFiringLimit", type = Accessor.Type.SETTER)
    public void setRuleFiringLimit(Long value)
    {
        getPersistenceContext().setPropertyValue("ruleFiringLimit", value);
    }
}
