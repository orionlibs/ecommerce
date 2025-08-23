package de.hybris.platform.adaptivesearchbackoffice.editors.facets;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractPanel;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaPanelRenderer;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetValueModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel;
import de.hybris.platform.adaptivesearchbackoffice.components.ActionsMenu;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractFacetConfigurationEditorData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class AsFacetValuesPanelRenderer extends AbstractEditorAreaPanelRenderer<AbstractAsFacetConfigurationModel>
{
    protected static final String FACET_VALUE_LABEL = "adaptivesearch.facet.value.label";
    protected static final String FACET_VALUES_PANEL_SCLASS = "yas-facet-values-panel";
    protected static final String STICKY_SCLASS = "yas-sticky";
    protected static final String FACET_VALUE_SCLASS = "yas-facet-value";
    protected static final String FACET_VALUE_NAME_SCLASS = "yas-facet-value-name";
    protected static final String FACET_VALUE_COUNT_SCLASS = "yas-facet-value-count";
    protected static final String ACTIONS_SCLASS = "yas-actions";
    protected static final String FACET_VALUE_COUNT_LABEL = "adaptivesearch.facet.value.count";
    protected static final String ACTIONS_CONTEXT = "as-facet-values-panel-actions";
    protected static final String LAST_INPUT_KEY = "lastInput";
    protected static final String EDITOR_DATA_KEY = "editorData";
    protected static final String PROMOTED_OBJECT_EXPRESSION = "currentObject.promotedValues";
    protected static final String EXCLUDED_OBJECT_EXPRESSION = "currentObject.excludedValues";
    protected static final int MAX_ITERATIONS = 3;


    public void render(Component component, AbstractPanel abstractPanel, AbstractAsFacetConfigurationModel facetConfiguration, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        List<AsFacetValueData> facetValues = resolveFacetValues(widgetInstanceManager.getWidgetslot(), 0);
        if(CollectionUtils.isEmpty(facetValues))
        {
            return;
        }
        Div panel = new Div();
        panel.setParent(component);
        panel.setSclass("yas-facet-values-panel");
        Label label = new Label(Labels.getLabel("adaptivesearch.facet.value.label"));
        label.setParent((Component)panel);
        Listbox listbox = new Listbox();
        listbox.setParent((Component)panel);
        listbox.setItemRenderer((item, data, index) -> renderItem(item, (FacetValueModel)data, widgetInstanceManager));
        listbox.setModel(createListModel(widgetInstanceManager, facetValues));
        widgetInstanceManager.getModel().addObserver("currentObject.promotedValues", () -> updateList(listbox, widgetInstanceManager));
        widgetInstanceManager.getModel().addObserver("currentObject.excludedValues", () -> updateList(listbox, widgetInstanceManager));
    }


    protected void updateList(Listbox listbox, WidgetInstanceManager widgetInstanceManager)
    {
        List<AsFacetValueData> facetValues = resolveFacetValues(widgetInstanceManager.getWidgetslot(), 0);
        listbox.setModel(createListModel(widgetInstanceManager, facetValues));
    }


    protected void renderItem(Listitem item, FacetValueModel model, WidgetInstanceManager widgetInstanceManager)
    {
        AsFacetValueData data = model.getData();
        String displayedName = StringUtils.isBlank(data.getName()) ? ("[" + data.getValue() + "]") : data.getName();
        String displayedValue = "[" + data.getValue() + "]";
        item.setValue(model);
        item.setSclass("yas-facet-value");
        if(model.isSticky())
        {
            item.setSclass("yas-sticky");
        }
        Listcell listcell = new Listcell();
        listcell.setParent((Component)item);
        listcell.setTooltiptext(displayedValue);
        Label nameLabel = new Label();
        nameLabel.setParent((Component)listcell);
        nameLabel.setSclass("yas-facet-value-name");
        nameLabel.setValue(displayedName);
        Label countLabel = new Label();
        countLabel.setParent((Component)listcell);
        countLabel.setSclass("yas-facet-value-count");
        countLabel.setValue(Labels.getLabel("adaptivesearch.facet.value.count", new Object[] {Long.valueOf(data.getCount())}));
        Listcell action = new Listcell();
        action.setSclass("yas-actions");
        action.setParent((Component)item);
        renderActions((Component)action, model, widgetInstanceManager);
    }


    protected void renderActions(Component parent, FacetValueModel data, WidgetInstanceManager widgetInstanceManager)
    {
        ActionsMenu actionsMenu = new ActionsMenu();
        actionsMenu.setInputValue(data);
        actionsMenu.setConfig("as-facet-values-panel-actions");
        actionsMenu.setWidgetInstanceManager(widgetInstanceManager);
        actionsMenu.setAttribute("widgetInstanceManager", widgetInstanceManager);
        actionsMenu.initialize();
        actionsMenu.setParent(parent);
    }


    protected ListModel createListModel(WidgetInstanceManager widgetInstanceManager, List<AsFacetValueData> facetValues)
    {
        AbstractAsFacetConfigurationModel facetConfiguration = (AbstractAsFacetConfigurationModel)widgetInstanceManager.getModel().getValue("currentObject", AbstractAsFacetConfigurationModel.class);
        LinkedHashMap<String, AbstractAsFacetValueConfigurationModel> stickyValuesMapping = createStickyValuesMapping(facetConfiguration);
        LinkedHashMap<String, AsFacetValueData> valuesMapping = createValuesMapping(facetValues);
        List<AsPromotedFacetValueModel> promotedValues = facetConfiguration.getPromotedValues();
        for(AsPromotedFacetValueModel promotedValue : promotedValues)
        {
            valuesMapping.remove(promotedValue.getValue());
            stickyValuesMapping.remove(promotedValue.getValue());
        }
        List<AsExcludedFacetValueModel> excludedValues = facetConfiguration.getExcludedValues();
        for(AsExcludedFacetValueModel excludedValue : excludedValues)
        {
            valuesMapping.remove(excludedValue.getValue());
            stickyValuesMapping.remove(excludedValue.getValue());
        }
        ArrayList<FacetValueModel> values = new ArrayList<>();
        for(AbstractAsFacetValueConfigurationModel facetValueConfiguration : stickyValuesMapping.values())
        {
            FacetValueModel value;
            AsFacetValueData facetValue = valuesMapping.remove(facetValueConfiguration.getValue());
            if(facetValue != null)
            {
                value = convertFacetValue(facetValue);
            }
            else
            {
                value = convertFacetValueConfiguration(facetValueConfiguration);
            }
            value.setSticky(true);
            value.setModel(facetValueConfiguration);
            values.add(value);
        }
        for(AsFacetValueData facetValue : valuesMapping.values())
        {
            values.add(convertFacetValue(facetValue));
        }
        return (ListModel)new ListModelList(values, true);
    }


    protected LinkedHashMap<String, AbstractAsFacetValueConfigurationModel> createStickyValuesMapping(AbstractAsFacetConfigurationModel facetConfiguration)
    {
        LinkedHashMap<String, AbstractAsFacetValueConfigurationModel> stickyValuesMapping = new LinkedHashMap<>();
        List<AsPromotedFacetValueModel> originalPromotedValues = (List<AsPromotedFacetValueModel>)facetConfiguration.getItemModelContext().getOriginalValue("promotedValues");
        if(CollectionUtils.isNotEmpty(originalPromotedValues))
        {
            for(AbstractAsFacetValueConfigurationModel promotedValue : originalPromotedValues)
            {
                stickyValuesMapping.put(promotedValue.getValue(), promotedValue);
            }
        }
        List<AsExcludedFacetValueModel> originalExcludedValues = (List<AsExcludedFacetValueModel>)facetConfiguration.getItemModelContext().getOriginalValue("excludedValues");
        if(CollectionUtils.isNotEmpty(originalExcludedValues))
        {
            for(AbstractAsFacetValueConfigurationModel excludedValue : originalExcludedValues)
            {
                stickyValuesMapping.put(excludedValue.getValue(), excludedValue);
            }
        }
        return stickyValuesMapping;
    }


    protected LinkedHashMap<String, AsFacetValueData> createValuesMapping(List<AsFacetValueData> facetValues)
    {
        LinkedHashMap<String, AsFacetValueData> valuesMapping = new LinkedHashMap<>();
        if(CollectionUtils.isNotEmpty(facetValues))
        {
            for(AsFacetValueData facetValue : facetValues)
            {
                valuesMapping.put(facetValue.getValue(), facetValue);
            }
        }
        return valuesMapping;
    }


    protected FacetValueModel convertFacetValue(AsFacetValueData facetValue)
    {
        FacetValueModel target = new FacetValueModel();
        target.setData(facetValue);
        return target;
    }


    protected FacetValueModel convertFacetValueConfiguration(AbstractAsFacetValueConfigurationModel facetValueConfiguration)
    {
        AsFacetValueData facetValue = new AsFacetValueData();
        facetValue.setValue(facetValueConfiguration.getValue());
        FacetValueModel target = new FacetValueModel();
        target.setData(facetValue);
        return target;
    }


    protected List<AsFacetValueData> resolveFacetValues(Widgetslot widgetslot, int iterationNumber)
    {
        if(iterationNumber >= 3)
        {
            return Collections.emptyList();
        }
        Widgetslot parentWidgetslot = WidgetTreeUIUtils.getParentWidgetslot((Component)widgetslot);
        TypeAwareSelectionContext selectionContext = (TypeAwareSelectionContext)parentWidgetslot.getViewModel().getValue("lastInput", TypeAwareSelectionContext.class);
        if(selectionContext == null)
        {
            return resolveFacetValues(parentWidgetslot, iterationNumber + 1);
        }
        Object editorData = selectionContext.getParameters().get("editorData");
        if(editorData instanceof AbstractFacetConfigurationEditorData)
        {
            AsFacetData facet = ((AbstractFacetConfigurationEditorData)editorData).getFacet();
            if(facet == null)
            {
                return Collections.emptyList();
            }
            return CollectionUtils.isNotEmpty(facet.getAllValues()) ? facet.getAllValues() : facet.getValues();
        }
        return resolveFacetValues(parentWidgetslot, iterationNumber + 1);
    }
}
