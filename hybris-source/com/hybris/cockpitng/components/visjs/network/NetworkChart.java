/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.NetworkSettings;
import com.hybris.cockpitng.components.visjs.network.data.Options;
import com.hybris.cockpitng.components.visjs.network.event.AddEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.AddNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.BlurEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.BlurNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ClickEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ClickNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.event.DeselectEdgesEvent;
import com.hybris.cockpitng.components.visjs.network.event.DeselectNodesEvent;
import com.hybris.cockpitng.components.visjs.network.event.DoubleClickEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.DoubleClickNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.DragEndEvent;
import com.hybris.cockpitng.components.visjs.network.event.EditEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.EditNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.EventFactory;
import com.hybris.cockpitng.components.visjs.network.event.HoverEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.HoverNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.RemoveEdgesEvent;
import com.hybris.cockpitng.components.visjs.network.event.RemoveNodesEvent;
import com.hybris.cockpitng.components.visjs.network.event.SelectEdgeEvent;
import com.hybris.cockpitng.components.visjs.network.event.SelectNodeEvent;
import com.hybris.cockpitng.components.visjs.network.event.ViewPositionChangeEvent;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

/**
 * ZK based component represents vis.js network chart.
 */
public class NetworkChart extends XulElement
{
    private static final Logger LOG = LoggerFactory.getLogger(NetworkChart.class);
    private static final String OPTIONS_PROPERTY = "options";
    private static final String SETTINGS_PROPERTY = "settings";
    private static final String INIT_NETWORK_PROPERTY = "network";
    private static final String REDRAW_NETWORK_PROPERTY = "redrawNetwork";
    private static final String NETWORK_UPDATE_PROPERTY = "networkUpdate";
    public static final String LABEL_PREFIX = "networkchart";
    private Options options = Options.EMPTY;
    private Network network;
    private NetworkSettings settings;
    private boolean initialized;
    private final transient ObjectMapper mapper = new ObjectMapper();
    private final transient EventFactory eventFactory = new EventFactory();

    static
    {
        final int flags = CE_NON_DEFERRABLE | CE_DUPLICATE_IGNORE | CE_IMPORTANT;
        addClientEvent(NetworkChart.class, ClickNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, DoubleClickNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, SelectNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, DeselectNodesEvent.NAME, flags);
        addClientEvent(NetworkChart.class, AddNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, EditNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, RemoveNodesEvent.NAME, flags);
        addClientEvent(NetworkChart.class, ClickEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, DoubleClickEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, SelectEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, DeselectEdgesEvent.NAME, flags);
        addClientEvent(NetworkChart.class, AddEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, EditEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, RemoveEdgesEvent.NAME, flags);
        addClientEvent(NetworkChart.class, HoverEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, HoverNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, BlurNodeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, BlurEdgeEvent.NAME, flags);
        addClientEvent(NetworkChart.class, ClickOnAddNodeButtonEvent.NAME, flags);
        addClientEvent(NetworkChart.class, DragEndEvent.NAME, flags);
        addClientEvent(NetworkChart.class, ViewPositionChangeEvent.NAME, flags);
    }

    @Override
    protected void renderProperties(final ContentRenderer renderer) throws IOException
    {
        super.renderProperties(renderer);
        injectLocales(options);
        render(renderer, OPTIONS_PROPERTY, mapper.writeValueAsString(options));
        if(settings != null)
        {
            render(renderer, SETTINGS_PROPERTY, mapper.writeValueAsString(settings));
        }
        if(network != null)
        {
            render(renderer, INIT_NETWORK_PROPERTY, mapper.writeValueAsString(network));
        }
        initialized = true;
    }


    protected void injectLocales(final Options options)
    {
        final String currentLocale = Locales.getCurrent().toString();
        if(StringUtils.isBlank(options.getLocale()))
        {
            options.setLocale(currentLocale);
        }
        if(options.getLocales() == null)
        {
            final Map<String, Map<String, String>> locales = new HashMap<>();
            final Map<String, Object> segmentedLabels = (Map<String, Object>)Labels.getSegmentedLabels().get(LABEL_PREFIX);
            final Map<String, String> defaultLabels = segmentedLabels.entrySet().stream()
                            .filter(entry -> entry.getValue() instanceof String)
                            .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.class.cast(entry.getValue())));
            locales.put(currentLocale, defaultLabels);
            options.setLocales(locales);
        }
    }


    /**
     * Updates network chart's view. This method allows you to add/update/remove nodes and edges on existing network chart.
     *
     * @param networkUpdates
     *           {@link NetworkUpdates} represents list of updates which should be applied to existing network chart
     */
    public void updateNetwork(final NetworkUpdates networkUpdates)
    {
        if(networkUpdates != null && CollectionUtils.isNotEmpty(networkUpdates.getUpdates()))
        {
            tryToSmartUpdate(NETWORK_UPDATE_PROPERTY, networkUpdates);
        }
    }


    /**
     * Updates network chart's options (only if passed options are not null)
     *
     * @param options
     *           object representing vis.js options
     */
    public void updateOptions(final Options options)
    {
        if(options != null)
        {
            tryToSmartUpdate(OPTIONS_PROPERTY, options);
        }
    }


    @Override
    public void service(final AuRequest request, final boolean everError)
    {
        final Event event = eventFactory.getEvent(request);
        if(event != null)
        {
            Events.postEvent(event);
        }
        else
        {
            super.service(request, everError);
        }
    }


    public Options getOptions()
    {
        return options;
    }


    /**
     * @param options
     *           {@link Options} of vis.js chart
     */
    public void setOptions(final Options options)
    {
        this.options = options;
    }


    public NetworkSettings getSettings()
    {
        return settings;
    }


    public void setSettings(final NetworkSettings settings)
    {
        this.settings = settings;
    }


    public Network getNetwork()
    {
        return network;
    }


    /**
     * Sets initial network.
     *
     * @param network
     *           {@link Network} initial network consists of nodes and edges
     */
    public void setNetwork(final Network network)
    {
        this.network = network;
        if(initialized)
        {
            tryToSmartUpdate(REDRAW_NETWORK_PROPERTY, network);
        }
    }


    protected void tryToSmartUpdate(final String property, final Object body)
    {
        try
        {
            smartUpdate(property, mapper.writeValueAsString(body));
        }
        catch(final IOException e)
        {
            LOG.error("Cannot update property " + property, e);
        }
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        final NetworkChart that = (NetworkChart)o;
        return getNetwork() != null ? getNetwork().equals(that.getNetwork()) : (that.getNetwork() == null);
    }


    @Override
    public int hashCode()
    {
        return getNetwork() != null ? getNetwork().hashCode() : 0;
    }
}
