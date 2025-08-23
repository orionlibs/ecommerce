/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor;

import com.hybris.cockpitng.common.configuration.EditorConfigurationUtil;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLogic;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer.DefaultRowRenderer;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.ListheaderSortState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate.InlineEditorValidatableContainer;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.validate.RowInlineEditorValidatableContainer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.impl.DefaultValidationContext;
import com.hybris.cockpitng.validation.impl.ValidationInfoFactoryWithPrefix;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.util.UILabelUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

public class DefaultExtendedMultiReferenceEditor<T> extends DefaultMultiReferenceEditor<T>
{
    /**
     * Name of the parameter which stores index of the menu column in table.
     *
     * @see #PARAM_MENU_COLUMN_INDEX_FIRST_COLUMN
     * @see #PARAM_MENU_COLUMN_INDEX_LAST_COLUMN
     */
    public static final String PARAM_MENU_COLUMN_INDEX = "menuColumnIndex";
    /**
     * The value indicating the first column in the table.
     */
    public static final String PARAM_MENU_COLUMN_INDEX_FIRST_COLUMN = "first";
    /**
     * The value indicating the last column in the table.
     */
    public static final String PARAM_MENU_COLUMN_INDEX_LAST_COLUMN = "last";
    public static final String EDITOR_CTX_INLINE_PROPERTY = "inlineProperty";
    protected static final String ATTRIBUTE_DELIMITER = ".";
    protected static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");
    public static final String INLINE_PREFIX = "inline";
    public static final String GLOBAL_SAVE_STYLE = "yw-global-save";
    public static final String GLOBAL_SAVE_ACTIVE_STYLE = "yw-global-save-active";
    public static final String INLINE_ROW_INDICATOR_WIDTH = "28px";// could not set with css
    public static final String NOT_INLINE_ACTION_COLUMN_WIDTH = "16px";// could not set with css
    public static final String ACTION_COLUMN_WIDTH = "48px";// could not set with css
    public static final String YE_OPACITY = "ye-opacity";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMultiReferenceEditor.class);
    private static final Pattern REGEX_EDITOR_PATTERN = Pattern
                    .compile("^(Extended)?MultiReference-(COLLECTION|LIST|SET)\\((.*)\\)$");
    public static final String HFLEX_COLUMN_MIN_WIDTH = "100";
    public static final String HFLEX_EXPAND_COULMN = "1";
    public static final String HFLEX_COLUMN_MIN_WIDTH_ATTR = "flexMinWidth";
    private static final String YE_ROW_VALIDATION_RESULT_POPUP = "ye-row-validation-result-popup";
    private static final String YE_GLOBAL_VALIDATION_RESULT_POPUP = "ye-global-validation-result-popup";
    private static final String PLACEHOLDER_KEY_PARAM = "placeholderKey";
    private ObjectValueService objectValueService;
    private ValidationRenderer validationRenderer;
    @Resource
    private ValidationHandler validationHandler;
    @Resource
    private WidgetUtils widgetUtils;
    @Resource
    private InlineEditorRowHandler<T> inlineEditorRowHandler;


    @Override
    public Pattern getRegexEditorPattern()
    {
        return REGEX_EDITOR_PATTERN;
    }


    @Override
    public String readTypeCode(final String valueType)
    {
        Validate.notNull("Value type may not be null", valueType);
        final Pattern pattern = getRegexEditorPattern();
        if(pattern == null)
        {
            throw new IllegalStateException("Provided Pattern may not be null");
        }
        final Matcher matcher = pattern.matcher(valueType);
        if(matcher.matches())
        {
            final int groupCount = matcher.groupCount();
            if(groupCount < 3)
            {
                throw new IllegalStateException("Could not capture group representing type code. Group count: " + groupCount);
            }
            return matcher.group(3);
        }
        else
        {
            throw new IllegalArgumentException("Improper value type: " + valueType);
        }
    }


    @Override
    protected String getCollectionType(final String valueType)
    {
        final Pattern pattern = getRegexEditorPattern();
        if(pattern == null)
        {
            throw new IllegalStateException("Provided Pattern may not be null");
        }
        final Matcher matcher = pattern.matcher(valueType);
        if(matcher.matches())
        {
            final int groupCount = matcher.groupCount();
            if(groupCount < 3)
            {
                throw new IllegalStateException("Could not capture group representing collection. Group count: " + groupCount);
            }
            return matcher.group(2);
        }
        else
        {
            throw new IllegalArgumentException("Improper collection type: " + valueType);
        }
    }


    protected void prepareGlobalValidationContainer(final Listbox listbox, final InlineEditorHeader header)
    {
        final InlineEditorValidatableContainer globalValidationContainer = new InlineEditorValidatableContainer(listbox,
                        getEditorState(), getInlineProperty());
        final Window globalValidationPopup = getValidationRenderer().createValidationViolationsPopup(globalValidationContainer,
                        e -> {
                            final ValidationResult validationResult = getEditorState().collectValidationResult();
                            final ValidationSeverity validationSeverity = validationResult.getHighestNotConfirmedSeverity();
                            if(ValidationSeverity.WARN.equals(validationSeverity))
                            {
                                validationResult.getNotConfirmed(ValidationSeverity.WARN).collect().forEach(info -> info.setConfirmed(true));
                            }
                            if(validationSeverity.isLowerThan(ValidationSeverity.ERROR))
                            {
                                globalSave(listbox.getListhead());
                            }
                        });
        UITools.modifySClass(globalValidationPopup, YE_GLOBAL_VALIDATION_RESULT_POPUP, true);
        globalValidationPopup.addEventListener(Events.ON_CLICK, e -> e.stopPropagation());
        globalValidationPopup.setParent(header.getGlobalSaveButton());
        globalValidationContainer.setValidationResultPopup(globalValidationPopup);
        header.setGlobalValidatableContainer(globalValidationContainer);
    }


    @Override
    public ReferenceEditorLayout<T> createReferenceLayout(final EditorContext context)
    {
        final WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)context.getParameterAs("wim");
        final Base config = EditorConfigurationUtil.getBaseConfiguration(widgetInstanceManager, getTypeCode());
        final ReferenceEditorLayout<T> ret = new ExtendReferenceEditorLayout(this, config, context);
        ret.setPlaceholderKey(Objects.toString(context.getParameter(PLACEHOLDER_KEY_PARAM)));
        return ret;
    }


    class ExtendReferenceEditorLayout extends ReferenceEditorLayout<T>
    {
        private final EditorContext context;
        private final List<ListColumn> columns;
        private final WidgetInstanceManager widgetInstanceManager;


        public ExtendReferenceEditorLayout(final ReferenceEditorLogic<T> referenceEditorInterface, final Base configuration,
                        final EditorContext context)
        {
            super(referenceEditorInterface, configuration);
            this.context = context;
            widgetInstanceManager = (WidgetInstanceManager)context.getParameterAs("wim");
            columns = EditorConfigurationUtil.getColumns(context, widgetInstanceManager, getTypeCode());
        }


        @Override
        public void createLayout(final Component parent)
        {
            super.createLayout(parent);
            final InlineEditorHeader header = createSelectedListHeader(getCurrentlySelectedList(), columns);
            if(inlineEditingEnabled(context))
            {
                prepareGlobalValidationContainer(getCurrentlySelectedList(), header);
            }
        }


        @Override
        protected ListitemRenderer<T> createSelectedItemsListItemRenderer()
        {
            assignCurrentObjectToRootType(widgetInstanceManager);
            // we need to pass modified editor property because getParentEditor is protected so it is not available
            // inside of InlineRowRenderer
            return new DefaultRowRenderer<>(DefaultExtendedMultiReferenceEditor.this, getSelectedElementsListModel(),
                            getInlineProperty());
        }


        private void assignCurrentObjectToRootType(final WidgetInstanceManager widgetInstanceManager)
        {
            final int index = getInlineProperty().indexOf(ATTRIBUTE_DELIMITER);
            if(index > 0)
            {
                final String typeKey = getInlineProperty().substring(0, index);
                final Object currentObject = context.getParameter(PARENT_OBJECT);
                widgetInstanceManager.getModel().put(typeKey, currentObject);
            }
        }
    }


    @Override
    public void render(final Component parent, final EditorContext<Collection<T>> context,
                    final EditorListener<Collection<T>> listener)
    {
        super.render(parent, context, listener);
        if(inlineEditingEnabled(context))
        {
            context.setParameter(EDITOR_CTX_INLINE_PROPERTY, getInlineProperty());
            createInlineEditorRefreshObserver().startObservingModel();
            addOnDesktopChangeGlobalListener();
            final EventListener<SortEvent> sortListener = e -> {
                final Listheader listheader = (Listheader)e.getTarget();
                final Listhead listhead = listheader.getListbox().getListhead();
                final ListheaderSortState listheaderSortState = new ListheaderSortState(listheader.getLabel(), e.isAscending());
                getEditorState().setListheaderSortState(listheaderSortState);
                if(listhead instanceof InlineEditorHeader)
                {
                    ((InlineEditorHeader)listhead).getRowValidatableContainer().getValidationResultPopup().setVisible(false);
                }
            };
            getListheaders().forEach(column -> column.addEventListener(Events.ON_SORT, sortListener));
            sort();
        }
    }


    protected void sort()
    {
        final ListheaderSortState listheaderSortState = getEditorState().getListheaderSortState()
                        .orElse(new ListheaderSortState("", false));
        getListheaders().stream()
                        .filter(listheader -> StringUtils.equals(listheader.getLabel(), listheaderSortState.getLabel())
                                        && StringUtils.isNotEmpty(listheader.getLabel()))
                        .findAny().ifPresent(listheader -> listheader.sort(listheaderSortState.isAscending()));
    }


    protected List<Listheader> getListheaders()
    {
        return getEditorLayout().getCurrentlySelectedList().getListhead().getChildren().stream().map(e -> (Listheader)e)
                        .collect(Collectors.toList());
    }


    protected InlineEditorRefreshObserver createInlineEditorRefreshObserver()
    {
        final String property = getParentEditor().getProperty();
        final String parentObjectProperty = StringUtils.substringBeforeLast(property, ".");
        final InlineEditorRefreshObserver observer = new InlineEditorRefreshObserver(getWidgetInstanceManager(),
                        parentObjectProperty, getInlineProperty());
        observer.setValueObserver(this::onModelValueChanged);
        observer.setRefreshEventConsumer(this::onInlineRefreshEvent);
        return observer;
    }


    protected void onModelValueChanged()
    {
        final WidgetInstanceManager wim = getWidgetInstanceManager();
        final Object object = wim.getModel().getValue(getInlineProperty(), Object.class);
        if(object instanceof Collection)
        {
            final Collection<T> collection = (Collection)object;
            collection.forEach(rowEntry -> doValidate(rowEntry));
        }
        else if(object != null)
        {
            doValidate((T)object);
        }
    }


    protected void onInlineRefreshEvent(final InlineEditorRefreshEvent event)
    {
        if(event.isReload())
        {
            getParentEditor().reload();
        }
        else if(CollectionUtils.isNotEmpty(event.getItemsToRefresh()))
        {
            event.getItemsToRefresh().forEach(item -> {
                final ListModelList<T> selectedElementsListModel = getEditorLayout().getSelectedElementsListModel();
                final int index = selectedElementsListModel.indexOf(item);
                if(index >= 0)
                {
                    selectedElementsListModel.set(index, (T)item);
                }
            });
        }
    }


    private void addOnDesktopChangeGlobalListener()
    {
        final Widgetslot slot = getWidgetInstanceManager().getWidgetslot();
        widgetUtils.addGlobalEventListener("onDesktopChange", slot, event -> desktopChange(), CockpitEvent.DESKTOP);
    }


    protected void desktopChange()
    {
        getEditorState().afterDesktopChanged();
        getParentEditor().reload();
    }


    protected boolean inlineEditingEnabled(final EditorContext<Collection<T>> context)
    {
        return context != null && context.isEditable()
                        && Objects.equals(context.getParameter("inlineEditing"), Boolean.TRUE.toString());
    }


    private void addOnClientInfoChangeGlobalListener(final InlineEditorHeader header)
    {
        widgetUtils.addGlobalEventListener(Events.ON_CLIENT_INFO, getWidgetInstanceManager().getWidgetslot(),
                        event -> header.invalidate(), StringUtils.EMPTY);
    }


    protected void prepareRowValidationContainer(final Listbox listbox, final Div globalSaveButton,
                    final InlineEditorHeader header)
    {
        final EditorState<T> editorState = getEditorState();
        final RowInlineEditorValidatableContainer rowValidationContainer = new RowInlineEditorValidatableContainer(listbox,
                        editorState);
        final Window rowValidationPopup = getValidationRenderer().createValidationViolationsPopup(rowValidationContainer, event -> {
            final T rowObject = editorState.getRow(rowValidationContainer.getLastRow());
            final ValidationResult validationResult = editorState.getValidationResult(rowObject);
            final ValidationSeverity validationSeverity = validationResult.getHighestNotConfirmedSeverity();
            if(ValidationSeverity.WARN.equals(validationSeverity))
            {
                validationResult.getNotConfirmed(ValidationSeverity.WARN).collect().forEach(info -> info.setConfirmed(true));
            }
            if(validationSeverity.isLowerThan(ValidationSeverity.ERROR))
            {
                getInlineEditorRowHandler().saveRow(rowObject, editorState, getEditorContext());
            }
        });
        UITools.modifySClass(rowValidationPopup, YE_ROW_VALIDATION_RESULT_POPUP, true);
        rowValidationPopup.addEventListener(Events.ON_CLICK, e -> e.stopPropagation());
        rowValidationPopup.setParent(globalSaveButton);
        rowValidationPopup.setTopmost();
        rowValidationPopup.setStyle("display:none");
        rowValidationContainer.setValidationResultPopup(rowValidationPopup);
        addOnClientInfoChangeGlobalListener(header);
        header.setRowValidatableContainer(rowValidationContainer);
    }


    protected InlineEditorHeader createSelectedListHeader(final Listbox listBox, final List<ListColumn> columns)
    {
        final InlineEditorHeader header = new InlineEditorHeader();
        final Div globalSaveButton = new Div();
        YTestTools.modifyYTestId(globalSaveButton, "inlineEditorGlobalSave");
        header.setGlobalSaveButton(globalSaveButton);
        if(inlineEditingEnabled(getEditorContext()))
        {
            final Listheader rowStatusColumn = new Listheader();
            rowStatusColumn.setWidth(INLINE_ROW_INDICATOR_WIDTH);
            header.appendChild(rowStatusColumn);
            prepareRowValidationContainer(listBox, globalSaveButton, header);
        }
        final Iterator<ListColumn> columnsIterator = columns.iterator();
        final int menuColumnIndex = getMenuColumnIndex(columns.size());
        header.setMenuColumnIndex(menuColumnIndex);
        for(int i = 0; i < columns.size() + 1; ++i)
        {
            if(i == menuColumnIndex)
            {
                renderMenuColumnHeader(header, globalSaveButton);
            }
            else
            {
                header.appendChild(createColumnHeader(columnsIterator.next()));
            }
        }
        listBox.appendChild(header);
        return header;
    }


    protected Listheader createColumnHeader(final ListColumn column)
    {
        final Listheader header = new Listheader(getColumnHeaderLabel(column, getTypeCode()));
        if(column.isSortable() && StringUtils.isNotBlank(column.getQualifier()))
        {
            final Comparator<Object> sorter = (left, right) -> {
                try
                {
                    final Object value1 = getObjectValueService().getValue(column.getQualifier(), left);
                    final Object value2 = getObjectValueService().getValue(column.getQualifier(), right);
                    if(value1 instanceof Comparable && value2 instanceof Comparable)
                    {
                        return ObjectUtils.compare((Comparable)value1, (Comparable)value2);
                    }
                    else
                    {
                        return ObjectUtils.compare(getLabelService().getObjectLabel(value1), getLabelService().getObjectLabel(value2));
                    }
                }
                catch(final Exception t)
                {
                    LOG.warn("Could not compare values", t);
                    return 0;
                }
            };
            header.setSortAscending(sorter);
            header.setSortDescending(Collections.reverseOrder(sorter));
            applyColumnAttributes(column, header);
            setColumnMaxLength(header, column);
        }
        else
        {
            header.setSort("none");
        }
        return header;
    }


    private static void setColumnMaxLength(final Listheader columnHeader, final ListColumn listColumn)
    {
        final Integer maxChar = listColumn.getMaxChar();
        if(maxChar != null)
        {
            columnHeader.setMaxlength(maxChar.intValue());
        }
    }


    protected void applyColumnAttributes(final ListColumn column, final Listheader columnHeader)
    {
        if(StringUtils.isNotBlank(column.getHflex()))
        {
            columnHeader.setHflex(column.getHflex());
            columnHeader.setClientAttribute(HFLEX_COLUMN_MIN_WIDTH_ATTR, calculateMinWidth(column));
        }
        else if(StringUtils.isNotBlank(column.getWidth()))
        {
            columnHeader.setWidth(column.getWidth());
        }
        else
        {
            columnHeader.setHflex(HFLEX_EXPAND_COULMN);
            columnHeader.setClientAttribute(HFLEX_COLUMN_MIN_WIDTH_ATTR, calculateMinWidth(column));
        }
    }


    private static String calculateMinWidth(final ListColumn column)
    {
        if(StringUtils.isNotBlank(column.getWidth()))
        {
            final String width = column.getWidth();
            final Matcher matcher = DIGIT_PATTERN.matcher(width);
            if(matcher.find())
            {
                return matcher.group();
            }
        }
        return HFLEX_COLUMN_MIN_WIDTH;
    }


    protected int getMenuColumnIndex(final int columnsQuantity)
    {
        final String index = (String)getEditorContext().getParameter(PARAM_MENU_COLUMN_INDEX);
        return PARAM_MENU_COLUMN_INDEX_LAST_COLUMN.equalsIgnoreCase(index) ? columnsQuantity : 0;
    }


    protected void renderMenuColumnHeader(final InlineEditorHeader head, final Div globalSaveButton)
    {
        final Listheader menuColumnHeader = new Listheader();
        menuColumnHeader.setWidth(ACTION_COLUMN_WIDTH);
        head.appendChild(menuColumnHeader);
        menuColumnHeader.appendChild(globalSaveButton);
        if(inlineEditingEnabled(getEditorContext()))
        {
            UITools.modifySClass(globalSaveButton, GLOBAL_SAVE_STYLE, true);
            final boolean modelModified = RowStateUtil.isModelChanged(getWidgetInstanceManager(), getInlineProperty());
            UITools.modifySClass(globalSaveButton, GLOBAL_SAVE_ACTIVE_STYLE, modelModified);
            globalSaveButton.addEventListener(Events.ON_CLICK, event -> {
                if(!globalSave(head))
                {
                    head.getRowValidatableContainer().getValidationResultPopup().setVisible(false);
                }
            });
        }
        else
        {
            globalSaveButton.setWidth(NOT_INLINE_ACTION_COLUMN_WIDTH);
        }
    }


    protected boolean globalSave(final Listhead listhead)
    {
        InlineEditorHeader inlineEditorHeader = null;
        if(listhead instanceof InlineEditorHeader)
        {
            inlineEditorHeader = (InlineEditorHeader)listhead;
        }
        boolean succeed = true;
        final WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager();
        if(RowStateUtil.isModelChanged(widgetInstanceManager, getInlineProperty()) && inlineEditorHeader != null)
        {
            final ValidationResult validation = doValidate(inlineEditorHeader.getGlobalValidatableContainer(),
                            inlineEditorHeader.getRowValidatableContainer());
            succeed = ValidationSeverity.WARN.isHigherThan(validation.getHighestNotConfirmedSeverity());
            if(succeed)
            {
                final InlineEditorRowHandler inlineEditorRowService = getInlineEditorRowHandler();
                inlineEditorRowService.doGlobalSave(widgetInstanceManager, getInlineProperty(), getEditorContext());
                updateGlobalSaveButtonState(inlineEditorHeader);
            }
        }
        return succeed;
    }


    private void updateGlobalSaveButtonState(final InlineEditorHeader inlineEditorHeader)
    {
        final boolean modelModified = RowStateUtil.isModelChanged(getWidgetInstanceManager(), getInlineProperty());
        UITools.modifySClass(inlineEditorHeader.getGlobalSaveButton(), GLOBAL_SAVE_ACTIVE_STYLE, modelModified);
    }


    private WidgetInstanceManager getWidgetInstanceManager()
    {
        return getEditorContext().getParameterAs("wim");
    }


    protected void doValidate(final T rowObject)
    {
        final EditorState<T> editorState = getEditorState();
        final RowState<T> rowState = editorState.getRowState(rowObject);
        if(rowState != null)
        {
            final List<ValidationInfo> validation = getValidationHandler().validate(rowObject, createValidationContext(rowState));
            rowState.getValidationResult().setValidationInfo(
                            ValidationInfoFactoryWithPrefix.addPrefix(validation, editorState.getRowPath(rowState.getRowIndex())));
        }
    }


    protected ValidationResult doValidate(final ValidatableContainer globalValdatableCnt,
                    final ValidatableContainer rowValidatableCnt)
    {
        final EditorState<T> editorState = getEditorState();
        try
        {
            globalValdatableCnt.setPreventBroadcastValidationChange(true);
            rowValidatableCnt.setPreventBroadcastValidationChange(true);
            editorState.getEntries().stream().filter(entry -> entry.getValue().isRowModified())
                            .forEach(entry -> doValidate(entry.getKey()));
        }
        finally
        {
            globalValdatableCnt.setPreventBroadcastValidationChange(false);
            rowValidatableCnt.setPreventBroadcastValidationChange(false);
        }
        editorState.validationChanged(getInlineProperty());
        return editorState.collectValidationResult();
    }


    @Override
    public void removeSelectedObject(final T obj)
    {
        super.removeSelectedObject(obj);
        getEditorState().removeRowState(obj);
    }


    protected EditorState<T> getEditorState()
    {
        return RowStateUtil.getExtendedMultiReferenceEditorState(getWidgetInstanceManager(), getInlineProperty());
    }


    protected ValidationContext createValidationContext(final RowState rowState)
    {
        final DefaultValidationContext validationContext = new DefaultValidationContext();
        final ValidationResult validationResult = rowState.getValidationResult()
                        .get(getEditorState().getRowPath(rowState.getRowIndex())).wrap();
        if(validationResult != null)
        {
            validationContext.setConfirmed(validationResult.getConfirmed().wrap());
        }
        return validationContext;
    }


    protected String getInlineProperty()
    {
        return String.format("%s%s", INLINE_PREFIX, getParentEditor().getProperty());
    }


    protected String getColumnHeaderLabel(final ListColumn column, final String typeCode)
    {
        return UILabelUtil.getColumnHeaderLabel(column, typeCode, getLabelService());
    }


    @Override
    public void addSelectedObject(final T obj)
    {
        super.addSelectedObject(obj);
        scrollIntoLastAddedItem();
    }


    private void scrollIntoLastAddedItem()
    {
        final Listbox currentlySelectedList = getEditorLayout().getCurrentlySelectedList();
        final Listitem lastItem = currentlySelectedList.getItemAtIndex(currentlySelectedList.getItemCount() - 1);
        Clients.scrollIntoView(lastItem);
    }


    public ObjectValueService getObjectValueService()
    {
        if(objectValueService == null)
        {
            this.objectValueService = SpringUtil.getApplicationContext().getBean("objectValueService", ObjectValueService.class);
        }
        return objectValueService;
    }


    public ValidationRenderer getValidationRenderer()
    {
        if(validationRenderer == null)
        {
            validationRenderer = (ValidationRenderer)SpringUtil.getBean("validationRenderer");
        }
        return validationRenderer;
    }


    protected InlineEditorRowHandler getInlineEditorRowHandler()
    {
        if(inlineEditorRowHandler == null)
        {
            inlineEditorRowHandler = new InlineEditorRowHandler<T>();
        }
        return inlineEditorRowHandler;
    }


    public ValidationHandler getValidationHandler()
    {
        return validationHandler;
    }
}
