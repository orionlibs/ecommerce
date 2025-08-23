package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

public class RuleGroupModel extends ItemModel
{
    public static final String _TYPECODE = "RuleGroup";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String EXCLUSIVE = "exclusive";
    public static final String RULES = "rules";
    public static final String RULETEMPLATES = "ruleTemplates";


    public RuleGroupModel()
    {
    }


    public RuleGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleGroupModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleGroupModel(String _code, ItemModel _owner)
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


    @Accessor(qualifier = "rules", type = Accessor.Type.GETTER)
    public Set<AbstractRuleModel> getRules()
    {
        return (Set<AbstractRuleModel>)getPersistenceContext().getPropertyValue("rules");
    }


    @Accessor(qualifier = "ruleTemplates", type = Accessor.Type.GETTER)
    public Set<AbstractRuleTemplateModel> getRuleTemplates()
    {
        return (Set<AbstractRuleTemplateModel>)getPersistenceContext().getPropertyValue("ruleTemplates");
    }


    @Accessor(qualifier = "exclusive", type = Accessor.Type.GETTER)
    public boolean isExclusive()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("exclusive"));
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


    @Accessor(qualifier = "exclusive", type = Accessor.Type.SETTER)
    public void setExclusive(boolean value)
    {
        getPersistenceContext().setPropertyValue("exclusive", toObject(value));
    }


    @Accessor(qualifier = "rules", type = Accessor.Type.SETTER)
    public void setRules(Set<AbstractRuleModel> value)
    {
        getPersistenceContext().setPropertyValue("rules", value);
    }


    @Accessor(qualifier = "ruleTemplates", type = Accessor.Type.SETTER)
    public void setRuleTemplates(Set<AbstractRuleTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("ruleTemplates", value);
    }
}
