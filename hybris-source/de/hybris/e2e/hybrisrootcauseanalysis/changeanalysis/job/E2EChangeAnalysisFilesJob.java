package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.job;

import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesPropertiesService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EChangesXmlService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EPropertiesFileService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EXmlFileService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

public class E2EChangeAnalysisFilesJob implements JobPerformable<CronJobModel>
{
    private E2EPropertiesFileService propertiesFileService;
    private E2EXmlFileService xmlFileService;
    private List<E2EChangesPropertiesService> changesPropertiesList;
    private List<E2EChangesXmlService> changesXmlList;
    private static final Logger LOG = Logger.getLogger(E2EChangeAnalysisFilesJob.class.getName());


    public boolean isAbortable()
    {
        return true;
    }


    public boolean isPerformable()
    {
        return true;
    }


    public PerformResult perform(CronJobModel model)
    {
        LOG.info("****** Running Job ******");
        for(E2EChangesPropertiesService changes : this.changesPropertiesList)
        {
            Properties prop = changes.getInfo();
            String nameFile = changes.getNameFile();
            this.propertiesFileService.writeFile(prop, nameFile);
        }
        for(E2EChangesXmlService changes : this.changesXmlList)
        {
            String code = changes.getCode();
            String nameFile = changes.getNameFile();
            MediaModel media = changes.getMedia(code);
            this.xmlFileService.copyToE2Efolder(media, nameFile);
        }
        LOG.info("** Job has run successfully **");
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    public void setPropertiesFileService(E2EPropertiesFileService propertiesFileService)
    {
        this.propertiesFileService = propertiesFileService;
    }


    public void setXmlFileService(E2EXmlFileService xmlFileService)
    {
        this.xmlFileService = xmlFileService;
    }


    public void setChangesPropertiesList(List<E2EChangesPropertiesService> changesPropertiesList)
    {
        this.changesPropertiesList = changesPropertiesList;
    }


    public void setChangesXmlList(List<E2EChangesXmlService> changesXmlList)
    {
        this.changesXmlList = changesXmlList;
    }
}
