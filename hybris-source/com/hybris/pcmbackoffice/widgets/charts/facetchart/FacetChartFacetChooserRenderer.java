package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class FacetChartFacetChooserRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(FacetChartFacetChooserRenderer.class);
    private static final String LABEL_FACET_CHOOSER = "solrchart.facetchooserrenderer.label";
    private static final String FACET_SELECTED_INDEX = "selectedFacetIndex";
    private WidgetInstanceManager widgetInstanceManager;
    private Consumer<String> facetChange;
    private List<FacetData> availableFacets;


    public void render(Div parent, WidgetInstanceManager widgetInstanceManager, Consumer<String> facetChange, List<FacetData> availableFacets)
    {
        this.facetChange = facetChange;
        this.widgetInstanceManager = widgetInstanceManager;
        this.availableFacets = availableFacets;
        if(availableFacets.size() < 2)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No facet is set, add some facet to configuration");
            }
            return;
        }
        parent.getChildren().clear();
        createLabel(parent);
        createCombobox(parent);
    }


    private void createLabel(Div parent)
    {
        Label label = new Label(this.widgetInstanceManager.getLabel("solrchart.facetchooserrenderer.label"));
        label.setParent((Component)parent);
    }


    private void createCombobox(Div parent)
    {
        Combobox combobox = new Combobox();
        this.availableFacets.forEach(facet -> {
            Comboitem comboitem = new Comboitem(facet.getDisplayName());
            comboitem.setValue(facet.getName());
            combobox.getChildren().add(comboitem);
        });
        combobox.setSelectedIndex(getSelectedIndex().intValue());
        combobox.setSclass("yw-solrfacetchart-combobox");
        combobox.setReadonly(true);
        combobox.addEventListener("onSelect", this::onSelectFacet);
        combobox.setParent((Component)parent);
    }


    void onSelectFacet(SelectEvent<Comboitem, String> event)
    {
        Set<Comboitem> selectedItems = event.getSelectedItems();
        if(selectedItems.isEmpty())
        {
            return;
        }
        Comboitem selectedItem = selectedItems.iterator().next();
        storeSelectedIndex(Integer.valueOf(selectedItem.getIndex()));
        this.facetChange.accept((String)selectedItem.getValue());
    }


    private Integer getSelectedIndex()
    {
        Integer selectedFacetIndex = (Integer)this.widgetInstanceManager.getModel().getValue("selectedFacetIndex", Integer.class);
        if(selectedFacetIndex == null)
        {
            selectedFacetIndex = Integer.valueOf(0);
        }
        return selectedFacetIndex;
    }


    private void storeSelectedIndex(Integer selectedItem)
    {
        this.widgetInstanceManager.getModel().setValue("selectedFacetIndex", selectedItem);
    }
}
