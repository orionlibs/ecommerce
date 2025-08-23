/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors.impl;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Objects;
import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Popup;

/**
 * Implements the common mechanisms for the pagination-enabled editors.
 */
public abstract class AbstractPaginableEditorRenderer<T> extends AbstractCockpitEditorRenderer<T>
{
    protected static final String LAST_EDITED_INDEX = "lastEditedIndex";
    protected static final String LAST_EDITED_VALUE = "lastEditedValue";
    protected static final String NULL = "null";
    protected static final String TRUE = "true";
    protected static final String CLEAR_EDITOR_IMG = "/icons/icon_func_inline_edit_cancel_default_16.png";
    protected static final String YE_LIST_LINE_EDITOR = "ye-list-line-editor";
    protected static final String YE_LIST_INLINE_SAVE = "ye-list-inline-save";
    protected static final String YE_LIST_INLINE_EDITOR = "ye-list-inline-editor";
    protected static final String YE_LIST_INLINE_CANCEL = "ye-list-inline-cancel";
    protected static final String YE_LIST_INLINE = "ye-list-inline";
    protected static final String YE_LIST_TOOLBAR_ADDPOPUP = "ye-list-toolbar-popup-add";
    protected static final String YE_LIST_TOOLBAR_ADDPOPUP_CONTAINER = "ye-list-toolbar-popup-add-container";
    protected static final String YE_LIST_TOOLBAR_ADDPOPUP_CHECKBOX = "ye-list-toolbar-popup-add-checkbox";
    protected static final String YE_LIST_TOOLBAR_BUTTON_EDIT = "ye-list-toolbar-button-edit";
    protected static final String YE_LIST_TOOLBAR = "ye-list-toolbar";
    protected static final String YE_LIST_TITLEBAR_LABEL = "ye-list-titlespan-label";
    protected static final String YE_LIST_TITLEBAR_VALUE = "ye-list-titlespan-value";
    protected static final String YE_LIST_CONTENT = "ye-list-content";
    protected static final String YE_LIST_LISTBOX = "ye-list-listbox";
    protected static final String YE_LIST_TITLEBAR_SPAN = "ye-list-titlespan";
    protected static final String YE_LIST_TITLEBAR = "ye-list-titlebar";
    protected static final String YE_LIST_CONTAINER = "ye-list";
    protected static final String CONFIRM_DELETE_PARAM = "confirmDelete";
    protected static final String ITEM_INDEX_TO_CUT = "cutMe";
    protected static final String ON_INIT_EVENT = "onInit";
    protected static final String ON_UPDATE_EVENT = "onUpdate";
    protected static final String ON_EDIT_EVENT = "onEdit";
    protected static final String YE_LIST_Y_BTB_PRIMARY = "y-btn-primary";
    private static final String YE_LIST_TOOLBAR_BUTTON_DELETE = "ye-list-toolbar-button-delete";
    private static final String YE_LIST_TOOLBAR_BUTTON_ADD = "ye-list-toolbar-button-add";
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final String LISTBOX_PAGE_SIZE = "pageSize";
    private static final String LISTBOX_NATIVE_PAGING_MOLD_OS = "os";
    private static final String LISTBOX_PAGING_MOLD = "paging";
    private static final String LISTBOX_NATIVE_PAGING_MOLD_DEFAULT = "default";
    private static final String ADVANCED_PAGING_MOLD = "advanced";
    private static final String LISTBOX_PAGING_MOLD_PARAM = "pagingMold";
    private static final String TOP = "top";


    /**
     * Creates single editor cell according to the <code>editorType</code>.
     *
     * @param editorType
     *           the type of the serializable Editor element
     * @return a serializable Editor to display
     */
    protected Editor createEditor(final EditorContext<T> context, final String editorType)
    {
        final Editor editorContainer = new Editor();
        editorContainer.setType(editorType);
        editorContainer.setReadOnly(!context.isEditable());
        editorContainer.setOptional(context.isOptional());
        editorContainer.setOrdered(context.isOrdered());
        editorContainer.addParameters(context.getParameters());
        editorContainer.setDefaultEditor(context.getParameterAs(Editor.VALUE_EDITOR));
        editorContainer.setWidgetInstanceManager((WidgetInstanceManager)context.getParameter("wim"));
        editorContainer.setReadableLocales(context.getReadableLocales());
        editorContainer.setWritableLocales(context.getWritableLocales());
        editorContainer.afterCompose();
        return editorContainer;
    }


    protected boolean selectionNotEmpty(final Listbox listbox)
    {
        return listbox.getSelectedCount() > 0;
    }


    /**
     * Creates button for removing selected entry.
     *
     * @param context
     *           to get some properties from
     */
    protected Button createDeleteButton(final Listbox listbox, final EditorContext<T> context)
    {
        final Button deleteButton = new Button();
        deleteButton.setSclass(YE_LIST_TOOLBAR_BUTTON_DELETE);
        deleteButton.setTooltiptext(getL10nDecorator(context, "buttonDeleteLabel", "button.label.delete"));
        deleteButton.setDisabled(true);
        deleteButton.addEventListener(Events.ON_CLICK, event -> Events.postEvent(ON_DELETE_EVENT, listbox, null));
        return deleteButton;
    }


    /**
     * Creates button for adding new entry.
     *
     * @param context
     *           to get some properties from
     */
    protected Button createAddButton(final Listbox listbox, final EditorContext<T> context)
    {
        final Button addButton = new Button();
        addButton.setSclass(YE_LIST_TOOLBAR_BUTTON_ADD);
        addButton.setDisabled(!context.isEditable());
        addButton.setTooltiptext(getL10nDecorator(context, "buttonAddLabel", "button.label.add"));
        addButton.addEventListener(Events.ON_CLICK, event -> Events.postEvent(ON_ADD_EVENT, listbox, null));
        return addButton;
    }


    protected abstract Popup createAddPopup(Listbox listbox, EditorListener<T> listener, EditorContext<T> context);


    protected int getPageSize(final EditorContext<T> context)
    {
        return NumberUtils.toInt(Objects.toString(context.getParameter(LISTBOX_PAGE_SIZE)), DEFAULT_PAGE_SIZE);
    }


    /**
     * Configure the inner {@link org.zkoss.zul.Paging} object of the list editor.
     *
     * @param editorView
     *           The editor to create paging for.
     * @param context
     *           defines editor's environment
     */
    protected void configurePaging(final Listbox editorView, final EditorContext<T> context)
    {
        final String pagingMold;
        if(ADVANCED_PAGING_MOLD.equals(context.getParameter(LISTBOX_PAGING_MOLD_PARAM)))
        {
            pagingMold = LISTBOX_NATIVE_PAGING_MOLD_DEFAULT;
        }
        else
        {
            pagingMold = LISTBOX_NATIVE_PAGING_MOLD_OS;
        }
        editorView.setMold(LISTBOX_PAGING_MOLD);
        editorView.getPagingChild().setMold(pagingMold);
        editorView.getPagingChild().setPageSize(getPageSize(context));
        editorView.setPagingPosition(TOP);
    }
}
