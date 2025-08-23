/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl.facet;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import com.hybris.cockpitng.util.UITools;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.VisibilityChangeEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultFacetRenderer
{
    public static final String I18N_FACET_SUPPORT_APPLIED_FACET_LABEL = "facetSupport.appliedFacet.label";
    public static final String SCLASS_YW_ADD_FACET = "yw-add-facet";
    public static final String SCLASS_YW_ADD_FACET_POPUP_OPENED = "yw-add-facet-opened";
    public static final String SCLASS_YW_SELECTED_FACET = "yw-selected-facet";
    public static final String SCLASS_YW_ADD_FACET_POPUP = "yw-add-facet-popup";
    public static final String SCLASS_YW_ADD_FACET_CONTAINER = "yw-add-facet-container";
    public static final String SCLASS_YW_SELECTED_FACETS = "yw-selected-facets";
    public static final String SCLASS_YW_SELECTED_FACET_VALUE_INLINE = "yw-selected-facet-value-inline";
    public static final String SCLASS_YW_REMOVE_FACET_INLINE = "yw-remove-facet-inline";
    public static final String MODEL_FACETS = "facets";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFacetRenderer.class);
    private WidgetInstanceManager widgetInstanceManager;
    private ManageFacetPopup popup;
    private Consumer<Collection<FacetData>> triggerSearch;


    public void initialize(final Component parent, final WidgetInstanceManager widgetInstanceManager,
                    final Consumer<Collection<FacetData>> triggerSearch)
    {
        this.triggerSearch = triggerSearch;
        this.widgetInstanceManager = widgetInstanceManager;
        reset();
        prepareAddFacetComponent(parent);
    }


    protected void prepareAddFacetComponent(final Component parent)
    {
        final Div buttonContainer = new Div();
        UITools.modifySClass(buttonContainer, SCLASS_YW_ADD_FACET_CONTAINER, true);
        final Div selectedFacetContainer = new Div();
        UITools.modifySClass(selectedFacetContainer, SCLASS_YW_SELECTED_FACETS, true);
        final Button button = new Button();
        UITools.modifySClass(button, SCLASS_YW_ADD_FACET, true);
        popup = new ManageFacetPopup(getWidgetInstanceManager(), appliedFacets -> {
            applySelectedFacets(appliedFacets, selectedFacetContainer);
            return null;
        });
        UITools.modifySClass(popup, SCLASS_YW_ADD_FACET_POPUP, true);
        popup.setParent(buttonContainer);
        button.addEventListener(Events.ON_CLICK, event -> {
            if(popup.isVisible())
            {
                popup.setVisible(false);
            }
            else
            {
                popup.setParent(buttonContainer);
                popup.showPopup();
            }
        });
        popup.addEventListener(Events.ON_VISIBILITY_CHANGE, (EventListener<VisibilityChangeEvent>)event -> UITools
                        .modifySClass(button, SCLASS_YW_ADD_FACET_POPUP_OPENED, !event.isHidden()));
        buttonContainer.appendChild(button);
        parent.appendChild(buttonContainer);
        parent.appendChild(selectedFacetContainer);
        renderSelectedFacets(popup.getSelectedFacetData(), selectedFacetContainer);
    }


    protected void applySelectedFacets(final Collection<FacetData> appliedFacets, final Component selectedFacetContainer)
    {
        renderSelectedFacets(appliedFacets, selectedFacetContainer);
        triggerSearch.accept(appliedFacets);
    }


    protected void renderSelectedFacets(final Collection<FacetData> appliedFacets, final Component parent)
    {
        parent.getChildren().clear();
        for(final FacetData facetData : appliedFacets)
        {
            facetData.getFacetValues().stream() //
                            .filter(FacetValueData::isSelected) //
                            .forEach(value -> renderFacet(parent, facetData, value));
        }
    }


    private void renderFacet(final Component parent, final FacetData facetData, final FacetValueData value)
    {
        final Div facetValueContainer = new Div();
        UITools.modifySClass(facetValueContainer, SCLASS_YW_SELECTED_FACET_VALUE_INLINE, true);
        final String labelText = getWidgetInstanceManager().getLabel(I18N_FACET_SUPPORT_APPLIED_FACET_LABEL, new Object[]
                        {facetData.getDisplayName(), value.getDisplayName()});
        final Label label = new Label(labelText);
        facetValueContainer.appendChild(label);
        final Div removeFacet = new Div();
        UITools.modifySClass(removeFacet, SCLASS_YW_REMOVE_FACET_INLINE, true);
        removeFacet.addEventListener(Events.ON_CLICK, click -> {
            if(popup.removeSelectedFacetValue(facetData, value))
            {
                facetValueContainer.detach();
            }
            else
            {
                LOG.warn("Could not find facet: {} -> {}", facetData, value);
            }
        });
        facetValueContainer.appendChild(removeFacet);
        parent.appendChild(facetValueContainer);
    }


    public void reset()
    {
        if(popup != null)
        {
            popup.reset();
        }
    }


    public void clearAllFacets(final Boolean isDisabledAutoSearch)
    {
        if(popup != null)
        {
            popup.clearAllFacets(isDisabledAutoSearch);
            popup.reset();
        }
        final Collection<FacetData> availableFacets = getAvailableFacets();
        if(availableFacets != null && isDisabledAutoSearch)
        {
            availableFacets.clear();
        }
    }


    public void adjustFacets(final Collection<FacetData> facets)
    {
        widgetInstanceManager.getModel().setValue(MODEL_FACETS, facets);
        if(CollectionUtils.isEmpty(facets))
        {
            popup.close();
        }
        else
        {
            popup.adjustFacets(facets);
        }
    }


    public Collection<FacetData> getAvailableFacets()
    {
        return widgetInstanceManager != null ? widgetInstanceManager.getModel().getValue(MODEL_FACETS, Collection.class)
                        : Collections.emptyList();
    }


    public Map<String, Set<String>> getSelectedFacets()
    {
        final Map<String, Set<String>> result = new HashMap<>();
        final Map<String, Set<ManageFacetPopup.FacetSelectionStatus>> status = popup.getStatus();
        status.keySet().forEach(element -> {
            final Set<String> values = status.get(element).stream().filter(ManageFacetPopup.FacetSelectionStatus::isSelected)
                            .map((facetSelectionStatus) -> facetSelectionStatus.getFacetValue().getName()).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(values))
            {
                result.put(element, values);
            }
        });
        return result;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }
}
