package de.hybris.platform.fraud.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFraudSymptomScoring extends GenericItem
{
    public static final String NAME = "name";
    public static final String SCORE = "score";
    public static final String EXPLANATION = "explanation";
    public static final String FRAUDREPORT = "fraudReport";
    protected static final BidirectionalOneToManyHandler<GeneratedFraudSymptomScoring> FRAUDREPORTHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.FRAUDSYMPTOMSCORING, false, "fraudReport", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("score", Item.AttributeMode.INITIAL);
        tmp.put("explanation", Item.AttributeMode.INITIAL);
        tmp.put("fraudReport", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        FRAUDREPORTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getExplanation(SessionContext ctx)
    {
        return (String)getProperty(ctx, "explanation");
    }


    public String getExplanation()
    {
        return getExplanation(getSession().getSessionContext());
    }


    public void setExplanation(SessionContext ctx, String value)
    {
        setProperty(ctx, "explanation", value);
    }


    public void setExplanation(String value)
    {
        setExplanation(getSession().getSessionContext(), value);
    }


    public FraudReport getFraudReport(SessionContext ctx)
    {
        return (FraudReport)getProperty(ctx, "fraudReport");
    }


    public FraudReport getFraudReport()
    {
        return getFraudReport(getSession().getSessionContext());
    }


    protected void setFraudReport(SessionContext ctx, FraudReport value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'fraudReport' is not changeable", 0);
        }
        FRAUDREPORTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setFraudReport(FraudReport value)
    {
        setFraudReport(getSession().getSessionContext(), value);
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


    public Double getScore(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "score");
    }


    public Double getScore()
    {
        return getScore(getSession().getSessionContext());
    }


    public double getScoreAsPrimitive(SessionContext ctx)
    {
        Double value = getScore(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getScoreAsPrimitive()
    {
        return getScoreAsPrimitive(getSession().getSessionContext());
    }


    protected void setScore(SessionContext ctx, Double value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'score' is not changeable", 0);
        }
        setProperty(ctx, "score", value);
    }


    protected void setScore(Double value)
    {
        setScore(getSession().getSessionContext(), value);
    }


    protected void setScore(SessionContext ctx, double value)
    {
        setScore(ctx, Double.valueOf(value));
    }


    protected void setScore(double value)
    {
        setScore(getSession().getSessionContext(), value);
    }
}
