package de.hybris.platform.solrfacetsearch.reporting.data.converters;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.reporting.SolrQueryAggregatedStatsModel;
import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;

public class AggregatedStatisticsConverter implements Converter<AggregatedSearchQueryInfo, SolrQueryAggregatedStatsModel>
{
    private ModelService modelService;
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    private CommonI18NService i18nService;


    public SolrQueryAggregatedStatsModel convert(AggregatedSearchQueryInfo result)
    {
        SolrQueryAggregatedStatsModel model = (SolrQueryAggregatedStatsModel)this.modelService.create(SolrQueryAggregatedStatsModel.class);
        SolrFacetSearchConfigModel indexConfig = this.solrFacetSearchConfigDao.findFacetSearchConfigByName(result.getIndexName());
        LanguageModel lang = this.i18nService.getLanguage(result.getLanguage());
        model.setIndexConfig(indexConfig);
        model.setLanguage(lang);
        model.setTime(result.getDate());
        model.setCount(result.getCount());
        model.setAvgNumberOfResults(result.getAverageNumberOfResults());
        model.setQuery(result.getQuery());
        return model;
    }


    public SolrQueryAggregatedStatsModel convert(AggregatedSearchQueryInfo source, SolrQueryAggregatedStatsModel prototype)
    {
        throw new UnsupportedOperationException();
    }


    public void setSolrFacetSearchConfigDao(SolrFacetSearchConfigDao solrFacetSearchConfigDao)
    {
        this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setI18nService(CommonI18NService i18nService)
    {
        this.i18nService = i18nService;
    }
}
