package de.hybris.platform.cluster;

import java.util.Map;

public interface BroadcastServiceConfiguration
{
    boolean enableClusterMode();


    Map<String, Class> getMethods();


    long getDynamicNodeID();


    int getConfiguredNodeID();


    long getClusterIslandID();


    boolean startPingOnTenantStartup();


    boolean enableAsynchonousSending();


    int getSenderMinThreads();


    int getSenderMaxThreads();


    int getSenderThreadsKeepAlive();


    int getMessageSendingQueueSize();


    boolean getMessageSendingQueueFairness();


    long getMessageSendingMaxWait();


    int getMessageSendingWaitDelay();


    int getMessageKeyCacheSize();
}
