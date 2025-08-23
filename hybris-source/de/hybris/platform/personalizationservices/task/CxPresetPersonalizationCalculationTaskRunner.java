package de.hybris.platform.personalizationservices.task;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CxPresetPersonalizationCalculationTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CxRegisteredUserSessionCloseTaskRunner.class);
    private CxService cxService;


    public void run(TaskService taskService, TaskModel task)
    {
        Map<String, Object> contextMap = (Map<String, Object>)task.getContext();
        Collection<CatalogVersionModel> catalogVersions = (Collection<CatalogVersionModel>)contextMap.get("catalogVersions");
        getCxService().calculateAndStoreDefaultPersonalization(catalogVersions);
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
        LOG.error("Calculating default personalization failed ", error);
    }


    protected CxService getCxService()
    {
        return this.cxService;
    }


    @Required
    public void setCxService(CxService cxService)
    {
        this.cxService = cxService;
    }
}
