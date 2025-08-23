package de.hybris.platform.solrfacetsearch.reporting.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.model.reporting.SolrQueryAggregatedStatsModel;
import de.hybris.platform.solrfacetsearch.reporting.AggregatedQueryStatsService;
import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import java.util.ArrayList;
import java.util.List;

public class DefaultAggregatedQueryStatsService implements AggregatedQueryStatsService
{
    private Converter<AggregatedSearchQueryInfo, SolrQueryAggregatedStatsModel> converter;
    private ModelService modelService;


    public void save(List<AggregatedSearchQueryInfo> aggregatedStatistics)
    {
        List<SolrQueryAggregatedStatsModel> aggregatedStatisticsModels = convertPojosToModels(aggregatedStatistics);
        this.modelService.saveAll(aggregatedStatisticsModels);
    }


    protected List<SolrQueryAggregatedStatsModel> convertPojosToModels(List<AggregatedSearchQueryInfo> allStatistics)
    {
        List<SolrQueryAggregatedStatsModel> res = new ArrayList<>();
        for(AggregatedSearchQueryInfo result : allStatistics)
        {
            SolrQueryAggregatedStatsModel model = (SolrQueryAggregatedStatsModel)this.converter.convert(result);
            res.add(model);
        }
        return res;
    }


    public void setConverter(Converter<AggregatedSearchQueryInfo, SolrQueryAggregatedStatsModel> converter)
    {
        this.converter = converter;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
