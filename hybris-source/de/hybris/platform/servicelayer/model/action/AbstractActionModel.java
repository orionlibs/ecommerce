package de.hybris.platform.servicelayer.model.action;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractActionModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractAction";
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String TARGET = "target";


    public AbstractActionModel()
    {
    }


    public AbstractActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractActionModel(String _code, String _target, ActionType _type)
    {
        setCode(_code);
        setTarget(_target);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractActionModel(String _code, ItemModel _owner, String _target, ActionType _type)
    {
        setCode(_code);
        setOwner(_owner);
        setTarget(_target);
        setType(_type);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "target", type = Accessor.Type.GETTER)
    public String getTarget()
    {
        return (String)getPersistenceContext().getPropertyValue("target");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public ActionType getType()
    {
        return (ActionType)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "target", type = Accessor.Type.SETTER)
    public void setTarget(String value)
    {
        getPersistenceContext().setPropertyValue("target", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(ActionType value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
