/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.refineby.renderer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;

public class StandardFacetRenderer implements FacetRenderer
{
    public static final String WIDGET_SETTING_MAX_INLINE_FACETS = "maxInlineFacets";
    public static final String YW_TOGGLE_FACETS = "y-btn-transparent";
    public static final String YW_FACET_NAME = "yw-facet-name";
    public static final String YW_FACET_VALUE_CONTAINER = "yw-facet-value-container";
    public static final String YW_FACET_VALUE = "yw-facet-value";
    public static final String I18N_SHOW_ALL_VALUES = "show.all.values";
    public static final String I18N_SHOW_LESS_VALUES = "show.less.values";
    public static final String PREFIX_TOP = "top-";
    public static final String PREFIX_EXPANDED = "expanded-";
    private String selectedFacetsModelKey;
    private String expandedFacetsModelKey;


    @Override
    public void renderFacet(final Component parent, final FacetData facet, final boolean selected, final WidgetInstanceManager wim,
                    final Context context)
    {
        if(!shouldFacetBeRendered(facet, wim))
        {
            return;
        }
        final Label facetLabel = createFacetLabel(calculateFacetLabel(facet, wim));
        parent.appendChild(facetLabel);
        final Div facetValueContainer = new Div();
        facetValueContainer.setSclass(YW_FACET_VALUE_CONTAINER);
        parent.appendChild(facetValueContainer);
        final Set<String> selectedFacets = findSelectedFacets(facet.getName(), wim);
        final int renderedFacetValues = renderVisibleFacetValues(facet, wim, facetValueContainer, selectedFacets);
        final int numberOfFacetValues = facet.getFacetValues().size();
        if(numberOfFacetValues > renderedFacetValues)
        {
            final HtmlBasedComponent expandedCheckboxParent = new Div();
            expandedCheckboxParent.setSclass(YW_FACET_VALUE_CONTAINER);
            expandedCheckboxParent.setVisible(false);
            parent.appendChild(expandedCheckboxParent);
            facet.getFacetValues().forEach(facetValue -> {
                final Component checkbox = createSingleValue(facet, facetValue, selectedFacets.contains(facetValue.getName()), wim);
                expandedCheckboxParent.appendChild(checkbox);
            });
            final Button showAllButton = createShowAllButton(facet.getName(), numberOfFacetValues, facetValueContainer,
                            expandedCheckboxParent, wim);
            facetValueContainer.appendChild(showAllButton);
            final Button showLessButton = createShowLessButton(facet.getName(), facetValueContainer, expandedCheckboxParent, wim);
            expandedCheckboxParent.appendChild(showLessButton);
        }
    }


    protected int renderVisibleFacetValues(final FacetData facet, final WidgetInstanceManager wim, final Div facetValueContainer,
                    final Set<String> selectedFacets)
    {
        final int maxValuesInline = calculateMaxValuesInline(wim);
        final Set<String> facetValuesToRender = findFacetValuesToRender(facet.getFacetValues(), selectedFacets, maxValuesInline);
        facet.getFacetValues().stream().filter(facetValue -> facetValuesToRender.contains(facetValue.getName()))
                        .map(facetValue -> createSingleValue(facet, facetValue, selectedFacets.contains(facetValue.getName()), wim))
                        .forEach(facetValueContainer::appendChild);
        return facetValuesToRender.size();
    }


    protected Set<String> findFacetValuesToRender(final Collection<FacetValueData> allFacetValues,
                    final Set<String> selectedFacetValues, final int maxNumberOfFacets)
    {
        final Set<String> facetValuesToRender = allFacetValues.stream().filter(fv -> !selectedFacetValues.contains(fv.getName()))
                        .limit(Math.max(maxNumberOfFacets - selectedFacetValues.size(), 0)).map(FacetValueData::getName)
                        .collect(Collectors.toSet());
        facetValuesToRender.addAll(selectedFacetValues);
        return facetValuesToRender;
    }


    /**
     * @deprecated Please use new implementation of the method
     *             {@link StandardFacetRenderer#createShowLessButton(String, HtmlBasedComponent, HtmlBasedComponent, WidgetInstanceManager)}
     * @since 2205
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Label createShowLessLabel(final String facetName, final HtmlBasedComponent checkboxParent,
                    final HtmlBasedComponent expandedCheckboxParent, final WidgetInstanceManager wim)
    {
        final Label showLess = new Label(wim.getLabel(I18N_SHOW_LESS_VALUES));
        showLess.setSclass(YW_TOGGLE_FACETS);
        showLess.addEventListener(Events.ON_CLICK, e -> {
            checkboxParent.setVisible(true);
            expandedCheckboxParent.setVisible(false);
            expandOrCollapseFacet(facetName, wim);
        });
        if(findExpandedFacets(wim).contains(facetName))
        {
            checkboxParent.setVisible(false);
            expandedCheckboxParent.setVisible(true);
        }
        return showLess;
    }


    protected Button createShowLessButton(final String facetName, final HtmlBasedComponent checkboxParent,
                    final HtmlBasedComponent expandedCheckboxParent, final WidgetInstanceManager wim)
    {
        final var showLess = new Button(wim.getLabel(I18N_SHOW_LESS_VALUES));
        showLess.setSclass(YW_TOGGLE_FACETS);
        showLess.addEventListener(Events.ON_CLICK, e -> {
            checkboxParent.setVisible(true);
            expandedCheckboxParent.setVisible(false);
            expandOrCollapseFacet(facetName, wim);
        });
        if(findExpandedFacets(wim).contains(facetName))
        {
            checkboxParent.setVisible(false);
            expandedCheckboxParent.setVisible(true);
        }
        return showLess;
    }


    /**
     * @deprecated Please use new implementation of the method
     *             {@link StandardFacetRenderer#createShowAllButton(String, int, HtmlBasedComponent, HtmlBasedComponent, WidgetInstanceManager)}
     * @since 2205
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Label createShowAllLabel(final String facetName, final int numberOfFacets, final HtmlBasedComponent checkboxParent,
                    final HtmlBasedComponent expandedCheckboxParent, final WidgetInstanceManager wim)
    {
        final Label showAll = new Label(wim.getLabel(I18N_SHOW_ALL_VALUES, new Object[]
                        {numberOfFacets}));
        showAll.setSclass(YW_TOGGLE_FACETS);
        showAll.addEventListener(Events.ON_CLICK, e -> {
            checkboxParent.setVisible(false);
            expandedCheckboxParent.setVisible(true);
            expandOrCollapseFacet(facetName, wim);
        });
        return showAll;
    }


    protected Button createShowAllButton(final String facetName, final int numberOfFacets, final HtmlBasedComponent checkboxParent,
                    final HtmlBasedComponent expandedCheckboxParent, final WidgetInstanceManager wim)
    {
        final var showAll = new Button(wim.getLabel(I18N_SHOW_ALL_VALUES, new Object[]
                        {numberOfFacets}));
        showAll.setSclass(YW_TOGGLE_FACETS);
        showAll.addEventListener(Events.ON_CLICK, e -> {
            checkboxParent.setVisible(false);
            expandedCheckboxParent.setVisible(true);
            expandOrCollapseFacet(facetName, wim);
        });
        return showAll;
    }


    protected Label createFacetLabel(final String label)
    {
        final Label facetName = new Label(label);
        facetName.setSclass(YW_FACET_NAME);
        return facetName;
    }


    protected Component createSingleValue(final FacetData facet, final FacetValueData facetValueData, final Boolean isChecked,
                    final WidgetInstanceManager wim)
    {
        final Checkbox check = new Checkbox(calculateFacetValueLabel(facetValueData, wim));
        check.setSclass(YW_FACET_VALUE);
        check.setChecked(isChecked);
        check.addEventListener(Events.ON_CHECK, e -> select(facet.getName(), facetValueData.getName(), wim));
        return check;
    }


    protected boolean shouldFacetBeRendered(final FacetData facet, final WidgetInstanceManager wim)
    {
        final Set<String> selectedFacets = findSelectedFacets(facet.getName(), wim);
        return !(facet.getFacetValues().isEmpty()
                        || (facet.getFacetValues().size() == 1 && !selectedFacets.contains(getFirstFacetValue(facet))));
    }


    protected String getFirstFacetValue(final FacetData facet)
    {
        return facet.getFacetValues().iterator().next().getName();
    }


    protected Set<String> findSelectedFacets(final String facetName, final WidgetInstanceManager wim)
    {
        Map<String, Set<String>> selection = wim.getModel().getValue(getSelectedFacetsModelKey(), Map.class);
        if(selection == null)
        {
            selection = Maps.newHashMap();
            wim.getModel().setValue(getSelectedFacetsModelKey(), selection);
        }
        return selection.computeIfAbsent(facetName, fn -> Sets.newHashSet());
    }


    protected void select(final String facetName, final String facetValue, final WidgetInstanceManager wim)
    {
        final Set<String> selectedFacets = findSelectedFacets(facetName, wim);
        if(selectedFacets.contains(facetValue))
        {
            selectedFacets.remove(facetValue);
        }
        else
        {
            selectedFacets.add(facetValue);
        }
        final Map selection = wim.getModel().getValue(getSelectedFacetsModelKey(), Map.class);
        wim.getModel().setValue(getSelectedFacetsModelKey(), selection);
    }


    protected Set findExpandedFacets(final WidgetInstanceManager wim)
    {
        Set expandedFacets = wim.getModel().getValue(getExpandedFacetsModelKey(), Set.class);
        if(expandedFacets == null)
        {
            expandedFacets = Sets.newHashSet();
            wim.getModel().setValue(getExpandedFacetsModelKey(), expandedFacets);
        }
        return expandedFacets;
    }


    protected void expandOrCollapseFacet(final String facetName, final WidgetInstanceManager wim)
    {
        final Set expandedFacets = findExpandedFacets(wim);
        if(expandedFacets.contains(facetName))
        {
            expandedFacets.remove(facetName);
        }
        else
        {
            expandedFacets.add(facetName);
        }
        wim.getModel().setValue(getExpandedFacetsModelKey(), expandedFacets);
    }


    protected String calculateFacetLabel(final FacetData facet, final WidgetInstanceManager wim)
    {
        final String label = wim.getLabel(facet.getDisplayName());
        return Strings.isBlank(label) ? facet.getDisplayName() : label;
    }


    protected String calculateFacetValueLabel(final FacetValueData facetValueData, final WidgetInstanceManager wim)
    {
        final String localizedLabel = wim.getLabel(facetValueData.getDisplayName());
        final String label = Strings.isBlank(localizedLabel) ? facetValueData.getDisplayName() : localizedLabel;
        if(FacetValueData.BREADCRUMB_COUNT == facetValueData.getCount())
        {
            return label;
        }
        else
        {
            return String.format("%s (%d)", label, facetValueData.getCount());
        }
    }


    /**
     * @deprecated
     * @since 1905
     */
    @Deprecated(since = "1905", forRemoval = true)
    protected Radiogroup prepareRadioGroup(final FacetData facet, final Div facetValueContainer, final String prefix)
    {
        final Radiogroup radio = new Radiogroup();
        radio.setParent(facetValueContainer);
        radio.setName(prefix + facet.getDisplayName());
        radio.setSclass(YW_FACET_VALUE_CONTAINER);
        facetValueContainer.appendChild(radio);
        return radio;
    }


    protected int calculateMaxValuesInline(final WidgetInstanceManager wim)
    {
        final int maxFacets = wim.getWidgetSettings().getInt(WIDGET_SETTING_MAX_INLINE_FACETS);
        return maxFacets < 1 ? 1 : maxFacets;
    }


    public String getSelectedFacetsModelKey()
    {
        return selectedFacetsModelKey;
    }


    @Required
    public void setSelectedFacetsModelKey(final String selectedFacetsModelKey)
    {
        this.selectedFacetsModelKey = selectedFacetsModelKey;
    }


    public String getExpandedFacetsModelKey()
    {
        return expandedFacetsModelKey;
    }


    @Required
    public void setExpandedFacetsModelKey(final String expandedFacetsModelKey)
    {
        this.expandedFacetsModelKey = expandedFacetsModelKey;
    }
}
