package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCxCustomizationsGroup extends GenericItem
{
    public static final String CODE = "code";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CUSTOMIZATIONS = "customizations";
    protected static final OneToManyHandler<CxCustomization> CUSTOMIZATIONSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXCUSTOMIZATION, true, "group", "groupPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
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


    public List<CxCustomization> getCustomizations(SessionContext ctx)
    {
        return (List<CxCustomization>)CUSTOMIZATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<CxCustomization> getCustomizations()
    {
        return getCustomizations(getSession().getSessionContext());
    }


    public void setCustomizations(SessionContext ctx, List<CxCustomization> value)
    {
        CUSTOMIZATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCustomizations(List<CxCustomization> value)
    {
        setCustomizations(getSession().getSessionContext(), value);
    }


    public void addToCustomizations(SessionContext ctx, CxCustomization value)
    {
        CUSTOMIZATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCustomizations(CxCustomization value)
    {
        addToCustomizations(getSession().getSessionContext(), value);
    }


    public void removeFromCustomizations(SessionContext ctx, CxCustomization value)
    {
        CUSTOMIZATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCustomizations(CxCustomization value)
    {
        removeFromCustomizations(getSession().getSessionContext(), value);
    }
}
