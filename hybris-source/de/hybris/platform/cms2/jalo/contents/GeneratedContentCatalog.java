package de.hybris.platform.cms2.jalo.contents;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedContentCatalog extends Catalog
{
    public static final String CATALOGLEVELNAME = "catalogLevelName";
    public static final String CMSSITES = "cmsSites";
    protected static String CATALOGSFORCMSSITE_SRC_ORDERED = "relation.CatalogsForCMSSite.source.ordered";
    protected static String CATALOGSFORCMSSITE_TGT_ORDERED = "relation.CatalogsForCMSSite.target.ordered";
    protected static String CATALOGSFORCMSSITE_MARKMODIFIED = "relation.CatalogsForCMSSite.markmodified";
    public static final String SUPERCATALOG = "superCatalog";
    public static final String SUBCATALOGS = "subCatalogs";
    protected static final BidirectionalOneToManyHandler<GeneratedContentCatalog> SUPERCATALOGHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CONTENTCATALOG, false, "superCatalog", null, false, true, 1);
    protected static final OneToManyHandler<ContentCatalog> SUBCATALOGSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CONTENTCATALOG, false, "superCatalog", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Catalog.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("catalogLevelName", Item.AttributeMode.INITIAL);
        tmp.put("superCatalog", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCatalogLevelName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedContentCatalog.getCatalogLevelName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "catalogLevelName");
    }


    public String getCatalogLevelName()
    {
        return getCatalogLevelName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllCatalogLevelName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "catalogLevelName", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllCatalogLevelName()
    {
        return getAllCatalogLevelName(getSession().getSessionContext());
    }


    public void setCatalogLevelName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedContentCatalog.setCatalogLevelName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "catalogLevelName", value);
    }


    public void setCatalogLevelName(String value)
    {
        setCatalogLevelName(getSession().getSessionContext(), value);
    }


    public void setAllCatalogLevelName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "catalogLevelName", value);
    }


    public void setAllCatalogLevelName(Map<Language, String> value)
    {
        setAllCatalogLevelName(getSession().getSessionContext(), value);
    }


    public Collection<CMSSite> getCmsSites(SessionContext ctx)
    {
        List<CMSSite> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, "CMSSite", null,
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<CMSSite> getCmsSites()
    {
        return getCmsSites(getSession().getSessionContext());
    }


    public long getCmsSitesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, "CMSSite", null);
    }


    public long getCmsSitesCount()
    {
        return getCmsSitesCount(getSession().getSessionContext());
    }


    public void setCmsSites(SessionContext ctx, Collection<CMSSite> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, null, value,
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED));
    }


    public void setCmsSites(Collection<CMSSite> value)
    {
        setCmsSites(getSession().getSessionContext(), value);
    }


    public void addToCmsSites(SessionContext ctx, CMSSite value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED));
    }


    public void addToCmsSites(CMSSite value)
    {
        addToCmsSites(getSession().getSessionContext(), value);
    }


    public void removeFromCmsSites(SessionContext ctx, CMSSite value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED));
    }


    public void removeFromCmsSites(CMSSite value)
    {
        removeFromCmsSites(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SUPERCATALOGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CMSSite");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED);
        }
        return true;
    }


    public Set<ContentCatalog> getSubCatalogs(SessionContext ctx)
    {
        return (Set<ContentCatalog>)SUBCATALOGSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<ContentCatalog> getSubCatalogs()
    {
        return getSubCatalogs(getSession().getSessionContext());
    }


    public void setSubCatalogs(SessionContext ctx, Set<ContentCatalog> value)
    {
        SUBCATALOGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSubCatalogs(Set<ContentCatalog> value)
    {
        setSubCatalogs(getSession().getSessionContext(), value);
    }


    public void addToSubCatalogs(SessionContext ctx, ContentCatalog value)
    {
        SUBCATALOGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSubCatalogs(ContentCatalog value)
    {
        addToSubCatalogs(getSession().getSessionContext(), value);
    }


    public void removeFromSubCatalogs(SessionContext ctx, ContentCatalog value)
    {
        SUBCATALOGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSubCatalogs(ContentCatalog value)
    {
        removeFromSubCatalogs(getSession().getSessionContext(), value);
    }


    public ContentCatalog getSuperCatalog(SessionContext ctx)
    {
        return (ContentCatalog)getProperty(ctx, "superCatalog");
    }


    public ContentCatalog getSuperCatalog()
    {
        return getSuperCatalog(getSession().getSessionContext());
    }


    public void setSuperCatalog(SessionContext ctx, ContentCatalog value)
    {
        SUPERCATALOGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSuperCatalog(ContentCatalog value)
    {
        setSuperCatalog(getSession().getSessionContext(), value);
    }
}
