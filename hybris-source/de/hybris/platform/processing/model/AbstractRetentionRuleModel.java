package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractRetentionRuleModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractRetentionRule";
    public static final String CODE = "code";
    public static final String ACTIONREFERENCE = "actionReference";


    public AbstractRetentionRuleModel()
    {
    }


    public AbstractRetentionRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRetentionRuleModel(String _actionReference, String _code)
    {
        setActionReference(_actionReference);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRetentionRuleModel(String _actionReference, String _code, ItemModel _owner)
    {
        setActionReference(_actionReference);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "actionReference", type = Accessor.Type.GETTER)
    public String getActionReference()
    {
        return (String)getPersistenceContext().getPropertyValue("actionReference");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "actionReference", type = Accessor.Type.SETTER)
    public void setActionReference(String value)
    {
        getPersistenceContext().setPropertyValue("actionReference", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }
}
