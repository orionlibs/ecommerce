package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CatalogVersionModel extends ItemModel
{
    public static final String _TYPECODE = "CatalogVersion";
    public static final String _CATALOG2VERSIONSRELATION = "Catalog2VersionsRelation";
    public static final String _PRINCIPAL2WRITEABLECATALOGVERSIONRELATION = "Principal2WriteableCatalogVersionRelation";
    public static final String _PRINCIPAL2READABLECATALOGVERSIONRELATION = "Principal2ReadableCatalogVersionRelation";
    public static final String _PREVIEWDATATOCATALOGVERSION = "PreviewDataToCatalogVersion";
    public static final String _SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION = "SolrFacetSearchConfig2CatalogVersionRelation";
    public static final String ACTIVE = "active";
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
    public static final String CATEGORYSYSTEMID = "categorySystemID";
    public static final String CATEGORYSYSTEMNAME = "categorySystemName";
    public static final String CATEGORYSYSTEMDESCRIPTION = "categorySystemDescription";
    public static final String ROOTCATEGORIES = "rootCategories";
    public static final String PREVIOUSUPDATEVERSION = "previousUpdateVersion";
    public static final String CATALOG = "catalog";
    public static final String SYNCHRONIZATIONS = "synchronizations";
    public static final String INCOMINGSYNCHRONIZATIONS = "incomingSynchronizations";
    public static final String AGREEMENTS = "agreements";
    public static final String WRITEPRINCIPALS = "writePrincipals";
    public static final String READPRINCIPALS = "readPrincipals";
    public static final String PREVIEWS = "previews";
    public static final String WORKFLOWTEMPLATES = "workflowTemplates";
    public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";


    public CatalogVersionModel()
    {
    }


    public CatalogVersionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionModel(CatalogModel _catalog, String _version)
    {
        setCatalog(_catalog);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionModel(CatalogModel _catalog, ItemModel _owner, String _version)
    {
        setCatalog(_catalog);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "agreements", type = Accessor.Type.GETTER)
    public List<AgreementModel> getAgreements()
    {
        return (List<AgreementModel>)getPersistenceContext().getPropertyValue("agreements");
    }


    @Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
    public CatalogModel getCatalog()
    {
        return (CatalogModel)getPersistenceContext().getPropertyValue("catalog");
    }


    @Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.GETTER)
    public String getCategorySystemDescription()
    {
        return getCategorySystemDescription(null);
    }


    @Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.GETTER)
    public String getCategorySystemDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("categorySystemDescription", loc);
    }


    @Accessor(qualifier = "categorySystemID", type = Accessor.Type.GETTER)
    public String getCategorySystemID()
    {
        return (String)getPersistenceContext().getPropertyValue("categorySystemID");
    }


    @Accessor(qualifier = "categorySystemName", type = Accessor.Type.GETTER)
    public String getCategorySystemName()
    {
        return getCategorySystemName(null);
    }


    @Accessor(qualifier = "categorySystemName", type = Accessor.Type.GETTER)
    public String getCategorySystemName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("categorySystemName", loc);
    }


    @Accessor(qualifier = "defaultCurrency", type = Accessor.Type.GETTER)
    public CurrencyModel getDefaultCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("defaultCurrency");
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.GETTER)
    public List<SolrFacetSearchConfigModel> getFacetSearchConfigs()
    {
        return (List<SolrFacetSearchConfigModel>)getPersistenceContext().getPropertyValue("facetSearchConfigs");
    }


    @Accessor(qualifier = "generationDate", type = Accessor.Type.GETTER)
    public Date getGenerationDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("generationDate");
    }


    @Accessor(qualifier = "generatorInfo", type = Accessor.Type.GETTER)
    public String getGeneratorInfo()
    {
        return (String)getPersistenceContext().getPropertyValue("generatorInfo");
    }


    @Accessor(qualifier = "inclAssurance", type = Accessor.Type.GETTER)
    public Boolean getInclAssurance()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("inclAssurance");
    }


    @Accessor(qualifier = "inclDuty", type = Accessor.Type.GETTER)
    public Boolean getInclDuty()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("inclDuty");
    }


    @Accessor(qualifier = "inclFreight", type = Accessor.Type.GETTER)
    public Boolean getInclFreight()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("inclFreight");
    }


    @Accessor(qualifier = "inclPacking", type = Accessor.Type.GETTER)
    public Boolean getInclPacking()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("inclPacking");
    }


    @Accessor(qualifier = "incomingSynchronizations", type = Accessor.Type.GETTER)
    public List<SyncItemJobModel> getIncomingSynchronizations()
    {
        return (List<SyncItemJobModel>)getPersistenceContext().getPropertyValue("incomingSynchronizations");
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("languages");
    }


    @Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.GETTER)
    public String getMimeRootDirectory()
    {
        return (String)getPersistenceContext().getPropertyValue("mimeRootDirectory");
    }


    @Accessor(qualifier = "previews", type = Accessor.Type.GETTER)
    public Collection<PreviewDataModel> getPreviews()
    {
        return (Collection<PreviewDataModel>)getPersistenceContext().getPropertyValue("previews");
    }


    @Accessor(qualifier = "previousUpdateVersion", type = Accessor.Type.GETTER)
    public Integer getPreviousUpdateVersion()
    {
        return (Integer)getPersistenceContext().getPropertyValue("previousUpdateVersion");
    }


    @Accessor(qualifier = "readPrincipals", type = Accessor.Type.GETTER)
    public List<PrincipalModel> getReadPrincipals()
    {
        return (List<PrincipalModel>)getPersistenceContext().getPropertyValue("readPrincipals");
    }


    @Accessor(qualifier = "rootCategories", type = Accessor.Type.GETTER)
    public List<CategoryModel> getRootCategories()
    {
        return (List<CategoryModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "rootCategories");
    }


    @Accessor(qualifier = "synchronizations", type = Accessor.Type.GETTER)
    public List<SyncItemJobModel> getSynchronizations()
    {
        return (List<SyncItemJobModel>)getPersistenceContext().getPropertyValue("synchronizations");
    }


    @Accessor(qualifier = "territories", type = Accessor.Type.GETTER)
    public Collection<CountryModel> getTerritories()
    {
        return (Collection<CountryModel>)getPersistenceContext().getPropertyValue("territories");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public String getVersion()
    {
        return (String)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "workflowTemplates", type = Accessor.Type.GETTER)
    public Collection<WorkflowTemplateModel> getWorkflowTemplates()
    {
        return (Collection<WorkflowTemplateModel>)getPersistenceContext().getPropertyValue("workflowTemplates");
    }


    @Accessor(qualifier = "writePrincipals", type = Accessor.Type.GETTER)
    public List<PrincipalModel> getWritePrincipals()
    {
        return (List<PrincipalModel>)getPersistenceContext().getPropertyValue("writePrincipals");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "agreements", type = Accessor.Type.SETTER)
    public void setAgreements(List<AgreementModel> value)
    {
        getPersistenceContext().setPropertyValue("agreements", value);
    }


    @Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
    public void setCatalog(CatalogModel value)
    {
        getPersistenceContext().setPropertyValue("catalog", value);
    }


    @Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.SETTER)
    public void setCategorySystemDescription(String value)
    {
        setCategorySystemDescription(value, null);
    }


    @Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.SETTER)
    public void setCategorySystemDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("categorySystemDescription", loc, value);
    }


    @Accessor(qualifier = "categorySystemID", type = Accessor.Type.SETTER)
    public void setCategorySystemID(String value)
    {
        getPersistenceContext().setPropertyValue("categorySystemID", value);
    }


    @Accessor(qualifier = "categorySystemName", type = Accessor.Type.SETTER)
    public void setCategorySystemName(String value)
    {
        setCategorySystemName(value, null);
    }


    @Accessor(qualifier = "categorySystemName", type = Accessor.Type.SETTER)
    public void setCategorySystemName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("categorySystemName", loc, value);
    }


    @Accessor(qualifier = "defaultCurrency", type = Accessor.Type.SETTER)
    public void setDefaultCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("defaultCurrency", value);
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.SETTER)
    public void setFacetSearchConfigs(List<SolrFacetSearchConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfigs", value);
    }


    @Accessor(qualifier = "generationDate", type = Accessor.Type.SETTER)
    public void setGenerationDate(Date value)
    {
        getPersistenceContext().setPropertyValue("generationDate", value);
    }


    @Accessor(qualifier = "generatorInfo", type = Accessor.Type.SETTER)
    public void setGeneratorInfo(String value)
    {
        getPersistenceContext().setPropertyValue("generatorInfo", value);
    }


    @Accessor(qualifier = "inclAssurance", type = Accessor.Type.SETTER)
    public void setInclAssurance(Boolean value)
    {
        getPersistenceContext().setPropertyValue("inclAssurance", value);
    }


    @Accessor(qualifier = "inclDuty", type = Accessor.Type.SETTER)
    public void setInclDuty(Boolean value)
    {
        getPersistenceContext().setPropertyValue("inclDuty", value);
    }


    @Accessor(qualifier = "inclFreight", type = Accessor.Type.SETTER)
    public void setInclFreight(Boolean value)
    {
        getPersistenceContext().setPropertyValue("inclFreight", value);
    }


    @Accessor(qualifier = "inclPacking", type = Accessor.Type.SETTER)
    public void setInclPacking(Boolean value)
    {
        getPersistenceContext().setPropertyValue("inclPacking", value);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
    public void setLanguages(Collection<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("languages", value);
    }


    @Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.SETTER)
    public void setMimeRootDirectory(String value)
    {
        getPersistenceContext().setPropertyValue("mimeRootDirectory", value);
    }


    @Accessor(qualifier = "previews", type = Accessor.Type.SETTER)
    public void setPreviews(Collection<PreviewDataModel> value)
    {
        getPersistenceContext().setPropertyValue("previews", value);
    }


    @Accessor(qualifier = "previousUpdateVersion", type = Accessor.Type.SETTER)
    public void setPreviousUpdateVersion(Integer value)
    {
        getPersistenceContext().setPropertyValue("previousUpdateVersion", value);
    }


    @Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
    public void setReadPrincipals(List<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("readPrincipals", value);
    }


    @Accessor(qualifier = "rootCategories", type = Accessor.Type.SETTER)
    public void setRootCategories(List<CategoryModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "rootCategories", value);
    }


    @Accessor(qualifier = "synchronizations", type = Accessor.Type.SETTER)
    public void setSynchronizations(List<SyncItemJobModel> value)
    {
        getPersistenceContext().setPropertyValue("synchronizations", value);
    }


    @Accessor(qualifier = "territories", type = Accessor.Type.SETTER)
    public void setTerritories(Collection<CountryModel> value)
    {
        getPersistenceContext().setPropertyValue("territories", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(String value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }


    @Accessor(qualifier = "workflowTemplates", type = Accessor.Type.SETTER)
    public void setWorkflowTemplates(Collection<WorkflowTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("workflowTemplates", value);
    }


    @Accessor(qualifier = "writePrincipals", type = Accessor.Type.SETTER)
    public void setWritePrincipals(List<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("writePrincipals", value);
    }
}
