package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class SolrFacetSearchKeywordRedirectModel extends ItemModel
{
    public static final String _TYPECODE = "SolrFacetSearchKeywordRedirect";
    public static final String _SOLRFACETSEARCHCONFIG2SOLRFACETSEARCHKEYWORDREDIRECT = "SolrFacetSearchConfig2SolrFacetSearchKeywordRedirect";
    public static final String LANGUAGE = "language";
    public static final String KEYWORD = "keyword";
    public static final String MATCHTYPE = "matchType";
    public static final String IGNORECASE = "ignoreCase";
    public static final String REDIRECT = "redirect";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";


    public SolrFacetSearchKeywordRedirectModel()
    {
    }


    public SolrFacetSearchKeywordRedirectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrFacetSearchKeywordRedirectModel(String _keyword, LanguageModel _language, SolrAbstractKeywordRedirectModel _redirect)
    {
        setKeyword(_keyword);
        setLanguage(_language);
        setRedirect(_redirect);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrFacetSearchKeywordRedirectModel(String _keyword, LanguageModel _language, ItemModel _owner, SolrAbstractKeywordRedirectModel _redirect)
    {
        setKeyword(_keyword);
        setLanguage(_language);
        setOwner(_owner);
        setRedirect(_redirect);
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getFacetSearchConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("facetSearchConfig");
    }


    @Accessor(qualifier = "ignoreCase", type = Accessor.Type.GETTER)
    public Boolean getIgnoreCase()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("ignoreCase");
    }


    @Accessor(qualifier = "keyword", type = Accessor.Type.GETTER)
    public String getKeyword()
    {
        return (String)getPersistenceContext().getPropertyValue("keyword");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "matchType", type = Accessor.Type.GETTER)
    public KeywordRedirectMatchType getMatchType()
    {
        return (KeywordRedirectMatchType)getPersistenceContext().getPropertyValue("matchType");
    }


    @Accessor(qualifier = "redirect", type = Accessor.Type.GETTER)
    public SolrAbstractKeywordRedirectModel getRedirect()
    {
        return (SolrAbstractKeywordRedirectModel)getPersistenceContext().getPropertyValue("redirect");
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
    public void setFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfig", value);
    }


    @Accessor(qualifier = "ignoreCase", type = Accessor.Type.SETTER)
    public void setIgnoreCase(Boolean value)
    {
        getPersistenceContext().setPropertyValue("ignoreCase", value);
    }


    @Accessor(qualifier = "keyword", type = Accessor.Type.SETTER)
    public void setKeyword(String value)
    {
        getPersistenceContext().setPropertyValue("keyword", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "matchType", type = Accessor.Type.SETTER)
    public void setMatchType(KeywordRedirectMatchType value)
    {
        getPersistenceContext().setPropertyValue("matchType", value);
    }


    @Accessor(qualifier = "redirect", type = Accessor.Type.SETTER)
    public void setRedirect(SolrAbstractKeywordRedirectModel value)
    {
        getPersistenceContext().setPropertyValue("redirect", value);
    }
}
