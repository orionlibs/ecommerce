package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractRuleEngineContextModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractRuleEngineContext";
    public static final String NAME = "name";


    public AbstractRuleEngineContextModel()
    {
    }


    public AbstractRuleEngineContextModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleEngineContextModel(String _name)
    {
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleEngineContextModel(String _name, ItemModel _owner)
    {
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }
}
