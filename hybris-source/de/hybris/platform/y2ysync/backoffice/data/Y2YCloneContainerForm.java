package de.hybris.platform.y2ysync.backoffice.data;

import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;

public class Y2YCloneContainerForm
{
    private Y2YStreamConfigurationContainerModel sourceContainer;
    private String targetContainerId;
    private Y2YStreamConfigurationContainerModel targetContainer;


    public Y2YStreamConfigurationContainerModel getSourceContainer()
    {
        return this.sourceContainer;
    }


    public void setSourceContainer(Y2YStreamConfigurationContainerModel sourceContainer)
    {
        this.sourceContainer = sourceContainer;
    }


    public String getTargetContainerId()
    {
        return this.targetContainerId;
    }


    public void setTargetContainerId(String targetContainerId)
    {
        this.targetContainerId = targetContainerId;
    }


    public Y2YStreamConfigurationContainerModel getTargetContainer()
    {
        return this.targetContainer;
    }


    public void setTargetContainer(Y2YStreamConfigurationContainerModel targetContainer)
    {
        this.targetContainer = targetContainer;
    }
}
