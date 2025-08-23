/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl.renderer;

import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.widgets.advancedsearch.AbstractSearchController;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchController;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchOperatorService;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchDataConditionEvaluator;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionDataList;
import com.hybris.cockpitng.common.model.TypeSelectorTreeModel;
import com.hybris.cockpitng.common.renderer.TypeSelectorTreeItemRenderer;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.Parameter;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.SortFieldType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.bool.AbstractBooleanEditorRenderer;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Tree;

public class AdvancedSearchRenderer
{
    public static final String RADIO_SORT_ORDER_ASC_ATTR = "radio-sort-order-asc";
    public static final String TOP = "Top";
    public static final String BOTTOM = "Bottom";
    public static final String ADD_ROW_POSITION = "addRowPosition";
    public static final String YW_ADVANCED_SEARCH_LOCALIZED = "yw-advancedsearch-localized";
    public static final String YW_ADVANCED_SEARCH_ASC = "yw-advancedsearch-asc";
    public static final String YW_ADVANCED_SEARCH_DESC = "yw-advancedsearch-desc";
    public static final String YW_ADVANCED_SEARCH_LINE = "yw-advancedsearch-line";
    public static final String YW_ADVANCED_SEARCH_LAST_ROW_FOR_CONDITION = "yw-advancedsearch-last-row-for-condition";
    public static final String YW_ADVANCED_SEARCH_ROWSPAN = "yw-advanced-search-rowspan";
    public static final String YW_ADVANCED_SEARCH_OPERATOR = "yw-advancedsearch-operator";
    public static final String YW_ADVANCED_SEARCH_SORTORDER_DIV = "yw-advancedsearch-sortorder-div";
    public static final String IS_NESTED_OBJECT_CREATION_DISABLED_SETTING = "isNestedObjectCreationDisabled";
    public static final String DISABLE_ATTRIBUTES_COMPARATOR = "disableAttributesComparator";
    public static final String SCLASS_EDITOR_MANDATORY_STYLE = "yw-advancedsearch-mandatory-field";
    public static final String DISABLE_SORT_ORDER = "disableSortOrder";
    public static final String SORT_ORDER_COLUMN_ID = "sortOrderColumn";
    public static final String SCLASS_EDITOR = "yw-advancedsearch-editor";
    private static final String SCLASS_ADD_BUTTON_STYLE = "yw-advancedsearch-add-btn";
    private static final String SCLASS_REMOVE_BUTTON_STYLE = "yw-advancedsearch-delete-btn ye-delete-btn";
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchRenderer.class);
    private static final SearchConditionData GRID_ADD_LINE_HOLDER = new SearchConditionData(null, null, null);
    private static final String INITIAL_TYPE_CODE = "initialTypeCode";
    private static final String DATE_FORMAT = "dateFormat";
    private static final String EQUALS_COMPARES_EXACT_DATE = "equalsComparesExactDate";
    private static final String COMPARES_EXACT_DATE = "comparesExactDate";
    private final TypeFacade typeFacade;
    private final LabelService labelService;
    private final AdvancedSearchOperatorService advancedSearchOperatorService;
    private final CockpitLocaleService cockpitLocaleService;
    private final PermissionFacade permissionFacade;
    private final Map<SearchAttributeDescriptor, Editor> fieldEditors = new HashMap<>();
    private Consumer<Event> editorsEventConsumer;
    private WidgetInstanceManager widgetInstanceManager;


    public AdvancedSearchRenderer(final TypeFacade typeFacade, final LabelService labelService,
                    final AdvancedSearchOperatorService advancedSearchOperatorService, final PermissionFacade permissionFacade,
                    final CockpitLocaleService cockpitLocaleService)
    {
        this(typeFacade, labelService, null, advancedSearchOperatorService, permissionFacade, cockpitLocaleService, null);
    }


    public AdvancedSearchRenderer(final TypeFacade typeFacade, final LabelService labelService,
                    final WidgetInstanceManager widgetInstanceManager, final AdvancedSearchOperatorService advancedSearchOperatorService,
                    final PermissionFacade permissionFacade, final CockpitLocaleService cockpitLocaleService)
    {
        this(typeFacade, labelService, widgetInstanceManager, advancedSearchOperatorService, permissionFacade, cockpitLocaleService,
                        null);
    }


    /**
     * @param editorsEventConsumer
     *           consumes an event when an enter is pressed in a search/comparator field. When the consumer is not passed
     *           support there is no enter support for search fields.
     */
    public AdvancedSearchRenderer(final TypeFacade typeFacade, final LabelService labelService,
                    final WidgetInstanceManager widgetInstanceManager, final AdvancedSearchOperatorService advancedSearchOperatorService,
                    final PermissionFacade permissionFacade, final CockpitLocaleService cockpitLocaleService,
                    final Consumer<Event> editorsEventConsumer)
    {
        this.typeFacade = typeFacade;
        this.labelService = labelService;
        this.widgetInstanceManager = widgetInstanceManager;
        this.advancedSearchOperatorService = advancedSearchOperatorService;
        this.permissionFacade = permissionFacade;
        this.cockpitLocaleService = cockpitLocaleService;
        this.editorsEventConsumer = editorsEventConsumer;
    }


    private static Column getSortOrderColumn(final Grid container)
    {
        Column sortOrderColumn = null;
        for(final Component column : container.getColumns().getChildren())
        {
            if(column.getId().equalsIgnoreCase(SORT_ORDER_COLUMN_ID))
            {
                sortOrderColumn = (Column)column;
            }
        }
        return sortOrderColumn;
    }


    private static EventListener<SelectEvent> createSelectionListenerForOperatorsInAddAttributeRow(final Row row,
                    final FieldType fieldType)
    {
        return selectEvent -> {
            if(selectEvent.getSelectedItems() != null && !selectEvent.getSelectedItems().isEmpty())
            {
                final ValueComparisonOperator operator = (ValueComparisonOperator)selectEvent.getSelectedObjects().iterator().next();
                final Editor theEditor = getCurrentEditorForAddAttributeRow(row);
                updateEditorStateAccordingToOperator(theEditor, operator, fieldType);
            }
        };
    }


    private static void updateEditorStateAccordingToOperator(final Editor editor,
                    final ValueComparisonOperator valueComparisonOperator, final FieldType fieldType)
    {
        editor.setReadOnly(!valueComparisonOperator.isRequireValue() || fieldType.isDisabled());
        updateDateEditorFormatAccordingToOperator(editor, valueComparisonOperator);
    }


    private static void updateDateEditorFormatAccordingToOperator(final Editor editor,
                    final ValueComparisonOperator valueComparisonOperator)
    {
        final Object equalsComparesExactDateObject = editor.getParameters().get(EQUALS_COMPARES_EXACT_DATE);
        final Object comparesExactDateObject = editor.getParameters().get(COMPARES_EXACT_DATE);
        final boolean hasEqualsComparesExactDateParameter = StringUtils.equals(valueComparisonOperator.getOperatorCode(), "equals")
                        && equalsComparesExactDateObject != null;
        final boolean useEqualsComparesExactDate = hasEqualsComparesExactDateParameter
                        && BooleanUtils.isTrue(BooleanUtils.toBooleanObject(equalsComparesExactDateObject.toString()));
        final boolean useComparesExactDate = !hasEqualsComparesExactDateParameter && comparesExactDateObject != null
                        && BooleanUtils.isTrue(BooleanUtils.toBooleanObject(comparesExactDateObject.toString()));
        if(useEqualsComparesExactDate || useComparesExactDate)
        {
            editor.addParameter(DATE_FORMAT, "medium");
        }
        else
        {
            editor.addParameter(DATE_FORMAT, "medium+none");
        }
        editor.reload();
    }


    private static Editor getCurrentEditorForAddAttributeRow(final Row row)
    {
        final int editorPosition = 2;
        return (Editor)row.getChildren().get(editorPosition);
    }


    private static void setEditorParametersFromConfiguration(final Editor editor, final FieldType field)
    {
        for(final Parameter parameter : field.getEditorParameter())
        {
            editor.addParameter(parameter.getName(), parameter.getValue());
        }
    }


    private static void hideNullOptionForBooleanEditor(final Editor editor)
    {
        if(StringUtils.equals(editor.getType(), Boolean.class.getCanonicalName()))
        {
            editor.addParameter(AbstractBooleanEditorRenderer.SHOW_OPTIONAL_FIELD_PARAM, Boolean.FALSE.toString());
        }
    }


    private static void markEmptyMandatoryField(final Editor editor)
    {
        final HtmlBasedComponent parent = (HtmlBasedComponent)editor.getParent();
        UITools.modifySClass(parent, SCLASS_EDITOR_MANDATORY_STYLE, true);
        Clients.scrollIntoView(editor);
    }


    private static void unmarkMandatoryField(final Editor editor)
    {
        final HtmlBasedComponent parent = (HtmlBasedComponent)editor.getParent();
        UITools.modifySClass(parent, SCLASS_EDITOR_MANDATORY_STYLE, false);
    }


    private static List<SearchConditionData> getSearchConditionData(final AdvancedSearchData advancedSearch)
    {
        final List<SearchConditionData> conditions = new ArrayList<>();
        advancedSearch.getSearchFields().stream()
                        .filter(field -> ObjectUtils.notEqual(AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY, field))
                        .forEach(field -> conditions.addAll(advancedSearch.getConditions(field)));
        return conditions;
    }


    private boolean isAddAttributeRowVisible()
    {
        return !"None".equalsIgnoreCase(getAddRowPosition());
    }


    protected SortData extractSortData(final AdvancedSearch configuration)
    {
        if(configuration != null && configuration.getSortField() != null)
        {
            final SortFieldType sortField = configuration.getSortField();
            return new SortData(sortField.getName(), sortField.isAsc());
        }
        return null;
    }


    public void renderVisible(final Grid container, final Radiogroup sortControlCnt, final Actions actionSlot,
                    final AdvancedSearch configuration, final DataType dataType)
    {
        final AdvancedSearchData advancedSearchData = getSearchDataFromModel();
        fieldEditors.clear();
        final Column sortOrderColumn = getSortOrderColumn(container);
        container.setModel(prepareGridModel());
        adjustSortOrderColumnVisibility(sortOrderColumn, advancedSearchData);
        container.setRowRenderer(createConditionRowRenderer(container, sortControlCnt, configuration, dataType, sortOrderColumn));
    }


    protected RowRenderer<SearchConditionData> createConditionRowRenderer(final Grid container, final Radiogroup sortControlCnt,
                    final AdvancedSearch configuration, final DataType dataType, final Column sortOrderColumn)
    {
        final AdvancedSearchData advancedSearchData = getSearchDataFromModel();
        return new SearchConditionRenderer(container, sortControlCnt, configuration, dataType, sortOrderColumn, advancedSearchData);
    }


    private void renderAddAttributeRow(final Grid container, final Row row, final int rowIndex, final SearchConditionData data,
                    final AdvancedSearch configuration, final DataType dataType)
    {
        UITools.modifySClass(row, "yw-add-field-row", true);
        YTestTools.modifyYTestId(row, "addFieldRow");
        final Collection<? extends FieldType> selectableAttributes = prepareAlphabeticallySortedAttributeList(
                        configuration.getFieldList() != null ? configuration.getFieldList().getField() : new ArrayList<>());
        if(CollectionUtils.isEmpty(selectableAttributes))
        {
            final Cell cell = new Cell();
            cell.appendChild(new Label(widgetInstanceManager.getLabel("cannotSpecifyAnySearchAttribute", new Object[]
                            {labelService.getObjectLabel(dataType.getCode())})));
            row.appendChild(cell);
            cell.setColspan(5);
            return;
        }
        final FieldType selectedField = selectableAttributes.iterator().next();
        final Combobox attSelector = new Combobox();
        prepareAttributeSelectorCombobox(attSelector, selectableAttributes, selectedField);
        final Button addAttribute = new Button();
        UITools.modifySClass(addAttribute, SCLASS_ADD_BUTTON_STYLE, true);
        YTestTools.modifyYTestId(addAttribute, "addFieldBtn");
        final String selectedFieldName;
        final String selectedFieldOperator;
        if(selectedField != null)
        {
            selectedFieldName = selectedField.getName();
            selectedFieldOperator = selectedField.getOperator();
        }
        else
        {
            selectedFieldName = "";
            selectedFieldOperator = "";
        }
        final ValueComparisonOperator valueComparisonOperator = advancedSearchOperatorService
                        .findMatchingOperator(dataType.getAttribute(selectedFieldName), selectedFieldOperator);
        final Editor editor = createEditor(selectedField, rowIndex, data, true);
        YTestTools.modifyYTestId(editor, "addValueSelector");
        updateEditorStateAccordingToOperator(editor, valueComparisonOperator, selectedField);
        final Cell cellOperator = new Cell();
        UITools.modifySClass(cellOperator, YW_ADVANCED_SEARCH_LINE, true);
        final Combobox operatorSelector = createOperator(selectedFieldName, valueComparisonOperator,
                        createSelectionListenerForOperatorsInAddAttributeRow(row, selectedField));
        YTestTools.modifyYTestId(operatorSelector, "addOperatorSelector");
        UITools.modifySClass(operatorSelector, YW_ADVANCED_SEARCH_OPERATOR, true);
        cellOperator.appendChild(operatorSelector);
        addAttribute.addEventListener(Events.ON_CLICK, event -> {
            final FieldType fieldType = attSelector.getSelectedItem().getValue();
            if(!fieldType.isDisabled())
            {
                final Comboitem selectedItem = operatorSelector.getSelectedItem();
                final ValueComparisonOperator operator = selectedItem.getValue();
                final Editor theEditor = getCurrentEditorForAddAttributeRow(row);
                final Object fieldValue = theEditor.getValue();
                getSearchDataFromModel().addCondition(fieldType, operator, fieldValue);
                container.setModel(prepareGridModel());
            }
        });
        final Cell emptyCellToSatisfyTogglingVisibilityOfSortColumn = new Cell();
        row.appendChild(attSelector);
        row.appendChild(cellOperator);
        row.appendChild(editor);
        row.appendChild(emptyCellToSatisfyTogglingVisibilityOfSortColumn);
        row.appendChild(addAttribute);
        final EventListener<Event> changeTypeEvent = event -> Optional.ofNullable(attSelector.getSelectedItem())
                        .<FieldType>map(Comboitem::getValue).ifPresent(fieldType -> {
                            final String fieldName = fieldType.getName();
                            final ValueComparisonOperator matchingOperator = advancedSearchOperatorService
                                            .findMatchingOperator(dataType.getAttribute(fieldName), fieldType.getOperator());
                            final ListModelList<ValueComparisonOperator> model = new ListModelList<>();
                            final Collection<ValueComparisonOperator> available = advancedSearchOperatorService
                                            .getAvailableOperators(dataType.getAttribute(fieldType.getName()));
                            model.addAll(available);
                            final Component currentEditor = getCurrentEditorForAddAttributeRow(row);
                            final Editor newEditor = createEditor(fieldType, rowIndex, data, true);
                            row.insertBefore(newEditor, currentEditor);
                            row.removeChild(currentEditor);
                            if(available.contains(matchingOperator))
                            {
                                model.setSelection(Collections.singletonList(matchingOperator));
                                updateEditorStateAccordingToOperator(newEditor, matchingOperator, fieldType);
                            }
                            operatorSelector.setModel(model);
                        });
        attSelector.addEventListener(Events.ON_CHANGE, changeTypeEvent);
        attSelector.addEventListener(Events.ON_OK, changeTypeEvent);
    }


    private void prepareAttributeSelectorCombobox(final Combobox attSelector,
                    final Collection<? extends FieldType> selectableAttributes, final FieldType selectedField)
    {
        UITools.modifySClass(attSelector, "yw-additional-attributes-selector", true);
        final ListModelList<FieldType> attributeModel = new ListModelList<>();
        attributeModel.addAll(selectableAttributes);
        attributeModel.setSelection(selectedField != null ? Collections.singletonList(selectedField) : Collections.emptyList());
        attSelector.setModel(attributeModel);
        YTestTools.modifyYTestId(attSelector, "addAttributeSelector");
        attSelector.setReadonly(true);
        attSelector.setItemRenderer((ComboitemRenderer<FieldType>)(item, data, index) -> {
            item.setDisabled(isFieldDisabled(data));
            item.setLabel(getAttributeLabel(data.getName()));
            item.setValue(data);
        });
    }


    private Collection<? extends FieldType> prepareAlphabeticallySortedAttributeList(final List<FieldType> fields)
    {
        final DataType dataType = getDataType();
        final Predicate<FieldType> searchableAttribute = fieldType -> {
            final DataAttribute attribute = dataType.getAttribute(fieldType.getName());
            return attribute != null && attribute.isSearchable()
                            && getPermissionFacade().canReadProperty(dataType.getCode(), fieldType.getName());
        };
        final Map<String, String> labelsCache = getAttributesLabels(fields);
        final Comparator<FieldType> alphabeticalComparator = (first, second) -> {
            final String fName = StringUtils.defaultIfBlank(labelsCache.get(first.getName()), StringUtils.EMPTY);
            final String sName = StringUtils.defaultIfBlank(labelsCache.get(second.getName()), StringUtils.EMPTY);
            return fName.compareToIgnoreCase(sName);
        };
        return fields.stream().filter(searchableAttribute).sorted(alphabeticalComparator).collect(Collectors.toList());
    }


    @InextensibleMethod
    private Map<String, String> getAttributesLabels(final List<FieldType> fields)
    {
        final String typeCode = getDataType().getCode();
        if(StringUtils.isNotBlank(typeCode) && typeCode.indexOf('.') == -1)
        {
            final Map<String, String> fieldLabelMap = new HashMap();
            fields.forEach(field -> {
                if(!fieldLabelMap.containsKey(field.getName()))
                {
                    fieldLabelMap.put(field.getName(), labelService.getObjectLabel(typeCode + '.' + field.getName()));
                }
            });
            return fieldLabelMap;
        }
        return new HashMap();
    }


    protected DataType getDataType()
    {
        try
        {
            return typeFacade.load(getSearchDataFromModel().getTypeCode());
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }


    protected DataType getInitialDataType()
    {
        try
        {
            final String initialDataType = getWidgetInstanceManager().getModel().getValue(INITIAL_TYPE_CODE, String.class);
            if(initialDataType != null)
            {
                return getTypeFacade().load(initialDataType);
            }
            return null;
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }


    public ListModelList<SearchConditionData> prepareGridModel()
    {
        final AdvancedSearchData searchData = getSearchDataFromModel();
        final ListModelList<SearchConditionData> gridModel = new ListModelList<>();
        if(TOP.equalsIgnoreCase(getAddRowPosition()))
        {
            gridModel.add(GRID_ADD_LINE_HOLDER);
        }
        searchData.getSearchFields().stream()
                        .filter(searchField -> ObjectUtils.notEqual(AdvancedSearchData.ORPHANED_SEARCH_CONDITIONS_KEY, searchField))
                        .filter(searchField -> getPermissionFacade().canReadProperty(searchData.getTypeCode(), searchField))
                        .forEach(searchField -> {
                            final List<SearchConditionData> scData = searchData.getConditions(searchField);
                            gridModel.addAll(
                                            scData.stream().filter(entry -> !(entry instanceof SearchConditionDataList)).collect(Collectors.toList()));
                        });
        if(BOTTOM.equalsIgnoreCase(getAddRowPosition()))
        {
            gridModel.add(GRID_ADD_LINE_HOLDER);
        }
        return gridModel;
    }


    public String getAddRowPosition()
    {
        return widgetInstanceManager.getWidgetSettings().getString(ADD_ROW_POSITION);
    }


    private Component createRemoveLine(final int attributeRowIndex, final Grid container,
                    final AdvancedSearchData advancedSearchData, final SearchConditionData data)
    {
        final Div removeContainer = new Div();
        UITools.modifySClass(removeContainer, "yw-remove-container", true);
        if(!data.getFieldType().isMandatory())
        {
            final Button remove = new Button();
            remove.addEventListener(Events.ON_CLICK, event -> {
                advancedSearchData.removeCondition(getActualIndexOfConditionInAdvancedSearchData(attributeRowIndex));
                container.setModel(prepareGridModel());
            });
            UITools.modifySClass(remove, SCLASS_REMOVE_BUTTON_STYLE, true);
            YTestTools.modifyYTestId(remove, "remove-" + data.getFieldType().getName() + "-" + attributeRowIndex);
            removeContainer.appendChild(remove);
        }
        return removeContainer;
    }


    private int getActualIndexOfConditionInAdvancedSearchData(final int rowIndex)
    {
        return rowIndex - (TOP.equalsIgnoreCase(getAddRowPosition()) ? 1 : 0);
    }


    protected Div createSortControls(final Radiogroup radiogroup, final SearchConditionData data, final SortData sortData)
    {
        final FieldType field = data.getFieldType();
        final Div sortControls = new Div();
        if(isSortPanelsVisible(field))
        {
            UITools.modifySClass(sortControls, YW_ADVANCED_SEARCH_SORTORDER_DIV, true);
            final Radio ascendingBtn = new Radio(StringUtils.EMPTY);
            ascendingBtn.setId(String.format("%s-asc-radio", field.getName()));
            ascendingBtn.setValue(field.getName());
            ascendingBtn.setSclass(YW_ADVANCED_SEARCH_ASC);
            ascendingBtn.setRadiogroup(radiogroup);
            ascendingBtn.setAttribute(RADIO_SORT_ORDER_ASC_ATTR, Boolean.TRUE);
            final Radio descendingBtn = new Radio(StringUtils.EMPTY);
            descendingBtn.setId(String.format("%s-desc-radio", field.getName()));
            descendingBtn.setSclass(YW_ADVANCED_SEARCH_DESC);
            descendingBtn.setValue(field.getName());
            descendingBtn.setRadiogroup(radiogroup);
            descendingBtn.setAttribute(RADIO_SORT_ORDER_ASC_ATTR, Boolean.FALSE);
            sortControls.appendChild(ascendingBtn);
            sortControls.appendChild(descendingBtn);
            if(sortData != null && Objects.equals(field.getName(), sortData.getSortAttribute()))
            {
                ascendingBtn.setSelected(sortData.isAscending());
                descendingBtn.setSelected(!sortData.isAscending());
            }
        }
        return sortControls;
    }


    protected boolean isSortPanelsVisible(final FieldType field)
    {
        return isSortable(field) && isSortPanelEnabled();
    }


    protected boolean isSortPanelEnabled()
    {
        return BooleanUtils.isNotTrue(Boolean.valueOf(widgetInstanceManager.getWidgetSettings().getBoolean(DISABLE_SORT_ORDER)));
    }


    protected boolean isSortable(final FieldType field)
    {
        boolean ret = false;
        if(field != null && BooleanUtils.isNotFalse(Boolean.valueOf(field.isSortable())))
        {
            final DataAttribute attribute = getDataAttribute(field);
            if(attribute != null && attribute.isSearchable())
            {
                final DataType valueType = attribute.getValueType();
                ret = DataAttribute.AttributeType.SINGLE.equals(attribute.getAttributeType())
                                && (valueType.isAtomic() || valueType.isEnum());
            }
        }
        return ret;
    }


    protected Editor createEditor(final FieldType field, final int rowIndex, final SearchConditionData data)
    {
        return createEditor(field, rowIndex, data, false);
    }


    private boolean isNestedObjectCreationDisabled()
    {
        return widgetInstanceManager.getWidgetSettings().getBoolean(IS_NESTED_OBJECT_CREATION_DISABLED_SETTING);
    }


    protected Editor createEditor(final FieldType field, final int rowIndex, final SearchConditionData data,
                    final boolean isEditorInAddAttributeRow)
    {
        final DataAttribute genericAttribute = getDataAttribute(field);
        final Editor editor = new Editor();
        final Set<Locale> writableLocales = getPermissionFacade().getAllWritableLocalesForCurrentUser();
        final Set<Locale> readableLocales = new HashSet<>(getPermissionFacade().getAllReadableLocalesForCurrentUser());
        readableLocales.addAll(writableLocales);
        editor.setWritableLocales(writableLocales);
        editor.setReadableLocales(readableLocales);
        editor.setLocalized(genericAttribute.isLocalized());
        editor.setOrdered(genericAttribute.isOrdered());
        editor.setReadOnly(field.isDisabled());
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setType(resolveEditorType(genericAttribute));
        if(isReferenceEditor(editor))
        {
            disableDblClickOnReferenceEditor(editor);
            applyReferenceAdvancedSearch(editor);
        }
        editor.setOptional(isNullable(genericAttribute));
        editor.setNestedObjectCreationDisabled(isNestedObjectCreationDisabled());
        editor.setPartOf(genericAttribute.isPartOf());
        final String qualifier = genericAttribute.getQualifier();
        YTestTools.modifyYTestId(editor, "editor_" + qualifier);
        final AdvancedSearchData searchData = getSearchDataFromModel();
        if(!isEditorInAddAttributeRow)
        {
            if(genericAttribute.isLocalized())
            {
                UITools.modifySClass(editor, YW_ADVANCED_SEARCH_LOCALIZED, true);
                editor.addEventListener(Editor.ON_VALUE_CHANGED,
                                event -> updateSearchDataForLocalizedValue(searchData, rowIndex, editor.getValue()));
                editor.addEventListener(Events.ON_SELECT,
                                event -> updateLocalizedEditorValueOnLanguageSelected(editor, event.getData(), searchData, rowIndex));
            }
            else
            {
                editor.addEventListener(Editor.ON_VALUE_CHANGED, event -> searchData
                                .getCondition(getActualIndexOfConditionInAdvancedSearchData(rowIndex)).updateValue(editor.getValue()));
            }
            editor.setValue(data.getValue());
        }
        if(StringUtils.isNotBlank(field.getEditor()))
        {
            editor.setDefaultEditor(field.getEditor());
        }
        setEditorParametersFromConfiguration(editor, field);
        hideNullOptionForBooleanEditor(editor);
        editor.afterCompose();
        UITools.modifySClass(editor, SCLASS_EDITOR, true);
        if(!isEditorInAddAttributeRow)
        {
            addEnterSupport(editor);
        }
        return editor;
    }


    protected DataAttribute getDataAttribute(final FieldType field)
    {
        final DataType genericType = getDataType();
        return genericType.getAttribute(field.getName());
    }


    protected void updateSearchDataForLocalizedValue(final AdvancedSearchData searchData, final int rowIndex,
                    final Object editorsValue)
    {
        final SearchConditionData scData = searchData.getCondition(getActualIndexOfConditionInAdvancedSearchData(rowIndex));
        if(editorsValue instanceof Map)
        {
            final Map<Locale, Object> localizedValue = ((Map<Locale, Object>)editorsValue);
            final Optional<Locale> currentLocale = localizedValue.keySet().stream().findFirst();
            final Optional<Map.Entry<Locale, Object>> first = localizedValue.entrySet().stream()
                            .filter(entry -> entry.getValue() != null).findFirst();
            if(first.isPresent())
            {
                scData.updateLocalizedValue(first.get().getKey(), first.get().getValue());
            }
            else if(currentLocale.isPresent())
            {
                scData.updateLocalizedValue(currentLocale.get(), null);
            }
            else
            {
                scData.resetLocalizedValues();
            }
        }
        else
        {
            scData.resetLocalizedValues();
        }
    }


    protected void updateLocalizedEditorValueOnLanguageSelected(final Editor editor, final Object eventData,
                    final AdvancedSearchData searchData, final int rowIndex)
    {
        if(eventData instanceof Locale)
        {
            final Map<Locale, Object> updateEditorValue = new HashMap<>();
            final Object value = editor.getValue();
            if(value instanceof Map && !((Map)value).isEmpty())
            {
                final Map<Locale, Object> currentEditorValue = (Map<Locale, Object>)value;
                final Optional<Object> foundValue = currentEditorValue.values().stream().filter(Objects::nonNull).findFirst();
                if(foundValue.isPresent())
                {
                    updateEditorValue.put((Locale)eventData, foundValue.get());
                }
                else
                {
                    updateEditorValue.put((Locale)eventData, null);
                }
                editor.setValue(updateEditorValue);
                updateSearchDataForLocalizedValue(searchData, rowIndex, updateEditorValue);
            }
            else
            {
                updateEditorValue.put((Locale)eventData, null);
                editor.setValue(updateEditorValue);
                updateSearchDataForLocalizedValue(searchData, rowIndex, updateEditorValue);
            }
        }
    }


    protected void addEnterSupport(final Component component)
    {
        if(editorsEventConsumer != null)
        {
            if(component instanceof Editor)
            {
                component.addEventListener(Editor.ON_EDITOR_EVENT, event -> {
                    if(Events.ON_OK.equals(event.getData()) || EditorListener.ENTER_PRESSED.equals(event.getData()))
                    {
                        editorsEventConsumer.accept(event);
                    }
                });
            }
            else if(component instanceof Combobox)
            {
                component.addEventListener(Events.ON_OK, event -> editorsEventConsumer.accept(event));
            }
        }
    }


    protected boolean isReferenceEditor(final Editor editor)
    {
        return StringUtils.startsWith(editor.getType(), "Reference");
    }


    protected void disableDblClickOnReferenceEditor(final Editor editor)
    {
        editor.addParameter(AbstractReferenceEditor.PARAM_DISABLE_DISPLAYING_DETAILS, Boolean.TRUE);
    }


    /**
     * Uses {@link AbstractReferenceEditor#PARAM_REFERENCE_ADVANCED_SEARCH_ENABLED} from the widget's settings.
     */
    protected void applyReferenceAdvancedSearch(final Editor editor)
    {
        editor.addParameter(AbstractReferenceEditor.PARAM_REFERENCE_ADVANCED_SEARCH_ENABLED, widgetInstanceManager
                        .getWidgetSettings().getBoolean(AbstractReferenceEditor.PARAM_REFERENCE_ADVANCED_SEARCH_ENABLED));
    }


    protected Map<Pattern, String> prepareAdvancedSearchEditorMappings()
    {
        return ImmutableMap.<Pattern, String>builder().put(EditorUtils.getReferenceSinglingMapping()).build();
    }


    protected String resolveEditorType(final DataAttribute genericAttribute)
    {
        return EditorUtils.getEditorType(genericAttribute, true, prepareAdvancedSearchEditorMappings());
    }


    private Combobox createOperator(final String fieldTypeName, final ValueComparisonOperator selectedOperator,
                    final EventListener<SelectEvent> selectionListener)
    {
        final DataAttribute attribute = getDataType().getAttribute(fieldTypeName);
        final Combobox operators = new Combobox();
        operators.setReadonly(true);
        final ListModelList<ValueComparisonOperator> model = new ListModelList<>();
        final Collection<ValueComparisonOperator> availableOperators = advancedSearchOperatorService
                        .getAvailableOperators(attribute);
        if(selectedOperator != null && availableOperators.contains(selectedOperator))
        {
            model.setSelection(Collections.singletonList(selectedOperator));
        }
        model.addAll(availableOperators);
        operators.setModel(model);
        operators.setItemRenderer((item, operator, index) -> {
            item.setLabel(Labels.getLabel(((ValueComparisonOperator)operator).getLabelKey()));
            item.setValue(operator);
        });
        if(selectionListener != null)
        {
            operators.addEventListener(Events.ON_SELECT, selectionListener);
        }
        return operators;
    }


    public void renderTypeSelector(final Bandbox typeSelectorBandbox, final Tree typeSelector, final DataType rootType)
    {
        final DataType initialDataType = getInitialDataType();
        final DataType dataType;
        if(initialDataType != null)
        {
            dataType = initialDataType;
            getWidgetInstanceManager().getModel().setValue(INITIAL_TYPE_CODE, null);
        }
        else
        {
            dataType = getDataType();
        }
        final TypeSelectorTreeModel model = createTypeSelectorTreeModel(rootType, getTypeFacade(), getPermissionFacade(),
                        hideTypesWithoutClazz());
        model.setSelection(Collections.singletonList(dataType));
        typeSelector.setModel(model);
        typeSelector.setItemRenderer(new TypeSelectorTreeItemRenderer(labelService, widgetInstanceManager));
        typeSelectorBandbox.setText(labelService.getObjectLabel(dataType.getCode()));
        typeSelectorBandbox.setReadonly(false);
        addTypeSelectorBandboxEventListeners(typeSelectorBandbox, typeSelector, model);
        typeSelector.addEventListener(Events.ON_SELECT, (EventListener<SelectEvent<?, DataType>>)event -> {
            final Set<DataType> selectedObjects = event.getSelectedObjects();
            if(selectedObjects.size() == 1)
            {
                final String typeCode = selectedObjects.iterator().next().getCode();
                widgetInstanceManager.getModel().put(AdvancedSearchController.MODEL_SUBTYPE_CHANGED, typeCode);
                typeSelectorBandbox.setText(labelService.getObjectLabel(typeCode));
                typeSelectorBandbox.close();
            }
        });
    }


    protected TypeSelectorTreeModel createTypeSelectorTreeModel(final DataType rootType, final TypeFacade typeFacade,
                    final PermissionFacade permissionFacade, final boolean hideTypesWithoutClazz)
    {
        return new TypeSelectorTreeModel(rootType, typeFacade, permissionFacade, hideTypesWithoutClazz);
    }


    protected boolean hideTypesWithoutClazz()
    {
        return getWidgetInstanceManager().getWidgetSettings()
                        .getBoolean(AdvancedSearchController.WIDGET_SETTING_HIDE_TYPES_WITHOUT_CLAZZ);
    }


    protected void addTypeSelectorBandboxEventListeners(final Bandbox typeSelectorBandbox, final Tree typeSelector,
                    final TypeSelectorTreeModel model)
    {
        typeSelectorBandbox.addEventListener(Events.ON_BLUR,
                        event -> handleTypeSelectorBandboxOnBlur(event, typeSelectorBandbox, model, typeSelector));
        typeSelectorBandbox.addEventListener(Events.ON_FOCUS,
                        event -> handleTypeSelectorBandboxOnFocus(event, typeSelectorBandbox, model, typeSelector));
        typeSelectorBandbox.addEventListener(Events.ON_CHANGING,
                        event -> handleTypeSelectorBandboxOnChanging(event, typeSelectorBandbox, model, typeSelector));
    }


    protected void handleTypeSelectorBandboxOnChanging(final Event event, final Bandbox typeSelectorBandbox,
                    final TypeSelectorTreeModel model, final Tree typeSelector)
    {
        changeTypeSelectorModel(model, typeSelector, ((InputEvent)event).getValue());
        UITools.postponeExecution(typeSelector, () -> {
            if(typeSelectorBandbox.getPopup() == null)
            {
                typeSelectorBandbox.open();
            }
        });
    }


    protected void handleTypeSelectorBandboxOnBlur(final Event event, final Bandbox typeSelectorBandbox,
                    final TypeSelectorTreeModel model, final Tree typeSelector)
    {
        typeSelectorBandbox.setText(labelService.getObjectLabel(getSearchDataFromModel().getTypeCode()));
        changeTypeSelectorModel(model, typeSelector, StringUtils.EMPTY);
    }


    protected void changeTypeSelectorModel(final TypeSelectorTreeModel model, final Tree typeSelector, final String filter)
    {
        final TypeSelectorTreeModel newModel = createTypeSelectorTreeModel(model.getRootType(), getTypeFacade(),
                        getPermissionFacade(), hideTypesWithoutClazz());
        newModel.setFilter(filter);
        newModel.setFilterLocale(getCockpitLocaleService().getCurrentLocale());
        typeSelector.setModel(newModel);
    }


    protected void handleTypeSelectorBandboxOnFocus(final Event event, final Bandbox typeSelectorBandbox,
                    final TypeSelectorTreeModel model, final Tree typeSelector)
    {
        typeSelectorBandbox.select();
    }


    protected String getAttributeLabel(final String qualifier)
    {
        final String typeCode = getDataType().getCode();
        if(StringUtils.isNotBlank(typeCode) && typeCode.indexOf('.') == -1)
        {
            final String locAttributeLabel = labelService.getObjectLabel(typeCode + '.' + qualifier);
            if(locAttributeLabel != null)
            {
                return locAttributeLabel;
            }
        }
        return qualifier;
    }


    public void rendererGlobalOperator(final Combobox operatorSelector)
    {
        final ListModelList<ValueComparisonOperator> model = new ListModelList<>();
        model.add(ValueComparisonOperator.OR);
        model.add(ValueComparisonOperator.AND);
        final ValueComparisonOperator selectedOperator = getSearchDataFromModel().getGlobalOperator();
        model.setSelection(Collections.singletonList(selectedOperator));
        operatorSelector.setModel(model);
        operatorSelector.setItemRenderer((ComboitemRenderer<ValueComparisonOperator>)(item, data, index) -> {
            item.setLabel(Labels.getLabel(data.getLabelKey()));
            item.setValue(data);
        });
    }


    protected AdvancedSearchData getSearchDataFromModel()
    {
        return widgetInstanceManager.getModel().getValue(AbstractSearchController.SEARCH_MODEL, AdvancedSearchData.class);
    }


    protected boolean isNullable(final DataAttribute attribute)
    {
        return !attribute.isMandatory();
    }


    protected void adjustEditor(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final ValueComparisonOperator valueComparisonOperator, final FieldType fileldType)
    {
        final Editor editorForField = getEditorForField(searchAttributeDescriptor);
        if(editorForField != null)
        {
            updateEditorStateAccordingToOperator(editorForField, valueComparisonOperator, fileldType);
        }
    }


    private boolean isFieldDisabled(final FieldType fieldType)
    {
        boolean disabledField = true;
        final AdvancedSearchData searchData = getSearchDataFromModel();
        if(!fieldType.isDisabled())
        {
            final List<SearchConditionData> conditions = searchData.getConditions(fieldType.getName());
            if(CollectionUtils.isNotEmpty(conditions))
            {
                for(final SearchConditionData conditionData : conditions)
                {
                    disabledField &= conditionData.getFieldType().isDisabled();
                }
            }
            else
            {
                disabledField = false;
            }
        }
        return disabledField;
    }


    protected void adjustSearchDataModel(final SearchAttributeDescriptor searchAttributeDescriptor,
                    final ValueComparisonOperator valueComparisonOperator)
    {
        final SearchConditionData searchConditionData = getSearchDataFromModel()
                        .getCondition(getActualIndexOfConditionInAdvancedSearchData(searchAttributeDescriptor.getAttributeNumber()));
        if((ValueComparisonOperator.IS_EMPTY.equals(valueComparisonOperator)
                        || ValueComparisonOperator.IS_NOT_EMPTY.equals(valueComparisonOperator)))
        {
            final DataType genericType = getDataType();
            final DataAttribute genericAttribute = genericType.getAttribute(searchAttributeDescriptor.getAttributeName());
            if(searchConditionData.getValue() == null && genericAttribute.isLocalized())
            {
                searchConditionData.updateLocalizedValue(getCockpitLocaleService().getCurrentLocale(), null);
            }
        }
    }


    protected void assignEditorToField(final SearchAttributeDescriptor searchAttributeDescriptor, final Editor editor)
    {
        fieldEditors.put(searchAttributeDescriptor, editor);
    }


    protected Editor getEditorForField(final SearchAttributeDescriptor searchAttributeDescriptor)
    {
        return fieldEditors.get(searchAttributeDescriptor);
    }


    private boolean isFieldComparatorDisabled(final AdvancedSearch configuration)
    {
        final boolean disabledByConfig = configuration.getFieldList().isDisableAttributesComparator();
        final boolean disabledBySetting = widgetInstanceManager.getWidgetSettings().getBoolean(DISABLE_ATTRIBUTES_COMPARATOR);
        return disabledByConfig || disabledBySetting;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void markEmptyMandatoryField(final String fieldName)
    {
        for(final Editor editor : getEditorsForField(fieldName))
        {
            markEmptyMandatoryField(editor);
        }
    }


    public void unmarkMandatoryField(final String fieldName)
    {
        for(final Editor editor : getEditorsForField(fieldName))
        {
            unmarkMandatoryField(editor);
        }
    }


    private List<Editor> getEditorsForField(final String field)
    {
        final List<Editor> editors = new ArrayList<>();
        for(final Entry<SearchAttributeDescriptor, Editor> entry : fieldEditors.entrySet())
        {
            if(entry.getKey().getAttributeName().equals(field))
            {
                editors.add(entry.getValue());
            }
        }
        return editors;
    }


    public void adjustSortOrderColumnVisibility(final Column sortOrder, final AdvancedSearchData advancedSearch)
    {
        final boolean visibility = isSortPanelEnabled() && atLeastOneFieldSortable(advancedSearch);
        sortOrder.setVisible(visibility);
    }


    private boolean atLeastOneFieldSortable(final AdvancedSearchData advancedSearch)
    {
        final List<SearchConditionData> conditions = getSearchConditionData(advancedSearch);
        for(final FieldType fieldType : getFieldTypes(conditions))
        {
            if(isSortable(fieldType) && !fieldType.isDisabled())
            {
                return true;
            }
        }
        return false;
    }


    private Collection<? extends FieldType> getFieldTypes(final List<SearchConditionData> conditions)
    {
        final List<FieldType> fields = conditions.stream().map(SearchConditionData::getFieldType).collect(Collectors.toList());
        return prepareAlphabeticallySortedAttributeList(fields);
    }


    public void setEditorsEventConsumer(final Consumer<Event> editorsEventConsumer)
    {
        this.editorsEventConsumer = editorsEventConsumer;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        if(this.widgetInstanceManager != null)
        {
            throw new IllegalStateException("Widget Instance Manager may be set only once!");
        }
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public Map<SearchAttributeDescriptor, Editor> getFieldEditors()
    {
        return fieldEditors;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    protected class SearchConditionRenderer implements RowRenderer<SearchConditionData>
    {
        private final Grid container;
        private final Radiogroup sortControlCnt;
        private final AdvancedSearch configuration;
        private final DataType dataType;
        private final Column sortOrderColumn;
        private final AdvancedSearchData advancedSearchData;


        public SearchConditionRenderer(final Grid container, final Radiogroup sortControlCnt, final AdvancedSearch configuration,
                        final DataType dataType, final Column sortOrderColumn, final AdvancedSearchData advancedSearchData)
        {
            this.container = container;
            this.sortControlCnt = sortControlCnt;
            this.configuration = configuration;
            this.dataType = dataType;
            this.sortOrderColumn = sortOrderColumn;
            this.advancedSearchData = advancedSearchData;
        }


        @Override
        public void render(final Row row, final SearchConditionData data, final int rowIndex)
        {
            if(Objects.equals(GRID_ADD_LINE_HOLDER, data))
            {
                renderAddAttributeRow(container, row, rowIndex, data, configuration, dataType);
            }
            else
            {
                addCellLabel(row, data);
                addCellOperator(row, data, rowIndex);
                addCellEditor(row, data, rowIndex, sortOrderColumn);
                addCellSort(row, data);
                addCellRemove(row, data, rowIndex);
                mergeCellsForLabelAndSortButtonsIfNecessary(data, rowIndex);
                applyRowClassIfItIsLastRowForGivenCondition(data, row);
            }
        }


        private void addCellLabel(final Row row, final SearchConditionData data)
        {
            final String qualifier = data.getFieldType().getName();
            final int conditionIndexForGivenQualifier = advancedSearchData.getConditions(qualifier).indexOf(data);
            if(conditionIndexForGivenQualifier == 0)
            {
                final Cell cellLabel = new Cell();
                final Label label = new Label(getAttributeLabel(qualifier));
                UITools.modifySClass(label, "yw-advanced-search-cnd-disabled", data.getFieldType().isDisabled());
                UITools.modifySClass(cellLabel, SCLASS_EDITOR_MANDATORY_STYLE, data.getFieldType().isMandatory());
                cellLabel.appendChild(label);
                row.appendChild(cellLabel);
            }
        }


        private void addCellOperator(final Row row, final SearchConditionData data, final int rowIndex)
        {
            final Component operatorComponent = createOperatorComponent(data.getFieldType(), data.getOperator(),
                            createSearchOperatorSelectionListener(data, rowIndex), rowIndex);
            final Cell cellOperator = new Cell();
            UITools.modifySClass(cellOperator, YW_ADVANCED_SEARCH_LINE, true);
            YTestTools.modifyYTestId(cellOperator, "operators-container-" + data.getFieldType().getName());
            cellOperator.appendChild(operatorComponent);
            row.appendChild(cellOperator);
        }


        private void addCellEditor(final Row row, final SearchConditionData data, final int rowIndex, final Column sortOrderColumn)
        {
            final Cell cellEditor = new Cell();
            final Editor editor = createEditor(data.getFieldType(), rowIndex, data);
            final SearchAttributeDescriptor searchAttributeDescriptor = new SearchAttributeDescriptor(data.getFieldType().getName(),
                            rowIndex);
            assignEditorToField(searchAttributeDescriptor, editor);
            adjustEditor(searchAttributeDescriptor, data.getOperator(), data.getFieldType());
            adjustSearchDataModel(searchAttributeDescriptor, data.getOperator());
            UITools.modifySClass(cellEditor, YW_ADVANCED_SEARCH_LINE, true);
            YTestTools.modifyYTestId(editor, "editor-" + data.getFieldType().getName() + "-" + rowIndex);
            YTestTools.modifyYTestId(cellEditor, "values-container-" + data.getFieldType().getName());
            cellEditor.appendChild(editor);
            row.appendChild(cellEditor);
            sortOrderColumn.setVisible(atLeastOneFieldSortable(advancedSearchData));
            markIfMandatoryAndEmpty(editor, data);
        }


        private void markIfMandatoryAndEmpty(final Editor editor, final SearchConditionData data)
        {
            final AdvancedSearch config = widgetInstanceManager.getModel()
                            .getValue(AdvancedSearchController.ADVANCED_SEARCH_CONFIGURATION, AdvancedSearch.class);
            final String fieldName = data.getFieldType().getName();
            if(AdvancedSearchDataConditionEvaluator.isMandatory(config, fieldName)
                            && !AdvancedSearchDataConditionEvaluator.atLeastOneConditionProvided(advancedSearchData, fieldName))
            {
                markEmptyMandatoryField(editor);
            }
        }


        private void addCellSort(final Row row, final SearchConditionData data)
        {
            final String qualifier = data.getFieldType().getName();
            final int conditionIndexForGivenQualifier = advancedSearchData.getConditions(qualifier).indexOf(data);
            if(conditionIndexForGivenQualifier == 0)
            {
                final Cell cellSort = new Cell();
                final SortData sortData = getSortData();
                final Div sortControls = createSortControls(sortControlCnt, data, sortData);
                cellSort.appendChild(sortControls);
                row.appendChild(cellSort);
            }
        }


        private SortData getSortData()
        {
            final SortData sortDataFromModel = advancedSearchData.getSortData();
            final SortData sortDataFromConfig = extractSortData(configuration);
            return sortDataFromModel != null ? sortDataFromModel : sortDataFromConfig;
        }


        private void addCellRemove(final Row row, final SearchConditionData data, final int rowIndex)
        {
            final Cell cellRemove = new Cell();
            UITools.modifySClass(cellRemove, YW_ADVANCED_SEARCH_LINE, true);
            YTestTools.modifyYTestId(cellRemove, "removes-container-" + data.getFieldType().getName());
            if(isAddAttributeRowVisible() && !data.getFieldType().isDisabled())
            {
                cellRemove.appendChild(createRemoveLine(rowIndex, container, advancedSearchData, data));
            }
            row.appendChild(cellRemove);
        }


        private void mergeCellsForLabelAndSortButtonsIfNecessary(final SearchConditionData data, final int rowIndex)
        {
            final String qualifier = data.getFieldType().getName();
            final int conditionIndexForGivenQualifier = advancedSearchData.getConditions(qualifier).indexOf(data);
            if(conditionIndexForGivenQualifier > 0)
            {
                final Cell firstCellLabelForGivenQualifier = (Cell)container.getCell(rowIndex - conditionIndexForGivenQualifier, 0);
                firstCellLabelForGivenQualifier.setRowspan(conditionIndexForGivenQualifier + 1);
                firstCellLabelForGivenQualifier.setSclass(YW_ADVANCED_SEARCH_ROWSPAN);
                final Cell firstCellWithSortButtonsForGivenQualifier = (Cell)container
                                .getCell(rowIndex - conditionIndexForGivenQualifier, 3);
                firstCellWithSortButtonsForGivenQualifier.setRowspan(conditionIndexForGivenQualifier + 1);
                firstCellWithSortButtonsForGivenQualifier.setSclass(YW_ADVANCED_SEARCH_ROWSPAN);
            }
        }


        private void applyRowClassIfItIsLastRowForGivenCondition(final SearchConditionData data, final Row row)
        {
            final String qualifier = data.getFieldType().getName();
            final int numberOfConditions = advancedSearchData.getConditions(qualifier).size();
            final int conditionIndexForGivenQualifier = advancedSearchData.getConditions(qualifier).indexOf(data);
            if(conditionIndexForGivenQualifier == numberOfConditions - 1)
            {
                UITools.modifySClass(row, YW_ADVANCED_SEARCH_LAST_ROW_FOR_CONDITION, true);
            }
        }


        private EventListener<SelectEvent> createSearchOperatorSelectionListener(final SearchConditionData data, final int tmpIndex)
        {
            return event -> {
                final Set selectedObjects = event.getSelectedObjects();
                if(selectedObjects.size() == 1)
                {
                    final ValueComparisonOperator valueComparisonOperator = (ValueComparisonOperator)selectedObjects.iterator()
                                    .next();
                    data.updateOperator(valueComparisonOperator);
                    final SearchAttributeDescriptor searchAttributeDescriptor = new SearchAttributeDescriptor(
                                    data.getFieldType().getName(), tmpIndex);
                    adjustEditor(searchAttributeDescriptor, valueComparisonOperator, data.getFieldType());
                    adjustSearchDataModel(searchAttributeDescriptor, valueComparisonOperator);
                }
            };
        }


        private Component createOperatorComponent(final FieldType field, final ValueComparisonOperator selectedOperator,
                        final EventListener<SelectEvent> selectionListener, final int index)
        {
            final DataAttribute attribute = getDataType().getAttribute(field.getName());
            final DataType valueType = attribute.getValueType();
            if(valueType == null)
            {
                return new Div();
            }
            else
            {
                final Div operatorContainer = new Div();
                final Combobox operator = createOperator(field.getName(), selectedOperator, selectionListener);
                YTestTools.modifyYTestId(operator, "operator-" + field.getName() + "-" + index);
                UITools.modifySClass(operator, YW_ADVANCED_SEARCH_OPERATOR, true);
                operatorContainer.appendChild(operator);
                operator.setDisabled(field.isDisabled() || isFieldComparatorDisabled(configuration));
                addEnterSupport(operator);
                return operatorContainer;
            }
        }
    }
}
