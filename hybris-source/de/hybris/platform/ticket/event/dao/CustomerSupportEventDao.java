package de.hybris.platform.ticket.event.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ticket.enums.EventType;
import de.hybris.platform.ticketsystem.events.model.SessionEventModel;
import java.util.Date;
import java.util.List;

public interface CustomerSupportEventDao
{
    SearchPageData<SessionEventModel> findAllEventsByAgent(EmployeeModel paramEmployeeModel, EventType paramEventType, Date paramDate1, Date paramDate2, PageableData paramPageableData, int paramInt);


    @Deprecated(since = "6.7", forRemoval = true)
    <T extends de.hybris.platform.core.model.user.CustomerModel> SearchPageData<T> findAllCustomersByEventsAndAgent(EmployeeModel paramEmployeeModel, EventType paramEventType, Date paramDate1, Date paramDate2, PageableData paramPageableData, int paramInt);


    <T extends de.hybris.platform.core.model.user.CustomerModel> SearchPageData<T> findAllCustomersByEventsAndAgent(EmployeeModel paramEmployeeModel, EventType paramEventType, Date paramDate1, Date paramDate2, PageableData paramPageableData, int paramInt, boolean paramBoolean);


    List<SessionEventModel> findAllEventsBeforeDate(EventType paramEventType, Date paramDate);
}
