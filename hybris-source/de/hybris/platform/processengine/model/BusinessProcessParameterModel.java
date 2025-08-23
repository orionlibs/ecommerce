package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BusinessProcessParameterModel extends ItemModel
{
    public static final String _TYPECODE = "BusinessProcessParameter";
    public static final String _PROCESS2PROCESSPARAMETERRELATION = "Process2ProcessParameterRelation";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String PROCESS = "process";


    public BusinessProcessParameterModel()
    {
    }


    public BusinessProcessParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BusinessProcessParameterModel(String _name, BusinessProcessModel _process, Object _value)
    {
        setName(_name);
        setProcess(_process);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BusinessProcessParameterModel(String _name, ItemModel _owner, BusinessProcessModel _process, Object _value)
    {
        setName(_name);
        setOwner(_owner);
        setProcess(_process);
        setValue(_value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "process", type = Accessor.Type.GETTER)
    public BusinessProcessModel getProcess()
    {
        return (BusinessProcessModel)getPersistenceContext().getPropertyValue("process");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Object getValue()
    {
        return getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "process", type = Accessor.Type.SETTER)
    public void setProcess(BusinessProcessModel value)
    {
        getPersistenceContext().setPropertyValue("process", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Object value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
