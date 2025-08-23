package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.restrictions.CMSCatalogRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CatalogModel extends ItemModel
{
    public static final String _TYPECODE = "Catalog";
    public static final String _COMPANY2PROVIDEDCATALOGS = "Company2ProvidedCatalogs";
    public static final String _COMPANY2PURCHASEDCATALOGS = "Company2PurchasedCatalogs";
    public static final String _CATALOGSFORBASESTORES = "CatalogsForBaseStores";
    public static final String _CATALOGSFORRESTRICTION = "CatalogsForRestriction";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
    public static final String ROOTCATEGORIES = "rootCategories";
    public static final String DEFAULTCATALOG = "defaultCatalog";
    public static final String VERSION = "version";
    public static final String MIMEROOTDIRECTORY = "mimeRootDirectory";
    public static final String GENERATIONDATE = "generationDate";
    public static final String DEFAULTCURRENCY = "defaultCurrency";
    public static final String INCLFREIGHT = "inclFreight";
    public static final String INCLPACKING = "inclPacking";
    public static final String INCLASSURANCE = "inclAssurance";
    public static final String INCLDUTY = "inclDuty";
    public static final String TERRITORIES = "territories";
    public static final String LANGUAGES = "languages";
    public static final String GENERATORINFO = "generatorInfo";
    public static final String AGREEMENTS = "agreements";
    public static final String PREVIEWURLTEMPLATE = "previewURLTemplate";
    public static final String URLPATTERNS = "urlPatterns";
    public static final String CATALOGVERSIONS = "catalogVersions";
    public static final String SUPPLIER = "supplier";
    public static final String BUYER = "buyer";
    public static final String BASESTORES = "baseStores";
    public static final String RESTRICTIONS = "restrictions";


    public CatalogModel()
    {
    }


    public CatalogModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getActiveCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("activeCatalogVersion");
    }


    @Accessor(qualifier = "agreements", type = Accessor.Type.GETTER)
    public Collection<AgreementModel> getAgreements()
    {
        return (Collection<AgreementModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "agreements");
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Collection<BaseStoreModel> getBaseStores()
    {
        return (Collection<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
    public CompanyModel getBuyer()
    {
        return (CompanyModel)getPersistenceContext().getPropertyValue("buyer");
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
    public Set<CatalogVersionModel> getCatalogVersions()
    {
        return (Set<CatalogVersionModel>)getPersistenceContext().getPropertyValue("catalogVersions");
    }


    @Accessor(qualifier = "defaultCatalog", type = Accessor.Type.GETTER)
    public Boolean getDefaultCatalog()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("defaultCatalog");
    }


    @Accessor(qualifier = "defaultCurrency", type = Accessor.Type.GETTER)
    public CurrencyModel getDefaultCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "defaultCurrency");
    }


    @Accessor(qualifier = "generationDate", type = Accessor.Type.GETTER)
    public Date getGenerationDate()
    {
        return (Date)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "generationDate");
    }


    @Accessor(qualifier = "generatorInfo", type = Accessor.Type.GETTER)
    public String getGeneratorInfo()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "generatorInfo");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "inclAssurance", type = Accessor.Type.GETTER)
    public Boolean getInclAssurance()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "inclAssurance");
    }


    @Accessor(qualifier = "inclDuty", type = Accessor.Type.GETTER)
    public Boolean getInclDuty()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "inclDuty");
    }


    @Accessor(qualifier = "inclFreight", type = Accessor.Type.GETTER)
    public Boolean getInclFreight()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "inclFreight");
    }


    @Accessor(qualifier = "inclPacking", type = Accessor.Type.GETTER)
    public Boolean getInclPacking()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "inclPacking");
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "languages");
    }


    @Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.GETTER)
    public String getMimeRootDirectory()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "mimeRootDirectory");
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


    @Accessor(qualifier = "previewURLTemplate", type = Accessor.Type.GETTER)
    public String getPreviewURLTemplate()
    {
        return (String)getPersistenceContext().getPropertyValue("previewURLTemplate");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<CMSCatalogRestrictionModel> getRestrictions()
    {
        return (Collection<CMSCatalogRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "rootCategories", type = Accessor.Type.GETTER)
    public List<CategoryModel> getRootCategories()
    {
        return (List<CategoryModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "rootCategories");
    }


    @Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
    public CompanyModel getSupplier()
    {
        return (CompanyModel)getPersistenceContext().getPropertyValue("supplier");
    }


    @Accessor(qualifier = "territories", type = Accessor.Type.GETTER)
    public Collection<CountryModel> getTerritories()
    {
        return (Collection<CountryModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "territories");
    }


    @Accessor(qualifier = "urlPatterns", type = Accessor.Type.GETTER)
    public Collection<String> getUrlPatterns()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("urlPatterns");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public String getVersion()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "version");
    }


    @Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.SETTER)
    public void setActiveCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("activeCatalogVersion", value);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Collection<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
    public void setBuyer(CompanyModel value)
    {
        getPersistenceContext().setPropertyValue("buyer", value);
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
    public void setCatalogVersions(Set<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogVersions", value);
    }


    @Accessor(qualifier = "defaultCatalog", type = Accessor.Type.SETTER)
    public void setDefaultCatalog(Boolean value)
    {
        getPersistenceContext().setPropertyValue("defaultCatalog", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
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


    @Accessor(qualifier = "previewURLTemplate", type = Accessor.Type.SETTER)
    public void setPreviewURLTemplate(String value)
    {
        getPersistenceContext().setPropertyValue("previewURLTemplate", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<CMSCatalogRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
    public void setSupplier(CompanyModel value)
    {
        getPersistenceContext().setPropertyValue("supplier", value);
    }


    @Accessor(qualifier = "urlPatterns", type = Accessor.Type.SETTER)
    public void setUrlPatterns(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("urlPatterns", value);
    }
}
