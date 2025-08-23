package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;

public interface CMSWorkflowTemplateDao
{
    List<WorkflowTemplateModel> getVisibleWorkflowTemplatesForCatalogVersion(CatalogVersionModel paramCatalogVersionModel, PrincipalModel paramPrincipalModel);
}
