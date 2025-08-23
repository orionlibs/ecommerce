/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default Fulltext search strategy that works on type system. Each filter is named by items attributes and a type of
 * filters values are determined on basis of attribute's type,
 */
public class DefaultFullTextSearchStrategy implements FullTextSearchStrategy
{
    /**
     * Strategy name to be used in configuration when preferred search strategy is to be changed
     */
    public static final String STRATEGY_NAME = "flexible";
    private TypeFacade typeFacade;
    private AdvancedSearchOperatorService advancedSearchOperatorService;


    @Override
    public String getFieldType(final String typeCode, final String fieldName)
    {
        final DataAttribute attribute = getTypeFacade().getAttribute(typeCode, fieldName);
        return attribute != null ? attribute.getValueType().getCode() : Object.class.getName();
    }


    @Override
    public boolean isLocalized(final String typeCode, final String fieldName)
    {
        final DataAttribute attribute = getTypeFacade().getAttribute(typeCode, fieldName);
        return attribute != null && attribute.isLocalized();
    }


    @Override
    public Collection<String> getAvailableLanguages(final String typeCode)
    {
        return Arrays.stream(Locale.getAvailableLocales()).map(Locale::getLanguage).collect(Collectors.toList());
    }


    @Override
    public Collection<ValueComparisonOperator> getAvailableOperators(final String typeCode, final String fieldName)
    {
        final DataAttribute attribute = getTypeFacade().getAttribute(typeCode, fieldName);
        return attribute != null ? getAdvancedSearchOperatorService().getAvailableOperators(attribute) : Collections.emptyList();
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected AdvancedSearchOperatorService getAdvancedSearchOperatorService()
    {
        return advancedSearchOperatorService;
    }


    @Required
    public void setAdvancedSearchOperatorService(final AdvancedSearchOperatorService advancedSearchOperatorService)
    {
        this.advancedSearchOperatorService = advancedSearchOperatorService;
    }


    @Override
    public String getStrategyName()
    {
        return STRATEGY_NAME;
    }
}
