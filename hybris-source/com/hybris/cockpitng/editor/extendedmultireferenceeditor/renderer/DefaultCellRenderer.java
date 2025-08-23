/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.renderer;

import static com.hybris.cockpitng.editor.localized.LocalizedSimpleEditor.INLINE_PARAM;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.InlineEditorRowHandler;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.InlinePopup;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.EditorState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowState;
import com.hybris.cockpitng.editor.extendedmultireferenceeditor.state.RowStateUtil;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import java.util.Collections;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

/**
 * Renders an inline-editable cell of a row in
 * {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor}.
 */
public class DefaultCellRenderer<T> implements CellRenderer<T>
{
    protected static final String SCLASS_LIST_CELL = "yw-ref-list-cell";
    protected static final String EDITABLE_PROPERTY = "ye-editable-property";
    protected static final String INLINE_CELL_DURING_EDIT = "ye-current-edit-state-inline-cell";
    protected static final String ON_CELL_UPDATE_EVENT = "onCellUpdate";
    private TypeFacade typeFacade;
    private PermissionFacade permissionFacade;
    private ObjectValueService objectValueService;
    private ValidationRenderer validationRenderer;
    private LabelService labelService;
    private InlineEditorRowHandler handler;


    @Override
    public void render(final CellContext<T> cellContext)
    {
        final Listcell cell = new ValidatableListcell(getRowState(cellContext), cellContext.getCellProperty(),
                        getValidationRenderer());
        UITools.modifySClass(cell, SCLASS_LIST_CELL, true);
        cell.getAttributes().put("qualifier", cellContext.getColumnConfig().getQualifier());
        if(canReadCellProperty(cellContext))
        {
            renderAsText(cell, cellContext);
            if(isCellEditable(cellContext))
            {
                UITools.modifySClass(cell, EDITABLE_PROPERTY, true);
                cell.addEventListener(ON_CELL_UPDATE_EVENT, cellContext.getChangeListener());
            }
        }
        else
        {
            cell.setLabel(getLabelService().getAccessDeniedLabel(cellContext.getRowEntry()));
        }
        cell.setParent(cellContext.getRow());
    }


    private boolean canReadCellProperty(final CellContext<T> cellContext)
    {
        return permissionFacade.canReadInstanceProperty(cellContext.getRowEntry(), cellContext.getColumnConfig().getQualifier());
    }


    protected void renderAsText(final Listcell cell, final CellContext<T> cellContext)
    {
        // remove the editor if present
        cell.getChildren().clear();
        UITools.modifySClass(cell, INLINE_CELL_DURING_EDIT, false);
        final ListColumn columnConfig = cellContext.getColumnConfig();
        final T rowEntry = cellContext.getRowEntry();
        final DataType rowEntryDataType = cellContext.getRowEntryDataType();
        final WidgetInstanceManager wim = cellContext.getWidgetInstanceManager();
        final WidgetComponentRenderer renderer = getListCellRenderer(columnConfig);
        renderer.render(cell, columnConfig, rowEntry, rowEntryDataType, wim);
        if(isCellEditable(cellContext))
        {
            if(isAtomicAttribute(cellContext))
            {
                cell.addEventListener(Events.ON_CLICK, createEditorRenderingListener(cell, cellContext));
                markCellIfEdited(cell, cellContext);
            }
            else if(isCompletxAttribute(cellContext))
            {
                cell.addEventListener(Events.ON_CLICK, createPopupRenderingListener(cell, cellContext));
                markCellIfEdited(cell, cellContext);
            }
        }
    }


    protected void markCellIfEdited(final Listcell cell, final CellContext<T> cellContext)
    {
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final boolean propertyModified = getRowState(cellContext).isPropertyModified(qualifier);
        UITools.modifySClass(cell, RowStateUtil.MODIFIED_CELL_CLASS, propertyModified);
    }


    protected Object getPropertyInitialValue(final CellContext<T> cellContext)
    {
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final T rowEntry = cellContext.getRowEntry();
        return getObjectValueService().getValue(qualifier, rowEntry);
    }


    protected void renderAsPopup(final Listcell cell, final CellContext<T> cellContext)
    {
        final Editor editor = createEditorInstance(cellContext);
        editor.setInitialValue(getPropertyInitialValue(cellContext));
        final InlinePopup<T> popup = new InlinePopup<>();
        popup.createPopup(cell, cellContext, editor);
        editor.addEventListener(Editor.ON_EDITOR_EVENT, event -> {
            if(EditorListener.ESCAPE_PRESSED.equals(event.getData()))
            {
                popup.closePopup(cellContext, cell);
            }
            else if(EditorListener.ENTER_PRESSED.equals(event.getData()) || Events.ON_OK.equals(event.getData()))
            {
                popup.confirm(editor, cellContext, cell);
            }
        });
        popup.getPopup().addEventListener(Events.ON_CANCEL, event -> popup.closePopup(cellContext, cell));
        editor.initialize();
    }


    /**
     * @param genericAttribute
     *           an attribute for which the editor type must be determined
     * @return resolved editor type
     */
    protected String resolveEditorType(final DataAttribute genericAttribute)
    {
        return EditorUtils.getEditorType(genericAttribute, true, Collections.emptyMap());
    }


    protected Editor createEditorInstance(final CellContext<T> cellContext)
    {
        if(cellContext == null)
        {
            return null;
        }
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final T entryRow = cellContext.getRowEntry();
        final Editor editor = new Editor();
        editor.addParameter(INLINE_PARAM, Boolean.TRUE);
        editor.addParameter(AbstractReferenceEditor.PARAM_REFERENCE_ADVANCED_SEARCH_ENABLED, Boolean.FALSE);
        editor.setNestedObjectCreationDisabled(true);
        editor.setOptional(isCellOptional(cellContext));
        editor.setWidgetInstanceManager(cellContext.getWidgetInstanceManager());
        if(cellContext.getRowEntryDataType() != null)
        {
            final DataAttribute dataAttribute = cellContext.getRowEntryDataType().getAttribute(qualifier);
            editor.setType(resolveEditorType(dataAttribute));
            if(dataAttribute.isLocalized())
            {
                editor.setWritableLocales(permissionFacade.getWritableLocalesForInstance(entryRow));
                editor.setReadableLocales(permissionFacade.getReadableLocalesForInstance(entryRow));
            }
        }
        return editor;
    }


    protected void renderAsEditor(final Listcell cell, final CellContext<T> cellContext)
    {
        // clear the text if present
        cell.setLabel(null);
        // set a class for edit state
        UITools.modifySClass(cell, INLINE_CELL_DURING_EDIT, true);
        final Editor editor = createEditorInstance(cellContext);
        editor.setValue(getPropertyInitialValue(cellContext));
        editor.initialize();
        editor.setParent(cell);
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final T rowEntry = cellContext.getRowEntry();
        editor.addEventListener(Editor.ON_EDITOR_EVENT, event -> {
            if(isEditFinished(event))
            {
                final Object editorValue = editor.getValue();
                getEditorState(cellContext).addRowState(rowEntry);
                if(EditorListener.ENTER_PRESSED.equals(event.getData()))
                {
                    getObjectValueService().setValue(qualifier, rowEntry, editorValue);
                    getObjectValueService().setValue(qualifier, getRowState(cellContext).getRow(), editorValue);
                    getRowState(cellContext).setPropertyModified(qualifier);
                    Events.postEvent(ON_CELL_UPDATE_EVENT, cell, rowEntry);
                }
                else if(EditorListener.ESCAPE_PRESSED.equals(event.getData()))
                {
                    renderAsText(cell, cellContext);
                }
                else if(EditorListener.FOCUS_LOST.equals(event.getData()))
                {
                    final Object entryValue = getPropertyInitialValue(cellContext);
                    if(ObjectUtils.notEqual(editorValue, entryValue))
                    {
                        getRowState(cellContext).setPropertyModified(qualifier);
                        getObjectValueService().setValue(qualifier, rowEntry, editorValue);
                        getObjectValueService().setValue(qualifier, getRowState(cellContext).getRow(), editorValue);
                        Events.postEvent(ON_CELL_UPDATE_EVENT, cell, rowEntry);
                    }
                    else
                    {
                        renderAsText(cell, cellContext);
                    }
                }
            }
        });
        editor.focus();
        // redraw, to fit the editor
        final Listitem row = cellContext.getRow();
        row.invalidate();
    }


    protected boolean isCellOptional(final CellContext<T> cellContext)
    {
        final DataType rowEntryDataType = cellContext.getRowEntryDataType();
        final String columnQualifier = cellContext.getColumnConfig().getQualifier();
        return !rowEntryDataType.getAttribute(columnQualifier).isMandatory();
    }


    protected boolean isEditFinished(final Event event)
    {
        return EditorListener.ENTER_PRESSED.equals(event.getData()) || EditorListener.FOCUS_LOST.equals(event.getData())
                        || EditorListener.ESCAPE_PRESSED.equals(event.getData());
    }


    private EditorState getEditorState(final CellContext<T> cellContext)
    {
        final WidgetInstanceManager wim = cellContext.getWidgetInstanceManager();
        final String parentEditorProperty = cellContext.getParentEditorProperty();
        return RowStateUtil.getExtendedMultiReferenceEditorState(wim, parentEditorProperty);
    }


    private RowState getRowState(final CellContext<T> cellContext)
    {
        final T rowEntry = cellContext.getRowEntry();
        return getEditorState(cellContext).getRowState(rowEntry);
    }


    protected WidgetComponentRenderer getListCellRenderer(final ListColumn column)
    {
        if(StringUtils.isNotBlank(column.getClazz()))
        {
            return BackofficeSpringUtil.createClassInstance(column.getClazz(), WidgetComponentRenderer.class);
        }
        else if(StringUtils.isNotBlank(column.getSpringBean()))
        {
            return BackofficeSpringUtil.getBean(column.getSpringBean(), WidgetComponentRenderer.class);
        }
        else
        {
            return BackofficeSpringUtil.getBean("listCellRenderer", WidgetComponentRenderer.class);
        }
    }


    protected EventListener<Event> createEditorRenderingListener(final Listcell cell, final CellContext<T> cellContext)
    {
        return new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                cell.removeEventListener(Events.ON_CLICK, this);
                renderAsEditor(cell, cellContext);
            }
        };
    }


    protected EventListener<Event> createPopupRenderingListener(final Listcell cell, final CellContext<T> cellContext)
    {
        return new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                cell.removeEventListener(Events.ON_CLICK, this);
                renderAsPopup(cell, cellContext);
            }
        };
    }


    protected boolean isAtomicAttribute(final CellContext cellContext)
    {
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final DataAttribute attribute = cellContext.getRowEntryDataType().getAttribute(qualifier);
        return attribute != null && attribute.isWritable() && attribute.getDefinedType().isAtomic() && !attribute.isLocalized();
    }


    protected boolean isCompletxAttribute(final CellContext cellContext)
    {
        final String qualifier = cellContext.getColumnConfig().getQualifier();
        final DataAttribute attribute = cellContext.getRowEntryDataType().getAttribute(qualifier);
        return attribute != null && attribute.isWritable() && (!attribute.getDefinedType().isAtomic() || attribute.isLocalized());
    }


    protected boolean isCellEditable(final CellContext<T> cellContext)
    {
        return cellContext.isInlineEditingEnabled() && (isAtomicAttribute(cellContext) || isCompletxAttribute(cellContext))
                        && canChangeCellProperty(cellContext);
    }


    private boolean canChangeCellProperty(final CellContext<T> cellContext)
    {
        return permissionFacade.canChangeInstanceProperty(cellContext.getRowEntry(), cellContext.getColumnConfig().getQualifier());
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


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public ObjectValueService getObjectValueService()
    {
        return this.objectValueService;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected ValidationRenderer getValidationRenderer()
    {
        return validationRenderer;
    }


    @Required
    public void setValidationRenderer(final ValidationRenderer validationRenderer)
    {
        this.validationRenderer = validationRenderer;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public InlineEditorRowHandler getHandler()
    {
        if(handler == null)
        {
            handler = BackofficeSpringUtil.getBean("inlineEditorRowHandler");
        }
        return handler;
    }
}
