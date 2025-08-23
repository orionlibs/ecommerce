/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer;

import com.hybris.cockpitng.common.configuration.EditorConfigurationUtil;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorDndHandler;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.InlineEditorHeader;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.InlineEditorRowHandler;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.impl.DefaultValidationContext;
import com.hybris.cockpitng.validation.impl.ValidationInfoFactoryWithPrefix;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

/**
 * Renders a row in a
 * {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor}.
 */
public class DefaultRowRenderer<T> implements ListitemRenderer<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRowRenderer.class);
    protected static final String SCLASS_LIST_CELL_FILL = "yw-ref-list-cell-fill";
    protected static final String SCLASS_LIST_CELL = "yw-ref-list-cell";
    protected static final String SCLASS_READ_RESTRICTED = "yw-ref-list-cell-restricted";
    protected static final String ACTION_CELL = "ye-actiondots";
    protected static final String ROW_POPUP_STYLE = "ye-inline-editor-row-popup";
    protected static final String GLOBAL_SAVE_ACTIVE_STYLE = "yw-global-save-active";
    private final AbstractReferenceEditor parentEditor;
    private final ListModelList<T> selectedElementsListModel;
    private final String editorProperty;
    private InlineEditorRowHandler inlineEditorRowHandler;
    private ValidationRenderer validationRenderer;
    private ValidationHandler validationHandler;
    private ExpressionResolverFactory expressionResolverFactory;
    private ReferenceEditorDndHandler<T> referenceEditorDndHandler;


    public DefaultRowRenderer(final AbstractReferenceEditor parentEditor, final ListModelList<T> selectedElementsListModel,
                    final String editorProperty)
    {
        this.parentEditor = parentEditor;
        this.selectedElementsListModel = selectedElementsListModel;
        this.editorProperty = editorProperty;
    }


    protected void doValidate(final RowState rowState, final T entry, final String... qualifiers)
    {
        final List<ValidationInfo> validationResult = qualifiers.length > 0
                        ? getValidationHandler().validate(entry, Arrays.asList(qualifiers))
                        : getValidationHandler().validate(entry);
        final String prefix = getEditorState().getRowPath(rowState.getRowIndex());
        if(qualifiers.length > 0 && CollectionUtils.isEmpty(validationResult))
        {
            Arrays.asList(qualifiers).forEach(
                            qualifier -> rowState.getValidationResult().removeValidationInfo(ObjectValuePath.getPath(prefix, qualifier)));
        }
        else if(qualifiers.length > 0)
        {
            final ValidationResult vr = ValidationInfoFactoryWithPrefix.addPrefix(new ValidationResult(validationResult), prefix);
            Arrays.asList(qualifiers).forEach(qualifier -> {
                final String path = ObjectValuePath.getPath(prefix, qualifier);
                final List<ValidationInfo> infos = vr.find(path).collect();
                rowState.getValidationResult().updateValidationInfo(path, infos);
            });
        }
        else if(CollectionUtils.isEmpty(validationResult))
        {
            rowState.getValidationResult().clear();
        }
        else
        {
            final ValidationResult vr = ValidationInfoFactoryWithPrefix.addPrefix(new ValidationResult(validationResult), prefix);
            rowState.getValidationResult().setValidationInfo(vr.getAll());
        }
    }


    @Override
    public void render(final Listitem row, final T entry, final int rowIndex)
    {
        try
        {
            final boolean validate = getEditorState().getRowState(entry) == null;
            final RowState rowState = getEditorState().addRowState(entry);
            final InlineEditorHeader header = getInlineEditorHeader(row);
            if(row != null)
            {
                row.getChildren().clear();
                row.setValue(entry);
            }
            final DataType rowEntryDataType = parentEditor.getTypeFacade().load(parentEditor.getTypeCode());
            final boolean canReadInstance = parentEditor.getPermissionFacade().canReadInstance(entry);
            UITools.modifySClass(row, SCLASS_READ_RESTRICTED, !canReadInstance);
            final boolean inlineEditingEnabled = inlineEditingEnabled();
            if(inlineEditingEnabled)
            {
                renderRowStatusCell(row, rowState);
            }
            else
            {
                if(row != null && !parentEditor.isDisableDisplayingDetails())
                {
                    row.addEventListener(Events.ON_DOUBLE_CLICK, event -> openRowsInEditorArea(entry));
                }
                getReferenceEditorDndHandler().enableDragAndDrop(row, parentEditor.getEditorLayout());
            }
            final List<ListColumn> columns = getColumns();
            final Iterator<ListColumn> columnsIterator = columns.iterator();
            final int menuColumnIndex = header != null ? header.getMenuColumnIndex() : columns.size();
            for(int i = 0; !columns.isEmpty() && i < columns.size() + 1; ++i)
            {
                if(i == menuColumnIndex)
                {
                    renderMenuColumn(row, entry, canReadInstance);
                }
                else
                {
                    final ListColumn column = columnsIterator.next();
                    final CellContext<T> cellContext = new CellContext<>();
                    cellContext.setRow(row);
                    cellContext.setRowEntry(entry);
                    cellContext.setRowEntryDataType(rowEntryDataType);
                    cellContext.setCellProperty(getCellProperty(rowIndex, column));
                    cellContext.setParentEditorProperty(getEditorProperty());
                    cellContext.setWidgetInstanceManager(getWidgetInstanceManager());
                    cellContext.setInlineEditingEnabled(inlineEditingEnabled);
                    cellContext.setColumnConfig(column);
                    cellContext.setChangeListener(event -> {
                        final T updatedEntry = (T)event.getData();
                        updateListbox(row.getListbox(), updatedEntry);
                        if(inlineEditingEnabled)
                        {
                            doValidate(rowState, updatedEntry, column.getQualifier());
                            triggerRowPopupCordRequest(row, header);
                        }
                    });
                    final CellRenderer<T> cellRenderer = getCellRenderer();
                    cellRenderer.render(cellContext);
                }
            }
            if(inlineEditingEnabled)
            {
                if(validate && header != null)
                {
                    try
                    {
                        header.getRowValidatableContainer().setPreventBroadcastValidationChange(true);
                        doValidate(rowState, entry);
                    }
                    finally
                    {
                        header.getRowValidatableContainer().setPreventBroadcastValidationChange(false);
                    }
                }
                getValidationRenderer().cleanAllValidationCss(row);
                UITools.modifySClass(row,
                                getValidationRenderer().getSeverityStyleClass(rowState.getValidationResult().getHighestSeverity()), true);
            }
        }
        catch(final Exception e)
        {
            LOG.error("Could not render row.", e);
        }
    }


    protected List<ListColumn> getColumns()
    {
        return EditorConfigurationUtil.getColumns(getContext(), getWidgetInstanceManager(), parentEditor.getTypeCode());
    }


    protected boolean inlineEditingEnabled()
    {
        final EditorContext context = getContext();
        return context != null && context.isEditable()
                        && Objects.equals(context.getParameter("inlineEditing"), Boolean.TRUE.toString());
    }


    protected ValidationContext createValidationContext(final T entry)
    {
        final DefaultValidationContext validationContext = new DefaultValidationContext();
        final EditorState<T> editorState = getEditorState();
        final ValidationResult validationResult = editorState.getValidationResult(entry);
        if(validationResult != null)
        {
            validationContext.setConfirmed(validationResult.getConfirmed().wrap());
        }
        return validationContext;
    }


    protected void undoRow(final T entry, final Listbox listbox, final String rowProperty)
    {
        final EditorState<T> editorState = getEditorState();
        final RowState<T> rowState = editorState.getRowState(entry);
        final InlineEditorHeader header = getInlineEditorHeader(listbox);
        final T reloaded = (T)getInlineEditorRowHandler().undoRow(entry, getEditorState(), getWidgetInstanceManager(), rowProperty,
                        getContext());
        if(header != null)
        {
            header.getRowValidatableContainer().setPreventBroadcastValidationChange(true);
            doValidate(rowState, reloaded);
            header.getRowValidatableContainer().setPreventBroadcastValidationChange(false);
        }
        updateListbox(listbox, reloaded);
    }


    private void saveRow(final T entry, final Listitem row)
    {
        final EditorState<T> editorState = getEditorState();
        final RowState<T> rowState = editorState.getRowState(entry);
        if(rowState != null && rowState.isRowModified())
        {
            final InlineEditorHeader inlineEditorHeader = getInlineEditorHeader(row);
            final ValidationContext context = createValidationContext(entry);
            final List<ValidationInfo> infos = getValidationHandler().validate(entry, context);
            final ValidationResult validationResult = editorState.updateValidationResult(entry, infos);
            if(ValidationSeverity.WARN.isHigherThan(validationResult.getHighestNotConfirmedSeverity()))
            {
                getInlineEditorRowHandler().saveRow(entry, getEditorState(), getContext());
                refreshInlineEditorHeader(inlineEditorHeader);
            }
            else
            {
                if(inlineEditorHeader != null)
                {
                    inlineEditorHeader.getGlobalValidatableContainer().getValidationResultPopup().setVisible(false);
                }
                triggerRowPopupCordRequest(row, inlineEditorHeader);
            }
        }
    }


    public void triggerRowPopupCordRequest(final Listitem listitem, final InlineEditorHeader header)
    {
        if(header != null && header.getRowValidatableContainer().getLastRow() == getEditorState().getRowState(listitem.getValue())
                        .getRowIndex())
        {
            final Component rowMenuCell = getRowMenuCell(listitem, header);
            final Window validationResultPopup = header.getRowValidatableContainer().getValidationResultPopup();
            if(rowMenuCell != null && validationResultPopup != null)
            {
                validationResultPopup.setParent(rowMenuCell);
                validationResultPopup.setStyle("display:initial");
                validationResultPopup.invalidate();
            }
        }
    }


    private static Component getRowMenuCell(final Listitem listitem, final InlineEditorHeader header)
    {
        if(header != null)
        {
            final int menuColumnIndex = header.getMenuColumnIndex();
            if(menuColumnIndex >= 0 && listitem.getChildren().size() > menuColumnIndex)
            {
                return listitem.getChildren().get(menuColumnIndex);
            }
        }
        return null;
    }


    protected void renderMenuColumn(final Listitem row, final T entry, final boolean canReadInstance)
    {
        final boolean atLeastOneOptionEnabled = isModified(entry) || isEditDetailsEnabled(entry) || isRemoveEnabled(entry);
        final boolean menuEnabled = canReadInstance && atLeastOneOptionEnabled;
        final Listcell actionColumn = new Listcell();
        actionColumn.setSclass(menuEnabled ? ACTION_CELL : (ACTION_CELL + "-disabled"));
        actionColumn.setParent(row);
        if(menuEnabled)
        {
            final Menupopup menupopup = createMenuPopup(row, entry);
            actionColumn.appendChild(menupopup);
            actionColumn.addEventListener(Events.ON_CLICK, event -> onActionClick(actionColumn, menupopup));
            menupopup.addEventListener(Events.ON_OPEN,
                            (final OpenEvent event) -> UITools.modifySClass(actionColumn, ACTION_CELL + "-active", event.isOpen()));
        }
    }


    private static InlineEditorHeader getInlineEditorHeader(final Listbox listbox)
    {
        InlineEditorHeader ret = null;
        if(listbox != null && listbox.getListhead() instanceof InlineEditorHeader)
        {
            ret = (InlineEditorHeader)listbox.getListhead();
        }
        return ret;
    }


    private static InlineEditorHeader getInlineEditorHeader(final Listitem row)
    {
        InlineEditorHeader ret = null;
        if(row != null)
        {
            ret = getInlineEditorHeader(row.getListbox());
        }
        return ret;
    }


    private String getEditorProperty()
    {
        return editorProperty;
    }


    private String getRowProperty(final int rowIndex)
    {
        return String.format("%s[%d]", getEditorProperty(), Integer.valueOf(rowIndex));
    }


    private String getCellProperty(final int rowIndex, final ListColumn columnConfig)
    {
        return String.format("%s[%d].%s", getEditorProperty(), Integer.valueOf(rowIndex), columnConfig.getQualifier());
    }


    protected CellRenderer getCellRenderer()
    {
        return BackofficeSpringUtil.getBean("extendedMultiReferenceEditorCellRenderer", CellRenderer.class);
    }


    protected Menupopup createMenuPopup(final Listitem row, final T entry)
    {
        final String rowProperty = getRowProperty(row.getIndex());
        final Menupopup menupopup = new Menupopup();
        menupopup.setSclass(ROW_POPUP_STYLE);
        final Menuitem saveRow = new Menuitem();
        saveRow.setLabel(getContext().getLabel("saverow"));
        saveRow.addEventListener(Events.ON_CLICK, event -> saveRow(entry, row));
        final Menuitem undo = new Menuitem();
        undo.setLabel(getContext().getLabel("undochanges"));
        undo.addEventListener(Events.ON_CLICK, event -> undoRow(entry, row.getListbox(), rowProperty));
        final Menuitem remove = new Menuitem();
        remove.setLabel(getContext().getLabel("removereference"));
        remove.addEventListener(Events.ON_CLICK, event -> parentEditor.removeSelectedObject(entry));
        final Menuitem editDetails = new Menuitem();
        editDetails.setLabel(getContext().getLabel("editdetails"));
        editDetails.addEventListener(Events.ON_CLICK, event -> openRowsInEditorArea(entry));
        if(isModified(entry))
        {
            menupopup.appendChild(saveRow);
            menupopup.appendChild(undo);
        }
        if(isRemoveEnabled(entry))
        {
            menupopup.appendChild(remove);
        }
        if(isEditDetailsEnabled(entry))
        {
            menupopup.appendChild(editDetails);
        }
        return menupopup;
    }


    protected void openRowsInEditorArea(final T entry)
    {
        parentEditor.triggerReferenceSelected(new TypeAwareSelectionContext<>(entry, getSelectedElementsListModel()));
    }


    protected boolean isRemoveEnabled(final T entry)
    {
        final EditorContext context = getContext();
        if(context != null)
        {
            if(!context.isEditable())
            {
                return false;
            }
            Object paramValue = null;
            final String parameter = (String)context.getParameterAs(AbstractReferenceEditor.PARAM_DISABLE_REMOVE_REFERENCE);
            if(StringUtils.isNotBlank(parameter))
            {
                paramValue = getExpressionResolverFactory().createResolver().getValue(entry, parameter);
            }
            return paramValue == null || BooleanUtils.isNotTrue(Boolean.valueOf(paramValue.toString()));
        }
        return true;
    }


    protected boolean isEditDetailsEnabled(final T entry)
    {
        final EditorContext context = getContext();
        if(context != null)
        {
            Object paramValue = null;
            final String parameter = (String)context.getParameterAs(AbstractReferenceEditor.PARAM_DISABLE_DISPLAYING_DETAILS);
            if(StringUtils.isNotBlank(parameter))
            {
                paramValue = getExpressionResolverFactory().createResolver().getValue(entry, parameter);
            }
            return paramValue == null || BooleanUtils.isNotTrue(Boolean.valueOf(paramValue.toString()));
        }
        return true;
    }


    protected boolean isModified(final T entry)
    {
        return getEditorState().getRowState(entry).isRowModified();
    }


    protected void updateListbox(final Listbox listbox, final T entry)
    {
        refreshCorrespondingRows(listbox, entry);
        refreshInlineEditorHeader(listbox, getEditorProperty());
    }


    protected void refreshInlineEditorHeader(final Listbox listbox, final String editorProperty)
    {
        final InlineEditorHeader inlineEditorHeader = getInlineEditorHeader(listbox);
        if(inlineEditorHeader != null)
        {
            final boolean modelModified = RowStateUtil.isModelChanged(getWidgetInstanceManager(), editorProperty);
            UITools.modifySClass(inlineEditorHeader.getGlobalSaveButton(), GLOBAL_SAVE_ACTIVE_STYLE, modelModified);
        }
    }


    protected void refreshInlineEditorHeader(final InlineEditorHeader inlineEditorHeader)
    {
        if(inlineEditorHeader != null)
        {
            final boolean modelModified = RowStateUtil.isModelChanged(getWidgetInstanceManager());
            UITools.modifySClass(inlineEditorHeader.getGlobalSaveButton(), GLOBAL_SAVE_ACTIVE_STYLE, modelModified);
        }
    }


    protected void refreshCorrespondingRows(final Listbox listbox, final T entry)
    {
        int rowIndex = 0;
        for(final Listitem row : listbox.getItems())
        {
            if(Objects.equals(row.getValue(), entry))
            {
                try
                {
                    listbox.getItemRenderer().render(row, entry, rowIndex);
                }
                catch(final Exception e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("ExtendedMultiReferenceEditor - cannot render row", e);
                    }
                    else
                    {
                        LOG.warn("ExtendedMultiReferenceEditor - cannot render row");
                    }
                }
            }
            rowIndex++;
        }
    }


    protected void renderRowStatusCell(final Listitem row, final RowState rowState)
    {
        final Listcell rowStatusCell = new DefaultRowStateCell(rowState, getValidationRenderer());
        rowStatusCell.setParent(row);
    }


    protected EditorState<T> getEditorState()
    {
        return RowStateUtil.getExtendedMultiReferenceEditorState(getWidgetInstanceManager(), getEditorProperty());
    }


    private static void onActionClick(final Listcell actionColumn, final Menupopup menupopup)
    {
        menupopup.open(actionColumn, "after_end");
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return (WidgetInstanceManager)getContext().getParameterAs("wim");
    }


    public EditorContext getContext()
    {
        return parentEditor.getEditorContext();
    }


    public ListModelList<T> getSelectedElementsListModel()
    {
        return selectedElementsListModel;
    }


    protected InlineEditorRowHandler getInlineEditorRowHandler()
    {
        if(inlineEditorRowHandler == null)
        {
            inlineEditorRowHandler = BackofficeSpringUtil.getBean("inlineEditorRowHandler");
        }
        return inlineEditorRowHandler;
    }


    protected ValidationRenderer getValidationRenderer()
    {
        if(validationRenderer == null)
        {
            validationRenderer = BackofficeSpringUtil.getBean("validationRenderer", ValidationRenderer.class);
        }
        return validationRenderer;
    }


    public void setValidationRenderer(final ValidationRenderer validationRenderer)
    {
        this.validationRenderer = validationRenderer;
    }


    protected ValidationHandler getValidationHandler()
    {
        if(this.validationHandler == null)
        {
            this.validationHandler = (ValidationHandler)SpringUtil.getBean("validationHandler", ValidationHandler.class);
        }
        return this.validationHandler;
    }


    public void setValidationHandler(final ValidationHandler validationHandler)
    {
        this.validationHandler = validationHandler;
    }


    protected ExpressionResolverFactory getExpressionResolverFactory()
    {
        if(this.expressionResolverFactory == null)
        {
            this.expressionResolverFactory = (ExpressionResolverFactory)SpringUtil.getBean("expressionResolverFactory",
                            ExpressionResolverFactory.class);
        }
        return this.expressionResolverFactory;
    }


    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }


    public ReferenceEditorDndHandler getReferenceEditorDndHandler()
    {
        if(referenceEditorDndHandler == null)
        {
            referenceEditorDndHandler = (ReferenceEditorDndHandler<T>)SpringUtil.getBean("referenceEditorDndHandler");
        }
        return referenceEditorDndHandler;
    }
}
