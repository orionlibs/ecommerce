package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SnIndexTypeModel extends ItemModel
{
    public static final String _TYPECODE = "SnIndexType";
    public static final String _SNINDEXCONFIGURATION2INDEXTYPE = "SnIndexConfiguration2IndexType";
    public static final String _SNBASESTORE2INDEXTYPE = "SnBaseStore2IndexType";
    public static final String _SNBASESITE2INDEXTYPE = "SnBaseSite2IndexType";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ITEMCOMPOSEDTYPE = "itemComposedType";
    public static final String IDENTITYPROVIDER = "identityProvider";
    public static final String IDENTITYPROVIDERPARAMETERS = "identityProviderParameters";
    public static final String DEFAULTVALUEPROVIDER = "defaultValueProvider";
    public static final String DEFAULTVALUEPROVIDERPARAMETERS = "defaultValueProviderParameters";
    public static final String LISTENERS = "listeners";
    public static final String INDEXCONFIGURATIONPOS = "indexConfigurationPOS";
    public static final String INDEXCONFIGURATION = "indexConfiguration";
    public static final String FIELDS = "fields";
    public static final String CATALOGS = "catalogs";
    public static final String CATALOGVERSIONS = "catalogVersions";
    public static final String INDEXERCRONJOBS = "indexerCronJobs";
    public static final String STORES = "stores";
    public static final String SITES = "sites";


    public SnIndexTypeModel()
    {
    }


    public SnIndexTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexTypeModel(String _id, ComposedTypeModel _itemComposedType)
    {
        setId(_id);
        setItemComposedType(_itemComposedType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexTypeModel(String _id, ComposedTypeModel _itemComposedType, ItemModel _owner)
    {
        setId(_id);
        setItemComposedType(_itemComposedType);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogs", type = Accessor.Type.GETTER)
    public List<CatalogModel> getCatalogs()
    {
        return (List<CatalogModel>)getPersistenceContext().getPropertyValue("catalogs");
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
    public List<CatalogVersionModel> getCatalogVersions()
    {
        return (List<CatalogVersionModel>)getPersistenceContext().getPropertyValue("catalogVersions");
    }


    @Accessor(qualifier = "defaultValueProvider", type = Accessor.Type.GETTER)
    public String getDefaultValueProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("defaultValueProvider");
    }


    @Accessor(qualifier = "defaultValueProviderParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getDefaultValueProviderParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("defaultValueProviderParameters");
    }


    @Accessor(qualifier = "fields", type = Accessor.Type.GETTER)
    public List<SnFieldModel> getFields()
    {
        return (List<SnFieldModel>)getPersistenceContext().getPropertyValue("fields");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "identityProvider", type = Accessor.Type.GETTER)
    public String getIdentityProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("identityProvider");
    }


    @Accessor(qualifier = "identityProviderParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getIdentityProviderParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("identityProviderParameters");
    }


    @Accessor(qualifier = "indexConfiguration", type = Accessor.Type.GETTER)
    public SnIndexConfigurationModel getIndexConfiguration()
    {
        return (SnIndexConfigurationModel)getPersistenceContext().getPropertyValue("indexConfiguration");
    }


    @Accessor(qualifier = "indexerCronJobs", type = Accessor.Type.GETTER)
    public List<AbstractSnIndexerCronJobModel> getIndexerCronJobs()
    {
        return (List<AbstractSnIndexerCronJobModel>)getPersistenceContext().getPropertyValue("indexerCronJobs");
    }


    @Accessor(qualifier = "itemComposedType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getItemComposedType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("itemComposedType");
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
    public List<String> getListeners()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("listeners");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "sites", type = Accessor.Type.GETTER)
    public List<BaseSiteModel> getSites()
    {
        return (List<BaseSiteModel>)getPersistenceContext().getPropertyValue("sites");
    }


    @Accessor(qualifier = "stores", type = Accessor.Type.GETTER)
    public List<BaseStoreModel> getStores()
    {
        return (List<BaseStoreModel>)getPersistenceContext().getPropertyValue("stores");
    }


    @Accessor(qualifier = "catalogs", type = Accessor.Type.SETTER)
    public void setCatalogs(List<CatalogModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogs", value);
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
    public void setCatalogVersions(List<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogVersions", value);
    }


    @Accessor(qualifier = "defaultValueProvider", type = Accessor.Type.SETTER)
    public void setDefaultValueProvider(String value)
    {
        getPersistenceContext().setPropertyValue("defaultValueProvider", value);
    }


    @Accessor(qualifier = "defaultValueProviderParameters", type = Accessor.Type.SETTER)
    public void setDefaultValueProviderParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("defaultValueProviderParameters", value);
    }


    @Accessor(qualifier = "fields", type = Accessor.Type.SETTER)
    public void setFields(List<SnFieldModel> value)
    {
        getPersistenceContext().setPropertyValue("fields", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "identityProvider", type = Accessor.Type.SETTER)
    public void setIdentityProvider(String value)
    {
        getPersistenceContext().setPropertyValue("identityProvider", value);
    }


    @Accessor(qualifier = "identityProviderParameters", type = Accessor.Type.SETTER)
    public void setIdentityProviderParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("identityProviderParameters", value);
    }


    @Accessor(qualifier = "indexConfiguration", type = Accessor.Type.SETTER)
    public void setIndexConfiguration(SnIndexConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("indexConfiguration", value);
    }


    @Accessor(qualifier = "indexerCronJobs", type = Accessor.Type.SETTER)
    public void setIndexerCronJobs(List<AbstractSnIndexerCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("indexerCronJobs", value);
    }


    @Accessor(qualifier = "itemComposedType", type = Accessor.Type.SETTER)
    public void setItemComposedType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("itemComposedType", value);
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
    public void setListeners(List<String> value)
    {
        getPersistenceContext().setPropertyValue("listeners", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "sites", type = Accessor.Type.SETTER)
    public void setSites(List<BaseSiteModel> value)
    {
        getPersistenceContext().setPropertyValue("sites", value);
    }


    @Accessor(qualifier = "stores", type = Accessor.Type.SETTER)
    public void setStores(List<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("stores", value);
    }
}
