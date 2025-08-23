package de.hybris.platform.mediaconversion.os.process;

import java.net.InetAddress;
import java.rmi.registry.Registry;

public interface RMIRegistryService
{
    Registry getRegistry();


    int getPort();


    InetAddress getLoopbackAddress();
}
