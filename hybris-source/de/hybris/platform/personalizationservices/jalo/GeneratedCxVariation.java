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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCxVariation extends GenericItem
{
    public static final String CODE = "code";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String NAME = "name";
    public static final String ENABLED = "enabled";
    public static final String STATUS = "status";
    public static final String CUSTOMIZATIONPOS = "customizationPOS";
    public static final String CUSTOMIZATION = "customization";
    public static final String ACTIONS = "actions";
    public static final String TRIGGERS = "triggers";
    protected static final BidirectionalOneToManyHandler<GeneratedCxVariation> CUSTOMIZATIONHANDLER = new BidirectionalOneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXVARIATION, false, "customization", "customizationPOS", true, true, 2);
    protected static final OneToManyHandler<CxAbstractAction> ACTIONSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXABSTRACTACTION, true, "variation", "variationPOS", true, true, 2);
    protected static final OneToManyHandler<CxAbstractTrigger> TRIGGERSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXABSTRACTTRIGGER, true, "variation", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("enabled", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("customizationPOS", Item.AttributeMode.INITIAL);
        tmp.put("customization", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CxAbstractAction> getActions(SessionContext ctx)
    {
        return (List<CxAbstractAction>)ACTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<CxAbstractAction> getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, List<CxAbstractAction> value)
    {
        ACTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setActions(List<CxAbstractAction> value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public void addToActions(SessionContext ctx, CxAbstractAction value)
    {
        ACTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToActions(CxAbstractAction value)
    {
        addToActions(getSession().getSessionContext(), value);
    }


    public void removeFromActions(SessionContext ctx, CxAbstractAction value)
    {
        ACTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromActions(CxAbstractAction value)
    {
        removeFromActions(getSession().getSessionContext(), value);
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
        CUSTOMIZATIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CxCustomization getCustomization(SessionContext ctx)
    {
        return (CxCustomization)getProperty(ctx, "customization");
    }


    public CxCustomization getCustomization()
    {
        return getCustomization(getSession().getSessionContext());
    }


    public void setCustomization(SessionContext ctx, CxCustomization value)
    {
        CUSTOMIZATIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCustomization(CxCustomization value)
    {
        setCustomization(getSession().getSessionContext(), value);
    }


    Integer getCustomizationPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "customizationPOS");
    }


    Integer getCustomizationPOS()
    {
        return getCustomizationPOS(getSession().getSessionContext());
    }


    int getCustomizationPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCustomizationPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCustomizationPOSAsPrimitive()
    {
        return getCustomizationPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCustomizationPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "customizationPOS", value);
    }


    void setCustomizationPOS(Integer value)
    {
        setCustomizationPOS(getSession().getSessionContext(), value);
    }


    void setCustomizationPOS(SessionContext ctx, int value)
    {
        setCustomizationPOS(ctx, Integer.valueOf(value));
    }


    void setCustomizationPOS(int value)
    {
        setCustomizationPOS(getSession().getSessionContext(), value);
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


    public Collection<CxAbstractTrigger> getTriggers(SessionContext ctx)
    {
        return TRIGGERSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CxAbstractTrigger> getTriggers()
    {
        return getTriggers(getSession().getSessionContext());
    }


    public void setTriggers(SessionContext ctx, Collection<CxAbstractTrigger> value)
    {
        TRIGGERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setTriggers(Collection<CxAbstractTrigger> value)
    {
        setTriggers(getSession().getSessionContext(), value);
    }


    public void addToTriggers(SessionContext ctx, CxAbstractTrigger value)
    {
        TRIGGERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToTriggers(CxAbstractTrigger value)
    {
        addToTriggers(getSession().getSessionContext(), value);
    }


    public void removeFromTriggers(SessionContext ctx, CxAbstractTrigger value)
    {
        TRIGGERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromTriggers(CxAbstractTrigger value)
    {
        removeFromTriggers(getSession().getSessionContext(), value);
    }
}
