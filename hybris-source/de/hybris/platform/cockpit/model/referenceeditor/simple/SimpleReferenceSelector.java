package de.hybris.platform.cockpit.model.referenceeditor.simple;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.generic.strategies.PredefinedValuesStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class SimpleReferenceSelector extends AbstractSimpleReferenceSelector
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleReferenceSelector.class);
    protected static final String ADV_QUERY_BTN_IMG = "/cockpit/images/open_advanced_editor.gif";
    protected static final String EDIT_BTN_IMG = "cockpit/images/editMedia.png";
    protected static final String INIITAL_ROWS_NUMBER = "editor.referencecollection_initial_rows";
    protected static final String REFERENCE_EDITOR_BTN_SCLASS = "referenceEditorButton";
    protected static final String LIST_VIEW_SELECTOR_SCLASS = "listViewInSelectorContext";
    protected static final String MODAL_DIALOG_SCLASS = "referenceModalDialogSclass";
    protected static final int ENTER_CHAR = 13;
    protected static final int ESC_CHAR = 27;
    protected static final int DOWN_ARROW = 40;
    protected static final int UP_ARROW = 38;
    protected static final String CLOSE_BTN_IMG = "/cockpit/images/close_btn.png";
    public static final String EDIT_START_EVENT = "onEditStart";
    public static final String EDIT_FINISH_EVENT = "onFinishEdit";
    protected static final String EMPTY_MESSAGE = "general.nothingtodisplay";
    protected static final String WILD_CARD = "*";
    protected static final String REFERENECE_SELECTOR_SCLASS = "referenceSelector";
    protected static final String REFERENECE_SELECTOR_LISTBOX_ODD_ROWS = "oddRowRowSclass";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE = "autoCompleteCmp";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE_POPUP = "autoCompletePopup";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE_LIST = "autoCompleteList";
    protected static final String REFERENECE_SELECTOR_AUTOCOMPLETE_CELLITEM = "autoCompleteCell";
    protected transient Listbox autoCompleteList;
    protected transient Toolbarbutton newItemButton;
    protected transient Bandbox autoCompleteComponent;
    protected transient Bandpopup autoCompletePopup;
    protected transient UIListView listView = null;
    protected transient Vbox container;
    protected transient Div componentContainer;
    protected transient Hbox mainHbox = new Hbox();
    protected Toolbarbutton openPopupEditorBtn;
    private Integer defaultAutocompleteTimeout = null;
    private boolean initialized = false;
    private String searchTerm;
    private boolean showEditButton = true;


    public boolean isInitialized()
    {
        return this.initialized;
    }


    private boolean disabled = false;
    private Boolean allowcreate = Boolean.FALSE;
    private boolean allowAutocompletion = true;
    private CreateContext createContext = null;
    private SimpleReferenceSelectorModel model;


    public void setFocus(boolean focus)
    {
        if(focus)
        {
            fireSelectorNormalMode();
        }
    }


    public void showAutoCompletePopup()
    {
        this.autoCompleteComponent.open();
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            updateAutoCompleteItemList();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public void updateMode()
    {
        if(this.initialized && !this.disabled)
        {
            SimpleSelectorModel.Mode mode = getModel().getMode();
            if(mode.equals(SimpleSelectorModel.Mode.VIEW_MODE))
            {
                this.autoCompleteComponent.setText(computeLabel(getModel().getValue()));
                this.autoCompleteComponent.close();
                Events.postEvent("onFinishEdit", (Component)this.autoCompleteComponent, null);
            }
            else if(!mode.equals(SimpleSelectorModel.Mode.NORMAL_MODE))
            {
                if(mode.equals(SimpleSelectorModel.Mode.ADVANCED_MODE))
                {
                    showReferenceSelectorModalDialog();
                }
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
                this.autoCompleteComponent.close();
                this.autoCompleteList.setVisible(false);
                return;
            }
            this.autoCompleteList.setVisible(true);
            updateAutoCompleteItemList();
            trimToLimit(this.autoCompleteList, getInitialRowsNumber(), autoCompletedSize);
        }
    }


    public void updateSearchResult()
    {
    }


    protected void fireSelectorNormalMode()
    {
        if(!isDisabled())
        {
            super.fireSelectorNormalMode();
            String computedLabel = computeLabel(getModel().getValue());
            this.autoCompleteComponent.setSelectionRange(0, computedLabel.length());
            Events.postEvent("onEditStart", (Component)this.autoCompleteComponent, null);
        }
    }


    public boolean initialize()
    {
        addEventListener("onClick", (EventListener)new Object(this));
        this.initialized = false;
        setSclass("referenceSelector");
        this.mainHbox.setWidths("none,22px,22px");
        this.mainHbox.setSclass("referenceSelector_hbox");
        Div btnDiv = new Div();
        btnDiv.setStyle("margin: 0 0 0 0;padding-left:2px;padding-top:2px;");
        Toolbarbutton advancedSearchBtn = createAdvancedSearchButton();
        this.openPopupEditorBtn = new Toolbarbutton("", "cockpit/images/editMedia.png");
        this.openPopupEditorBtn.setStyle("padding-left:3px");
        this.openPopupEditorBtn.setSclass("btnEdit");
        this.openPopupEditorBtn.setTooltiptext(Labels.getLabel("editor.edit.tooltip"));
        this.openPopupEditorBtn.addEventListener("onClick", (EventListener)new Object(this));
        if(getModel() != null)
        {
            this.container = new Vbox();
            this.container.setWidth("100%");
            this.autoCompleteComponent = new Bandbox();
            this.autoCompleteComponent.setReadonly((isDisabled() || !isAutocompletionAllowed() || !isAllowSelect()));
            this.autoCompleteComponent.setText(computeLabel(getModel().getValue()));
            this.autoCompleteComponent.setSclass("autoCompleteCmp");
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = "AutocompleteInput_" + getId();
                UITools.applyTestID((Component)this.autoCompleteComponent, id);
            }
            String onkeyUpAction =
                            "onkeyup: var pressedKey = event.keyCode; if (typeof deferredSender !== 'undefined') clearTimeout(deferredSender);  if(  event.keyCode == 0 )     pressedKey = event.charCode; var notAllowedCodes = new Array(13,16,17,27,35,36,37,39);for(var item in notAllowedCodes ) if(notAllowedCodes[item]==pressedKey)\treturn true; var inputValue = this.value; deferredSender = setTimeout( function() { comm.sendUser('"
                                            + this.autoCompleteComponent.getId() + "',inputValue,pressedKey); }, " + getDefaultAutocompleteTimeout() + ");";
            this.autoCompleteComponent.setAction(onkeyUpAction);
            this.autoCompleteComponent.addEventListener("onUser", (EventListener)new Object(this));
            this.autoCompleteComponent.setButtonVisible(false);
            this.autoCompleteComponent.addEventListener("onOpen", (EventListener)new Object(this));
            this.autoCompleteComponent.addEventListener("onCancel", (EventListener)new Object(this));
            Vbox containerInner = new Vbox();
            containerInner.setWidth("100%");
            containerInner.setStyle("table-layout: fixed;");
            this.autoCompletePopup = new Bandpopup();
            this.autoCompletePopup.appendChild((Component)containerInner);
            this.autoCompletePopup.setSclass("autoCompletePopup");
            this.autoCompleteList = new Listbox();
            this.autoCompleteList.setSclass("autoCompleteList");
            this.autoCompleteList.setOddRowSclass("oddRowRowSclass");
            this.autoCompleteList.setWidth("100%");
            this.autoCompleteList.setRows(8);
            this.autoCompleteList.setFixedLayout(false);
            this.autoCompleteList.setItemRenderer(autoCompleteListItemRenderer());
            this.autoCompleteList.addEventListener("onOK", (EventListener)new Object(this));
            containerInner.appendChild((Component)this.autoCompleteList);
            this.autoCompletePopup.appendChild((Component)containerInner);
            containerInner.appendChild((Component)createVericalSpacer("3px"));
            if(!isDisabled() && isAutocompletionAllowed())
            {
                this.autoCompletePopup.setParent((Component)this.autoCompleteComponent);
            }
            if(!isDisabled())
            {
                this.componentContainer = new Div();
                this.componentContainer.setSclass("autoCompleteCmp_cnt");
                this.componentContainer.appendChild((Component)this.autoCompleteComponent);
                this.mainHbox.appendChild((Component)this.componentContainer);
                this.mainHbox.appendChild((Component)advancedSearchBtn);
                updateEditButtonVisibility();
                this.mainHbox.appendChild((Component)this.openPopupEditorBtn);
            }
            else
            {
                this.componentContainer = new Div();
                this.componentContainer.setSclass("autoCompleteCmp_cnt");
                this.componentContainer.appendChild((Component)this.autoCompleteComponent);
                this.mainHbox.appendChild((Component)this.componentContainer);
            }
            appendChild((Component)this.mainHbox);
            this.initialized = true;
        }
        return this.initialized;
    }


    protected Toolbarbutton createAdvancedSearchButton()
    {
        Toolbarbutton advancedSearchBtn = new Toolbarbutton("", "/cockpit/images/open_advanced_editor.gif");
        advancedSearchBtn.setStyle("padding-left:2px");
        advancedSearchBtn.setTooltiptext(Labels.getLabel("general.add.reference"));
        advancedSearchBtn.addEventListener("onClick", (EventListener)new Object(this));
        return advancedSearchBtn;
    }


    protected void updateEditButtonVisibility()
    {
        boolean areaRelatedEditVisibility = false;
        if(this.model instanceof DefaultSimpleReferenceSelectorModel)
        {
            Object object = ((DefaultSimpleReferenceSelectorModel)this.model).getParameters().get("showEditButton");
            if(object instanceof String && Boolean.parseBoolean((String)object) && ((DefaultSimpleReferenceSelectorModel)this.model)
                            .getValue() != null)
            {
                areaRelatedEditVisibility = true;
            }
        }
        this.openPopupEditorBtn.setVisible((this.showEditButton && areaRelatedEditVisibility));
    }


    public CreateContext getCreateContext()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getModel().getRootType().getCode());
        CreateContext createContext = (this.createContext == null) ? new CreateContext((ObjectType)objectTemplate.getBaseType(), null, (PropertyDescriptor)getModel().getParameters().get("propertyDescriptor"), null) : this.createContext;
        return createContext;
    }


    public void updateItems()
    {
        if(this.initialized)
        {
            this.autoCompleteComponent.setText(computeLabel(getModel().getValue()));
            updateEditButtonVisibility();
        }
    }


    public void setModel(SimpleReferenceSelectorModel model)
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


    public DefaultSimpleReferenceSelectorModel getModel()
    {
        return (DefaultSimpleReferenceSelectorModel)this.model;
    }


    public void updateRootTypeChanged()
    {
    }


    public void updateRootSearchTypeChanged()
    {
    }


    public void doModalDialogSaveClicked()
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
        if(!actualSelectedItems.isEmpty())
        {
            fireSaveActualItem(actualSelectedItems.iterator().next());
        }
    }


    public void saveCurrentValue(Object currentValue)
    {
        this.autoCompleteComponent.close();
        fireSaveActualItem(currentValue);
    }


    public void addEventSelectorListener(String event, EventListener listener)
    {
        this.autoCompleteComponent.addEventListener(event, listener);
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


    public boolean isAutocompletionAllowed()
    {
        return this.allowAutocompletion;
    }


    public void setAutocompletionAllowed(boolean allowed)
    {
        this.allowAutocompletion = allowed;
    }


    public void setInitString(String initStr)
    {
        this.autoCompleteComponent.setText(initStr);
        if(StringUtils.isNotBlank(initStr))
        {
            this.autoCompleteComponent.setSelectionRange(initStr.length() + 1, initStr.length() + 1);
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


    public void fireSaveActualSelected()
    {
        if(this.autoCompleteList.getSelectedItem() != null)
        {
            fireSaveActualItem(this.autoCompleteList.getSelectedItem().getValue());
        }
    }


    public boolean isEntrySelected(Object value)
    {
        return (value != null && value.equals(getModel().getValue()));
    }


    protected ListitemRenderer autoCompleteListItemRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    protected void updateAutoCompleteItemList()
    {
        this.autoCompleteList.setModel((ListModel)new SimpleListModel(this.model.getAutoCompleteResult()));
    }


    protected void showReferenceSelectorModalDialog()
    {
        ObjectTemplate objectTemplate = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getModel().getRootType().getCode());
        Object object = new Object(this, objectTemplate, getPage().getFirstRoot(), null);
        Map<String, Object> customParameters = new HashMap<>();
        if(getModel().getParameters() != null)
        {
            customParameters.putAll(getModel().getParameters());
        }
        customParameters.put("forceCreateInWizard", Boolean.TRUE);
        object.setParameters(customParameters);
        object.setAllowCreate(isAllowcreate().booleanValue());
        object.setAllowSelect(isAllowSelect());
        object.setCreateContext(getCreateContext());
        object.setPredefinedValues(getPredefinedValues(getModel().getParameters()));
        object.start();
    }


    protected Map<String, Object> getPredefinedValues(Map<String, ? extends Object> parameters)
    {
        Map<String, Object> ret = new HashMap<>();
        Object predefinedPropertyValues = parameters.get("predefinedPropertyValues");
        if(predefinedPropertyValues instanceof Map)
        {
            for(Object entry : ((Map)predefinedPropertyValues).entrySet())
            {
                if(entry instanceof Map.Entry)
                {
                    Object key = ((Map.Entry)entry).getKey();
                    if(key instanceof PropertyDescriptor)
                    {
                        ret.put(((PropertyDescriptor)key).getQualifier(), ((Map.Entry)entry).getValue());
                    }
                }
            }
        }
        String predefinedValuesStrategy = (String)parameters.get("predefinedValuesStrategy");
        if(StringUtils.isNotBlank(predefinedValuesStrategy))
        {
            PredefinedValuesStrategy strategy = (PredefinedValuesStrategy)SpringUtil.getBean(predefinedValuesStrategy);
            if(strategy != null)
            {
                ret.putAll(strategy.getPredefinedValues(getCreateContext()));
            }
        }
        return ret;
    }


    private boolean isAllowSelect()
    {
        Map<String, ? extends Object> parameters = (getModel() == null) ? null : getModel().getParameters();
        if(parameters != null && parameters.containsKey("allowSelect"))
        {
            Object object = parameters.get("allowSelect");
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


    protected String computeLabel(Object typedObject)
    {
        if(typedObject instanceof TypedObject)
        {
            String label = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject((TypedObject)typedObject);
            if(!"<null>".equals(label))
            {
                return label;
            }
            return ((TypedObject)typedObject).getObject().toString();
        }
        return "";
    }


    protected void selectOrDeselectAutoCompleteItems(int index)
    {
        if(index != -1)
        {
            Object itemValue = this.autoCompleteList.getModel().getElementAt(index);
            saveCurrentValue(itemValue);
        }
    }


    private Space createVericalSpacer(String verticalSpace)
    {
        Space verticalSpacer = new Space();
        verticalSpacer.setOrient("vertical");
        verticalSpacer.setHeight(verticalSpace);
        return verticalSpacer;
    }


    private void trimToLimit(Listbox listbox, int limit, int actualSize)
    {
        listbox.setRows(Math.min(limit, actualSize));
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


    public String getSearchTerm()
    {
        return this.searchTerm;
    }


    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }


    public boolean isShowEditButton()
    {
        return this.showEditButton;
    }


    public void setShowEditButton(boolean showEditButton)
    {
        this.showEditButton = showEditButton;
    }


    protected int getInitialRowsNumber()
    {
        return Integer.parseInt(UITools.getCockpitParameter("editor.referencecollection_initial_rows", Executions.getCurrent()));
    }
}
