package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ruleengine.constants.GeneratedRuleEngineConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedDroolsKIEBase extends GenericItem
{
    public static final String NAME = "name";
    public static final String EQUALITYBEHAVIOR = "equalityBehavior";
    public static final String EVENTPROCESSINGMODE = "eventProcessingMode";
    public static final String DEFAULTKIESESSION = "defaultKIESession";
    public static final String KIEMODULE = "kieModule";
    public static final String KIESESSIONS = "kieSessions";
    public static final String RULES = "rules";
    protected static final BidirectionalOneToManyHandler<GeneratedDroolsKIEBase> KIEMODULEHANDLER = new BidirectionalOneToManyHandler(GeneratedRuleEngineConstants.TC.DROOLSKIEBASE, false, "kieModule", null, false, true, 0);
    protected static final OneToManyHandler<DroolsKIESession> KIESESSIONSHANDLER = new OneToManyHandler(GeneratedRuleEngineConstants.TC.DROOLSKIESESSION, false, "kieBase", null, false, true, 0);
    protected static final OneToManyHandler<DroolsRule> RULESHANDLER = new OneToManyHandler(GeneratedRuleEngineConstants.TC.DROOLSRULE, false, "kieBase", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("equalityBehavior", Item.AttributeMode.INITIAL);
        tmp.put("eventProcessingMode", Item.AttributeMode.INITIAL);
        tmp.put("defaultKIESession", Item.AttributeMode.INITIAL);
        tmp.put("kieModule", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        KIEMODULEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public DroolsKIESession getDefaultKIESession(SessionContext ctx)
    {
        return (DroolsKIESession)getProperty(ctx, "defaultKIESession");
    }


    public DroolsKIESession getDefaultKIESession()
    {
        return getDefaultKIESession(getSession().getSessionContext());
    }


    public void setDefaultKIESession(SessionContext ctx, DroolsKIESession value)
    {
        setProperty(ctx, "defaultKIESession", value);
    }


    public void setDefaultKIESession(DroolsKIESession value)
    {
        setDefaultKIESession(getSession().getSessionContext(), value);
    }


    public EnumerationValue getEqualityBehavior(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "equalityBehavior");
    }


    public EnumerationValue getEqualityBehavior()
    {
        return getEqualityBehavior(getSession().getSessionContext());
    }


    public void setEqualityBehavior(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "equalityBehavior", value);
    }


    public void setEqualityBehavior(EnumerationValue value)
    {
        setEqualityBehavior(getSession().getSessionContext(), value);
    }


    public EnumerationValue getEventProcessingMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "eventProcessingMode");
    }


    public EnumerationValue getEventProcessingMode()
    {
        return getEventProcessingMode(getSession().getSessionContext());
    }


    public void setEventProcessingMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "eventProcessingMode", value);
    }


    public void setEventProcessingMode(EnumerationValue value)
    {
        setEventProcessingMode(getSession().getSessionContext(), value);
    }


    public DroolsKIEModule getKieModule(SessionContext ctx)
    {
        return (DroolsKIEModule)getProperty(ctx, "kieModule");
    }


    public DroolsKIEModule getKieModule()
    {
        return getKieModule(getSession().getSessionContext());
    }


    protected void setKieModule(SessionContext ctx, DroolsKIEModule value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'kieModule' is not changeable", 0);
        }
        KIEMODULEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setKieModule(DroolsKIEModule value)
    {
        setKieModule(getSession().getSessionContext(), value);
    }


    public Collection<DroolsKIESession> getKieSessions(SessionContext ctx)
    {
        return KIESESSIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<DroolsKIESession> getKieSessions()
    {
        return getKieSessions(getSession().getSessionContext());
    }


    public void setKieSessions(SessionContext ctx, Collection<DroolsKIESession> value)
    {
        KIESESSIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setKieSessions(Collection<DroolsKIESession> value)
    {
        setKieSessions(getSession().getSessionContext(), value);
    }


    public void addToKieSessions(SessionContext ctx, DroolsKIESession value)
    {
        KIESESSIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToKieSessions(DroolsKIESession value)
    {
        addToKieSessions(getSession().getSessionContext(), value);
    }


    public void removeFromKieSessions(SessionContext ctx, DroolsKIESession value)
    {
        KIESESSIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromKieSessions(DroolsKIESession value)
    {
        removeFromKieSessions(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Set<DroolsRule> getRules(SessionContext ctx)
    {
        return (Set<DroolsRule>)RULESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<DroolsRule> getRules()
    {
        return getRules(getSession().getSessionContext());
    }


    public void setRules(SessionContext ctx, Set<DroolsRule> value)
    {
        RULESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setRules(Set<DroolsRule> value)
    {
        setRules(getSession().getSessionContext(), value);
    }


    public void addToRules(SessionContext ctx, DroolsRule value)
    {
        RULESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToRules(DroolsRule value)
    {
        addToRules(getSession().getSessionContext(), value);
    }


    public void removeFromRules(SessionContext ctx, DroolsRule value)
    {
        RULESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromRules(DroolsRule value)
    {
        removeFromRules(getSession().getSessionContext(), value);
    }
}
