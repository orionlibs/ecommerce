/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.search.builder.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.google.common.collect.Lists;
import com.hybris.backoffice.cockpitng.search.builder.ConditionQueryBuilder;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchQueryData;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SimpleSearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.enums.RelationEndCardinalityEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.core.model.type.ViewAttributeDescriptorModel;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * <p>
 * Query builder for a generic attribute.
 * </p>
 * <p/>
 * Following attributes are supported:
 * <p/>
 *
 * <pre>
 * 	<li/>atomics</li>
 * 	<li/>references</li>
 * 	<li/>enums</li>
 * 	</ul>
 * </pre>
 */
public class GenericConditionQueryBuilder implements ConditionQueryBuilder
{
    public static final String EDITOR_PARAM_COMPARES_EXACT_DATE = "comparesExactDate";
    public static final String EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE = "equalsComparesExactDate";
    public static final String EDITOR_PARAM_DATE_SEARCH_ROUNDING = "dateSearchRounding";
    private static final String PROPERTY_SIMPLESEARCH_CASESENSITIVE = "cockpit.search.simplesearch.casesensitive";
    private static final String PROPERTY_ADVANCEDSEARCH_CASESENSITIVE = "cockpit.search.advancedsearch.casesensitive";
    private static final Logger LOG = LoggerFactory.getLogger(GenericConditionQueryBuilder.class);
    private static final String EXACT = "exact";
    private static final String SECOND = "second";
    private static final String MINUTE = "minute";
    private static final String HOUR = "hour";
    private static final String DAY = "day";
    private static final String DEFAULT_DATE_ROUNDING_KEY = SECOND;
    private static final Map<String, Integer> DATE_ROUNDINGS = new HashMap<>();

    static
    {
        DATE_ROUNDINGS.put(EXACT, null);
        DATE_ROUNDINGS.put(SECOND, Calendar.SECOND);
        DATE_ROUNDINGS.put(MINUTE, Calendar.MINUTE);
        DATE_ROUNDINGS.put(HOUR, Calendar.HOUR_OF_DAY);
        DATE_ROUNDINGS.put(DAY, Calendar.DAY_OF_MONTH);
    }

    private TypeService typeService;
    private TypeFacade typeFacade;
    private ModelService modelService;
    private Set<Character> separators;


    private static boolean isCollectionType(final AttributeDescriptorModel attrDescriptorModel)
    {
        return attrDescriptorModel.getAttributeType() instanceof CollectionTypeModel;
    }


    private boolean isCaseSensitive(final SearchQueryData searchQueryData)
    {
        if(searchQueryData instanceof SimpleSearchQueryData)
        {
            return Config.getBoolean(PROPERTY_SIMPLESEARCH_CASESENSITIVE, false);
        }
        if(searchQueryData instanceof AdvancedSearchQueryData)
        {
            return searchQueryData.isCaseSensitive().orElseGet(() -> {
                final AdvancedSearchMode searchMode = ((AdvancedSearchQueryData)searchQueryData).getAdvancedSearchMode();
                if(searchMode == AdvancedSearchMode.SIMPLE)
                {
                    return Config.getBoolean(PROPERTY_SIMPLESEARCH_CASESENSITIVE, false);
                }
                if(searchMode == AdvancedSearchMode.ADVANCED)
                {
                    return Config.getBoolean(PROPERTY_ADVANCEDSEARCH_CASESENSITIVE, false);
                }
                return false;
            });
        }
        return false;
    }


    protected List<GenericCondition> buildQueryCondition(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final SearchQueryCondition searchQueryCondition, final SearchQueryData searchQueryData)
    {
        final List<GenericCondition> conditions = Lists.newArrayList();
        final Object value = searchQueryCondition.getValue();
        final ValueComparisonOperator attributeOperator = searchQueryCondition.getOperator();
        conditions.addAll(buildCondition(value, attributeOperator, searchQueryData, searchAttributeDescriptor));
        return conditions;
    }


    @Override
    public List<GenericCondition> buildQuery(final GenericQuery query, final String typeCode,
                    final SearchAttributeDescriptor searchAttributeDescriptor, final SearchQueryData searchQueryData)
    {
        final List<GenericCondition> conditions = Lists.newArrayList();
        final Object value = searchQueryData.getAttributeValue(searchAttributeDescriptor);
        final ValueComparisonOperator attributeOperator = searchQueryData.getValueComparisonOperator(searchAttributeDescriptor);
        conditions.addAll(buildCondition(value, attributeOperator, searchQueryData, searchAttributeDescriptor));
        return conditions;
    }


    @Override
    public List<GenericCondition> buildQuery(final GenericQuery query, final String typeCode, final SearchQueryCondition condition,
                    final SearchQueryData searchQueryData)
    {
        final List<GenericCondition> conditions = Lists.newArrayList();
        final Object value = condition.getValue();
        final ValueComparisonOperator attributeOperator = condition.getOperator();
        conditions.addAll(buildCondition(value, attributeOperator, searchQueryData, condition.getDescriptor()));
        return conditions;
    }


    protected List<GenericCondition> buildCondition(final Object value, final ValueComparisonOperator attributeOperator,
                    final SearchQueryData searchQueryData, final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        final List<GenericCondition> conditions = Lists.newArrayList();
        if(ValueComparisonOperator.isUnary(attributeOperator))
        {
            buildUnaryCondition(value, attributeOperator, searchQueryData, searchAttributeDescriptor).ifPresent(conditions::add);
        }
        else if(value instanceof String)
        {
            buildStringCondition((String)value, attributeOperator, searchQueryData, searchAttributeDescriptor)
                            .ifPresent(conditions::add);
        }
        else if(value != null)
        {
            final GenericCondition genericCondition = createSingleTokenCondition(searchQueryData, searchAttributeDescriptor, value,
                            attributeOperator);
            if(genericCondition != null)
            {
                conditions.add(genericCondition);
            }
        }
        return conditions;
    }


    protected Optional<GenericCondition> buildUnaryCondition(final Object value, final ValueComparisonOperator attributeOperator,
                    final SearchQueryData searchQueryData, final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        final String qualifier = searchAttributeDescriptor.getAttributeName();
        final String typeCode = searchQueryData.getSearchType();
        final AttributeDescriptorModel attDescriptorModel = typeService.getAttributeDescriptor(typeCode, qualifier);
        if(isSupportedRelationType(attDescriptorModel)
                        && BooleanUtils.isFalse(((RelationDescriptorModel)attDescriptorModel).getRelationType().getAbstract()))
        {
            return Optional.ofNullable(createSingleTokenCondition(searchQueryData, searchAttributeDescriptor, value));
        }
        else
        {
            final GenericCondition genericCondition = handleUnaryOperator(searchQueryData.getSearchType(),
                            searchAttributeDescriptor.getAttributeName(), attributeOperator, value);
            return Optional.ofNullable(genericCondition);
        }
    }


    protected Optional<GenericCondition> buildStringCondition(final String value, final ValueComparisonOperator attributeOperator,
                    final SearchQueryData searchQueryData, final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        final List<String> tokens;
        if(searchQueryData.isTokenizable())
        {
            tokens = splitQuery(value);
        }
        else
        {
            tokens = Lists.newArrayList(value);
        }
        if(tokens.size() > 1)
        {
            final GenericConditionList genericConditionList = GenericCondition.or();
            for(final String singleToken : tokens)
            {
                final GenericCondition genericCondition = createSingleTokenCondition(searchQueryData, searchAttributeDescriptor,
                                singleToken, attributeOperator);
                if(genericCondition != null)
                {
                    genericConditionList.addToConditionList(genericCondition);
                }
            }
            if(CollectionUtils.isNotEmpty(genericConditionList.getConditionList()))
            {
                return Optional.of(genericConditionList);
            }
            else
            {
                return Optional.empty();
            }
        }
        else
        {
            final Object tokenValue = CollectionUtils.isNotEmpty(tokens) ? tokens.iterator().next() : StringUtils.EMPTY;
            final GenericCondition genericCondition = createSingleTokenCondition(searchQueryData, searchAttributeDescriptor,
                            tokenValue, attributeOperator);
            return Optional.ofNullable(genericCondition);
        }
    }


    /**
     * @deprecated since 1808, use {@link ValueComparisonOperator#isUnary} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected boolean isUnaryOperator(final ValueComparisonOperator operator)
    {
        return ValueComparisonOperator.isUnary(operator);
    }


    protected GenericCondition createRelationCondition(final RelationDescriptorModel relationDescriptor, final String typeCode,
                    final String qualifier, final ValueComparisonOperator comparisonOperator, final Object value)
    {
        final GenericCondition ret;
        // 1:n relation
        if(BooleanUtils.isTrue(relationDescriptor.getRelationType().getAbstract()))
        {
            final RelationEndCardinalityEnum relationEndCardinality = getCardinality(relationDescriptor);
            final Operator operator = lookupRelationOperator(comparisonOperator, false);
            // 1 - end e.g. AbstractOrderEntry.order -> param must be an order
            if(relationEndCardinality.equals(RelationEndCardinalityEnum.MANY))
            {
                ret = createMany2OneRelationCondition(relationDescriptor, typeCode, qualifier, operator, value);
            }
            // n - end e.g. AbstractOrder.entries -> param must be an entry
            else
            {
                ret = createOne2ManyRelationCondition(relationDescriptor, typeCode, operator, value);
            }
        }
        // n:m
        else
        {
            final Operator operator = lookupRelationOperator(comparisonOperator, true);
            ret = createMany2ManyRelationCondition(relationDescriptor, typeCode, operator, value);
        }
        return ret;
    }


    protected RelationEndCardinalityEnum getCardinality(final RelationDescriptorModel relationDescriptorModel)
    {
        final RelationMetaTypeModel relationType = relationDescriptorModel.getRelationType();
        return relationDescriptorModel.getIsSource() ? relationType.getProperty("sourceTypeCardinality")
                        : relationType.getProperty("targetTypeCardinality");
    }


    protected GenericCondition createMany2OneRelationCondition(final RelationDescriptorModel relationDescriptor,
                    final String typeCode, final String qualifier, final Operator operator, final Object value)
    {
        return GenericCondition.createConditionForValueComparison(new GenericSearchField(typeCode, qualifier), operator, value);
    }


    protected GenericCondition createOne2ManyRelationCondition(final RelationDescriptorModel relationDescriptor,
                    final String typeCode, final Operator operator, final Object value)
    {
        final boolean isSource = BooleanUtils.isTrue(relationDescriptor.getIsSource());
        final RelationMetaTypeModel relationType = relationDescriptor.getRelationType();
        final ComposedTypeModel otherOneType = isSource ? relationType.getTargetType() : relationType.getSourceType();
        final String otherOneQualifier = isSource ? relationType.getSourceTypeRole() : relationType.getTargetTypeRole();
        final String otherTypeCode = otherOneType.getCode();
        final GenericQuery subQuery = new GenericQuery(otherTypeCode);
        // read foreign key field in subselect
        subQuery.addSelectField(new GenericSelectField(otherTypeCode, otherOneQualifier, ItemPropertyValue.class));
        // limit subselect to argument item
        subQuery.addCondition(GenericCondition.equals(new GenericSearchField(otherTypeCode, ItemModel.PK), value));
        // get items which match the foreign key value from subselect
        return GenericCondition.createSubQueryCondition(new GenericSearchField(typeCode, ItemModel.PK), operator, subQuery);
    }


    protected GenericCondition createMany2ManyRelationCondition(final RelationDescriptorModel relationDescriptor,
                    final String typeCode, final Operator operator, final Object value)
    {
        final GenericQuery subQuery = buildMany2ManyQuery(relationDescriptor, value, operator);
        if(Operator.NOT_EXISTS.equals(operator) || Operator.EXISTS.equals(operator))
        {
            return GenericCondition.createSubQueryCondition(new GenericSearchField(typeCode, ItemModel.PK),
                            Operator.NOT_EXISTS.equals(operator) ? Operator.NOT_IN : Operator.IN, subQuery);
        }
        else
        {
            return GenericCondition.createSubQueryCondition(new GenericSearchField(typeCode, ItemModel.PK), operator, subQuery);
        }
    }


    protected GenericQuery buildMany2ManyQuery(final RelationDescriptorModel relationDescriptor, final Object value,
                    final Operator operator)
    {
        final boolean isSource = BooleanUtils.isTrue(relationDescriptor.getIsSource());
        final String relationTypeCode = relationDescriptor.getRelationType().getCode();
        final GenericQuery subQuery = new GenericQuery(relationTypeCode);
        subQuery.addSelectField(
                        new GenericSelectField(relationTypeCode, isSource ? LinkModel.SOURCE : LinkModel.TARGET, ItemPropertyValue.class));
        if(Operator.NOT_EXISTS.equals(operator) || Operator.EXISTS.equals(operator))
        {
            return subQuery;
        }
        else if(value != null)
        {
            final String conditionQualifier = isSource ? LinkModel.TARGET : LinkModel.SOURCE;
            if(Operator.IN.equals(operator) || Operator.NOT_IN.equals(operator))
            {
                final Collection<?> objects = value instanceof Collection ? (Collection<?>)value : Collections.singletonList(value);
                if(objects.isEmpty())
                {
                    throw new IllegalArgumentException("Passed empty collection for IN operator. Such condition is invalid.");
                }
                subQuery.addCondition(GenericCondition.in(new GenericSearchField(relationTypeCode, conditionQualifier), objects));
            }
            else
            {
                subQuery.addCondition(GenericCondition.equals(new GenericSearchField(relationTypeCode, conditionQualifier), value));
            }
        }
        return subQuery;
    }


    private Operator lookupRelationOperator(final ValueComparisonOperator comparisonOperator, final boolean many2manyRelation)
    {
        switch(comparisonOperator)
        {
            case IN:
            case CONTAINS:
                return Operator.IN;
            case NOT_IN:
            case DOES_NOT_CONTAIN:
                return Operator.NOT_IN;
            case EQUALS:
                return Operator.EQUAL;
            case IS_EMPTY:
                return many2manyRelation ? Operator.NOT_IN : Operator.IS_NULL;
            case IS_NOT_EMPTY:
                return many2manyRelation ? Operator.IN : Operator.IS_NOT_NULL;
            default:
                throw new IllegalStateException("Unsupported operator " + comparisonOperator + " for references");
        }
    }


    protected GenericCondition createSingleTokenCondition(final SearchQueryData searchQueryData,
                    final SearchAttributeDescriptor searchAttributeDescriptor, final Object value)
    {
        return createSingleTokenCondition(searchQueryData, searchAttributeDescriptor, value, null);
    }


    protected GenericCondition createSingleTokenCondition(final SearchQueryData searchQueryData,
                    final SearchAttributeDescriptor searchAttributeDescriptor, final Object value,
                    final ValueComparisonOperator givenOperator)
    {
        validateParameterNotNull(searchQueryData, "Parameter 'searchQueryData' must not be null!");
        validateParameterNotNull(searchQueryData.getSearchType(), "Parameter 'searchQueryData.searchType' must not be empty!");
        validateParameterNotNull(searchAttributeDescriptor, "Parameter 'searchAttributeDescriptor' must not be null!");
        final String qualifier = searchAttributeDescriptor.getAttributeName();
        final String typeCode = searchQueryData.getSearchType();
        final ValueComparisonOperator operator = givenOperator != null ? givenOperator
                        : searchQueryData.getValueComparisonOperator(searchAttributeDescriptor);
        GenericCondition ret = null;
        final AttributeDescriptorModel attDescriptorModel = typeService.getAttributeDescriptor(typeCode, qualifier);
        if(isSupportedRelationType(attDescriptorModel))
        {
            ret = createRelationCondition((RelationDescriptorModel)attDescriptorModel, typeCode, qualifier, operator, value);
        }
        else if(isCollectionType(attDescriptorModel))
        {
            LOG.warn("Collection types aren't supported now by Field Search Facade!");
        }
        else
        {
            final Object extractedValue = extractRequiredValue(value, attDescriptorModel);
            final boolean isViewType = typeService.getComposedTypeForCode(searchQueryData.getSearchType()) instanceof ViewTypeModel;
            if(operator == null)
            {
                ret = GenericCondition.equals(new GenericSearchField(typeCode, qualifier), value);
            }
            else if("contains".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(value instanceof String)
                {
                    ret = isCaseSensitive(searchQueryData)
                                    ? GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, qualifier),
                                    prepare((String)value, "%", "%", false))
                                    : GenericCondition.like(new GenericSearchField(typeCode, qualifier),
                                                    prepare((String)value, "%", "%", false));
                }
                else if(value instanceof List)
                {
                    ret = GenericCondition.createConditionForValueComparison(new GenericSearchField(typeCode, qualifier), Operator.IN,
                                    value);
                }
                else
                {
                    LOG.warn("{} is not supported with CONTAINS operator.", value.getClass().getCanonicalName());
                }
            }
            else if("doesNotContain".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(value instanceof String)
                {
                    ret = isCaseSensitive(searchQueryData)
                                    ? GenericCondition.caseSensitiveNotLike(new GenericSearchField(typeCode, qualifier),
                                    prepare((String)value, "%", "%", false))
                                    : GenericCondition.notLike(new GenericSearchField(typeCode, qualifier),
                                                    prepare((String)value, "%", "%", false));
                }
                else if(value instanceof List)
                {
                    ret = GenericCondition.createConditionForValueComparison(new GenericSearchField(typeCode, qualifier),
                                    Operator.NOT_IN, value);
                }
                else
                {
                    LOG.warn("{} is not supported with DOES_NOT_CONTAIN operator.", value.getClass().getCanonicalName());
                }
            }
            else if("startswith".equalsIgnoreCase(operator.getOperatorCode()))
            {
                ret = isCaseSensitive(searchQueryData)
                                ? GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, qualifier),
                                prepare((String)value, null, "%", false))
                                : GenericCondition.like(new GenericSearchField(typeCode, qualifier), prepare((String)value, null, "%", false));
            }
            else if("endswith".equalsIgnoreCase(operator.getOperatorCode()))
            {
                ret = isCaseSensitive(searchQueryData)
                                ? GenericCondition.caseSensitiveLike(new GenericSearchField(typeCode, qualifier),
                                prepare((String)value, "%", null, false))
                                : GenericCondition.like(new GenericSearchField(typeCode, qualifier), prepare((String)value, "%", null, false));
            }
            else if("like".equalsIgnoreCase(operator.getOperatorCode()))
            {
                ret = GenericCondition.like(new GenericSearchField(typeCode, qualifier), prepare((String)value, null, null, false));
            }
            else if("equals".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(extractedValue instanceof Date && !isViewType)
                {
                    ret = resolveDateEqualsCondition(searchAttributeDescriptor, qualifier, typeCode, (Date)extractedValue);
                }
                else
                {
                    ret = GenericCondition.equals(new GenericSearchField(typeCode, qualifier), extractedValue);
                }
            }
            else if("unequal".equalsIgnoreCase(operator.getOperatorCode()))
            {
                ret = GenericCondition.notEquals(new GenericSearchField(typeCode, qualifier),
                                extractRequiredValue(value, attDescriptorModel));
            }
            else if("greater".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(extractedValue instanceof Date && !isViewType)
                {
                    ret = resolveDateGreaterCondition(searchAttributeDescriptor, qualifier, typeCode, (Date)extractedValue);
                }
                else
                {
                    ret = GenericCondition.greater(new GenericSearchField(typeCode, qualifier), value);
                }
            }
            else if("less".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(extractedValue instanceof Date && !isViewType)
                {
                    ret = resolveDateLessCondition(searchAttributeDescriptor, qualifier, typeCode, (Date)extractedValue);
                }
                else
                {
                    ret = GenericCondition.less(new GenericSearchField(typeCode, qualifier), value);
                }
            }
            else if("greaterOrEquals".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(extractedValue instanceof Date && !isViewType)
                {
                    ret = resolveDateGreaterOrEqualsCondition(searchAttributeDescriptor, qualifier, typeCode, (Date)extractedValue);
                }
                else
                {
                    ret = GenericCondition.greaterOrEqual(new GenericSearchField(typeCode, qualifier), value);
                }
            }
            else if("lessOrEquals".equalsIgnoreCase(operator.getOperatorCode()))
            {
                if(extractedValue instanceof Date && !isViewType)
                {
                    ret = resolveDateLessOrEqualsCondition(searchAttributeDescriptor, qualifier, typeCode, (Date)extractedValue);
                }
                else
                {
                    ret = GenericCondition.lessOrEqual(new GenericSearchField(typeCode, qualifier), value);
                }
            }
            else if("isEmpty".equalsIgnoreCase(operator.getOperatorCode()))
            {
                final GenericSearchField field = new GenericSearchField(typeCode, qualifier);
                if(isStringTypeAttribute(typeCode, qualifier))
                {
                    ret = GenericCondition.or(Arrays.asList(GenericCondition.createIsNullCondition(field),
                                    GenericCondition.equals(field, StringUtils.EMPTY)));
                }
                else
                {
                    ret = GenericCondition.createIsNullCondition(field);
                }
            }
            else if("isNotEmpty".equalsIgnoreCase(operator.getOperatorCode()))
            {
                final GenericSearchField field = new GenericSearchField(typeCode, qualifier);
                if(isStringTypeAttribute(typeCode, qualifier))
                {
                    ret = GenericCondition.and(Arrays.asList(GenericCondition.createIsNotNullCondition(field),
                                    GenericCondition.notEquals(field, StringUtils.EMPTY)));
                }
                else
                {
                    ret = GenericCondition.createIsNotNullCondition(field);
                }
            }
            else if("between".equalsIgnoreCase(operator.getOperatorCode()))
            {
                final List<GenericCondition> conditions = new ArrayList<>();
                final GenericSearchField field = new GenericSearchField(typeCode, qualifier);
                if(value != null)
                {
                    conditions.add(GenericCondition.greaterOrEqual(field, value));
                }
                if(conditions.size() > 1)
                {
                    ret = GenericCondition.and(conditions);
                }
                else if(conditions.size() == 1)
                {
                    ret = conditions.get(0);
                }
            }
            else if("in".equals(operator.getOperatorCode()))
            {
                final GenericSearchField field = new GenericSearchField(typeCode, qualifier);
                ret = GenericCondition.createConditionForValueComparison(field, Operator.IN, value);
            }
            else if("notIn".equals(operator.getOperatorCode()))
            {
                final GenericSearchField field = new GenericSearchField(typeCode, qualifier);
                ret = GenericCondition.createConditionForValueComparison(field, Operator.NOT_IN, value);
            }
            else
            {
                throw new IllegalArgumentException("Unsupported operator " + operator + " for descriptor " + this);
            }
        }
        return ret;
    }


    protected Date getUpToDate(final Date date, final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        return getUpToDate(date, searchAttributeDescriptor, false);
    }


    protected Date getUpToDate(final Date date, final SearchAttributeDescriptor searchAttributeDescriptor,
                    final boolean hasEqualsComparesExactDateParameter)
    {
        final Optional<Integer> dateRounding = getDateRounding(searchAttributeDescriptor);
        if(dateRounding.isPresent())
        {
            final Date dateSince = getSinceDate(date, searchAttributeDescriptor, hasEqualsComparesExactDateParameter);
            final boolean isExactDate = hasEqualsComparesExactDateParameter ? isEqualsComparesExactDate(searchAttributeDescriptor)
                            : isComparesExactDate(searchAttributeDescriptor);
            if(isExactDate)
            {
                return getRoundedDate(dateSince, searchAttributeDescriptor);
            }
            else
            {
                return DateUtils.addDays(dateSince, 1);
            }
        }
        return date;
    }


    protected Date getRoundedDate(final Date date, final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        final Optional<Integer> dateRounding = getDateRounding(searchAttributeDescriptor);
        return dateRounding.map(rounding -> {
            switch(rounding)
            {
                case Calendar.SECOND:
                    return DateUtils.addSeconds(date, 1);
                case Calendar.MINUTE:
                    return DateUtils.addMinutes(date, 1);
                case Calendar.HOUR_OF_DAY:
                    return DateUtils.addHours(date, 1);
                case Calendar.DAY_OF_MONTH:
                    return DateUtils.addDays(date, 1);
                default:
                    return DateUtils.addSeconds(date, 1);
            }
        }).orElse(DateUtils.addDays(date, 1));
    }


    protected Date getSinceDate(final Date date, final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        return getSinceDate(date, searchAttributeDescriptor, false);
    }


    protected Date getSinceDate(final Date date, final SearchAttributeDescriptor searchAttributeDescriptor,
                    final boolean hasEqualsComparesExactDateParameter)
    {
        final Optional<Integer> dateRounding = getDateRounding(searchAttributeDescriptor);
        if(hasEqualsComparesExactDateParameter)
        {
            return dateRounding.map(rounding -> DateUtils.truncate(date,
                            isEqualsComparesExactDate(searchAttributeDescriptor) ? rounding : Calendar.MINUTE)).orElse(date);
        }
        return dateRounding.map(rounding -> DateUtils.truncate(date,
                        isComparesExactDate(searchAttributeDescriptor) ? rounding : Calendar.MINUTE)).orElse(date);
    }


    protected boolean isEqualsComparesExactDate(final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        return BooleanUtils.isTrue(BooleanUtils
                        .toBooleanObject(searchAttributeDescriptor.getEditorParameters().get(EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE)));
    }


    protected boolean isComparesExactDate(final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        return BooleanUtils.isTrue(
                        BooleanUtils.toBooleanObject(searchAttributeDescriptor.getEditorParameters().get(EDITOR_PARAM_COMPARES_EXACT_DATE)));
    }


    protected Optional<Integer> getDateRounding(final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        final String editorParameterDateSearchRounding = searchAttributeDescriptor.getEditorParameters()
                        .get(EDITOR_PARAM_DATE_SEARCH_ROUNDING);
        final Integer rounding = Optional.ofNullable(editorParameterDateSearchRounding).map(String::toLowerCase)
                        .map(DATE_ROUNDINGS::get).orElse(DATE_ROUNDINGS.get(DEFAULT_DATE_ROUNDING_KEY));
        return Optional.ofNullable(rounding);
    }


    protected GenericCondition resolveDateEqualsCondition(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final String qualifier, final String typeCode, final Date extractedValue)
    {
        final Optional<Integer> dateRounding = getDateRounding(searchAttributeDescriptor);
        if(dateRounding.isPresent())
        {
            final boolean hasEqualsComparesExactDateParameter = searchAttributeDescriptor.getEditorParameters()
                            .get(EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE) != null;
            final Date dateSince = getSinceDate(extractedValue, searchAttributeDescriptor, hasEqualsComparesExactDateParameter);
            final Date dateUpTo = getUpToDate(extractedValue, searchAttributeDescriptor, hasEqualsComparesExactDateParameter);
            return GenericCondition.and(GenericCondition.greaterOrEqual(new GenericSearchField(typeCode, qualifier), dateSince),
                            GenericCondition.less(new GenericSearchField(typeCode, qualifier), dateUpTo));
        }
        return GenericCondition.equals(new GenericSearchField(typeCode, qualifier), extractedValue);
    }


    protected GenericCondition resolveDateGreaterCondition(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final String qualifier, final String typeCode, final Date extractedValue)
    {
        final Date dateUpTo = getUpToDate(extractedValue, searchAttributeDescriptor);
        return GenericCondition.greaterOrEqual(new GenericSearchField(typeCode, qualifier), dateUpTo);
    }


    protected GenericCondition resolveDateLessCondition(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final String qualifier, final String typeCode, final Date extractedValue)
    {
        final Date dateSince = getSinceDate(extractedValue, searchAttributeDescriptor);
        return GenericCondition.less(new GenericSearchField(typeCode, qualifier), dateSince);
    }


    protected GenericCondition resolveDateGreaterOrEqualsCondition(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final String qualifier, final String typeCode, final Date extractedValue)
    {
        final Date dateSince = getSinceDate(extractedValue, searchAttributeDescriptor);
        return GenericCondition.greaterOrEqual(new GenericSearchField(typeCode, qualifier), dateSince);
    }


    protected GenericCondition resolveDateLessOrEqualsCondition(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final String qualifier, final String typeCode, final Date extractedValue)
    {
        final Date dateUpTo = getUpToDate(extractedValue, searchAttributeDescriptor);
        return GenericCondition.less(new GenericSearchField(typeCode, qualifier), dateUpTo);
    }


    private Object extractRequiredValue(final Object value, final AttributeDescriptorModel attDescriptorModel)
    {
        if(attDescriptorModel instanceof ViewAttributeDescriptorModel
                        && (value instanceof ItemModel || value instanceof HybrisEnumValue))
        {
            return modelService.getSource(value);
        }
        return value;
    }


    protected GenericCondition handleUnaryOperator(final String typeCode, final String attributeName,
                    final ValueComparisonOperator operator, final Object value)
    {
        GenericConditionList genericConditionList = null;
        final GenericSearchField field = new GenericSearchField(typeCode, attributeName);
        if(isStringTypeAttribute(typeCode, attributeName))
        {
            if(ValueComparisonOperator.IS_EMPTY.getOperatorCode().equalsIgnoreCase(operator.getOperatorCode()))
            {
                genericConditionList = GenericCondition.or(Arrays.asList(GenericCondition.createIsNullCondition(field),
                                GenericCondition.equals(field, StringUtils.EMPTY)));
            }
            else if(ValueComparisonOperator.IS_NOT_EMPTY.getOperatorCode().equalsIgnoreCase(operator.getOperatorCode()))
            {
                genericConditionList = GenericCondition.and(Arrays.asList(GenericCondition.createIsNotNullCondition(field),
                                GenericCondition.notEquals(field, StringUtils.EMPTY)));
            }
        }
        else
        {
            if(ValueComparisonOperator.IS_EMPTY.getOperatorCode().equalsIgnoreCase(operator.getOperatorCode()))
            {
                return GenericCondition.createIsNullCondition(field);
            }
            else if(ValueComparisonOperator.IS_NOT_EMPTY.getOperatorCode().equalsIgnoreCase(operator.getOperatorCode()))
            {
                return GenericCondition.createIsNotNullCondition(field);
            }
        }
        return genericConditionList;
    }


    private boolean isStringTypeAttribute(final String typeCode, final String attributeName)
    {
        final DataAttribute attribute = typeFacade.getAttribute(typeCode, attributeName);
        return attribute.getValueType().equals(DataType.STRING);
    }


    protected boolean isSupportedRelationType(final AttributeDescriptorModel attrDescriptorModel)
    {
        boolean isSupportedCollectionType = false;
        if(attrDescriptorModel instanceof RelationDescriptorModel)
        {
            final RelationDescriptorModel relationDescriptionModel = (RelationDescriptorModel)attrDescriptorModel;
            isSupportedCollectionType = relationDescriptionModel.getSearch().booleanValue();
        }
        return isSupportedCollectionType;
    }


    protected boolean isMany2ManyRelationAttribute(final AttributeDescriptorModel attrDescriptorModel)
    {
        boolean ret = false;
        if(attrDescriptorModel instanceof RelationDescriptorModel)
        {
            final RelationDescriptorModel relationDescriptionModel = (RelationDescriptorModel)attrDescriptorModel;
            ret = BooleanUtils.isNotTrue(relationDescriptionModel.getRelationType().getAbstract());
        }
        return ret;
    }


    protected String prepare(final String token, final String prefix, final String postfix, final boolean exact)
    {
        String tmp = exact ? token.trim() : token.trim().replace('*', '%').replace('?', '_');
        if(prefix != null && !tmp.startsWith(prefix))
        {
            tmp = prefix + tmp;
        }
        if(postfix != null && !tmp.endsWith(postfix))
        {
            tmp = tmp + postfix;
        }
        return tmp;
    }


    /**
     * Helper method to split a single line of text into several tokens.
     *
     * @param query
     *           the single line of text
     */
    protected List<String> splitQuery(final String query)
    {
        final List<String> ret = new ArrayList<>();
        int last = 0;
        int index = 0;
        boolean block = false;
        for(final int s = query.length(); index < s; index++)
        {
            final char character = query.charAt(index);
            if(character == '"')
            {
                if(block)
                {
                    final String tmp = query.substring(last, index).trim();
                    if(tmp.length() > 0)
                    {
                        ret.add(tmp);
                    }
                    block = false;
                    last = index + 1; // skip "
                }
                else
                {
                    block = true;
                    if(index > last)
                    {
                        final String tmp = query.substring(last, index).trim();
                        if(tmp.length() > 0)
                        {
                            ret.add(tmp);
                        }
                    }
                    last = index + 1; // skip "
                }
            }
            else if(!block && separators.contains(Character.valueOf(character)))
            {
                final String tmp = query.substring(last, index).trim();
                if(tmp.length() > 0)
                {
                    ret.add(tmp);
                }
                last = index + 1;
            }
        }
        if(last < query.length())
        {
            final String tmp = query.substring(last).trim();
            if(tmp.length() > 0)
            {
                ret.add(tmp);
            }
        }
        return ret;
    }


    public TypeService getTypeService()
    {
        return typeService;
    }


    @Required
    public void setTypeService(final TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setSeparators(final Set<Character> separators)
    {
        this.separators = separators;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}
