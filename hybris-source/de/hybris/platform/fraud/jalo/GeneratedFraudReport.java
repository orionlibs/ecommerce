package de.hybris.platform.fraud.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedFraudReport extends GenericItem
{
    public static final String CODE = "code";
    public static final String PROVIDER = "provider";
    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String EXPLANATION = "explanation";
    public static final String ORDER = "order";
    public static final String FRAUDSYMPTOMSCORINGS = "fraudSymptomScorings";
    protected static final BidirectionalOneToManyHandler<GeneratedFraudReport> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.FRAUDREPORT, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<FraudSymptomScoring> FRAUDSYMPTOMSCORINGSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.FRAUDSYMPTOMSCORING, true, "fraudReport", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("provider", Item.AttributeMode.INITIAL);
        tmp.put("timestamp", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("explanation", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
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
        ORDERHANDLER.newInstance(ctx, allAttributes);
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


    public List<FraudSymptomScoring> getFraudSymptomScorings(SessionContext ctx)
    {
        return (List<FraudSymptomScoring>)FRAUDSYMPTOMSCORINGSHANDLER.getValues(ctx, (Item)this);
    }


    public List<FraudSymptomScoring> getFraudSymptomScorings()
    {
        return getFraudSymptomScorings(getSession().getSessionContext());
    }


    public void setFraudSymptomScorings(SessionContext ctx, List<FraudSymptomScoring> value)
    {
        FRAUDSYMPTOMSCORINGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setFraudSymptomScorings(List<FraudSymptomScoring> value)
    {
        setFraudSymptomScorings(getSession().getSessionContext(), value);
    }


    public void addToFraudSymptomScorings(SessionContext ctx, FraudSymptomScoring value)
    {
        FRAUDSYMPTOMSCORINGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToFraudSymptomScorings(FraudSymptomScoring value)
    {
        addToFraudSymptomScorings(getSession().getSessionContext(), value);
    }


    public void removeFromFraudSymptomScorings(SessionContext ctx, FraudSymptomScoring value)
    {
        FRAUDSYMPTOMSCORINGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromFraudSymptomScorings(FraudSymptomScoring value)
    {
        removeFromFraudSymptomScorings(getSession().getSessionContext(), value);
    }


    public Order getOrder(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "order");
    }


    public Order getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, Order value)
    {
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrder(Order value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public String getProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "provider");
    }


    public String getProvider()
    {
        return getProvider(getSession().getSessionContext());
    }


    public void setProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "provider", value);
    }


    public void setProvider(String value)
    {
        setProvider(getSession().getSessionContext(), value);
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


    public Date getTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "timestamp");
    }


    public Date getTimestamp()
    {
        return getTimestamp(getSession().getSessionContext());
    }


    protected void setTimestamp(SessionContext ctx, Date value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'timestamp' is not changeable", 0);
        }
        setProperty(ctx, "timestamp", value);
    }


    protected void setTimestamp(Date value)
    {
        setTimestamp(getSession().getSessionContext(), value);
    }
}
