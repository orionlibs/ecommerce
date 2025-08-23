/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

/**
 * Class responsible for displaying given context attribute filter
 */
public final class FilterByAttributeUI implements SearchCriteriaModelObserver
{
    private final String attributeName;
    private final Component parent;
    private final boolean disabled;
    private boolean expanded;
    private Vlayout contextAttributeLayout;
    private Hlayout findParentSection;
    private Hlayout selectedAttributeContainer;
    private Textbox filterAttributeValuesTxt;
    private Listbox availableAttributeList;
    private Button ancestorsOnlyBtn;
    private Button clearParentBtn;
    private Textbox typeToSearchParentTxt;
    protected static final String ATTRIB_VAL_WIDTH = "250px";
    protected static final String CSS_DISABLED_LISTBOX = "disabled_listbox";
    private final SearchCriteriaModel searchCriteriaModel;
    private final DefaultCockpitConfigurationService cockpitConfigurationService;


    public FilterByAttributeUI(final String attributeName, final SearchCriteriaModel searchCriteriaModel,
                    final DefaultCockpitConfigurationService cockpitConfigurationService, final Component parent)
    {
        this(attributeName, searchCriteriaModel, cockpitConfigurationService, parent, false);
    }


    public FilterByAttributeUI(final String attributeName, final SearchCriteriaModel searchCriteriaModel,
                    final DefaultCockpitConfigurationService cockpitConfigurationService, final Component parent, final boolean disabled)
    {
        this.attributeName = attributeName;
        this.parent = parent;
        this.cockpitConfigurationService = cockpitConfigurationService;
        this.searchCriteriaModel = searchCriteriaModel;
        this.expanded = true;
        this.disabled = disabled;
    }


    public void render()
    {
        createMainLayout();
        createLabel();
        createSelectedAttributeValueContainer();
        createFilterAttribureValuesTxt();
        createAvailableAttributeList();
        createFindParentSection();
        createListenerOnListSelection();
        createListenerOnFilterValuesByName();
        createListenerOnFilterByParent();
        applySearchCriteria();
    }


    private void applySearchCriteria()
    {
        final String criteria = searchCriteriaModel.getSearchCriteria().get(attributeName);
        final Set<String> attributes = searchCriteriaModel.getAvailableAttributeValuesForAttribute(attributeName);
        if(criteria != null && attributes.contains(criteria))
        {
            handleNewValueForAttribute(criteria);
        }
    }


    protected void createMainLayout()
    {
        contextAttributeLayout = new Vlayout();
        contextAttributeLayout.setSclass("attribute-container");
        parent.appendChild(contextAttributeLayout);
    }


    protected void createLabel()
    {
        final Label attributeNameLabel = new Label(attributeName);
        attributeNameLabel.setSclass("attribute-name-label");
        contextAttributeLayout.appendChild(attributeNameLabel);
    }


    protected void createSelectedAttributeValueContainer()
    {
        selectedAttributeContainer = new Hlayout();
        selectedAttributeContainer.setSclass("selected-attribute-container");
        selectedAttributeContainer.setVisible(false);
        contextAttributeLayout.appendChild(selectedAttributeContainer);
    }


    protected void createFilterAttribureValuesTxt()
    {
        filterAttributeValuesTxt = new Textbox();
        filterAttributeValuesTxt.setWidth(ATTRIB_VAL_WIDTH);
        filterAttributeValuesTxt.setPlaceholder(Labels.getLabel("cockpit_config_filter.search") + " " + attributeName);
        filterAttributeValuesTxt.setInstant(true);
        filterAttributeValuesTxt.setSclass("attribute-filter-textbox");
        filterAttributeValuesTxt.addEventListener(Events.ON_OK,
                        event -> handleNewValueForAttribute(filterAttributeValuesTxt.getValue()));
        filterAttributeValuesTxt.setValue(searchCriteriaModel.getSearchCriteria().get(attributeName));
        filterAttributeValuesTxt.setDisabled(disabled);
        contextAttributeLayout.appendChild(filterAttributeValuesTxt);
    }


    protected void createAvailableAttributeList()
    {
        availableAttributeList = new Listbox();
        availableAttributeList
                        .setModel(new ListModelArray<>(searchCriteriaModel.getAvailableAttributeValuesForAttribute(attributeName).toArray()));
        availableAttributeList.setMultiple(false);
        availableAttributeList.setRows(3);
        availableAttributeList.setWidth(ATTRIB_VAL_WIDTH);
        availableAttributeList.setEmptyMessage(Labels.getLabel("cockpit_config_filter.no.attributes"));
        availableAttributeList.setDisabled(disabled);
        availableAttributeList.getItems().forEach(item -> item.setDisabled(disabled));
        contextAttributeLayout.appendChild(availableAttributeList);
    }


    protected void createFindParentSection()
    {
        if(!couldSearchByParent())
        {
            return;
        }
        findParentSection = new Hlayout();
        findParentSection.setSclass("attribute-find-parent");
        findParentSection.setWidth(ATTRIB_VAL_WIDTH);
        contextAttributeLayout.appendChild(findParentSection);
        ancestorsOnlyBtn = new Button();
        ancestorsOnlyBtn.setSclass("search-parent");
        ancestorsOnlyBtn.setDisabled(true);
        typeToSearchParentTxt = new Textbox();
        typeToSearchParentTxt.setWidth("180px");
        typeToSearchParentTxt.addEventListener(Events.ON_CHANGING,
                        (final InputEvent event) -> ancestorsOnlyBtn.setDisabled(StringUtils.isEmpty(event.getValue())));
        typeToSearchParentTxt.setPlaceholder(Labels.getLabel("cockpit_config_filter.find.parent.for"));
        typeToSearchParentTxt.setDisabled(disabled);
        findParentSection.appendChild(typeToSearchParentTxt);
        clearParentBtn = new Button();
        clearParentBtn.setSclass("clear-parent yo-delete-btn");
        clearParentBtn.addEventListener(Events.ON_CLICK, e -> {
            typeToSearchParentTxt.setText(StringUtils.EMPTY);
            ancestorsOnlyBtn.setDisabled(true);
            updateListModel();
        });
        findParentSection.appendChild(ancestorsOnlyBtn);
        findParentSection.appendChild(clearParentBtn);
    }


    public void createListenerOnListSelection()
    {
        availableAttributeList.addEventListener(Events.ON_SELECT, (SelectEvent event) -> {
            if(!event.getSelectedObjects().isEmpty())
            {
                final String selectedValue = (String)event.getSelectedObjects().iterator().next();
                handleNewValueForAttribute(selectedValue);
            }
        });
    }


    protected void handleNewValueForAttribute(final String selectedValue)
    {
        if(searchCriteriaModel.hasSearchCriteriaForAttribute(attributeName))
        {
            searchCriteriaModel.removeSearchCriteria(attributeName);
        }
        searchCriteriaModel.addSearchCriteria(attributeName, selectedValue);
        appendSelectedElement(selectedAttributeContainer, selectedValue);
        reactOnSelectionChange(true);
        availableAttributeList.clearSelection();
        updateListModel();
    }


    public void createListenerOnFilterValuesByName()
    {
        filterAttributeValuesTxt.addEventListener(Events.ON_CHANGE, e -> updateListModel());
    }


    public void createListenerOnFilterByParent()
    {
        if(couldSearchByParent())
        {
            ancestorsOnlyBtn.addEventListener(Events.ON_CLICK, ev -> updateListModel());
            typeToSearchParentTxt.addEventListener(Events.ON_OK, ev -> updateListModel());
        }
    }


    protected void appendSelectedElement(final Hlayout selectedAttribute, final String selectedValue)
    {
        selectedAttribute.setVisible(true);
        selectedAttribute.getChildren().clear();
        final Label label = new Label();
        label.setValue(selectedValue);
        label.setSclass("selected-attribute-value-label");
        selectedAttribute.appendChild(label);
        if(!disabled)
        {
            final Button remove = new Button();
            remove.setSclass("selected-attribute-value-remove yo-delete-btn");
            remove.setDisabled(false);
            remove.addEventListener(Events.ON_CLICK, e -> {
                searchCriteriaModel.removeSearchCriteria(attributeName);
                selectedAttribute.getChildren().clear();
                selectedAttribute.setVisible(false);
                reactOnSelectionChange(false);
                updateListModel();
            });
            selectedAttribute.appendChild(remove);
        }
    }


    protected void reactOnSelectionChange(final boolean selected)
    {
        filterAttributeValuesTxt.setDisabled(selected);
        UITools.modifySClass(availableAttributeList, CSS_DISABLED_LISTBOX, selected);
        if(couldSearchByParent())
        {
            ancestorsOnlyBtn.setDisabled(selected);
            clearParentBtn.setDisabled(selected);
            typeToSearchParentTxt.setDisabled(selected);
        }
        if(!expanded)
        {
            collapse();
        }
    }


    protected void updateListModel()
    {
        final Set<String> setAttrib = searchCriteriaModel.getAvailableAttributeValuesForAttribute(attributeName);
        if(setAttrib == null)
        {
            availableAttributeList.setModel(new ListModelList<>(Collections.emptyList()));
        }
        else
        {
            final List<String> availableAttributeValuesForAttribute = new ArrayList<>(setAttrib);
            final List<String> byParent = filterByParentAttribute(availableAttributeValuesForAttribute);
            final List<String> byParentAndName = filterByValueName(byParent);
            availableAttributeList.setModel(new ListModelList<Object>(byParentAndName));
            availableAttributeList.setDisabled(disabled);
            availableAttributeList.getItems().forEach(item -> item.setDisabled(disabled));
        }
    }


    protected List<String> filterByParentAttribute(final List<String> values)
    {
        final List<String> result = new ArrayList<>(values);
        if(couldSearchByParent())
        {
            final String typeToSearchParentTxtValue = typeToSearchParentTxt.getValue();
            if(StringUtils.isNotBlank(typeToSearchParentTxtValue))
            {
                final CockpitConfigurationContextStrategy strategy = cockpitConfigurationService.getContextStrategies()
                                .get(attributeName);
                if(strategy != null)
                {
                    final SetUniqueList parents = SetUniqueList.decorate(new ArrayList<>());
                    parents.add(typeToSearchParentTxtValue);
                    for(int i = 0; i < parents.size(); i++)
                    {
                        parents.addAll(strategy.getParentContexts((String)parents.get(i)));
                    }
                    result.retainAll(parents);
                }
                else
                {
                    result.clear();
                }
            }
        }
        return result;
    }


    protected List<String> filterByValueName(final List<String> values)
    {
        final String searchCondition = filterAttributeValuesTxt.getText();
        if(StringUtils.isNotBlank(searchCondition))
        {
            return values.stream().filter(s -> StringUtils.containsIgnoreCase(s, searchCondition)).collect(Collectors.toList());
        }
        else
        {
            return values;
        }
    }


    @Override
    public void notifyChanged(final String changedAttribute)
    {
        if(!StringUtils.equals(attributeName, changedAttribute))
        {
            updateListModel();
        }
    }


    protected boolean couldSearchByParent()
    {
        return cockpitConfigurationService.getContextStrategies().get(attributeName) != null;
    }


    public void collapse()
    {
        expanded = false;
        if(!searchCriteriaModel.hasSearchCriteriaForAttribute(attributeName))
        {
            contextAttributeLayout.setVisible(false);
        }
        else
        {
            filterAttributeValuesTxt.setVisible(false);
            availableAttributeList.setVisible(false);
            if(couldSearchByParent())
            {
                findParentSection.setVisible(false);
            }
        }
    }


    public void expand()
    {
        expanded = true;
        contextAttributeLayout.setVisible(true);
        filterAttributeValuesTxt.setVisible(true);
        availableAttributeList.setVisible(true);
        if(couldSearchByParent())
        {
            findParentSection.setVisible(true);
        }
    }
}
