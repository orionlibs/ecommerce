package de.hybris.platform.platformbackoffice.services.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.Parameter;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.dao.BackofficeSavedQueryDAO;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSearchConditionModel;
import de.hybris.platform.platformbackoffice.services.BackofficeSavedQueriesService;
import de.hybris.platform.platformbackoffice.services.converters.BackofficeSavedQueryValueConverter;
import de.hybris.platform.platformbackoffice.services.converters.SavedQueryValue;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeSavedQueriesServiceImpl implements BackofficeSavedQueriesService
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSavedQueriesServiceImpl.class);
    private BackofficeSavedQueryDAO backofficeSavedQueryDAO;
    private ModelService modelService;
    private TypeFacade typeFacade;
    private List<BackofficeSavedQueryValueConverter> savedQueryValueConverters;


    public List<BackofficeSavedQueryModel> getSavedQueries(UserModel userModel)
    {
        return this.backofficeSavedQueryDAO.findSavedQueries(userModel);
    }


    public BackofficeSavedQueryModel createSavedQuery(Map<Locale, String> queryName, AdvancedSearchData asd, UserModel userModel, List<UserGroupModel> groups)
    {
        BackofficeSavedQueryModel queryModel = (BackofficeSavedQueryModel)this.modelService.create(BackofficeSavedQueryModel.class);
        queryName.entrySet().forEach(locQueryName -> {
            if(StringUtils.isNotBlank((CharSequence)locQueryName.getValue()))
            {
                queryModel.setName((String)locQueryName.getValue(), (Locale)locQueryName.getKey());
            }
        });
        queryModel.setQueryOwner(userModel);
        queryModel.setUserGroups(groups);
        queryModel.setTypeCode(asd.getTypeCode());
        queryModel.setGlobalOperatorCode(asd.getGlobalOperator().getOperatorCode());
        queryModel.setIncludeSubtypes(asd.getIncludeSubtypes());
        queryModel.setTokenizable(Boolean.valueOf(asd.isTokenizable()));
        queryModel.setSearchMode(
                        (asd.getAdvancedSearchMode() != null) ? asd.getAdvancedSearchMode().name() : AdvancedSearchMode.ADVANCED.name());
        if(asd.getSortData() != null)
        {
            queryModel.setSortAttribute(asd.getSortData().getSortAttribute());
            queryModel.setSortAsc(Boolean.valueOf(asd.getSortData().isAscending()));
        }
        List<BackofficeSearchConditionModel> conditions = extractAndConvertConditions(asd);
        conditions.forEach(condition -> condition.setSavedQuery(queryModel));
        queryModel.setConditions(conditions);
        queryModel.setSavedQueriesParameters(asd.getParameters());
        this.modelService.save(queryModel);
        return queryModel;
    }


    public AdvancedSearchInitContext getAdvancedSearchInitContext(BackofficeSavedQueryModel sqm)
    {
        AdvancedSearchData asd = new AdvancedSearchData();
        asd.setTokenizable(BooleanUtils.isTrue(sqm.getTokenizable()));
        asd.setIncludeSubtypes(Boolean.valueOf(BooleanUtils.isTrue(sqm.getIncludeSubtypes())));
        asd.setGlobalOperator(getGlobalOperatorByCode(sqm.getGlobalOperatorCode()));
        asd.setTypeCode(sqm.getTypeCode());
        String searchMode = sqm.getSearchMode();
        asd.setAdvancedSearchMode((searchMode == null) ? AdvancedSearchMode.ADVANCED : AdvancedSearchMode.valueOf(searchMode));
        if(StringUtils.isNotEmpty(sqm.getSortAttribute()))
        {
            SortData sortData = new SortData();
            sortData.setAscending(BooleanUtils.isTrue(sqm.getSortAsc()));
            sortData.setSortAttribute(sqm.getSortAttribute());
            asd.setSortData(sortData);
        }
        List<String> notConvertedAttributesValues = Lists.newArrayList();
        List<SearchConditionData> searchConditions = extractAndConvertConditions(sqm, notConvertedAttributesValues);
        if(CollectionUtils.isNotEmpty(searchConditions))
        {
            searchConditions
                            .forEach(condition -> asd.addCondition(condition.getFieldType(), condition.getOperator(), condition.getValue()));
        }
        AdvancedSearchInitContext initCtx = new AdvancedSearchInitContext(asd, null);
        if(CollectionUtils.isNotEmpty(notConvertedAttributesValues))
        {
            initCtx.addAttribute("notConvertedAttributeValues", notConvertedAttributesValues);
        }
        return initCtx;
    }


    protected List<SearchConditionData> extractAndConvertConditions(BackofficeSavedQueryModel savedQueryModel, List<String> notConvertedAttributesValues)
    {
        try
        {
            DataType dataType = this.typeFacade.load(savedQueryModel.getTypeCode());
            return (List<SearchConditionData>)savedQueryModel.getConditions().stream()
                            .map(conditionModel -> convertCondition(conditionModel, dataType, notConvertedAttributesValues))
                            .collect(Collectors.toList());
        }
        catch(TypeNotFoundException e)
        {
            LOG.warn(String.format("Cannot find type: %s", new Object[] {savedQueryModel.getTypeCode()}), (Throwable)e);
            return Lists.newArrayList();
        }
    }


    protected SearchConditionData convertCondition(BackofficeSearchConditionModel condition, DataType dataType, List<String> notConvertedAttributesValues)
    {
        FieldType fieldType = new FieldType();
        fieldType.setName(condition.getAttribute());
        fieldType.setDisabled(condition.getDisabled());
        fieldType.setMandatory(condition.getMandatory());
        fieldType.setOperator(condition.getOperatorCode());
        fieldType.setSortable(condition.getSortable());
        fieldType.setSelected(condition.getSelected());
        fieldType.setEditor(condition.getEditor());
        if(MapUtils.isNotEmpty(condition.getEditorParameters()))
        {
            List<Parameter> params = (List<Parameter>)condition.getEditorParameters().entrySet().stream().map(entry -> {
                Parameter param = new Parameter();
                param.setName((String)entry.getKey());
                param.setValue((String)entry.getValue());
                return param;
            }).collect(Collectors.toList());
            fieldType.getEditorParameter().addAll(params);
        }
        DataAttribute attribute = dataType.getAttribute(condition.getAttribute());
        SavedQueryValue sqv = new SavedQueryValue(condition.getLanguageCode(), condition.getValue(), condition.getValueReference());
        Object value = convertValue(sqv, attribute);
        if((StringUtils.isNotBlank(condition.getValue()) || condition.getValueReference() != null) && value == null)
        {
            notConvertedAttributesValues.add(condition.getAttribute());
        }
        return new SearchConditionData(fieldType, value, ValueComparisonOperator.getOperatorByCode(condition.getOperatorCode()));
    }


    protected List<BackofficeSearchConditionModel> extractAndConvertConditions(AdvancedSearchData asd)
    {
        List<BackofficeSearchConditionModel> conditions = Lists.newArrayList();
        if(CollectionUtils.isEmpty(asd.getSearchFields()))
        {
            return conditions;
        }
        try
        {
            DataType dataType = this.typeFacade.load(asd.getTypeCode());
            for(String fieldName : asd.getSearchFields())
            {
                List<SearchConditionData> fieldConditions = asd.getConditions(fieldName);
                if(CollectionUtils.isNotEmpty(fieldConditions))
                {
                    conditions.addAll(convertConditions(dataType, fieldConditions));
                }
            }
        }
        catch(TypeNotFoundException e)
        {
            LOG.warn(String.format("Cannot find type: %s", new Object[] {asd.getTypeCode()}), (Throwable)e);
        }
        return conditions;
    }


    private List<BackofficeSearchConditionModel> convertConditions(DataType dataType, List<SearchConditionData> fieldConditions)
    {
        List<BackofficeSearchConditionModel> conditions = new ArrayList<>();
        for(SearchConditionData scd : fieldConditions)
        {
            BackofficeSearchConditionModel scm = convertCondition(scd, dataType);
            if(scm != null)
            {
                conditions.add(scm);
            }
        }
        return conditions;
    }


    protected BackofficeSearchConditionModel convertCondition(SearchConditionData scd, DataType dataType)
    {
        FieldType fieldType = scd.getFieldType();
        if(fieldType != null)
        {
            BackofficeSearchConditionModel conditionModel = (BackofficeSearchConditionModel)this.modelService.create(BackofficeSearchConditionModel.class);
            if(scd.getValue() != null)
            {
                SavedQueryValue queryValue = convertValue(scd.getValue(), dataType.getAttribute(fieldType.getName()));
                if(queryValue != null)
                {
                    conditionModel.setValue(queryValue.getValue());
                    conditionModel.setLanguageCode(queryValue.getLocaleCode());
                    conditionModel.setValueReference(queryValue.getValueRef());
                }
            }
            conditionModel.setOperatorCode(scd.getOperator().getOperatorCode());
            conditionModel.setAttribute(fieldType.getName());
            conditionModel.setDisabled(Boolean.valueOf(fieldType.isDisabled()));
            conditionModel.setMandatory(Boolean.valueOf(fieldType.isMandatory()));
            conditionModel.setSelected(Boolean.valueOf(fieldType.isSelected()));
            conditionModel.setSortable(Boolean.valueOf(fieldType.isSortable()));
            conditionModel.setEditor(fieldType.getEditor());
            if(CollectionUtils.isNotEmpty(fieldType.getEditorParameter()))
            {
                Map<String, String> editorParams = Maps.newHashMap();
                fieldType.getEditorParameter().forEach(param -> editorParams.put(param.getName(), param.getValue()));
                conditionModel.setEditorParameters(editorParams);
            }
            return conditionModel;
        }
        return null;
    }


    protected Object convertValue(SavedQueryValue savedQueryValue, DataAttribute dataAttribute)
    {
        if(dataAttribute != null && (StringUtils.isNotBlank(savedQueryValue.getValue()) || savedQueryValue.getValue() == null || savedQueryValue
                        .getValueRef() != null))
        {
            Optional<BackofficeSavedQueryValueConverter> matchingConverter = this.savedQueryValueConverters.stream().filter(converter -> converter.canHandle(dataAttribute)).findFirst();
            if(matchingConverter.isPresent())
            {
                return ((BackofficeSavedQueryValueConverter)matchingConverter.get()).convertValue(savedQueryValue, dataAttribute);
            }
            if(LOG.isWarnEnabled())
            {
                LOG.warn(String.format("Cannot find converter for saved query attribute value [type:%s] [code:%s]", new Object[] {dataAttribute
                                .getValueType().getType(), dataAttribute.getValueType().getCode()}));
            }
        }
        return null;
    }


    protected SavedQueryValue convertValue(Object value, DataAttribute dataAttribute)
    {
        if(dataAttribute != null && value != null)
        {
            Optional<BackofficeSavedQueryValueConverter> matchingConverter = this.savedQueryValueConverters.stream().filter(converter -> converter.canHandle(dataAttribute)).findFirst();
            if(matchingConverter.isPresent())
            {
                return ((BackofficeSavedQueryValueConverter)matchingConverter.get()).convertValue(value, dataAttribute);
            }
            DataType valueType = dataAttribute.getValueType();
            if(valueType != null && LOG.isWarnEnabled())
            {
                LOG.warn(String.format("Cannot find converter for saved query attribute value [type:%s] [code:%s]", new Object[] {valueType
                                .getType(), valueType.getCode()}));
            }
        }
        return null;
    }


    protected ValueComparisonOperator getGlobalOperatorByCode(String operatorCode)
    {
        try
        {
            return ValueComparisonOperator.getOperatorByCode(operatorCode);
        }
        catch(IllegalArgumentException ex)
        {
            LOG.warn(String.format("Cannot convert global operator code:%s to ValueComparisonOperator", new Object[] {operatorCode}), ex);
            return ValueComparisonOperator.AND;
        }
    }


    @Required
    public void setBackofficeSavedQueryDAO(BackofficeSavedQueryDAO backofficeSavedQueryDAO)
    {
        this.backofficeSavedQueryDAO = backofficeSavedQueryDAO;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setSavedQueryValueConverters(List<BackofficeSavedQueryValueConverter> savedQueryValueConverters)
    {
        this.savedQueryValueConverters = savedQueryValueConverters;
    }
}
