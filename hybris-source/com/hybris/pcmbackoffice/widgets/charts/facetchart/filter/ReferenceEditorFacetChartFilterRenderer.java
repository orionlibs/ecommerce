package com.hybris.pcmbackoffice.widgets.charts.facetchart.filter;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.pcmbackoffice.widgets.charts.facetchart.FacetChartFiltersRenderer;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class ReferenceEditorFacetChartFilterRenderer implements FacetChartFiltersRenderer
{
    private static final String SCLASS_BUTTON_REMOVE_FILTER = "ye-text-button ye-delete-btn";
    private WidgetInstanceManager widgetInstanceManager;
    private BiConsumer<String, Set<String>> facetSelectionChange;
    private PermissionFacade permissionFacade;
    private ReferenceEditorFacetChartFilterAdapter adapter;
    private String facetName;
    private String labelFacetName;
    private String filterTypeName;
    private String editorId;
    private String selectedFilterValue;
    private int order;


    public void renderFilters(WidgetInstanceManager widgetInstanceManager, Div filterContainer, BiConsumer<String, Set<String>> facetSelectionChange)
    {
        this.widgetInstanceManager = widgetInstanceManager;
        this.facetSelectionChange = facetSelectionChange;
        this.editorId = "facet_chart_filter_editor_id_" + this.facetName;
        this.selectedFilterValue = "selectedFilter_" + this.facetName;
        Div container = new Div();
        filterContainer.appendChild((Component)container);
        appendFilterNameLabel(container);
        appendRemoveFilterButton(container);
        appendChosenBox(container);
    }


    public void onFilterDeleted(CockpitEvent event, Executable onDeletedCallback)
    {
        this.adapter.deleteFilter(event, this.selectedFilterValue, this.widgetInstanceManager, onDeletedCallback);
        Collection<?> selectedValues = (Collection)this.widgetInstanceManager.getModel().getValue(this.selectedFilterValue, Collection.class);
        if(canAcceptFacetSelectionChange(event) && selectedValues != null)
        {
            Set<String> selected = convertToFacetValues(selectedValues);
            this.facetSelectionChange.accept(this.facetName, selected);
        }
    }


    protected boolean canAcceptFacetSelectionChange(CockpitEvent event)
    {
        return (event != null && event.getData() != null && event.getData() instanceof Collection &&
                        !((Collection)event.getData()).isEmpty());
    }


    protected void appendFilterNameLabel(Div filterContainer)
    {
        Label catalogVersionLabel = new Label(Labels.getLabel(this.labelFacetName));
        catalogVersionLabel.setParent((Component)filterContainer);
    }


    protected void appendRemoveFilterButton(Div filterContainer)
    {
        Button removeFilterButton = new Button();
        removeFilterButton.setClass("ye-text-button ye-delete-btn");
        removeFilterButton.addEventListener("onClick", this::removeAllFilters);
        removeFilterButton.setParent((Component)filterContainer);
    }


    protected void removeAllFilters(Event event)
    {
        storeSelectedFilters(Collections.emptySet());
        findEditor(event).ifPresent(e -> e.setValue(null));
        this.facetSelectionChange.accept(this.facetName, Collections.emptySet());
    }


    protected Optional<Editor> findEditor(Event event)
    {
        return event.getTarget().getParent().getChildren().stream().filter(d -> d.getId().equals(this.editorId)).map(e -> (Editor)e)
                        .findFirst();
    }


    protected void appendChosenBox(Div filterContainer)
    {
        Editor editor = createEditor();
        filterContainer.appendChild((Component)editor);
    }


    protected Editor createEditor()
    {
        String editorValueType = "MultiReference-SET(" + this.filterTypeName + ")";
        Editor editor = (new EditorBuilder(this.widgetInstanceManager)).setReadOnly(false).setValueType(editorValueType).setValueCreationEnabled(false).setOptional(false).setPermissionFacade(this.permissionFacade).build();
        editor.setId(this.editorId);
        editor.addEventListener("onValueChanged", this::filterChanged);
        editor.setValue(restoreFiltersFromModel());
        return editor;
    }


    protected void filterChanged(Event event)
    {
        storeSelectedFilters(event.getData());
        Set<String> selected = convertToFacetValues(event.getData());
        this.facetSelectionChange.accept(this.facetName, selected);
    }


    protected Set<String> convertToFacetValues(Object data)
    {
        if(data instanceof Collection)
        {
            Objects.requireNonNull(this.adapter);
            return (Set<String>)((Collection)data).stream().map(this.adapter::convertToFacetValue).collect(Collectors.toSet());
        }
        return Collections.singleton(this.adapter.convertToFacetValue(data));
    }


    protected void storeSelectedFilters(Object selectedFilters)
    {
        this.widgetInstanceManager.getModel().setValue(this.selectedFilterValue, selectedFilters);
    }


    protected Collection restoreFiltersFromModel()
    {
        Collection<?> selectedValues = (Collection)this.widgetInstanceManager.getModel().getValue(this.selectedFilterValue, Collection.class);
        if(selectedValues != null && !selectedValues.isEmpty())
        {
            return selectedValues;
        }
        return Collections.emptySet();
    }


    public int getOrder()
    {
        return this.order;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setFacetName(String facetName)
    {
        this.facetName = facetName;
    }


    @Required
    public void setLabelFacetName(String labelFacetName)
    {
        this.labelFacetName = labelFacetName;
    }


    @Required
    public void setFilterTypeName(String filterTypeName)
    {
        this.filterTypeName = filterTypeName;
    }


    @Required
    public void setAdapter(ReferenceEditorFacetChartFilterAdapter adapter)
    {
        this.adapter = adapter;
    }


    @Required
    public void setOrder(int order)
    {
        this.order = order;
    }
}
