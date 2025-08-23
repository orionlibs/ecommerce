package de.hybris.platform.cms2.model.contents;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class ContentCatalogModel extends CatalogModel
{
    public static final String _TYPECODE = "ContentCatalog";
    public static final String _CATALOGSFORCMSSITE = "CatalogsForCMSSite";
    public static final String _CATALOG2CATALOGRELATION = "Catalog2CatalogRelation";
    public static final String CATALOGLEVELNAME = "catalogLevelName";
    public static final String CMSSITES = "cmsSites";
    public static final String SUPERCATALOG = "superCatalog";
    public static final String SUBCATALOGS = "subCatalogs";


    public ContentCatalogModel()
    {
    }


    public ContentCatalogModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentCatalogModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ContentCatalogModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogLevelName", type = Accessor.Type.GETTER)
    public String getCatalogLevelName()
    {
        return getCatalogLevelName(null);
    }


    @Accessor(qualifier = "catalogLevelName", type = Accessor.Type.GETTER)
    public String getCatalogLevelName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("catalogLevelName", loc);
    }


    @Accessor(qualifier = "cmsSites", type = Accessor.Type.GETTER)
    public Collection<CMSSiteModel> getCmsSites()
    {
        return (Collection<CMSSiteModel>)getPersistenceContext().getPropertyValue("cmsSites");
    }


    @Accessor(qualifier = "subCatalogs", type = Accessor.Type.GETTER)
    public Set<ContentCatalogModel> getSubCatalogs()
    {
        return (Set<ContentCatalogModel>)getPersistenceContext().getPropertyValue("subCatalogs");
    }


    @Accessor(qualifier = "superCatalog", type = Accessor.Type.GETTER)
    public ContentCatalogModel getSuperCatalog()
    {
        return (ContentCatalogModel)getPersistenceContext().getPropertyValue("superCatalog");
    }


    @Accessor(qualifier = "catalogLevelName", type = Accessor.Type.SETTER)
    public void setCatalogLevelName(String value)
    {
        setCatalogLevelName(value, null);
    }


    @Accessor(qualifier = "catalogLevelName", type = Accessor.Type.SETTER)
    public void setCatalogLevelName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("catalogLevelName", loc, value);
    }


    @Accessor(qualifier = "cmsSites", type = Accessor.Type.SETTER)
    public void setCmsSites(Collection<CMSSiteModel> value)
    {
        getPersistenceContext().setPropertyValue("cmsSites", value);
    }


    @Accessor(qualifier = "subCatalogs", type = Accessor.Type.SETTER)
    public void setSubCatalogs(Set<ContentCatalogModel> value)
    {
        getPersistenceContext().setPropertyValue("subCatalogs", value);
    }


    @Accessor(qualifier = "superCatalog", type = Accessor.Type.SETTER)
    public void setSuperCatalog(ContentCatalogModel value)
    {
        getPersistenceContext().setPropertyValue("superCatalog", value);
    }
}
