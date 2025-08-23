package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.process.ProcessContextRegistry;
import de.hybris.platform.mediaconversion.os.process.RMIRegistryService;
import de.hybris.platform.mediaconversion.os.process.rmi.DrainType;
import de.hybris.platform.mediaconversion.os.process.rmi.RemoteDrain;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRemoteDrain implements RemoteDrain
{
    private static final Logger LOG = Logger.getLogger(DefaultRemoteDrain.class);
    private RMIRegistryService rmiRegistryService;
    private Remote remoteObject;
    private ProcessContextRegistry processContextRegistry;


    public void drain(int pid, DrainType type, String message) throws RemoteException
    {
        ProcessContext context = getProcessContextRegistry().retrieve(pid);
        if(context == null)
        {
            LOG.error("No local context available for pid " + pid + ". Data lost: " + message);
        }
        else if(DrainType.ERROR.equals(type))
        {
            context.getStdError().drain(message);
        }
        else
        {
            context.getStdOutput().drain(message);
        }
    }


    public void init()
    {
        try
        {
            LOG.debug("Registering LoggingCallback.");
            this.remoteObject = UnicastRemoteObject.exportObject((Remote)this, 0);
            getRmiRegistryService().getRegistry().bind(RemoteDrain.SERVICE_NAME, this.remoteObject);
        }
        catch(AccessException e)
        {
            LOG.error("Failed to register LoggingCallback. Access denied.", e);
        }
        catch(RemoteException e)
        {
            LOG.error("Failed to register LoggingCallback.", e);
        }
        catch(AlreadyBoundException e)
        {
            LOG.error("Failed to register LoggingCallback. Service already bound.", e);
        }
    }


    public void destroy()
    {
        if(this.remoteObject == null)
        {
            LOG.warn("Service not bound to rmi registry.");
            return;
        }
        try
        {
            LOG.debug("Unregistering LoggingCallback.");
            try
            {
                getRmiRegistryService().getRegistry().unbind(RemoteDrain.SERVICE_NAME);
            }
            catch(NotBoundException e)
            {
                LOG.info("LoggingCallback not bound.", e);
            }
            UnicastRemoteObject.unexportObject((Remote)this, true);
        }
        catch(NoSuchObjectException e)
        {
            LOG.info("RemoteDrain has been already unexported");
        }
        catch(RemoteException e)
        {
            LOG.warn("Failed to deactivate LoggingCallback.", e);
        }
        finally
        {
            this.remoteObject = null;
        }
    }


    public RMIRegistryService getRmiRegistryService()
    {
        return this.rmiRegistryService;
    }


    @Required
    public void setRmiRegistryService(RMIRegistryService rmiRegistryService)
    {
        this.rmiRegistryService = rmiRegistryService;
    }


    public ProcessContextRegistry getProcessContextRegistry()
    {
        return this.processContextRegistry;
    }


    @Required
    public void setProcessContextRegistry(ProcessContextRegistry processContextRegistry)
    {
        this.processContextRegistry = processContextRegistry;
    }
}
