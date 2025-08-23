package de.hybris.platform.cluster;

import java.util.Map;

public interface BroadcastMethod
{
    void init(BroadcastService paramBroadcastService);


    void shutdown();


    void send(RawMessage paramRawMessage);


    void registerProcessor(BroadcastMessageListener paramBroadcastMessageListener);


    void unregisterProcessor(BroadcastMessageListener paramBroadcastMessageListener);


    Map<String, String> getSettings();
}
