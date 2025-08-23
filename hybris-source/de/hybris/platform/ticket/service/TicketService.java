package de.hybris.platform.ticket.service;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.ticket.enums.CsEventReason;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.List;

public interface TicketService
{
    List<CsAgentGroupModel> getAgentGroups();


    List<CsAgentGroupModel> getAgentGroupsForBaseStore(BaseStoreModel paramBaseStoreModel);


    List<EmployeeModel> getAgents();


    List<EmployeeModel> getAgentsForBaseStore(BaseStoreModel paramBaseStoreModel);


    List<CsEventReason> getEventReasons();


    List<CsTicketEventModel> getEventsForTicket(CsTicketModel paramCsTicketModel);


    List<CsTicketEventModel> getTicketEventsForCustomerByTicket(CsTicketModel paramCsTicketModel);


    List<CsInterventionType> getInterventionTypes();


    List<CsResolutionType> getResolutionTypes();


    List<CsTicketCategory> getTicketCategories();


    CsTicketModel getTicketForTicketEvent(CsTicketEventModel paramCsTicketEventModel);


    CsTicketModel getTicketForTicketId(String paramString);


    List<CsTicketPriority> getTicketPriorities();


    List<CsTicketModel> getTicketsForAgent(EmployeeModel paramEmployeeModel);


    List<CsTicketModel> getTicketsForAgentGroup(CsAgentGroupModel paramCsAgentGroupModel);


    List<CsTicketModel> getTicketsForCategory(CsTicketCategory... paramVarArgs);


    List<CsTicketModel> getTicketsForCustomer(UserModel paramUserModel);


    List<CsTicketModel> getTicketsForOrder(OrderModel paramOrderModel);


    List<CsTicketModel> getTicketsForPriority(CsTicketPriority... paramVarArgs);


    List<CsTicketModel> getTicketsForResolutionType(CsResolutionType... paramVarArgs);


    List<CsTicketModel> getTicketsForState(CsTicketState... paramVarArgs);


    List<CsTicketState> getTicketStates();


    CommentTypeModel getTicketType(String paramString);


    List<CsTicketModel> getTicketsForCustomerOrderByModifiedTime(UserModel paramUserModel);


    SearchPageData<CsTicketModel> getTicketsForCustomerOrderByModifiedTime(UserModel paramUserModel, BaseSiteModel paramBaseSiteModel, PageableData paramPageableData);


    AbstractOrderModel getAssociatedObject(String paramString1, String paramString2, String paramString3);
}
