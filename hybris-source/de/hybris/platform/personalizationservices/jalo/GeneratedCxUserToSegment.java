package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxUserToSegment extends GenericItem
{
    public static final String PROVIDER = "provider";
    public static final String AFFINITY = "affinity";
    public static final String BASESITE = "baseSite";
    public static final String SEGMENT = "segment";
    public static final String USER = "user";
    protected static final BidirectionalOneToManyHandler<GeneratedCxUserToSegment> SEGMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXUSERTOSEGMENT, false, "segment", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCxUserToSegment> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXUSERTOSEGMENT, false, "user", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("provider", Item.AttributeMode.INITIAL);
        tmp.put("affinity", Item.AttributeMode.INITIAL);
        tmp.put("baseSite", Item.AttributeMode.INITIAL);
        tmp.put("segment", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BigDecimal getAffinity(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "affinity");
    }


    public BigDecimal getAffinity()
    {
        return getAffinity(getSession().getSessionContext());
    }


    public void setAffinity(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "affinity", value);
    }


    public void setAffinity(BigDecimal value)
    {
        setAffinity(getSession().getSessionContext(), value);
    }


    public BaseSite getBaseSite(SessionContext ctx)
    {
        return (BaseSite)getProperty(ctx, "baseSite");
    }


    public BaseSite getBaseSite()
    {
        return getBaseSite(getSession().getSessionContext());
    }


    public void setBaseSite(SessionContext ctx, BaseSite value)
    {
        setProperty(ctx, "baseSite", value);
    }


    public void setBaseSite(BaseSite value)
    {
        setBaseSite(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SEGMENTHANDLER.newInstance(ctx, allAttributes);
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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


    public CxSegment getSegment(SessionContext ctx)
    {
        return (CxSegment)getProperty(ctx, "segment");
    }


    public CxSegment getSegment()
    {
        return getSegment(getSession().getSessionContext());
    }


    public void setSegment(SessionContext ctx, CxSegment value)
    {
        SEGMENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSegment(CxSegment value)
    {
        setSegment(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
