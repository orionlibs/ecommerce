package de.hybris.platform.workflow.daos;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public interface WorkflowDao
{
    List<WorkflowModel> findAllWorkflows(Date paramDate1, Date paramDate2);


    SearchResult<WorkflowModel> findAllWorkflows(Date paramDate1, Date paramDate2, EnumSet<WorkflowStatus> paramEnumSet, int paramInt1, int paramInt2);


    List<WorkflowModel> findAllAdhocWorkflows(Date paramDate1, Date paramDate2);


    SearchResult<WorkflowModel> findAllAdhocWorkflows(Date paramDate1, Date paramDate2, EnumSet<WorkflowStatus> paramEnumSet, int paramInt1, int paramInt2);


    List<WorkflowModel> findWorkflowsByUserAndTemplate(UserModel paramUserModel, WorkflowTemplateModel paramWorkflowTemplateModel);


    List<WorkflowModel> findWorkflowsByCode(String paramString);
}
