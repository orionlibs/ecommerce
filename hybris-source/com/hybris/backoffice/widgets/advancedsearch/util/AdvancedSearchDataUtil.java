/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.util;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.ConnectionOperatorType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.search.impl.FieldSearchFacadeStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class AdvancedSearchDataUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchDataUtil.class);
    private FieldSearchFacadeStrategyRegistry fieldSearchFacadeStrategyRegistry;
    @WireVariable
    private PermissionFacade permissionFacade;
    @WireVariable
    private AdvancedSearchOperatorService advancedSearchOperatorService;


    public AdvancedSearchData buildAdvancedSearchData(final AdvancedSearch advancedSearch, final DataType dataType)
    {
        Validate.notNull("Configuration may not be null", advancedSearch);
        Validate.notNull("Data type may not be null", dataType);
        final AdvancedSearchData model = new AdvancedSearchData(advancedSearch.getFieldList());
        model.setAdvancedSearchMode(AdvancedSearchMode.ADVANCED);
        final FieldListType fieldList = advancedSearch.getFieldList();
        if(fieldList != null)
        {
            for(final FieldType field : fieldList.getField())
            {
                if(permissionFacade.canReadProperty(dataType.getCode(), field.getName())
                                && (field.isSelected() || field.isMandatory()))
                {
                    final DataAttribute dataAttr = dataType.getAttribute(field.getName());
                    tryAddCondition(advancedSearchOperatorService, dataType, model, field, dataAttr);
                }
            }
        }
        model.setTypeCode(dataType.getCode());
        if(advancedSearch.getSortField() != null)
        {
            model.setSortData(new SortData(advancedSearch.getSortField().getName(), advancedSearch.getSortField().isAsc()));
        }
        if(fieldList != null)
        {
            model.setIncludeSubtypes(fieldList.isIncludeSubtypes());
        }
        if(advancedSearch.getConnectionOperator() != null)
        {
            model.setGlobalOperator(
                            Objects.equals(advancedSearch.getConnectionOperator(), ConnectionOperatorType.OR) ? ValueComparisonOperator.OR
                                            : ValueComparisonOperator.AND);
        }
        else
        {
            model.setGlobalOperator(ValueComparisonOperator.OR);
        }
        return model;
    }


    public static void tryAddCondition(final AdvancedSearchOperatorService advancedSearchOperatorService, final DataType dataType,
                    final AdvancedSearchData model, final FieldType field, final DataAttribute dataAttr)
    {
        if(dataAttr != null && dataAttr.isSearchable())
        {
            Validate.notNull(String.format("Cannot find attribute = %s for type = %s ", field.getName(), dataType.getCode()),
                            dataAttr);
            final ValueComparisonOperator operator = advancedSearchOperatorService.findMatchingOperator(dataAttr,
                            field.getOperator());
            if(operator == null)
            {
                LOG.warn("Operator {} is not recognized. Skipping.", field.getName());
                return;
            }
            model.addCondition(field, operator, null);
        }
    }


    public boolean useOrForGlobalOperator(final String typeCode)
    {
        final FieldSearchFacadeStrategy fieldSearchFacadeStrategy = this.fieldSearchFacadeStrategyRegistry.getStrategy(typeCode);
        if(fieldSearchFacadeStrategy == null)
        {
            return false;
        }
        else
        {
            return fieldSearchFacadeStrategy.useOrForGlobalOperator();
        }
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setAdvancedSearchOperatorService(final AdvancedSearchOperatorService advancedSearchOperatorService)
    {
        this.advancedSearchOperatorService = advancedSearchOperatorService;
    }


    public void setFieldSearchFacadeStrategyRegistry(final FieldSearchFacadeStrategyRegistry fieldSearchFacadeStrategyRegistry)
    {
        this.fieldSearchFacadeStrategyRegistry = fieldSearchFacadeStrategyRegistry;
    }
}


