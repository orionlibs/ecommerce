package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.advancedsearch.impl.AdvancedSearchView;
import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.ListView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.misc.ComponentController;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelector;
import de.hybris.platform.cockpit.model.referenceeditor.AbstractReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceSelectorModel;
import de.hybris.platform.cockpit.model.referenceeditor.SelectorModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class ReferenceSelector extends AbstractReferenceSelector implements DesktopRemovalAwareComponent
{
    private static final String ON_CONFIRM_CLOSE_LATER = "onConfirmCloseLater";
    private static final Logger log = LoggerFactory.getLogger(ReferenceSelector.class);
    protected static final String CENTER = "center";
    protected static final String _100PERCENT = "100%";
    protected static final String _99PERCENT = "99%";
    protected static final String LEFT_BRACKET = "[ ";
    protected static final String RIGHT_BRACKET = " ]";
    protected static final String ELLIPSIS = "...";
    protected static final String VERTICAL = "vertical";
    protected static final String COMMA = ", ";
    protected static final int MAX_ROWS = 5;
    protected static final String REFERENCE_EDITOR_BTN_SCLASS = "referenceEditorButton";
    protected static final String LIST_VIEW_SELECTOR_SCLASS = "listViewInSelectorContext";
    protected static final String MODAL_DIALOG_SCLASS = "referenceModalDialogSclass";
    protected static final int ENTER_CHAR = 13;
    protected static final int ESC_CHAR = 27;
    protected static final String CLOSE_BTN_IMG = "/cockpit/images/close_btn.png";
    protected static final String EMPTY_MESSAGE = "general.nothingtodisplay";
    protected static final String WIDTH = "width";
    protected static final String HEIGHT = "height";
    protected static final String ADV_QUERY_BTN_IMG = "/cockpit/images/open_advanced_editor.gif";
    private transient ReferenceSelectorModalDialog refSelectorModalDialog;
    protected transient Bandbox component;
    protected transient Table labelContainer;
    protected transient Bandpopup popupContainer;
    protected transient Vbox container;
    protected transient Listbox notConfirmedItems;
    protected transient Listbox autoCompleteList;
    protected transient Listbox temporaryItemsList;
    protected transient Bandbox autoCompleteInputField;
    protected transient Bandpopup autoCompletePopup;
    protected transient Toolbarbutton addButton;
    protected transient UIListView listView = null;
    protected transient UIAdvancedSearchView advancedSearchComponent;
    protected transient ComponentController listViewController;
    protected transient ComponentController advancedSearchController;
    protected static final String ON_CLICK_EVENT = "onclick";
    protected static final String REFERENECE_SELECTOR_SCLASS = "referenceSelector";
    protected static final String REFERENECE_SELECTOR_CMP_SCLASS = "referenceSelectorCmp";
    protected static final String REFERENECE_SELECTOR_TABLE_LABEL_SCLASS = "referenceSelectorLabel";
    protected static final String REFERENECE_SELECTOR_TABLE_LABEL_FIRSTCELL = "firstCell";
    protected static final String REFERENECE_SELECTOR_TABLE_LABEL_SECONDCELL = "secondCell";
    protected static final String REFERENECE_SELECTOR_TABLE_LABEL_INFOLABEL = "itemsInfoLabel";
    protected static final String REFERENECE_SELECTOR_CMP_POPUP_SCLASS = "componentPopup";
    protected static final String REFERENECE_SELECTOR_NOT_CONFIRMED_SCLASS = "referenceSelectorSelectedItems";
    protected static final String REFERENECE_SELECTOR_LISTBOX_ODD_ROWS = "oddRowRowSclass";
    protected static final String REFERENECE_SELECTOR_NOT_CONFIRMED_LABEL = "selectedItemLabel";
    protected static final String REFERENECE_SELECTOR_NOT_CONFIRMED_CELLLABEL = "selectedCellItem";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE = "autoCompleteCmp";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE_POPUP = "autoCompletePopup";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE_LIST = "autoCompleteList";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE_CELLITEM = "autoCompleteCell";
    protected static final String REFERENECE_SELECTOR_TEMPORARY_LIST = "referenceSelectorSelectedItems";
    protected static final String REFERENECE_SELECTOR_TEMPORARY_LIST_CELLITEM = "selectedCellItem";
    private Map<String, ? extends Object> parameters = new HashMap<>();
    protected ReferenceSelectorModel model;
    protected static final boolean SHOW_TEMPORARY_SELECTED = false;
    protected boolean initialized = false;
    protected boolean disabled = false;
    protected Boolean allowcreate = Boolean.FALSE;
    private CreateContext createContext = null;
    private Integer defaultAutocompleteTimeout = null;
    private final EventListener confirmCloseListener = (EventListener)new Object(this);


    public void setFocus(boolean focus)
    {
        fireSelectorNormalMode();
        if(StringUtils.isNotBlank(this.autoCompleteInputField.getText()))
        {
            Events.echoEvent("onUser", (Component)this.autoCompleteInputField, this.autoCompleteInputField.getText().trim());
        }
    }


    public Boolean isAllowcreate()
    {
        return this.allowcreate;
    }


    public void setAllowcreate(Boolean allowcreate)
    {
        this.allowcreate = allowcreate;
    }


    public void setCreateContext(CreateContext createContext)
    {
        this.createContext = createContext;
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            updateNotConfirmedItemList();
            updateAutoCompleteItemList();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    protected void updateAutoCompleteItemList()
    {
        this.autoCompleteList.setModel((ListModel)new SimpleListModel(this.model.getAutoCompleteResult()));
    }


    protected void updateNotConfirmedItemList()
    {
        this.notConfirmedItems.setModel((ListModel)new SimpleListModel(this.model.getNotConfirmedItems()));
    }


    protected void updateTemporaryItemList()
    {
        this.temporaryItemsList.setModel((ListModel)new SimpleListModel(this.model.getTemporaryItems()));
    }


    protected void showReferenceSelectorModalDialog()
    {
        try
        {
            this.refSelectorModalDialog = new ReferenceSelectorModalDialog(this);
            this.refSelectorModalDialog.setVisible(Boolean.FALSE.booleanValue());
            getRoot().appendChild((Component)this.refSelectorModalDialog);
            this.refSelectorModalDialog.doHighlighted();
        }
        catch(InterruptedException e)
        {
            log.error("Some internal errors occure while creating reference selector modal dialog.");
        }
    }


    public void showComponentPopup()
    {
        this.labelContainer.setVisible(Boolean.FALSE.booleanValue());
        this.component.setReadonly(Boolean.FALSE.booleanValue());
        this.component.setText(computeLabel());
        this.component.setReadonly(Boolean.TRUE.booleanValue());
        this.component.setVisible(Boolean.TRUE.booleanValue());
        this.component.open();
        focusElement((HtmlBasedComponent)this.autoCompleteInputField);
    }


    public void closeComponentPopup()
    {
        clearAutoCompleteInputText();
        this.autoCompleteInputField.close();
        this.component.close();
        this.component.setVisible(Boolean.FALSE.booleanValue());
        this.labelContainer.setVisible(Boolean.TRUE.booleanValue());
        Events.postEvent("onClose", (Component)this.component, null);
    }


    public void closeReferenceSelectorModalDialog()
    {
        if(this.refSelectorModalDialog != null)
        {
            this.refSelectorModalDialog.detach();
            this.refSelectorModalDialog = null;
        }
    }


    public void showAutoCompletePopup()
    {
        this.autoCompleteInputField.open();
    }


    public void updateTemporaryItems()
    {
        if(this.initialized)
        {
            if(getModel().getTemporaryItems().size() > 0)
            {
                this.addButton.setVisible(Boolean.TRUE.booleanValue());
            }
        }
    }


    public void updateMode()
    {
        if(this.initialized && !this.disabled)
        {
            SelectorModel.Mode mode = getModel().getMode();
            if(mode.equals(SelectorModel.Mode.VIEW_MODE))
            {
                closeComponentPopup();
            }
            else if(mode.equals(SelectorModel.Mode.NORMAL_MODE))
            {
                closeReferenceSelectorModalDialog();
                showComponentPopup();
                updateNotConfirmedItemList();
                updateAutoCompleteItemList();
            }
            else if(mode.equals(SelectorModel.Mode.ADVANCED_MODE))
            {
                showReferenceSelectorModalDialog();
            }
        }
    }


    public void updateAutoCompleteResult()
    {
        if(this.initialized)
        {
            int autoCompletedSize = getModel().getAutoCompleteResult().size();
            if(autoCompletedSize == 0)
            {
                this.autoCompleteInputField.close();
                this.autoCompleteList.setVisible(Boolean.FALSE.booleanValue());
                return;
            }
            this.autoCompleteList.setVisible(Boolean.TRUE.booleanValue());
            updateAutoCompleteItemList();
            trimToLimit(this.autoCompleteList, 5, autoCompletedSize);
        }
    }


    public void updateSearchResult()
    {
        if(this.initialized)
        {
            this.refSelectorModalDialog.removeChildComponents();
            if(getModel().getSearchResult().isEmpty())
            {
                this.refSelectorModalDialog.addChildComponent((Component)new Label(Labels.getLabel("general.nothingtodisplay")));
            }
            else if(this.listView != null)
            {
                this.refSelectorModalDialog.addChildComponent((Component)this.listView);
                this.refSelectorModalDialog.getPagingToolbar().setVisible(Boolean.TRUE.booleanValue());
                if(UITools.isFromOtherDesktop((Component)this.listView))
                {
                    log.error("No valid list view component available.");
                }
                else
                {
                    this.listView.updateItems();
                }
            }
            this.refSelectorModalDialog.getPagingToolbar().setTotalSize(getModel().getTotalSize());
        }
    }


    protected UIListView loadListView()
    {
        if(getModel() != null)
        {
            MutableTableModel tableModel = getModel().getTableModel();
            if(tableModel != null)
            {
                ((DefaultListModel)tableModel.getListComponentModel().getListModel()).clearAndAddAll(getModel().getSearchResult());
                tableModel.getListComponentModel().setMultiple(getModel().isMultiple());
                tableModel.getListComponentModel().setEditable(false);
                this.listView = (UIListView)new ListView();
                this.listView.setSclass("listViewInSelectorContext");
                this.listView.setModel((TableModel)tableModel);
                this.listView.setHeight("100%");
                this.listView.setWidth("100%");
                if(this.listViewController != null)
                {
                    this.listViewController.unregisterListeners();
                }
                this.listViewController = (ComponentController)new DefaultListViewSelectorController((AbstractReferenceSelectorModel)getModel(), this.listView);
                this.listViewController.initialize();
            }
        }
        return this.listView;
    }


    public boolean initialize()
    {
        addEventListener("onConfirmCloseLater", this.confirmCloseListener);
        this.initialized = false;
        setSclass("referenceSelector");
        Hbox mainHbox = new Hbox();
        mainHbox.setWidths("100%,none");
        Div btnDiv = new Div();
        btnDiv.setSclass("z-combobox");
        btnDiv.setStyle("margin: 0 0 0 0;");
        Image img = new Image();
        img.setSclass("z-combobox-img");
        img.setParent((Component)btnDiv);
        btnDiv.addEventListener("onClick", (EventListener)new Object(this));
        addEventListener("onBlur", (EventListener)new Object(this));
        this.component = new Bandbox();
        this.component.setSclass("referenceSelectorCmp");
        this.component.setVisible(Boolean.FALSE.booleanValue());
        this.component.setWidth("99%");
        this.component.setButtonVisible(Boolean.FALSE.booleanValue());
        this.component.setAction("onclick:comm.sendUser('" + this.component.getId() + "');");
        this.component.addEventListener("onUser", (EventListener)new Object(this));
        this.component.addEventListener("onOpen", (EventListener)new Object(this));
        this.labelContainer = new Table();
        this.labelContainer.setVisible(Boolean.TRUE.booleanValue());
        this.labelContainer.setSclass("referenceSelectorLabel");
        this.labelContainer.addEventListener("onClick", (EventListener)new Object(this));
        this.labelContainer.appendChild((Component)createLabelRepresentation());
        if(getModel() != null)
        {
            this.popupContainer = new Bandpopup();
            this.popupContainer.setSclass("componentPopup");
            this.component.appendChild((Component)this.popupContainer);
            this.container = new Vbox();
            this.container.setWidth("100%");
            this.container.setParent((Component)this.popupContainer);
            this.notConfirmedItems = new Listbox();
            this.notConfirmedItems.setSclass("referenceSelectorSelectedItems");
            this.notConfirmedItems.setOddRowSclass("oddRowRowSclass");
            this.notConfirmedItems.setMultiple(Boolean.TRUE.booleanValue());
            this.notConfirmedItems.setFixedLayout(Boolean.FALSE.booleanValue());
            if(getModel().getItems() == null)
            {
                this.notConfirmedItems.setVisible(false);
            }
            else
            {
                this.notConfirmedItems.setVisible(!getModel().getItems().isEmpty());
            }
            this.notConfirmedItems.setItemRenderer(notConfirmedListItemrenderer());
            this.notConfirmedItems.addEventListener("onUser", (EventListener)new Object(this));
            this.autoCompleteInputField = new Bandbox();
            this.autoCompleteInputField.setReadonly((isDisabled() || !isAllowSelect()));
            this.autoCompleteInputField.setSclass("autoCompleteCmp");
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "AutocompleteInput_" + getId();
                UITools.applyTestID((Component)this.autoCompleteInputField, id);
            }
            String onkeyUpAction =
                            "onkeyup: var pressedKey = event.keyCode; if (typeof deferredSender !== 'undefined') clearTimeout(deferredSender); if(  event.keyCode == 0 )     pressedKey = event.charCode; var notAllowedCodes = new Array(13,16,17,35,36,37,39);for(var item in notAllowedCodes ) if(notAllowedCodes[item]==pressedKey)\treturn true; var inputValue = this.value; deferredSender = setTimeout( function() { comm.sendUser('"
                                            + this.autoCompleteInputField.getId() + "',inputValue,pressedKey); }, " + getDefaultAutocompleteTimeout() + ");";
            this.autoCompleteInputField.setAction(onkeyUpAction);
            this.autoCompleteInputField.addEventListener("onUser", (EventListener)new Object(this));
            this.autoCompleteInputField.setButtonVisible(Boolean.FALSE.booleanValue());
            this.autoCompleteInputField.focus();
            this.autoCompletePopup = new Bandpopup();
            Vbox containerInner = new Vbox();
            containerInner.setWidth("100%");
            containerInner.setStyle("table-layout: fixed;");
            this.autoCompletePopup.appendChild((Component)containerInner);
            this.autoCompletePopup.setSclass("autoCompletePopup");
            this.autoCompleteList = new Listbox();
            this.autoCompleteList.setSclass("autoCompleteList");
            this.autoCompleteList.setOddRowSclass("oddRowRowSclass");
            this.autoCompleteList.setWidth("100%");
            this.autoCompleteList.setMultiple(Boolean.TRUE.booleanValue());
            this.autoCompleteList.setFixedLayout(Boolean.FALSE.booleanValue());
            this.autoCompleteList.addEventListener("onUser", (EventListener)new Object(this));
            this.autoCompleteList.setItemRenderer(autoCompleteListItemRenderer());
            containerInner.appendChild((Component)addOnkeyDownFeature(this.autoCompleteList));
            this.autoCompletePopup.appendChild((Component)containerInner);
            this.addButton = new Toolbarbutton();
            if(getModel().isMultiple())
            {
                this.addButton.setLabel(Labels.getLabel("general.add"));
            }
            else
            {
                this.addButton.setLabel(Labels.getLabel("referenceselector.ok"));
            }
            this.addButton.setSclass("referenceEditorButton");
            this.addButton.addEventListener("onClick", (EventListener)new Object(this));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "AddButton_" + getId();
                UITools.applyTestID((Component)this.addButton, id);
            }
            Toolbarbutton cancelButton = new Toolbarbutton(Labels.getLabel("general.close"));
            cancelButton.setSclass("referenceEditorButton");
            cancelButton.addEventListener("onClick", (EventListener)new Object(this));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "CancelButton_" + getId();
                UITools.applyTestID((Component)cancelButton, id);
            }
            this.addButton.setVisible(Boolean.FALSE.booleanValue());
            containerInner.appendChild((Component)createVericalSpacer("3px"));
            Hbox buttonBox = new Hbox();
            buttonBox.appendChild((Component)this.addButton);
            buttonBox.appendChild((Component)cancelButton);
            containerInner.appendChild((Component)buttonBox);
            this.autoCompletePopup.appendChild((Component)containerInner);
            this.autoCompletePopup.setParent((Component)this.autoCompleteInputField);
            UICockpitPerspective cperspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getModel().getRootType().getCode());
            Toolbarbutton newItemButton = null;
            if(cperspective instanceof de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective)
            {
                newItemButton = new Toolbarbutton("", "/cockpit/images/open_advanced_editor.gif");
                newItemButton.addEventListener("onClick", (EventListener)new Object(this, objectTemplate));
            }
            Hbox refBox = new Hbox();
            refBox.appendChild((Component)this.autoCompleteInputField);
            if(newItemButton != null)
            {
                refBox.appendChild((Component)newItemButton);
            }
            containerInner.appendChild((Component)buttonBox);
            this.container.appendChild((Component)addOnkeyDownFeature(this.notConfirmedItems));
            this.container.appendChild((Component)createVericalSpacer("5px"));
            this.container.appendChild((Component)refBox);
            this.container.appendChild((Component)createVericalSpacer("5px"));
            Hbox footer = createFooter();
            footer.setAlign("center");
            this.container.appendChild((Component)footer);
            Div auxDiv = new Div();
            auxDiv.appendChild((Component)this.component);
            auxDiv.appendChild((Component)this.labelContainer);
            mainHbox.appendChild((Component)auxDiv);
            mainHbox.appendChild((Component)btnDiv);
            appendChild((Component)mainHbox);
            this.initialized = true;
        }
        return this.initialized;
    }


    private boolean isAllowSelect()
    {
        if(this.parameters != null && this.parameters.containsKey("allowSelect"))
        {
            Object object = this.parameters.get("allowSelect");
            if(object instanceof Boolean)
            {
                return ((Boolean)object).booleanValue();
            }
            if(object instanceof String && !StringUtils.isEmpty((String)object))
            {
                return Boolean.parseBoolean((String)object);
            }
        }
        return true;
    }


    public void loadViewComponents()
    {
        this.listView = loadListView();
        this.advancedSearchComponent = loadAdvancedSearchView();
    }


    protected UIAdvancedSearchView loadAdvancedSearchView()
    {
        this.advancedSearchComponent = (UIAdvancedSearchView)new AdvancedSearchView();
        this.advancedSearchComponent.setModel(getModel().getAdvancedSearchModel());
        if(this.advancedSearchController != null)
        {
            this.advancedSearchController.unregisterListeners();
        }
        this.advancedSearchController = (ComponentController)new DefaultSelectorAdvancedSearchController((AbstractReferenceSelectorModel)getModel(), this.advancedSearchComponent);
        this.advancedSearchController.initialize();
        return this.advancedSearchComponent;
    }


    public void setAdvancedSearchComponent(UIAdvancedSearchView searchComponent)
    {
    }


    protected String computeLabel()
    {
        StringBuilder labelBuffer = new StringBuilder();
        if(getModel().getItems() == null)
        {
            return labelBuffer.toString();
        }
        for(Iterator<Object> iter = getModel().getItems().iterator(); iter.hasNext(); )
        {
            Object singleItem = iter.next();
            labelBuffer.append(getModel().getItemLabel(singleItem));
            if(iter.hasNext())
            {
                labelBuffer.append(", ");
            }
        }
        return labelBuffer.toString();
    }


    public void updateItemsNotConfirmed()
    {
        if(this.initialized)
        {
            if(getModel().getNotConfirmedItems().size() > 0)
            {
                this.notConfirmedItems.setVisible(Boolean.TRUE.booleanValue());
                updateNotConfirmedItemList();
                trimToLimit(this.notConfirmedItems, 5, getModel().getNotConfirmedItems().size());
            }
            else
            {
                this.notConfirmedItems.setVisible(Boolean.FALSE.booleanValue());
            }
        }
    }


    public void updateItems()
    {
        if(this.initialized)
        {
            if(getModel().getItems() == null)
            {
                return;
            }
            this.labelContainer.getChildren().clear();
            Tr tableRow = createLabelRepresentation();
            tableRow.setParent((Component)this.labelContainer);
        }
    }


    public void setModel(ReferenceSelectorModel model)
    {
        if(this.model != model)
        {
            this.model = model;
            if(this.model != null)
            {
                initialize();
            }
        }
    }


    public ReferenceSelectorModel getModel()
    {
        return this.model;
    }


    private Hbox createFooter()
    {
        Hbox footerContainer = new Hbox();
        footerContainer.setAlign("center");
        Toolbarbutton okButton = createFooterButton("referenceselector.ok", (EventListener)new Object(this));
        UITools.applyTestID((Component)okButton, "OkButton_" + getId());
        footerContainer.appendChild((Component)okButton);
        return footerContainer;
    }


    private Toolbarbutton createFooterButton(String localizedLabelKey, EventListener buttonEventListener)
    {
        Toolbarbutton newButton = new Toolbarbutton();
        newButton.setSclass("referenceEditorButton");
        newButton.setLabel(Labels.getLabel(localizedLabelKey));
        newButton.addEventListener("onClick", buttonEventListener);
        return newButton;
    }


    private Space createVericalSpacer(String verticalSpace)
    {
        Space verticalSpacer = new Space();
        verticalSpacer.setOrient("vertical");
        verticalSpacer.setHeight(verticalSpace);
        return verticalSpacer;
    }


    public void reset()
    {
        getModel().reset();
    }


    private void trimToLimit(Listbox listbox, int limit, int actualSize)
    {
        listbox.setRows(Math.min(limit, actualSize));
    }


    protected void focusElement(HtmlBasedComponent focusableComponent)
    {
        focusableComponent.focus();
    }


    protected boolean isSelected(List<? extends Object> elements, Object candidate)
    {
        return elements.contains(candidate);
    }


    protected void clearAutoCompleteInputText()
    {
        this.autoCompleteInputField.smartUpdate("value", "");
    }


    protected void selectOrDeselectAutoCompleteItems(int index)
    {
        if(index != -1)
        {
            Object itemValue = this.autoCompleteList.getModel().getElementAt(index);
            Listitem listboxItem = this.autoCompleteList.getItemAtIndex(index);
            if(getModel().isMultiple())
            {
                boolean selected = (isSelected(getModel().getTemporaryItems(), itemValue) || isSelected(getModel().getNotConfirmedItems(), itemValue));
                if(selected)
                {
                    selectDeselectCheckbox(listboxItem, Boolean.FALSE.booleanValue());
                    fireDeselectTemporaryItem(itemValue);
                    fireRemoveTemporaryItem(itemValue);
                }
                else
                {
                    selectDeselectCheckbox(listboxItem, Boolean.TRUE.booleanValue());
                    fireSelectTemporaryItem(itemValue);
                    fireAddTemporaryItem(itemValue);
                }
            }
            else
            {
                fireDeselectTemporaryItems();
                fireSelectTemporaryItem(itemValue);
                fireClearTemporaryItems();
                fireAddTemporaryItem(itemValue);
                performOnOkAction();
            }
        }
    }


    protected void uncheckAllListItem(Listbox listbox)
    {
        for(Object uiComponent : listbox.getChildren())
        {
            if(uiComponent instanceof Listitem)
            {
                Listitem listboxItem = (Listitem)uiComponent;
                if(listboxItem.getFirstChild().getFirstChild() != null)
                {
                    selectDeselectCheckbox(listboxItem, Boolean.FALSE.booleanValue());
                }
            }
        }
    }


    protected void selectOrDeselectTemporaryItems(int index)
    {
        Listitem listboxItem = this.temporaryItemsList.getItemAtIndex(index);
        Object itemValue = this.temporaryItemsList.getModel().getElementAt(index);
        if(isSelected(getModel().getActualSelectedTempItems(), itemValue))
        {
            selectDeselectCheckbox(listboxItem, Boolean.FALSE.booleanValue());
            fireDeselectTemporaryItem(itemValue);
        }
        else
        {
            selectDeselectCheckbox(listboxItem, Boolean.TRUE.booleanValue());
            fireSelectTemporaryItem(itemValue);
        }
    }


    protected void selectOrDeselectItems(int index)
    {
        Listitem listboxItem = this.notConfirmedItems.getItemAtIndex(index);
        Object itemValue = this.notConfirmedItems.getModel().getElementAt(index);
        if(isSelected(getModel().getActualSelectedItems(), itemValue))
        {
            selectDeselectCheckbox(listboxItem, Boolean.FALSE.booleanValue());
            fireDeselectItem(itemValue);
        }
        else
        {
            selectDeselectCheckbox(listboxItem, Boolean.TRUE.booleanValue());
            fireSelectItem(itemValue);
        }
    }


    private Div addOnkeyDownFeature(Listbox listbox)
    {
        String onKeyDownScript = "onkeydown:var pressedKey = event.keyCode; if(event.keyCode == 0 ) \tpressedKey = event.charCode;  if( pressedKey == 13 || pressedKey == 27) {   comm.sendUser('" + listbox.getId() + "', pressedKey); return false;} return false;";
        Div activeContainer = new Div();
        activeContainer.setAction(onKeyDownScript);
        activeContainer.appendChild((Component)listbox);
        return activeContainer;
    }


    protected Tr createLabelRepresentation()
    {
        Tr tableRow = new Tr();
        Td firstCell = new Td();
        firstCell.setSclass("firstCell");
        Label substitueLabel = new Label();
        substitueLabel.setValue(computeLabel());
        firstCell.appendChild((Component)substitueLabel);
        firstCell.setParent((Component)tableRow);
        Td secondCell = new Td();
        secondCell.setSclass("secondCell");
        secondCell.setParent((Component)tableRow);
        return tableRow;
    }


    private void selectDeselectCheckbox(Listitem particularItem, boolean value)
    {
        Object unknownCmp = particularItem.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getFirstChild();
        if(unknownCmp instanceof Checkbox)
        {
            ((Checkbox)unknownCmp).setChecked(value);
        }
    }


    public void doItemActivated()
    {
        if(!getModel().isMultiple())
        {
            doModalDialogSaveClicked();
        }
    }


    private void tryPerformOkAfterSave()
    {
        fireSaveActualItems();
    }


    private void doModalDialogSaveClicked()
    {
        if(getModel().getTableModel() == null)
        {
            fireAbortAndCloseAdvancedMode();
            return;
        }
        MutableListModel cmpModel = getModel().getTableModel().getListComponentModel();
        List<Object> actualSelectedItems = new ArrayList();
        for(Integer index : cmpModel.getSelectedIndexes())
        {
            actualSelectedItems.add(cmpModel.getValueAt(index.intValue()));
        }
        fireAddToNotConfirmedItems(actualSelectedItems);
        fireSelectItems(actualSelectedItems);
        fireConfirmAndCloseAdvancedMode();
        Events.echoEvent("onConfirmCloseLater", (Component)this, null);
    }


    private void cleanup()
    {
        if(this.listViewController != null)
        {
            this.listViewController.unregisterListeners();
        }
        if(this.advancedSearchController != null)
        {
            this.advancedSearchController.unregisterListeners();
        }
    }


    public void desktopRemoved(Desktop desktop)
    {
        cleanup();
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    public void updateRootTypeChanged()
    {
    }


    public void updateRootSearchTypeChanged()
    {
    }


    protected ListitemRenderer autoCompleteListItemRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    protected ListitemRenderer notConfirmedListItemrenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    protected Table wrappIntoTable(Component firstCellContent, Component secondCellConent, String firstCellStyle, String secondCellStyle)
    {
        Table cellContainer = new Table();
        cellContainer.setDynamicProperty("width", "100%");
        Tr containerRow = new Tr();
        containerRow.setParent((Component)cellContainer);
        Td containerCellFirst = new Td();
        containerCellFirst.setDynamicProperty("width", "2%");
        containerCellFirst.setStyle(firstCellStyle);
        containerCellFirst.setParent((Component)containerRow);
        containerCellFirst.appendChild(firstCellContent);
        Td containerCellSecond = new Td();
        containerCellSecond.setDynamicProperty("width", "100%");
        containerCellSecond.setStyle(secondCellStyle);
        containerCellSecond.setParent((Component)containerRow);
        containerCellSecond.appendChild(secondCellConent);
        return cellContainer;
    }


    public void setInitString(String initStr)
    {
        this.autoCompleteInputField.setText(initStr);
    }


    public void setFocus()
    {
        fireSelectorNormalMode();
        log.debug("Fire in normal mode.");
    }


    protected void performOnOkAction()
    {
        this.autoCompleteInputField.close();
        fireAddToNotConfirmedItems(getModel().getActualSelectedTempItems());
        fireSelectItems(getModel().getActualSelectedTempItems());
        fireClearTemporaryItems();
        clearAutoCompleteInputText();
        if(!getModel().isMultiple())
        {
            fireSaveActualItems();
        }
    }


    public void addEventSelectorListener(String event, EventListener listener)
    {
        this.component.addEventListener(event, listener);
    }


    public boolean isDisabled()
    {
        return this.disabled;
    }


    public void setDisabled(boolean disabled)
    {
        if(this.disabled != disabled)
        {
            this.disabled = disabled;
        }
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public int getDefaultAutocompleteTimeout()
    {
        if(this.defaultAutocompleteTimeout == null)
        {
            try
            {
                this.defaultAutocompleteTimeout = Integer.valueOf(UITools.getCockpitParameter("default.autocompleteTimeout",
                                Executions.getCurrent()));
            }
            catch(Exception e)
            {
                this.defaultAutocompleteTimeout = Integer.valueOf(500);
            }
        }
        return this.defaultAutocompleteTimeout.intValue();
    }
}
