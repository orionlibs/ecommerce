package de.hybris.platform.solrfacetsearch.model.reporting;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Date;

public class SolrQueryAggregatedStatsModel extends ItemModel
{
    public static final String _TYPECODE = "SolrQueryAggregatedStats";
    public static final String TIME = "time";
    public static final String INDEXCONFIG = "indexConfig";
    public static final String LANGUAGE = "language";
    public static final String QUERY = "query";
    public static final String COUNT = "count";
    public static final String AVGNUMBEROFRESULTS = "avgNumberOfResults";


    public SolrQueryAggregatedStatsModel()
    {
    }


    public SolrQueryAggregatedStatsModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrQueryAggregatedStatsModel(double _avgNumberOfResults, long _count, SolrFacetSearchConfigModel _indexConfig, LanguageModel _language, String _query, Date _time)
    {
        setAvgNumberOfResults(_avgNumberOfResults);
        setCount(_count);
        setIndexConfig(_indexConfig);
        setLanguage(_language);
        setQuery(_query);
        setTime(_time);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrQueryAggregatedStatsModel(double _avgNumberOfResults, long _count, SolrFacetSearchConfigModel _indexConfig, LanguageModel _language, ItemModel _owner, String _query, Date _time)
    {
        setAvgNumberOfResults(_avgNumberOfResults);
        setCount(_count);
        setIndexConfig(_indexConfig);
        setLanguage(_language);
        setOwner(_owner);
        setQuery(_query);
        setTime(_time);
    }


    @Accessor(qualifier = "avgNumberOfResults", type = Accessor.Type.GETTER)
    public double getAvgNumberOfResults()
    {
        return toPrimitive((Double)getPersistenceContext().getPropertyValue("avgNumberOfResults"));
    }


    @Accessor(qualifier = "count", type = Accessor.Type.GETTER)
    public long getCount()
    {
        return toPrimitive((Long)getPersistenceContext().getPropertyValue("count"));
    }


    @Accessor(qualifier = "indexConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getIndexConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("indexConfig");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "time", type = Accessor.Type.GETTER)
    public Date getTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("time");
    }


    @Accessor(qualifier = "avgNumberOfResults", type = Accessor.Type.SETTER)
    public void setAvgNumberOfResults(double value)
    {
        getPersistenceContext().setPropertyValue("avgNumberOfResults", toObject(value));
    }


    @Accessor(qualifier = "count", type = Accessor.Type.SETTER)
    public void setCount(long value)
    {
        getPersistenceContext().setPropertyValue("count", toObject(value));
    }


    @Accessor(qualifier = "indexConfig", type = Accessor.Type.SETTER)
    public void setIndexConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("indexConfig", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "time", type = Accessor.Type.SETTER)
    public void setTime(Date value)
    {
        getPersistenceContext().setPropertyValue("time", value);
    }
}
