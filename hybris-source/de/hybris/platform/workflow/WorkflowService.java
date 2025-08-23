package de.hybris.platform.workflow;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public interface WorkflowService
{
    List<WorkflowModel> getWorkflowsForTemplateAndUser(WorkflowTemplateModel paramWorkflowTemplateModel, UserModel paramUserModel);


    List<WorkflowModel> getAllAdhocWorkflows(EnumSet<WorkflowStatus> paramEnumSet, Date paramDate1, Date paramDate2);


    List<WorkflowModel> getAllWorkflows(EnumSet<WorkflowStatus> paramEnumSet, Date paramDate1, Date paramDate2);


    WorkflowModel createWorkflow(WorkflowTemplateModel paramWorkflowTemplateModel, ItemModel paramItemModel, UserModel paramUserModel);


    WorkflowModel createWorkflow(String paramString, WorkflowTemplateModel paramWorkflowTemplateModel, List<ItemModel> paramList, UserModel paramUserModel);


    WorkflowModel createWorkflow(WorkflowTemplateModel paramWorkflowTemplateModel, UserModel paramUserModel);


    boolean isPlanned(WorkflowModel paramWorkflowModel);


    boolean isRunning(WorkflowModel paramWorkflowModel);


    boolean isPaused(WorkflowModel paramWorkflowModel);


    boolean isFinished(WorkflowModel paramWorkflowModel);


    boolean isCompleted(WorkflowModel paramWorkflowModel);


    boolean isTerminated(WorkflowModel paramWorkflowModel);


    boolean canBeStarted(WorkflowModel paramWorkflowModel);


    boolean isAdhocWorkflow(WorkflowModel paramWorkflowModel);


    boolean assignUser(PrincipalModel paramPrincipalModel, WorkflowModel paramWorkflowModel);


    boolean unassignUser(WorkflowModel paramWorkflowModel);


    WorkflowModel createAdhocWorkflow(String paramString, List<ItemModel> paramList, UserModel paramUserModel);


    Date getStartTime(WorkflowModel paramWorkflowModel);


    WorkflowModel getWorkflowForCode(String paramString);


    SearchResult<WorkflowModel> getAllWorkflows(EnumSet<WorkflowStatus> paramEnumSet, Date paramDate1, Date paramDate2, int paramInt1, int paramInt2);


    SearchResult<WorkflowModel> getAllAdhocWorkflows(EnumSet<WorkflowStatus> paramEnumSet, Date paramDate1, Date paramDate2, int paramInt1, int paramInt2);
}
