package de.hybris.platform.platformbackoffice.widgets.processengine;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class RepairProcessWizardHandler implements FlowActionHandler
{
    private BusinessProcessService businessProcessService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        Map<String, Object> currentContext = (Map<String, Object>)adapter.getWidgetInstanceManager().getModel().getValue("currentContext", Map.class);
        BusinessProcessModel businessProcess = (BusinessProcessModel)currentContext.get("businessProcess");
        String targetStep = (String)currentContext.get("targetStep");
        this.businessProcessService.restartProcess(businessProcess, targetStep);
        currentContext.put("successful", Boolean.TRUE);
        adapter.custom();
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }
}
