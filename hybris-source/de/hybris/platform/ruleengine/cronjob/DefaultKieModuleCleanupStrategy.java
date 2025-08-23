package de.hybris.platform.ruleengine.cronjob;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleMediaModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultKieModuleCleanupStrategy implements MaintenanceCleanupStrategy<DroolsKIEModuleModel, CronJobModel>
{
    public static final String LAST_VERSIONS_OF_KIE_MODULES_TO_KEEP = "kieModuleCleanupStrategy.lastVersionsOfKieModulesToKeep";
    private static final int DEFAULT_LAST_VERSIONS_OF_KIE_MODULES_TO_KEEP = 3;
    private static final String FIND_ALL_MODULES = "select {pk} from {DroolsKIEModule}";
    private static final String FIND_MODULE_MEDIA = "select {pk} from {DroolsKIEModuleMedia} where {kieModuleName} = ?name";
    private ModelService modelService;
    private MediaService mediaService;
    private ConfigurationService configurationService;
    private FlexibleSearchService flexibleSearchService;


    public FlexibleSearchQuery createFetchQuery(CronJobModel cronJob)
    {
        if(!(cronJob.getJob() instanceof de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel))
        {
            throw new IllegalStateException("The job was not a MaintenanceCleanupJob");
        }
        return new FlexibleSearchQuery("select {pk} from {DroolsKIEModule}");
    }


    public void process(List<DroolsKIEModuleModel> elements)
    {
        for(DroolsKIEModuleModel module : elements)
        {
            FlexibleSearchQuery fsq = new FlexibleSearchQuery("select {pk} from {DroolsKIEModuleMedia} where {kieModuleName} = ?name");
            fsq.addQueryParameter("name", module.getName());
            List<DroolsKIEModuleMediaModel> medias = new ArrayList<>(getFlexibleSearchService().search(fsq).getResult());
            if(medias.size() > getNumberOfLastVersionsOfKieModuleToKeep())
            {
                medias.sort(Comparator.comparingInt(o -> Integer.valueOf(o.getReleaseId().replaceAll("\\D", "")).intValue()));
                List<DroolsKIEModuleMediaModel> mediaToRemove = medias.subList(0, medias.size() - getNumberOfLastVersionsOfKieModuleToKeep());
                mediaToRemove.forEach(media -> getMediaService().removeDataFromMediaQuietly((MediaModel)media));
                getModelService().removeAll(mediaToRemove);
            }
        }
    }


    protected int getNumberOfLastVersionsOfKieModuleToKeep()
    {
        return getConfigurationService().getConfiguration().getInt("kieModuleCleanupStrategy.lastVersionsOfKieModulesToKeep", 3);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
