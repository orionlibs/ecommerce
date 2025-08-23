package de.hybris.platform.adaptivesearchbackoffice.editors.facets;

import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.data.AsFacetVisibility;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetFiltersRequestData;
import de.hybris.platform.adaptivesearchbackoffice.data.FacetRequestData;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRenderer;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.MultiReferenceEditorLogic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class AsFacetValuesRenderer implements EditorRenderer<MultiReferenceEditorLogic<AbstractFacetConfigurationEditorData, AbstractAsFacetConfigurationModel>, AbstractFacetConfigurationEditorData>
{
    protected static final String FACET_VALUES_SCLASS = "yas-facet-values";
    protected static final String FACET_VALUES_TOP_SCLASS = "yas-facet-values-top";
    protected static final String FACET_VALUES_ALL_SCLASS = "yas-facet-values-all";
    protected static final String FACET_VALUE_SCLASS = "yas-facet-value";
    protected static final String FACET_VALUE_NAME_SCLASS = "yas-facet-value-name";
    protected static final String FACET_VALUE_COUNT_SCLASS = "yas-facet-value-count";
    protected static final String ACTIONS_SCLASS = "yas-actions";
    protected static final String ACTION_SCLASS = "yas-action";
    protected static final String STICKY_SCLASS = "yas-sticky";
    protected static final String FACET_VALUE_COUNT_LABEL = "adaptivesearch.facet.value.count";
    protected static final String FACET_VALUE_SHOW_MORE_LABEL = "adaptivesearch.facet.value.showMore";
    protected static final String FACET_VALUE_SHOW_LESS_LABEL = "adaptivesearch.facet.value.showLess";


    public boolean isEnabled(MultiReferenceEditorLogic logic)
    {
        return true;
    }


    public boolean canRender(MultiReferenceEditorLogic logic, Component parent, AbstractFacetConfigurationEditorData data)
    {
        if(data == null || data.getFacet() == null)
        {
            return false;
        }
        AsFacetData facet = data.getFacet();
        return (CollectionUtils.isNotEmpty(facet.getValues()) || CollectionUtils.isNotEmpty(facet.getSelectedValues()));
    }


    public void beforeRender(MultiReferenceEditorLogic<AbstractFacetConfigurationEditorData, AbstractAsFacetConfigurationModel> logic, Component parent, AbstractFacetConfigurationEditorData data)
    {
        Component item = logic.findEditorItem(parent);
        item.addEventListener("onOpen", event -> {
            OpenEvent openEvent = (OpenEvent)event;
            sendFacetRequest(logic, data, openEvent.isOpen() ? resolveFacetVisibility(data) : AsFacetVisibility.SHOW);
        });
    }


    public void render(MultiReferenceEditorLogic logic, Component parent, AbstractFacetConfigurationEditorData data)
    {
        Div facetValuesDiv = new Div();
        facetValuesDiv.setParent(parent);
        facetValuesDiv.setSclass("yas-facet-values");
        AsFacetVisibility facetVisibility = resolveFacetVisibility(data);
        boolean isFacetValuesOpen = (facetVisibility == AsFacetVisibility.SHOW_VALUES);
        logic.setOpen((Component)facetValuesDiv, isFacetValuesOpen);
        renderFacetValuesTop(logic, (Component)facetValuesDiv, data);
        renderFacetValuesAll(logic, (Component)facetValuesDiv, data);
        facetValuesDiv.addEventListener("onOpen", event -> {
            OpenEvent openEvent = (OpenEvent)event;
            sendFacetRequest(logic, data, openEvent.isOpen() ? AsFacetVisibility.SHOW_VALUES : AsFacetVisibility.SHOW_TOP_VALUES);
        });
    }


    protected void renderFacetValuesTop(MultiReferenceEditorLogic logic, Component facetValues, AbstractFacetConfigurationEditorData data)
    {
        AsFacetData facet = data.getFacet();
        if(facet != null && CollectionUtils.isNotEmpty(facet.getTopValues()))
        {
            Div facetValuesTopDiv = new Div();
            facetValuesTopDiv.setParent(facetValues);
            facetValuesTopDiv.setSclass("yas-facet-values-top");
            facetValuesTopDiv.setVisible(!logic.isOpen(facetValues));
            renderFacetValues(logic, (Component)facetValuesTopDiv, data, facet.getTopValues(), facet.getSelectedValues());
            Div actionsDiv = new Div();
            actionsDiv.setParent((Component)facetValuesTopDiv);
            actionsDiv.setSclass("yas-actions");
            Div showMoreActionDiv = new Div();
            showMoreActionDiv.setParent((Component)actionsDiv);
            showMoreActionDiv.setSclass("yas-action");
            A showMoreAction = new A();
            showMoreAction.setParent((Component)showMoreActionDiv);
            showMoreAction.setLabel(Labels.getLabel("adaptivesearch.facet.value.showMore"));
            facetValues.addEventListener("onOpen", event -> facetValuesTopDiv.setVisible(!((OpenEvent)event).isOpen()));
            showMoreAction.addEventListener("onClick", event -> logic.setOpen(facetValues, true));
        }
    }


    protected void renderFacetValuesAll(MultiReferenceEditorLogic logic, Component facetValues, AbstractFacetConfigurationEditorData data)
    {
        AsFacetData facet = data.getFacet();
        Div facetValuesAllDiv = new Div();
        facetValuesAllDiv.setParent(facetValues);
        facetValuesAllDiv.setSclass("yas-facet-values-all");
        facetValuesAllDiv.setVisible(logic.isOpen(facetValues));
        List<AsFacetValueData> values = (facet != null) ? facet.getValues() : Collections.<AsFacetValueData>emptyList();
        List<AsFacetValueData> selectedValues = (facet != null) ? facet.getSelectedValues() : Collections.<AsFacetValueData>emptyList();
        List<AsFacetValueData> topValues = (facet != null) ? facet.getTopValues() : Collections.<AsFacetValueData>emptyList();
        renderFacetValues(logic, (Component)facetValuesAllDiv, data, values, selectedValues);
        if(CollectionUtils.isNotEmpty(topValues))
        {
            Div actionsDiv = new Div();
            actionsDiv.setParent((Component)facetValuesAllDiv);
            actionsDiv.setSclass("yas-actions");
            Div showLessActionDiv = new Div();
            showLessActionDiv.setParent((Component)actionsDiv);
            showLessActionDiv.setSclass("yas-action");
            A showLessAction = new A();
            showLessAction.setParent((Component)showLessActionDiv);
            showLessAction.setLabel(Labels.getLabel("adaptivesearch.facet.value.showLess"));
            facetValues.addEventListener("onOpen", event -> facetValuesAllDiv.setVisible(((OpenEvent)event).isOpen()));
            showLessAction.addEventListener("onClick", event -> logic.setOpen(facetValues, false));
        }
    }


    protected void renderFacetValues(MultiReferenceEditorLogic logic, Component parent, AbstractFacetConfigurationEditorData data, List<AsFacetValueData> facetValues, List<AsFacetValueData> selectedFacetValues)
    {
        Listbox listbox = new Listbox();
        listbox.setParent(parent);
        listbox.setCheckmark(true);
        listbox.setModel((ListModel)createFacetValuesModel(facetValues, selectedFacetValues));
        listbox.setItemRenderer(this::renderFacetValue);
        listbox.setAttribute("org.zkoss.zul.listbox.rightSelect", Boolean.FALSE);
        listbox.addEventListener("onSelect", event -> {
            if(event instanceof SelectEvent)
            {
                SelectEvent selectEvent = (SelectEvent)event;
                Set<AsFacetValueData> selectedObjects = selectEvent.getSelectedObjects();
                sendFacetFilterRequest(logic, data, (List<String>)selectedObjects.stream().map(AsFacetValueData::getValue).collect(Collectors.toList()));
            }
        });
    }


    protected void renderFacetValue(Listitem listitem, Object data, int index)
    {
        AsFacetValueData facetValue = (AsFacetValueData)data;
        ListModel<AsFacetValueData> model = listitem.getListbox().getModel();
        int stickyValuesSize = ((FacetValuesListModel)model).getStickyValuesSize();
        if(index < stickyValuesSize)
        {
            listitem.setSclass("yas-facet-value yas-sticky");
        }
        else
        {
            listitem.setSclass("yas-facet-value");
        }
        Listcell listcell = new Listcell();
        listcell.setParent((Component)listitem);
        Label nameLabel = new Label();
        nameLabel.setParent((Component)listcell);
        nameLabel.setSclass("yas-facet-value-name");
        nameLabel.setValue(facetValue.getName());
        Label countLabel = new Label();
        countLabel.setParent((Component)listcell);
        countLabel.setSclass("yas-facet-value-count");
        countLabel.setValue(Labels.getLabel("adaptivesearch.facet.value.count", new Object[] {Long.valueOf(facetValue.getCount())}));
    }


    protected FacetValuesListModel createFacetValuesModel(List<AsFacetValueData> facetValues, List<AsFacetValueData> selectedFacetValues)
    {
        Map<String, AsFacetValueData> stickyFacetValues = new LinkedHashMap<>();
        if(CollectionUtils.isNotEmpty(selectedFacetValues))
        {
            for(AsFacetValueData facetValue : selectedFacetValues)
            {
                stickyFacetValues.put(facetValue.getValue(), facetValue);
            }
        }
        if(CollectionUtils.isNotEmpty(facetValues))
        {
            for(AsFacetValueData facetValue : facetValues)
            {
                stickyFacetValues.remove(facetValue.getValue());
            }
        }
        List<AsFacetValueData> data = new ArrayList<>();
        data.addAll(stickyFacetValues.values());
        data.addAll(facetValues);
        List<AsFacetValueData> selectedObjects = (List<AsFacetValueData>)data.stream().filter(AsFacetValueData::isSelected).collect(Collectors.toList());
        FacetValuesListModel model = new FacetValuesListModel(data, stickyFacetValues.size());
        model.setMultiple(true);
        model.setSelection(selectedObjects);
        return model;
    }


    protected AsFacetVisibility resolveFacetVisibility(AbstractFacetConfigurationEditorData data)
    {
        AsFacetData facet = data.getFacet();
        if(facet != null && facet.getVisibility() != AsFacetVisibility.SHOW_VALUES && CollectionUtils.isNotEmpty(facet.getTopValues()))
        {
            return AsFacetVisibility.SHOW_TOP_VALUES;
        }
        return AsFacetVisibility.SHOW_VALUES;
    }


    protected void sendFacetRequest(MultiReferenceEditorLogic logic, AbstractFacetConfigurationEditorData data, AsFacetVisibility facetVisibility)
    {
        FacetRequestData request = new FacetRequestData();
        request.setIndexProperty(data.getIndexProperty());
        request.setFacetVisibility(facetVisibility);
        logic.getWidgetInstanceManager().sendOutput("searchRequest", request);
    }


    protected void sendFacetFilterRequest(MultiReferenceEditorLogic logic, AbstractFacetConfigurationEditorData data, List<String> selectedFacetValues)
    {
        FacetFiltersRequestData request = new FacetFiltersRequestData();
        request.setIndexProperty(data.getIndexProperty());
        request.setValues(selectedFacetValues);
        logic.getWidgetInstanceManager().sendOutput("searchRequest", request);
    }
}
