package de.hybris.platform.warehousing.taskassignment.services;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;

public interface WarehousingConsignmentWorkflowService
{
    void startConsignmentWorkflow(ConsignmentModel paramConsignmentModel);


    void terminateConsignmentWorkflow(ConsignmentModel paramConsignmentModel);


    void decideWorkflowAction(ConsignmentModel paramConsignmentModel, String paramString1, String paramString2);


    WorkflowActionModel getWorkflowActionForTemplateCode(String paramString, ConsignmentModel paramConsignmentModel);
}
