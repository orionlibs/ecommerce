package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SiteMapMediaCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SiteMapMediaCronJob";
    public static final String CONTENTSITE = "contentSite";
    public static final String SITEMAPURLLIMITPERFILE = "siteMapUrlLimitPerFile";


    public SiteMapMediaCronJobModel()
    {
    }


    public SiteMapMediaCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SiteMapMediaCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SiteMapMediaCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "contentSite", type = Accessor.Type.GETTER)
    public CMSSiteModel getContentSite()
    {
        return (CMSSiteModel)getPersistenceContext().getPropertyValue("contentSite");
    }


    @Accessor(qualifier = "siteMapUrlLimitPerFile", type = Accessor.Type.GETTER)
    public Integer getSiteMapUrlLimitPerFile()
    {
        return (Integer)getPersistenceContext().getPropertyValue("siteMapUrlLimitPerFile");
    }


    @Accessor(qualifier = "contentSite", type = Accessor.Type.SETTER)
    public void setContentSite(CMSSiteModel value)
    {
        getPersistenceContext().setPropertyValue("contentSite", value);
    }


    @Accessor(qualifier = "siteMapUrlLimitPerFile", type = Accessor.Type.SETTER)
    public void setSiteMapUrlLimitPerFile(Integer value)
    {
        getPersistenceContext().setPropertyValue("siteMapUrlLimitPerFile", value);
    }
}
