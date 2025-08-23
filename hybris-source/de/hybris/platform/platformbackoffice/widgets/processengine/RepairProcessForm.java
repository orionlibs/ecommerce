package de.hybris.platform.platformbackoffice.widgets.processengine;

import de.hybris.platform.processengine.model.BusinessProcessModel;

public class RepairProcessForm
{
    private BusinessProcessModel businessProcess;
    private String targetStep;
    private boolean successful = true;


    public BusinessProcessModel getBusinessProcess()
    {
        return this.businessProcess;
    }


    public void setBusinessProcess(BusinessProcessModel businessProcess)
    {
        this.businessProcess = businessProcess;
    }


    public String getTargetStep()
    {
        return this.targetStep;
    }


    public void setTargetStep(String targetStep)
    {
        this.targetStep = targetStep;
    }


    public boolean isSuccessful()
    {
        return this.successful;
    }


    public void setSuccessful(boolean successful)
    {
        this.successful = successful;
    }
}
