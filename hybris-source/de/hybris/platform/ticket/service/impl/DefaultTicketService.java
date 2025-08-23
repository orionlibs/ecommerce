package de.hybris.platform.ticket.service.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.ticket.dao.AgentDao;
import de.hybris.platform.ticket.dao.TicketDao;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.resolver.TicketAssociatedObjectResolver;
import de.hybris.platform.ticket.service.TicketService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketService implements TicketService
{
    private TicketDao ticketDao;
    private AgentDao agentDao;
    private CommentService commentService;
    private EnumerationService enumerationService;
    private String ticketSystemDomain;
    private String ticketSystemComponent;
    private Map<String, TicketAssociatedObjectResolver> associatedTicketObjectResolverMap;


    public List<CsAgentGroupModel> getAgentGroups()
    {
        return this.agentDao.findAgentGroups();
    }


    public List<CsAgentGroupModel> getAgentGroupsForBaseStore(BaseStoreModel store)
    {
        if(store == null)
        {
            throw new IllegalArgumentException("store must not be null");
        }
        return this.agentDao.findAgentGroupsByBaseStore(store);
    }


    public List<EmployeeModel> getAgents()
    {
        return this.agentDao.findAgents();
    }


    public List<EmployeeModel> getAgentsForBaseStore(BaseStoreModel store)
    {
        if(store == null)
        {
            throw new IllegalArgumentException("store must not be null");
        }
        return this.agentDao.findAgentsByBaseStore(store);
    }


    public List<CsEventReason> getEventReasons()
    {
        return this.enumerationService.getEnumerationValues(GeneratedTicketsystemConstants.TC.CSEVENTREASON);
    }


    public List<CsTicketEventModel> getEventsForTicket(CsTicketModel ticket)
    {
        return this.ticketDao.findTicketEventsByTicket(ticket);
    }


    public List<CsTicketEventModel> getTicketEventsForCustomerByTicket(CsTicketModel ticket)
    {
        return this.ticketDao.findTicketEventsForCustomerByTicket(ticket);
    }


    public List<CsInterventionType> getInterventionTypes()
    {
        return this.enumerationService.getEnumerationValues(GeneratedTicketsystemConstants.TC.CSINTERVENTIONTYPE);
    }


    public List<CsResolutionType> getResolutionTypes()
    {
        return this.enumerationService.getEnumerationValues(GeneratedTicketsystemConstants.TC.CSRESOLUTIONTYPE);
    }


    public List<CsTicketCategory> getTicketCategories()
    {
        return this.enumerationService.getEnumerationValues(GeneratedTicketsystemConstants.TC.CSTICKETCATEGORY);
    }


    public CsTicketModel getTicketForTicketEvent(CsTicketEventModel ticketEvent)
    {
        Collection<ItemModel> relatedItems = ticketEvent.getRelatedItems();
        if(relatedItems != null)
        {
            if(relatedItems.size() > 1)
            {
                throw new IllegalStateException("A ticket event should only associated with a single ticket. Error occurred on event [" + ticketEvent
                                .getPk() + "]");
            }
            if(relatedItems.size() == 1)
            {
                ItemModel item = relatedItems.iterator().next();
                if(item instanceof CsTicketModel)
                {
                    return (CsTicketModel)item;
                }
                throw new IllegalStateException("A ticket event must be associated with a ticket. Error occurred on event [" + ticketEvent
                                .getPk() + "] found related item [" + item + "]");
            }
        }
        return null;
    }


    public CsTicketModel getTicketForTicketId(String ticketId)
    {
        if(ticketId == null || "".equals(ticketId))
        {
            return null;
        }
        List<CsTicketModel> tickets = this.ticketDao.findTicketsById(ticketId);
        if(tickets.isEmpty())
        {
            return null;
        }
        if(tickets.size() > 1)
        {
            throw new AmbiguousIdentifierException("CsTicket with ticketId'" + ticketId + "' is not unique, " + tickets
                            .size() + " results!");
        }
        return tickets.get(0);
    }


    public List<CsTicketPriority> getTicketPriorities()
    {
        return this.enumerationService.getEnumerationValues(GeneratedTicketsystemConstants.TC.CSTICKETPRIORITY);
    }


    public List<CsTicketModel> getTicketsForAgent(EmployeeModel agent)
    {
        if(agent == null)
        {
            return this.ticketDao.findTicketsWithNullAgent();
        }
        return this.ticketDao.findTicketsByAgent(agent);
    }


    public List<CsTicketModel> getTicketsForAgentGroup(CsAgentGroupModel agentGroup)
    {
        if(agentGroup == null)
        {
            return this.ticketDao.findTicketsWithNullAgentGroup();
        }
        return this.ticketDao.findTicketsByAgentGroup(agentGroup);
    }


    public List<CsTicketModel> getTicketsForCategory(CsTicketCategory... category)
    {
        if(category == null || category.length == 0)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByCategory(category);
    }


    public List<CsTicketModel> getTicketsForCustomer(UserModel customer)
    {
        if(customer == null)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByCustomer(customer);
    }


    public List<CsTicketModel> getTicketsForCustomerOrderByModifiedTime(UserModel user)
    {
        if(user == null)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByCustomerOrderByModifiedTime(user);
    }


    public SearchPageData<CsTicketModel> getTicketsForCustomerOrderByModifiedTime(UserModel user, BaseSiteModel baseSite, PageableData pageableData)
    {
        return this.ticketDao.findTicketsByCustomerOrderByModifiedTime(user, baseSite, pageableData);
    }


    public List<CsTicketModel> getTicketsForOrder(OrderModel order)
    {
        if(order == null)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByOrder(order);
    }


    public List<CsTicketModel> getTicketsForPriority(CsTicketPriority... priority)
    {
        if(priority == null || priority.length == 0)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByPriority(priority);
    }


    public List<CsTicketModel> getTicketsForResolutionType(CsResolutionType... resolutionType)
    {
        if(resolutionType == null || resolutionType.length == 0)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByResolutionType(resolutionType);
    }


    public List<CsTicketModel> getTicketsForState(CsTicketState... state)
    {
        if(state == null || state.length == 0)
        {
            return Collections.emptyList();
        }
        return this.ticketDao.findTicketsByState(state);
    }


    public List<CsTicketState> getTicketStates()
    {
        return this.enumerationService.getEnumerationValues(GeneratedTicketsystemConstants.TC.CSTICKETSTATE);
    }


    public CommentTypeModel getTicketType(String type)
    {
        DomainModel domain = this.commentService.getDomainForCode(this.ticketSystemDomain);
        ComponentModel component = this.commentService.getComponentForCode(domain, this.ticketSystemComponent);
        return this.commentService.getCommentTypeForCode(component, type);
    }


    public AbstractOrderModel getAssociatedObject(String associatedCode, String userUid, String siteUid)
    {
        if(!StringUtils.isEmpty(associatedCode))
        {
            String[] associatedToTokens = associatedCode.split("=");
            if(this.associatedTicketObjectResolverMap.containsKey(associatedToTokens[0]))
            {
                TicketAssociatedObjectResolver ticketAssociatedObjectResolver = this.associatedTicketObjectResolverMap.get(associatedToTokens[0]);
                return ticketAssociatedObjectResolver.getObject(associatedToTokens[1], userUid, siteUid);
            }
        }
        return null;
    }


    protected Map<String, TicketAssociatedObjectResolver> getAssociatedTicketObjectResolverMap()
    {
        return this.associatedTicketObjectResolverMap;
    }


    @Required
    public void setAssociatedTicketObjectResolverMap(Map<String, TicketAssociatedObjectResolver> associatedTicketObjectResolverMap)
    {
        this.associatedTicketObjectResolverMap = associatedTicketObjectResolverMap;
    }


    public void setAgentDao(AgentDao agentDao)
    {
        this.agentDao = agentDao;
    }


    @Required
    public void setCommentService(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    @Required
    public void setTicketDao(TicketDao ticketDao)
    {
        this.ticketDao = ticketDao;
    }


    @Required
    public void setTicketSystemComponent(String ticketSystemComponent)
    {
        this.ticketSystemComponent = ticketSystemComponent;
    }


    @Required
    public void setTicketSystemDomain(String ticketSystemDomain)
    {
        this.ticketSystemDomain = ticketSystemDomain;
    }
}
