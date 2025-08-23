package de.hybris.platform.product.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.ConfigurationCategory;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractConfiguratorSetting extends GenericItem
{
    public static final String ID = "id";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CONFIGURATORTYPE = "configuratorType";
    public static final String QUALIFIER = "qualifier";
    public static final String CONFIGURATIONCATEGORYPOS = "configurationCategoryPOS";
    public static final String CONFIGURATIONCATEGORY = "configurationCategory";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractConfiguratorSetting> CONFIGURATIONCATEGORYHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.ABSTRACTCONFIGURATORSETTING, false, "configurationCategory", "configurationCategoryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("configuratorType", Item.AttributeMode.INITIAL);
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("configurationCategoryPOS", Item.AttributeMode.INITIAL);
        tmp.put("configurationCategory", Item.AttributeMode.INITIAL);
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


    protected void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'catalogVersion' is not changeable", 0);
        }
        setProperty(ctx, "catalogVersion", value);
    }


    protected void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    public ConfigurationCategory getConfigurationCategory(SessionContext ctx)
    {
        return (ConfigurationCategory)getProperty(ctx, "configurationCategory");
    }


    public ConfigurationCategory getConfigurationCategory()
    {
        return getConfigurationCategory(getSession().getSessionContext());
    }


    protected void setConfigurationCategory(SessionContext ctx, ConfigurationCategory value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'configurationCategory' is not changeable", 0);
        }
        CONFIGURATIONCATEGORYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setConfigurationCategory(ConfigurationCategory value)
    {
        setConfigurationCategory(getSession().getSessionContext(), value);
    }


    Integer getConfigurationCategoryPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "configurationCategoryPOS");
    }


    Integer getConfigurationCategoryPOS()
    {
        return getConfigurationCategoryPOS(getSession().getSessionContext());
    }


    int getConfigurationCategoryPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getConfigurationCategoryPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getConfigurationCategoryPOSAsPrimitive()
    {
        return getConfigurationCategoryPOSAsPrimitive(getSession().getSessionContext());
    }


    void setConfigurationCategoryPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "configurationCategoryPOS", value);
    }


    void setConfigurationCategoryPOS(Integer value)
    {
        setConfigurationCategoryPOS(getSession().getSessionContext(), value);
    }


    void setConfigurationCategoryPOS(SessionContext ctx, int value)
    {
        setConfigurationCategoryPOS(ctx, Integer.valueOf(value));
    }


    void setConfigurationCategoryPOS(int value)
    {
        setConfigurationCategoryPOS(getSession().getSessionContext(), value);
    }


    public EnumerationValue getConfiguratorType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "configuratorType");
    }


    public EnumerationValue getConfiguratorType()
    {
        return getConfiguratorType(getSession().getSessionContext());
    }


    protected void setConfiguratorType(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'configuratorType' is not changeable", 0);
        }
        setProperty(ctx, "configuratorType", value);
    }


    protected void setConfiguratorType(EnumerationValue value)
    {
        setConfiguratorType(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONFIGURATIONCATEGORYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    protected void setId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'id' is not changeable", 0);
        }
        setProperty(ctx, "id", value);
    }


    protected void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    protected void setQualifier(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'qualifier' is not changeable", 0);
        }
        setProperty(ctx, "qualifier", value);
    }


    protected void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }
}
