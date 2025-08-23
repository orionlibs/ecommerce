package de.hybris.platform.commerceservices.backoffice.widgets.organization.create.orgunit.employee;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.services.OrgUnitService;
import de.hybris.platform.commerceservices.organization.utils.OrgUtils;
import de.hybris.platform.commerceservices.util.CommerceSearchUtils;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCreateOrgUnitEmployeeWizardHandler implements FlowActionHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultCreateOrgUnitEmployeeWizardHandler.class);
    private NotificationStack notificationStack;
    private OrgUnitService orgUnitService;
    private ModelService modelService;
    private NotificationService notificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        try
        {
            EmployeeModel newOrgUnitEmployee = (EmployeeModel)adapter.getWidgetInstanceManager().getModel().getValue("neworgunitempl", EmployeeModel.class);
            Set<PrincipalGroupModel> orgRoles = (Set<PrincipalGroupModel>)adapter.getWidgetInstanceManager().getModel().getValue("orgRoles", Set.class);
            Set<OrgUnitModel> orgUnits = (Set<OrgUnitModel>)adapter.getWidgetInstanceManager().getModel().getValue("orgUnits", Set.class);
            newOrgUnitEmployee.setGroups(orgRoles);
            getModelService().save(newOrgUnitEmployee);
            for(OrgUnitModel orgUnitModel : orgUnits)
            {
                getOrgUnitService().addMembers(
                                OrgUtils.createOrgUnitMemberParameter(orgUnitModel.getUid(), Collections.singleton(newOrgUnitEmployee), EmployeeModel.class,
                                                CommerceSearchUtils.getAllOnOnePagePageableData()));
            }
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error for creating org unit employee.", e);
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
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
