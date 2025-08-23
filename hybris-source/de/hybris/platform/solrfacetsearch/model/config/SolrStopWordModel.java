package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrStopWordModel extends ItemModel
{
    public static final String _TYPECODE = "SolrStopWord";
    public static final String STOPWORD = "StopWord";
    public static final String LANGUAGEPOS = "languagePOS";
    public static final String LANGUAGE = "language";
    public static final String FACETSEARCHCONFIGPOS = "facetSearchConfigPOS";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";


    public SolrStopWordModel()
    {
    }


    public SolrStopWordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrStopWordModel(String _StopWord, LanguageModel _language)
    {
        setStopWord(_StopWord);
        setLanguage(_language);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrStopWordModel(String _StopWord, LanguageModel _language, ItemModel _owner)
    {
        setStopWord(_StopWord);
        setLanguage(_language);
        setOwner(_owner);
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


    @Accessor(qualifier = "StopWord", type = Accessor.Type.GETTER)
    public String getStopWord()
    {
        return (String)getPersistenceContext().getPropertyValue("StopWord");
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


    @Accessor(qualifier = "StopWord", type = Accessor.Type.SETTER)
    public void setStopWord(String value)
    {
        getPersistenceContext().setPropertyValue("StopWord", value);
    }
}
