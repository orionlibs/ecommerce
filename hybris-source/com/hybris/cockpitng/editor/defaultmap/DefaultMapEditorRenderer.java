/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultmap;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractPaginableEditorRenderer;
import com.hybris.cockpitng.labels.LabelService;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listfoot;
import org.zkoss.zul.Listfooter;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;

/**
 * Default renderer for map editors.
 */
public class DefaultMapEditorRenderer<K, V> extends AbstractPaginableEditorRenderer<Map<K, V>>
{
    protected static final String SAVE_EDITOR_IMG = "/icons/icon-checkbox-green.svg";
    protected static final String CLEAR_EDITOR_IMG = "/icons/icon-delete-red.svg";
    protected static final String KEY_VALUE_SEPARATOR_IMG = "/icons/icon_arrow_small_default.png";
    protected static final String MODE_CTX_PARAM = "mode";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultMapEditorRenderer.class);
    private static final String BEAN_ID_TYPE_FACADE = "typeFacade";
    private static final String BEAN_ID_LABEL_SERVICE = "labelService";
    private static final Pattern MAP_EDITORS_PATTERN = Pattern.compile("^(.*),(.*)$");
    private static final String YE_MAP_EDITOR_BTN_CONTAINER = "ye-map-editor-btn-container";
    private static final String YE_MAP_INLINE_EDITOR_KEY = "ye-map-inline-editor-key";
    private static final String YE_MAP_FOOTER = "ye-map-footer";
    private static final String YE_MAP_FOOTER_ADD_CLASS = "ye-map-add";
    private static final String SAVE_EDITOR_CLASS = "ye-map-editor-btn y-btn-primary";
    private static final String CLEAR_EDITOR_CLASS = "ye-map-editor-btn y-btn-secondary";
    private static final String ICONSCLASS_CANCEL_BUTTON = "z-icon-close";
    private static final String ICONSCLASS_CONFIRM_BUTTON = "z-icon-check";
    private static final String DEFAULT_ADD_VALUE_IMAGE = "/icons/icon-plus.png";
    private static final String KEY_VALUE_SEPARATOR_IMG_CLASS = "ye-map-separator";
    private static final String KEY_LABEL_CLASS = "ye-map-key";
    private static final String VALUE_LABEL_CLASS = "ye-map-value";
    private static final String REMOVE_BUTTON_CLASS = "ye-remove-button ye-delete-btn";
    private static final char OPEN_BRACKET_CHAR = '(';
    private static final char CLOSE_BRACKET_CHAR = ')';
    private static final char EDITOR_TYPE_SEPARATOR = ',';
    protected Listbox editorView;
    private LabelService labelService;
    private TypeFacade typeFacade;


    @Override
    public void render(final Component parent, final EditorContext<Map<K, V>> context, final EditorListener<Map<K, V>> listener)
    {
        Validate.notNull("All parameters are mandatory", parent, context, listener);
        final Div container = new Div();
        container.setSclass(YE_LIST_CONTAINER);
        final Div content = new Div();
        content.setSclass(YE_LIST_CONTENT);
        container.appendChild(content);
        editorView = new Listbox();
        editorView.setSclass(YE_LIST_LISTBOX + " " + YE_LIST_LISTBOX + "-defaultmap");
        configurePaging(editorView, context);
        editorView.setItemRenderer(createItemRenderer(context, listener));
        editorView.addEventListener(ON_UPDATE_EVENT, event -> context.removeParameter(ITEM_INDEX_TO_CUT));
        editorView.setEmptyMessage(getL10nDecorator(context, "noValue", "title.no.value"));
        final Map<K, V> initMap = new LinkedHashMap<>();
        if(context.getInitialValue() != null)
        {
            initMap.putAll(context.getInitialValue());
        }
        final ListModelMap<K, V> listModel = new ListModelMap<>();
        listModel.putAll(initMap);
        editorView.setModel(listModel);
        if(context.isEditable())
        {
            final Listfoot listfoot = new Listfoot();
            listfoot.setSclass(YE_MAP_FOOTER);
            listfoot.appendChild(createFooter(listener, context));
            editorView.appendChild(listfoot);
        }
        content.appendChild(editorView);
        container.setParent(parent);
    }


    protected Listfooter createFooter(final EditorListener<Map<K, V>> listener, final EditorContext<Map<K, V>> context)
    {
        final Listfooter footer = new Listfooter();
        footer.appendChild(createFooterContent(context, listener, footer));
        return footer;
    }


    protected Div createFooterContent(final EditorContext<Map<K, V>> context, final EditorListener<Map<K, V>> listener,
                    final Listfooter footer)
    {
        final Div footerContent = new Div();
        footerContent.setSclass(YE_MAP_FOOTER_ADD_CLASS + "-div");
        final Image addImage = new Image(context.getResourceUrl(getFooterContentImage()));
        final Label addLabel = new Label(getL10nDecorator(context, "buttonAddLabel", "button.label.add"));
        addLabel.setSclass(YE_MAP_FOOTER_ADD_CLASS + "-label");
        addImage.setSclass(YE_MAP_FOOTER_ADD_CLASS + "-icon");
        addLabel.addEventListener(Events.ON_CLICK, event -> {
            footer.getChildren().clear();
            context.setParameter(MODE_CTX_PARAM, InlineEditorMode.CREATE);
            final Div inlineEditor = createInlineEditor(context, listener, null, null);
            footer.appendChild(inlineEditor);
            Events.postEvent(ON_INIT_EVENT, inlineEditor, null);
        });
        footerContent.appendChild(addImage);
        footerContent.appendChild(addLabel);
        return footerContent;
    }


    protected String getFooterContentImage()
    {
        return DEFAULT_ADD_VALUE_IMAGE;
    }


    protected void swapValues(final Listbox listbox, final EditorListener<Map<K, V>> listener,
                    final Listitem swappableListitemYang, final Listitem swappableListitemYing)
    {
        final Map<K, V> currentContent = getCurrentContent(listbox);
        final Map.Entry<K, V> entryYing = swappableListitemYing.getValue();
        final Map.Entry<K, V> entryYang = swappableListitemYang.getValue();
        if(entryYing == null || entryYang == null)
        {
            return;
        }
        final V fromValue = currentContent.put(entryYang.getKey(), entryYing.getValue());
        currentContent.put(entryYing.getKey(), fromValue);
        updateCurrentValue(listbox, listener, currentContent);
    }


    /**
     * Defines {@link ListitemRenderer} for the items in the list.
     *
     * @param context
     *           defines editor's environment
     * @param listener
     *           reacts on editor's events
     * @return {@link ListitemRenderer}
     */
    protected ListitemRenderer<Map.Entry<K, V>> createItemRenderer(final EditorContext<Map<K, V>> context,
                    final EditorListener<Map<K, V>> listener)
    {
        return new MapEditorListitemRenderer(context, listener);
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
     * @return {@link Div} with the inline editor
     */
    protected Div createInlineEditor(final EditorContext<Map<K, V>> context, final EditorListener<Map<K, V>> listener,
                    final Listitem listItem, final Map.Entry<K, V> data)
    {
        final InlineEditorMode mode = context.getParameterAs(MODE_CTX_PARAM);
        final Div inlineEditorContainerKey = new Div();
        inlineEditorContainerKey.setSclass(YE_LIST_INLINE);
        final Div inlineEditorContainerValue = new Div();
        inlineEditorContainerValue.setSclass(YE_LIST_INLINE);
        final Div buttonContainer = new Div();
        buttonContainer.setSclass(YE_MAP_EDITOR_BTN_CONTAINER);
        final Pair<String, String> defaultEditors = extractDefaultEditors(context);
        final Editor inlineEditorValue = createEditor(
                        prepareNestedContext(context, defaultEditors.getRight(), extractValueEditorType(context)),
                        extractValueEditorType(context));
        final HtmlBasedComponent inlineEditorKey;
        final EventListener<Event> saveInlineValueListener;
        switch(mode)
        {
            case EDIT:
                inlineEditorKey = getInlineEditorKeyComponent(data.getKey());
                saveInlineValueListener = createSaveInlineValueListener(data.getKey(), null, inlineEditorValue, listener, context);
                inlineEditorValue.setValue(data.getValue());
                break;
            case CREATE:
            default:
                inlineEditorKey = createEditor(prepareNestedContext(context, defaultEditors.getLeft(), extractKeyEditorType(context)),
                                extractKeyEditorType(context));
                saveInlineValueListener = createSaveInlineValueListener(null, (Editor)inlineEditorKey, inlineEditorValue, listener,
                                context);
                break;
        }
        inlineEditorKey.setSclass(YE_MAP_INLINE_EDITOR_KEY);
        inlineEditorContainerKey.appendChild(inlineEditorKey);
        inlineEditorValue.setSclass(YE_LIST_INLINE_EDITOR);
        inlineEditorValue.addEventListener(Editor.ON_EDITOR_EVENT, saveInlineValueListener);
        inlineEditorContainerValue.appendChild(inlineEditorValue);
        final Button saveButton = new Button();
        saveButton.addEventListener(Events.ON_CLICK, saveInlineValueListener);
        saveButton.setSclass(SAVE_EDITOR_CLASS);
        saveButton.setIconSclass(ICONSCLASS_CONFIRM_BUTTON);
        final Button cancelButton = new Button();
        cancelButton.addEventListener(Events.ON_CLICK, event -> revert(mode, listener, context));
        cancelButton.setSclass(CLEAR_EDITOR_CLASS);
        cancelButton.setIconSclass(ICONSCLASS_CANCEL_BUTTON);
        buttonContainer.appendChild(cancelButton);
        buttonContainer.appendChild(saveButton);
        final Div editor = new Div();
        editor.setSclass(YE_LIST_LINE_EDITOR);
        final var editorFields = new Div();
        editorFields.setSclass(YE_LIST_LINE_EDITOR + "-fields");
        editorFields.appendChild(inlineEditorContainerKey);
        editorFields.appendChild(inlineEditorContainerValue);
        editor.appendChild(editorFields);
        editor.appendChild(buttonContainer);
        editor.addEventListener(ON_INIT_EVENT, event -> {
            switch(mode)
            {
                case EDIT:
                    inlineEditorValue.focus();
                    break;
                case CREATE:
                default:
                    inlineEditorKey.focus();
                    break;
            }
        });
        return editor;
    }


    protected String getObjectLabel(final Object key)
    {
        if(key != null && !isCollectionOrMap(key))
        {
            try
            {
                final String type = getTypeFacade().getType(key);
                final DataType dataType = getTypeFacade().load(type);
                if(dataType.isAtomic())
                {
                    return ObjectUtils.toString(key);
                }
            }
            catch(final TypeNotFoundException e)
            {
                LOG.warn("Could not find element's type", e);
            }
        }
        return StringUtils.defaultIfEmpty(getLabelService().getObjectLabel(key), ObjectUtils.toString(key));
    }


    protected boolean isCollectionOrMap(final @Nullable Object obj)
    {
        return obj instanceof Collection || obj instanceof Map;
    }


    protected HtmlBasedComponent getInlineEditorKeyComponent(final Object key)
    {
        return new Label(getObjectLabel(key));
    }


    protected EventListener<Event> createSaveInlineValueListener(final K key, final Editor inlineEditorKey,
                    final Editor inlineEditorValue, final EditorListener<Map<K, V>> listener, final EditorContext<Map<K, V>> context)
    {
        return event -> {
            final String data = ObjectUtils.toString(event.getData());
            if(StringUtils.isEmpty(data) || EditorListener.ENTER_PRESSED.equals(data))
            {
                final K newKey = Objects.nonNull(key) ? key
                                : Objects.nonNull(inlineEditorKey) ? (K)inlineEditorKey.getValue() : null;
                final V value = (V)inlineEditorValue.getValue();
                editValue(newKey, value, editorView, listener);
                revertFooter(context, listener);
            }
            if(EditorListener.ESCAPE_PRESSED.equals(data))
            {
                revert(context.getParameterAs(MODE_CTX_PARAM), listener, context);
            }
        };
    }


    protected void revert(final InlineEditorMode mode, final EditorListener<Map<K, V>> listener,
                    final EditorContext<Map<K, V>> context)
    {
        switch(mode)
        {
            case EDIT:
                revertValue(editorView, listener);
                break;
            case CREATE:
                revertFooter(context, listener);
                break;
            default:
        }
    }


    protected void revertValue(final Listbox listbox, final EditorListener<Map<K, V>> listener)
    {
        final Map<K, V> currentValue = getCurrentContent(listbox);
        updateCurrentValue(listbox, listener, currentValue);
    }


    protected void updateCurrentValue(final Listbox listbox, final EditorListener<Map<K, V>> listener, final Map<K, V> newValue)
    {
        final ListModelMap<K, V> mapModel = new ListModelMap<>();
        mapModel.putAll(newValue);
        listbox.setModel(mapModel);
        listener.onValueChanged(newValue);
        Events.postEvent(ON_UPDATE_EVENT, listbox, null);
    }


    protected void revertFooter(final EditorContext<Map<K, V>> context, final EditorListener<Map<K, V>> listener)
    {
        final Listfooter listfooter = (Listfooter)editorView.getListfoot().getFirstChild();
        listfooter.getChildren().clear();
        listfooter.appendChild(createFooterContent(context, listener, listfooter));
    }


    protected String extractKeyEditorType(final EditorContext<Map<K, V>> context)
    {
        return extractArguments(extractEmbeddedType(context)).getKey();
    }


    protected String extractValueEditorType(final EditorContext<Map<K, V>> context)
    {
        return extractArguments(extractEmbeddedType(context)).getValue();
    }


    protected Pair<String, String> extractArguments(final String value)
    {
        final int separatorPosition = getEditorTypeSeparatorPosition(value);
        if(separatorPosition != StringUtils.INDEX_NOT_FOUND)
        {
            final String k = value.substring(0, separatorPosition).trim();
            final String v = value.substring(separatorPosition + 1, value.length()).trim();
            return new ImmutablePair<>(k, v);
        }
        return new ImmutablePair<>(value, StringUtils.EMPTY);
    }


    protected int getEditorTypeSeparatorPosition(final String value)
    {
        int numberOfOpenBrackets = 0;
        for(int i = 0; i < value.length(); i++)
        {
            final char c = value.charAt(i);
            if(c == OPEN_BRACKET_CHAR)
            {
                numberOfOpenBrackets++;
            }
            else if(c == CLOSE_BRACKET_CHAR)
            {
                numberOfOpenBrackets--;
            }
            else if(numberOfOpenBrackets == 0 && c == EDITOR_TYPE_SEPARATOR)
            {
                return i;
            }
        }
        return StringUtils.INDEX_NOT_FOUND;
    }


    @Override
    protected Popup createAddPopup(final Listbox listbox, final EditorListener<Map<K, V>> listener,
                    final EditorContext<Map<K, V>> context)
    {
        // not implemented, uses inline editor instead
        return null;
    }


    protected EditorContext<Map<K, V>> prepareNestedContext(final EditorContext<Map<K, V>> context, final String editorType,
                    final String valueType)
    {
        final EditorContext<Map<K, V>> editorContext = new EditorContext<>(context.getInitialValue(), context.getDefinition(),
                        context.getParameters(), context.getLabels(), context.getReadableLocales(), context.getWritableLocales());
        editorContext.setParameter(Editor.VALUE_EDITOR, editorType);
        editorContext.setValueType(valueType);
        editorContext.setEditable(context.isEditable());
        editorContext.setOrdered(context.isOrdered());
        editorContext.setOptional(context.isOptional());
        return editorContext;
    }


    protected Pair<String, String> extractDefaultEditors(final EditorContext<Map<K, V>> context)
    {
        final String valueEditor = context.getParameterAs(Editor.VALUE_EDITOR);
        if(StringUtils.isNotBlank(valueEditor))
        {
            final String embeddedEditor = extractEmbeddedEditor(valueEditor);
            if(StringUtils.isNotBlank(embeddedEditor))
            {
                final Matcher matcher = MAP_EDITORS_PATTERN.matcher(embeddedEditor);
                if(matcher.matches())
                {
                    return new ImmutablePair<>(matcher.group(1).trim(), matcher.group(2).trim());
                }
            }
        }
        return new ImmutablePair<>(null, null);
    }


    protected void editValue(final K key, final V value, final Listbox listbox, final EditorListener<Map<K, V>> listener)
    {
        final Map<K, V> currentValue = getCurrentContent(listbox);
        currentValue.put(key, value);
        updateCurrentValue(listbox, listener, currentValue);
    }


    protected void deleteValueAtIndex(final int idx, final Listbox listbox, final EditorListener<Map<K, V>> listener)
    {
        final Map<K, V> currentValue = getCurrentContent(listbox);
        currentValue.remove((listbox.getItemAtIndex(idx).<Map.Entry<K, V>>getValue()).getKey());
        updateCurrentValue(listbox, listener, currentValue);
    }


    protected ListModel<Map.Entry<K, V>> getListModel(final Listbox listbox)
    {
        return listbox.getListModel();
    }


    protected Map<K, V> getCurrentContent(final Listbox listbox)
    {
        final ListModelMap<K, V> listModel = (ListModelMap<K, V>)getListModel(listbox);
        if(listModel == null)
        {
            return new HashMap<>();
        }
        return listModel.getInnerMap();
    }


    /**
     * @return the labelService
     */
    protected LabelService getLabelService()
    {
        if(labelService == null)
        {
            this.labelService = SpringUtil.getApplicationContext().getBean(BEAN_ID_LABEL_SERVICE, LabelService.class);
        }
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public TypeFacade getTypeFacade()
    {
        if(typeFacade == null)
        {
            typeFacade = SpringUtil.getApplicationContext().getBean(BEAN_ID_TYPE_FACADE, TypeFacade.class);
        }
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected enum InlineEditorMode
    {
        CREATE, EDIT
    }


    protected class MapEditorListitemRenderer implements ListitemRenderer<Map.Entry<K, V>>
    {
        private final EditorContext<Map<K, V>> context;
        private final EditorListener<Map<K, V>> listener;


        public MapEditorListitemRenderer(final EditorContext<Map<K, V>> context, final EditorListener<Map<K, V>> listener)
        {
            this.context = context;
            this.listener = listener;
        }


        @Override
        public void render(final Listitem listitem, final Map.Entry<K, V> data, final int idx)
        {
            listitem.setValue(data);
            final Listcell listcell = renderItem(data);
            listitem.appendChild(listcell);
            if(context.isEditable())
            {
                listitem.setDraggable(editorView.getUuid());
                listitem.setDroppable(editorView.getUuid());
                listitem.addEventListener(Events.ON_DROP, event -> onDrop((DropEvent)event, listitem));
                listcell.addEventListener(Events.ON_DOUBLE_CLICK, event -> showInlineEditor(listitem, data));
            }
        }


        protected Listcell renderItem(final Map.Entry<K, V> item)
        {
            final Listcell container = new Listcell();
            if(item != null)
            {
                final Label keyLabel = new Label();
                keyLabel.setSclass(KEY_LABEL_CLASS);
                keyLabel.setValue(item.getKey() != null ? getObjectLabel(item.getKey()) : StringUtils.EMPTY);
                container.appendChild(keyLabel);
                final Image arrowImage = new Image(context.getResourceUrl(KEY_VALUE_SEPARATOR_IMG));
                arrowImage.setSclass(KEY_VALUE_SEPARATOR_IMG_CLASS);
                container.appendChild(arrowImage);
                final Label valueLabel = new Label();
                valueLabel.setSclass(VALUE_LABEL_CLASS);
                valueLabel.setValue(item.getValue() != null ? getObjectLabel(item.getValue()) : StringUtils.EMPTY);
                container.appendChild(valueLabel);
                final Div removeImage = new Div();
                removeImage.setSclass(REMOVE_BUTTON_CLASS);
                removeImage.addEventListener(Events.ON_CLICK, this::onRemove);
                removeImage.setVisible(context.isEditable());
                container.appendChild(removeImage);
            }
            return container;
        }


        protected void onRemove(final Event event)
        {
            if(selectionNotEmpty(editorView))
            {
                final boolean confirmDelete = BooleanUtils.toBoolean((String)context.getParameter(CONFIRM_DELETE_PARAM));
                if(confirmDelete)
                {
                    Messagebox.show(getL10nDecorator(context, "deletePopupMessageLabel", "delete.popup.message"),
                                    getL10nDecorator(context, "deletePopupTitleLabel", "delete.popup.title"),
                                    new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.CANCEL}, Messagebox.QUESTION,
                                    messageBoxEvent -> {
                                        final Messagebox.Button selection = messageBoxEvent.getButton();
                                        if(Messagebox.Button.YES.equals(selection))
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
        }


        protected void onDrop(final DropEvent event, final Listitem listitem)
        {
            final Component dragged = event.getDragged();
            if(dragged instanceof Listitem)
            {
                final Listitem draggedListItem = (Listitem)dragged;
                swapValues(listitem.getListbox(), listener, draggedListItem, listitem);
            }
        }


        protected void showInlineEditor(final Listitem listItem, final Map.Entry<K, V> data)
        {
            listItem.getChildren().clear();
            context.setParameter(MODE_CTX_PARAM, InlineEditorMode.EDIT);
            final Listcell inlineEditorCell = new Listcell();
            inlineEditorCell.setSclass(YE_LIST_LINE_EDITOR);
            inlineEditorCell.appendChild(createInlineEditor(context, listener, listItem, data));
            listItem.appendChild(inlineEditorCell);
            listItem.setContext((Popup)null);
            Events.postEvent(ON_INIT_EVENT, inlineEditorCell, null);
        }
    }
}
