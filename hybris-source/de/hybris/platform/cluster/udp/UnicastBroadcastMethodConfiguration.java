package de.hybris.platform.cluster.udp;

import java.net.InetSocketAddress;
import java.util.List;

public interface UnicastBroadcastMethodConfiguration
{
    InetSocketAddress getServerAddress();


    int getPacketSize();


    List<InetSocketAddress> getClusterNodes();


    int getSyncNodesIntervalSeconds();
}
