package de.hybris.platform.workflow.systemsetup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "workflow")
public class WorkflowSystemSetup
{
    private static final Logger LOG = Logger.getLogger(WorkflowSystemSetup.class);
    private FlexibleSearchService flexibleSearchService;
    private ImportService importService;


    @SystemSetup(process = SystemSetup.Process.ALL, type = SystemSetup.Type.ESSENTIAL)
    public void createEssentialData()
    {
        LOG.info("Importing resource /workflow/adhoc_workflow_template.impex");
        String adhocTemplateName = Config.getParameter("workflow.adhoctemplate.name");
        if(adhocTemplateName == null || adhocTemplateName.isEmpty())
        {
            LOG.error("Missing adhoc template name for key: 'workflow.adhoctemplate.name'. Check project.properties file of workflow extension.");
        }
        else
        {
            List<Object> ret = this.flexibleSearchService.search("SELECT {pk} FROM {WorkflowTemplate as wt} WHERE {wt.code} LIKE '" + adhocTemplateName + "'").getResult();
            if(ret.isEmpty())
            {
                this.importService.importData((ImpExResource)new StreamBasedImpExResource(WorkflowSystemSetup.class
                                .getResourceAsStream("/workflow/adhoc_workflow_template.impex"), "UTF-8",
                                Character.valueOf(';')));
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("Adhoc template was not imported since it already exist in the system.");
            }
        }
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }
}
