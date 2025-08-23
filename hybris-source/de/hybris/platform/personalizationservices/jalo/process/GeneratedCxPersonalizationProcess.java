package de.hybris.platform.personalizationservices.jalo.process;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCxPersonalizationProcess extends BusinessProcess
{
    public static final String KEY = "key";
    public static final String CATALOGVERSIONS = "catalogVersions";
    protected static String CXPERSPROCTOCATVER_SRC_ORDERED = "relation.CxPersProcToCatVer.source.ordered";
    protected static String CXPERSPROCTOCATVER_TGT_ORDERED = "relation.CxPersProcToCatVer.target.ordered";
    protected static String CXPERSPROCTOCATVER_MARKMODIFIED = "relation.CxPersProcToCatVer.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("key", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<CatalogVersion> getCatalogVersions(SessionContext ctx)
    {
        List<CatalogVersion> items = getLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, "CatalogVersion", null, false, false);
        return items;
    }


    public Collection<CatalogVersion> getCatalogVersions()
    {
        return getCatalogVersions(getSession().getSessionContext());
    }


    public long getCatalogVersionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, "CatalogVersion", null);
    }


    public long getCatalogVersionsCount()
    {
        return getCatalogVersionsCount(getSession().getSessionContext());
    }


    public void setCatalogVersions(SessionContext ctx, Collection<CatalogVersion> value)
    {
        setLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED));
    }


    public void setCatalogVersions(Collection<CatalogVersion> value)
    {
        setCatalogVersions(getSession().getSessionContext(), value);
    }


    public void addToCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        addLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED));
    }


    public void addToCatalogVersions(CatalogVersion value)
    {
        addToCatalogVersions(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        removeLinkedItems(ctx, false, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED));
    }


    public void removeFromCatalogVersions(CatalogVersion value)
    {
        removeFromCatalogVersions(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CatalogVersion");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED);
        }
        return true;
    }


    public String getKey(SessionContext ctx)
    {
        return (String)getProperty(ctx, "key");
    }


    public String getKey()
    {
        return getKey(getSession().getSessionContext());
    }


    public void setKey(SessionContext ctx, String value)
    {
        setProperty(ctx, "key", value);
    }


    public void setKey(String value)
    {
        setKey(getSession().getSessionContext(), value);
    }
}
