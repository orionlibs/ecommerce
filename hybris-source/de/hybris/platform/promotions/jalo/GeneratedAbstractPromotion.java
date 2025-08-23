package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractPromotion extends GenericItem
{
    public static final String PROMOTIONTYPE = "promotionType";
    public static final String CODE = "code";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String DETAILSURL = "detailsURL";
    public static final String RESTRICTIONS = "restrictions";
    public static final String ENABLED = "enabled";
    public static final String PRIORITY = "priority";
    public static final String IMMUTABLEKEYHASH = "immutableKeyHash";
    public static final String IMMUTABLEKEY = "immutableKey";
    public static final String NAME = "name";
    public static final String PROMOTIONGROUP = "PromotionGroup";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractPromotion> PROMOTIONGROUPHANDLER = new BidirectionalOneToManyHandler(GeneratedPromotionsConstants.TC.ABSTRACTPROMOTION, false, "PromotionGroup", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("startDate", Item.AttributeMode.INITIAL);
        tmp.put("endDate", Item.AttributeMode.INITIAL);
        tmp.put("detailsURL", Item.AttributeMode.INITIAL);
        tmp.put("enabled", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("immutableKeyHash", Item.AttributeMode.INITIAL);
        tmp.put("immutableKey", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("PromotionGroup", Item.AttributeMode.INITIAL);
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


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PROMOTIONGROUPHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public String getDetailsURL(SessionContext ctx)
    {
        return (String)getProperty(ctx, "detailsURL");
    }


    public String getDetailsURL()
    {
        return getDetailsURL(getSession().getSessionContext());
    }


    public void setDetailsURL(SessionContext ctx, String value)
    {
        setProperty(ctx, "detailsURL", value);
    }


    public void setDetailsURL(String value)
    {
        setDetailsURL(getSession().getSessionContext(), value);
    }


    public Boolean isEnabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enabled");
    }


    public Boolean isEnabled()
    {
        return isEnabled(getSession().getSessionContext());
    }


    public boolean isEnabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnabledAsPrimitive()
    {
        return isEnabledAsPrimitive(getSession().getSessionContext());
    }


    public void setEnabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enabled", value);
    }


    public void setEnabled(Boolean value)
    {
        setEnabled(getSession().getSessionContext(), value);
    }


    public void setEnabled(SessionContext ctx, boolean value)
    {
        setEnabled(ctx, Boolean.valueOf(value));
    }


    public void setEnabled(boolean value)
    {
        setEnabled(getSession().getSessionContext(), value);
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


    public String getImmutableKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "immutableKey");
    }


    public String getImmutableKey()
    {
        return getImmutableKey(getSession().getSessionContext());
    }


    public void setImmutableKey(SessionContext ctx, String value)
    {
        setProperty(ctx, "immutableKey", value);
    }


    public void setImmutableKey(String value)
    {
        setImmutableKey(getSession().getSessionContext(), value);
    }


    public String getImmutableKeyHash(SessionContext ctx)
    {
        return (String)getProperty(ctx, "immutableKeyHash");
    }


    public String getImmutableKeyHash()
    {
        return getImmutableKeyHash(getSession().getSessionContext());
    }


    public void setImmutableKeyHash(SessionContext ctx, String value)
    {
        setProperty(ctx, "immutableKeyHash", value);
    }


    public void setImmutableKeyHash(String value)
    {
        setImmutableKeyHash(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPromotion.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedAbstractPromotion.setName requires a session language", 0);
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


    public PromotionGroup getPromotionGroup(SessionContext ctx)
    {
        return (PromotionGroup)getProperty(ctx, "PromotionGroup");
    }


    public PromotionGroup getPromotionGroup()
    {
        return getPromotionGroup(getSession().getSessionContext());
    }


    public void setPromotionGroup(SessionContext ctx, PromotionGroup value)
    {
        PROMOTIONGROUPHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPromotionGroup(PromotionGroup value)
    {
        setPromotionGroup(getSession().getSessionContext(), value);
    }


    public String getPromotionType()
    {
        return getPromotionType(getSession().getSessionContext());
    }


    public Map<Language, String> getAllPromotionType()
    {
        return getAllPromotionType(getSession().getSessionContext());
    }


    public Collection<AbstractPromotionRestriction> getRestrictions()
    {
        return getRestrictions(getSession().getSessionContext());
    }


    public void setRestrictions(Collection<AbstractPromotionRestriction> value)
    {
        setRestrictions(getSession().getSessionContext(), value);
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


    public String getTitle(SessionContext ctx)
    {
        return (String)getProperty(ctx, "title");
    }


    public String getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, String value)
    {
        setProperty(ctx, "title", value);
    }


    public void setTitle(String value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public abstract String getPromotionType(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllPromotionType(SessionContext paramSessionContext);


    public abstract Collection<AbstractPromotionRestriction> getRestrictions(SessionContext paramSessionContext);


    public abstract void setRestrictions(SessionContext paramSessionContext, Collection<AbstractPromotionRestriction> paramCollection);
}
