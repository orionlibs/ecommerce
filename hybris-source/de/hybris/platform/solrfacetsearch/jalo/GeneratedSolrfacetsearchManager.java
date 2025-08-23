package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrEndpointUrl;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedType;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexerQuery;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexerQueryParameter;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrServerConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrStopWord;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrSynonymConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrValueRange;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrValueRangeSet;
import de.hybris.platform.solrfacetsearch.jalo.cron.SolrQueryStatisticsCollectorCronJob;
import de.hybris.platform.solrfacetsearch.jalo.cron.SolrUpdateStopWordsCronJob;
import de.hybris.platform.solrfacetsearch.jalo.cron.SolrUpdateSynonymsCronJob;
import de.hybris.platform.solrfacetsearch.jalo.hmc.ComposedIndexedType;
import de.hybris.platform.solrfacetsearch.jalo.indexer.SolrIndexedCoresRecord;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrExtIndexerCronJob;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexOptimizationCronJob;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerCronJob;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerHotUpdateCronJob;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrCategoryRedirect;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrFacetSearchKeywordRedirect;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrProductRedirect;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrURIRedirect;
import de.hybris.platform.solrfacetsearch.jalo.reporting.SolrQueryAggregatedStats;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrfacetsearchManager extends Extension
{
    protected static String SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2CatalogVersionRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2CatalogVersionRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2CatalogVersionRelation.markmodified";
    protected static String SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2CurrencyRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2CurrencyRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2CurrencyRelation.markmodified";
    protected static String SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED = "relation.SolrFacetSearchConfig2LanguageRelation.source.ordered";
    protected static String SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED = "relation.SolrFacetSearchConfig2LanguageRelation.target.ordered";
    protected static String SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED = "relation.SolrFacetSearchConfig2LanguageRelation.markmodified";
    protected static final OneToManyHandler<SolrSynonymConfig> SOLRSYNONYMCONFIG2LANGUAGESYNONYMSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSYNONYMCONFIG, false, "language", "languagePOS", true, true, 2);
    protected static final OneToManyHandler<SolrStopWord> SOLRSTOPWORD2LANGUAGESTOPWORDSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSTOPWORD, false, "language", "languagePOS", true, true, 2);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public ComposedIndexedType createComposedIndexedType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.COMPOSEDINDEXEDTYPE);
            return (ComposedIndexedType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ComposedIndexedType : " + e.getMessage(), 0);
        }
    }


    public ComposedIndexedType createComposedIndexedType(Map attributeValues)
    {
        return createComposedIndexedType(getSession().getSessionContext(), attributeValues);
    }


    public SolrCategoryRedirect createSolrCategoryRedirect(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRCATEGORYREDIRECT);
            return (SolrCategoryRedirect)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrCategoryRedirect : " + e.getMessage(), 0);
        }
    }


    public SolrCategoryRedirect createSolrCategoryRedirect(Map attributeValues)
    {
        return createSolrCategoryRedirect(getSession().getSessionContext(), attributeValues);
    }


    public SolrEndpointUrl createSolrEndpointUrl(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRENDPOINTURL);
            return (SolrEndpointUrl)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrEndpointUrl : " + e.getMessage(), 0);
        }
    }


    public SolrEndpointUrl createSolrEndpointUrl(Map attributeValues)
    {
        return createSolrEndpointUrl(getSession().getSessionContext(), attributeValues);
    }


    public SolrExtIndexerCronJob createSolrExtIndexerCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLREXTINDEXERCRONJOB);
            return (SolrExtIndexerCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrExtIndexerCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrExtIndexerCronJob createSolrExtIndexerCronJob(Map attributeValues)
    {
        return createSolrExtIndexerCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrFacetSearchConfig createSolrFacetSearchConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRFACETSEARCHCONFIG);
            return (SolrFacetSearchConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrFacetSearchConfig : " + e.getMessage(), 0);
        }
    }


    public SolrFacetSearchConfig createSolrFacetSearchConfig(Map attributeValues)
    {
        return createSolrFacetSearchConfig(getSession().getSessionContext(), attributeValues);
    }


    public SolrFacetSearchKeywordRedirect createSolrFacetSearchKeywordRedirect(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRFACETSEARCHKEYWORDREDIRECT);
            return (SolrFacetSearchKeywordRedirect)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrFacetSearchKeywordRedirect : " + e.getMessage(), 0);
        }
    }


    public SolrFacetSearchKeywordRedirect createSolrFacetSearchKeywordRedirect(Map attributeValues)
    {
        return createSolrFacetSearchKeywordRedirect(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndex createSolrIndex(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEX);
            return (SolrIndex)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndex : " + e.getMessage(), 0);
        }
    }


    public SolrIndex createSolrIndex(Map attributeValues)
    {
        return createSolrIndex(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexConfig createSolrIndexConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXCONFIG);
            return (SolrIndexConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexConfig : " + e.getMessage(), 0);
        }
    }


    public SolrIndexConfig createSolrIndexConfig(Map attributeValues)
    {
        return createSolrIndexConfig(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexedCoresRecord createSolrIndexedCoresRecord(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDCORESRECORD);
            return (SolrIndexedCoresRecord)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexedCoresRecord : " + e.getMessage(), 0);
        }
    }


    public SolrIndexedCoresRecord createSolrIndexedCoresRecord(Map attributeValues)
    {
        return createSolrIndexedCoresRecord(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexedProperty createSolrIndexedProperty(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDPROPERTY);
            return (SolrIndexedProperty)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexedProperty : " + e.getMessage(), 0);
        }
    }


    public SolrIndexedProperty createSolrIndexedProperty(Map attributeValues)
    {
        return createSolrIndexedProperty(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexedType createSolrIndexedType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDTYPE);
            return (SolrIndexedType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexedType : " + e.getMessage(), 0);
        }
    }


    public SolrIndexedType createSolrIndexedType(Map attributeValues)
    {
        return createSolrIndexedType(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexerBatch createSolrIndexerBatch(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERBATCH);
            return (SolrIndexerBatch)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexerBatch : " + e.getMessage(), 0);
        }
    }


    public SolrIndexerBatch createSolrIndexerBatch(Map attributeValues)
    {
        return createSolrIndexerBatch(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexerCronJob createSolrIndexerCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERCRONJOB);
            return (SolrIndexerCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexerCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrIndexerCronJob createSolrIndexerCronJob(Map attributeValues)
    {
        return createSolrIndexerCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexerDistributedProcess createSolrIndexerDistributedProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERDISTRIBUTEDPROCESS);
            return (SolrIndexerDistributedProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexerDistributedProcess : " + e.getMessage(), 0);
        }
    }


    public SolrIndexerDistributedProcess createSolrIndexerDistributedProcess(Map attributeValues)
    {
        return createSolrIndexerDistributedProcess(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexerHotUpdateCronJob createSolrIndexerHotUpdateCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERHOTUPDATECRONJOB);
            return (SolrIndexerHotUpdateCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexerHotUpdateCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrIndexerHotUpdateCronJob createSolrIndexerHotUpdateCronJob(Map attributeValues)
    {
        return createSolrIndexerHotUpdateCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexerQuery createSolrIndexerQuery(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERQUERY);
            return (SolrIndexerQuery)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexerQuery : " + e.getMessage(), 0);
        }
    }


    public SolrIndexerQuery createSolrIndexerQuery(Map attributeValues)
    {
        return createSolrIndexerQuery(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexerQueryParameter createSolrIndexerQueryParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERQUERYPARAMETER);
            return (SolrIndexerQueryParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexerQueryParameter : " + e.getMessage(), 0);
        }
    }


    public SolrIndexerQueryParameter createSolrIndexerQueryParameter(Map attributeValues)
    {
        return createSolrIndexerQueryParameter(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexOperation createSolrIndexOperation(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPERATION);
            return (SolrIndexOperation)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexOperation : " + e.getMessage(), 0);
        }
    }


    public SolrIndexOperation createSolrIndexOperation(Map attributeValues)
    {
        return createSolrIndexOperation(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexOperationRecord createSolrIndexOperationRecord(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPERATIONRECORD);
            return (SolrIndexOperationRecord)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexOperationRecord : " + e.getMessage(), 0);
        }
    }


    public SolrIndexOperationRecord createSolrIndexOperationRecord(Map attributeValues)
    {
        return createSolrIndexOperationRecord(getSession().getSessionContext(), attributeValues);
    }


    public SolrIndexOptimizationCronJob createSolrIndexOptimizationCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPTIMIZATIONCRONJOB);
            return (SolrIndexOptimizationCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrIndexOptimizationCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrIndexOptimizationCronJob createSolrIndexOptimizationCronJob(Map attributeValues)
    {
        return createSolrIndexOptimizationCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrProductRedirect createSolrProductRedirect(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRPRODUCTREDIRECT);
            return (SolrProductRedirect)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrProductRedirect : " + e.getMessage(), 0);
        }
    }


    public SolrProductRedirect createSolrProductRedirect(Map attributeValues)
    {
        return createSolrProductRedirect(getSession().getSessionContext(), attributeValues);
    }


    public SolrQueryAggregatedStats createSolrQueryAggregatedStats(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRQUERYAGGREGATEDSTATS);
            return (SolrQueryAggregatedStats)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrQueryAggregatedStats : " + e.getMessage(), 0);
        }
    }


    public SolrQueryAggregatedStats createSolrQueryAggregatedStats(Map attributeValues)
    {
        return createSolrQueryAggregatedStats(getSession().getSessionContext(), attributeValues);
    }


    public SolrQueryStatisticsCollectorCronJob createSolrQueryStatisticsCollectorCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRQUERYSTATISTICSCOLLECTORCRONJOB);
            return (SolrQueryStatisticsCollectorCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrQueryStatisticsCollectorCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrQueryStatisticsCollectorCronJob createSolrQueryStatisticsCollectorCronJob(Map attributeValues)
    {
        return createSolrQueryStatisticsCollectorCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrSearchConfig createSolrSearchConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHCONFIG);
            return (SolrSearchConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSearchConfig : " + e.getMessage(), 0);
        }
    }


    public SolrSearchConfig createSolrSearchConfig(Map attributeValues)
    {
        return createSolrSearchConfig(getSession().getSessionContext(), attributeValues);
    }


    public SolrSearchQueryProperty createSolrSearchQueryProperty(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYPROPERTY);
            return (SolrSearchQueryProperty)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSearchQueryProperty : " + e.getMessage(), 0);
        }
    }


    public SolrSearchQueryProperty createSolrSearchQueryProperty(Map attributeValues)
    {
        return createSolrSearchQueryProperty(getSession().getSessionContext(), attributeValues);
    }


    public SolrSearchQuerySort createSolrSearchQuerySort(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYSORT);
            return (SolrSearchQuerySort)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSearchQuerySort : " + e.getMessage(), 0);
        }
    }


    public SolrSearchQuerySort createSolrSearchQuerySort(Map attributeValues)
    {
        return createSolrSearchQuerySort(getSession().getSessionContext(), attributeValues);
    }


    public SolrSearchQueryTemplate createSolrSearchQueryTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYTEMPLATE);
            return (SolrSearchQueryTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSearchQueryTemplate : " + e.getMessage(), 0);
        }
    }


    public SolrSearchQueryTemplate createSolrSearchQueryTemplate(Map attributeValues)
    {
        return createSolrSearchQueryTemplate(getSession().getSessionContext(), attributeValues);
    }


    public SolrServerConfig createSolrServerConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSERVERCONFIG);
            return (SolrServerConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrServerConfig : " + e.getMessage(), 0);
        }
    }


    public SolrServerConfig createSolrServerConfig(Map attributeValues)
    {
        return createSolrServerConfig(getSession().getSessionContext(), attributeValues);
    }


    public SolrSort createSolrSort(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSORT);
            return (SolrSort)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSort : " + e.getMessage(), 0);
        }
    }


    public SolrSort createSolrSort(Map attributeValues)
    {
        return createSolrSort(getSession().getSessionContext(), attributeValues);
    }


    public SolrSortField createSolrSortField(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSORTFIELD);
            return (SolrSortField)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSortField : " + e.getMessage(), 0);
        }
    }


    public SolrSortField createSolrSortField(Map attributeValues)
    {
        return createSolrSortField(getSession().getSessionContext(), attributeValues);
    }


    public SolrStopWord createSolrStopWord(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSTOPWORD);
            return (SolrStopWord)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrStopWord : " + e.getMessage(), 0);
        }
    }


    public SolrStopWord createSolrStopWord(Map attributeValues)
    {
        return createSolrStopWord(getSession().getSessionContext(), attributeValues);
    }


    public SolrSynonymConfig createSolrSynonymConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRSYNONYMCONFIG);
            return (SolrSynonymConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrSynonymConfig : " + e.getMessage(), 0);
        }
    }


    public SolrSynonymConfig createSolrSynonymConfig(Map attributeValues)
    {
        return createSolrSynonymConfig(getSession().getSessionContext(), attributeValues);
    }


    public SolrUpdateStopWordsCronJob createSolrUpdateStopWordsCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRUPDATESTOPWORDSCRONJOB);
            return (SolrUpdateStopWordsCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrUpdateStopWordsCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrUpdateStopWordsCronJob createSolrUpdateStopWordsCronJob(Map attributeValues)
    {
        return createSolrUpdateStopWordsCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrUpdateSynonymsCronJob createSolrUpdateSynonymsCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRUPDATESYNONYMSCRONJOB);
            return (SolrUpdateSynonymsCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrUpdateSynonymsCronJob : " + e.getMessage(), 0);
        }
    }


    public SolrUpdateSynonymsCronJob createSolrUpdateSynonymsCronJob(Map attributeValues)
    {
        return createSolrUpdateSynonymsCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrURIRedirect createSolrURIRedirect(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRURIREDIRECT);
            return (SolrURIRedirect)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrURIRedirect : " + e.getMessage(), 0);
        }
    }


    public SolrURIRedirect createSolrURIRedirect(Map attributeValues)
    {
        return createSolrURIRedirect(getSession().getSessionContext(), attributeValues);
    }


    public SolrValueRange createSolrValueRange(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRVALUERANGE);
            return (SolrValueRange)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrValueRange : " + e.getMessage(), 0);
        }
    }


    public SolrValueRange createSolrValueRange(Map attributeValues)
    {
        return createSolrValueRange(getSession().getSessionContext(), attributeValues);
    }


    public SolrValueRangeSet createSolrValueRangeSet(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedSolrfacetsearchConstants.TC.SOLRVALUERANGESET);
            return (SolrValueRangeSet)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SolrValueRangeSet : " + e.getMessage(), 0);
        }
    }


    public SolrValueRangeSet createSolrValueRangeSet(Map attributeValues)
    {
        return createSolrValueRangeSet(getSession().getSessionContext(), attributeValues);
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(SessionContext ctx, CatalogVersion item)
    {
        List<SolrFacetSearchConfig> items = item.getLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, "SolrFacetSearchConfig", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(CatalogVersion item)
    {
        return getFacetSearchConfigs(getSession().getSessionContext(), item);
    }


    public long getFacetSearchConfigsCount(SessionContext ctx, CatalogVersion item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, "SolrFacetSearchConfig", null);
    }


    public long getFacetSearchConfigsCount(CatalogVersion item)
    {
        return getFacetSearchConfigsCount(getSession().getSessionContext(), item);
    }


    public void setFacetSearchConfigs(SessionContext ctx, CatalogVersion item, List<SolrFacetSearchConfig> value)
    {
        item.setLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void setFacetSearchConfigs(CatalogVersion item, List<SolrFacetSearchConfig> value)
    {
        setFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public void addToFacetSearchConfigs(SessionContext ctx, CatalogVersion item, SolrFacetSearchConfig value)
    {
        item.addLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void addToFacetSearchConfigs(CatalogVersion item, SolrFacetSearchConfig value)
    {
        addToFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public void removeFromFacetSearchConfigs(SessionContext ctx, CatalogVersion item, SolrFacetSearchConfig value)
    {
        item.removeLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void removeFromFacetSearchConfigs(CatalogVersion item, SolrFacetSearchConfig value)
    {
        removeFromFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(SessionContext ctx, Currency item)
    {
        List<SolrFacetSearchConfig> items = item.getLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, "SolrFacetSearchConfig", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(Currency item)
    {
        return getFacetSearchConfigs(getSession().getSessionContext(), item);
    }


    public long getFacetSearchConfigsCount(SessionContext ctx, Currency item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, "SolrFacetSearchConfig", null);
    }


    public long getFacetSearchConfigsCount(Currency item)
    {
        return getFacetSearchConfigsCount(getSession().getSessionContext(), item);
    }


    public void setFacetSearchConfigs(SessionContext ctx, Currency item, List<SolrFacetSearchConfig> value)
    {
        item.setLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED));
    }


    public void setFacetSearchConfigs(Currency item, List<SolrFacetSearchConfig> value)
    {
        setFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public void addToFacetSearchConfigs(SessionContext ctx, Currency item, SolrFacetSearchConfig value)
    {
        item.addLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED));
    }


    public void addToFacetSearchConfigs(Currency item, SolrFacetSearchConfig value)
    {
        addToFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public void removeFromFacetSearchConfigs(SessionContext ctx, Currency item, SolrFacetSearchConfig value)
    {
        item.removeLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2CURRENCYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2CURRENCYRELATION_MARKMODIFIED));
    }


    public void removeFromFacetSearchConfigs(Currency item, SolrFacetSearchConfig value)
    {
        removeFromFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(SessionContext ctx, Language item)
    {
        List<SolrFacetSearchConfig> items = item.getLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, "SolrFacetSearchConfig", null,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrFacetSearchConfig> getFacetSearchConfigs(Language item)
    {
        return getFacetSearchConfigs(getSession().getSessionContext(), item);
    }


    public long getFacetSearchConfigsCount(SessionContext ctx, Language item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, "SolrFacetSearchConfig", null);
    }


    public long getFacetSearchConfigsCount(Language item)
    {
        return getFacetSearchConfigsCount(getSession().getSessionContext(), item);
    }


    public void setFacetSearchConfigs(SessionContext ctx, Language item, List<SolrFacetSearchConfig> value)
    {
        item.setLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED));
    }


    public void setFacetSearchConfigs(Language item, List<SolrFacetSearchConfig> value)
    {
        setFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public void addToFacetSearchConfigs(SessionContext ctx, Language item, SolrFacetSearchConfig value)
    {
        item.addLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED));
    }


    public void addToFacetSearchConfigs(Language item, SolrFacetSearchConfig value)
    {
        addToFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public void removeFromFacetSearchConfigs(SessionContext ctx, Language item, SolrFacetSearchConfig value)
    {
        item.removeLinkedItems(ctx, false, GeneratedSolrfacetsearchConstants.Relations.SOLRFACETSEARCHCONFIG2LANGUAGERELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRFACETSEARCHCONFIG2LANGUAGERELATION_MARKMODIFIED));
    }


    public void removeFromFacetSearchConfigs(Language item, SolrFacetSearchConfig value)
    {
        removeFromFacetSearchConfigs(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "solrfacetsearch";
    }


    public List<SolrStopWord> getStopWords(SessionContext ctx, Language item)
    {
        return (List<SolrStopWord>)SOLRSTOPWORD2LANGUAGESTOPWORDSHANDLER.getValues(ctx, (Item)item);
    }


    public List<SolrStopWord> getStopWords(Language item)
    {
        return getStopWords(getSession().getSessionContext(), item);
    }


    public void setStopWords(SessionContext ctx, Language item, List<SolrStopWord> value)
    {
        SOLRSTOPWORD2LANGUAGESTOPWORDSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setStopWords(Language item, List<SolrStopWord> value)
    {
        setStopWords(getSession().getSessionContext(), item, value);
    }


    public void addToStopWords(SessionContext ctx, Language item, SolrStopWord value)
    {
        SOLRSTOPWORD2LANGUAGESTOPWORDSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToStopWords(Language item, SolrStopWord value)
    {
        addToStopWords(getSession().getSessionContext(), item, value);
    }


    public void removeFromStopWords(SessionContext ctx, Language item, SolrStopWord value)
    {
        SOLRSTOPWORD2LANGUAGESTOPWORDSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromStopWords(Language item, SolrStopWord value)
    {
        removeFromStopWords(getSession().getSessionContext(), item, value);
    }


    public List<SolrSynonymConfig> getSynonyms(SessionContext ctx, Language item)
    {
        return (List<SolrSynonymConfig>)SOLRSYNONYMCONFIG2LANGUAGESYNONYMSHANDLER.getValues(ctx, (Item)item);
    }


    public List<SolrSynonymConfig> getSynonyms(Language item)
    {
        return getSynonyms(getSession().getSessionContext(), item);
    }


    public void setSynonyms(SessionContext ctx, Language item, List<SolrSynonymConfig> value)
    {
        SOLRSYNONYMCONFIG2LANGUAGESYNONYMSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setSynonyms(Language item, List<SolrSynonymConfig> value)
    {
        setSynonyms(getSession().getSessionContext(), item, value);
    }


    public void addToSynonyms(SessionContext ctx, Language item, SolrSynonymConfig value)
    {
        SOLRSYNONYMCONFIG2LANGUAGESYNONYMSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToSynonyms(Language item, SolrSynonymConfig value)
    {
        addToSynonyms(getSession().getSessionContext(), item, value);
    }


    public void removeFromSynonyms(SessionContext ctx, Language item, SolrSynonymConfig value)
    {
        SOLRSYNONYMCONFIG2LANGUAGESYNONYMSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromSynonyms(Language item, SolrSynonymConfig value)
    {
        removeFromSynonyms(getSession().getSessionContext(), item, value);
    }
}
