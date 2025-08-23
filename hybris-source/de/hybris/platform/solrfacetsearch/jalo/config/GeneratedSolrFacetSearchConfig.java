package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.cron.SolrUpdateStopWordsCronJob;
import de.hybris.platform.solrfacetsearch.jalo.cron.SolrUpdateSynonymsCronJob;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerCronJob;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrFacetSearchKeywordRedirect;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrFacetSearchConfig extends GenericItem
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DOCUMENT = "document";
    public static final String SOLRSEARCHCONFIG = "solrSearchConfig";
    public static final String SOLRINDEXCONFIG = "solrIndexConfig";
    public static final String SOLRSERVERCONFIG = "solrServerConfig";
    public static final String INDEXNAMEPREFIX = "indexNamePrefix";
    public static final String LANGUAGESYNONYMMAPPING = "languageSynonymMapping";
    public static final String LANGUAGESTOPWORDMAPPING = "languageStopWordMapping";
    public static final String LANGUAGEKEYWORDREDIRECTMAPPING = "languageKeywordRedirectMapping";
    public static final String ENABLEDLANGUAGEFALLBACKMECHANISM = "enabledLanguageFallbackMechanism";
    public static final String LISTENERS = "listeners";
    public static final String CATALOGVERSIONS = "catalogVersions";
    protected static String SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2CatalogVersionRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2CatalogVersionRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2CatalogVersionRelation.markmodified";
    public static final String CURRENCIES = "currencies";
    protected static String SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2CurrencyRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2CurrencyRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2CurrencyRelation.markmodified";
    public static final String LANGUAGES = "languages";
    protected static String SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2LanguageRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2LanguageRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2LanguageRelation.markmodified";
    public static final String SOLRVALUERANGESETS = "solrValueRangeSets";
    protected static String SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2SolrValueRangeSetRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2SolrValueRangeSetRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2SolrValueRangeSetRelation.markmodified";
    public static final String SOLRINDEXERCRONJOB = "solrIndexerCronJob";
    public static final String SOLRINDEXEDTYPES = "solrIndexedTypes";
    public static final String KEYWORDREDIRECTS = "keywordRedirects";
    public static final String SOLRUPDATESYNONYMSCRONJOBS = "solrUpdateSynonymsCronJobs";
    public static final String SOLRUPDATESTOPWORDSCRONJOBS = "solrUpdateStopWordsCronJobs";
    public static final String SYNONYMS = "synonyms";
    public static final String STOPWORDS = "StopWords";
    protected static final OneToManyHandler<SolrIndexerCronJob> SOLRINDEXERCRONJOBHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERCRONJOB, false, "facetSearchConfig", "facetSearchConfigPOS", true, true, 2);
    protected static final OneToManyHandler<SolrIndexedType> SOLRINDEXEDTYPESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDTYPE, false, "solrFacetSearchConfig", "solrFacetSearchConfigPOS", true, true, 2);
    protected static final OneToManyHandler<SolrFacetSearchKeywordRedirect> KEYWORDREDIRECTSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRFACETSEARCHKEYWORDREDIRECT, false, "facetSearchConfig", null, false, true, 0);
    protected static final OneToManyHandler<SolrUpdateSynonymsCronJob> SOLRUPDATESYNONYMSCRONJOBSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRUPDATESYNONYMSCRONJOB, false, "solrFacetSearchConfig", "solrFacetSearchConfigPOS", true, true, 2);
    protected static final OneToManyHandler<SolrUpdateStopWordsCronJob> SOLRUPDATESTOPWORDSCRONJOBSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRUPDATESTOPWORDSCRONJOB, false, "solrFacetSearchConfig", "solrFacetSearchConfigPOS", true, true, 2);
    protected static final OneToManyHandler<SolrSynonymConfig> SYNONYMSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSYNONYMCONFIG, false, "facetSearchConfig", "facetSearchConfigPOS", true, true, 2);
    protected static final OneToManyHandler<SolrStopWord> STOPWORDSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSTOPWORD, false, "facetSearchConfig", "facetSearchConfigPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("document", Item.AttributeMode.INITIAL);
        tmp.put("solrSearchConfig", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexConfig", Item.AttributeMode.INITIAL);
        tmp.put("solrServerConfig", Item.AttributeMode.INITIAL);
        tmp.put("indexNamePrefix", Item.AttributeMode.INITIAL);
        tmp.put("enabledLanguageFallbackMechanism", Item.AttributeMode.INITIAL);
        tmp.put("listeners", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CatalogVersion> getCatalogVersions(SessionContext ctx)
    {
        List<CatalogVersion> items = getLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, "CatalogVersion", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<CatalogVersion> getCatalogVersions()
    {
        return getCatalogVersions(getSession().getSessionContext());
    }


    public long getCatalogVersionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, "CatalogVersion", null);
    }


    public long getCatalogVersionsCount()
    {
        return getCatalogVersionsCount(getSession().getSessionContext());
    }


    public void setCatalogVersions(SessionContext ctx, List<CatalogVersion> value)
    {
        setLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void setCatalogVersions(List<CatalogVersion> value)
    {
        setCatalogVersions(getSession().getSessionContext(), value);
    }


    public void addToCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        addLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void addToCatalogVersions(CatalogVersion value)
    {
        addToCatalogVersions(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        removeLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void removeFromCatalogVersions(CatalogVersion value)
    {
        removeFromCatalogVersions(getSession().getSessionContext(), value);
    }


    public List<Currency> getCurrencies(SessionContext ctx)
    {
        List<Currency> items = getLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, "Currency", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Currency> getCurrencies()
    {
        return getCurrencies(getSession().getSessionContext());
    }


    public long getCurrenciesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, "Currency", null);
    }


    public long getCurrenciesCount()
    {
        return getCurrenciesCount(getSession().getSessionContext());
    }


    public void setCurrencies(SessionContext ctx, List<Currency> value)
    {
        setLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED));
    }


    public void setCurrencies(List<Currency> value)
    {
        setCurrencies(getSession().getSessionContext(), value);
    }


    public void addToCurrencies(SessionContext ctx, Currency value)
    {
        addLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED));
    }


    public void addToCurrencies(Currency value)
    {
        addToCurrencies(getSession().getSessionContext(), value);
    }


    public void removeFromCurrencies(SessionContext ctx, Currency value)
    {
        removeLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED));
    }


    public void removeFromCurrencies(Currency value)
    {
        removeFromCurrencies(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public Media getDocument(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "document");
    }


    public Media getDocument()
    {
        return getDocument(getSession().getSessionContext());
    }


    public void setDocument(SessionContext ctx, Media value)
    {
        setProperty(ctx, "document", value);
    }


    public void setDocument(Media value)
    {
        setDocument(getSession().getSessionContext(), value);
    }


    public Boolean isEnabledLanguageFallbackMechanism(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enabledLanguageFallbackMechanism");
    }


    public Boolean isEnabledLanguageFallbackMechanism()
    {
        return isEnabledLanguageFallbackMechanism(getSession().getSessionContext());
    }


    public boolean isEnabledLanguageFallbackMechanismAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnabledLanguageFallbackMechanism(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnabledLanguageFallbackMechanismAsPrimitive()
    {
        return isEnabledLanguageFallbackMechanismAsPrimitive(getSession().getSessionContext());
    }


    public void setEnabledLanguageFallbackMechanism(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enabledLanguageFallbackMechanism", value);
    }


    public void setEnabledLanguageFallbackMechanism(Boolean value)
    {
        setEnabledLanguageFallbackMechanism(getSession().getSessionContext(), value);
    }


    public void setEnabledLanguageFallbackMechanism(SessionContext ctx, boolean value)
    {
        setEnabledLanguageFallbackMechanism(ctx, Boolean.valueOf(value));
    }


    public void setEnabledLanguageFallbackMechanism(boolean value)
    {
        setEnabledLanguageFallbackMechanism(getSession().getSessionContext(), value);
    }


    public String getIndexNamePrefix(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexNamePrefix");
    }


    public String getIndexNamePrefix()
    {
        return getIndexNamePrefix(getSession().getSessionContext());
    }


    public void setIndexNamePrefix(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexNamePrefix", value);
    }


    public void setIndexNamePrefix(String value)
    {
        setIndexNamePrefix(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CatalogVersion");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Currency");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("Language");
        if(relationSecondEnd2.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd3 = TypeManager.getInstance().getComposedType("SolrValueRangeSet");
        if(relationSecondEnd3.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Collection<SolrFacetSearchKeywordRedirect> getKeywordRedirects(SessionContext ctx)
    {
        return KEYWORDREDIRECTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrFacetSearchKeywordRedirect> getKeywordRedirects()
    {
        return getKeywordRedirects(getSession().getSessionContext());
    }


    public void setKeywordRedirects(SessionContext ctx, Collection<SolrFacetSearchKeywordRedirect> value)
    {
        KEYWORDREDIRECTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setKeywordRedirects(Collection<SolrFacetSearchKeywordRedirect> value)
    {
        setKeywordRedirects(getSession().getSessionContext(), value);
    }


    public void addToKeywordRedirects(SessionContext ctx, SolrFacetSearchKeywordRedirect value)
    {
        KEYWORDREDIRECTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToKeywordRedirects(SolrFacetSearchKeywordRedirect value)
    {
        addToKeywordRedirects(getSession().getSessionContext(), value);
    }


    public void removeFromKeywordRedirects(SessionContext ctx, SolrFacetSearchKeywordRedirect value)
    {
        KEYWORDREDIRECTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromKeywordRedirects(SolrFacetSearchKeywordRedirect value)
    {
        removeFromKeywordRedirects(getSession().getSessionContext(), value);
    }


    public List<SolrFacetSearchKeywordRedirect> getLanguageKeywordRedirectMapping()
    {
        return getLanguageKeywordRedirectMapping(getSession().getSessionContext());
    }


    public Map<Language, List<SolrFacetSearchKeywordRedirect>> getAllLanguageKeywordRedirectMapping()
    {
        return getAllLanguageKeywordRedirectMapping(getSession().getSessionContext());
    }


    public void setLanguageKeywordRedirectMapping(List<SolrFacetSearchKeywordRedirect> value)
    {
        setLanguageKeywordRedirectMapping(getSession().getSessionContext(), value);
    }


    public void setAllLanguageKeywordRedirectMapping(Map<Language, List<SolrFacetSearchKeywordRedirect>> value)
    {
        setAllLanguageKeywordRedirectMapping(getSession().getSessionContext(), value);
    }


    public List<Language> getLanguages(SessionContext ctx)
    {
        List<Language> items = getLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, "Language", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Language> getLanguages()
    {
        return getLanguages(getSession().getSessionContext());
    }


    public long getLanguagesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, "Language", null);
    }


    public long getLanguagesCount()
    {
        return getLanguagesCount(getSession().getSessionContext());
    }


    public void setLanguages(SessionContext ctx, List<Language> value)
    {
        setLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED));
    }


    public void setLanguages(List<Language> value)
    {
        setLanguages(getSession().getSessionContext(), value);
    }


    public void addToLanguages(SessionContext ctx, Language value)
    {
        addLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED));
    }


    public void addToLanguages(Language value)
    {
        addToLanguages(getSession().getSessionContext(), value);
    }


    public void removeFromLanguages(SessionContext ctx, Language value)
    {
        removeLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED));
    }


    public void removeFromLanguages(Language value)
    {
        removeFromLanguages(getSession().getSessionContext(), value);
    }


    public List<SolrStopWord> getLanguageStopWordMapping()
    {
        return getLanguageStopWordMapping(getSession().getSessionContext());
    }


    public Map<Language, List<SolrStopWord>> getAllLanguageStopWordMapping()
    {
        return getAllLanguageStopWordMapping(getSession().getSessionContext());
    }


    public void setLanguageStopWordMapping(List<SolrStopWord> value)
    {
        setLanguageStopWordMapping(getSession().getSessionContext(), value);
    }


    public void setAllLanguageStopWordMapping(Map<Language, List<SolrStopWord>> value)
    {
        setAllLanguageStopWordMapping(getSession().getSessionContext(), value);
    }


    public List<SolrSynonymConfig> getLanguageSynonymMapping()
    {
        return getLanguageSynonymMapping(getSession().getSessionContext());
    }


    public Map<Language, List<SolrSynonymConfig>> getAllLanguageSynonymMapping()
    {
        return getAllLanguageSynonymMapping(getSession().getSessionContext());
    }


    public void setLanguageSynonymMapping(List<SolrSynonymConfig> value)
    {
        setLanguageSynonymMapping(getSession().getSessionContext(), value);
    }


    public void setAllLanguageSynonymMapping(Map<Language, List<SolrSynonymConfig>> value)
    {
        setAllLanguageSynonymMapping(getSession().getSessionContext(), value);
    }


    public Collection<String> getListeners(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "listeners");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getListeners()
    {
        return getListeners(getSession().getSessionContext());
    }


    public void setListeners(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "listeners", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setListeners(Collection<String> value)
    {
        setListeners(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public SolrIndexConfig getSolrIndexConfig(SessionContext ctx)
    {
        return (SolrIndexConfig)getProperty(ctx, "solrIndexConfig");
    }


    public SolrIndexConfig getSolrIndexConfig()
    {
        return getSolrIndexConfig(getSession().getSessionContext());
    }


    public void setSolrIndexConfig(SessionContext ctx, SolrIndexConfig value)
    {
        setProperty(ctx, "solrIndexConfig", value);
    }


    public void setSolrIndexConfig(SolrIndexConfig value)
    {
        setSolrIndexConfig(getSession().getSessionContext(), value);
    }


    public List<SolrIndexedType> getSolrIndexedTypes(SessionContext ctx)
    {
        return (List<SolrIndexedType>)SOLRINDEXEDTYPESHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrIndexedType> getSolrIndexedTypes()
    {
        return getSolrIndexedTypes(getSession().getSessionContext());
    }


    public void setSolrIndexedTypes(SessionContext ctx, List<SolrIndexedType> value)
    {
        SOLRINDEXEDTYPESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrIndexedTypes(List<SolrIndexedType> value)
    {
        setSolrIndexedTypes(getSession().getSessionContext(), value);
    }


    public void addToSolrIndexedTypes(SessionContext ctx, SolrIndexedType value)
    {
        SOLRINDEXEDTYPESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrIndexedTypes(SolrIndexedType value)
    {
        addToSolrIndexedTypes(getSession().getSessionContext(), value);
    }


    public void removeFromSolrIndexedTypes(SessionContext ctx, SolrIndexedType value)
    {
        SOLRINDEXEDTYPESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrIndexedTypes(SolrIndexedType value)
    {
        removeFromSolrIndexedTypes(getSession().getSessionContext(), value);
    }


    public List<SolrIndexerCronJob> getSolrIndexerCronJob(SessionContext ctx)
    {
        return (List<SolrIndexerCronJob>)SOLRINDEXERCRONJOBHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrIndexerCronJob> getSolrIndexerCronJob()
    {
        return getSolrIndexerCronJob(getSession().getSessionContext());
    }


    public void setSolrIndexerCronJob(SessionContext ctx, List<SolrIndexerCronJob> value)
    {
        SOLRINDEXERCRONJOBHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrIndexerCronJob(List<SolrIndexerCronJob> value)
    {
        setSolrIndexerCronJob(getSession().getSessionContext(), value);
    }


    public void addToSolrIndexerCronJob(SessionContext ctx, SolrIndexerCronJob value)
    {
        SOLRINDEXERCRONJOBHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrIndexerCronJob(SolrIndexerCronJob value)
    {
        addToSolrIndexerCronJob(getSession().getSessionContext(), value);
    }


    public void removeFromSolrIndexerCronJob(SessionContext ctx, SolrIndexerCronJob value)
    {
        SOLRINDEXERCRONJOBHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrIndexerCronJob(SolrIndexerCronJob value)
    {
        removeFromSolrIndexerCronJob(getSession().getSessionContext(), value);
    }


    public SolrSearchConfig getSolrSearchConfig(SessionContext ctx)
    {
        return (SolrSearchConfig)getProperty(ctx, "solrSearchConfig");
    }


    public SolrSearchConfig getSolrSearchConfig()
    {
        return getSolrSearchConfig(getSession().getSessionContext());
    }


    public void setSolrSearchConfig(SessionContext ctx, SolrSearchConfig value)
    {
        setProperty(ctx, "solrSearchConfig", value);
    }


    public void setSolrSearchConfig(SolrSearchConfig value)
    {
        setSolrSearchConfig(getSession().getSessionContext(), value);
    }


    public SolrServerConfig getSolrServerConfig(SessionContext ctx)
    {
        return (SolrServerConfig)getProperty(ctx, "solrServerConfig");
    }


    public SolrServerConfig getSolrServerConfig()
    {
        return getSolrServerConfig(getSession().getSessionContext());
    }


    public void setSolrServerConfig(SessionContext ctx, SolrServerConfig value)
    {
        setProperty(ctx, "solrServerConfig", value);
    }


    public void setSolrServerConfig(SolrServerConfig value)
    {
        setSolrServerConfig(getSession().getSessionContext(), value);
    }


    public List<SolrUpdateStopWordsCronJob> getSolrUpdateStopWordsCronJobs(SessionContext ctx)
    {
        return (List<SolrUpdateStopWordsCronJob>)SOLRUPDATESTOPWORDSCRONJOBSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrUpdateStopWordsCronJob> getSolrUpdateStopWordsCronJobs()
    {
        return getSolrUpdateStopWordsCronJobs(getSession().getSessionContext());
    }


    public void setSolrUpdateStopWordsCronJobs(SessionContext ctx, List<SolrUpdateStopWordsCronJob> value)
    {
        SOLRUPDATESTOPWORDSCRONJOBSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrUpdateStopWordsCronJobs(List<SolrUpdateStopWordsCronJob> value)
    {
        setSolrUpdateStopWordsCronJobs(getSession().getSessionContext(), value);
    }


    public void addToSolrUpdateStopWordsCronJobs(SessionContext ctx, SolrUpdateStopWordsCronJob value)
    {
        SOLRUPDATESTOPWORDSCRONJOBSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrUpdateStopWordsCronJobs(SolrUpdateStopWordsCronJob value)
    {
        addToSolrUpdateStopWordsCronJobs(getSession().getSessionContext(), value);
    }


    public void removeFromSolrUpdateStopWordsCronJobs(SessionContext ctx, SolrUpdateStopWordsCronJob value)
    {
        SOLRUPDATESTOPWORDSCRONJOBSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrUpdateStopWordsCronJobs(SolrUpdateStopWordsCronJob value)
    {
        removeFromSolrUpdateStopWordsCronJobs(getSession().getSessionContext(), value);
    }


    public List<SolrUpdateSynonymsCronJob> getSolrUpdateSynonymsCronJobs(SessionContext ctx)
    {
        return (List<SolrUpdateSynonymsCronJob>)SOLRUPDATESYNONYMSCRONJOBSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrUpdateSynonymsCronJob> getSolrUpdateSynonymsCronJobs()
    {
        return getSolrUpdateSynonymsCronJobs(getSession().getSessionContext());
    }


    public void setSolrUpdateSynonymsCronJobs(SessionContext ctx, List<SolrUpdateSynonymsCronJob> value)
    {
        SOLRUPDATESYNONYMSCRONJOBSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrUpdateSynonymsCronJobs(List<SolrUpdateSynonymsCronJob> value)
    {
        setSolrUpdateSynonymsCronJobs(getSession().getSessionContext(), value);
    }


    public void addToSolrUpdateSynonymsCronJobs(SessionContext ctx, SolrUpdateSynonymsCronJob value)
    {
        SOLRUPDATESYNONYMSCRONJOBSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrUpdateSynonymsCronJobs(SolrUpdateSynonymsCronJob value)
    {
        addToSolrUpdateSynonymsCronJobs(getSession().getSessionContext(), value);
    }


    public void removeFromSolrUpdateSynonymsCronJobs(SessionContext ctx, SolrUpdateSynonymsCronJob value)
    {
        SOLRUPDATESYNONYMSCRONJOBSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrUpdateSynonymsCronJobs(SolrUpdateSynonymsCronJob value)
    {
        removeFromSolrUpdateSynonymsCronJobs(getSession().getSessionContext(), value);
    }


    public List<SolrValueRangeSet> getSolrValueRangeSets(SessionContext ctx)
    {
        List<SolrValueRangeSet> items = getLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, "SolrValueRangeSet", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrValueRangeSet> getSolrValueRangeSets()
    {
        return getSolrValueRangeSets(getSession().getSessionContext());
    }


    public long getSolrValueRangeSetsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, "SolrValueRangeSet", null);
    }


    public long getSolrValueRangeSetsCount()
    {
        return getSolrValueRangeSetsCount(getSession().getSessionContext());
    }


    public void setSolrValueRangeSets(SessionContext ctx, List<SolrValueRangeSet> value)
    {
        setLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void setSolrValueRangeSets(List<SolrValueRangeSet> value)
    {
        setSolrValueRangeSets(getSession().getSessionContext(), value);
    }


    public void addToSolrValueRangeSets(SessionContext ctx, SolrValueRangeSet value)
    {
        addLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void addToSolrValueRangeSets(SolrValueRangeSet value)
    {
        addToSolrValueRangeSets(getSession().getSessionContext(), value);
    }


    public void removeFromSolrValueRangeSets(SessionContext ctx, SolrValueRangeSet value)
    {
        removeLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void removeFromSolrValueRangeSets(SolrValueRangeSet value)
    {
        removeFromSolrValueRangeSets(getSession().getSessionContext(), value);
    }


    public List<SolrStopWord> getStopWords(SessionContext ctx)
    {
        return (List<SolrStopWord>)STOPWORDSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrStopWord> getStopWords()
    {
        return getStopWords(getSession().getSessionContext());
    }


    public void setStopWords(SessionContext ctx, List<SolrStopWord> value)
    {
        STOPWORDSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setStopWords(List<SolrStopWord> value)
    {
        setStopWords(getSession().getSessionContext(), value);
    }


    public void addToStopWords(SessionContext ctx, SolrStopWord value)
    {
        STOPWORDSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToStopWords(SolrStopWord value)
    {
        addToStopWords(getSession().getSessionContext(), value);
    }


    public void removeFromStopWords(SessionContext ctx, SolrStopWord value)
    {
        STOPWORDSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromStopWords(SolrStopWord value)
    {
        removeFromStopWords(getSession().getSessionContext(), value);
    }


    public List<SolrSynonymConfig> getSynonyms(SessionContext ctx)
    {
        return (List<SolrSynonymConfig>)SYNONYMSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrSynonymConfig> getSynonyms()
    {
        return getSynonyms(getSession().getSessionContext());
    }


    public void setSynonyms(SessionContext ctx, List<SolrSynonymConfig> value)
    {
        SYNONYMSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSynonyms(List<SolrSynonymConfig> value)
    {
        setSynonyms(getSession().getSessionContext(), value);
    }


    public void addToSynonyms(SessionContext ctx, SolrSynonymConfig value)
    {
        SYNONYMSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSynonyms(SolrSynonymConfig value)
    {
        addToSynonyms(getSession().getSessionContext(), value);
    }


    public void removeFromSynonyms(SessionContext ctx, SolrSynonymConfig value)
    {
        SYNONYMSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSynonyms(SolrSynonymConfig value)
    {
        removeFromSynonyms(getSession().getSessionContext(), value);
    }


    public abstract List<SolrFacetSearchKeywordRedirect> getLanguageKeywordRedirectMapping(SessionContext paramSessionContext);


    public abstract Map<Language, List<SolrFacetSearchKeywordRedirect>> getAllLanguageKeywordRedirectMapping(SessionContext paramSessionContext);


    public abstract void setLanguageKeywordRedirectMapping(SessionContext paramSessionContext, List<SolrFacetSearchKeywordRedirect> paramList);


    public abstract void setAllLanguageKeywordRedirectMapping(SessionContext paramSessionContext, Map<Language, List<SolrFacetSearchKeywordRedirect>> paramMap);


    public abstract List<SolrStopWord> getLanguageStopWordMapping(SessionContext paramSessionContext);


    public abstract Map<Language, List<SolrStopWord>> getAllLanguageStopWordMapping(SessionContext paramSessionContext);


    public abstract void setLanguageStopWordMapping(SessionContext paramSessionContext, List<SolrStopWord> paramList);


    public abstract void setAllLanguageStopWordMapping(SessionContext paramSessionContext, Map<Language, List<SolrStopWord>> paramMap);


    public abstract List<SolrSynonymConfig> getLanguageSynonymMapping(SessionContext paramSessionContext);


    public abstract Map<Language, List<SolrSynonymConfig>> getAllLanguageSynonymMapping(SessionContext paramSessionContext);


    public abstract void setLanguageSynonymMapping(SessionContext paramSessionContext, List<SolrSynonymConfig> paramList);


    public abstract void setAllLanguageSynonymMapping(SessionContext paramSessionContext, Map<Language, List<SolrSynonymConfig>> paramMap);
}
