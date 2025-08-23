package de.hybris.platform.cluster;

import java.util.Set;

public interface BroadcastService
{
    void send(RawMessage paramRawMessage);


    void registerBroadcastListener(BroadcastMessageListener paramBroadcastMessageListener, boolean paramBoolean);


    void unregisterBroadcastListener(BroadcastMessageListener paramBroadcastMessageListener);


    long getDynamicClusterNodeID();


    BroadcastMethod getBroadcastMethod(String paramString);


    Set<String> getBroadcastMethodNames();


    boolean accept(RawMessage paramRawMessage);
}
