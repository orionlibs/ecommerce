/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.core.jco.monitor.provider;

import com.sap.conn.jco.monitor.JCoConnectionData;
import com.sap.conn.jco.monitor.JCoConnectionMonitor;
import java.util.List;

/**
 * Provides the local JCo connection monitor context.
 */
public class JCoConnectionMonitorLocalContext implements JCoConnectionMonitorContext
{
    @Override
    public List<? extends JCoConnectionData> getJCoConnectionData()
    {
        return JCoConnectionMonitor.getConnectionsData();
    }


    @Override
    public long getSnapshotTimestamp()
    {
        return System.currentTimeMillis();
    }
}
