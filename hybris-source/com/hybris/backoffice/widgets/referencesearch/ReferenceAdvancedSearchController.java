/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.referencesearch;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.util.AdvancedSearchDataUtil;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchConditionHandler;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.fest.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;

public class ReferenceAdvancedSearchController extends DefaultWidgetController
{
    protected static final String ADVANCED_SEARCH_PARAM_PREFIX = "advancedSearch/";
    protected static final String COLLECTION_BROWSER_PARAM_PREFIX = "collectionBrowser/";
    protected static final String LABEL_KEY_ITEMS_SELECTED = "referenceadvancedsearch.button.selected";
    protected static final String LABEL_KEY_SELECT = "referenceadvancedsearch.button.select";
    protected static final String SOCKET_INPUT_REFERENCE_SEARCH_CTX = "referenceSearchCtx";
    protected static final String SOCKET_INPUT_RESET_SELECTION = "resetSelection";
    protected static final String SOCKET_OUTPUT_ADVANCED_SEARCH_INIT_CTX = "advancedSearchInitCtx";
    protected static final String SOCKET_OUTPUT_SELECTED_REFERENCES = "selectedReferences";
    protected static final String SOCKET_OUTPUT_CANCEL = "cancel";
    protected static final String SETTING_MULTISELECT = "multiSelect";
    protected static final String SELECTED_ITEMS = "selectedItems";
    protected static final String SELECTED_ITEM = "selectedItem";
    protected static final String CONTEXT = "context";
    protected static final String SELECT_BUTTON = "selectButton";
    protected static final String CANCEL_BUTTON = "cancelButton";
    private static final Logger LOG = LoggerFactory.getLogger(ReferenceAdvancedSearchController.class);
    private static final String ADVANCED_SEARCH_CONFIG_CTX_CODE_KEY = "advancedSearchConfigCtxCode";
    @Wire
    protected Button selectButton;
    @Wire
    protected Widgetslot advancedSearch;
    @Wire
    protected Widgetslot collectionBrowser;
    @WireVariable
    private transient ReferenceEditorSearchConditionHandler referenceEditorSearchConditionHandler;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient AdvancedSearchDataUtil advancedSearchDataUtil;
    @WireVariable
    private transient CockpitConfigurationService cockpitConfigurationService;
    @WireVariable
    private transient BackofficeTypeUtils backofficeTypeUtils;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        restoreWidgetState();
    }


    protected void restoreWidgetState()
    {
        final TypeAwareSelectionContext ctx = getValue(CONTEXT, TypeAwareSelectionContext.class);
        if(ctx != null)
        {
            final List selectedItems = getValue(SELECTED_ITEMS, List.class);
            final Object selectedItem = getValue(SELECTED_ITEM, Object.class);
            onReferenceSearchCtx(ctx);
            if(isMultiselect())
            {
                onMultiReferencesSelected(selectedItems);
            }
            else
            {
                onSingleReferenceSelected(selectedItem);
            }
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_REFERENCE_SEARCH_CTX)
    public void onReferenceSearchCtx(final TypeAwareSelectionContext ctx)
    {
        if(ctx != null)
        {
            setValue(CONTEXT, ctx);
            setValue(SETTING_MULTISELECT, ctx.isMultiSelect());
            updateAdvancedSearchWidget(ctx);
            updateCollectionBrowserWidget(ctx);
            if(StringUtils.isNotBlank(ctx.getTypeCode()))
            {
                sendOutput(SOCKET_OUTPUT_ADVANCED_SEARCH_INIT_CTX, createAdvancedSearchInitCtx(ctx));
            }
        }
    }


    protected void updateAdvancedSearchWidget(final TypeAwareSelectionContext ctx)
    {
        if(ctx != null)
        {
            final boolean settingsChanged = updateWidgetSlotSettings(advancedSearch, ADVANCED_SEARCH_PARAM_PREFIX,
                            ctx.getParameters());
            if(settingsChanged)
            {
                advancedSearch.updateView();
            }
        }
    }


    protected void updateCollectionBrowserWidget(final TypeAwareSelectionContext ctx)
    {
        if(ctx != null)
        {
            boolean settingsChanged = updateWidgetSlotSettings(collectionBrowser, COLLECTION_BROWSER_PARAM_PREFIX,
                            ctx.getParameters());
            final Object currentMultiselectVal = collectionBrowser.getSettings().get(SETTING_MULTISELECT);
            if(ObjectUtils.notEqual(currentMultiselectVal, ctx.isMultiSelect()))
            {
                collectionBrowser.getSettings().put(SETTING_MULTISELECT, ctx.isMultiSelect());
                settingsChanged = true;
            }
            if(settingsChanged)
            {
                collectionBrowser.updateView();
            }
        }
    }


    protected boolean updateWidgetSlotSettings(final Widgetslot slot, final String widgetPrefix, final Map<String, Object> params)
    {
        final AtomicBoolean updated = new AtomicBoolean(false);
        if(slot != null && slot.getWidgetInstance() != null && StringUtils.isNotBlank(widgetPrefix) && MapUtils.isNotEmpty(params))
        {
            final Widget widget = slot.getWidgetInstance().getWidget();
            final TypedSettingsMap settings = widget.getWidgetSettings();
            final String definitionId = widget.getWidgetDefinitionId();
            params.entrySet()//
                            .stream()//
                            .filter(entry -> entry.getKey().startsWith(widgetPrefix))//
                            .forEach(entry -> {
                                final String targetParamName = entry.getKey().substring(widgetPrefix.length());
                                final Object parsedValue = settings.parseIfSetting(definitionId, targetParamName, entry.getValue());
                                if(ObjectUtils.notEqual(settings.get(targetParamName), parsedValue))
                                {
                                    settings.put(targetParamName, parsedValue);
                                    updated.compareAndSet(false, true);
                                }
                            });
        }
        return updated.get();
    }


    @SocketEvent(socketId = SELECTED_ITEMS)
    public void onMultiReferencesSelected(final List<Object> selectedItems)
    {
        setValue(SELECTED_ITEMS, selectedItems);
        updateSelectButtonState();
    }


    @SocketEvent(socketId = SELECTED_ITEM)
    public void onSingleReferenceSelected(final Object selectedItem)
    {
        setValue(SELECTED_ITEM, selectedItem);
        updateSelectButtonState();
    }


    @SocketEvent(socketId = SOCKET_INPUT_RESET_SELECTION)
    public void resetSelection()
    {
        getModel().remove(SELECTED_ITEMS);
        getModel().remove(SELECTED_ITEM);
        updateSelectButtonState();
    }


    protected void updateSelectButtonState()
    {
        final int numberOfItems;
        if(isMultiselect())
        {
            final List selectedItems = getValue(SELECTED_ITEMS, List.class);
            numberOfItems = selectedItems != null ? selectedItems.size() : 0;
        }
        else
        {
            final Object selectedItem = getValue(SELECTED_ITEM, Object.class);
            numberOfItems = selectedItem != null ? 1 : 0;
        }
        if(numberOfItems > 0)
        {
            selectButton.setLabel(getLabel(LABEL_KEY_ITEMS_SELECTED, new Object[]
                            {numberOfItems}));
            selectButton.setDisabled(false);
        }
        else
        {
            selectButton.setLabel(getLabel(LABEL_KEY_SELECT));
            selectButton.setDisabled(true);
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = SELECT_BUTTON)
    public void onSelectButton()
    {
        List referencesToSend = null;
        if(isMultiselect())
        {
            referencesToSend = getValue(SELECTED_ITEMS, List.class);
        }
        else
        {
            final Object selectedItem = getValue(SELECTED_ITEM, Object.class);
            if(selectedItem != null)
            {
                referencesToSend = Lists.newArrayList(selectedItem);
            }
        }
        if(CollectionUtils.isNotEmpty(referencesToSend))
        {
            sendOutput(SOCKET_OUTPUT_SELECTED_REFERENCES, referencesToSend);
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CANCEL_BUTTON)
    public void onCancelButton()
    {
        sendOutput(SOCKET_OUTPUT_CANCEL, getValue(CONTEXT, TypeAwareSelectionContext.class));
    }


    protected AdvancedSearchInitContext createAdvancedSearchInitCtx(final TypeAwareSelectionContext ctx)
    {
        String closestSuperType = ctx.getTypeCode();
        final List availableItems = ctx.getAvailableItems();
        if(!Collections.isEmpty(availableItems))
        {
            closestSuperType = getBackofficeTypeUtils().findClosestSuperType(availableItems);
        }
        final Map<String, Object> conditionsFromConfig = referenceEditorSearchConditionHandler
                        .getSearchConditions(ctx.getParameters());
        final Map<String, Object> referenceSearchContextMap = (Map<String, Object>)ctx.getParameters()
                        .get(TypeAwareSelectionContext.SEARCH_CTX_PARAM);
        final AdvancedSearch advancedSearchConfig = loadAdvancedSearchConfiguration(ctx.getTypeCode());
        final AdvancedSearchData advancedSearchData = buildAdvancedSearchData(ctx.getTypeCode(), conditionsFromConfig,
                        advancedSearchConfig, referenceSearchContextMap);
        final AdvancedSearchInitContext advancedSearchInitContext = new AdvancedSearchInitContext(advancedSearchData,
                        advancedSearchConfig);
        advancedSearchInitContext.setInitialTypeCode(closestSuperType);
        return advancedSearchInitContext;
    }


    /**
     * Provides advanced search data for advanced reference search
     */
    protected AdvancedSearchData buildAdvancedSearchData(final String typeCode, final Map<String, Object> searchConditions,
                    final AdvancedSearch advancedSearch, final Map<String, Object> contextMap)
    {
        final DataType dataType = loadDataTypeForCode(typeCode);
        final AdvancedSearchData advancedSearchData = new AdvancedSearchData(
                        advancedSearchDataUtil.buildAdvancedSearchData(advancedSearch, dataType));
        advancedSearchData.setTypeCode(typeCode);
        final List<SearchConditionData> narrowingConditions = searchConditions.keySet().stream()
                        .filter(key -> !key.equals(ReferenceEditorSearchConditionHandler.PARAM_SEARCH_CONDITION_GROUP_OPERATOR)).map(key -> {
                            final String qualifier = StringUtils.substringBeforeLast(key,
                                            ReferenceEditorSearchConditionHandler.SEARCH_CONDITION_DELIMITER);
                            final ValueComparisonOperator conditionOperator = referenceEditorSearchConditionHandler.getConditionOperator(key);
                            final FieldType fieldType = createFieldType(qualifier, conditionOperator.getOperatorCode(), advancedSearch);
                            final Object value = referenceEditorSearchConditionHandler.getValue(searchConditions, contextMap, key);
                            return new SearchConditionData(fieldType, value, conditionOperator);
                        }).collect(Collectors.toList());
        advancedSearchData.addConditionList(referenceEditorSearchConditionHandler.getSearchOperator(searchConditions),
                        narrowingConditions);
        return advancedSearchData;
    }


    protected FieldType createFieldType(final String qualifier, final String operator, final AdvancedSearch advancedSearch)
    {
        final FieldType fieldType = new FieldType();
        fieldType.setName(qualifier);
        fieldType.setDisabled(true);
        fieldType.setOperator(operator);
        final Optional<FieldType> configureFieldType = advancedSearch.getFieldList().getField().stream()
                        .filter(field -> StringUtils.equals(qualifier, field.getName())).findAny();
        if(configureFieldType.isPresent())
        {
            fieldType.setSortable(configureFieldType.get().isSortable());
            fieldType.setMandatory(configureFieldType.get().isMandatory());
        }
        return fieldType;
    }


    protected AdvancedSearch loadAdvancedSearchConfiguration(final String typeCode)
    {
        AdvancedSearch advancedSearchCfg = null;
        try
        {
            if(advancedSearch != null && advancedSearch.getWidgetInstance() != null)
            {
                final DefaultConfigContext context = new DefaultConfigContext(extractAdvancedSearchConfigComponentName(), typeCode);
                advancedSearchCfg = getCockpitConfigurationService().loadConfiguration(context, AdvancedSearch.class,
                                advancedSearch.getWidgetInstance());
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.error("Error loading cockpit configuration for advanced-search", e);
        }
        return advancedSearchCfg;
    }


    protected String extractAdvancedSearchConfigComponentName()
    {
        String ret = StringUtils.EMPTY;
        final Object rawValue = advancedSearch.getSettings().get(ADVANCED_SEARCH_CONFIG_CTX_CODE_KEY);
        if(rawValue != null)
        {
            ret = ObjectUtils.toString(rawValue);
        }
        return ret;
    }


    protected boolean isMultiselect()
    {
        return BooleanUtils.toBoolean(getValue(SETTING_MULTISELECT, Boolean.class));
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


    public CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    protected BackofficeTypeUtils getBackofficeTypeUtils()
    {
        return backofficeTypeUtils;
    }
}
