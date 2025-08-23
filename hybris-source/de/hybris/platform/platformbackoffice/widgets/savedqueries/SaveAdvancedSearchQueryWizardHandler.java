package de.hybris.platform.platformbackoffice.widgets.savedqueries;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.platformbackoffice.services.BackofficeSavedQueriesService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class SaveAdvancedSearchQueryWizardHandler implements FlowActionHandler
{
    private BackofficeSavedQueriesService backofficeSavedQueriesService;
    private UserService userService;
    private CockpitEventQueue cockpitEventQueue;
    private NotificationService notificationService;
    protected static final String BACKOFFICE_SAVED_QUERY_MODEL = "backofficeSavedQueryModel";
    protected static final String SAVE_ADVANCED_SEARCH_QUERY_FORM = "saveAdvancedSearchQueryForm";


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        SaveAdvancedSearchQueryForm queryForm = (SaveAdvancedSearchQueryForm)adapter.getWidgetInstanceManager().getModel().getValue("saveAdvancedSearchQueryForm", SaveAdvancedSearchQueryForm.class);
        BackofficeSavedQueryModel savedQuery = getBackofficeSavedQueriesService().createSavedQuery(queryForm.getQueryName(), queryForm
                        .getAdvancedSearchData(), getUserService().getCurrentUser(), queryForm.getUserGroups());
        adapter.getWidgetInstanceManager().sendOutput("wizardResult",
                        Collections.singletonMap("backofficeSavedQueryModel", savedQuery));
        adapter.custom();
        publishCRUDCockpitEventNotification("objectCreated", savedQuery);
        getNotificationService().notifyUser(getNotificationSource(adapter.getWidgetInstanceManager()), "CreateObject", NotificationEvent.Level.SUCCESS, new Object[] {savedQuery});
    }


    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(WidgetInstanceManager wim)
    {
        return getNotificationService().getWidgetNotificationSource(wim);
    }


    private void publishCRUDCockpitEventNotification(String info, Object obj)
    {
        getCockpitEventQueue().publishEvent((CockpitEvent)new DefaultCockpitEvent(info, obj, null));
    }


    protected BackofficeSavedQueriesService getBackofficeSavedQueriesService()
    {
        return this.backofficeSavedQueriesService;
    }


    @Required
    public void setBackofficeSavedQueriesService(BackofficeSavedQueriesService backofficeSavedQueriesService)
    {
        this.backofficeSavedQueriesService = backofficeSavedQueriesService;
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


    protected CockpitEventQueue getCockpitEventQueue()
    {
        return this.cockpitEventQueue;
    }


    @Required
    public void setCockpitEventQueue(CockpitEventQueue cockpitEventQueue)
    {
        this.cockpitEventQueue = cockpitEventQueue;
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
