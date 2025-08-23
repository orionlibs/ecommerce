package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AbstractRuleModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractRule";
    public static final String _RULEGROUP2ABSTRACTRULE = "RuleGroup2AbstractRule";
    public static final String UUID = "uuid";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String PRIORITY = "priority";
    public static final String MAXALLOWEDRUNS = "maxAllowedRuns";
    public static final String STACKABLE = "stackable";
    public static final String STATUS = "status";
    public static final String VERSION = "version";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String RULESMODULES = "rulesModules";
    public static final String RULEGROUP = "ruleGroup";
    public static final String ENGINERULES = "engineRules";
    public static final String SAPCONDITIONTYPE = "sapConditionType";


    public AbstractRuleModel()
    {
    }


    public AbstractRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleModel(String _code, ItemModel _owner, String _uuid)
    {
        setCode(_code);
        setOwner(_owner);
        setUuid(_uuid);
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


    @Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
    public Date getEndDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("endDate");
    }


    @Accessor(qualifier = "engineRules", type = Accessor.Type.GETTER)
    public Set<AbstractRuleEngineRuleModel> getEngineRules()
    {
        return (Set<AbstractRuleEngineRuleModel>)getPersistenceContext().getPropertyValue("engineRules");
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


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "ruleGroup", type = Accessor.Type.GETTER)
    public RuleGroupModel getRuleGroup()
    {
        return (RuleGroupModel)getPersistenceContext().getPropertyValue("ruleGroup");
    }


    @Accessor(qualifier = "rulesModules", type = Accessor.Type.GETTER)
    public List<AbstractRulesModuleModel> getRulesModules()
    {
        return (List<AbstractRulesModuleModel>)getPersistenceContext().getPropertyValue("rulesModules");
    }


    @Accessor(qualifier = "sapConditionType", type = Accessor.Type.GETTER)
    public String getSapConditionType()
    {
        return (String)getPersistenceContext().getPropertyValue("sapConditionType");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "stackable", type = Accessor.Type.GETTER)
    public Boolean getStackable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("stackable");
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
    public Date getStartDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("startDate");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public RuleStatus getStatus()
    {
        return (RuleStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "uuid", type = Accessor.Type.GETTER)
    public String getUuid()
    {
        return (String)getPersistenceContext().getPropertyValue("uuid");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public Long getVersion()
    {
        return (Long)getPersistenceContext().getPropertyValue("version");
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


    @Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
    public void setEndDate(Date value)
    {
        getPersistenceContext().setPropertyValue("endDate", value);
    }


    @Accessor(qualifier = "engineRules", type = Accessor.Type.SETTER)
    public void setEngineRules(Set<AbstractRuleEngineRuleModel> value)
    {
        getPersistenceContext().setPropertyValue("engineRules", value);
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


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "ruleGroup", type = Accessor.Type.SETTER)
    public void setRuleGroup(RuleGroupModel value)
    {
        getPersistenceContext().setPropertyValue("ruleGroup", value);
    }


    @Accessor(qualifier = "rulesModules", type = Accessor.Type.SETTER)
    public void setRulesModules(List<AbstractRulesModuleModel> value)
    {
        getPersistenceContext().setPropertyValue("rulesModules", value);
    }


    @Accessor(qualifier = "sapConditionType", type = Accessor.Type.SETTER)
    public void setSapConditionType(String value)
    {
        getPersistenceContext().setPropertyValue("sapConditionType", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "stackable", type = Accessor.Type.SETTER)
    public void setStackable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("stackable", value);
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
    public void setStartDate(Date value)
    {
        getPersistenceContext().setPropertyValue("startDate", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(RuleStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "uuid", type = Accessor.Type.SETTER)
    public void setUuid(String value)
    {
        getPersistenceContext().setPropertyValue("uuid", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
