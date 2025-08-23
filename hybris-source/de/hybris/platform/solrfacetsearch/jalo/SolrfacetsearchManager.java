package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloTypeException;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerCronJob;
import de.hybris.platform.solrfacetsearch.jalo.indexer.cron.SolrIndexerHotUpdateCronJob;
import de.hybris.platform.util.JspContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class SolrfacetsearchManager extends GeneratedSolrfacetsearchManager
{
    private static final Logger LOG = Logger.getLogger(SolrfacetsearchManager.class.getName());


    public SolrfacetsearchManager()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("constructor of SolrfacetsearchManager called.");
        }
    }


    public static SolrfacetsearchManager getInstance()
    {
        return (SolrfacetsearchManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("solrfacetsearch");
    }


    public void init()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("init() of SolrfacetsearchManager called. " + getTenant().getTenantID());
        }
    }


    public void destroy()
    {
    }


    public void createEssentialData(Map<String, String> params, JspContext jspc)
    {
    }


    public void createProjectData(Map<String, String> params, JspContext jspc)
    {
    }


    public SolrFacetSearchConfig getSolrFacetConfig(String name)
    {
        FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
        String query = "SELECT {pk} FROM {SolrFacetSearchConfig} WHERE {name}=?name";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        List<SolrFacetSearchConfig> result = flexibleSearch.search("SELECT {pk} FROM {SolrFacetSearchConfig} WHERE {name}=?name", parameters, SolrFacetSearchConfig.class).getResult();
        if(result == null || result.isEmpty())
        {
            throw new FlexibleSearchException("Solr configuration [" + name + "] could not be found");
        }
        if(result.size() != 1)
        {
            throw new FlexibleSearchException("Solr configuration [" + name + "] is ambiguous.");
        }
        return result.get(0);
    }


    public SolrIndexerCronJob createSolrIndexerCronJob(SolrFacetSearchConfig solrFacetSearchConfig, EnumerationValue solrIndexerOperation) throws JaloTypeException
    {
        String code = CronJobManager.getInstance().getNextCronjobNumber() + "-Solrfacetsearch-IndexerOperation";
        return createSolrIndexerCronJob(code, solrFacetSearchConfig, solrIndexerOperation);
    }


    public SolrIndexerHotUpdateCronJob createSolrIndexerHotUpdateCronJob(SolrFacetSearchConfig solrFacetSearchConfig, EnumerationValue solrIndexerOperation, String indexedType, Collection<Item> items) throws JaloTypeException
    {
        String code = CronJobManager.getInstance().getNextCronjobNumber() + "-Solrfacetsearch-IndexerOperation";
        return createSolrIndexerHotUpdateCronJob(code, solrFacetSearchConfig, solrIndexerOperation, indexedType, items);
    }


    public SolrIndexerCronJob createSolrIndexerCronJob(String code, SolrFacetSearchConfig solrFacetSearchConfig, EnumerationValue solrIndexerOperation) throws JaloTypeException
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        ComposedType solrIndexerCronJob = jaloSession.getTypeManager().getComposedType(SolrIndexerCronJob.class);
        Map<String, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("indexerOperation", solrIndexerOperation);
        values.put("facetSearchConfig", solrFacetSearchConfig);
        values.put("logToDatabase", Boolean.TRUE);
        ServicelayerJob servicelayerJob = getSolrIndexerJob();
        values.put("job", servicelayerJob);
        return (SolrIndexerCronJob)solrIndexerCronJob.newInstance(values);
    }


    public SolrIndexerHotUpdateCronJob createSolrIndexerHotUpdateCronJob(String code, SolrFacetSearchConfig solrFacetSearchConfig, EnumerationValue solrIndexerOperation, String indexedType, Collection<Item> items) throws JaloTypeException
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        ComposedType solrIndexerCronJob = jaloSession.getTypeManager().getComposedType(SolrIndexerHotUpdateCronJob.class);
        Map<String, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("indexerOperation", solrIndexerOperation);
        values.put("facetSearchConfig", solrFacetSearchConfig);
        values.put("indexTypeName", indexedType);
        values.put("items", items);
        values.put("logToDatabase", Boolean.TRUE);
        ServicelayerJob servicelayerJob = getSolrIndexerJob(true);
        values.put("job", servicelayerJob);
        return (SolrIndexerHotUpdateCronJob)solrIndexerCronJob.newInstance(values);
    }


    public ServicelayerJob getSolrIndexerJob()
    {
        return getSolrIndexerJob(false);
    }


    public ServicelayerJob getSolrIndexerJob(boolean hotUpdate)
    {
        FlexibleSearch flexibleSearch = FlexibleSearch.getInstance();
        String query = " select {PK} from {ServicelayerJob} where {CODE} = ?code ";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", hotUpdate ? "solrIndexerHotUpdateJob" :
                        "solrIndexerJob");
        List<ServicelayerJob> result = flexibleSearch.search(" select {PK} from {ServicelayerJob} where {CODE} = ?code ", parameters, ServicelayerJob.class).getResult();
        if(result == null || result.isEmpty())
        {
            throw new FlexibleSearchException("Job definition wasn't found");
        }
        if(result.size() != 1)
        {
            throw new FlexibleSearchException("Code solrIndexerJob is not unambiguous.");
        }
        return result.get(0);
    }
}
