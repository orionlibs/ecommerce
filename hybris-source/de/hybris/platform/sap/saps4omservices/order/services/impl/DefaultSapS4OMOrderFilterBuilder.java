/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.order.services.impl;

import com.microsoft.sqlserver.jdbc.StringUtils;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.saps4omservices.constants.Saps4omservicesConstants;
import de.hybris.platform.sap.saps4omservices.filter.dto.FilterData;
import de.hybris.platform.sap.saps4omservices.order.services.SapS4OMOrderFilterBuilder;
import de.hybris.platform.sap.saps4omservices.order.services.SapS4OMOrderFilterBuilderHook;
import de.hybris.platform.sap.saps4omservices.utils.SapS4OrderUtil;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSapS4OMOrderFilterBuilder implements SapS4OMOrderFilterBuilder
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSapS4OMOrderFilterBuilder.class);
    private static final String SINGLE_QUOTE = "'";
    private static final String EQUAL_OPERATOR = "eq";
    private static final String AND_OPERATOR = "and ";
    private static final String FILETR_SUFFIX = "$filter=";
    private static final String PAGING_TOP_SUFFIX = "$top=";
    private static final String PAGING_SKIP_SUFFIX = "$skip=";
    private static final String ORDER_BY_SUFFIX = "$orderby=";
    private static final String PAGING_COUNT_SUFFIX = "$inlinecount=";
    private static final String PAGING_COUNT = "allpages";
    private static final String EXPAND_PARAM_ITEM = "to_Item";
    private static final String EXPAND_PARAM_PRICING_ELEMENT = "to_PricingElement";
    private static final String EXPAND_PARAM_PARTNER = "to_Partner";
    private static final String EXPAND_SUFFIX = "$expand=";
    private SapS4OrderUtil sapS4OrderUtil;
    private List<SapS4OMOrderFilterBuilderHook> sapS4OMOrderFilterBuilderHooks;
    private ConfigurationService configurationService;


    @Override
    public Map<String, List<FilterData>> getOrderHistoryFilters(CustomerModel customerModel, OrderStatus[] status,
                    PageableData paginationData, String sortType)
    {
        Map<String, List<FilterData>> filters = new HashMap<>();
        List<FilterData> orderHistoryFilters = new ArrayList<>();
        final String orderTransactionType = getSapS4OrderUtil().getCommonTransactionType();
        if(!StringUtils.isEmpty(orderTransactionType))
        {
            LOG.debug("Order transaction type is {}", orderTransactionType);
            final FilterData orderTypeFilterData = new FilterData.FilterDataBuilder(getConfigurationService().getConfiguration().getString(Saps4omservicesConstants.ORDER_TYPE))
                            .valueWithSpacePrefix(SINGLE_QUOTE + orderTransactionType + SINGLE_QUOTE)
                            .operatorWithSpacePrefix(EQUAL_OPERATOR)
                            .separatorWithSpacePrefix(AND_OPERATOR)
                            .build();
            orderHistoryFilters.add(orderTypeFilterData);
        }
        final String soldToParty = getSapS4OrderUtil().getSoldToParty(customerModel);
        if(!StringUtils.isEmpty(soldToParty))
        {
            LOG.debug("Sold to party is {}", soldToParty);
            final FilterData soldToFilterData = new FilterData.FilterDataBuilder(getConfigurationService().getConfiguration().getString(Saps4omservicesConstants.SOLD_TO))
                            .valueWithSpacePrefix(SINGLE_QUOTE + soldToParty + SINGLE_QUOTE)
                            .operatorWithSpacePrefix(EQUAL_OPERATOR)
                            .build();
            orderHistoryFilters.add(soldToFilterData);
        }
        final int currentPage = paginationData.getCurrentPage();
        final int pageSize = paginationData.getPageSize();
        final int skip = pageSize * currentPage;
        filters.put(FILETR_SUFFIX, orderHistoryFilters);
        FilterData paginTopFiler = new FilterData.FilterDataBuilder(String.valueOf(pageSize))
                        .build();
        filters.put(PAGING_TOP_SUFFIX, Arrays.asList(paginTopFiler));
        FilterData paginSkipFiler = new FilterData.FilterDataBuilder(String.valueOf(skip))
                        .build();
        filters.put(PAGING_SKIP_SUFFIX, Arrays.asList(paginSkipFiler));
        FilterData orderByFiler = new FilterData.FilterDataBuilder(sortType)
                        .valueWithSpacePrefix(getConfigurationService().getConfiguration().getString(Saps4omservicesConstants.SORT_ORDER))
                        .build();
        filters.put(ORDER_BY_SUFFIX, Arrays.asList(orderByFiler));
        FilterData pageCountFiler = new FilterData.FilterDataBuilder(PAGING_COUNT)
                        .build();
        filters.put(PAGING_COUNT_SUFFIX, Arrays.asList(pageCountFiler));
        LOG.debug("Before populate - Order history filters: {}", filters.toString());
        afterPoulateOrderHistoryFilters(filters, customerModel, status, paginationData, sortType);
        LOG.debug("After populate - Order history filters: {}", filters.toString());
        return filters;
    }


    @Override
    public Map<String, List<FilterData>> getOrderDetailFilters()
    {
        Map<String, List<FilterData>> expandFilters = new HashMap<>();
        List<FilterData> orderDetailsExpandFilter = new ArrayList<>();
        FilterData expandOrderItemPrice = new FilterData.FilterDataBuilder(EXPAND_PARAM_ITEM + "/" + EXPAND_PARAM_PRICING_ELEMENT)
                        .filterDataOperator(",")
                        .build();
        orderDetailsExpandFilter.add(expandOrderItemPrice);
        FilterData expandOrderPrice = new FilterData.FilterDataBuilder(EXPAND_PARAM_PRICING_ELEMENT)
                        .filterDataOperator(",")
                        .build();
        orderDetailsExpandFilter.add(expandOrderPrice);
        FilterData expandOrderPartner = new FilterData.FilterDataBuilder(EXPAND_PARAM_PARTNER)
                        .build();
        orderDetailsExpandFilter.add(expandOrderPartner);
        expandFilters.put(EXPAND_SUFFIX, orderDetailsExpandFilter);
        LOG.debug("Before populate - Expand filters: {}", expandFilters.toString());
        afterPoulateOrderDetailFilters(expandFilters);
        LOG.debug("After populate - Expand filters: {}", expandFilters.toString());
        return expandFilters;
    }


    protected void afterPoulateOrderHistoryFilters(Map<String, List<FilterData>> filters,
                    CustomerModel customerModel, OrderStatus[] status, PageableData paginationData, String sortType)
    {
        if(getSapS4OMOrderFilterBuilderHooks() != null)
        {
            for(final SapS4OMOrderFilterBuilderHook orderFilterBuilderHook : getSapS4OMOrderFilterBuilderHooks())
            {
                orderFilterBuilderHook.afterOrderHistoryFilter(filters, customerModel, status, paginationData, sortType);
            }
        }
    }


    protected void afterPoulateOrderDetailFilters(Map<String, List<FilterData>> filters)
    {
        if(getSapS4OMOrderFilterBuilderHooks() != null)
        {
            for(final SapS4OMOrderFilterBuilderHook orderFilterBuilderHook : getSapS4OMOrderFilterBuilderHooks())
            {
                orderFilterBuilderHook.afterOrderDetailsFilter(filters);
            }
        }
    }


    public SapS4OrderUtil getSapS4OrderUtil()
    {
        return sapS4OrderUtil;
    }


    public void setSapS4OrderUtil(SapS4OrderUtil sapS4OrderUtil)
    {
        this.sapS4OrderUtil = sapS4OrderUtil;
    }


    public List<SapS4OMOrderFilterBuilderHook> getSapS4OMOrderFilterBuilderHooks()
    {
        return sapS4OMOrderFilterBuilderHooks;
    }


    public void setSapS4OMOrderFilterBuilderHooks(
                    List<SapS4OMOrderFilterBuilderHook> sapS4OMOrderFilterBuilderHooks)
    {
        this.sapS4OMOrderFilterBuilderHooks = sapS4OMOrderFilterBuilderHooks;
    }


    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
