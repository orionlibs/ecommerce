package de.hybris.platform.ticket.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.ticket.events.jalo.CsCustomerEvent;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventCsAgentGroupEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventCsTicketCategoryEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventCsTicketPriorityEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventCsTicketStateEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventEmployeeEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketChangeEventStringEntry;
import de.hybris.platform.ticket.events.jalo.CsTicketEmail;
import de.hybris.platform.ticket.events.jalo.CsTicketEvent;
import de.hybris.platform.ticket.events.jalo.CsTicketResolutionEvent;
import de.hybris.platform.ticketsystem.events.jalo.SessionEndEvent;
import de.hybris.platform.ticketsystem.events.jalo.SessionEvent;
import de.hybris.platform.ticketsystem.events.jalo.SessionStartEvent;
import de.hybris.platform.ticketsystem.jalo.CSTicketStagnationCronJob;
import de.hybris.platform.ticketsystem.jalo.SessionEventsRemovalCronJob;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedTicketsystemManager extends Extension
{
    protected static String AGENT2BASESTORE_SRC_ORDERED = "relation.Agent2BaseStore.source.ordered";
    protected static String AGENT2BASESTORE_TGT_ORDERED = "relation.Agent2BaseStore.target.ordered";
    protected static String AGENT2BASESTORE_MARKMODIFIED = "relation.Agent2BaseStore.markmodified";
    protected static String CSAGENTGROUP2BASESTORE_SRC_ORDERED = "relation.CsAgentGroup2BaseStore.source.ordered";
    protected static String CSAGENTGROUP2BASESTORE_TGT_ORDERED = "relation.CsAgentGroup2BaseStore.target.ordered";
    protected static String CSAGENTGROUP2BASESTORE_MARKMODIFIED = "relation.CsAgentGroup2BaseStore.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public CsAgentGroup createCsAgentGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSAGENTGROUP);
            return (CsAgentGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsAgentGroup : " + e.getMessage(), 0);
        }
    }


    public CsAgentGroup createCsAgentGroup(Map attributeValues)
    {
        return createCsAgentGroup(getSession().getSessionContext(), attributeValues);
    }


    public CsCustomerEvent createCsCustomerEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSCUSTOMEREVENT);
            return (CsCustomerEvent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsCustomerEvent : " + e.getMessage(), 0);
        }
    }


    public CsCustomerEvent createCsCustomerEvent(Map attributeValues)
    {
        return createCsCustomerEvent(getSession().getSessionContext(), attributeValues);
    }


    public CsTicket createCsTicket(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKET);
            return (CsTicket)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicket : " + e.getMessage(), 0);
        }
    }


    public CsTicket createCsTicket(Map attributeValues)
    {
        return createCsTicket(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventCsAgentGroupEntry createCsTicketChangeEventCsAgentGroupEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTCSAGENTGROUPENTRY);
            return (CsTicketChangeEventCsAgentGroupEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventCsAgentGroupEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventCsAgentGroupEntry createCsTicketChangeEventCsAgentGroupEntry(Map attributeValues)
    {
        return createCsTicketChangeEventCsAgentGroupEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventCsTicketCategoryEntry createCsTicketChangeEventCsTicketCategoryEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTCSTICKETCATEGORYENTRY);
            return (CsTicketChangeEventCsTicketCategoryEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventCsTicketCategoryEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventCsTicketCategoryEntry createCsTicketChangeEventCsTicketCategoryEntry(Map attributeValues)
    {
        return createCsTicketChangeEventCsTicketCategoryEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventCsTicketPriorityEntry createCsTicketChangeEventCsTicketPriorityEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTCSTICKETPRIORITYENTRY);
            return (CsTicketChangeEventCsTicketPriorityEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventCsTicketPriorityEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventCsTicketPriorityEntry createCsTicketChangeEventCsTicketPriorityEntry(Map attributeValues)
    {
        return createCsTicketChangeEventCsTicketPriorityEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventCsTicketStateEntry createCsTicketChangeEventCsTicketStateEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTCSTICKETSTATEENTRY);
            return (CsTicketChangeEventCsTicketStateEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventCsTicketStateEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventCsTicketStateEntry createCsTicketChangeEventCsTicketStateEntry(Map attributeValues)
    {
        return createCsTicketChangeEventCsTicketStateEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventEmployeeEntry createCsTicketChangeEventEmployeeEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTEMPLOYEEENTRY);
            return (CsTicketChangeEventEmployeeEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventEmployeeEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventEmployeeEntry createCsTicketChangeEventEmployeeEntry(Map attributeValues)
    {
        return createCsTicketChangeEventEmployeeEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventEntry createCsTicketChangeEventEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTENTRY);
            return (CsTicketChangeEventEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventEntry createCsTicketChangeEventEntry(Map attributeValues)
    {
        return createCsTicketChangeEventEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketChangeEventStringEntry createCsTicketChangeEventStringEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETCHANGEEVENTSTRINGENTRY);
            return (CsTicketChangeEventStringEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketChangeEventStringEntry : " + e.getMessage(), 0);
        }
    }


    public CsTicketChangeEventStringEntry createCsTicketChangeEventStringEntry(Map attributeValues)
    {
        return createCsTicketChangeEventStringEntry(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketEmail createCsTicketEmail(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETEMAIL);
            return (CsTicketEmail)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketEmail : " + e.getMessage(), 0);
        }
    }


    public CsTicketEmail createCsTicketEmail(Map attributeValues)
    {
        return createCsTicketEmail(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketEvent createCsTicketEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETEVENT);
            return (CsTicketEvent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketEvent : " + e.getMessage(), 0);
        }
    }


    public CsTicketEvent createCsTicketEvent(Map attributeValues)
    {
        return createCsTicketEvent(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketEventEmailConfiguration createCsTicketEventEmailConfiguration(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETEVENTEMAILCONFIGURATION);
            return (CsTicketEventEmailConfiguration)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketEventEmailConfiguration : " + e.getMessage(), 0);
        }
    }


    public CsTicketEventEmailConfiguration createCsTicketEventEmailConfiguration(Map attributeValues)
    {
        return createCsTicketEventEmailConfiguration(getSession().getSessionContext(), attributeValues);
    }


    public CsTicketResolutionEvent createCsTicketResolutionEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETRESOLUTIONEVENT);
            return (CsTicketResolutionEvent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CsTicketResolutionEvent : " + e.getMessage(), 0);
        }
    }


    public CsTicketResolutionEvent createCsTicketResolutionEvent(Map attributeValues)
    {
        return createCsTicketResolutionEvent(getSession().getSessionContext(), attributeValues);
    }


    public CSTicketStagnationCronJob createCSTicketStagnationCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.CSTICKETSTAGNATIONCRONJOB);
            return (CSTicketStagnationCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CSTicketStagnationCronJob : " + e.getMessage(), 0);
        }
    }


    public CSTicketStagnationCronJob createCSTicketStagnationCronJob(Map attributeValues)
    {
        return createCSTicketStagnationCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SessionEndEvent createSessionEndEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.SESSIONENDEVENT);
            return (SessionEndEvent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SessionEndEvent : " + e.getMessage(), 0);
        }
    }


    public SessionEndEvent createSessionEndEvent(Map attributeValues)
    {
        return createSessionEndEvent(getSession().getSessionContext(), attributeValues);
    }


    public SessionEvent createSessionEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.SESSIONEVENT);
            return (SessionEvent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SessionEvent : " + e.getMessage(), 0);
        }
    }


    public SessionEvent createSessionEvent(Map attributeValues)
    {
        return createSessionEvent(getSession().getSessionContext(), attributeValues);
    }


    public SessionEventsRemovalCronJob createSessionEventsRemovalCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.SESSIONEVENTSREMOVALCRONJOB);
            return (SessionEventsRemovalCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SessionEventsRemovalCronJob : " + e.getMessage(), 0);
        }
    }


    public SessionEventsRemovalCronJob createSessionEventsRemovalCronJob(Map attributeValues)
    {
        return createSessionEventsRemovalCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SessionStartEvent createSessionStartEvent(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTicketsystemConstants.TC.SESSIONSTARTEVENT);
            return (SessionStartEvent)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SessionStartEvent : " + e.getMessage(), 0);
        }
    }


    public SessionStartEvent createSessionStartEvent(Map attributeValues)
    {
        return createSessionStartEvent(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "ticketsystem";
    }


    public List<Employee> getTicketemployees(SessionContext ctx, BaseStore item)
    {
        List<Employee> items = item.getLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, "Employee", null,
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true));
        return items;
    }


    public List<Employee> getTicketemployees(BaseStore item)
    {
        return getTicketemployees(getSession().getSessionContext(), item);
    }


    public long getTicketemployeesCount(SessionContext ctx, BaseStore item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, "Employee", null);
    }


    public long getTicketemployeesCount(BaseStore item)
    {
        return getTicketemployeesCount(getSession().getSessionContext(), item);
    }


    public void setTicketemployees(SessionContext ctx, BaseStore item, List<Employee> value)
    {
        item.setLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, null, value,
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(AGENT2BASESTORE_MARKMODIFIED));
    }


    public void setTicketemployees(BaseStore item, List<Employee> value)
    {
        setTicketemployees(getSession().getSessionContext(), item, value);
    }


    public void addToTicketemployees(SessionContext ctx, BaseStore item, Employee value)
    {
        item.addLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(AGENT2BASESTORE_MARKMODIFIED));
    }


    public void addToTicketemployees(BaseStore item, Employee value)
    {
        addToTicketemployees(getSession().getSessionContext(), item, value);
    }


    public void removeFromTicketemployees(SessionContext ctx, BaseStore item, Employee value)
    {
        item.removeLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(AGENT2BASESTORE_MARKMODIFIED));
    }


    public void removeFromTicketemployees(BaseStore item, Employee value)
    {
        removeFromTicketemployees(getSession().getSessionContext(), item, value);
    }


    public List<CsAgentGroup> getTicketgroups(SessionContext ctx, BaseStore item)
    {
        List<CsAgentGroup> items = item.getLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, "CsAgentGroup", null,
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true));
        return items;
    }


    public List<CsAgentGroup> getTicketgroups(BaseStore item)
    {
        return getTicketgroups(getSession().getSessionContext(), item);
    }


    public long getTicketgroupsCount(SessionContext ctx, BaseStore item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, "CsAgentGroup", null);
    }


    public long getTicketgroupsCount(BaseStore item)
    {
        return getTicketgroupsCount(getSession().getSessionContext(), item);
    }


    public void setTicketgroups(SessionContext ctx, BaseStore item, List<CsAgentGroup> value)
    {
        item.setLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, null, value,
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED));
    }


    public void setTicketgroups(BaseStore item, List<CsAgentGroup> value)
    {
        setTicketgroups(getSession().getSessionContext(), item, value);
    }


    public void addToTicketgroups(SessionContext ctx, BaseStore item, CsAgentGroup value)
    {
        item.addLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED));
    }


    public void addToTicketgroups(BaseStore item, CsAgentGroup value)
    {
        addToTicketgroups(getSession().getSessionContext(), item, value);
    }


    public void removeFromTicketgroups(SessionContext ctx, BaseStore item, CsAgentGroup value)
    {
        item.removeLinkedItems(ctx, false, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED));
    }


    public void removeFromTicketgroups(BaseStore item, CsAgentGroup value)
    {
        removeFromTicketgroups(getSession().getSessionContext(), item, value);
    }


    public List<BaseStore> getTicketstores(SessionContext ctx, Employee item)
    {
        List<BaseStore> items = item.getLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, "BaseStore", null,
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true));
        return items;
    }


    public List<BaseStore> getTicketstores(Employee item)
    {
        return getTicketstores(getSession().getSessionContext(), item);
    }


    public long getTicketstoresCount(SessionContext ctx, Employee item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, "BaseStore", null);
    }


    public long getTicketstoresCount(Employee item)
    {
        return getTicketstoresCount(getSession().getSessionContext(), item);
    }


    public void setTicketstores(SessionContext ctx, Employee item, List<BaseStore> value)
    {
        item.setLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, null, value,
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(AGENT2BASESTORE_MARKMODIFIED));
    }


    public void setTicketstores(Employee item, List<BaseStore> value)
    {
        setTicketstores(getSession().getSessionContext(), item, value);
    }


    public void addToTicketstores(SessionContext ctx, Employee item, BaseStore value)
    {
        item.addLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(AGENT2BASESTORE_MARKMODIFIED));
    }


    public void addToTicketstores(Employee item, BaseStore value)
    {
        addToTicketstores(getSession().getSessionContext(), item, value);
    }


    public void removeFromTicketstores(SessionContext ctx, Employee item, BaseStore value)
    {
        item.removeLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.AGENT2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(AGENT2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(AGENT2BASESTORE_MARKMODIFIED));
    }


    public void removeFromTicketstores(Employee item, BaseStore value)
    {
        removeFromTicketstores(getSession().getSessionContext(), item, value);
    }
}
