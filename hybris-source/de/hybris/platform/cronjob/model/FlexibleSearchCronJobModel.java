package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class FlexibleSearchCronJobModel extends MediaProcessCronJobModel
{
    public static final String _TYPECODE = "FlexibleSearchCronJob";
    public static final String QUERY = "query";
    public static final String FAILONUNKNOWN = "failOnUnknown";
    public static final String DONTNEEDTOTAL = "dontNeedTotal";
    public static final String RANGESTART = "rangeStart";
    public static final String COUNT = "count";
    public static final String SEARCHRESULT = "searchResult";


    public FlexibleSearchCronJobModel()
    {
    }


    public FlexibleSearchCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlexibleSearchCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FlexibleSearchCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "count", type = Accessor.Type.GETTER)
    public Integer getCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("count");
    }


    @Accessor(qualifier = "dontNeedTotal", type = Accessor.Type.GETTER)
    public Boolean getDontNeedTotal()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("dontNeedTotal");
    }


    @Accessor(qualifier = "failOnUnknown", type = Accessor.Type.GETTER)
    public Boolean getFailOnUnknown()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("failOnUnknown");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "rangeStart", type = Accessor.Type.GETTER)
    public Integer getRangeStart()
    {
        return (Integer)getPersistenceContext().getPropertyValue("rangeStart");
    }


    @Accessor(qualifier = "searchResult", type = Accessor.Type.GETTER)
    public Collection<String> getSearchResult()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("searchResult");
    }


    @Accessor(qualifier = "count", type = Accessor.Type.SETTER)
    public void setCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("count", value);
    }


    @Accessor(qualifier = "dontNeedTotal", type = Accessor.Type.SETTER)
    public void setDontNeedTotal(Boolean value)
    {
        getPersistenceContext().setPropertyValue("dontNeedTotal", value);
    }


    @Accessor(qualifier = "failOnUnknown", type = Accessor.Type.SETTER)
    public void setFailOnUnknown(Boolean value)
    {
        getPersistenceContext().setPropertyValue("failOnUnknown", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "rangeStart", type = Accessor.Type.SETTER)
    public void setRangeStart(Integer value)
    {
        getPersistenceContext().setPropertyValue("rangeStart", value);
    }
}
