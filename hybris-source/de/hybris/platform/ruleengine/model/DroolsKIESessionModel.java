package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DroolsKIESessionModel extends ItemModel
{
    public static final String _TYPECODE = "DroolsKIESession";
    public static final String _DROOLSKIEBASE2SESSION = "DroolsKIEBase2Session";
    public static final String NAME = "name";
    public static final String SESSIONTYPE = "sessionType";
    public static final String KIEBASE = "kieBase";


    public DroolsKIESessionModel()
    {
    }


    public DroolsKIESessionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIESessionModel(String _name)
    {
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIESessionModel(String _name, ItemModel _owner)
    {
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "kieBase", type = Accessor.Type.GETTER)
    public DroolsKIEBaseModel getKieBase()
    {
        return (DroolsKIEBaseModel)getPersistenceContext().getPropertyValue("kieBase");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "sessionType", type = Accessor.Type.GETTER)
    public DroolsSessionType getSessionType()
    {
        return (DroolsSessionType)getPersistenceContext().getPropertyValue("sessionType");
    }


    @Accessor(qualifier = "kieBase", type = Accessor.Type.SETTER)
    public void setKieBase(DroolsKIEBaseModel value)
    {
        getPersistenceContext().setPropertyValue("kieBase", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "sessionType", type = Accessor.Type.SETTER)
    public void setSessionType(DroolsSessionType value)
    {
        getPersistenceContext().setPropertyValue("sessionType", value);
    }
}
