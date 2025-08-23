package de.hybris.platform.workflow.services;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionCommentModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Deprecated(since = "ages", forRemoval = true)
public interface WorkflowService
{
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_REMOVE = "remove";
    public static final String ACTION_SAVE = "save";


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachment(ComposedType paramComposedType);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> paramList);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getAllUserWorkflowActionsWithAttachments(List<String> paramList, Collection<WorkflowActionStatus> paramCollection);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionCommentModel> getCommentsForAction(WorkflowActionModel paramWorkflowActionModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<ItemModel> getAttachmentsForAction(WorkflowActionModel paramWorkflowActionModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<ItemModel> getAttachmentsForAction(WorkflowActionModel paramWorkflowActionModel, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    List<ItemModel> getAttachmentsForAction(WorkflowActionModel paramWorkflowActionModel, List<String> paramList);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowDecisionModel> getDecisionsForAction(WorkflowActionModel paramWorkflowActionModel);


    @Deprecated(since = "ages", forRemoval = true)
    void decideAction(WorkflowActionModel paramWorkflowActionModel, WorkflowDecisionModel paramWorkflowDecisionModel);


    @Deprecated(since = "ages", forRemoval = true)
    void addComment(Object paramObject, WorkflowActionModel paramWorkflowActionModel);


    @Deprecated(since = "ages", forRemoval = true)
    void evaluteActivationScripts(ItemModel paramItemModel, Map paramMap1, Map paramMap2, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    WorkflowModel createWorkflow(WorkflowTemplateModel paramWorkflowTemplateModel, ItemModel paramItemModel);


    @Deprecated(since = "ages", forRemoval = true)
    WorkflowModel createWorkflow(String paramString, WorkflowTemplateModel paramWorkflowTemplateModel, List<ItemModel> paramList);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isAutomatedComment(WorkflowActionCommentModel paramWorkflowActionCommentModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowTemplateModel> getAllWorkflowTemplates();


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowModel> getWorkflowsByTemplate(WorkflowTemplateModel paramWorkflowTemplateModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowModel> getAllWorkflows(int paramInt1, int paramInt2, Date paramDate1, Date paramDate2, Date paramDate3, Date paramDate4);


    @Deprecated(since = "ages", forRemoval = true)
    WorkflowModel createAdhocWorkflow(String paramString, List<ItemModel> paramList);


    @Deprecated(since = "ages", forRemoval = true)
    void assignUser(PrincipalModel paramPrincipalModel, WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowTemplateModel> getAllVisibleWorkflowTemplates();


    @Deprecated(since = "ages", forRemoval = true)
    boolean isPaused(Object paramObject);


    @Deprecated(since = "ages", forRemoval = true)
    void startWorkflow(ItemModel paramItemModel);


    @Deprecated(since = "ages", forRemoval = true)
    void terminateWorkflow(ItemModel paramItemModel);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isTerminated(Object paramObject);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isRunning(Object paramObject);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isFinished(Object paramObject);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isPlanned(Object paramObject);


    @Deprecated(since = "ages", forRemoval = true)
    void addItems(ItemModel paramItemModel, List<ItemModel> paramList);


    @Deprecated(since = "ages", forRemoval = true)
    ItemModel containsItem(ItemModel paramItemModel, List<ItemModel> paramList);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isAdhocWorkflow(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    boolean canBeStarted(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    Date getStartTime(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    void unassignUser(PrincipalModel paramPrincipalModel, WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getNormalWorkflowActions(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getEndWorkflowActions(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getWorkflowActionsByType(EnumerationValue paramEnumerationValue, WorkflowModel paramWorkflowModel);
}
