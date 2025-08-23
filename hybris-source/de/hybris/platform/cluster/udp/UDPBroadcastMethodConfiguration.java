package de.hybris.platform.cluster.udp;

public interface UDPBroadcastMethodConfiguration
{
    String getMulticastAddress();


    int getPort();


    String getInterfaceName();


    String getNetworkInterfaceName();


    int getPacketSize();
}
