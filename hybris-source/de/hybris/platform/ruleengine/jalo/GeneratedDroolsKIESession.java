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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDroolsKIESession extends GenericItem
{
    public static final String NAME = "name";
    public static final String SESSIONTYPE = "sessionType";
    public static final String KIEBASE = "kieBase";
    protected static final BidirectionalOneToManyHandler<GeneratedDroolsKIESession> KIEBASEHANDLER = new BidirectionalOneToManyHandler(GeneratedRuleEngineConstants.TC.DROOLSKIESESSION, false, "kieBase", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("sessionType", Item.AttributeMode.INITIAL);
        tmp.put("kieBase", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        KIEBASEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public DroolsKIEBase getKieBase(SessionContext ctx)
    {
        return (DroolsKIEBase)getProperty(ctx, "kieBase");
    }


    public DroolsKIEBase getKieBase()
    {
        return getKieBase(getSession().getSessionContext());
    }


    public void setKieBase(SessionContext ctx, DroolsKIEBase value)
    {
        KIEBASEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setKieBase(DroolsKIEBase value)
    {
        setKieBase(getSession().getSessionContext(), value);
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


    public EnumerationValue getSessionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "sessionType");
    }


    public EnumerationValue getSessionType()
    {
        return getSessionType(getSession().getSessionContext());
    }


    public void setSessionType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "sessionType", value);
    }


    public void setSessionType(EnumerationValue value)
    {
        setSessionType(getSession().getSessionContext(), value);
    }
}
