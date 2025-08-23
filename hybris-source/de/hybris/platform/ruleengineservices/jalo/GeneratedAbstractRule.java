package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ruleengine.constants.GeneratedRuleEngineConstants;
import de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule;
import de.hybris.platform.ruleengine.jalo.AbstractRulesModule;
import de.hybris.platform.ruleengineservices.constants.GeneratedRuleEngineServicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedAbstractRule extends GenericItem
{
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
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractRule> RULEGROUPHANDLER = new BidirectionalOneToManyHandler(GeneratedRuleEngineServicesConstants.TC.ABSTRACTRULE, false, "ruleGroup", null, false, true, 1);
    protected static final OneToManyHandler<AbstractRuleEngineRule> ENGINERULESHANDLER = new OneToManyHandler(GeneratedRuleEngineConstants.TC.ABSTRACTRULEENGINERULE, false, "sourceRule", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uuid", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("startDate", Item.AttributeMode.INITIAL);
        tmp.put("endDate", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("maxAllowedRuns", Item.AttributeMode.INITIAL);
        tmp.put("stackable", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("version", Item.AttributeMode.INITIAL);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
        tmp.put("rulesModules", Item.AttributeMode.INITIAL);
        tmp.put("ruleGroup", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        RULEGROUPHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRule.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRule.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public Date getEndDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endDate");
    }


    public Date getEndDate()
    {
        return getEndDate(getSession().getSessionContext());
    }


    public void setEndDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endDate", value);
    }


    public void setEndDate(Date value)
    {
        setEndDate(getSession().getSessionContext(), value);
    }


    public Set<AbstractRuleEngineRule> getEngineRules(SessionContext ctx)
    {
        return (Set<AbstractRuleEngineRule>)ENGINERULESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<AbstractRuleEngineRule> getEngineRules()
    {
        return getEngineRules(getSession().getSessionContext());
    }


    public void setEngineRules(SessionContext ctx, Set<AbstractRuleEngineRule> value)
    {
        ENGINERULESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEngineRules(Set<AbstractRuleEngineRule> value)
    {
        setEngineRules(getSession().getSessionContext(), value);
    }


    public void addToEngineRules(SessionContext ctx, AbstractRuleEngineRule value)
    {
        ENGINERULESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEngineRules(AbstractRuleEngineRule value)
    {
        addToEngineRules(getSession().getSessionContext(), value);
    }


    public void removeFromEngineRules(SessionContext ctx, AbstractRuleEngineRule value)
    {
        ENGINERULESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEngineRules(AbstractRuleEngineRule value)
    {
        removeFromEngineRules(getSession().getSessionContext(), value);
    }


    public Integer getMaxAllowedRuns(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxAllowedRuns");
    }


    public Integer getMaxAllowedRuns()
    {
        return getMaxAllowedRuns(getSession().getSessionContext());
    }


    public int getMaxAllowedRunsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxAllowedRuns(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxAllowedRunsAsPrimitive()
    {
        return getMaxAllowedRunsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxAllowedRuns(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxAllowedRuns", value);
    }


    public void setMaxAllowedRuns(Integer value)
    {
        setMaxAllowedRuns(getSession().getSessionContext(), value);
    }


    public void setMaxAllowedRuns(SessionContext ctx, int value)
    {
        setMaxAllowedRuns(ctx, Integer.valueOf(value));
    }


    public void setMaxAllowedRuns(int value)
    {
        setMaxAllowedRuns(getSession().getSessionContext(), value);
    }


    public String getMessageFired(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRule.getMessageFired requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "messageFired");
    }


    public String getMessageFired()
    {
        return getMessageFired(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessageFired(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "messageFired", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageFired()
    {
        return getAllMessageFired(getSession().getSessionContext());
    }


    public void setMessageFired(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRule.setMessageFired requires a session language", 0);
        }
        setLocalizedProperty(ctx, "messageFired", value);
    }


    public void setMessageFired(String value)
    {
        setMessageFired(getSession().getSessionContext(), value);
    }


    public void setAllMessageFired(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "messageFired", value);
    }


    public void setAllMessageFired(Map<Language, String> value)
    {
        setAllMessageFired(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRule.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractRule.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public RuleGroup getRuleGroup(SessionContext ctx)
    {
        return (RuleGroup)getProperty(ctx, "ruleGroup");
    }


    public RuleGroup getRuleGroup()
    {
        return getRuleGroup(getSession().getSessionContext());
    }


    public void setRuleGroup(SessionContext ctx, RuleGroup value)
    {
        RULEGROUPHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setRuleGroup(RuleGroup value)
    {
        setRuleGroup(getSession().getSessionContext(), value);
    }


    public List<AbstractRulesModule> getRulesModules(SessionContext ctx)
    {
        List<AbstractRulesModule> coll = (List<AbstractRulesModule>)getProperty(ctx, "rulesModules");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<AbstractRulesModule> getRulesModules()
    {
        return getRulesModules(getSession().getSessionContext());
    }


    public void setRulesModules(SessionContext ctx, List<AbstractRulesModule> value)
    {
        setProperty(ctx, "rulesModules", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setRulesModules(List<AbstractRulesModule> value)
    {
        setRulesModules(getSession().getSessionContext(), value);
    }


    public Boolean isStackable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "stackable");
    }


    public Boolean isStackable()
    {
        return isStackable(getSession().getSessionContext());
    }


    public boolean isStackableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isStackable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isStackableAsPrimitive()
    {
        return isStackableAsPrimitive(getSession().getSessionContext());
    }


    public void setStackable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "stackable", value);
    }


    public void setStackable(Boolean value)
    {
        setStackable(getSession().getSessionContext(), value);
    }


    public void setStackable(SessionContext ctx, boolean value)
    {
        setStackable(ctx, Boolean.valueOf(value));
    }


    public void setStackable(boolean value)
    {
        setStackable(getSession().getSessionContext(), value);
    }


    public Date getStartDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startDate");
    }


    public Date getStartDate()
    {
        return getStartDate(getSession().getSessionContext());
    }


    public void setStartDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startDate", value);
    }


    public void setStartDate(Date value)
    {
        setStartDate(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public String getUuid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uuid");
    }


    public String getUuid()
    {
        return getUuid(getSession().getSessionContext());
    }


    protected void setUuid(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'uuid' is not changeable", 0);
        }
        setProperty(ctx, "uuid", value);
    }


    protected void setUuid(String value)
    {
        setUuid(getSession().getSessionContext(), value);
    }


    public Long getVersion(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "version");
    }


    public Long getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    public long getVersionAsPrimitive(SessionContext ctx)
    {
        Long value = getVersion(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getVersionAsPrimitive()
    {
        return getVersionAsPrimitive(getSession().getSessionContext());
    }


    public void setVersion(SessionContext ctx, Long value)
    {
        setProperty(ctx, "version", value);
    }


    public void setVersion(Long value)
    {
        setVersion(getSession().getSessionContext(), value);
    }


    public void setVersion(SessionContext ctx, long value)
    {
        setVersion(ctx, Long.valueOf(value));
    }


    public void setVersion(long value)
    {
        setVersion(getSession().getSessionContext(), value);
    }
}
