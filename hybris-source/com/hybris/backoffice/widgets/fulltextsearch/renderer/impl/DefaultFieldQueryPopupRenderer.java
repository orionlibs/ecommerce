/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch.renderer.impl;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchController;
import com.hybris.backoffice.widgets.fulltextsearch.SearchFilterValidationStrategyRegistry;
import com.hybris.backoffice.widgets.fulltextsearch.renderer.FieldQueryFilter;
import com.hybris.backoffice.widgets.fulltextsearch.renderer.FullTextSearchFilter;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.config.fulltextsearch.jaxb.FulltextSearch;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.i18n.LocalizedValuesService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;

public class DefaultFieldQueryPopupRenderer extends AbstractWidgetComponentRenderer<Popup, FulltextSearch, AdvancedSearchData>
{
    protected static final String ATTRIBUTE_FILTERS = "fieldquery-filters";
    protected static final String ATTRIBUTE_ADVANCEDSEARCH_DATA = "fieldquery-advancedsearch-data";
    protected static final String ATTRIBUTE_DATA_TYPE = "fieldquery-datatype";
    protected static final String ATTRIBUTE_CONFIGURATION = "fieldquery-configuration";
    private static final String LABEL_FIELDQUERY_POPUP_TITLE = "fieldquerypopup.title";
    private static final String LABEL_FIELDQUERY_POPUP_ADD_FILTER = "fieldquerypopup.button.addfilter";
    private static final String LABEL_FIELDQUERY_POPUP_APPLY_FILTERS = "fieldquerypopup.button.apply";
    private static final String SCLASS_FIELDQUERY_POPUP_LAYOUT = "yw-fulltextsearch-fieldquery-popup-layout";
    private static final String SCLASS_FIELDQUERY_POPUP_TITLE = "yw-fulltextsearch-fieldquery-popup-title";
    private static final String SCLASS_FIELDQUERY_POPUP_HEADER = "yw-fulltextsearch-fieldquery-popup-header";
    private static final String SCLASS_FIELDQUERY_POPUP_BODY = "yw-fulltextsearch-fieldquery-popup-body";
    private static final String SCLASS_FIELDQUERY_POPUP_FOOTER = "yw-fulltextsearch-fieldquery-popup-footer";
    private static final String SCLASS_FIELDQUERY_POPUP_FILTER = "yw-fulltextsearch-fieldquery-popup-filter";
    private static final String SCLASS_FIELDQUERY_POPUP_FILTER_DISABLED = "yw-fulltextsearch-fieldquery-popup-filter-disabled";
    private static final String SCLASS_FIELDQUERY_POPUP_FILTER_APPLIED = "yw-fulltextsearch-fieldquery-popup-filter-applied";
    private static final String SCLASS_FIELDQUERY_POPUP_FILTER_FIELDS = "yw-fulltextsearch-fieldquery-popup-filter-fields";
    private static final String SCLASS_FIELDQUERY_POPUP_BUTTON_ADD_FILTER = "yw-fulltextsearch-fieldquery-popup-button-add-filter";
    private static final String SCLASS_FIELDQUERY_POPUP_BUTTON_APPLY = "yw-fulltextsearch-fieldquery-popup-button-apply";
    private static final String SCLASS_FIELDQUERY_POPUP_BUTTON_REMOVE_FILTER = "yw-fulltextsearch-fieldquery-popup-button-remove-filter ye-delete-btn";
    private static final String SCLASS_FIELDQUERY_POPUP_CHECKBOX_FILTER = "yw-fulltextsearch-fieldquery-popup-checkbox-filter ye-switch-checkbox";
    private static final String SCLASS_FIELDQUERY_POPUP_FILTER_CONTROL_CONTAINER = "yw-fulltextsearch-fieldquery-popup-filter-control-container";
    private static final String SCLASS_Y_BTN_PRIMARY = "yw-fulltextsearch-fieldquery-popup-apply-filters y-btn-primary";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFieldQueryPopupRenderer.class);
    private WidgetComponentRenderer<Component, FulltextSearch, FieldQueryFilter> fieldRenderer;
    private CockpitLocaleService cockpitLocaleService;
    private LocalizedValuesService localizedValuesService;
    private SearchFilterValidationStrategyRegistry searchFilterValidationStrategyRegistry;
    private NotificationService notificationService;


    @Override
    public void render(final Popup popup, final FulltextSearch fullTextSearchConfiguration,
                    final AdvancedSearchData advancedSearchData, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        if(fullTextSearchConfiguration == null)
        {
            throw new IllegalArgumentException("Cannot render popup without configuration");
        }
        setAdvancedSearchData(popup, advancedSearchData);
        setDataType(popup, dataType);
        setFulltextSearchConfig(popup, fullTextSearchConfiguration);
        renderPopup(popup, widgetInstanceManager);
        restoreFiltersFromModel(popup, widgetInstanceManager);
        fireComponentRendered(popup, fullTextSearchConfiguration, advancedSearchData);
    }


    protected Component renderPopup(final Popup popup, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div layout = new Div();
        layout.setSclass(SCLASS_FIELDQUERY_POPUP_LAYOUT);
        popup.appendChild(layout);
        final Component header = createFieldQueryPopupHeader(popup, widgetInstanceManager);
        layout.appendChild(header);
        final Component popupBody = createFieldQueryPopupBody(popup, widgetInstanceManager);
        layout.appendChild(popupBody);
        final Component popupFooter = createFieldQueryPopupFooter(popup, widgetInstanceManager);
        layout.appendChild(popupFooter);
        return layout;
    }


    protected Component createFieldQueryPopupHeader(final Popup parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div headerContainer = new Div();
        final Label popupTitleLabel = new Label(widgetInstanceManager.getLabel(LABEL_FIELDQUERY_POPUP_TITLE));
        headerContainer.setSclass(SCLASS_FIELDQUERY_POPUP_HEADER);
        popupTitleLabel.setSclass(SCLASS_FIELDQUERY_POPUP_TITLE);
        headerContainer.appendChild(popupTitleLabel);
        return headerContainer;
    }


    protected Component createFieldQueryPopupBody(final Popup parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div fieldQueryPopupBody = new Div();
        fieldQueryPopupBody.setSclass(SCLASS_FIELDQUERY_POPUP_BODY);
        return fieldQueryPopupBody;
    }


    protected Component createFieldQueryPopupFooter(final Popup parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Div buttonsDiv = new Div();
        buttonsDiv.setSclass(SCLASS_FIELDQUERY_POPUP_FOOTER);
        final Component addFilterButton = renderAddFilterButton(buttonsDiv, widgetInstanceManager);
        addFilterButton.addEventListener(Events.ON_CLICK, event -> addNewFilter(parent, widgetInstanceManager));
        setButtonVisibility(parent, addFilterButton);
        final Component applyFiltersButton = renderApplyFiltersButton(buttonsDiv, widgetInstanceManager);
        applyFiltersButton.addEventListener(Events.ON_CLICK, event -> applyFilters(parent));
        return buttonsDiv;
    }


    protected void setButtonVisibility(final Component parent, final Component button)
    {
        final FulltextSearch config = getFulltextSearchConfig(parent);
        if(config != null)
        {
            if(config.getFieldList() != null && !config.getFieldList().getField().isEmpty())
            {
                button.setVisible(true);
            }
            else
            {
                button.setVisible(false);
                LOG.warn("FieldQuery is enabled, but no fields were defined.");
            }
        }
    }


    protected final Component renderAddFilterButton(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Button button = new Button(widgetInstanceManager.getLabel(LABEL_FIELDQUERY_POPUP_ADD_FILTER));
        UITools.modifySClass(button, SCLASS_FIELDQUERY_POPUP_BUTTON_ADD_FILTER, true);
        parent.appendChild(button);
        return button;
    }


    protected final Component renderApplyFiltersButton(final Component parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Button button = new Button(widgetInstanceManager.getLabel(LABEL_FIELDQUERY_POPUP_APPLY_FILTERS));
        UITools.modifySClass(button, SCLASS_Y_BTN_PRIMARY, true);
        UITools.modifySClass(button, SCLASS_FIELDQUERY_POPUP_BUTTON_APPLY, true);
        parent.appendChild(button);
        return button;
    }


    protected void onFieldQueryValueChange(final FieldQueryFilter fieldQuery, final FullTextSearchFilter filter)
    {
        if(isFieldValueEmpty(fieldQuery))
        {
            if(fieldQuery.getLocale() == null)
            {
                filter.setValue(null);
            }
            else
            {
                final Map<Locale, Object> value = new HashMap<>();
                value.put(fieldQuery.getLocale(), StringUtils.EMPTY);
                filter.setValue(value);
            }
        }
        else
        {
            filter.setValue(fieldQuery.getValue());
        }
    }


    protected boolean isFieldValueEmpty(final FieldQueryFilter fieldQuery)
    {
        boolean valueEmpty = true;
        if(fieldQuery != null)
        {
            if(fieldQuery.getValue() instanceof Map)
            {
                final Map<Locale, Object> localizedValue = (Map)fieldQuery.getValue();
                valueEmpty = localizedValue.keySet().stream().filter(Objects::nonNull)
                                .noneMatch(locale -> localizedValue.get(locale) != null);
            }
            else if(fieldQuery.getOperator() != null && (fieldQuery.getOperator().isRequireValue()))
            {
                valueEmpty = fieldQuery.getValue() == null || StringUtils.isEmpty(fieldQuery.getValue().toString());
            }
        }
        return valueEmpty;
    }


    protected void onFieldQueryEnabledChange(final FieldQueryFilter fieldQuery, final FullTextSearchFilter filter,
                    final Checkbox checkboxToggle)
    {
        filter.setEnabled(fieldQuery.isEnabled());
        checkboxToggle.setChecked(filter.isEnabled());
        Events.postEvent(Events.ON_CHECK, checkboxToggle, Boolean.valueOf(checkboxToggle.isChecked()));
    }


    protected void onFieldQueryNameChange(final FieldQueryFilter fieldQuery, final FullTextSearchFilter filter)
    {
        filter.setName(fieldQuery.getName());
    }


    protected void addNewFilter(final Popup parent, final WidgetInstanceManager widgetInstanceManager)
    {
        addNewFilter(parent, widgetInstanceManager, UUID.randomUUID().toString(), null);
    }


    protected void addNewFilter(final Popup parent, final WidgetInstanceManager widgetInstanceManager, final String filterId,
                    final FullTextSearchFilter appliedFilter)
    {
        final FulltextSearch fulltextSearchConfig = getFulltextSearchConfig(parent);
        final AdvancedSearchData advancedSearchData = getAdvancedSearchData(parent);
        final ProxyRenderer<Popup, FulltextSearch, AdvancedSearchData> proxyRenderer = new ProxyRenderer(this, parent,
                        fulltextSearchConfig, advancedSearchData);
        final Div filterContainer = createFilterContainer(appliedFilter);
        getFieldQueryPopupBody(parent).appendChild(filterContainer);
        final Div filterButtonsContainer = createFilterButtonsContainer();
        appendRemoveFilterButton(parent, filterButtonsContainer, filterContainer, filterId);
        final FieldQueryFilter fieldQueryFilter = appliedFilter != null
                        ? buildFieldQueryFilter(proxyRenderer.getData(), filterId, appliedFilter)
                        : buildFieldQueryFilter(proxyRenderer.getData(), filterId);
        final FullTextSearchFilter filter = getFilters(parent).computeIfAbsent(filterId,
                        id -> appliedFilter != null ? new FullTextSearchFilter(appliedFilter) : new FullTextSearchFilter(true));
        if(appliedFilter == null || isValidFilter(fulltextSearchConfig, fieldQueryFilter, getDataType(parent)))
        {
            final Checkbox checkbox = createCheckboxFilter(parent, appliedFilter, filterContainer, filterId);
            filterButtonsContainer.appendChild(checkbox);
            final ValueObserver changeObserver = new ValueObserver()
            {
                @Override
                public void modelChanged(final String property)
                {
                    onFieldQueryChange(property, fieldQueryFilter, filter, checkbox);
                }


                @Override
                public void modelChanged()
                {
                    // not implemented
                }
            };
            fieldQueryFilter.addObserver(changeObserver);
        }
        else
        {
            filter.setEnabled(false);
            getNotificationService().notifyUser(widgetInstanceManager,
                            FullTextSearchController.NOTIFICATION_EVENT_TYPE_FILTER_INVALID, Level.WARNING, filter.getName());
        }
        filterContainer.appendChild(filterButtonsContainer);
        final Div filterFields = createFilterFields();
        filterContainer.appendChild(filterFields);
        proxyRenderer.render(getFieldRenderer(), filterFields, proxyRenderer.getConfig(), fieldQueryFilter, getDataType(parent),
                        widgetInstanceManager);
        Clients.resize(parent);
        fireComponentRendered(filterContainer, parent, proxyRenderer.getConfig(), proxyRenderer.getData());
    }


    protected boolean isValidFilter(final FulltextSearch configuration, final FieldQueryFilter filter, final DataType dataType)
    {
        return getSearchFilterValidationStrategyRegistry().getStrategy(configuration.getPreferredSearchStrategy())
                        .isValid(dataType.getCode(), filter.getName(), filter.getValue(), filter.getOperator());
    }


    protected void onFieldQueryChange(final String property, final FieldQueryFilter fieldQueryFilter,
                    final FullTextSearchFilter filter, final Checkbox checkbox)
    {
        switch(property)
        {
            case FieldQueryFilter.PROPERTY_VALUE:
                onFieldQueryValueChange(fieldQueryFilter, filter);
                break;
            case FieldQueryFilter.PROPERTY_ENABLED:
                onFieldQueryEnabledChange(fieldQueryFilter, filter, checkbox);
                break;
            case FieldQueryFilter.PROPERTY_NAME:
                onFieldQueryNameChange(fieldQueryFilter, filter);
                break;
            case FieldQueryFilter.PROPERTY_LOCALE:
                onFieldQueryLocaleChange(fieldQueryFilter, filter);
                break;
            case FieldQueryFilter.PROPERTY_OPERATOR:
                onFieldQueryOperatorChange(fieldQueryFilter, filter);
                break;
            default:
                break;
        }
    }


    private Div createFilterContainer(final FullTextSearchFilter value)
    {
        final Div filterContainer = new Div();
        UITools.modifySClass(filterContainer, SCLASS_FIELDQUERY_POPUP_FILTER, true);
        UITools.modifySClass(filterContainer, SCLASS_FIELDQUERY_POPUP_FILTER_APPLIED, value != null);
        return filterContainer;
    }


    protected void onFieldQueryLocaleChange(final FieldQueryFilter fieldQueryFilter, final FullTextSearchFilter filter)
    {
        filter.setLocale(fieldQueryFilter.getLocale());
    }


    protected void onFieldQueryOperatorChange(final FieldQueryFilter fieldQueryFilter, final FullTextSearchFilter filter)
    {
        filter.setOperator(fieldQueryFilter.getOperator());
    }


    protected Div createFilterFields()
    {
        final Div component = new Div();
        component.setSclass(SCLASS_FIELDQUERY_POPUP_FILTER_FIELDS);
        return component;
    }


    protected Component appendRemoveFilterButton(final Popup parent, final Div controlButtonContainer, final Div filterContainer,
                    final String filterId)
    {
        final Button removeFilterButton = new Button();
        removeFilterButton.setClass(SCLASS_FIELDQUERY_POPUP_BUTTON_REMOVE_FILTER);
        removeFilterButton.addEventListener(Events.ON_CLICK, event -> removeFilter(parent, filterContainer, filterId));
        controlButtonContainer.appendChild(removeFilterButton);
        return removeFilterButton;
    }


    protected Checkbox createCheckboxFilter(final Popup parent, final FullTextSearchFilter filter, final Div filterContainer,
                    final String filterId)
    {
        final Checkbox checkbox = new Checkbox();
        UITools.modifySClass(checkbox, SCLASS_FIELDQUERY_POPUP_CHECKBOX_FILTER, true);
        if(filter == null || !filter.isEnabled())
        {
            UITools.modifySClass(filterContainer, SCLASS_FIELDQUERY_POPUP_FILTER_DISABLED, true);
        }
        checkbox.setChecked(filter != null && filter.isEnabled());
        checkbox.addEventListener(Events.ON_CHECK,
                        event -> toggleFilter(parent, filterContainer, (Checkbox)event.getTarget(), filterId));
        return checkbox;
    }


    protected Div createFilterButtonsContainer()
    {
        final Div controls = new Div();
        controls.setSclass(SCLASS_FIELDQUERY_POPUP_FILTER_CONTROL_CONTAINER);
        return controls;
    }


    protected void removeFilter(final Popup parent, final Component filterContainer, final String filterId)
    {
        filterContainer.setParent(null);
        getFilters(parent).remove(filterId);
    }


    protected void toggleFilter(final Popup parent, final Div filterContainer, final Checkbox checkbox, final String filterId)
    {
        getFilters(parent).computeIfPresent(filterId, (id, filter) -> {
            UITools.modifySClass(filterContainer, SCLASS_FIELDQUERY_POPUP_FILTER_DISABLED, !checkbox.isChecked());
            filter.setEnabled(checkbox.isChecked());
            return filter;
        });
    }


    protected void notifyFiltersChanged(final Popup parent, final Map<String, FullTextSearchFilter> filters)
    {
        Events.postEvent(FullTextSearchController.EVENT_ON_APPLY_FILTERS, parent, filters);
    }


    protected void applyFilters(final Popup parent)
    {
        notifyFiltersChanged(parent, new LinkedHashMap<>(getFilters(parent)));
        parent.close();
    }


    protected void restoreFiltersFromModel(final Popup parent, final WidgetInstanceManager widgetInstanceManager)
    {
        final Map<String, FullTextSearchFilter> filters = getFiltersFromModel(widgetInstanceManager);
        getFilters(parent).clear();
        if(MapUtils.isNotEmpty(filters))
        {
            final Set<Map.Entry<String, FullTextSearchFilter>> entries = filters.entrySet();
            entries.forEach(filter -> addNewFilter(parent, widgetInstanceManager, filter.getKey(), filter.getValue()));
        }
    }


    protected Map<String, FullTextSearchFilter> getFiltersFromModel(final WidgetInstanceManager widgetInstanceManager)
    {
        final WidgetModel model = widgetInstanceManager.getModel();
        final Map<String, FullTextSearchFilter> filters = model.getValue(FullTextSearchController.MODEL_FIELD_QUERY_POPUP_FILTERS,
                        Map.class);
        if(MapUtils.isEmpty(filters))
        {
            return new HashMap<>();
        }
        return filters;
    }


    protected FieldQueryFilter buildFieldQueryFilter(final AdvancedSearchData data, final String filterId)
    {
        return new FieldQueryFilter(filterId, data, (String)null);
    }


    protected FieldQueryFilter buildFieldQueryFilter(final AdvancedSearchData data, final String filterId,
                    final FullTextSearchFilter filter)
    {
        return new FieldQueryFilter(filterId, data, filter);
    }


    protected FulltextSearch getFulltextSearchConfig(final Component parent)
    {
        return (FulltextSearch)parent.getAttribute(ATTRIBUTE_CONFIGURATION);
    }


    protected void setFulltextSearchConfig(final Component parent, final FulltextSearch config)
    {
        parent.setAttribute(ATTRIBUTE_CONFIGURATION, config);
    }


    protected DataType getDataType(final Popup parent)
    {
        return (DataType)parent.getAttribute(ATTRIBUTE_DATA_TYPE);
    }


    protected void setDataType(final Popup parent, final DataType dataType)
    {
        parent.setAttribute(ATTRIBUTE_DATA_TYPE, dataType);
    }


    protected AdvancedSearchData getAdvancedSearchData(final Popup parent)
    {
        return (AdvancedSearchData)parent.getAttribute(ATTRIBUTE_ADVANCEDSEARCH_DATA);
    }


    protected void setAdvancedSearchData(final Popup parent, final AdvancedSearchData data)
    {
        parent.setAttribute(ATTRIBUTE_ADVANCEDSEARCH_DATA, data);
    }


    protected Map<String, FullTextSearchFilter> getFilters(final Popup parent)
    {
        Map<String, FullTextSearchFilter> filters = (Map<String, FullTextSearchFilter>)parent.getAttribute(ATTRIBUTE_FILTERS);
        if(filters == null)
        {
            filters = new LinkedHashMap<>();
            parent.setAttribute(ATTRIBUTE_FILTERS, filters);
        }
        return filters;
    }


    protected void setFilters(final Popup parent, final Map<String, FullTextSearchFilter> filters)
    {
        parent.setAttribute(ATTRIBUTE_FILTERS, filters);
    }


    protected Div getFieldQueryPopupBody(final Popup popup)
    {
        return (Div)popup.query(".".concat(SCLASS_FIELDQUERY_POPUP_BODY));
    }


    protected WidgetComponentRenderer<Component, FulltextSearch, FieldQueryFilter> getFieldRenderer()
    {
        return fieldRenderer;
    }


    public void setFieldRenderer(final WidgetComponentRenderer<Component, FulltextSearch, FieldQueryFilter> fieldRenderer)
    {
        this.fieldRenderer = fieldRenderer;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected LocalizedValuesService getLocalizedValuesService()
    {
        return localizedValuesService;
    }


    @Required
    public void setLocalizedValuesService(final LocalizedValuesService localizedValuesService)
    {
        this.localizedValuesService = localizedValuesService;
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


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
