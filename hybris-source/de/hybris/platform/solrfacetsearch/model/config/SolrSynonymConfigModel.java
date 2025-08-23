package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrSynonymConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSynonymConfig";
    public static final String SYNONYMFROM = "synonymFrom";
    public static final String SYNONYMTO = "synonymTo";
    public static final String LANGUAGEPOS = "languagePOS";
    public static final String LANGUAGE = "language";
    public static final String FACETSEARCHCONFIGPOS = "facetSearchConfigPOS";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";


    public SolrSynonymConfigModel()
    {
    }


    public SolrSynonymConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSynonymConfigModel(LanguageModel _language, String _synonymFrom)
    {
        setLanguage(_language);
        setSynonymFrom(_synonymFrom);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSynonymConfigModel(LanguageModel _language, ItemModel _owner, String _synonymFrom)
    {
        setLanguage(_language);
        setOwner(_owner);
        setSynonymFrom(_synonymFrom);
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getFacetSearchConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("facetSearchConfig");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "synonymFrom", type = Accessor.Type.GETTER)
    public String getSynonymFrom()
    {
        return (String)getPersistenceContext().getPropertyValue("synonymFrom");
    }


    @Accessor(qualifier = "synonymTo", type = Accessor.Type.GETTER)
    public String getSynonymTo()
    {
        return (String)getPersistenceContext().getPropertyValue("synonymTo");
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
    public void setFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfig", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "synonymFrom", type = Accessor.Type.SETTER)
    public void setSynonymFrom(String value)
    {
        getPersistenceContext().setPropertyValue("synonymFrom", value);
    }


    @Accessor(qualifier = "synonymTo", type = Accessor.Type.SETTER)
    public void setSynonymTo(String value)
    {
        getPersistenceContext().setPropertyValue("synonymTo", value);
    }
}
