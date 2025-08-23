package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.process.RMIRegistryService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import org.apache.log4j.Logger;

public class DefaultRMIRegistryService implements RMIRegistryService
{
    private static final Logger LOG = Logger.getLogger(DefaultRMIRegistryService.class);
    private int port = 1099;
    private String loopback;
    private InetAddress loopbackAddress;
    private Registry registry;


    public int getPort()
    {
        return this.port;
    }


    public void setPort(int port)
    {
        this.port = port;
    }


    public String getLoopback()
    {
        return this.loopback;
    }


    public void setLoopback(String loopback)
    {
        this.loopback = (loopback != null && loopback.isEmpty()) ? null : loopback;
        this.loopbackAddress = null;
    }


    public Registry getRegistry()
    {
        return this.registry;
    }


    public void init()
    {
        LOG.info("Creating Registry on port " + getPort() + ".");
        try
        {
            LocalRMISocketFactory socketFactory = new LocalRMISocketFactory(getLoopbackAddress());
            this.registry = LocateRegistry.createRegistry(getPort(), (RMIClientSocketFactory)socketFactory, (RMIServerSocketFactory)socketFactory);
        }
        catch(RemoteException e)
        {
            throw new IllegalStateException("Failed to create Registry at port '" + getPort() + "'.", e);
        }
    }


    public void destroy()
    {
        try
        {
            LOG.info("Unexporting the registry.");
            UnicastRemoteObject.unexportObject(this.registry, true);
        }
        catch(NoSuchObjectException e)
        {
            LOG.info("Registry has been already unexported.");
        }
        finally
        {
            this.registry = null;
        }
    }


    public InetAddress getLoopbackAddress()
    {
        try
        {
            if(this.loopbackAddress == null)
            {
                this.loopbackAddress = InetAddress.getByName(this.loopback);
            }
            return this.loopbackAddress;
        }
        catch(UnknownHostException e)
        {
            throw new IllegalStateException("Invalid loopback address '" + this.loopback + "' specified in configuration.", e);
        }
    }
}
