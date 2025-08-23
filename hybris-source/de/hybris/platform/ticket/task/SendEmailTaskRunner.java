package de.hybris.platform.ticket.task;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.TicketEventEmailStrategy;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class SendEmailTaskRunner implements TaskRunner<TaskModel>
{
    public static final String TICKET_MODEL_KEY = "ticketEventModelKey";
    public static final String TICKET_EVENT_MODEL_KEY = "ticketEventKey";
    public static final String LOCALE_MODEL_KEY = "localeKey";
    private TicketEventEmailStrategy ticketEventEmailStrategy;
    private I18NService i18NService;


    public void run(TaskService taskService, TaskModel taskModel)
    {
        Map<String, Object> context = (Map<String, Object>)taskModel.getContext();
        CsTicketModel ticket = (CsTicketModel)context.get("ticketEventModelKey");
        CsTicketEventModel event = (CsTicketEventModel)context.get("ticketEventKey");
        getI18NService().setCurrentLocale((Locale)context.get("localeKey"));
        getTicketEventEmailStrategy().sendEmailsForEvent(ticket, event);
    }


    public void handleError(TaskService taskService, TaskModel taskModel, Throwable throwable)
    {
    }


    protected TicketEventEmailStrategy getTicketEventEmailStrategy()
    {
        return this.ticketEventEmailStrategy;
    }


    @Required
    public void setTicketEventEmailStrategy(TicketEventEmailStrategy ticketEventEmailStrategy)
    {
        this.ticketEventEmailStrategy = ticketEventEmailStrategy;
    }


    protected I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
