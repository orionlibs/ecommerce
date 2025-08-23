package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrStopWordModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSynonymConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LanguageModel extends C2LItemModel
{
    public static final String _TYPECODE = "Language";
    public static final String _SYNCJOB2LANGREL = "SyncJob2LangRel";
    public static final String _SOLRFACETSEARCHCONFIG2LANGUAGERELATION = "SolrFacetSearchConfig2LanguageRelation";
    public static final String _SOLRSYNONYMCONFIG2LANGUAGE = "SolrSynonymConfig2Language";
    public static final String _SOLRSTOPWORD2LANGUAGE = "SolrStopWord2Language";
    public static final String _BASESTORE2LANGUAGEREL = "BaseStore2LanguageRel";
    public static final String FALLBACKLANGUAGES = "fallbackLanguages";
    public static final String SYNCJOBS = "syncJobs";
    public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
    public static final String SYNONYMS = "synonyms";
    public static final String STOPWORDS = "StopWords";
    public static final String BASESTORES = "baseStores";
    public static final String SAPCODE = "sapCode";


    public LanguageModel()
    {
    }


    public LanguageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LanguageModel(String _isocode)
    {
        setIsocode(_isocode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public LanguageModel(String _isocode, ItemModel _owner)
    {
        setIsocode(_isocode);
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Collection<BaseStoreModel> getBaseStores()
    {
        return (Collection<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.GETTER)
    public List<SolrFacetSearchConfigModel> getFacetSearchConfigs()
    {
        return (List<SolrFacetSearchConfigModel>)getPersistenceContext().getPropertyValue("facetSearchConfigs");
    }


    @Accessor(qualifier = "fallbackLanguages", type = Accessor.Type.GETTER)
    public List<LanguageModel> getFallbackLanguages()
    {
        List<LanguageModel> value = (List<LanguageModel>)getPersistenceContext().getPropertyValue("fallbackLanguages");
        return (value != null) ? value : Collections.<LanguageModel>emptyList();
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.GETTER)
    public String getSapCode()
    {
        return (String)getPersistenceContext().getPropertyValue("sapCode");
    }


    @Accessor(qualifier = "StopWords", type = Accessor.Type.GETTER)
    public List<SolrStopWordModel> getStopWords()
    {
        return (List<SolrStopWordModel>)getPersistenceContext().getPropertyValue("StopWords");
    }


    @Accessor(qualifier = "synonyms", type = Accessor.Type.GETTER)
    public List<SolrSynonymConfigModel> getSynonyms()
    {
        return (List<SolrSynonymConfigModel>)getPersistenceContext().getPropertyValue("synonyms");
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Collection<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.SETTER)
    public void setFacetSearchConfigs(List<SolrFacetSearchConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfigs", value);
    }


    @Accessor(qualifier = "fallbackLanguages", type = Accessor.Type.SETTER)
    public void setFallbackLanguages(List<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("fallbackLanguages", value);
    }


    @Accessor(qualifier = "sapCode", type = Accessor.Type.SETTER)
    public void setSapCode(String value)
    {
        getPersistenceContext().setPropertyValue("sapCode", value);
    }


    @Accessor(qualifier = "StopWords", type = Accessor.Type.SETTER)
    public void setStopWords(List<SolrStopWordModel> value)
    {
        getPersistenceContext().setPropertyValue("StopWords", value);
    }


    @Accessor(qualifier = "synonyms", type = Accessor.Type.SETTER)
    public void setSynonyms(List<SolrSynonymConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("synonyms", value);
    }
}
