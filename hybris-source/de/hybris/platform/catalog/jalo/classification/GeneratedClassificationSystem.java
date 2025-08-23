package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedClassificationSystem extends Catalog
{
    protected static final OneToManyHandler<CatalogVersion> CATALOGVERSIONSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION, true, "catalog", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Catalog.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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


    public void addToCatalogVersions(SessionContext ctx, ClassificationSystemVersion value)
    {
        CATALOGVERSIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCatalogVersions(ClassificationSystemVersion value)
    {
        addToCatalogVersions(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogVersions(SessionContext ctx, ClassificationSystemVersion value)
    {
        CATALOGVERSIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCatalogVersions(ClassificationSystemVersion value)
    {
        removeFromCatalogVersions(getSession().getSessionContext(), value);
    }
}
