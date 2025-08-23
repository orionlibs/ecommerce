package de.hybris.platform.workflow.dao;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Date;
import java.util.List;

@Deprecated(since = "ages", forRemoval = true)
public interface WorkflowDAO extends Dao
{
    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowModel> getAllWorkflows(Date paramDate1, Date paramDate2);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowModel> getAllAdhocWorkflows(Date paramDate1, Date paramDate2);


    @Deprecated(since = "ages", forRemoval = true)
    List<Object> getUsersWorkflowTemplates();


    @Deprecated(since = "ages", forRemoval = true)
    WorkflowTemplateModel getAdhocWorkflowTemplate();


    @Deprecated(since = "ages", forRemoval = true)
    Employee getAdhocWorkflowTemplateDummyOwner();


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getNormalWorkflowActions(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getEndWorkflowActions(WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowActionModel> getWorkflowActionsByType(EnumerationValue paramEnumerationValue, WorkflowModel paramWorkflowModel);


    @Deprecated(since = "ages", forRemoval = true)
    List<WorkflowTemplateModel> getWorkflowTemplatesVisibleForUser(PrincipalModel paramPrincipalModel);
}
