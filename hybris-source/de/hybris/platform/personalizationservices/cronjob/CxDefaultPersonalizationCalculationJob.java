package de.hybris.platform.personalizationservices.cronjob;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.personalizationservices.model.process.CxDefaultPersonalizationCalculationCronJobModel;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.site.impl.DefaultBaseSiteService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CxDefaultPersonalizationCalculationJob extends AbstractJobPerformable<CxDefaultPersonalizationCalculationCronJobModel>
{
    private static final Logger LOG = Logger.getLogger(CxDefaultPersonalizationCalculationJob.class.getName());
    private CxService cxService;
    private BaseSiteService baseSiteService;
    private CxCatalogService cxCatalogService;


    public PerformResult perform(CxDefaultPersonalizationCalculationCronJobModel job)
    {
        if(CollectionUtils.isEmpty(job.getBaseSites()))
        {
            LOG.warn("There is no sites defined for " + job.getCode());
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
        Collection<CatalogVersionModel> personalizedActiveCatalogVersions = getPersonalizedActiveCatalogVersions(job
                        .getBaseSites());
        if(CollectionUtils.isEmpty(personalizedActiveCatalogVersions))
        {
            LOG.warn("There is no active personalized catalog versions for job " + job.getCode());
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
        getCxService().calculateAndStoreDefaultPersonalization(personalizedActiveCatalogVersions);
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected Collection<CatalogVersionModel> getPersonalizedActiveCatalogVersions(Set<BaseSiteModel> baseSites)
    {
        Collection<CatalogVersionModel> personalizedActiveCatalogVersions = new ArrayList<>();
        baseSites.forEach(baseSite -> {
            this.baseSiteService.setCurrentBaseSite(baseSite, true);
            personalizedActiveCatalogVersions.addAll(this.cxCatalogService.getConfiguredCatalogVersions());
        });
        return personalizedActiveCatalogVersions;
    }


    @Required
    public void setBaseSiteService(DefaultBaseSiteService baseSiteService)
    {
        this.baseSiteService = (BaseSiteService)baseSiteService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setCxService(CxService cxService)
    {
        this.cxService = cxService;
    }


    protected CxService getCxService()
    {
        return this.cxService;
    }


    @Required
    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }
}
