package de.hybris.platform.patches.strategies.impl;

import de.hybris.platform.patches.actions.data.PatchActionData;
import de.hybris.platform.patches.enums.ExecutionStatus;
import de.hybris.platform.patches.enums.ExecutionUnitType;
import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.patches.model.PatchExecutionUnitModel;
import de.hybris.platform.patches.service.PatchExecutionService;
import de.hybris.platform.patches.strategies.PatchExecutionUnitTrackingStrategy;
import de.hybris.platform.patches.utils.PatchExecutionUnitUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPatchExecutionUnitTrackingStrategy implements PatchExecutionUnitTrackingStrategy
{
    protected TimeService timeService;
    protected ModelService modelService;
    protected PatchExecutionService patchExecutionService;
    protected ExecutionUnitType executionUnitType;


    public PatchExecutionUnitModel trackBeforePerform(PatchActionData patchActionData)
    {
        PatchExecutionModel patchExecution = this.patchExecutionService.getSessionPatchExecution();
        PatchExecutionUnitModel patchExecutionUnit = new PatchExecutionUnitModel();
        String name = patchActionData.getName();
        String idHash = PatchExecutionUnitUtils.generateHashId(patchActionData.getName(), patchActionData
                        .getOrganisationUnit(), patchExecution);
        patchExecutionUnit.setPatch(patchExecution);
        patchExecutionUnit.setName(name);
        patchExecutionUnit.setIdHash(idHash);
        patchExecutionUnit.setOrderNumber(Integer.valueOf(PatchExecutionUnitUtils.getNextUnitNumber(patchExecution)));
        patchExecutionUnit
                        .setOrganisation(PatchExecutionUnitUtils.generateOrganisationUnitName(patchActionData.getOrganisationUnit()));
        patchExecutionUnit.setExecutionType(this.executionUnitType);
        this.modelService.save(patchExecutionUnit);
        return patchExecutionUnit;
    }


    public void trackAfterPerform(PatchExecutionUnitModel patchExecutionUnit, PatchActionData patchActionData, Throwable ex)
    {
        if(patchExecutionUnit != null)
        {
            if(ex != null)
            {
                patchExecutionUnit.setErrorLog(ex.getMessage());
                patchExecutionUnit.setExecutionStatus(ExecutionStatus.ERROR);
            }
            else
            {
                patchExecutionUnit.setExecutionStatus(ExecutionStatus.SUCCESS);
            }
            patchExecutionUnit.setExecutionTime(this.timeService.getCurrentTime());
            this.modelService.save(patchExecutionUnit);
        }
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
    public void setPatchExecutionService(PatchExecutionService patchExecutionService)
    {
        this.patchExecutionService = patchExecutionService;
    }


    @Required
    public void setExecutionUnitType(ExecutionUnitType executionUnitType)
    {
        this.executionUnitType = executionUnitType;
    }
}
