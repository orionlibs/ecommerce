package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ListUIEditor;
import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.editor.search.ConditionUIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class AdvancedSearchView extends AbstractAdvancedSearchView
{
    private static final String EDITOR_CONTEXT = "editorContext";
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedSearchView.class);
    private AdvancedSearchModel model;
    protected boolean initialized = false;
    protected static final String SEARCH_MAGNIFIER_BTN_IMG = "/cockpit/images/BUTTON_search.png";
    protected static final String EDIT_BTN_IMG = "/cockpit/images/icon_func_search_edit.png";
    protected static final String EDIT_BTN_IMG_ACTIVE = "/cockpit/images/icon_func_search_edit_white.png";
    protected static final String CLEAR_BTN_IMG = "/cockpit/images/icon_func_search_clear.png";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG_SEARCH_BUTTON = "CreateWebsite_ContentCatalog_Search_button";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG = "CreateWebsite_ContentCatalog_";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG_ASCENDING_CHECKBOX = "CreateWebsite_ContentCatalog_Ascending_checkbox";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG_EXCLUDESUBTYPES_CHECKBOX = "CreateWebsite_ContentCatalog_ExcludeSubtypes_checkbox";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG_TYPE_COMBOBOX = "CreateWebsite_ContentCatalog_Type_combobox";
    private static final String COCKPIT_ID_CREATEWEBSITE_CONTENTCATALOG_SORT_COMBOBOX = "CreateWebsite_ContentCatalog_Sort_combobox";
    private static final String ABSTRACT_TYPE_COMPBO_ITEM = "abstractTypeComboItem";
    private final transient List<Component> rightCustomButtons = new ArrayList<>();
    private final transient List<Component> leftCustomButtons = new ArrayList<>();
    private final Map<SearchField, Component> visibleFieldComponentMap = new HashMap<>();
    private final Map<SearchField, Component> hiddenFieldComponentMap = new HashMap<>();
    private final Map<SearchFieldGroup, Component> visibleGroupComponentMap = new HashMap<>();
    private final Map<SearchFieldGroup, Component> hiddenGroupComponentMap = new HashMap<>();
    private SearchField nextSearchFieldToFocus = null;
    private transient Combobox typeCombo;
    private transient Combobox sortCombo;
    private transient Checkbox sortOrderCheckbox;
    private transient Checkbox excludeSubtypesCheckbox;
    private transient Div editDiv;
    private transient Div mainDiv;
    private transient Checkbox focusComponent;
    private transient Toolbarbutton editBtn;


    public AdvancedSearchView()
    {
    }


    public AdvancedSearchView(List<? extends Component> leftButtons, List<? extends Component> rightButtons)
    {
        if(rightButtons != null)
        {
            this.rightCustomButtons.addAll(rightButtons);
        }
        if(leftButtons != null)
        {
            this.leftCustomButtons.addAll(leftButtons);
        }
    }


    public final boolean initialize()
    {
        this.initialized = false;
        if(getModel() != null)
        {
            setSclass("advanceSearchContainer");
            Vbox advAreaVbox = new Vbox();
            this.focusComponent = new Checkbox();
            this.focusComponent.setStyle("position: absolute; left: -100px;");
            Div topDiv = new Div();
            topDiv.setSclass("advanceSearchTopArea");
            advAreaVbox.appendChild((Component)topDiv);
            renderTopArea((Component)topDiv);
            this.mainDiv = new Div();
            this.mainDiv.addEventListener("onFocusLater", (EventListener)new Object(this));
            this.mainDiv.appendChild((Component)this.focusComponent);
            renderMainSearchFieldArea((Component)this.mainDiv);
            this.mainDiv.setSclass("advancedSearchMainArea");
            advAreaVbox.appendChild((Component)this.mainDiv);
            Div bottomDiv = new Div();
            advAreaVbox.appendChild((Component)bottomDiv);
            bottomDiv.setSclass("advSearchBottomToolbar");
            renderBottomArea((Component)bottomDiv);
            appendChild((Component)advAreaVbox);
            updateTypes();
            updateSelectedType();
            updateSortFields();
            this.initialized = true;
        }
        return this.initialized;
    }


    private Component createEditGroupsRecursively(SearchFieldGroup group)
    {
        EditSearchGroupComponent editSearchGroupComponent = createEditSearchGroupComponent(group);
        Component container = editSearchGroupComponent.getGroupContainer();
        renderEditSearchFieldEntries(group.getHiddenSearchFields(), container);
        for(SearchFieldGroup subGroup : group.getPartiallyVisibleGroups())
        {
            container.appendChild(createEditGroupsRecursively(subGroup));
        }
        return (Component)editSearchGroupComponent;
    }


    private Component createGroupsRecursively(SearchFieldGroup group)
    {
        SearchGroupComponent grpComp = createSearchGroupComponent(group);
        Component container = grpComp.getGroupContainer();
        for(SearchField field : group.getVisibleSearchFields())
        {
            container.appendChild(createSearchFieldRowComponent(field));
        }
        for(SearchFieldGroup subGroup : group.getVisibleSearchFieldGroups())
        {
            container.appendChild(createGroupsRecursively(subGroup));
        }
        return (Component)grpComp;
    }


    private void renderMainSearchFieldArea(Component parent)
    {
        SearchFieldGroup rootGroup = getModel().getRootSearchFieldGroup();
        for(SearchField field : rootGroup.getVisibleSearchFields())
        {
            parent.appendChild(createSearchFieldRowComponent(field));
        }
        for(SearchFieldGroup group : rootGroup.getVisibleSearchFieldGroups())
        {
            parent.appendChild(createGroupsRecursively(group));
        }
        focusSearchField(null);
    }


    private void focusSearchField(SearchField field)
    {
        try
        {
            List<SearchField> allSearchFields = getModel().getRootSearchFieldGroup().getAllSearchFields();
            SearchField focusField = null;
            if(field == null)
            {
                for(SearchField searchField : allSearchFields)
                {
                    if(searchField.isVisible())
                    {
                        focusField = searchField;
                        break;
                    }
                }
            }
            else
            {
                focusField = field;
            }
            if(focusField != null)
            {
                Component component = this.visibleFieldComponentMap.get(focusField);
                if(component != null)
                {
                    Object attribute = component.getAttribute("editorContext");
                    if(attribute instanceof EditorContext)
                    {
                        EditorContext ctx = (EditorContext)attribute;
                        ctx.editor.setFocus(ctx.editorView, false);
                    }
                }
            }
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn("Could not set focus.");
            }
        }
    }


    private void renderTopArea(Component parent)
    {
        Hbox hbox = new Hbox();
        Label label = new Label(Labels.getLabel("advancedsearch.objectTemplate"));
        label.setStyle("margin-right: 4px; margin-left: 8px; margin-top: 2px; display: block;");
        hbox.appendChild((Component)label);
        this.typeCombo = new Combobox();
        this.typeCombo.setCols(14);
        this.typeCombo.setReadonly(true);
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.typeCombo, "CreateWebsite_ContentCatalog_Type_combobox");
        }
        hbox.appendChild((Component)this.typeCombo);
        this.typeCombo.addEventListener("onChange", (EventListener)new Object(this));
        this.typeCombo.addEventListener("onLater", (EventListener)new Object(this));
        label = new Label(Labels.getLabel("advancedsearch.sort"));
        label.setStyle("margin-right: 4px; margin-left: 8px; margin-top: 2px; display: block;");
        hbox.appendChild((Component)label);
        this.sortCombo = new Combobox();
        this.sortCombo.setCols(14);
        this.sortCombo.setReadonly(true);
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.sortCombo, "CreateWebsite_ContentCatalog_Sort_combobox");
        }
        this.sortCombo.addEventListener("onChange", (EventListener)new Object(this));
        hbox.appendChild((Component)this.sortCombo);
        label = new Label(Labels.getLabel("advancedsearch.sortorder.asc"));
        label.setStyle("margin-right: 4px; margin-left: 8px; margin-top: 2px; display: block;");
        hbox.appendChild((Component)label);
        this.sortOrderCheckbox = new Checkbox();
        this.sortOrderCheckbox.setChecked(getParameterContainer().isSortAscending());
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.sortOrderCheckbox, "CreateWebsite_ContentCatalog_Ascending_checkbox");
        }
        this.sortOrderCheckbox.addEventListener("onCheck", (EventListener)new Object(this));
        hbox.appendChild((Component)this.sortOrderCheckbox);
        label = new Label(Labels.getLabel("advancedsearch.excludesubtypes"));
        label.setStyle("margin-right: 4px; margin-left: 8px; margin-top: 2px; display: block;");
        hbox.appendChild((Component)label);
        this.excludeSubtypesCheckbox = new Checkbox();
        this.excludeSubtypesCheckbox.setChecked(getParameterContainer().isExcludeSubtypes());
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.excludeSubtypesCheckbox, "CreateWebsite_ContentCatalog_ExcludeSubtypes_checkbox");
        }
        this.excludeSubtypesCheckbox.addEventListener("onCheck", (EventListener)new Object(this));
        hbox.appendChild((Component)this.excludeSubtypesCheckbox);
        parent.appendChild((Component)hbox);
        this.editDiv = new Div();
        this.editDiv.setSclass("advancedSearchConfigArea");
        this.editDiv.setVisible(isInEditMode());
        this.editDiv.setAction("onshow: anima.slideDown(#{self}); onhide: anima.slideUp(#{self})");
        renderEditArea((Component)this.editDiv);
        parent.appendChild((Component)this.editDiv);
    }


    private void renderBottomArea(Component parent)
    {
        Hbox toolbarBox = new Hbox();
        toolbarBox.setWidth("100%");
        Div leftDiv = new Div();
        for(Component tbtn : this.leftCustomButtons)
        {
            leftDiv.appendChild(tbtn);
            leftDiv.appendChild((Component)new Space());
        }
        toolbarBox.appendChild((Component)leftDiv);
        Div rightDiv = new Div();
        rightDiv.setAlign("right");
        toolbarBox.appendChild((Component)rightDiv);
        Toolbarbutton clearBtn = new Toolbarbutton("", "/cockpit/images/icon_func_search_clear.png");
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "ClearAdvSearchBtn_";
            UITools.applyTestID((Component)clearBtn, "ClearAdvSearchBtn_");
        }
        rightDiv.appendChild((Component)clearBtn);
        clearBtn.setTooltiptext(Labels.getLabel("advancedsearch.button.clear.tooltip"));
        clearBtn.addEventListener("onClick", (EventListener)new Object(this));
        rightDiv.appendChild((Component)new Space());
        this.editBtn = new Toolbarbutton("", "/cockpit/images/icon_func_search_edit.png");
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = "ToggleEditModeBtn_";
            UITools.applyTestID((Component)this.editBtn, "ToggleEditModeBtn_");
        }
        rightDiv.appendChild((Component)this.editBtn);
        this.editBtn.setTooltiptext(Labels.getLabel("advancedsearch.button.edit.tooltip"));
        this.editBtn.addEventListener("onClick", (EventListener)new Object(this));
        rightDiv.appendChild((Component)new Space());
        Button searchBtn = new Button(Labels.getLabel(""));
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            if("cmscockpit".equals(UITools.getWebAppName(UITools.getCurrentZKRoot().getDesktop())))
            {
                UITools.applyTestID((Component)searchBtn, "CreateWebsite_ContentCatalog_Search_button");
            }
            else
            {
                String id = "AdvSearchSearchBtn_";
                UITools.applyTestID((Component)searchBtn, "AdvSearchSearchBtn_");
            }
        }
        searchBtn.setSclass("advSearchBtn");
        searchBtn.setTooltiptext(Labels.getLabel("general.search"));
        searchBtn.setImage("/cockpit/images/BUTTON_search.png");
        searchBtn.addEventListener("onClick", (EventListener)new Object(this));
        searchBtn.addEventListener("onLaterSearch", (EventListener)new Object(this));
        rightDiv.appendChild((Component)searchBtn);
        for(Component tbtn : this.rightCustomButtons)
        {
            rightDiv.appendChild((Component)new Space());
            rightDiv.appendChild(tbtn);
        }
        parent.appendChild((Component)toolbarBox);
    }


    protected void renderEditArea(Component parent)
    {
        SearchFieldGroup rootSearchFieldGroup = getModel().getRootSearchFieldGroup();
        renderEditSearchFieldEntries(rootSearchFieldGroup.getHiddenSearchFields(), parent);
        for(SearchFieldGroup group : rootSearchFieldGroup.getPartiallyVisibleGroups())
        {
            parent.appendChild(createEditGroupsRecursively(group));
        }
    }


    protected void renderEditSearchFieldEntries(Collection<SearchField> entries, Component parent)
    {
        boolean first = true;
        for(SearchField field : entries)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                parent.appendChild((Component)new Label(", "));
            }
            parent.appendChild(createEditSearchFieldEntry(field));
        }
    }


    protected SearchGroupComponent createSearchGroupComponent(SearchFieldGroup group)
    {
        SearchGroupComponent ret = new SearchGroupComponent((EventListener)new Object(this, group));
        ret.init(group.getLabel());
        this.visibleGroupComponentMap.put(group, ret);
        return ret;
    }


    protected EditSearchGroupComponent createEditSearchGroupComponent(SearchFieldGroup group)
    {
        EditSearchGroupComponent ret = new EditSearchGroupComponent((EventListener)new Object(this, group));
        ret.init(group.getLabel());
        this.hiddenGroupComponentMap.put(group, ret);
        return ret;
    }


    protected Component createEditSearchFieldEntry(SearchField field)
    {
        Toolbarbutton button = new Toolbarbutton(field.getLabel());
        button.setTooltiptext(field.getName());
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = field.getName() + "_AddBtn_";
            UITools.applyTestID((Component)button, id);
        }
        getParameterContainer().remove(field);
        button.addEventListener("onClick", (EventListener)new Object(this, field));
        this.hiddenFieldComponentMap.put(field, button);
        return (Component)button;
    }


    protected void createSimpleRowComponent(Component parent, Object rawInitialValue, PropertyDescriptor propertyDescriptor, SearchField field, UIEditor editor)
    {
        Object initialValue = rawInitialValue;
        if(editor instanceof de.hybris.platform.cockpit.model.editor.ReferenceUIEditor)
        {
            if(rawInitialValue != null)
            {
                TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
                if(rawInitialValue instanceof Collection)
                {
                    initialValue = typeService.wrapItems((Collection)rawInitialValue);
                }
                else
                {
                    initialValue = typeService.wrapItem(rawInitialValue);
                }
            }
        }
        if(editor instanceof ListUIEditor)
        {
            ((ListUIEditor)editor).setAvailableValues(UISessionUtils.getCurrentSession().getTypeService()
                            .getAvailableValues(propertyDescriptor));
        }
        if("BOOLEAN".equals(propertyDescriptor.getEditorType()) && rawInitialValue instanceof String)
        {
            initialValue = BooleanUtils.toBooleanObject((String)rawInitialValue);
        }
        Map<String, Object> customParameters = new HashMap<>();
        customParameters.put("attributeQualifier", propertyDescriptor.getQualifier());
        customParameters.put("propertyDescriptor", propertyDescriptor);
        customParameters.put("searchMode", Boolean.TRUE);
        Map<String, String> parametersForSearchField = getModel().getParametersForSearchField(field);
        if(parametersForSearchField != null)
        {
            customParameters.putAll(parametersForSearchField);
        }
        HtmlBasedComponent htmlBasedComponent = editor.createViewComponent(initialValue, customParameters, (EditorListener)new Object(this, field));
        parent.appendChild((Component)htmlBasedComponent);
    }


    protected void fireActionPerformed(String actionCode, SearchField field)
    {
        if("enter_pressed".equals(actionCode))
        {
            fireSearch();
            this.nextSearchFieldToFocus = field;
            Events.echoEvent("onFocusLater", (Component)this.mainDiv, null);
        }
        else if("up_pressed".equals(actionCode))
        {
            List<SearchField> visibleSearchFields = getVisibleSearchFieldsSorted();
            int index = visibleSearchFields.indexOf(field);
            if(index > 0)
            {
                focusSearchField(visibleSearchFields.get(index - 1));
            }
        }
        else if("down_pressed".equals(actionCode))
        {
            List<SearchField> visibleSearchFields = getVisibleSearchFieldsSorted();
            int index = visibleSearchFields.indexOf(field);
            if(index < visibleSearchFields.size() - 1)
            {
                focusSearchField(visibleSearchFields.get(index + 1));
            }
        }
    }


    private List<SearchField> getVisibleSearchFieldsSorted()
    {
        List<SearchField> ret = new ArrayList<>();
        SearchFieldGroup rootGroup = getModel().getRootSearchFieldGroup();
        for(SearchField searchField : rootGroup.getAllSearchFields())
        {
            if(searchField.isVisible())
            {
                ret.add(searchField);
            }
        }
        return ret;
    }


    protected Component createSearchFieldRowComponent(SearchField field)
    {
        Div ret = new Div();
        ret.setSclass("searchFieldRow");
        Hbox hbox = new Hbox();
        hbox.setWidth("100%");
        hbox.setWidths("140px, none");
        Toolbarbutton removeBtn = new Toolbarbutton("", "/cockpit/images/remove.png");
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            String id = field.getName() + "_RemoveBtn_";
            UITools.applyTestID((Component)removeBtn, id);
        }
        removeBtn.setTooltiptext(Labels.getLabel("advancedsearch.button.remove.tooltip"));
        removeBtn.addEventListener("onClick", (EventListener)new Object(this, field));
        removeBtn.setSclass("advancedSearchRemoveBtn");
        Div labelDiv = new Div();
        labelDiv.appendChild((Component)removeBtn);
        Label label = new Label(field.getLabel());
        label.setTooltiptext(field.getName());
        labelDiv.appendChild((Component)label);
        hbox.appendChild((Component)labelDiv);
        UIEditor editor = getModel().getEditor(field);
        PropertyDescriptor propertyDescriptor = getModel().getPropertyDescriptor(field);
        hbox.removeAttribute("editorContext");
        if(editor == null)
        {
            hbox.appendChild((Component)new Label("<Error - No editor found>"));
        }
        else if(editor instanceof ConditionUIEditor)
        {
            HtmlBasedComponent viewComponent = editor.createViewComponent(getParameterContainer().get(field), ((ConditionUIEditor)editor)
                            .getParameters(), (EditorListener)new Object(this, field));
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                UITools.applyTestID(viewComponent.getFirstChild().getFirstChild(), "CreateWebsite_ContentCatalog_" + label
                                .getValue() + "_input");
            }
            hbox.appendChild((Component)viewComponent);
            hbox.setAttribute("editorContext", new EditorContext(this, editor, viewComponent));
        }
        else if(getParameterContainer().get(field) == null ||
                        AdvancedSearchHelper.isSingleSimpleCondition(getParameterContainer().get(field)))
        {
            createSimpleRowComponent((Component)hbox,
                            (getParameterContainer().get(field) == null) ? null :
                                            AdvancedSearchHelper.getSingleSimpleCondition(getParameterContainer().get(field)), propertyDescriptor, field, editor);
        }
        else
        {
            LOG.error("Could not create search row component.");
        }
        this.visibleFieldComponentMap.put(field, hbox);
        ret.addEventListener("onClick", (EventListener)new Object(this, field));
        ret.appendChild((Component)hbox);
        return (Component)ret;
    }


    public AdvancedSearchModel getModel()
    {
        return this.model;
    }


    public void setModel(AdvancedSearchModel model)
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


    public void updateSearchField(SearchField searchField, Object value)
    {
        getParameterContainer().put(searchField, AdvancedSearchHelper.createSimpleConditionValue(value));
        updateSearchFields();
    }


    public void updateSearchFields()
    {
        this.mainDiv.getChildren().clear();
        if(this.focusComponent != null)
        {
            this.mainDiv.appendChild((Component)this.focusComponent);
        }
        renderMainSearchFieldArea((Component)this.mainDiv);
        this.editDiv.getChildren().clear();
        renderEditArea((Component)this.editDiv);
    }


    public void updateSearchGroups()
    {
        updateSearchFields();
    }


    public void updateSelectedType()
    {
        if(this.model != null)
        {
            ObjectTemplate selectedType = getModel().getSelectedType();
            for(Comboitem ci : this.typeCombo.getItems())
            {
                if(ci.getValue().equals(selectedType))
                {
                    this.typeCombo.setSelectedItem(ci);
                }
            }
        }
    }


    public void updateTypes()
    {
        if(this.typeCombo != null)
        {
            this.typeCombo.getItems().clear();
            if(this.model != null)
            {
                if(getModel().getTypes() != null)
                {
                    for(ObjectTemplate type : getModel().getTypes())
                    {
                        Comboitem item = new Comboitem(type.getCode());
                        item.setValue(type);
                        String tooltipPrefix = Labels.getLabel("advancedsearch.type.concrete");
                        if(type.isAbstract())
                        {
                            tooltipPrefix = Labels.getLabel("advancedsearch.type.abstract");
                            UITools.modifySClass((HtmlBasedComponent)item, "abstractTypeComboItem", true);
                        }
                        item.setTooltiptext(tooltipPrefix + ": " + tooltipPrefix);
                        this.typeCombo.appendChild((Component)item);
                    }
                }
            }
        }
    }


    public void updateSortFields()
    {
        if(this.sortCombo != null)
        {
            this.sortCombo.getItems().clear();
            if(this.model != null)
            {
                if(getModel().getSortableProperties() != null)
                {
                    for(PropertyDescriptor sortProp : getModel().getSortableProperties())
                    {
                        Comboitem item = new Comboitem(sortProp.getName());
                        item.setValue(sortProp);
                        this.sortCombo.appendChild((Component)item);
                    }
                    try
                    {
                        int index = getModel().getSortableProperties().indexOf(getModel().getSortedByProperty());
                        if(!this.sortCombo.getChildren().isEmpty())
                        {
                            this.sortCombo.setSelectedIndex((index >= 0) ? index : 0);
                        }
                        getParameterContainer().setSortProperty(getModel().getSortedByProperty());
                        getParameterContainer().setSortAscending(getModel().isSortAscending());
                    }
                    catch(Exception e)
                    {
                        LOG.warn("Problem occurred while setting selected sort property", e);
                        if(!this.sortCombo.getChildren().isEmpty())
                        {
                            this.sortCombo.setSelectedIndex(0);
                        }
                    }
                }
            }
        }
        if(this.sortOrderCheckbox != null)
        {
            this.sortOrderCheckbox.setChecked(getParameterContainer().isSortAscending());
        }
    }


    public void setEditMode(boolean value)
    {
        super.setEditMode(value);
        UITools.modifySClass((HtmlBasedComponent)this, "editModeSearch", value);
        UITools.modifySClass((HtmlBasedComponent)this.editBtn, "editbtnEnabled", isInEditMode());
        this.editBtn.setImage(isInEditMode() ? "/cockpit/images/icon_func_search_edit_white.png" : "/cockpit/images/icon_func_search_edit.png");
        if(this.editDiv != null)
        {
            this.editDiv.setVisible(isInEditMode());
        }
    }


    protected void resizeCorrespondingContentBrowser()
    {
        AbstractBrowserArea browserArea = (AbstractBrowserArea)UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        BrowserModel browserModel = browserArea.getFocusedBrowser();
        if(browserModel.isAdvancedHeaderDropdownSticky())
        {
            browserArea.getCorrespondingContentBrowser(browserModel).resize();
        }
    }


    public boolean update()
    {
        boolean success = false;
        if(this.initialized)
        {
            updateSearchGroups();
            updateSearchFields();
            updateSortFields();
            updateTypes();
            updateSelectedType();
            success = true;
        }
        else
        {
            success = initialize();
        }
        return success;
    }


    public AdvancedSearchParameterContainer getParameterContainer()
    {
        return getModel().getParameterContainer();
    }
}
