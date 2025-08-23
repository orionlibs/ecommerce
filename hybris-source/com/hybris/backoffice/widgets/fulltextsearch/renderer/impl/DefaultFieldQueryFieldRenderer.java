/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch.renderer.impl;

import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.services.ClassificationLabelService;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchController;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.backoffice.widgets.fulltextsearch.SearchFilterValidationStrategyRegistry;
import com.hybris.backoffice.widgets.fulltextsearch.renderer.FieldQueryFilter;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FieldType;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FulltextSearch;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.LocalizedValuesService;
import com.hybris.cockpitng.labels.LabelStringObjectHandler;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;

public class DefaultFieldQueryFieldRenderer extends AbstractWidgetComponentRenderer<Component, FulltextSearch, FieldQueryFilter>
{
    public static final String SCLASS_YW_UNARY_FQ_FILTER = "yw-unary-fq-filter";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFieldQueryFieldRenderer.class);
    private static final String SCLASS_FILTER_NAME = "yw-fulltextsearch-fieldquery-popup-filtername";
    private static final String SCLASS_OPERATOR = "yw-fulltextsearch-fieldquery-popup-operator";
    private static final String MODEL_FIELD_VALUES = "fieldFilterValues";
    private static final String ATTRIBUTE_FILTER_EDITOR = "filter-editor";
    private static final String ATTRIBUTE_FILTER_OPERATOR = "filter-operator";
    private static final String ATTRIBUTE_FIILTER_DATA = "fieldquery-filter-data";
    private static final String ATTRIBUTE_DATA_TYPE = "fieldquery-filter-datatype";
    private static final String ATTRIBUTE_CONFIGURATION = "fieldquery-filter-configuration";
    private static final String REFERENCE_SEARCH_SETTING = "referenceAdvancedSearchEnabled";
    private TypeFacade typeFacade;
    private CockpitConfigurationService cockpitConfigurationService;
    private PermissionFacade permissionFacade;
    private LocalizedValuesService localizedValuesService;
    private List<FullTextSearchStrategy> searchStrategies;
    private FullTextSearchStrategy defaultSearchStrategy;
    private ClassificationLabelService classificationLabelService;
    private LabelStringObjectHandler labelStringObjectHandler;
    private SearchFilterValidationStrategyRegistry searchFilterValidationStrategyRegistry;


    @Override
    public void render(final Component parent, final FulltextSearch configuration, final FieldQueryFilter filter,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        setFulltextSearchConfig(parent, configuration);
        setFieldQueryData(parent, filter);
        setDataType(parent, dataType);
        if(configuration == null || filter == null)
        {
            LOGGER.warn("Null configuration cannot be rendered");
        }
        else if(filter.isApplied() && !isValidFilter(configuration, filter, dataType))
        {
            renderFilterName(parent, widgetInstanceManager);
        }
        else
        {
            try
            {
                if(filter.isApplied())
                {
                    renderFilterName(parent, widgetInstanceManager);
                    renderOperatorChooser(parent, widgetInstanceManager);
                }
                else
                {
                    renderFilterChooser(parent, widgetInstanceManager);
                    renderOperatorChooser(parent, widgetInstanceManager);
                }
                renderEditor(parent, widgetInstanceManager);
            }
            catch(final TypeNotFoundException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
        fireComponentRendered(parent, configuration, filter);
    }


    protected boolean isValidFilter(final FulltextSearch configuration, final FieldQueryFilter filter, final DataType dataType)
    {
        return getSearchFilterValidationStrategyRegistry().getStrategy(configuration.getPreferredSearchStrategy())
                        .isValid(dataType.getCode(), filter.getName(), filter.getValue(), filter.getOperator());
    }


    protected FieldType findField(final FulltextSearch configuration, final String name)
    {
        if(configuration != null && configuration.getFieldList() != null && configuration.getFieldList().getField() != null)
        {
            return configuration.getFieldList().getField().stream().filter(field -> StringUtils.equals(field.getName(), name))
                            .findFirst().orElse(null);
        }
        else
        {
            return null;
        }
    }


    protected String resolveFilterLabel(final String typeCode, final FieldType field,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        String resultLabel = StringUtils.EMPTY;
        final String fieldLabel = field.getLabel();
        if(StringUtils.isNotBlank(fieldLabel))
        {
            resultLabel = widgetInstanceManager.getLabel(fieldLabel);
        }
        if(canCreateResultLabel(resultLabel, fieldLabel))
        {
            resultLabel = resolveLabelByKey(fieldLabel);
        }
        if(canCreateResultLabel(resultLabel, field.getDisplayName()))
        {
            resultLabel = field.getDisplayName();
        }
        if(canCreateResultLabel(resultLabel, fieldLabel))
        {
            resultLabel = getLabelFromLabelService(typeCode, fieldLabel);
        }
        if(canCreateResultLabel(resultLabel, fieldLabel))
        {
            final Locale locale = localizedValuesService.getSelectedLocaleOrDefault(null);
            resultLabel = getClassificationLabelService().getClassificationLabel(fieldLabel, locale);
        }
        if(canCreateResultLabel(resultLabel, field.getName()))
        {
            resultLabel = getLabelByFieldName(typeCode, field.getName());
        }
        return resultLabel;
    }


    protected String resolveLabelByKey(final String key)
    {
        return Labels.getLabel(key);
    }


    protected boolean canCreateResultLabel(final String resultLabel, final String labelData)
    {
        return StringUtils.isBlank(resultLabel) && StringUtils.isNotBlank(labelData);
    }


    protected String getLabelByFieldName(final String typeCode, final String fieldName)
    {
        final String label = getLabelFromLabelService(typeCode, fieldName);
        return StringUtils.isNotBlank(label) ? label : ObjectValuePath.getPath(typeCode, fieldName);
    }


    protected String getLabelFromLabelService(final String typeCode, final String labelName)
    {
        String labelByTypeCodeAndName = labelStringObjectHandler.getObjectLabel(labelName);
        if(StringUtils.isBlank(labelByTypeCodeAndName))
        {
            labelByTypeCodeAndName = labelStringObjectHandler.getObjectLabel(ObjectValuePath.getPath(typeCode, labelName));
        }
        return labelByTypeCodeAndName;
    }


    protected void renderFilterName(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final FulltextSearch config = getFulltextSearchConfig(parent);
        final FieldQueryFilter queryData = getFieldQueryData(parent);
        final String fieldName = queryData.getName();
        final FieldType field = findField(config, fieldName);
        final Label label;
        if(field != null)
        {
            final String attributeLabel = resolveFilterLabel(queryData.getSearchData().getTypeCode(), field, widgetInstanceManager);
            label = new Label(attributeLabel);
        }
        else
        {
            LOGGER.warn("Using fallback label for filter {}", fieldName);
            label = new Label(fieldName);
        }
        UITools.modifySClass(label, SCLASS_FILTER_NAME, true);
        parent.appendChild(label);
        fireComponentRendered(label, parent, config, queryData);
    }


    protected void onFilterChanged(final FieldQueryFilter data, final String name)
    {
        data.setName(name);
        data.setValue(null);
        data.setOperator(null);
    }


    protected void onFilterChanged(final FieldQueryFilter data, final ValueComparisonOperator operator)
    {
        data.setOperator(operator);
        if(!operator.isRequireValue())
        {
            data.setValue(null);
        }
    }


    protected void onFilterChanged(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Component operator = (Component)parent.removeAttribute(ATTRIBUTE_FILTER_OPERATOR);
        if(operator != null)
        {
            parent.removeChild(operator);
        }
        renderOperatorChooser(parent, widgetInstanceManager);
        onFilterOperatorChanged(parent, widgetInstanceManager);
    }


    protected void onFilterOperatorChanged(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Component editor = (Component)parent.removeAttribute(ATTRIBUTE_FILTER_EDITOR);
        if(editor != null)
        {
            parent.removeChild(editor);
        }
        try
        {
            renderEditor(parent, widgetInstanceManager);
        }
        catch(final TypeNotFoundException e)
        {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }


    protected FieldType getSelectedField(final Component parent)
    {
        FieldType selectedField = null;
        final FieldQueryFilter data = getFieldQueryData(parent);
        final FulltextSearch configuration = getFulltextSearchConfig(parent);
        if(data != null && data.getName() != null)
        {
            selectedField = findField(configuration, data.getName());
        }
        if(selectedField == null)
        {
            selectedField = configuration.getFieldList().getField().iterator().next();
        }
        return selectedField;
    }


    protected void renderFilterChooser(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final FieldType selectedField = getSelectedField(parent);
        final FieldQueryFilter data = getFieldQueryData(parent);
        data.setName(selectedField != null ? selectedField.getName() : null);
        final FulltextSearch configuration = getFulltextSearchConfig(parent);
        if(configuration.getFieldList().getField().size() > 1)
        {
            final Combobox selector = new Combobox();
            UITools.modifySClass(selector, SCLASS_FILTER_NAME, true);
            final ListModelList<FieldType> attributeModel = new ListModelList<>();
            configuration.getFieldList().getField().stream().filter(field -> {
                final boolean isKnownType = findSearchStrategy(configuration.getPreferredSearchStrategy())
                                .getFieldType(getDataType(parent).getCode(), field.getName()) != null;
                if(!isKnownType)
                {
                    LOGGER.error("Unable to determine field configuration for field '{}' in '{}'", field.getName(),
                                    widgetInstanceManager.getWidgetslot().getWidgetInstance().getId());
                }
                return isKnownType;
            }).forEach(attributeModel::add);
            attributeModel.setSelection(selectedField != null ? Collections.singletonList(selectedField) : Collections.emptyList());
            selector.setModel(attributeModel);
            selector.addEventListener(Events.ON_SELECT, event -> {
                final FieldType fieldType = attributeModel.get(selector.getSelectedIndex());
                onFilterChanged(data, fieldType.getName());
                onFilterChanged(parent, widgetInstanceManager);
            });
            selector.setReadonly(true);
            selector.setItemRenderer((item, data1, index) -> {
                item.setLabel(resolveFilterLabel(data.getSearchData().getTypeCode(), (FieldType)data1, widgetInstanceManager));
                item.setValue(data1);
            });
            parent.appendChild(selector);
            fireComponentRendered(selector, parent, configuration, data);
        }
        else
        {
            renderFilterName(parent, widgetInstanceManager);
        }
    }


    protected void renderOperator(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final FieldQueryFilter queryData = getFieldQueryData(parent);
        final ValueComparisonOperator operator = queryData.getOperator();
        final Label label = new Label(resolveOperatorLabel(operator, queryData, widgetInstanceManager));
        UITools.modifySClass(label, SCLASS_OPERATOR, true);
        parent.appendChild(label);
        parent.setAttribute(ATTRIBUTE_FILTER_OPERATOR, label);
        fireComponentRendered(label, parent, getFulltextSearchConfig(parent), getFieldQueryData(parent));
    }


    protected String resolveOperatorLabel(final ValueComparisonOperator operator, final FieldQueryFilter fieldQueryFilter,
                    final WidgetInstanceManager wim)
    {
        final String fieldType = getSearchStrategy(wim).getFieldType(fieldQueryFilter.getSearchData().getTypeCode(),
                        fieldQueryFilter.getName());
        final String typeSpecificOperatorLabelKey = operator.getLabelKey() + "." + fieldType;
        final String typeSpecificOperatorLabel = resolveLabelByKey(typeSpecificOperatorLabelKey);
        final String label = StringUtils.isNotBlank(typeSpecificOperatorLabel) ? typeSpecificOperatorLabel
                        : resolveLabelByKey(operator.getLabelKey());
        return label.toLowerCase(Locales.getCurrent());
    }


    protected void renderOperatorChooser(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final FieldQueryFilter data = getFieldQueryData(parent);
        final FullTextSearchStrategy searchStrategy = getSearchStrategy(widgetInstanceManager);
        final Collection<ValueComparisonOperator> availableOperators = searchStrategy
                        .getAvailableOperators(data.getSearchData().getTypeCode(), data.getName());
        ValueComparisonOperator selection = data.getOperator();
        if(selection == null && !availableOperators.isEmpty())
        {
            selection = availableOperators.iterator().next();
        }
        data.setOperator(selection);
        if(availableOperators.size() > 1)
        {
            final Combobox selector = new Combobox();
            UITools.modifySClass(selector, SCLASS_OPERATOR, true);
            final ListModelList<ValueComparisonOperator> attributeModel = new ListModelList<>();
            attributeModel.addAll(availableOperators);
            attributeModel.setSelection(selection != null ? Collections.singleton(selection) : Collections.emptyList());
            selector.setModel(attributeModel);
            selector.addEventListener(Events.ON_SELECT, event -> {
                final ValueComparisonOperator operator = attributeModel.get(selector.getSelectedIndex());
                onFilterChanged(data, operator);
                onFilterOperatorChanged(parent, widgetInstanceManager);
                if(!operator.isRequireValue() && data.getValue() == null)
                {
                    final String typeCode = data.getSearchData().getTypeCode();
                    if(searchStrategy.isLocalized(typeCode, data.getName()))
                    {
                        onValueChanged(data, new HashMap<>());
                    }
                }
            });
            selector.setReadonly(true);
            selector.setItemRenderer((item, data1, index) -> {
                item.setLabel(resolveOperatorLabel((ValueComparisonOperator)data1, data, widgetInstanceManager)
                                .toLowerCase(Locales.getCurrent()));
                item.setValue(data1);
            });
            parent.appendChild(selector);
            parent.setAttribute(ATTRIBUTE_FILTER_OPERATOR, selector);
            fireComponentRendered(selector, parent, getFulltextSearchConfig(parent), data);
        }
        else
        {
            renderOperator(parent, widgetInstanceManager);
        }
    }


    protected String getEditorProperty(final FieldQueryFilter data)
    {
        final String normalized = "_" + data.getFilterId().replaceAll("[^a-z|0-9]", "");
        return MODEL_FIELD_VALUES + normalized;
    }


    protected Map<Pattern, String> prepareEditorMappings()
    {
        return ImmutableMap.<Pattern, String>builder().put(EditorUtils.getReferenceSinglingMapping()).build();
    }


    protected void renderEditor(final Component parent, final WidgetInstanceManager widgetInstanceManager)
                    throws TypeNotFoundException
    {
        final FulltextSearch configuration = getFulltextSearchConfig(parent);
        final FieldQueryFilter data = getFieldQueryData(parent);
        final FieldType fieldConfiguration = findField(configuration, data.getName());
        final FullTextSearchStrategy searchStrategy = getSearchStrategy(widgetInstanceManager);
        if(fieldConfiguration != null)
        {
            final String typeCode = data.getSearchData().getTypeCode();
            final String fieldType = searchStrategy.getFieldType(typeCode, data.getName());
            if(fieldType != null)
            {
                final Editor editor = createEditor(parent, widgetInstanceManager);
                parent.setAttribute(ATTRIBUTE_FILTER_EDITOR, editor);
                parent.appendChild(editor);
                fireComponentRendered(editor, parent, configuration, data);
            }
            else
            {
                LOGGER.error("Unable to determine type for field '{}' in '{}'", data.getName(),
                                widgetInstanceManager.getWidgetslot().getId());
            }
        }
        else
        {
            LOGGER.error("Unable to determine field configuration for field '{}' in '{}'", data.getName(),
                            widgetInstanceManager.getWidgetslot().getId());
        }
    }


    protected Editor createEditor(final Component parent, final WidgetInstanceManager widgetInstanceManager)
                    throws TypeNotFoundException
    {
        final FullTextSearchStrategy searchStrategy = getSearchStrategy(widgetInstanceManager);
        final FulltextSearch configuration = getFulltextSearchConfig(parent);
        final FieldQueryFilter data = getFieldQueryData(parent);
        final FieldType fieldConfiguration = findField(configuration, data.getName());
        final String typeCode = data.getSearchData().getTypeCode();
        final String editorProperty = getEditorProperty(data);
        final String fieldType = searchStrategy.getFieldType(typeCode, data.getName());
        final DataType fieldDataType = getTypeFacade().load(fieldType);
        final Editor editor = new Editor();
        final boolean localized = searchStrategy.isLocalized(typeCode, data.getName());
        if(localized)
        {
            final Collection<String> searchableLanguages = searchStrategy.getAvailableLanguages(typeCode);
            final Set<Locale> writableLocales = getPermissionFacade().getAllWritableLocalesForCurrentUser().stream()
                            .filter(locale -> searchableLanguages.contains(locale.getLanguage())).collect(Collectors.toSet());
            final Set<Locale> readableLocales = getPermissionFacade().getAllReadableLocalesForCurrentUser().stream()
                            .filter(locale -> searchableLanguages.contains(locale.getLanguage())).collect(Collectors.toSet());
            readableLocales.addAll(writableLocales);
            editor.setWritableLocales(writableLocales);
            editor.setReadableLocales(readableLocales);
        }
        fieldConfiguration.getEditorParameter().stream().filter(parameter -> StringUtils.isNotBlank(parameter.getName()))
                        .forEach(parameter -> editor.addParameter(parameter.getName(), parameter.getValue()));
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setProperty(editorProperty);
        editor.setType(EditorUtils.getEditorType(fieldDataType, localized ? Boolean.TRUE : null, prepareEditorMappings()));
        adjustReferenceSearch(editor, fieldDataType);
        editor.setLocalized(localized);
        editor.setOptional(true);
        editor.setDefaultEditor(fieldConfiguration.getEditor());
        editor.setAtomic(fieldDataType.isAtomic());
        editor.setReadOnly(isReadOnly(data, localized));
        editor.initialize();
        editor.addEventListener(Editor.ON_VALUE_CHANGED, event -> onValueChanged(data, editor.getValue()));
        if(localized)
        {
            editor.addEventListener(Events.ON_SELECT, event -> onLanguageChanged(data, event.getData()));
        }
        if(data.getOperator().isUnary())
        {
            editor.setSclass(SCLASS_YW_UNARY_FQ_FILTER);
        }
        return editor;
    }


    /**
     * The method returns false for required values. In case of localized value the editor renders locale chooser that will
     * be changed by the user and value input which could be read-only; in that case the method should return false even if
     * the field isn't required.
     *
     * @param data
     * @param localized
     * @return true in case the value should be read-only and false otherwise
     */
    protected boolean isReadOnly(final FieldQueryFilter data, final boolean localized)
    {
        final boolean requiresPartialValueChange = !localized;
        final boolean requiredValue = data.getOperator().isRequireValue();
        return requiresPartialValueChange && !requiredValue;
    }


    protected void adjustReferenceSearch(final Editor editor, final DataType dataType)
    {
        if(dataType.getType() == DataType.Type.COMPOUND)
        {
            editor.addParameter(REFERENCE_SEARCH_SETTING, Boolean.FALSE);
        }
    }


    protected FullTextSearchStrategy getSearchStrategy(final WidgetInstanceManager instanceManager)
    {
        final String preferredStrategyName = instanceManager.getModel().getValue(FullTextSearchController.MODEL_PREFERRED_STRATEGY,
                        String.class);
        if(StringUtils.isNotBlank(preferredStrategyName))
        {
            return findSearchStrategy(preferredStrategyName);
        }
        else
        {
            return getDefaultSearchStrategy();
        }
    }


    protected FullTextSearchStrategy findSearchStrategy(final String preferredStrategyName)
    {
        final Optional<FullTextSearchStrategy> preferredStrategy = getSearchStrategies().stream()
                        .filter(strategy -> StringUtils.equalsIgnoreCase(strategy.getStrategyName(), preferredStrategyName)).findFirst();
        return preferredStrategy.orElse(getDefaultSearchStrategy());
    }


    protected void onValueChanged(final FieldQueryFilter data, final Object value)
    {
        populateFilterQueryLocale(data, value);
        data.setValue(value);
    }


    protected void populateFilterQueryLocale(final FieldQueryFilter data, final Object value)
    {
        if(data.getLocale() == null && value instanceof Map)
        {
            final Optional<Locale> populatedLocale = ((Map<Locale, Object>)value).keySet().stream().findFirst();
            if(populatedLocale.isPresent())
            {
                data.setLocale(populatedLocale.get());
                return;
            }
            final Map<Locale, Object> localizedValues = (Map<Locale, Object>)data.getValue();
            final Locale defaultLocale = localizedValuesService.getSelectedLocaleOrDefault(localizedValues);
            data.setLocale(defaultLocale);
        }
    }


    protected void onLanguageChanged(final FieldQueryFilter data, final Object eventData)
    {
        if(eventData instanceof Locale)
        {
            data.setLocale((Locale)eventData);
        }
    }


    protected FulltextSearch getFulltextSearchConfig(final Component parent)
    {
        return (FulltextSearch)parent.getAttribute(ATTRIBUTE_CONFIGURATION);
    }


    protected void setFulltextSearchConfig(final Component parent, final FulltextSearch config)
    {
        parent.setAttribute(ATTRIBUTE_CONFIGURATION, config);
    }


    protected DataType getDataType(final Component parent)
    {
        return (DataType)parent.getAttribute(ATTRIBUTE_DATA_TYPE);
    }


    protected void setDataType(final Component parent, final DataType dataType)
    {
        parent.setAttribute(ATTRIBUTE_DATA_TYPE, dataType);
    }


    protected FieldQueryFilter getFieldQueryData(final Component parent)
    {
        return (FieldQueryFilter)parent.getAttribute(ATTRIBUTE_FIILTER_DATA);
    }


    protected void setFieldQueryData(final Component parent, final FieldQueryFilter data)
    {
        parent.setAttribute(ATTRIBUTE_FIILTER_DATA, data);
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public LocalizedValuesService getLocalizedValuesService()
    {
        return localizedValuesService;
    }


    @Required
    public void setLocalizedValuesService(final LocalizedValuesService localizedValuesService)
    {
        this.localizedValuesService = localizedValuesService;
    }


    public List<FullTextSearchStrategy> getSearchStrategies()
    {
        return searchStrategies;
    }


    @Required
    public void setSearchStrategies(final List<FullTextSearchStrategy> searchStrategies)
    {
        this.searchStrategies = searchStrategies;
    }


    public FullTextSearchStrategy getDefaultSearchStrategy()
    {
        return defaultSearchStrategy;
    }


    @Required
    public void setDefaultSearchStrategy(final FullTextSearchStrategy defaultSearchStrategy)
    {
        this.defaultSearchStrategy = defaultSearchStrategy;
    }


    public ClassificationLabelService getClassificationLabelService()
    {
        return classificationLabelService;
    }


    @Required
    public void setClassificationLabelService(final ClassificationLabelService classificationLabelService)
    {
        this.classificationLabelService = classificationLabelService;
    }


    public LabelStringObjectHandler getLabelStringObjectHandler()
    {
        return labelStringObjectHandler;
    }


    @Required
    public void setLabelStringObjectHandler(final LabelStringObjectHandler labelStringObjectHandler)
    {
        this.labelStringObjectHandler = labelStringObjectHandler;
    }


    protected SearchFilterValidationStrategyRegistry getSearchFilterValidationStrategyRegistry()
    {
        return searchFilterValidationStrategyRegistry;
    }


    @Required
    public void setSearchFilterValidationStrategyRegistry(
                    final SearchFilterValidationStrategyRegistry searchFilterValidationStrategyRegistry)
    {
        this.searchFilterValidationStrategyRegistry = searchFilterValidationStrategyRegistry;
    }
}
