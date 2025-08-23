/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.refineby;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.refineby.renderer.FacetRenderer;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.config.refineby.jaxb.Facet;
import com.hybris.cockpitng.config.refineby.jaxb.FacetConfig;
import com.hybris.cockpitng.config.refineby.jaxb.FacetsOrder;
import com.hybris.cockpitng.config.refineby.jaxb.NotListedFacets;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.facet.FacetData;
import com.hybris.cockpitng.search.data.facet.FacetValueData;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;

public class RefineByController extends DefaultWidgetController
{
    public static final String SOCKET_IN_FULL_TEXT_SEARCH_DATA = "fullTextSearchData";
    public static final String SOCKET_OUT_SEARCH_DATA = "searchData";
    public static final String WIDGET_SETTING_INSTANT_FACETS = "instantFacets";
    public static final String WIDGET_SETTING_SELECTED_FACETS_POSITION = "selectedFacetsPosition";
    public static final String WIDGET_SETTING_DEFAULT_FACET_RENDERER_BEAN_ID = "defaultFacetRendererBeanId";
    public static final String YW_FACET_SLOT = "yw-facet-slot";
    public static final String MODEL_SELECTED_FACETS = "selectedFacets";
    public static final String MODEL_EXPANDED_FACETS = "expandedFacets";
    public static final String MODEL_LAST_FACET_DATA = "lastFacetData";
    public static final String SOCKET_IN_RESET = "reset";
    public static final String SETTING_CONFIGURATION_CONTEXT = "refineByConfigCtx";
    private static final Logger LOG = LoggerFactory.getLogger(RefineByController.class);
    private transient FacetRenderer defaultRenderer;
    @Wire
    private Div infoContainer;
    @Wire
    private Div facetContainer;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final String beanId = getDefaultFacetRendererBeanId();
        if(StringUtils.isNotBlank(beanId))
        {
            defaultRenderer = (FacetRenderer)SpringUtil.getBean(beanId, FacetRenderer.class);
            if(defaultRenderer == null)
            {
                LOG.warn("Could not resolve default facet renderer by bean id: [{}]", beanId);
            }
        }
        final FullTextSearchData data = getLastFactData();
        if(data != null)
        {
            onFullTextSearchData(data);
        }
    }


    @SocketEvent(socketId = SOCKET_IN_RESET)
    public void reset()
    {
        getModel().removeAllObservers();
        getModel().remove(MODEL_SELECTED_FACETS);
        getModel().remove(MODEL_EXPANDED_FACETS);
    }


    @SocketEvent(socketId = SOCKET_IN_FULL_TEXT_SEARCH_DATA)
    public void onFullTextSearchData(final FullTextSearchData data)
    {
        if(data != null)
        {
            facetContainer.getChildren().clear();
            getModel().removeAllObservers();
            final Object query = data.getContext().getAttribute(FieldSearchFacadeStrategy.CONTEXT_ORIGINAL_QUERY);
            if(isShowFacets(data) && query instanceof AdvancedSearchData)
            {
                infoContainer.setVisible(false);
                facetContainer.setVisible(true);
                setValue(MODEL_SELECTED_FACETS, extractSelectedFacets(data.getFacets()));
                final AdvancedSearchData searchData = (AdvancedSearchData)query;
                renderFacets(data.getFacets(), searchData, false);
                getModel().addObserver(MODEL_SELECTED_FACETS, () -> applyFacetSelection(searchData));
            }
            else
            {
                infoContainer.setVisible(true);
                facetContainer.setVisible(false);
            }
            setValue(MODEL_LAST_FACET_DATA, data);
        }
        else
        {
            reset();
        }
    }


    protected Map<String, Set<String>> extractSelectedFacets(final Collection<FacetData> facets)
    {
        final Map<String, Set<String>> result = new HashMap<>();
        facets.forEach(facet -> {
            final Set<String> selectedFacetValues = extractSelectedFacetValueNames(facet.getFacetValues());
            result.put(facet.getName(), selectedFacetValues);
        });
        return result;
    }


    protected Set<String> extractSelectedFacetValueNames(final Collection<FacetValueData> facetValues)
    {
        return facetValues.stream().filter(FacetValueData::isSelected).map(FacetValueData::getName).collect(Collectors.toSet());
    }


    protected FacetConfig loadConfig()
    {
        final String componentConfContext = getWidgetSettings().getString(SETTING_CONFIGURATION_CONTEXT);
        FacetConfig refineByConfig = null;
        try
        {
            refineByConfig = getWidgetInstanceManager().loadConfiguration(new DefaultConfigContext(componentConfContext),
                            FacetConfig.class);
            if(refineByConfig == null)
            {
                LOG.warn("Cannot retrieve refine by widget configuration");
            }
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Cannot retrieve refine by widget configuration", e);
        }
        return refineByConfig;
    }


    protected Context prepareContext(final FacetConfig facetConfig, final String facetName)
    {
        final DefaultContext defaultContext = new DefaultContext();
        if(facetConfig.getFacets() != null && facetConfig.getFacets().getFacet() != null)
        {
            facetConfig.getFacets().getFacet().stream().filter(facet -> facetName.equals(facet.getName())).findFirst().ifPresent(
                            facet -> facet.getParameter().forEach(param -> defaultContext.addAttribute(param.getName(), param.getValue())));
        }
        defaultContext.addAttribute(SETTING_CONFIGURATION_CONTEXT, facetConfig);
        return defaultContext;
    }


    protected FullTextSearchData getLastFactData()
    {
        return getValue(MODEL_LAST_FACET_DATA, FullTextSearchData.class);
    }


    public void applyFacetSelection(final AdvancedSearchData query)
    {
        final AdvancedSearchData queryData = new AdvancedSearchData(query);
        queryData.setTokenizable(true);
        queryData.setSelectedFacets(ObjectUtils.defaultIfNull(getValue(MODEL_SELECTED_FACETS, Map.class), new HashMap<>()));
        queryData.setAdvancedSearchMode(AdvancedSearchMode.SIMPLE);
        sendOutput(SOCKET_OUT_SEARCH_DATA, queryData);
    }


    protected boolean isShowFacets(final FullTextSearchData data)
    {
        if(CollectionUtils.isNotEmpty(data.getFacets()))
        {
            return CollectionUtils.isNotEmpty(data.getFacets().stream()
                            .filter(facetData -> CollectionUtils.isNotEmpty(facetData.getFacetValues())).collect(Collectors.toList()));
        }
        return false;
    }


    protected void renderFacets(final Collection<FacetData> facets, final AdvancedSearchData query, final boolean selected)
    {
        final FacetConfig facetConfig = loadConfig();
        final Collection<FacetData> filteredFacets = filterFacets(facets, facetConfig);
        for(final FacetData facet : filteredFacets)
        {
            final FacetRenderer renderer = resolveRenderer(facetConfig, facet);
            if(renderer == null)
            {
                LOG.warn("No renderer found for facet: [{}]", facet);
            }
            else
            {
                final Div container = new Div();
                container.setSclass(YW_FACET_SLOT);
                renderer.renderFacet(container, facet, selected, getWidgetInstanceManager(),
                                prepareContext(facetConfig, facet.getName()));
                facetContainer.appendChild(container);
            }
        }
    }


    protected Collection<FacetData> filterFacets(final Collection<FacetData> facets, final FacetConfig facetConfig)
    {
        final List<String> facetsBlacklist = mapBlacklist(facetConfig);
        final List<String> listedFacets = mapListedFacets(facetConfig);
        List<FacetData> filteredFacets = Lists.newArrayList(facets);
        if(!facetsBlacklist.isEmpty())
        {
            filteredFacets = filteredFacets.stream().filter(facet -> !facetsBlacklist.contains(facet.getName()))
                            .collect(Collectors.toList());
        }
        if(facetConfig.getFacets() != null && facetConfig.getFacets().getNotListed() == NotListedFacets.SKIP)
        {
            filteredFacets = filteredFacets.stream().filter(facet -> listedFacets.contains(facet.getName()))
                            .collect(Collectors.toList());
        }
        if(!listedFacets.isEmpty() && facetConfig.getFacets().getOrder() == FacetsOrder.LISTED)
        {
            filteredFacets.sort(Comparator.comparingInt(f -> {
                final int index = listedFacets.indexOf(f.getName());
                return index != -1 ? index : Integer.MAX_VALUE;
            }));
        }
        return filteredFacets;
    }


    protected List<String> mapListedFacets(final FacetConfig facetConfig)
    {
        if(facetConfig.getFacets() == null)
        {
            return new ArrayList<>();
        }
        return facetConfig.getFacets().getFacet().stream().map(Facet::getName).collect(Collectors.toList());
    }


    protected List<String> mapBlacklist(final FacetConfig facetConfig)
    {
        if(facetConfig.getBlacklist() == null)
        {
            return new ArrayList<>();
        }
        return facetConfig.getBlacklist().getFacet().stream().map(Facet::getName).collect(Collectors.toList());
    }


    protected FacetRenderer resolveRenderer(final FacetConfig config, final FacetData facet)
    {
        if(config.getFacets() == null)
        {
            return defaultRenderer;
        }
        for(final Facet facetCfg : config.getFacets().getFacet())
        {
            if(facetCfg.getName().equals(facet.getName()) && StringUtils.isNotBlank(facetCfg.getRenderer()))
            {
                final FacetRenderer renderer = (FacetRenderer)SpringUtil.getBean(facetCfg.getRenderer(), FacetRenderer.class);
                if(renderer != null)
                {
                    return renderer;
                }
            }
        }
        return defaultRenderer;
    }


    public boolean isInstantFacets()
    {
        return getWidgetSettings().getBoolean(WIDGET_SETTING_INSTANT_FACETS);
    }


    public String getSelectedFacetsPosition()
    {
        return getWidgetSettings().getString(WIDGET_SETTING_SELECTED_FACETS_POSITION);
    }


    public String getDefaultFacetRendererBeanId()
    {
        return getWidgetSettings().getString(WIDGET_SETTING_DEFAULT_FACET_RENDERER_BEAN_ID);
    }


    public FacetRenderer getDefaultRenderer()
    {
        return defaultRenderer;
    }


    public Div getInfoContainer()
    {
        return infoContainer;
    }


    public Div getFacetContainer()
    {
        return facetContainer;
    }
}
