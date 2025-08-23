package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

public class DroolsRuleModel extends AbstractRuleEngineRuleModel
{
    public static final String _TYPECODE = "DroolsRule";
    public static final String _DROOLSKIEBASE2RULE = "DroolsKIEBase2Rule";
    public static final String RULEPACKAGE = "rulePackage";
    public static final String GLOBALS = "globals";
    public static final String KIEBASE = "kieBase";


    public DroolsRuleModel()
    {
    }


    public DroolsRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsRuleModel(String _code, RuleType _ruleType, String _uuid, Long _version)
    {
        setCode(_code);
        setRuleType(_ruleType);
        setUuid(_uuid);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsRuleModel(String _code, ItemModel _owner, RuleType _ruleType, String _uuid, Long _version)
    {
        setCode(_code);
        setOwner(_owner);
        setRuleType(_ruleType);
        setUuid(_uuid);
        setVersion(_version);
    }


    @Accessor(qualifier = "globals", type = Accessor.Type.GETTER)
    public Map<String, String> getGlobals()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("globals");
    }


    @Accessor(qualifier = "kieBase", type = Accessor.Type.GETTER)
    public DroolsKIEBaseModel getKieBase()
    {
        return (DroolsKIEBaseModel)getPersistenceContext().getPropertyValue("kieBase");
    }


    @Accessor(qualifier = "rulePackage", type = Accessor.Type.GETTER)
    public String getRulePackage()
    {
        return (String)getPersistenceContext().getPropertyValue("rulePackage");
    }


    @Accessor(qualifier = "globals", type = Accessor.Type.SETTER)
    public void setGlobals(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("globals", value);
    }


    @Accessor(qualifier = "kieBase", type = Accessor.Type.SETTER)
    public void setKieBase(DroolsKIEBaseModel value)
    {
        getPersistenceContext().setPropertyValue("kieBase", value);
    }


    @Accessor(qualifier = "rulePackage", type = Accessor.Type.SETTER)
    public void setRulePackage(String value)
    {
        getPersistenceContext().setPropertyValue("rulePackage", value);
    }
}
