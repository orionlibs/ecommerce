/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.commonreferenceeditor;

import static com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout.CSS_YE_BUTTON_ACTIVE;
import static com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout.CSS_YE_CREATE_TYPE_SELECTOR_BUTTON;
import static com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout.CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER;
import static com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout.CSS_YE_DEFAULT_REFERENCE_EDITOR_PREVIEW_POPUP_IMAGE;
import static com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout.CSS_YE_EDITOR_DISABLED;
import static com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout.CSS_YE_REMOVE_ENABLED;

import com.hybris.cockpitng.common.model.TypeSelectorTreeModel;
import com.hybris.cockpitng.common.renderer.TypeSelectorTreeItemRenderer;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

public class ReferenceEditorListItemRendererFactory<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(ReferenceEditorListItemRendererFactory.class);
    private static final String ON_RIGHT_ARROW = "onRightArrow";
    private static final String ON_LEFT_ARROW = "onLeftArrow";
    private static final String YTESTID_CREATE_NEW_ENTRY = "create-reference-btn";
    private static final String CSS_CREATE_NEW_REFERENCE = "ye-create-new-reference";
    private static final String ON_OPEN_CONTAINER_EVENT = "onOpenContainer";
    private static final String ON_CLOSE_CONTAINER_EVENT = "onCloseContainer";
    private static final String AUTOCORRECTION_PREFIX_KEY = "autocorrection.prefix";
    private static final String AUTOCORRECTION_SUFFIX_KEY = "autocorrection.suffix";
    private static final String CSS_AUTOCORRECTION_REFERENCE = "ye-default-reference-editor-bandbox-autocorrection-reference";
    private static final String CSS_AUTOCORRECTION_PREFIX = "ye-default-reference-editor-bandbox-autocorrection-prefix";
    private static final String CSS_AUTOCORRECTION_VALUE = "ye-default-reference-editor-bandbox-autocorrection-value";
    private static final String CSS_AUTOCORRECTION_SUFFIX = "ye-default-reference-editor-bandbox-autocorrection-suffix";
    private static final String CSS_LISTBOX_ITEM_NOACCESS = "ye-default-reference-editor-bandbox-listcell-noaccess";
    private static final String CSS_REFERENCE_EDITOR_REMOVE_BTN = "ye-default-reference-editor-remove-button ye-delete-btn";
    private static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_LABEL = "ye-default-reference-editor-selected-item-label";
    private static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_CONTAINER = "ye-default-reference-editor-selected-item-container";
    /**
     * @deprecated since 2005, method is not used any more, use {@link ReferenceEditorDndHandler} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected static final String CUSTOM_DRAGGED_ITEM_INDEX_DATA = "sap.custom.renderOnDemandDragData";
    private static final String YTESTID_REFERENCE_ENTRY = "reference-editor-reference";
    private static final String YTESTID_REMOVE_BUTTON = "reference-editor-remove-button";
    private static final int ENTER_KEY_CODE = 13;
    final ReferenceEditorLayout<T> referenceEditorLayout;
    private PermissionFacade permissionFacade;
    private LabelService labelService;
    private ReferenceEditorRenderProhibitingPredicate referenceEditorRenderProhibitingPredicate;
    private ReferenceEditorDndHandler<T> referenceEditorDndHandler;


    public ReferenceEditorListItemRendererFactory(final ReferenceEditorLayout<T> referenceEditorLayout)
    {
        this.referenceEditorLayout = referenceEditorLayout;
    }


    public ListitemRenderer<T> createListItemRenderer()
    {
        return (item, data, index) -> {
            final Listcell cell = new Listcell();
            final boolean canClick;
            if(data instanceof NestedObjectCreator)
            {
                renderNestedObject(item, (NestedObjectCreator)data, cell);
                final String typeCode = Optional.ofNullable(((NestedObjectCreator)data).getUserChosenType())
                                .orElseGet(referenceEditorLayout.getReferenceEditor()::getTypeCode);
                canClick = getPermissionFacade().canCreateTypeInstance(typeCode);
            }
            else if(data instanceof AutoCorrectionInfo)
            {
                renderAutoCorrectionRow(item, (AutoCorrectionInfo)data, cell);
                item.appendChild(cell);
                canClick = true;
            }
            else
            {
                renderPreview(item, data, cell);
                canClick = getPermissionFacade().canReadInstance(data);
            }
            if(canClick)
            {
                item.addEventListener(Events.ON_CLICK, event -> listBoxItemChosen(data, event));
                item.addEventListener(Events.ON_OK, event -> listBoxItemChosen(data, event));
            }
            else
            {
                UITools.addSClass(item, CSS_LISTBOX_ITEM_NOACCESS);
            }
        };
    }


    protected void renderNestedObject(final Listitem item, final NestedObjectCreator data, final Listcell cell)
                    throws TypeNotFoundException
    {
        renderNestedObjectCreator(item, data, cell, referenceEditorLayout.getCurrentlySelectedList(),
                        referenceEditorLayout.getReferenceEditor());
        item.appendChild(cell);
        item.addEventListener(ON_RIGHT_ARROW, this::openTypeSelectorTree);
        item.addEventListener(ON_LEFT_ARROW, this::closeTypeSelectorTree);
    }


    protected void renderPreview(final Listitem item, final T data, final Listcell cell)
    {
        final ObjectPreview preview = getObjectPreviewService().getPreview(data, referenceEditorLayout.getConfiguration());
        if(preview != null)
        {
            final Image image = new Image(preview.getUrl());
            cell.appendChild(image);
            preparePreviewPopup(cell, preview, image);
        }
        cell.appendChild(new Label(referenceEditorLayout.getReferenceEditor().getStringRepresentationOfObject(data)));
        item.appendChild(cell);
        item.addEventListener(ON_RIGHT_ARROW, event -> nextPage());
        item.addEventListener(ON_LEFT_ARROW, event -> previousPage());
    }


    public ListitemRenderer<T> createSelectedItemsListItemRenderer()
    {
        return (item, data, index) -> {
            final ReferenceEditorLogic<T> referenceEditor = referenceEditorLayout.getReferenceEditor();
            final ListModelList<T> selectedElementsListModel = referenceEditorLayout.getSelectedElementsListModel();
            getReferenceEditorDndHandler().enableDragAndDrop(item, referenceEditorLayout);
            item.setTooltiptext(referenceEditor.getStringRepresentationOfObject(data));
            final Div layout = new Div();
            UITools.addSClass(layout, CSS_REFERENCE_EDITOR_SELECTED_ITEM_CONTAINER);
            final Listcell cell = createCell(data, referenceEditor, selectedElementsListModel);
            createPreview(data, referenceEditorLayout.getConfiguration(), layout, cell);
            final Label label = createLabel(data, referenceEditor);
            layout.appendChild(label);
            if(!referenceEditor.isDisableRemoveReference() && referenceEditor.isEditable()
                            && getPermissionFacade().canReadInstance(data))
            {
                final Div removeImage = createRemoveImage(data, referenceEditor);
                layout.appendChild(removeImage);
                UITools.addSClass(layout, CSS_YE_REMOVE_ENABLED);
            }
            cell.appendChild(layout);
            cell.setParent(item);
        };
    }


    protected Listcell createCell(final T data, final ReferenceEditorLogic<T> referenceEditor,
                    final ListModelList<T> selectedElementsListModel)
    {
        final Listcell cell = new Listcell();
        if(!referenceEditor.isDisableDisplayingDetails())
        {
            cell.addEventListener(Events.ON_DOUBLE_CLICK, event -> referenceEditor
                            .triggerReferenceSelected(new TypeAwareSelectionContext<T>(data, selectedElementsListModel.getInnerList())));
        }
        return cell;
    }


    protected void createPreview(final T data, final Base configuration, final Div layout, final Listcell cell)
    {
        final ObjectPreview preview = getObjectPreviewService().getPreview(data, configuration);
        if(preview != null)
        {
            final Image image = new Image(preview.getUrl());
            preparePreviewPopup(cell, preview, image);
            layout.appendChild(image);
        }
    }


    protected Div createRemoveImage(final T data, final ReferenceEditorLogic<T> referenceEditor)
    {
        final Div removeImage = new Div();
        UITools.addSClass(removeImage, CSS_REFERENCE_EDITOR_REMOVE_BTN);
        YTestTools.modifyYTestId(removeImage, YTESTID_REMOVE_BUTTON);
        removeImage.addEventListener(Events.ON_CLICK, event -> referenceEditor.removeSelectedObject(data));
        return removeImage;
    }


    private Label createLabel(final T data, final ReferenceEditorLogic<T> referenceEditor)
    {
        final Label label = new Label(referenceEditor.getStringRepresentationOfObject(data));
        UITools.addSClass(label, CSS_REFERENCE_EDITOR_SELECTED_ITEM_LABEL);
        YTestTools.modifyYTestId(label, YTESTID_REFERENCE_ENTRY);
        label.setMultiline(true);
        UITools.modifySClass(label, CSS_YE_EDITOR_DISABLED, referenceEditorLayout.isReadOnly());
        return label;
    }


    /**
     * @deprecated since 2005, method is not used any more, use {@link ReferenceEditorDndHandler} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void enableDragAndDrop(final Listitem item, final ReferenceEditorLogic<T> referenceEditor,
                    final Listbox currentlySelectedList, final ListModelList<T> selectedElementsListModel)
    {
        //deprecated
    }


    /**
     * @deprecated since 2005, method is not used any more, use {@link ReferenceEditorDndHandler} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void storeDraggedItemIndex(final Listitem draggedItem)
    {
        //deprecated
    }


    /**
     * @deprecated since 2005, method is not used any more, use {@link ReferenceEditorDndHandler} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected Optional<Integer> popDraggedItemIndex(final Desktop desktop)
    {
        return Optional.empty();
    }


    public TreeitemRenderer createTypeSelectorItemRenderer(final EditorContext context)
    {
        return new TypeSelectorTreeItemRenderer(getLabelService(), context)
        {
            @Override
            protected boolean isDisabled(final DataType type)
            {
                return !getPermissionFacade().canCreateTypeInstance(type.getCode());
            }


            @Override
            public void render(final Treeitem item, final DataType type, final int index)
            {
                if(getReferenceEditorRenderProhibitingPredicate().isProhibited(type))
                {
                    return;
                }
                super.render(item, type, index);
                if(type.isAbstract())
                {
                    UITools.addSClass(item.getTreerow(), "ye-create-type-selector-abstracttype");
                }
            }
        };
    }


    protected void renderNestedObjectCreator(final Listitem item, final NestedObjectCreator data, final Listcell cell,
                    final Listbox parentListBox, final ReferenceEditorLogic referenceEditorLogic) throws TypeNotFoundException
    {
        Validate.notNull("All arguments are mandatory", item, data, cell, parentListBox, referenceEditorLogic);
        YTestTools.modifyYTestId(item, YTESTID_CREATE_NEW_ENTRY);
        UITools.addSClass(item, CSS_CREATE_NEW_REFERENCE);
        final Label label = new Label(referenceEditorLogic.getStringRepresentationOfObject(data));
        cell.appendChild(label);
        final Div container = new Div();
        UITools.addSClass(container, CSS_YE_CREATE_TYPE_SELECTOR_CONTAINER);
        final TypeSelectorTreeModel model = referenceEditorLayout.createTypeSelectorTreeModel(referenceEditorLogic.getTypeCode());
        container.setVisible(false);
        final Tree typeSelector = referenceEditorLayout.createTypeSelectorTree(model, referenceEditorLogic, typeCode -> {
            parentListBox.clearSelection();
            closeBandbox();
            label.setValue(data.getLabel(typeCode));
            data.setUserChosenType(typeCode);
            referenceEditorLogic.createNewReference(typeCode);
        });
        if(typeSelector != null)
        {
            final Button btn = createTypeSelectorOpenerButton(referenceEditorLogic, container, model);
            cell.appendChild(btn);
            container.appendChild(typeSelector);
            cell.appendChild(container);
        }
    }


    protected void openTypeSelectorTree(final Event event)
    {
        final Component typeSelectorBtn = getTypeSelectorButton(event);
        executeZkEvent(typeSelectorBtn, new Event(ON_OPEN_CONTAINER_EVENT));
    }


    protected void closeTypeSelectorTree(final Event event)
    {
        final Component typeSelectorBtn = getTypeSelectorButton(event);
        executeZkEvent(typeSelectorBtn, new Event(ON_CLOSE_CONTAINER_EVENT));
    }


    protected void renderAutoCorrectionRow(final Listitem item, final AutoCorrectionInfo autoCorrectionInfo, final Listcell cell)
    {
        Validate.notNull("All arguments are mandatory", item, autoCorrectionInfo, cell);
        UITools.addSClass(item, CSS_AUTOCORRECTION_REFERENCE);
        final Label autoCorrectionPrefix = new Label(Labels.getLabel(AUTOCORRECTION_PREFIX_KEY));
        UITools.addSClass(autoCorrectionPrefix, CSS_AUTOCORRECTION_PREFIX);
        cell.appendChild(autoCorrectionPrefix);
        final Label autoCorrectedValue = new Label(autoCorrectionInfo.getValue());
        UITools.addSClass(autoCorrectedValue, CSS_AUTOCORRECTION_VALUE);
        cell.appendChild(autoCorrectedValue);
        final Label autoCorrectionSuffix = new Label(Labels.getLabel(AUTOCORRECTION_SUFFIX_KEY));
        UITools.addSClass(autoCorrectionSuffix, CSS_AUTOCORRECTION_SUFFIX);
        cell.appendChild(autoCorrectionSuffix);
    }


    protected void nextPage()
    {
        final Paging paging = referenceEditorLayout.getPaging();
        Validate.notNull("Paging must be initialized", paging);
        if(paging.getActivePage() + 1 < paging.getPageCount())
        {
            final int pageToChoose = paging.getActivePage() + 1;
            paging.setActivePage(pageToChoose);
            referenceEditorLayout.changePage(pageToChoose);
        }
    }


    protected void previousPage()
    {
        final Paging paging = referenceEditorLayout.getPaging();
        Validate.notNull("Paging must be initialized", paging);
        if(paging.getActivePage() > 0)
        {
            final int pageToChoose = paging.getActivePage() - 1;
            paging.setActivePage(pageToChoose);
            referenceEditorLayout.changePage(pageToChoose);
        }
    }


    protected void listBoxItemChosen(final T selected, final Event event)
    {
        if(selected instanceof AutoCorrectionInfo)
        {
            showAutoCorrectedSearchResults((AutoCorrectionInfo)selected);
            return;
        }
        if(selected instanceof NestedObjectCreator)
        {
            final String typeCode = StringUtils.defaultIfBlank(((NestedObjectCreator)selected).getUserChosenType(),
                            referenceEditorLayout.getReferenceEditor().getTypeCode());
            final DataType dataType = referenceEditorLayout.loadDataType(typeCode);
            if(dataType != null)
            {
                if(dataType.isAbstract())
                {
                    final Component typeSelectorBtn = getTypeSelectorButton(event);
                    Events.sendEvent(typeSelectorBtn, new Event(Events.ON_CLICK));
                }
                else
                {
                    closeBandbox();
                    referenceEditorLayout.getReferenceEditor().createNewReference(typeCode);
                }
            }
        }
        else
        {
            closeBandbox();
            referenceEditorLayout.getReferenceEditor().addSelectedObject(selected);
            sendEnterPressedEventIfNeeded(event);
        }
        referenceEditorLayout.getReferenceEditor().preserveFocus();
        referenceEditorLayout.getBandbox().setText(null);
    }


    protected void sendEnterPressedEventIfNeeded(final Event event)
    {
        if(event instanceof KeyEvent && ((KeyEvent)event).getKeyCode() == ENTER_KEY_CODE
                        && (referenceEditorLayout.getReferenceEditor() instanceof AbstractReferenceEditor))
        {
            ((AbstractReferenceEditor)referenceEditorLayout.getReferenceEditor()).getEditorListener()
                            .onEditorEvent(EditorListener.ENTER_PRESSED);
        }
    }


    protected Button createTypeSelectorOpenerButton(final ReferenceEditorLogic referenceEditorLogic, final Div container,
                    final TypeSelectorTreeModel model)
    {
        final Button btn = new Button();
        UITools.addSClass(btn, CSS_YE_CREATE_TYPE_SELECTOR_BUTTON);
        btn.addEventListener(Events.ON_CLICK, e -> {
            if(!container.isVisible())
            {
                openTypeSelectorContainer(referenceEditorLogic, container, model, btn);
            }
            else
            {
                closeTypeSelectorContainer(container, btn);
            }
        });
        btn.addEventListener(ON_OPEN_CONTAINER_EVENT, e -> openTypeSelectorContainer(referenceEditorLogic, container, model, btn));
        btn.addEventListener(ON_CLOSE_CONTAINER_EVENT, e -> closeTypeSelectorContainer(container, btn));
        return btn;
    }


    protected void showAutoCorrectedSearchResults(final AutoCorrectionInfo selected)
    {
        Validate.notNull("Auto correction info may not be null", selected);
        final String autoCorrectionValue = selected.getValue();
        referenceEditorLayout.getBandbox().setValue(autoCorrectionValue);
        referenceEditorLayout.getReferenceEditor().updateReferencesListBoxModel(autoCorrectionValue);
        referenceEditorLayout.buildPaging();
        referenceEditorLayout.getBandbox().focus();
    }


    protected Component getTypeSelectorButton(final Event event)
    {
        Validate.notNull("Event may not be null", event);
        return event.getTarget().query("." + CSS_YE_CREATE_TYPE_SELECTOR_BUTTON);
    }


    protected void closeBandbox()
    {
        referenceEditorLayout.getListbox().clearSelection();
        referenceEditorLayout.getBandbox().setOpen(false);
        referenceEditorLayout.getBandbox().invalidate();
        executeZkEvent(referenceEditorLayout.getBandbox(),
                        new OpenEvent(Events.ON_OPEN, referenceEditorLayout.getBandbox(), false));
    }


    protected void openTypeSelectorContainer(final ReferenceEditorLogic referenceEditorLogic, final Div container,
                    final TypeSelectorTreeModel model, final Button btn)
    {
        Validate.notNull("All arguments are mandatory", referenceEditorLogic, container, model, btn);
        referenceEditorLayout.expandFirstNonAbstractSubtype(model, referenceEditorLogic.getTypeCode());
        container.setVisible(true);
        UITools.addSClass(btn, CSS_YE_BUTTON_ACTIVE);
        referenceEditorLayout.getListbox().focus();
    }


    protected void closeTypeSelectorContainer(final Div container, final Button btn)
    {
        Validate.notNull("All arguments are mandatory", container, btn);
        container.setVisible(false);
        UITools.removeSClass(btn, CSS_YE_BUTTON_ACTIVE);
        referenceEditorLayout.getListbox().focus();
    }


    /**
     * @deprecated since 2005, method is not used any more, use {@link ReferenceEditorDndHandler} instead.
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void dragAndDropItems(final Listitem dragged, final Listitem dropped)
    {
        // deprecated
    }


    protected void preparePreviewPopup(final Component parent, final ObjectPreview preview, final Image target)
    {
        Validate.notNull("All arguments are mandatory", parent, preview, target);
        if(preview.isFallback())
        {
            return;
        }
        final Popup zoomPopup = new Popup();
        final Image popupImage = new Image(preview.getUrl());
        UITools.addSClass(popupImage, CSS_YE_DEFAULT_REFERENCE_EDITOR_PREVIEW_POPUP_IMAGE);
        zoomPopup.appendChild(popupImage);
        parent.appendChild(zoomPopup);
        target.addEventListener(Events.ON_MOUSE_OVER, event -> zoomPopup.open(target, "before_start"));
        target.addEventListener(Events.ON_MOUSE_OUT, event -> zoomPopup.close());
    }


    protected ObjectPreviewService getObjectPreviewService()
    {
        return (ObjectPreviewService)SpringUtil.getBean("objectPreviewService");
    }


    protected PermissionFacade getPermissionFacade()
    {
        if(permissionFacade == null)
        {
            permissionFacade = (PermissionFacade)SpringUtil.getApplicationContext().getBean("permissionFacade");
        }
        return permissionFacade;
    }


    protected LabelService getLabelService()
    {
        if(labelService == null)
        {
            labelService = (LabelService)SpringUtil.getApplicationContext().getBean("labelService");
        }
        return labelService;
    }


    protected ReferenceEditorRenderProhibitingPredicate getReferenceEditorRenderProhibitingPredicate()
    {
        if(referenceEditorRenderProhibitingPredicate == null)
        {
            referenceEditorRenderProhibitingPredicate = (ReferenceEditorRenderProhibitingPredicate)SpringUtil
                            .getApplicationContext().getBean("referenceEditorRenderProhibitingPredicate");
        }
        return referenceEditorRenderProhibitingPredicate;
    }


    public ReferenceEditorDndHandler<T> getReferenceEditorDndHandler()
    {
        if(referenceEditorDndHandler == null)
        {
            referenceEditorDndHandler = (ReferenceEditorDndHandler)SpringUtil.getBean("referenceEditorDndHandler");
        }
        return referenceEditorDndHandler;
    }


    void executeZkEvent(final Component component, final Event event)
    {
        Events.sendEvent(component, event);
    }
}
