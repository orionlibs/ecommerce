/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.persistence.impl;

import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnectionRemove;
import com.hybris.cockpitng.modules.persistence.WidgetConnectionsRemover;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWidgetConnectionsRemover implements WidgetConnectionsRemover
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetConnectionsRemover.class);


    @Override
    public void filterConnections(final List<WidgetConnection> widgetConnections, final List<WidgetConnectionRemove> connectionRemovals)
    {
        removeInvalidConnectionRemovals(connectionRemovals);
        for(final WidgetConnectionRemove removal : connectionRemovals)
        {
            final List<WidgetConnection> connectionsToRemove;
            if(StringUtils.isNotBlank(removal.getName()))
            {
                connectionsToRemove = widgetConnections.stream()
                                .filter(con -> removal.getName().equals(con.getName()))
                                .collect(Collectors.toList());
                widgetConnections.removeAll(connectionsToRemove);
                continue;
            }
            connectionsToRemove = widgetConnections.stream()
                            .filter(con -> doesMatch(removal.getSourceWidgetId(), con.getSourceWidgetId()))
                            .filter(con -> doesMatch(removal.getTargetWidgetId(), con.getTargetWidgetId()))
                            .filter(con -> doesMatch(removal.getInputId(), con.getInputId()))
                            .filter(con -> doesMatch(removal.getOutputId(), con.getOutputId())).collect(Collectors.toList());
            widgetConnections.removeAll(connectionsToRemove);
        }
    }


    private static boolean doesMatch(final String sourceWidgetId, final String sourceWidgetId2)
    {
        return StringUtils.isBlank(sourceWidgetId) || sourceWidgetId.equals(sourceWidgetId2);
    }


    private static void removeInvalidConnectionRemovals(final List<WidgetConnectionRemove> connectionRemovals)
    {
        final List<WidgetConnectionRemove> invalidRemovals = connectionRemovals.stream().filter(c -> !isConnectionRemovalValid(c))
                        .peek(c -> LOG
                                        .warn("Can not remove widget connection: socketId must be given along with widgetId. " + Objects.toString(c)))
                        .collect(Collectors.toList());
        connectionRemovals.removeAll(invalidRemovals);
    }


    private static boolean isConnectionRemovalValid(final WidgetConnectionRemove connectionRemoval)
    {
        return isSocketGivenAlongWithWidget(connectionRemoval.getOutputId(), connectionRemoval.getSourceWidgetId())
                        && isSocketGivenAlongWithWidget(connectionRemoval.getInputId(), connectionRemoval.getTargetWidgetId());
    }


    private static boolean isSocketGivenAlongWithWidget(final String outputId, final String sourceWidgetId)
    {
        return StringUtils.isBlank(outputId) || StringUtils.isNotBlank(sourceWidgetId);
    }
}
