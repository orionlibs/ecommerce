/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import com.hybris.cockpitng.common.model.TypeSelectorTreeModel;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Span;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;
import org.zkoss.zul.ext.Pageable;

/**
 * Class responsible for creating layout for {@link DefaultReferenceEditor} and {@link DefaultMultiReferenceEditor}
 */
public class ReferenceEditorLayout<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(ReferenceEditorLayout.class);
    public static final String ORG_ZKOSS_ZUL_LISTBOX_ROD = "org.zkoss.zul.listbox.rod";
    public static final String ORG_ZKOSS_ZUL_LISTBOX_INIT_ROD_SIZE = "org.zkoss.zul.listbox.initRodSize";
    public static final String CSS_YW_LISTBOX_DISABLED = "yw-listbox-disabled";
    public static final String CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER = "ye-create-type-selector-container";
    public static final String CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER_TREE = "ye-create-type-selector-container-tree";
    public static final String CSS_YE_CREATE_TYPE_SELECTOR_BUTTON = "ye-create-type-selector-button";
    public static final String CSS_YE_BUTTON_ACTIVE = "ye-button-active";
    public static final String CSS_Z_BANDBOX_BUTTON = "z-bandbox-button";
    public static final String CSS_Z_BANDBOX_ICON = "z-bandbox-icon";
    public static final String CSS_Z_ICON_CARET_DOWN = "z-icon-caret-down";
    public static final String CSS_REFERENCE_EDITOR_PAGING = "referenceEditorPaging";
    public static final String CSS_WIDTH_100_PERCENT = "100%";
    public static final String CSS_YE_EDITOR_DISABLED = "ye-editor-disabled";
    public static final String CSS_YE_REMOVE_ENABLED = "ye-remove-enabled";
    public static final String CSS_YE_DEFAULT_REFERENCE_EDITOR_PREVIEW_POPUP_IMAGE = "ye-default-reference-editor-preview-popup-image";
    private static final String CSS_MAIN_REFERENCE_EDITOR = "ye-default-reference-editor";
    private static final String CSS_REFERENCE_EDITOR_LISTBOX = "ye-default-reference-editor-listbox";
    private static final String CSS_REFERENCE_EDITOR_BANDBOX = "ye-default-reference-editor-bandbox";
    private static final String CSS_CREATE_ONLY = "ye-create-only";
    private static final String CSS_REFERENCE_EDITOR_BANDPOPUP = "ye-default-reference-editor-bandpopup";
    private static final String CSS_REFERENCE_EDITOR_SELECTED_ITEMS_LISTBOX = "ye-default-reference-editor-selected-listbox";
    private static final String CSS_REFERENCE_EDITOR_BANDBOX_LIST_EXPANDED = "ye-default-reference-editor-bandbox-list-expanded";
    private static final String CSS_REFERENCE_EDITOR_BANDBOX_LIST_COLLAPSED = "ye-default-reference-editor-bandbox-list-collapsed";
    private static final String YTESTID_REFERENCE_EDITOR_BANDBOX = "reference-editor-bandbox";
    private static final String YTESTID_REFERENCE_EDITOR_BANDBOX_POPUP = "reference-editor-bandbox-popup";
    private static final String ON_RIGHT_ARROW = "onRightArrow";
    private static final String ON_LEFT_ARROW = "onLeftArrow";
    private static final String ON_DOWN_ARROW = "onDownArrow";
    private static final String ON_UP_ARROW = "onUpArrow";
    private static final String JS_BANDBOX_ADJUST_FUNCTION_PATTERN = "var bandbox = zk.Widget.$('#%s');bandbox._checkPopupSpaceAndPosition(bandbox.getPopupNode_(), bandbox.$n());";
    private final ListModelList<T> availableElementsListModel = new ListModelList<>();
    private final Listbox currentlySelectedList = new Listbox();
    private final ListModelList<T> selectedElementsListModel = new ListModelList<>();
    private final ReferenceEditorLogic<T> referenceEditor;
    private final Base configuration;
    private boolean ordered;
    private boolean readOnly;
    private String placeholderKey;
    private Bandbox bandbox;
    private Bandbox createOnlyBandbox;
    private A dropButton;
    private Listbox listbox;
    private Paging paging;
    private PermissionFacade permissionFacade;
    private TypeFacade typeFacade;
    private LabelService labelService;
    private int renderOnDemandSize = -1; // default for single reference editor
    private int selectedItemsMaxSize = 1; // default for single reference editor
    private final ReferenceEditorListItemRendererFactory<T> referenceEditorListItemRendererFactory;


    public ReferenceEditorLayout(final ReferenceEditorLogic<T> referenceEditorInterface)
    {
        this(referenceEditorInterface, null);
    }


    public ReferenceEditorLayout(final ReferenceEditorLogic<T> referenceEditorInterface, final Base configuration)
    {
        this.referenceEditor = referenceEditorInterface;
        this.configuration = configuration;
        referenceEditorListItemRendererFactory = new ReferenceEditorListItemRendererFactory<>(this);
    }


    public void setEditableState(final boolean contextIsEditable)
    {
        this.readOnly = !contextIsEditable;
        if(!contextIsEditable)
        {
            if(referenceEditor.isOnlyCreateMode())
            {
                getCreateOnlyBandbox().setDisabled(true);
                getDropButton().setDisabled(true);
            }
            else
            {
                getBandbox().setReadonly(true);
                getBandbox().setButtonVisible(false);
                getBandbox().setDisabled(true);
            }
            UITools.modifySClass(currentlySelectedList, CSS_YW_LISTBOX_DISABLED, readOnly);
            currentlySelectedList.setDisabled(readOnly);
        }
    }


    protected void renderCreateOnlyLayout(final Component parent)
    {
        final Div container = new Div();
        container.setParent(parent);
        UITools.addSClass(getCreateOnlyBandbox(), CSS_CREATE_ONLY);
        getCreateOnlyBandbox().setAutodrop(false);
        getCreateOnlyBandbox().setReadonly(true);
        getCreateOnlyBandbox().setButtonVisible(false);
        getCreateOnlyBandbox().setPopupWidth("100%");
        getCreateOnlyBandbox().setDisabled(readOnly);
        YTestTools.modifyYTestId(getCreateOnlyBandbox(), YTESTID_REFERENCE_EDITOR_BANDBOX);
        UITools.addSClass(getCreateOnlyBandbox(), CSS_REFERENCE_EDITOR_BANDBOX);
        container.appendChild(getCreateOnlyBandbox());
        UITools.addSClass(getDropButton(), CSS_Z_BANDBOX_BUTTON);
        UITools.addSClass(getDropButton(), CSS_CREATE_ONLY);
        final Span caretIcon = new Span();
        UITools.addSClass(caretIcon, CSS_Z_BANDBOX_ICON);
        UITools.addSClass(caretIcon, CSS_Z_ICON_CARET_DOWN);
        getDropButton().appendChild(caretIcon);
        container.appendChild(getDropButton());
        final Bandpopup createOnlyBandpopup = new Bandpopup();
        final NestedObjectCreator nestedObjectCreator = new NestedObjectCreator();
        createOnlyBandpopup.setParent(getCreateOnlyBandbox());
        getDropButton().addEventListener(Events.ON_CLICK, e -> {
            final boolean opened = getCreateOnlyBandbox().isOpen();
            if(!opened)
            {
                createOnlyBandpopup.setParent(getCreateOnlyBandbox());
                renderPartOfPopup(getCreateOnlyBandbox(), nestedObjectCreator, referenceEditor);
            }
            else
            {
                removePopupContent(createOnlyBandpopup);
                getCreateOnlyBandbox().getChildren().remove(getCreateOnlyBandbox().getDropdown());
            }
            getCreateOnlyBandbox().setOpen(!opened);
            getCreateOnlyBandbox().invalidate();
        });
        getCreateOnlyBandbox().addEventListener(Events.ON_OPEN, (EventListener<OpenEvent>)openEvent -> {
            if(!openEvent.isOpen())
            {
                removePopupContent(createOnlyBandpopup);
                getCreateOnlyBandbox().getChildren().remove(getCreateOnlyBandbox().getDropdown());
            }
        });
        getCreateOnlyBandbox().addEventListener(Events.ON_CLICK, e -> {
            final String typeCode = StringUtils.defaultIfBlank(nestedObjectCreator.getUserChosenType(),
                            referenceEditor.getTypeCode());
            final DataType dataType = loadDataType(typeCode);
            if(dataType != null)
            {
                if(dataType.isAbstract())
                {
                    Events.sendEvent(getDropButton(), new Event(Events.ON_CLICK));
                }
                else
                {
                    referenceEditor.createNewReference(typeCode);
                }
            }
        });
        // prevent on click event from drop down list to be forwarded to bandbox
        getCreateOnlyBandbox().getDropdown().addEventListener(Events.ON_CLICK, Event::stopPropagation);
        final String labelText = referenceEditor.getStringRepresentationOfObject((T)nestedObjectCreator);
        getCreateOnlyBandbox().setText(labelText);
        getCreateOnlyBandbox().setId(Editor.DEFAULT_FOCUS_COMPONENT_ID);
        UITools.addSClass(createOnlyBandpopup, CSS_CREATE_ONLY);
        YTestTools.modifyYTestId(createOnlyBandpopup, YTESTID_REFERENCE_EDITOR_BANDBOX_POPUP);
    }


    public void createLayout(final Component parent)
    {
        final Div verticalLayout = new Div();
        verticalLayout.setParent(parent);
        UITools.addSClass(verticalLayout, CSS_MAIN_REFERENCE_EDITOR);
        UITools.addSClass(currentlySelectedList, CSS_REFERENCE_EDITOR_SELECTED_ITEMS_LISTBOX);
        currentlySelectedList.setParent(verticalLayout);
        currentlySelectedList.setModel(selectedElementsListModel);
        currentlySelectedList.setVisible(!selectedElementsListModel.isEmpty());
        currentlySelectedList.setItemRenderer(createSelectedItemsListItemRenderer());
        if(renderOnDemandSize != -1)
        {
            currentlySelectedList.setAttribute(ORG_ZKOSS_ZUL_LISTBOX_ROD, true);
            currentlySelectedList.setAttribute(ORG_ZKOSS_ZUL_LISTBOX_INIT_ROD_SIZE, renderOnDemandSize);
        }
        currentlySelectedList.applyProperties();
        currentlySelectedList.addEventListener(ZulEvents.ON_AFTER_RENDER, event -> {
            final int size = selectedElementsListModel.size();
            final int rows = size > selectedItemsMaxSize ? selectedItemsMaxSize : size;
            currentlySelectedList.setRows(rows);
        });
        appendEditorsToLayout(verticalLayout);
    }


    protected void appendEditorsToLayout(final Div verticalLayout)
    {
        if(referenceEditor.isOnlyCreateMode())
        {
            renderCreateOnlyLayout(verticalLayout);
            if(!referenceEditor.allowNestedObjectCreation() || !referenceEditor.isEditable())
            {
                getCreateOnlyBandbox().setDisabled(true);
                getDropButton().setDisabled(true);
            }
        }
        else
        {
            final boolean canReadAttributeType = canReadAttributeTypeOrSubType(referenceEditor.getTypeCode());
            createBandBox(verticalLayout);
            if(!referenceEditor.isEditable() || !canReadAttributeType)
            {
                getBandbox().setDisabled(true);
            }
        }
    }


    protected boolean canReadAttributeTypeOrSubType(final String typeCode)
    {
        final boolean canReadType = getPermissionFacade().canReadType(typeCode);
        if(!canReadType)
        {
            final DataType dataType = loadDataType(typeCode);
            if(dataType == null)
            {
                return false;
            }
            final List<String> subtypes = dataType.getSubtypes();
            return subtypes.stream().map(this::canReadAttributeTypeOrSubType).anyMatch(BooleanUtils::isTrue);
        }
        return true;
    }


    protected void createBandBox(final Div verticalLayout)
    {
        Validate.notNull("Parent component may not be null", verticalLayout);
        bandbox = new Bandbox();
        getBandbox().setParent(verticalLayout);
        getBandbox().setDisabled(readOnly);
        YTestTools.modifyYTestId(getBandbox(), YTESTID_REFERENCE_EDITOR_BANDBOX);
        UITools.addSClass(getBandbox(), CSS_REFERENCE_EDITOR_BANDBOX);
        UITools.addSClass(getBandbox(), CSS_REFERENCE_EDITOR_BANDBOX_LIST_COLLAPSED);
        getBandbox().setAutodrop(true);
        getBandbox().setPopupWidth("100%");
        getBandbox().setCtrlKeys("#down");
        if(StringUtils.isNotBlank(this.placeholderKey))
        {
            getBandbox().setPlaceholder(Labels.getLabel(placeholderKey));
        }
        getBandbox().addEventListener(Events.ON_OK, event -> referenceEditor.forwardEditorEvent(Events.ON_OK));
        getBandbox().setId(Editor.DEFAULT_FOCUS_COMPONENT_ID);
        final Bandpopup bandpopup = new Bandpopup();
        UITools.addSClass(bandpopup, CSS_REFERENCE_EDITOR_BANDPOPUP);
        bandpopup.setParent(getBandbox());
        YTestTools.modifyYTestId(bandpopup, YTESTID_REFERENCE_EDITOR_BANDBOX_POPUP);
        getBandbox().addEventListener(Events.ON_OPEN, (final OpenEvent event) -> onOpenHandler(event, bandpopup));
        getBandbox().addEventListener(Events.ON_CLICK, event -> {
            if(!getBandbox().isOpen())
            {
                getBandbox().setOpen(true);
                Events.sendEvent(new OpenEvent(Events.ON_OPEN, getBandbox(), true));
            }
        });
        getBandbox().addEventListener(Events.ON_CTRL_KEY, event -> {
            if(KeyEvent.DOWN == ((KeyEvent)event).getKeyCode() && !getListbox().getItems().isEmpty())
            {
                setListboxFocus();
            }
        });
        if(referenceEditor.isReferenceAdvancedSearchEnabled())
        {
            openReferenceSearchOnBandboxButton(getBandbox());
        }
    }


    protected void onOpenHandler(final OpenEvent event, final Bandpopup bandpopup)
    {
        Validate.notNull("All arguments are mandatory", event, bandpopup);
        Validate.notNull("Reference editor must be initialized before invocation of this method", referenceEditor);
        if(event.isOpen())
        {
            updateReferenceEditorModel(getBandbox().getText());
            createPopupContent(bandpopup);
        }
        else
        {
            removePopupContent(bandpopup);
        }
    }


    @InextensibleMethod
    private void updateReferenceEditorModel(final String searchQuery)
    {
        if(StringUtils.isBlank(searchQuery))
        {
            referenceEditor.updateReferencesListBoxModel();
        }
        else
        {
            referenceEditor.updateReferencesListBoxModel(searchQuery);
        }
    }


    protected void openReferenceSearchOnBandboxButton(final Bandbox bandbox)
    {
        Validate.notNull("Bandbox may not be null", bandbox);
        bandbox.setWidgetOverride("_doBtnClick",
                        "function(event){CockpitNG.sendEvent('#'+event.target.uuid,'onSearchBtnClick'); event.stop();}");
        bandbox.addEventListener("onSearchBtnClick", event -> {
            if(!bandbox.isDisabled())
            {
                referenceEditor.openReferenceAdvancedSearch(selectedElementsListModel.getInnerList());
            }
        });
    }


    protected void createPopupContent(final Bandpopup bandpopup)
    {
        Validate.notNull("Bandpopup may not be null", bandpopup);
        bandpopup.getChildren().clear();
        final Div bandpopupLayout = new Div();
        bandpopupLayout.setParent(bandpopup);
        listbox = new Listbox();
        getListbox().setWidth(CSS_WIDTH_100_PERCENT);
        getListbox().setModel(availableElementsListModel);
        getListbox().setParent(bandpopupLayout);
        getListbox().setItemRenderer(createListItemRenderer());
        UITools.addSClass(getListbox(), CSS_REFERENCE_EDITOR_LISTBOX);
        getListbox().setWidgetOverride("_doRight",
                        "function(event){CockpitNG.sendEvent('#'+event.uuid,'" + ON_RIGHT_ARROW + "');}");
        getListbox().setWidgetOverride("_doLeft", "function(event){CockpitNG.sendEvent('#'+event.uuid,'" + ON_LEFT_ARROW + "');}");
        getListbox().setWidgetOverride("_doKeyDown", "function(event){" + //
                        "var keyCode = event.keyCode; " + //
                        "if (keyCode == 40) { " + //
                        "CockpitNG.sendEvent('#'+event.currentTarget.uuid,'" + ON_DOWN_ARROW + "');" + //
                        "} " + //
                        "this.$_doKeyDown(event); " + //
                        "}");
        getListbox().addEventListener(ON_DOWN_ARROW, this::jumpToTypeSelectorForLastElement);
        getListbox().addEventListener(Events.ON_CANCEL, event -> {
            closeBandbox();
            getBandbox().focus();
        });
        paging = new Paging();
        UITools.addSClass(paging, CSS_REFERENCE_EDITOR_PAGING);
        paging.setParent(bandpopupLayout);
        paging.addEventListener(ZulEvents.ON_PAGING, (EventListener<PagingEvent>)event -> {
            if(referenceEditor.getPageable() == null)
            {
                return;
            }
            final Pageable zulPageable = event.getPageable();
            if(zulPageable != null)
            {
                changePage(zulPageable.getActivePage());
            }
        });
        // don't propagate click to bandbox after select
        getListbox().addEventListener(Events.ON_CLICK, Event::stopPropagation);
        buildPaging();
    }


    protected void jumpToTypeSelectorForLastElement(final Event event)
    {
        final int selectedIndex = getListbox().getSelectedIndex();
        if(selectedIndex + 1 >= getListbox().getItemCount())
        {
            jumpToTypeSelector(event);
        }
    }


    protected void jumpToTypeSelector(final Event event)
    {
        final Tree typeSelectorTree = getTypeSelectorTree(event);
        final Div typeSelectorContainer = getTypeSelectorContainer(event);
        if(typeSelectorTree != null && typeSelectorContainer.isVisible())
        {
            final Iterator<Treeitem> treeItems = typeSelectorTree.getItems().iterator();
            if(treeItems.hasNext())
            {
                getListbox().clearSelection();
                typeSelectorTree.selectItem(treeItems.next());
                typeSelectorTree.focus();
            }
        }
    }


    protected Tree getTypeSelectorTree(final Event event)
    {
        Validate.notNull("Event may not be null", event);
        return (Tree)event.getTarget().query("." + CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER_TREE);
    }


    protected Div getTypeSelectorContainer(final Event event)
    {
        Validate.notNull("Event may not be null", event);
        return (Div)event.getTarget().query("." + CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER);
    }


    /**
     * @deprecated since 6.7, method is not used any more, use {@link ReferenceEditorListItemRendererFactory#nextPage()}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void nextPage()
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use {@link ReferenceEditorListItemRendererFactory#previousPage()}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void previousPage()
    {//deprecated
    }


    public void changePage(final int pageNumber)
    {
        getListbox().setModel((ListModelList<?>)null);
        referenceEditor.getPageable().setPageNumber(pageNumber);
        refreshPopupContent();
        refreshPagingState();
        getListbox().setModel(availableElementsListModel);
        setListboxFocus();
        Clients.evalJavaScript(String.format(JS_BANDBOX_ADJUST_FUNCTION_PATTERN, getBandbox().getUuid()));
    }


    protected void setListboxFocus()
    {
        getListbox().focus();
        final ListModel<Object> model = getListbox().getModel();
        if(model != null && model.getSize() > 0)
        {
            getListbox().setSelectedIndex(0);
        }
        else
        {
            LOG.warn("Could not send listbox focus due to empty or null model: {}", model);
        }
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#listBoxItemChosen(Object, Event)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void listBoxItemChosen(final T selected, final Event event)
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#getTypeSelectorButton(Event)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected Component getTypeSelectorButton(final Event event)
    {
        return null;
    }


    protected void closeBandbox()
    {
        getListbox().clearSelection();
        getBandbox().setOpen(false);
        getBandbox().invalidate();
        Events.sendEvent(getBandbox(), new OpenEvent(Events.ON_OPEN, getBandbox(), false));
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#showAutoCorrectedSearchResults(AutoCorrectionInfo)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void showAutoCorrectedSearchResults(final AutoCorrectionInfo selected)
    {//deprecated
    }


    public Listbox getListbox()
    {
        if(listbox == null)
        {
            listbox = new Listbox();
        }
        return listbox;
    }


    protected void removePopupContent(final Bandpopup bandpopup)
    {
        if(bandpopup != null)
        {
            bandpopup.getChildren().clear();
        }
        listbox = null;
        paging = null;
    }


    protected ListitemRenderer<T> createSelectedItemsListItemRenderer()
    {
        return getListItemRendererFactory().createSelectedItemsListItemRenderer();
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#dragAndDropItems(Listitem, Listitem)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void dragAndDropItems(final Listitem dragged, final Listitem dropped)
    {//deprecated
    }


    protected ListitemRenderer<T> createListItemRenderer()
    {
        return getListItemRendererFactory().createListItemRenderer();
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#openTypeSelectorTree(Event)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void openTypeSelectorTree(final Event event)
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#closeTypeSelectorTree(Event)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void closeTypeSelectorTree(final Event event)
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#renderAutoCorrectionRow(Listitem, AutoCorrectionInfo, Listcell)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderAutoCorrectionRow(final Listitem item, final AutoCorrectionInfo autoCorrectionInfo, final Listcell cell)
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#preparePreviewPopup(Component, ObjectPreview, Image)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void preparePreviewPopup(final Component parent, final ObjectPreview preview, final Image target)
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#getObjectPreviewService()} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected ObjectPreviewService getObjectPreviewService()
    {
        return null;
    }


    protected void refreshPagingState()
    {
        if(referenceEditor.getPageable() != null && paging != null)
        {
            if(referenceEditor.getPageable().hasNextPage() || referenceEditor.getPageable().hasPreviousPage())
            {
                paging.setVisible(true);
                paging.setPageSize(referenceEditor.getPageable().getPageSize());
                paging.setTotalSize(referenceEditor.getPageable().getTotalCount());
                paging.setActivePage(referenceEditor.getPageable().getPageNumber());
            }
            else
            {
                paging.setVisible(false);
            }
        }
    }


    public void addListeners()
    {
        addOnTypingEventListener();
        addOnSelectedItemsModelChange();
        addOnOpenBandboxEventListener();
    }


    protected void addOnOpenBandboxEventListener()
    {
        getBandbox().addEventListener(Events.ON_OPEN, (final OpenEvent event) -> {
            final Bandbox component = getBandbox();
            final boolean open = event.isOpen();
            UITools.modifySClass(component, CSS_REFERENCE_EDITOR_BANDBOX_LIST_EXPANDED, open);
            UITools.modifySClass(component, CSS_REFERENCE_EDITOR_BANDBOX_LIST_COLLAPSED, !open);
        });
    }


    protected void addOnSelectedItemsModelChange()
    {
        selectedElementsListModel.addListDataListener(event -> {
            final int size = event.getModel().getSize();
            if(size == 0)
            {
                currentlySelectedList.setVisible(false);
            }
            else
            {
                currentlySelectedList.setVisible(true);
            }
        });
    }


    protected void addOnTypingEventListener()
    {
        getBandbox().addEventListener(Events.ON_CHANGING, (EventListener<InputEvent>)event -> {
            final String searchQuery = event.getValue() == null ? StringUtils.EMPTY : event.getValue();
            updateReferenceEditorModel(searchQuery);
            buildPaging();
            Clients.evalJavaScript(String.format(JS_BANDBOX_ADJUST_FUNCTION_PATTERN, getBandbox().getUuid()));
        });
    }


    public void buildPaging()
    {
        if(referenceEditor.getPageable() == null)
        {
            return;
        }
        refreshPopupContent();
        refreshPagingState();
    }


    protected void refreshPopupContent()
    {
        availableElementsListModel.clear();
        final List<T> currentPage = referenceEditor.getPageable().getCurrentPage();
        availableElementsListModel.addAll(currentPage);
        if(currentPage.isEmpty() && referenceEditor.getPageable() instanceof FullTextSearchPageable)
        {
            final FullTextSearchPageable<?> fullTextSearchPageable = (FullTextSearchPageable<?>)referenceEditor.getPageable();
            final FullTextSearchData searchData = fullTextSearchPageable.getFullTextSearchData();
            if(searchData != null)
            {
                final String autoCorrection = searchData.getAutocorrection();
                if(StringUtils.isNotBlank(autoCorrection))
                {
                    availableElementsListModel.add((T)new AutoCorrectionInfo(autoCorrection));
                }
            }
        }
        addCreateNewReferenceListitem();
    }


    public void onAddSelectedObject(final T obj, final boolean hideBandboxWhenAnyItemSelected)
    {
        if(obj != null)
        {
            if(contains(obj))
            {
                selectedElementsListModel.remove(obj);
            }
            selectedElementsListModel.add(obj);
        }
        decideOnBandboxVisibility(hideBandboxWhenAnyItemSelected);
        handleBandboxFilerText(hideBandboxWhenAnyItemSelected);
    }


    public void onRemoveSelectedObject(final T obj, final boolean hideBandboxWhenAnyItemSelected)
    {
        selectedElementsListModel.remove(obj);
        decideOnBandboxVisibility(hideBandboxWhenAnyItemSelected);
        handleBandboxFilerText(hideBandboxWhenAnyItemSelected);
    }


    protected void addCreateNewReferenceListitem()
    {
        if(referenceEditor.allowNestedObjectCreation())
        {
            final NestedObjectCreator createNewReferenceListitem = new NestedObjectCreator();
            availableElementsListModel.add((T)createNewReferenceListitem);
        }
    }


    protected void decideOnBandboxVisibility(final boolean hideBandboxWhenAnyItemSelected)
    {
        if(hideBandboxWhenAnyItemSelected && !selectedElementsListModel.isEmpty())
        {
            getBandbox().setVisible(false);
        }
        else
        {
            getBandbox().setVisible(true);
        }
    }


    protected void handleBandboxFilerText(final boolean clearText)
    {
        if(clearText && StringUtils.isNotEmpty(getBandbox().getValue()))
        {
            getBandbox().setValue(StringUtils.EMPTY);
            referenceEditor.updateReferencesListBoxModel();
            buildPaging();
        }
    }


    protected void renderPartOfPopup(final Bandbox parent, final NestedObjectCreator data,
                    final ReferenceEditorLogic referenceEditorLogic) throws TypeNotFoundException
    {
        Validate.notNull("All arguments are mandatory", parent, data, referenceEditorLogic);
        if(parent.getDropdown() == null)
        {
            return;
        }
        parent.getDropdown().getChildren().clear();
        final Div container = new Div();
        parent.getDropdown().appendChild(container);
        UITools.addSClass(container, CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER);
        final String typeCode = referenceEditorLogic.getTypeCode();
        final TypeSelectorTreeModel model = createTypeSelectorTreeModel(typeCode);
        expandFirstNonAbstractSubtype(model, typeCode);
        final Tree typeSelector = createTypeSelectorTree(model, referenceEditorLogic, tCode -> {
            data.setUserChosenType(tCode);
            parent.setText(referenceEditor.getStringRepresentationOfObject((T)data));
            referenceEditor.createNewReference(tCode);
        });
        container.appendChild(typeSelector);
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#renderNestedObjectCreator(Listitem, NestedObjectCreator, Listcell, Listbox, ReferenceEditorLogic)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderNestedObjectCreator(final Listitem item, final NestedObjectCreator data, final Listcell cell,
                    final Listbox parentListBox, final ReferenceEditorLogic referenceEditorLogic) throws TypeNotFoundException
    {//deprecated
    }


    public TypeSelectorTreeModel createTypeSelectorTreeModel(final String typeCode) throws TypeNotFoundException
    {
        return new TypeSelectorTreeModel(getTypeFacade().load(typeCode), getTypeFacade(), getPermissionFacade());
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#createTypeSelectorOpenerButton(ReferenceEditorLogic, Div, TypeSelectorTreeModel)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected Button createTypeSelectorOpenerButton(final ReferenceEditorLogic referenceEditorLogic, final Div container,
                    final TypeSelectorTreeModel model)
    {
        return null;
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#openTypeSelectorContainer(ReferenceEditorLogic, Div, TypeSelectorTreeModel, Button)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void openTypeSelectorContainer(final ReferenceEditorLogic referenceEditorLogic, final Div container,
                    final TypeSelectorTreeModel model, final Button btn)
    {//deprecated
    }


    /**
     * @deprecated since 6.7, method is not used any more, use
     *             {@link ReferenceEditorListItemRendererFactory#closeTypeSelectorContainer(Div, Button)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void closeTypeSelectorContainer(final Div container, final Button btn)
    {//deprecated
    }


    public Tree createTypeSelectorTree(final TypeSelectorTreeModel model, final ReferenceEditorLogic referenceEditorLogic,
                    final Consumer<String> itemCreationConsumer)
    {
        final Tree typeSelector = new Tree();
        UITools.addSClass(typeSelector, CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER_TREE);
        typeSelector.setModel(model);
        final EditorContext context = referenceEditorLogic instanceof AbstractReferenceEditor
                        ? ((AbstractReferenceEditor)referenceEditorLogic).getEditorContext()
                        : null;
        typeSelector.setItemRenderer(createTypeSelectorItemRenderer(context));
        typeSelector.addEventListener(Events.ON_OK,
                        createTypeSelectorListener(referenceEditorLogic, itemCreationConsumer, typeSelector));
        typeSelector.addEventListener(Events.ON_CLICK,
                        createTypeSelectorListener(referenceEditorLogic, itemCreationConsumer, typeSelector));
        typeSelector.setWidgetOverride("_doKeyDown", "function(event){" + //
                        "var keyCode = event.keyCode; " + //
                        "if (keyCode === 38 && this.getSelectedIndex() === 0) { " + //
                        "CockpitNG.sendEvent('#'+event.currentTarget.uuid,'" + ON_UP_ARROW + "');" + //
                        "} " + //
                        " else" + //
                        "{" + //
                        "this.$_doKeyDown(event); " + //
                        "}" + //
                        "}"); //
        typeSelector.addEventListener(ON_UP_ARROW, event -> jumpToListboxForFirstElement(typeSelector));
        return typeSelector;
    }


    protected void jumpToListboxForFirstElement(final Tree typeSelector)
    {
        Validate.notNull("Type selector may not be null", typeSelector);
        final Set<Treeitem> selectedItems = typeSelector.getSelectedItems();
        selectedItems.stream().findFirst().ifPresent(treeitem -> {
            final Iterator<Treeitem> treeitemIterator = typeSelector.getItems().iterator();
            if(treeitemIterator.hasNext() && treeitemIterator.next().equals(treeitem))
            {
                typeSelector.clearSelection();
                getListbox().setSelectedIndex(getListbox().getItemCount() - 1);
                getListbox().focus();
            }
        });
    }


    protected EventListener<Event> createTypeSelectorListener(final ReferenceEditorLogic referenceEditorLogic,
                    final Consumer<String> itemCreationConsumer, final Tree typeSelector)
    {
        return event -> {
            final Set<Treeitem> selectedItems = typeSelector.getSelectedItems();
            if(selectedItems.size() == 1)
            {
                final Treeitem treeitem = selectedItems.iterator().next();
                final DataType type = treeitem.getValue();
                if(type.isAbstract())
                {
                    changeSelectedItemState(typeSelector);
                }
                else
                {
                    final String typeCode = type.getCode();
                    itemCreationConsumer.accept(typeCode);
                }
                referenceEditorLogic.preserveFocus();
            }
        };
    }


    @InextensibleMethod
    private void changeSelectedItemState(final Tree typeSelector)
    {
        if(typeSelector == null)
        {
            return;
        }
        final Treeitem selectedItem = typeSelector.getSelectedItem();
        if(selectedItem != null)
        {
            selectedItem.setOpen(!selectedItem.isOpen());
            selectedItem.setSelected(false);
        }
    }


    protected TreeitemRenderer createTypeSelectorItemRenderer(final EditorContext context)
    {
        return getListItemRendererFactory().createTypeSelectorItemRenderer(context);
    }


    public void expandFirstNonAbstractSubtype(final AbstractTreeModel treeModel, final String currentTypeCode)
    {
        final DataType currentType = loadDataType(currentTypeCode);
        if(currentType != null && currentType.isAbstract())
        {
            treeModel.addOpenObject(currentType);
            final Optional<String> anyNonAbstractSubtype = currentType.getSubtypes().stream().filter(subtypeCode -> {
                final DataType dataType = loadDataType(subtypeCode);
                return dataType != null && !dataType.isAbstract();
            }).findAny();
            if(!anyNonAbstractSubtype.isPresent())
            {
                currentType.getSubtypes().forEach(subtypeCode -> expandFirstNonAbstractSubtype(treeModel, subtypeCode));
            }
        }
    }


    public DataType loadDataType(final String typeCode)
    {
        DataType ret = null;
        try
        {
            ret = getTypeFacade().load(typeCode);
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Cannot find type %s", typeCode), e);
            }
        }
        return ret;
    }


    public boolean contains(final T obj)
    {
        return selectedElementsListModel.contains(obj);
    }


    public void clearSelection()
    {
        selectedElementsListModel.clear();
    }


    public boolean isOrdered()
    {
        return ordered;
    }


    public void setOrdered(final boolean ordered)
    {
        this.ordered = ordered;
    }


    public String getPlaceholderKey()
    {
        return placeholderKey;
    }


    public void setPlaceholderKey(final String placeholderKey)
    {
        this.placeholderKey = placeholderKey;
    }


    public Listbox getCurrentlySelectedList()
    {
        return currentlySelectedList;
    }


    public ListModelList<T> getSelectedElementsListModel()
    {
        return selectedElementsListModel;
    }


    protected PermissionFacade getPermissionFacade()
    {
        if(permissionFacade == null)
        {
            permissionFacade = (PermissionFacade)SpringUtil.getApplicationContext().getBean("permissionFacade");
        }
        return permissionFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        if(typeFacade == null)
        {
            typeFacade = (TypeFacade)SpringUtil.getApplicationContext().getBean("typeFacade");
        }
        return typeFacade;
    }


    protected LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = (LabelService)SpringUtil.getApplicationContext().getBean("labelService");
        }
        return labelService;
    }


    public Bandbox getBandbox()
    {
        if(bandbox == null)
        {
            bandbox = new Bandbox();
        }
        return bandbox;
    }


    protected Bandbox getCreateOnlyBandbox()
    {
        if(createOnlyBandbox == null)
        {
            createOnlyBandbox = new Bandbox();
        }
        return createOnlyBandbox;
    }


    protected A getDropButton()
    {
        if(dropButton == null)
        {
            this.dropButton = new A();
        }
        return dropButton;
    }


    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected A getDropButon()
    {
        return getDropButton();
    }


    public ListModelList<T> getAvailableElementsListModel()
    {
        return availableElementsListModel;
    }


    public ReferenceEditorLogic<T> getReferenceEditor()
    {
        return referenceEditor;
    }


    public Base getConfiguration()
    {
        return configuration;
    }


    public boolean isReadOnly()
    {
        return readOnly;
    }


    public Paging getPaging()
    {
        return paging;
    }


    public int getRenderOnDemandSize()
    {
        return renderOnDemandSize;
    }


    public void setRenderOnDemandSize(final int renderOnDemandSize)
    {
        this.renderOnDemandSize = renderOnDemandSize;
    }


    public int getSelectedItemsMaxSize()
    {
        return selectedItemsMaxSize;
    }


    public void setSelectedItemsMaxSize(final int selectedItemsMaxSize)
    {
        this.selectedItemsMaxSize = selectedItemsMaxSize;
    }


    protected ReferenceEditorListItemRendererFactory<T> getListItemRendererFactory()
    {
        return referenceEditorListItemRendererFactory;
    }
}
