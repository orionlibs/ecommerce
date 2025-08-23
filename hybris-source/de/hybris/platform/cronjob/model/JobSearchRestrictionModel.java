package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class JobSearchRestrictionModel extends ItemModel
{
    public static final String _TYPECODE = "JobSearchRestriction";
    public static final String _JOBSEARCHRESTRICTIONRELATION = "JobSearchRestrictionRelation";
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String QUERY = "query";
    public static final String JOBPOS = "jobPOS";
    public static final String JOB = "job";


    public JobSearchRestrictionModel()
    {
    }


    public JobSearchRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JobSearchRestrictionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public JobModel getJob()
    {
        return (JobModel)getPersistenceContext().getPropertyValue("job");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public ComposedTypeModel getType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        getPersistenceContext().setPropertyValue("job", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }
}
