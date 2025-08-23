package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractRuleTemplateModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractRuleTemplate";
    public static final String _RULEGROUP2ABSTRACTRULETEMPLATE = "RuleGroup2AbstractRuleTemplate";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String MAXALLOWEDRUNS = "maxAllowedRuns";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String RULEGROUP = "ruleGroup";


    public AbstractRuleTemplateModel()
    {
    }


    public AbstractRuleTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleTemplateModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleTemplateModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.GETTER)
    public Integer getMaxAllowedRuns()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxAllowedRuns");
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


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "ruleGroup", type = Accessor.Type.GETTER)
    public RuleGroupModel getRuleGroup()
    {
        return (RuleGroupModel)getPersistenceContext().getPropertyValue("ruleGroup");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.SETTER)
    public void setMaxAllowedRuns(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxAllowedRuns", value);
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


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "ruleGroup", type = Accessor.Type.SETTER)
    public void setRuleGroup(RuleGroupModel value)
    {
        getPersistenceContext().setPropertyValue("ruleGroup", value);
    }
}
