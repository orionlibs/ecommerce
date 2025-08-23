package de.hybris.platform.cms2.workflow.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;

public interface CMSWorkflowTemplateService
{
    List<WorkflowTemplateModel> getVisibleWorkflowTemplatesForCatalogVersion(CatalogVersionModel paramCatalogVersionModel);
}
