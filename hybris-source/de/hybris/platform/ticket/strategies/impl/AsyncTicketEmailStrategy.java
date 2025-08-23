package de.hybris.platform.ticket.strategies.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.TicketEventEmailStrategy;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Required;

public class AsyncTicketEmailStrategy implements TicketEventEmailStrategy
{
    private TaskService taskService;
    private I18NService i18NService;
    private ModelService modelService;


    public void sendEmailsForEvent(CsTicketModel ticket, CsTicketEventModel event)
    {
        sendEmailsForAssignAgentTicketEvent(ticket, event, null);
    }


    public void sendEmailsForAssignAgentTicketEvent(CsTicketModel ticket, CsTicketEventModel event, CsEmailRecipients recipientType)
    {
        TaskModel sendEmailTask = (TaskModel)getModelService().create(TaskModel.class);
        sendEmailTask.setRunnerBean("sendEmailTaskRunner");
        sendEmailTask.setExecutionDate(new Date());
        HashMap<String, Object> taskContext = new HashMap<>();
        taskContext.put("ticketEventModelKey", ticket);
        taskContext.put("ticketEventKey", event);
        taskContext.put("localeKey", getI18NService().getCurrentLocale());
        sendEmailTask.setContext(taskContext);
        getTaskService().scheduleTask(sendEmailTask);
    }


    protected TaskService getTaskService()
    {
        return this.taskService;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
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
