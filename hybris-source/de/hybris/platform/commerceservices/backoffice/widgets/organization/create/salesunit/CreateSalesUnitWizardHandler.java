package de.hybris.platform.commerceservices.backoffice.widgets.organization.create.salesunit;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitParameter;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import java.util.Map;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CreateSalesUnitWizardHandler implements FlowActionHandler
{
    private static final Logger LOG = Logger.getLogger(CreateSalesUnitWizardHandler.class);
    private NotificationStack notificationStack;
    private OrgUnitService orgUnitService;
    private NotificationService notificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        try
        {
            OrgUnitParameter param = new OrgUnitParameter();
            OrgUnitModel newSalesUnit = (OrgUnitModel)adapter.getWidgetInstanceManager().getModel().getValue("newsalesunit", OrgUnitModel.class);
            OrgUnitModel parentUnit = (OrgUnitModel)adapter.getWidgetInstanceManager().getModel().getValue("parentUnit", OrgUnitModel.class);
            param.setUid(newSalesUnit.getUid());
            param.setName(newSalesUnit.getName());
            param.setActive(newSalesUnit.getActive());
            param.setParentUnit(parentUnit);
            param.setDescription(newSalesUnit.getDescription());
            param.setLineOfBusiness(newSalesUnit.getLineOfBuisness());
            param.setSupplier(Boolean.TRUE);
            Optional<OrgUnitModel> createdOrgUnit = getOrgUnitService().createAndGetUnit(param);
            createdOrgUnit.ifPresent(orgUnitModel -> adapter.getWidgetInstanceManager().getModel().setValue("newsalesunit", orgUnitModel));
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error for creating sales unit.", e);
            }
            ConfigurableFlowController controller = (ConfigurableFlowController)adapter.getWidgetInstanceManager().getWidgetslot().getAttribute("widgetController");
            if("step2".equals(controller.getCurrentStep().getId()))
            {
                adapter.back();
            }
            if(e instanceof ModelSavingException)
            {
                ModelSavingException ex = (ModelSavingException)e;
                while(ex != null)
                {
                    Throwable ie = ex.getCause();
                    getNotificationService().notifyUser(ie.getMessage(), "CreateObject", NotificationEvent.Level.FAILURE, new Object[] {ex});
                    ex = ex.getNextException();
                }
            }
            else
            {
                getNotificationService().notifyUser(e.getMessage(), "CreateObject", NotificationEvent.Level.FAILURE, new Object[] {e});
            }
            return;
        }
        adapter.done();
    }


    protected NotificationStack getNotificationStack()
    {
        return this.notificationStack;
    }


    @Required
    public void setNotificationStack(NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }


    protected OrgUnitService getOrgUnitService()
    {
        return this.orgUnitService;
    }


    @Required
    public void setOrgUnitService(OrgUnitService orgUnitService)
    {
        this.orgUnitService = orgUnitService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
