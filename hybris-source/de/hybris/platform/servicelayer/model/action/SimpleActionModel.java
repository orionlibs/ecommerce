package de.hybris.platform.servicelayer.model.action;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SimpleActionModel extends AbstractActionModel
{
    public static final String _TYPECODE = "SimpleAction";


    public SimpleActionModel()
    {
    }


    public SimpleActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleActionModel(String _code, String _target, ActionType _type)
    {
        setCode(_code);
        setTarget(_target);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleActionModel(String _code, ItemModel _owner, String _target, ActionType _type)
    {
        setCode(_code);
        setOwner(_owner);
        setTarget(_target);
        setType(_type);
    }
}
