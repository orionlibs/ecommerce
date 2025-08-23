package de.hybris.platform.ticket.dao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.List;
import java.util.Set;

public interface TicketDao
{
    List<CsTicketModel> findTicketsByAgentGroupState(EmployeeModel paramEmployeeModel, CsAgentGroupModel paramCsAgentGroupModel, CsTicketState paramCsTicketState);


    List<CsTicketModel> findTicketsByStringInTicketOrEvent(String paramString);


    List<CsTicketModel> findTicketsByStringInTicketOrEventAndStates(String paramString, Set<CsTicketState> paramSet);


    List<CsTicketEventModel> findTicketEventsByTicket(CsTicketModel paramCsTicketModel);


    List<CsTicketEventModel> findTicketEventsForCustomerByTicket(CsTicketModel paramCsTicketModel);


    List<CsTicketModel> findTicketsByAgent(EmployeeModel paramEmployeeModel);


    List<CsTicketModel> findTicketsByAgentGroup(CsAgentGroupModel paramCsAgentGroupModel);


    List<CsTicketModel> findTicketsByCategory(CsTicketCategory... paramVarArgs);


    List<CsTicketModel> findTicketsByCustomer(UserModel paramUserModel);


    List<CsTicketModel> findTicketsById(String paramString);


    List<CsTicketModel> findTicketsByOrder(OrderModel paramOrderModel);


    List<CsTicketModel> findTicketsByPriority(CsTicketPriority... paramVarArgs);


    List<CsTicketModel> findTicketsByResolutionType(CsResolutionType... paramVarArgs);


    List<CsTicketModel> findTicketsByState(CsTicketState... paramVarArgs);


    List<CsTicketModel> findTicketsWithNullAgent();


    List<CsTicketModel> findTicketsWithNullAgentGroup();


    List<CsTicketModel> findTicketsByCustomerOrderByModifiedTime(UserModel paramUserModel);


    SearchPageData<CsTicketModel> findTicketsByCustomerOrderByModifiedTime(UserModel paramUserModel, BaseSiteModel paramBaseSiteModel, PageableData paramPageableData);
}
