package de.hybris.platform.cms2.workflow.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowTemplateDao;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowTemplateService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSWorkflowTemplateService implements CMSWorkflowTemplateService
{
    private CMSWorkflowTemplateDao cmsWorkflowTemplateDao;
    private UserService userService;


    public List<WorkflowTemplateModel> getVisibleWorkflowTemplatesForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        return getCmsWorkflowTemplateDao().getVisibleWorkflowTemplatesForCatalogVersion(catalogVersion, (PrincipalModel)
                        getUserService().getCurrentUser());
    }


    protected CMSWorkflowTemplateDao getCmsWorkflowTemplateDao()
    {
        return this.cmsWorkflowTemplateDao;
    }


    @Required
    public void setCmsWorkflowTemplateDao(CMSWorkflowTemplateDao cmsWorkflowTemplateDao)
    {
        this.cmsWorkflowTemplateDao = cmsWorkflowTemplateDao;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
