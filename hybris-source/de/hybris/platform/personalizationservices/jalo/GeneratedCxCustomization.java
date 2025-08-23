package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCxCustomization extends GenericItem
{
    public static final String CODE = "code";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String LONGDESCRIPTION = "longDescription";
    public static final String STATUS = "status";
    public static final String ENABLEDSTARTDATE = "enabledStartDate";
    public static final String ENABLEDENDDATE = "enabledEndDate";
    public static final String VARIATIONS = "variations";
    public static final String GROUPPOS = "groupPOS";
    public static final String GROUP = "group";
    protected static final OneToManyHandler<CxVariation> VARIATIONSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXVARIATION, true, "customization", "customizationPOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedCxCustomization> GROUPHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXCUSTOMIZATION, false, "group", "groupPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("longDescription", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("enabledStartDate", Item.AttributeMode.INITIAL);
        tmp.put("enabledEndDate", Item.AttributeMode.INITIAL);
        tmp.put("groupPOS", Item.AttributeMode.INITIAL);
        tmp.put("group", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
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
        GROUPHANDLER.newInstance(ctx, allAttributes);
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


    public Date getEnabledEndDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "enabledEndDate");
    }


    public Date getEnabledEndDate()
    {
        return getEnabledEndDate(getSession().getSessionContext());
    }


    public void setEnabledEndDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "enabledEndDate", value);
    }


    public void setEnabledEndDate(Date value)
    {
        setEnabledEndDate(getSession().getSessionContext(), value);
    }


    public Date getEnabledStartDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "enabledStartDate");
    }


    public Date getEnabledStartDate()
    {
        return getEnabledStartDate(getSession().getSessionContext());
    }


    public void setEnabledStartDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "enabledStartDate", value);
    }


    public void setEnabledStartDate(Date value)
    {
        setEnabledStartDate(getSession().getSessionContext(), value);
    }


    public CxCustomizationsGroup getGroup(SessionContext ctx)
    {
        return (CxCustomizationsGroup)getProperty(ctx, "group");
    }


    public CxCustomizationsGroup getGroup()
    {
        return getGroup(getSession().getSessionContext());
    }


    public void setGroup(SessionContext ctx, CxCustomizationsGroup value)
    {
        GROUPHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setGroup(CxCustomizationsGroup value)
    {
        setGroup(getSession().getSessionContext(), value);
    }


    Integer getGroupPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "groupPOS");
    }


    Integer getGroupPOS()
    {
        return getGroupPOS(getSession().getSessionContext());
    }


    int getGroupPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getGroupPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getGroupPOSAsPrimitive()
    {
        return getGroupPOSAsPrimitive(getSession().getSessionContext());
    }


    void setGroupPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "groupPOS", value);
    }


    void setGroupPOS(Integer value)
    {
        setGroupPOS(getSession().getSessionContext(), value);
    }


    void setGroupPOS(SessionContext ctx, int value)
    {
        setGroupPOS(ctx, Integer.valueOf(value));
    }


    void setGroupPOS(int value)
    {
        setGroupPOS(getSession().getSessionContext(), value);
    }


    public String getLongDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "longDescription");
    }


    public String getLongDescription()
    {
        return getLongDescription(getSession().getSessionContext());
    }


    public void setLongDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "longDescription", value);
    }


    public void setLongDescription(String value)
    {
        setLongDescription(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
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


    public List<CxVariation> getVariations(SessionContext ctx)
    {
        return (List<CxVariation>)VARIATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<CxVariation> getVariations()
    {
        return getVariations(getSession().getSessionContext());
    }


    public void setVariations(SessionContext ctx, List<CxVariation> value)
    {
        VARIATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setVariations(List<CxVariation> value)
    {
        setVariations(getSession().getSessionContext(), value);
    }


    public void addToVariations(SessionContext ctx, CxVariation value)
    {
        VARIATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToVariations(CxVariation value)
    {
        addToVariations(getSession().getSessionContext(), value);
    }


    public void removeFromVariations(SessionContext ctx, CxVariation value)
    {
        VARIATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromVariations(CxVariation value)
    {
        removeFromVariations(getSession().getSessionContext(), value);
    }
}
