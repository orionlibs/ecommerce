package de.hybris.platform.patches.service.impl;

import de.hybris.platform.patches.Patch;
import de.hybris.platform.patches.enums.ExecutionStatus;
import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.patches.model.PatchExecutionUnitModel;
import de.hybris.platform.patches.service.PatchExecutionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPatchExecutionService implements PatchExecutionService
{
    private static final String PATCH_EXECUTION_SESSION_KEY = "patchExecution";
    private static final String JENKINS_BUILD_KEY = "patches.jenkinsBuild";
    private static final String GIT_HASH_CODE_KEY = "patches.gitHashCode";
    private TimeService timeService;
    private ModelService modelService;
    private SessionService sessionService;
    private ConfigurationService configurationService;
    private DefaultGenericDao<PatchExecutionModel> patchExecutionDao;


    public List<PatchExecutionModel> getExecutedPatches()
    {
        return this.patchExecutionDao.find();
    }


    public PatchExecutionModel getPatchExecutionById(String patchId)
    {
        return getPatchExecutionByIdWithSortParameters(patchId, null);
    }


    public PatchExecutionModel getLatestPatchExecutionById(String patchId)
    {
        return getPatchExecutionByIdWithSortParameters(patchId,
                        SortParameters.singletonDescending("executionTime"));
    }


    public void registerPatchExecution(PatchExecutionModel patch)
    {
        this.modelService.refresh(patch);
        patch.setExecutionStatus(computeExecutionStatus(patch));
        patch.setExecutionTime(this.timeService.getCurrentTime());
        this.modelService.save(patch);
    }


    public void registerPatchExecutionError(String log)
    {
        PatchExecutionModel patch = getSessionPatchExecution();
        this.modelService.refresh(patch);
        patch.setExecutionStatus(ExecutionStatus.ERROR);
        patch.setErrorLog(log);
        patch.setExecutionTime(this.timeService.getCurrentTime());
        this.modelService.save(patch);
    }


    public PatchExecutionModel createPatchExecution(Patch patch)
    {
        PatchExecutionModel patchModel = (PatchExecutionModel)this.modelService.create(PatchExecutionModel.class);
        patchModel.setPatchId(patch.getPatchId());
        patchModel.setPatchName(patch.getPatchName());
        patchModel.setPatchDescription(patch.getPatchDescription());
        patchModel.setExecutionTime(this.timeService.getCurrentTime());
        patchModel.setGitHashCode(this.configurationService.getConfiguration().getString("patches.gitHashCode", ""));
        patchModel.setJenkinsBuild(this.configurationService.getConfiguration().getString("patches.jenkinsBuild", ""));
        if(patch instanceof de.hybris.platform.patches.Rerunnable)
        {
            patchModel.setRerunnable(true);
        }
        PatchExecutionModel prevPatch = getLatestPatchExecutionById(patch.getPatchId());
        if(prevPatch != null)
        {
            patchModel.setPreviousExecution(prevPatch);
            prevPatch.setNextExecution(patchModel);
            this.modelService.save(prevPatch);
        }
        this.modelService.save(patchModel);
        setPatchExecutionInJaloSession(patchModel);
        return patchModel;
    }


    public PatchExecutionModel getSessionPatchExecution()
    {
        return (PatchExecutionModel)this.sessionService.getAttribute("patchExecution");
    }


    private ExecutionStatus computeExecutionStatus(PatchExecutionModel patch)
    {
        for(PatchExecutionUnitModel patchExecutionUnit : patch.getPatchUnits())
        {
            if(ExecutionStatus.ERROR.equals(patchExecutionUnit.getExecutionStatus()))
            {
                return ExecutionStatus.ERROR;
            }
        }
        return ExecutionStatus.SUCCESS;
    }


    private PatchExecutionModel getPatchExecutionByIdWithSortParameters(String patchId, SortParameters sortParameters)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("patchId", patchId);
        Collection<PatchExecutionModel> result = this.patchExecutionDao.find(attributes, sortParameters);
        if(result.isEmpty())
        {
            return null;
        }
        return result.iterator().next();
    }


    private void setPatchExecutionInJaloSession(PatchExecutionModel patchExecution)
    {
        this.sessionService.setAttribute("patchExecution", patchExecution);
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setPatchExecutionDao(DefaultGenericDao<PatchExecutionModel> patchExecutionDao)
    {
        this.patchExecutionDao = patchExecutionDao;
    }
}
