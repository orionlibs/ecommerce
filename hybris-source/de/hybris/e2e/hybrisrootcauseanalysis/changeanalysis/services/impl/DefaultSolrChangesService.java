package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.utils.E2EUtils;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrEndpointUrlModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrStopWordModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSynonymConfigModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import java.util.Collection;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrChangesService implements E2EChangesPropertiesService
{
    private static final Logger LOG = Logger.getLogger(DefaultSolrChangesService.class.getName());
    private static final String PREFIX_SOLRFACETSEARCH = "solrfacetsearchconfig";
    private boolean sorted;
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    private String nameFile;


    public Properties getInfo()
    {
        Properties properties = new Properties();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Getting info from SolrFacetSearchConfigDao");
        }
        Collection<SolrFacetSearchConfigModel> facetSearchConfigs = this.solrFacetSearchConfigDao.findAllFacetSearchConfigs();
        if(!facetSearchConfigs.isEmpty())
        {
            for(SolrFacetSearchConfigModel model : facetSearchConfigs)
            {
                String name = model.getName();
                if(name == null)
                {
                    LOG.error("The name field cannot be empty");
                    continue;
                }
                saveToProperties(model, properties, name);
            }
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug("The search has not found any results");
        }
        if(isSorted())
        {
            properties = E2EUtils.getSortedProperties(properties);
        }
        return properties;
    }


    public void saveToProperties(SolrFacetSearchConfigModel model, Properties prop, String name)
    {
        int index = 0;
        prop.put("solrfacetsearchconfig." + name + ".name", model.getName());
        prop.put("solrfacetsearchconfig." + name + ".description",
                        E2EUtils.isNull(model.getDescription()));
        for(CurrencyModel currency : model.getCurrencies())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".name." + index,
                            E2EUtils.isNull(currency.getName()));
        }
        index = 0;
        for(CatalogVersionModel catalogVersion : model.getCatalogVersions())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".id." + index,
                            E2EUtils.isNull(catalogVersion.getCatalog().getId()));
        }
        SolrIndexConfigModel solrIndexConfig = model.getSolrIndexConfig();
        prop.put("solrfacetsearchconfig." + name + ".name",
                        E2EUtils.isNull(solrIndexConfig.getName()));
        index = 0;
        for(SolrIndexedTypeModel solrIndexedTypes : model.getSolrIndexedTypes())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".indexName." + index,
                            E2EUtils.isNull(solrIndexedTypes.getIndexName()));
        }
        SolrServerConfigModel solrServerConfig = model.getSolrServerConfig();
        prop.put("solrfacetsearchconfig." + name + ".aliveCheckInterval",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.getAliveCheckInterval())));
        prop.put("solrfacetsearchconfig." + name + ".connectionTimeout",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.getConnectionTimeout())));
        prop.put("solrfacetsearchconfig." + name + ".embeddedMaster",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.isEmbeddedMaster())));
        prop.put("solrfacetsearchconfig." + name + ".maxTotalConnections",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.getMaxTotalConnections())));
        prop.put("solrfacetsearchconfig." + name + ".maxTotalConnectionsPerHostConfig",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.getMaxTotalConnectionsPerHostConfig())));
        prop.put("solrfacetsearchconfig." + name + ".mode.code",
                        E2EUtils.isNull(solrServerConfig.getMode().getCode()));
        prop.put("solrfacetsearchconfig." + name + ".mode.name",
                        E2EUtils.isNull(solrServerConfig.getMode().name().toString()));
        prop.put("solrfacetsearchconfig." + name + ".name",
                        E2EUtils.isNull(solrServerConfig.getName()));
        index = 0;
        for(SolrEndpointUrlModel solrEndpointUrl : solrServerConfig.getSolrEndpointUrls())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".solrEndpointUrls.url" + index,
                            E2EUtils.isNull(solrEndpointUrl.getUrl()));
        }
        prop.put("solrfacetsearchconfig." + name + ".socketTimeout",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.getSocketTimeout())));
        prop.put("solrfacetsearchconfig." + name + ".tcpNoDelay",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.isTcpNoDelay())));
        prop.put("solrfacetsearchconfig." + name + ".useMasterNodeExclusivelyForIndexing",
                        E2EUtils.isNull(String.valueOf(solrServerConfig.isUseMasterNodeExclusivelyForIndexing())));
        index = 0;
        for(LanguageModel language : model.getLanguages())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".name." + index,
                            E2EUtils.isNull(language.getName()));
        }
        index = 0;
        for(SolrStopWordModel solrStopWord : model.getStopWords())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".StopWord." + index,
                            E2EUtils.isNull(solrStopWord.getStopWord()));
        }
        index = 0;
        for(SolrSynonymConfigModel synonym : model.getSynonyms())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".synonymFrom." + index,
                            E2EUtils.isNull(synonym.getSynonymFrom()));
            prop.put("solrfacetsearchconfig." + name + ".synonymTo." + index,
                            E2EUtils.isNull(synonym.getSynonymTo()));
        }
        index = 0;
        for(SolrFacetSearchKeywordRedirectModel keyword : model.getKeywordRedirects())
        {
            index++;
            prop.put("solrfacetsearchconfig." + name + ".keyword." + index,
                            E2EUtils.isNull(keyword.getKeyword()));
            prop.put("solrfacetsearchconfig." + name + ".ignoreCase." + index,
                            E2EUtils.isNull(String.valueOf(keyword.getIgnoreCase())));
        }
    }


    public String getNameFile()
    {
        return this.nameFile;
    }


    public boolean isSorted()
    {
        return this.sorted;
    }


    public void setSorted(boolean sorted)
    {
        this.sorted = sorted;
    }


    @Required
    public void setSolrFacetSearchConfigDao(SolrFacetSearchConfigDao solrFacetSearchConfigDao)
    {
        this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
    }


    public void setNameFile(String nameFile)
    {
        this.nameFile = nameFile;
    }
}
