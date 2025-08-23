/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.persistence;

import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnection;
import com.hybris.cockpitng.core.persistence.impl.jaxb.WidgetConnectionRemove;
import java.util.List;

public interface WidgetConnectionsRemover
{
    void filterConnections(final List<WidgetConnection> widgetConnections, final List<WidgetConnectionRemove> connectionRemovals);
}
