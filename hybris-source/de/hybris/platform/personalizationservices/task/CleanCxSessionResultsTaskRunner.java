package de.hybris.platform.personalizationservices.task;

import de.hybris.platform.personalizationservices.action.dao.CxActionResultDao;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CleanCxSessionResultsTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = Logger.getLogger(CleanCxSessionResultsTaskRunner.class);
    private ModelService modelService;
    private CxActionResultDao actionResultDao;


    public void run(TaskService taskService, TaskModel task)
    {
        String sessionKey = (String)task.getContext();
        List<CxResultsModel> resultList = this.actionResultDao.findResultsBySessionKey(sessionKey);
        this.modelService.removeAll(resultList);
        LOG.debug("Removed personalization results : " + resultList.size());
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
        LOG.error("Cleaninig personalization results failed ", error);
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


    protected CxActionResultDao getActionResultDao()
    {
        return this.actionResultDao;
    }


    @Required
    public void setActionResultDao(CxActionResultDao actionResultDao)
    {
        this.actionResultDao = actionResultDao;
    }
}
