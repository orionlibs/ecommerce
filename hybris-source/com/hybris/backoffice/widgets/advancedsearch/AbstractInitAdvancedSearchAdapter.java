/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.util.AdvancedSearchDataUtil;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * Useful abstract class contains methods for fetching
 * {@link com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch} configuration and building
 * initial {@link com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData} based on this config Note: Your
 * custom advanced search adapter that initialize this widget should extends this class.
 */
public abstract class AbstractInitAdvancedSearchAdapter extends DefaultWidgetController implements AdvancedSearchInitializer
{
    public static final String SOCKET_IN_NODE_SELECTED = "nodeSelected";
    public static final String SOCKET_OUT_CONTEXT = "outContext";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractInitAdvancedSearchAdapter.class);
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient PermissionFacade permissionFacade;
    @WireVariable
    private transient AdvancedSearchOperatorService advancedSearchOperatorService;


    /**
     * Creates init context for advanced search widget
     *
     * @param navigationNode
     *           - navigation node
     */
    @SocketEvent(socketId = SOCKET_IN_NODE_SELECTED)
    public void createAdvancedSearchInitContext(final NavigationNode navigationNode)
    {
        if(navigationNode == null)
        {
            LOG.warn("navigationNode is null");
            return;
        }
        AdvancedSearchData searchData = null;
        AdvancedSearch config = null;
        if(Objects.equals(getNavigationNodeId(), navigationNode.getId()))
        {
            config = loadAdvancedConfiguration(getTypeCode());
            searchData = initFromConfig(config, getTypeCode());
            addSearchDataConditions(searchData, Optional.of(navigationNode));
        }
        final AdvancedSearchInitContext initContext = new AdvancedSearchInitContext(searchData, config);
        sendOutput(getOutputSocketName(), initContext);
    }


    protected AdvancedSearch loadAdvancedConfiguration(final String type)
    {
        final DefaultConfigContext context = new DefaultConfigContext("advanced-search", type.trim());
        try
        {
            return getWidgetInstanceManager().loadConfiguration(context, AdvancedSearch.class);
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.warn(String.format("Could not load advanced search configuration for type [%s] ", type), cce);
        }
        return null;
    }


    protected AdvancedSearchData initFromConfig(final AdvancedSearch advancedSearch, final String typeCode)
    {
        final FieldListType fieldList = advancedSearch.getFieldList();
        final AdvancedSearchData searchData = new AdvancedSearchData(fieldList);
        final DataType dataType = loadDataTypeForCode(typeCode);
        if(dataType == null)
        {
            return null;
        }
        if(fieldList != null)
        {
            for(final FieldType field : fieldList.getField())
            {
                if(permissionFacade.canReadProperty(dataType.getCode(), field.getName()) && field.isSelected())
                {
                    final DataAttribute dataAttr = dataType.getAttribute(field.getName());
                    AdvancedSearchDataUtil.tryAddCondition(advancedSearchOperatorService, dataType, searchData, field, dataAttr);
                }
            }
            searchData.setIncludeSubtypes(fieldList.isIncludeSubtypes());
        }
        searchData.setTypeCode(dataType.getCode());
        if(advancedSearch.getSortField() != null)
        {
            searchData.setSortData(new SortData(advancedSearch.getSortField().getName(), advancedSearch.getSortField().isAsc()));
        }
        searchData.setGlobalOperator(ValueComparisonOperator.AND);
        return searchData;
    }


    protected DataType loadDataTypeForCode(final String typeCode)
    {
        DataType dataType = null;
        if(StringUtils.isNotBlank(typeCode))
        {
            final String type = typeCode.trim();
            try
            {
                dataType = typeFacade.load(type);
            }
            catch(final TypeNotFoundException e)
            {
                LOG.error("Could not find type " + type, e);
            }
        }
        return dataType;
    }


    protected String getOutputSocketName()
    {
        return SOCKET_OUT_CONTEXT;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public AdvancedSearchOperatorService getAdvancedSearchOperatorService()
    {
        return advancedSearchOperatorService;
    }


    public void setAdvancedSearchOperatorService(final AdvancedSearchOperatorService advancedSearchOperatorService)
    {
        this.advancedSearchOperatorService = advancedSearchOperatorService;
    }


    /**
     * Returns navigation node identifier for which advanced search data context should be created
     *
     * @return navigation node identifier
     */
    public abstract String getNavigationNodeId();


    /**
     * Returns type for which advanced search data context will be created
     *
     * @return type identifier
     */
    public abstract String getTypeCode();
}
