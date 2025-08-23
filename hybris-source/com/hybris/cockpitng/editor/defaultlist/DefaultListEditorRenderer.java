/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultlist;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractPaginableEditorRenderer;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.Range;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Span;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Vlayout;

/**
 * Default renderer for list editors.
 */
public class DefaultListEditorRenderer<E> extends AbstractPaginableEditorRenderer<List<E>>
{
    private static final String ON_ADD_AFTER_EVENT = "onAddAfter";
    private static final String SAVE_ON_BLUR_PARAM = "saveOnBlur";
    private static final String WHITELISTED_VALUES_CONTEXT_PARAMETER = "whitelistedValues";
    private static final String NOTIFICATION_SOURCE = "defaultListEditorRenderer";
    private static final String ADD_POPUP_BUTTON_LABEL = "addPopupButtonLabel";
    private static final String KEY_ADD_POPUP_BUTTON_LABEL = "add.popup.button.label";
    private static final String PRIMARY_BUTTON_SCLASS = "y-btn-primary";
    private static final String CANCEL_POPUP_BUTTON_LABEL = "cancelPopupButtonLabel";
    private static final String KEY_CANCEL_POPUP_BUTTON_LABEL = "cancel.popup.button.label";
    private static final String CANCEL_BUTTON_SCLASS = "ye-btn-cancel";
    private static final String BUTTON_CONTAINER_SCLASS = "ye-btn-container";
    private static final String LIST_EMPTY_MESSAGE_LABEL = "listEmptyMessageLabel";
    private static final String KEY_LIST_EMPTY_MESSAGE_LABEL = "list.empty.message.label";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListEditorRenderer.class);
    protected Listbox editorView;
    @Resource
    private LabelService labelService;
    @Resource
    private NotificationService notificationService;


    private static EventListener<Event> createButtonActivatingEventListener(final Button button)
    {
        return event -> {
            final Object data = event.getData();
            if(data instanceof Range)
            {
                final boolean startIsNull = ((Range)data).getStart() == null;
                final boolean endIsNull = ((Range)data).getEnd() == null;
                button.setDisabled(startIsNull || endIsNull);
            }
            else
            {
                button.setDisabled(StringUtils.isEmpty(Objects.toString(data, "")));
            }
        };
    }


    @Override
    public void render(final Component parent, final EditorContext<List<E>> context, final EditorListener<List<E>> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Div container = new Div();
        container.setSclass(YE_LIST_CONTAINER);
        final Div titleBar = new Div();
        container.getChildren().add(titleBar);
        final Span titleBarSpan = new Span();
        titleBarSpan.setSclass(YE_LIST_TITLEBAR_SPAN);
        titleBar.getChildren().add(titleBarSpan);
        titleBar.setSclass(YE_LIST_TITLEBAR);
        final Toolbar toolbar = new Toolbar();
        container.getChildren().add(toolbar);
        toolbar.setSclass(YE_LIST_TOOLBAR);
        final Div content = new Div();
        content.setSclass(YE_LIST_CONTENT);
        container.appendChild(content);
        editorView = new Listbox();
        editorView.setSclass(YE_LIST_LISTBOX);
        configurePaging(editorView, context);
        editorView.setItemRenderer(createItemRenderer(context, listener));
        content.getChildren().add(editorView);
        final Div addItemContainer = createAddInlineEditor(context, listener, editorView, container, content);
        final Popup addAfterPopup = createSimpleAddPopup(editorView, listener, context);
        toolbar.getChildren().add(addAfterPopup);
        final Popup addPopup = createAddPopup(editorView, listener, context);
        toolbar.getChildren().add(addPopup);
        final Button buttonAdd = createAddButton(editorView, context);
        toolbar.getChildren().add(buttonAdd);
        final Button buttonEdit = createEditButton(editorView, context);
        toolbar.getChildren().add(buttonEdit);
        final Button buttonDelete = createDeleteButton(editorView, context);
        toolbar.getChildren().add(buttonDelete);
        container.setParent(parent);
        editorView.addEventListener(ON_ADD_EVENT, event -> {
            if(editorView.getModel() instanceof Collection && CollectionUtils.isEmpty((Collection)editorView.getModel()))
            {
                content.getChildren().remove(editorView);
            }
            container.appendChild(addItemContainer);
            Events.postEvent(ON_INIT_EVENT, addItemContainer, null);
        });
        editorView.addEventListener(ON_ADD_AFTER_EVENT, event -> {
            addAfterPopup.open(editorView.getSelectedItem(), "after_start");
            Events.postEvent(ON_INIT_EVENT, addAfterPopup, null);
        });
        editorView.addEventListener(Events.ON_SELECT, (final SelectEvent<Listitem, E> event) -> {
            final boolean somethingIsSelected = selectionNotEmpty(editorView);
            final boolean buttonDisabled = (!somethingIsSelected) || (!context.isEditable());
            buttonEdit.setDisabled(buttonDisabled);
            buttonDelete.setDisabled(buttonDisabled);
            final Listbox listbox = event.getReference().getListbox();
            final int lastEditedIndex = NumberUtils.toInt(Objects.toString(listbox.getAttribute(LAST_EDITED_INDEX), ""), -1);
            final Object lastEditedValue = listbox.getAttribute(LAST_EDITED_VALUE);
            if(lastEditedValue != null && lastEditedIndex != -1)
            {
                boolean saveOnBlur = true;
                final Object parameter = context.getParameter(SAVE_ON_BLUR_PARAM);
                if(parameter != null)
                {
                    saveOnBlur = BooleanUtils.toBoolean(Objects.toString(parameter, ""));
                }
                if(saveOnBlur)
                {
                    editValue(lastEditedIndex, (E)lastEditedValue, listbox, listener);
                }
                else
                {
                    revertValue(listbox, listener);
                }
            }
        });
        editorView.addEventListener(ON_DELETE_EVENT, event -> {
            if(selectionNotEmpty(editorView))
            {
                final boolean confirmDelete = BooleanUtils.toBoolean((String)context.getParameter(CONFIRM_DELETE_PARAM));
                if(confirmDelete)
                {
                    Messagebox.show(getL10nDecorator(context, "deletePopupMessage", "delete.popup.message"),
                                    getL10nDecorator(context, "deletePopupTitle", "delete.popup.title"),
                                    new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.CANCEL}, Messagebox.QUESTION,
                                    messageBoxEvent -> {
                                        final org.zkoss.zul.Messagebox.Button selection = messageBoxEvent.getButton();
                                        if(org.zkoss.zul.Messagebox.Button.YES.equals(selection))
                                        {
                                            deleteValueAtIndex(editorView.getSelectedIndex(), editorView, listener);
                                        }
                                    });
                }
                else
                {
                    deleteValueAtIndex(editorView.getSelectedIndex(), editorView, listener);
                }
            }
        });
        editorView.addEventListener(ON_UPDATE_EVENT, event -> {
            final boolean selectionNotEmpty = selectionNotEmpty(editorView);
            buttonEdit.setDisabled(!selectionNotEmpty);
            buttonDelete.setDisabled(!selectionNotEmpty);
            context.removeParameter(ITEM_INDEX_TO_CUT);
            editorView.removeAttribute(LAST_EDITED_INDEX);
            editorView.removeAttribute(LAST_EDITED_VALUE);
        });
        final List<E> initList = new ArrayList<>();
        if(context.getInitialValue() != null)
        {
            final List<E> initListValues = new ArrayList<>();
            if(isWhitelistedValuesParameterSet(context))
            {
                initListValues.addAll(removeNotWhitelistedValuesFromInitList(context));
            }
            else
            {
                initListValues.addAll(context.getInitialValue());
            }
            initList.addAll(initListValues);
        }
        final ListModelList<E> listModel = new ListModelList<>();
        listModel.addAll(initList);
        editorView.setModel(listModel);
        editorView.setEmptyMessage(getL10nDecorator(context, LIST_EMPTY_MESSAGE_LABEL, KEY_LIST_EMPTY_MESSAGE_LABEL));
    }


    /**
     * Defines {@link ListitemRenderer} for the items in the list.
     *
     * @param context
     * @param listener
     * @return {@link ListitemRenderer}
     */
    protected ListitemRenderer<E> createItemRenderer(final EditorContext<List<E>> context, final EditorListener<List<E>> listener)
    {
        return new ListitemRenderer<E>()
        {
            @Override
            public void render(final Listitem listItem, final E data, final int index)
            {
                listItem.setValue(data);
                listItem.setLabel(renderItem(data));
                listItem.setDraggable(editorView.getUuid());
                listItem.setDroppable(editorView.getUuid());
                listItem.addEventListener(Events.ON_DROP, (final DropEvent event) -> {
                    final Component dragged = event.getDragged();
                    if(dragged instanceof Listitem)
                    {
                        final Listitem draggedListItem = (Listitem)dragged;
                        final int cutMe = draggedListItem.getIndex();
                        final int pasteAfterMe = listItem.getIndex();
                        moveValueAtIndex(cutMe, pasteAfterMe, listItem.getListbox(), listener);
                    }
                });
                listItem.addEventListener(ON_EDIT_EVENT, event -> showInlineEditor(listItem, data));
                listItem.addEventListener(Events.ON_OK, event -> showInlineEditor(listItem, data));
            }


            protected String renderItem(final E item)
            {
                if(item instanceof String)
                {
                    return (String)item;
                }
                return item == null ? NULL : labelService.getObjectLabel(item);
            }


            private void showInlineEditor(final Listitem listItem, final E data)
            {
                listItem.setLabel(null);
                listItem.getChildren().clear();
                final Listcell inlineEditorCell = createInlineEditor(context, listener, listItem, data);
                listItem.appendChild(inlineEditorCell);
                listItem.setContext((Popup)null);
                Events.postEvent(ON_INIT_EVENT, inlineEditorCell, null);
            }
        };
    }


    private EventListener<Event> createSaveInlineValueListener(final Editor inlineEditor, final Listitem listItem,
                    final EditorListener<List<E>> listener, final EditorContext context)
    {
        return event -> {
            final String data = Objects.toString(event.getData(), "");
            if(StringUtils.isEmpty(data) || EditorListener.ENTER_PRESSED.equals(data))
            {
                final E value = (E)inlineEditor.getValue();
                if(value != null)
                {
                    if(isWhitelistedValuesParameterSet(context) && isValueNotWhitelisted(context, value))
                    {
                        showNotWhitelistedEditNotificationWarning(value);
                        return;
                    }
                    editValue(listItem.getIndex(), value, listItem.getListbox(), listener);
                }
            }
            if(EditorListener.ESCAPE_PRESSED.equals(data))
            {
                revertValue(listItem.getListbox(), listener);
            }
        };
    }


    /**
     * Creates inline editor for list item.
     *
     * @param context
     *           - editor context
     * @param listener
     *           - editor listener
     * @param listItem
     *           - list item where the editor should be rendered
     * @param data
     *           - initial data for the editor
     * @return {@link Listcell} with the inline editor
     */
    protected Listcell createInlineEditor(final EditorContext<List<E>> context, final EditorListener<List<E>> listener,
                    final Listitem listItem, final E data)
    {
        final Div inlineEditorContainer = new Div();
        inlineEditorContainer.setSclass(YE_LIST_INLINE);
        final String listType = extractEmbeddedType(context);
        final Editor inlineEditor = createEditorWithNestedContext(context, listType);
        inlineEditor.setValue(data);
        inlineEditor.setSclass(YE_LIST_INLINE_EDITOR);
        final Button saveButton = new Button(getL10nDecorator(context, "saveButtonLabel", "save.button.label"));
        UITools.modifySClass(saveButton, YE_LIST_INLINE_SAVE, true);
        UITools.modifySClass(saveButton, YE_LIST_Y_BTB_PRIMARY, true);
        saveButton.addEventListener(Events.ON_CLICK, createSaveInlineValueListener(inlineEditor, listItem, listener, context));
        inlineEditor.addEventListener(Editor.ON_EDITOR_EVENT,
                        createSaveInlineValueListener(inlineEditor, listItem, listener, context));
        inlineEditor.addEventListener(Editor.ON_EDITOR_EVENT, event -> {
            if(event.getData().equals(EditorListener.FOCUS_LOST))
            {
                listItem.getListbox().setAttribute(LAST_EDITED_VALUE, inlineEditor.getValue());
                listItem.getListbox().setAttribute(LAST_EDITED_INDEX, listItem.getIndex());
            }
        });
        inlineEditor.addEventListener(Editor.ON_VALUE_CHANGED, createButtonActivatingEventListener(saveButton));
        final Button cancelButton = new Button(getL10nDecorator(context, "cancelButtonLabel", "cancel.button.label"));
        cancelButton.addEventListener(Events.ON_CLICK, event -> revertValue(listItem.getListbox(), listener));
        inlineEditorContainer.appendChild(inlineEditor);
        final Listcell listCell = new Listcell();
        listCell.setSclass(YE_LIST_LINE_EDITOR);
        listCell.appendChild(inlineEditorContainer);
        listCell.appendChild(saveButton);
        listCell.appendChild(cancelButton);
        listCell.addEventListener(ON_INIT_EVENT, event -> inlineEditor.focus());
        return listCell;
    }


    protected Div createAddInlineEditor(final EditorContext<List<E>> context, final EditorListener<List<E>> listener,
                    final Listbox listbox, final Div container, final Div content)
    {
        final Div addInlineEditorContainer = new Div();
        addInlineEditorContainer.setClass("ye-list-toolbar-inline-editor-add");
        final Vlayout layout = new Vlayout();
        addInlineEditorContainer.appendChild(layout);
        final String listType = extractEmbeddedType(context);
        final Editor inlineEditor = createEditorWithNestedContext(context, listType);
        applyRequiredSClasses(listType, inlineEditor);
        final Button addButton = new Button();
        addButton.setLabel(getL10nDecorator(context, ADD_POPUP_BUTTON_LABEL, KEY_ADD_POPUP_BUTTON_LABEL));
        addButton.setDisabled(true);
        addButton.setSclass(PRIMARY_BUTTON_SCLASS);
        inlineEditor.addEventListener(Editor.ON_VALUE_CHANGED, createButtonActivatingEventListener(addButton));
        final Button cancel = new Button();
        cancel.setLabel(getL10nDecorator(context, CANCEL_POPUP_BUTTON_LABEL, KEY_CANCEL_POPUP_BUTTON_LABEL));
        cancel.setSclass(CANCEL_BUTTON_SCLASS);
        cancel.addEventListener(Events.ON_CLICK, event -> {
            container.removeChild(addInlineEditorContainer);
            if(editorView.getModel() instanceof Collection
                            && CollectionUtils.isEmpty((Collection)editorView.getModel()))
            {
                content.getChildren().add(editorView);
            }
        });
        final Div btnContainer = new Div();
        btnContainer.setSclass(BUTTON_CONTAINER_SCLASS);
        btnContainer.appendChild(addButton);
        btnContainer.appendChild(cancel);
        layout.appendChild(inlineEditor);
        layout.appendChild(btnContainer);
        final Checkbox addToTopCheckbox = new Checkbox();
        addToTopCheckbox.setClass(YE_LIST_TOOLBAR_ADDPOPUP_CHECKBOX);
        addToTopCheckbox.setLabel(getL10nDecorator(context, "addPopupAddtotopLabel", "add.popup.addtotop.label"));
        layout.appendChild(addToTopCheckbox);
        final DefaultListEditorComponents defaultListEditorComponents = new DefaultListEditorComponents(inlineEditor, listbox,
                        addInlineEditorContainer, addToTopCheckbox, container, content);
        addButton.addEventListener(Events.ON_CLICK, createInlineSaveEventListener(defaultListEditorComponents, listener, context));
        inlineEditor.addEventListener(Editor.ON_EDITOR_EVENT,
                        createInlineSaveEventListener(defaultListEditorComponents, listener, context));
        addInlineEditorContainer.addEventListener(ON_INIT_EVENT, event -> {
            inlineEditor.setValue(null);
            inlineEditor.focus();
            addButton.setDisabled(true);
        });
        return addInlineEditorContainer;
    }


    /**
     * Creates editor for nested typewith nested e
     *
     * @param context
     * @param listType
     * @return
     */
    protected Editor createEditorWithNestedContext(final EditorContext<List<E>> context, final String listType)
    {
        return createEditor(createNestedContext(context), listType);
    }


    /**
     * Create popup for adding new list item.
     *
     * @param listbox
     * @param listener
     * @param context
     * @return {@link Popup}
     */
    protected Popup createSimpleAddPopup(final Listbox listbox, final EditorListener<List<E>> listener,
                    final EditorContext<List<E>> context)
    {
        final Popup popup = new Popup();
        popup.setSclass(YE_LIST_TOOLBAR_ADDPOPUP);
        final Vlayout layout = new Vlayout();
        layout.setSclass(YE_LIST_TOOLBAR_ADDPOPUP_CONTAINER);
        layout.setSpacing("auto");
        popup.getChildren().add(layout);
        final String listType = extractEmbeddedType(context);
        final Editor addPopupEditor = createEditorWithNestedContext(context, listType);
        applyRequiredSClasses(listType, addPopupEditor);
        final Button addButton = new Button();
        addButton.setLabel(getL10nDecorator(context, ADD_POPUP_BUTTON_LABEL, KEY_ADD_POPUP_BUTTON_LABEL));
        addButton.setDisabled(true);
        addButton.setSclass(PRIMARY_BUTTON_SCLASS);
        addPopupEditor.addEventListener(Editor.ON_VALUE_CHANGED, createButtonActivatingEventListener(addButton));
        layout.appendChild(addPopupEditor);
        final Button cancel = new Button();
        cancel.setLabel(getL10nDecorator(context, CANCEL_POPUP_BUTTON_LABEL, KEY_CANCEL_POPUP_BUTTON_LABEL));
        cancel.setSclass(CANCEL_BUTTON_SCLASS);
        cancel.addEventListener(Events.ON_CLICK, event -> {
            popup.close();
            addPopupEditor.reload();
        });
        final Div btnContainer = new Div();
        btnContainer.setSclass(BUTTON_CONTAINER_SCLASS);
        btnContainer.appendChild(addButton);
        btnContainer.setSclass(BUTTON_CONTAINER_SCLASS);
        btnContainer.appendChild(cancel);
        btnContainer.appendChild(addButton);
        layout.appendChild(btnContainer);
        addButton.addEventListener(Events.ON_CLICK, createSimplePopupSaveEventListener(addPopupEditor, listbox, listener, popup));
        addPopupEditor.addEventListener(Editor.ON_EDITOR_EVENT,
                        createSimplePopupSaveEventListener(addPopupEditor, listbox, listener, popup));
        popup.addEventListener(ON_INIT_EVENT, event -> {
            addPopupEditor.setValue(null);
            addPopupEditor.focus();
            addButton.setDisabled(true);
        });
        return popup;
    }


    protected <T> EditorContext<T> createNestedContext(final EditorContext<T> context)
    {
        final EditorContext clone = EditorContext.clone(context, context.getInitialValue());
        clone.setParameter(Editor.VALUE_EDITOR, extractEmbeddedEditor(context.getParameterAs(Editor.VALUE_EDITOR)));
        return clone;
    }


    private EventListener<Event> createSimplePopupSaveEventListener(final Editor addPopupEditor, final Listbox listbox,
                    final EditorListener<List<E>> listener, final Popup popup)
    {
        return event -> {
            final String data = Objects.toString(event.getData(), "");
            if(StringUtils.isEmpty(data) || EditorListener.ENTER_PRESSED.equals(data))
            {
                final E value = (E)addPopupEditor.getValue();
                if(value != null)
                {
                    addNewValue(listbox.getSelectedIndex() + 1, value, listbox, listener);
                    popup.close();
                }
            }
        };
    }


    /**
     * Create popup for adding new list item after selected item.
     *
     * @param listbox
     * @param listener
     * @param context
     * @return {@link Popup}
     * @deprecated since 2105, use the {@link #createAddInlineEditor(EditorContext, EditorListener, Listbox, Div, Div)}
     *             instead.
     */
    @Override
    @Deprecated(since = "2105", forRemoval = true)
    protected Popup createAddPopup(final Listbox listbox, final EditorListener<List<E>> listener,
                    final EditorContext<List<E>> context)
    {
        final Popup popup = new Popup();
        popup.setSclass(YE_LIST_TOOLBAR_ADDPOPUP);
        final Vlayout layout = new Vlayout();
        layout.setSclass(YE_LIST_TOOLBAR_ADDPOPUP_CONTAINER);
        layout.setSpacing("auto");
        popup.getChildren().add(layout);
        popup.setWidgetOverride("stayOpen", "true");
        final String listType = extractEmbeddedType(context);
        final Editor addPopupEditor = createEditorWithNestedContext(context, listType);
        applyRequiredSClasses(listType, addPopupEditor);
        final Button addButton = new Button();
        addButton.setLabel(getL10nDecorator(context, ADD_POPUP_BUTTON_LABEL, KEY_ADD_POPUP_BUTTON_LABEL));
        addButton.setDisabled(true);
        addButton.setSclass(PRIMARY_BUTTON_SCLASS);
        addPopupEditor.addEventListener(Editor.ON_VALUE_CHANGED, createButtonActivatingEventListener(addButton));
        layout.appendChild(addPopupEditor);
        final Button cancel = new Button();
        cancel.setLabel(getL10nDecorator(context, CANCEL_POPUP_BUTTON_LABEL, KEY_CANCEL_POPUP_BUTTON_LABEL));
        cancel.setSclass(CANCEL_BUTTON_SCLASS);
        cancel.addEventListener(Events.ON_CLICK, event -> {
            popup.close();
            addPopupEditor.reload();
        });
        final Div btnContainer = new Div();
        btnContainer.setSclass(BUTTON_CONTAINER_SCLASS);
        btnContainer.appendChild(addButton);
        btnContainer.setSclass(BUTTON_CONTAINER_SCLASS);
        btnContainer.appendChild(cancel);
        btnContainer.appendChild(addButton);
        layout.appendChild(btnContainer);
        final Checkbox addToTopCheckbox = new Checkbox();
        addToTopCheckbox.setClass(YE_LIST_TOOLBAR_ADDPOPUP_CHECKBOX);
        addToTopCheckbox.setLabel(getL10nDecorator(context, "addPopupAddtotopLabel", "add.popup.addtotop.label"));
        layout.getChildren().add(addToTopCheckbox);
        addButton.addEventListener(Events.ON_CLICK,
                        createPopupSaveEventListener(addPopupEditor, listbox, listener, popup, addToTopCheckbox, context));
        addPopupEditor.addEventListener(Editor.ON_EDITOR_EVENT,
                        createPopupSaveEventListener(addPopupEditor, listbox, listener, popup, addToTopCheckbox, context));
        popup.addEventListener(ON_INIT_EVENT, event -> {
            addPopupEditor.setValue(null);
            addPopupEditor.focus();
            addButton.setDisabled(true);
        });
        return popup;
    }


    protected void applyRequiredSClasses(final String listType, final Editor addPopupEditor)
    {
        final String listStyle = listType.replaceAll("\\.", "-");
        if(StringUtils.isNotEmpty(listStyle))
        {
            UITools.modifySClass(addPopupEditor, YE_LIST_TOOLBAR_ADDPOPUP + "-editor", true);
            final int indexOf = listStyle.indexOf('(');
            if(indexOf > -1)
            {
                UITools.modifySClass(addPopupEditor,
                                YE_LIST_TOOLBAR_ADDPOPUP + "-" + StringUtils.lowerCase(listStyle.substring(0, indexOf)), true);
            }
            else
            {
                UITools.modifySClass(addPopupEditor, YE_LIST_TOOLBAR_ADDPOPUP + "-" + StringUtils.lowerCase(listStyle), true);
            }
        }
    }


    private EventListener<Event> createPopupSaveEventListener(final Editor addPopupEditor, final Listbox listbox,
                    final EditorListener<List<E>> listener, final Popup popup, final Checkbox addToTopCheckbox, final EditorContext context)
    {
        return event -> {
            final String data = Objects.toString(event.getData(), "");
            if(StringUtils.isEmpty(data) || EditorListener.ENTER_PRESSED.equals(data))
            {
                final E value = (E)addPopupEditor.getValue();
                if(value != null)
                {
                    if(isWhitelistedValuesParameterSet(context) && isValueNotWhitelisted(context, value))
                    {
                        showNotWhitelistedValueAddNotificationWarning(value);
                        return;
                    }
                    else if(addToTopCheckbox.isChecked())
                    {
                        addNewValue(0, value, listbox, listener);
                    }
                    else
                    {
                        addNewValue(-1, value, listbox, listener);
                    }
                    popup.close();
                }
            }
        };
    }


    private EventListener<Event> createInlineSaveEventListener(final DefaultListEditorComponents defaultListEditorComponents,
                    final EditorListener<List<E>> listener, final EditorContext context)
    {
        return event -> {
            final String data = Objects.toString(event.getData(), "");
            if(StringUtils.isEmpty(data) || EditorListener.ENTER_PRESSED.equals(data))
            {
                final E value = (E)defaultListEditorComponents.getAddInlineEditor().getValue();
                if(value != null)
                {
                    if(isWhitelistedValuesParameterSet(context) && isValueNotWhitelisted(context, value))
                    {
                        showNotWhitelistedValueAddNotificationWarning(value);
                        return;
                    }
                    else if(defaultListEditorComponents.getAddToTopCheckbox().isChecked())
                    {
                        addNewValue(0, value, defaultListEditorComponents.getListbox(), listener);
                    }
                    else
                    {
                        addNewValue(-1, value, defaultListEditorComponents.getListbox(), listener);
                    }
                    defaultListEditorComponents.getContainer().removeChild(defaultListEditorComponents.getAddInlineEditorContainer());
                    handleRelationshipBetweenContentAndEditView(defaultListEditorComponents.getListbox(), defaultListEditorComponents.getContent());
                }
            }
        };
    }


    private boolean isWhitelistedValuesParameterSet(final EditorContext<?> context)
    {
        return context.containsParameter(WHITELISTED_VALUES_CONTEXT_PARAMETER);
    }


    private void handleRelationshipBetweenContentAndEditView(final Listbox listbox, final Div content)
    {
        if(!content.getChildren().contains(listbox))
        {
            content.getChildren().add(listbox);
        }
    }


    private boolean isValueNotWhitelisted(final EditorContext<?> context, final E value)
    {
        return !getWhitelistedValues(context).contains(value);
    }


    private List<E> removeNotWhitelistedValuesFromInitList(final EditorContext<?> context)
    {
        final List<E> whitelistedValues = getWhitelistedValues(context);
        final List<E> initialValueList = (List<E>)context.getInitialValue();
        final Predicate<E> blacklistedValuesLoggingPredicate = value -> {
            final boolean isWhitelisted = whitelistedValues.contains(value);
            if(!isWhitelisted)
            {
                LOG.warn("The value: '{}' is not a whitelisted value and won't be displayed.", value);
            }
            return isWhitelisted;
        };
        return initialValueList.stream().filter(blacklistedValuesLoggingPredicate).collect(Collectors.toList());
    }


    private List<E> getWhitelistedValues(final EditorContext<?> context)
    {
        final String beanName = (String)context.getParameter(WHITELISTED_VALUES_CONTEXT_PARAMETER);
        return (List<E>)SpringUtil.getBean(beanName);
    }


    private void showNotWhitelistedValueAddNotificationWarning(final E value)
    {
        LOG.warn("The value cannot be added. '{}' is not a whitelisted value.", value);
        getNotificationService().notifyUser(NOTIFICATION_SOURCE, "valueAdditionWarning", NotificationEvent.Level.WARNING, value);
    }


    private void showNotWhitelistedEditNotificationWarning(final E value)
    {
        LOG.warn("The value cannot be accepted. '{}' is not a whitelisted value.", value);
        getNotificationService().notifyUser(NOTIFICATION_SOURCE, "valueEditionWarning", NotificationEvent.Level.WARNING, value);
    }


    private void revertValue(final Listbox listbox, final EditorListener<List<E>> listener)
    {
        final List<E> currentValue = getCurrentContent(listbox);
        updateCurrentValue(listbox, listener, currentValue);
    }


    private void addNewValue(final int index, final E value, final Listbox listbox, final EditorListener<List<E>> listener)
    {
        final List<E> currentValue = getCurrentContent(listbox);
        if(index == -1 || index >= currentValue.size())
        {
            currentValue.add(value);
        }
        else
        {
            currentValue.add(index, value);
        }
        updateCurrentValue(listbox, listener, currentValue);
    }


    private void editValue(final int index, final E value, final Listbox listbox, final EditorListener<List<E>> listener)
    {
        final List<E> currentValue = getCurrentContent(listbox);
        if(index == -1 || index >= currentValue.size())
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        else
        {
            currentValue.remove(index);
            currentValue.add(index, value);
        }
        updateCurrentValue(listbox, listener, currentValue);
    }


    private void deleteValueAtIndex(final int index, final Listbox listbox, final EditorListener<List<E>> listener)
    {
        final List<E> currentValue = getCurrentContent(listbox);
        if(index == -1 || index >= currentValue.size())
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        else
        {
            currentValue.remove(index);
        }
        updateCurrentValue(listbox, listener, currentValue);
    }


    private void moveValueAtIndex(final int index, final int referenceIndex, final Listbox listbox,
                    final EditorListener<List<E>> listener)
    {
        int pasteWhere = referenceIndex;
        final List<E> currentValue = getCurrentContent(listbox);
        if(index == -1 || index >= currentValue.size())
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        final E cutValue = currentValue.remove(index);
        if(index < 0 || index > pasteWhere)
        {
            pasteWhere++;
        }
        currentValue.add(pasteWhere, cutValue);
        updateCurrentValue(listbox, listener, currentValue);
    }


    /**
     * Creates button for editing selected entry. The button handler calls
     * {@link Events#postEvent(String, Component, Object)}.
     *
     * @param context
     *           to get some properties from
     */
    protected Button createEditButton(final Listbox listbox, final EditorContext<List<E>> context)
    {
        final Button editButton = new Button();
        editButton.setSclass(YE_LIST_TOOLBAR_BUTTON_EDIT);
        editButton.setDisabled(true);
        editButton.setTooltiptext(getL10nDecorator(context, "buttonLabelEdit", "button.label.edit"));
        editButton.addEventListener(Events.ON_CLICK, event -> {
            if(selectionNotEmpty(listbox))
            {
                Events.postEvent(ON_EDIT_EVENT, listbox.getSelectedItem(), null);
            }
        });
        return editButton;
    }


    private void updateCurrentValue(final Listbox listbox, final EditorListener<List<E>> listener, final List<E> newValue)
    {
        final ListModelList<E> listModel = new ListModelList<>();
        listModel.addAll(newValue);
        listbox.setModel(listModel);
        listener.onValueChanged(CollectionUtils.isEmpty(newValue) ? null : newValue);
        Events.postEvent(ON_UPDATE_EVENT, listbox, null);
    }


    private ListModelList<E> getListModel(final Listbox listbox)
    {
        return (ListModelList<E>)listbox.getListModel();
    }


    private List<E> getCurrentContent(final Listbox listbox)
    {
        final ListModelList<E> listModel = getListModel(listbox);
        if(listModel == null)
        {
            return new ArrayList<>();
        }
        return listModel.getInnerList();
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    private class DefaultListEditorComponents
    {
        private final Editor addInlineEditor;
        private final Listbox listbox;
        private final Div addInlineEditorContainer;
        private final Checkbox addToTopCheckbox;
        private final Div container;
        private final Div content;


        public DefaultListEditorComponents(final Editor addInlineEditor, final Listbox listbox, final Div addInlineEditorContainer,
                        final Checkbox addToTopCheckbox, final Div container, final Div content)
        {
            this.addInlineEditor = addInlineEditor;
            this.listbox = listbox;
            this.addInlineEditorContainer = addInlineEditorContainer;
            this.addToTopCheckbox = addToTopCheckbox;
            this.container = container;
            this.content = content;
        }


        public Editor getAddInlineEditor()
        {
            return addInlineEditor;
        }


        public Listbox getListbox()
        {
            return listbox;
        }


        public Div getAddInlineEditorContainer()
        {
            return addInlineEditorContainer;
        }


        public Checkbox getAddToTopCheckbox()
        {
            return addToTopCheckbox;
        }


        public Div getContainer()
        {
            return container;
        }


        public Div getContent()
        {
            return content;
        }
    }
}
