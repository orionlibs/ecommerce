package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCatalog extends GenericItem
{
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
    public static final String DEFAULTCATALOG = "defaultCatalog";
    public static final String PREVIEWURLTEMPLATE = "previewURLTemplate";
    public static final String URLPATTERNS = "urlPatterns";
    public static final String CATALOGVERSIONS = "catalogVersions";
    public static final String SUPPLIER = "supplier";
    public static final String BUYER = "buyer";
    protected static final OneToManyHandler<CatalogVersion> CATALOGVERSIONSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.CATALOGVERSION, true, "catalog", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedCatalog> SUPPLIERHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.CATALOG, false, "supplier", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCatalog> BUYERHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.CATALOG, false, "buyer", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("activeCatalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("defaultCatalog", Item.AttributeMode.INITIAL);
        tmp.put("previewURLTemplate", Item.AttributeMode.INITIAL);
        tmp.put("urlPatterns", Item.AttributeMode.INITIAL);
        tmp.put("supplier", Item.AttributeMode.INITIAL);
        tmp.put("buyer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getActiveCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "activeCatalogVersion");
    }


    public CatalogVersion getActiveCatalogVersion()
    {
        return getActiveCatalogVersion(getSession().getSessionContext());
    }


    public void setActiveCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "activeCatalogVersion", value);
    }


    public void setActiveCatalogVersion(CatalogVersion value)
    {
        setActiveCatalogVersion(getSession().getSessionContext(), value);
    }


    public Company getBuyer(SessionContext ctx)
    {
        return (Company)getProperty(ctx, "buyer");
    }


    public Company getBuyer()
    {
        return getBuyer(getSession().getSessionContext());
    }


    public void setBuyer(SessionContext ctx, Company value)
    {
        BUYERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setBuyer(Company value)
    {
        setBuyer(getSession().getSessionContext(), value);
    }


    public Set<CatalogVersion> getCatalogVersions(SessionContext ctx)
    {
        return (Set<CatalogVersion>)CATALOGVERSIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<CatalogVersion> getCatalogVersions()
    {
        return getCatalogVersions(getSession().getSessionContext());
    }


    public void setCatalogVersions(SessionContext ctx, Set<CatalogVersion> value)
    {
        CATALOGVERSIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCatalogVersions(Set<CatalogVersion> value)
    {
        setCatalogVersions(getSession().getSessionContext(), value);
    }


    public void addToCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        CATALOGVERSIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCatalogVersions(CatalogVersion value)
    {
        addToCatalogVersions(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        CATALOGVERSIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCatalogVersions(CatalogVersion value)
    {
        removeFromCatalogVersions(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SUPPLIERHANDLER.newInstance(ctx, allAttributes);
        BUYERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDefaultCatalog(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "defaultCatalog");
    }


    public Boolean isDefaultCatalog()
    {
        return isDefaultCatalog(getSession().getSessionContext());
    }


    public boolean isDefaultCatalogAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDefaultCatalog(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDefaultCatalogAsPrimitive()
    {
        return isDefaultCatalogAsPrimitive(getSession().getSessionContext());
    }


    public void setDefaultCatalog(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "defaultCatalog", value);
    }


    public void setDefaultCatalog(Boolean value)
    {
        setDefaultCatalog(getSession().getSessionContext(), value);
    }


    public void setDefaultCatalog(SessionContext ctx, boolean value)
    {
        setDefaultCatalog(ctx, Boolean.valueOf(value));
    }


    public void setDefaultCatalog(boolean value)
    {
        setDefaultCatalog(getSession().getSessionContext(), value);
    }


    public String getId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "id");
    }


    public String getId()
    {
        return getId(getSession().getSessionContext());
    }


    public void setId(SessionContext ctx, String value)
    {
        setProperty(ctx, "id", value);
    }


    public void setId(String value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCatalog.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedCatalog.setName requires a session language", 0);
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


    public String getPreviewURLTemplate(SessionContext ctx)
    {
        return (String)getProperty(ctx, "previewURLTemplate");
    }


    public String getPreviewURLTemplate()
    {
        return getPreviewURLTemplate(getSession().getSessionContext());
    }


    public void setPreviewURLTemplate(SessionContext ctx, String value)
    {
        setProperty(ctx, "previewURLTemplate", value);
    }


    public void setPreviewURLTemplate(String value)
    {
        setPreviewURLTemplate(getSession().getSessionContext(), value);
    }


    public Company getSupplier(SessionContext ctx)
    {
        return (Company)getProperty(ctx, "supplier");
    }


    public Company getSupplier()
    {
        return getSupplier(getSession().getSessionContext());
    }


    public void setSupplier(SessionContext ctx, Company value)
    {
        SUPPLIERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSupplier(Company value)
    {
        setSupplier(getSession().getSessionContext(), value);
    }


    public Collection<String> getUrlPatterns(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "urlPatterns");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getUrlPatterns()
    {
        return getUrlPatterns(getSession().getSessionContext());
    }


    public void setUrlPatterns(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "urlPatterns", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setUrlPatterns(Collection<String> value)
    {
        setUrlPatterns(getSession().getSessionContext(), value);
    }
}
