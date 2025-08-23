/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.search.builder.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericFieldCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchFieldType;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Extended query builder version that is responsible for construction proper condition(s) for localized attribute.
 * </p>
 */
public class LocalizedGenericConditionQueryBuilder extends GenericConditionQueryBuilder
{
    private CommonI18NService commonI18NService;
    private I18NService i18nService;
    private LocalizationService localizationService;


    protected List<Pair<LanguageModel, Object>> getCurrentLocaleValues(final Object value)
    {
        final List<Pair<LanguageModel, Object>> ret = Lists.newArrayList();
        if(value instanceof Map)
        {
            for(final Entry<Locale, Object> entry : ((Map<Locale, Object>)value).entrySet())
            {
                if(entry.getKey() != null)
                {
                    final var locale = entry.getKey();
                    final var languageModel = retrieveLanguageModel(locale);
                    if(languageModel != null)
                    {
                        ret.add(Pair.of(languageModel, entry.getValue()));
                    }
                }
            }
        }
        else
        {
            ret.add(Pair.of(retrieveLanguageModel(i18nService.getCurrentLocale()), value));
        }
        return ret;
    }


    @Override
    protected GenericCondition createSingleTokenCondition(final SearchQueryData searchQueryData,
                    final SearchAttributeDescriptor qualifier, final Object value, final ValueComparisonOperator givenOperator)
    {
        validateParameterNotNull(searchQueryData, "Parameter 'searchQueryData' must not be null!");
        validateParameterNotNull(searchQueryData.getSearchType(), "Parameter 'searchQueryData.searchType' must not be null!");
        GenericConditionList ret = null;
        final List<Pair<LanguageModel, Object>> langValuePairs = getCurrentLocaleValues(value);
        final ValueComparisonOperator globalOperator = searchQueryData.getGlobalComparisonOperator();
        final String typeCode = searchQueryData.getSearchType();
        final String attributeName = qualifier.getAttributeName();
        final AttributeDescriptorModel attributeDescriptorModel = getTypeService().getAttributeDescriptor(typeCode, attributeName);
        final boolean many2ManyRelationAttribute = isMany2ManyRelationAttribute(attributeDescriptorModel);
        for(final Pair<LanguageModel, Object> langValuePair : langValuePairs)
        {
            GenericCondition genericField = null;
            if(langValuePair != null)
            {
                if(many2ManyRelationAttribute)
                {
                    genericField = super.createSingleTokenCondition(searchQueryData, qualifier, langValuePair, givenOperator);
                }
                else if(langValuePair.getRight() != null)
                {
                    final var genericFieldCondition = (GenericFieldCondition)super.createSingleTokenCondition(
                                    searchQueryData, qualifier, langValuePair.getRight(), givenOperator);
                    makeUpLanguageCondition(genericFieldCondition, langValuePair);
                    genericField = genericFieldCondition;
                }
                if(genericField != null)
                {
                    ret = globalOperator == ValueComparisonOperator.OR ? GenericCondition.or() : GenericCondition.and();
                    ret.addToConditionList(genericField);
                }
            }
        }
        return ret;
    }


    @Override
    protected GenericCondition handleUnaryOperator(final String typeCode, final String attributeName,
                    final ValueComparisonOperator operator, final Object value)
    {
        Pair<LanguageModel, Object> langValuePair = null;
        final List<Pair<LanguageModel, Object>> langValuePairs = getCurrentLocaleValues(value);
        final GenericCondition genericCondition = super.handleUnaryOperator(typeCode, attributeName, operator, value);
        if(CollectionUtils.isNotEmpty(langValuePairs))
        {
            langValuePair = langValuePairs.iterator().next();
        }
        if(langValuePair != null)
        {
            handleUnaryOperatorWithLang(genericCondition, langValuePair);
        }
        return genericCondition;
    }


    private void handleUnaryOperatorWithLang(GenericCondition genericCondition, Pair<LanguageModel, Object> langValuePair)
    {
        makeUpLanguageCondition(genericCondition, langValuePair);
        if(genericCondition instanceof GenericConditionList)
        {
            final var genericConditionList = (GenericConditionList)genericCondition;
            for(var condition : genericConditionList.getConditionList())
            {
                makeUpLanguageCondition(condition, langValuePair);
            }
        }
    }


    private void makeUpLanguageCondition(GenericCondition genericCondition, Pair<LanguageModel, Object> langValuePair)
    {
        if(genericCondition instanceof GenericFieldCondition)
        {
            final var genericFieldCondition = (GenericFieldCondition)genericCondition;
            genericFieldCondition.getField().setLanguagePK(langValuePair.getLeft().getPk());
            genericFieldCondition.getField().addFieldType(GenericSearchFieldType.LOCALIZED);
            genericFieldCondition.getField().addFieldType(GenericSearchFieldType.OUTER_JOIN);
        }
    }


    @Override
    protected GenericCondition createMany2ManyRelationCondition(final RelationDescriptorModel relationDescriptor,
                    final String typeCode, final Operator operator, final Object value)
    {
        if(!(value instanceof Pair))
        {
            return null;
        }
        final Pair<LanguageModel, Object> localValue = (Pair<LanguageModel, Object>)value;
        final GenericQuery subQuery = buildMany2ManyQuery(relationDescriptor, localValue.getRight(), operator);
        subQuery.addCondition(GenericCondition.equals(
                        new GenericSearchField(relationDescriptor.getRelationType().getCode(), LinkModel.LANGUAGE), localValue.getLeft()));
        return GenericCondition.createSubQueryCondition(new GenericSearchField(typeCode, ItemModel.PK), operator, subQuery);
    }


    private LanguageModel retrieveLanguageModel(final Locale locale)
    {
        try
        {
            return commonI18NService.getLanguage(localizationService.getDataLanguageIsoCode(locale));
        }
        catch(final UnknownIdentifierException e)
        {
            return commonI18NService.getLanguage(locale.toString());
        }
    }


    public void setI18nService(final I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public void setLocalizationService(final LocalizationService localizationService)
    {
        this.localizationService = localizationService;
    }
}
