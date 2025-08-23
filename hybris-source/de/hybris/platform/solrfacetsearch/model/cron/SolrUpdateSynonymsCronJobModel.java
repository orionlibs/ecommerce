package de.hybris.platform.solrfacetsearch.model.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class SolrUpdateSynonymsCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SolrUpdateSynonymsCronJob";
    public static final String _SOLRFACETSEARCHCONFIG2SOLRUPDATESYNONYMSCRONJOB = "SolrFacetSearchConfig2SolrUpdateSynonymsCronJob";
    public static final String LANGUAGE = "language";
    public static final String SOLRFACETSEARCHCONFIGPOS = "solrFacetSearchConfigPOS";
    public static final String SOLRFACETSEARCHCONFIG = "solrFacetSearchConfig";


    public SolrUpdateSynonymsCronJobModel()
    {
    }


    public SolrUpdateSynonymsCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrUpdateSynonymsCronJobModel(JobModel _job, SolrFacetSearchConfigModel _solrFacetSearchConfig)
    {
        setJob(_job);
        setSolrFacetSearchConfig(_solrFacetSearchConfig);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrUpdateSynonymsCronJobModel(JobModel _job, ItemModel _owner, SolrFacetSearchConfigModel _solrFacetSearchConfig)
    {
        setJob(_job);
        setOwner(_owner);
        setSolrFacetSearchConfig(_solrFacetSearchConfig);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "solrFacetSearchConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getSolrFacetSearchConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("solrFacetSearchConfig");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "solrFacetSearchConfig", type = Accessor.Type.SETTER)
    public void setSolrFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrFacetSearchConfig", value);
    }
}
