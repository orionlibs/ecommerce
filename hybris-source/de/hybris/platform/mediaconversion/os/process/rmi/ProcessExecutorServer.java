package de.hybris.platform.mediaconversion.os.process.rmi;

import de.hybris.platform.mediaconversion.os.Drain;
import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public final class ProcessExecutorServer implements RemoteProcessExecutor
{
    public static final String LOOPBACK_CONFIG_KEY = "os.rmi.loopback.address";
    public static final String START_CONFIRMATION = "OK";
    public static final int DEFAULT_PORT = 2198;
    private static final int MAX_UNEXPORT_TRIES = 10;
    private static final int UNEXPORT_RETRY_DELAY = 500;
    private final RemoteDrain logCall;
    private final ProcessExecutor executor;


    private ProcessExecutorServer(RemoteDrain logCall)
    {
        this.logCall = logCall;
        if(this.logCall == null)
        {
            throw new IllegalArgumentException("LoggingCallback must not be null.");
        }
        this.executor = (ProcessExecutor)new BasicProcessExecutor();
    }


    public int execute(RemoteProcessContext context) throws IOException
    {
        ProcessContext localContext = new ProcessContext(context.getContext(), createDrain(DrainType.OUT, context.getPid()), createDrain(DrainType.ERROR, context.getPid()));
        return this.executor.execute(localContext);
    }


    private Drain createDrain(DrainType type, int pid)
    {
        return (Drain)new Object(this, pid, type);
    }


    public void quit() throws IOException
    {
        System.out.println("Quitting.");
        int waitCount = 0;
        while(!UnicastRemoteObject.unexportObject((Remote)this, (waitCount++ < 10)))
        {
            System.err.println("Failed to unexport ProcessExecutorServer. Waiting.");
            try
            {
                Thread.sleep(500L);
            }
            catch(InterruptedException e)
            {
                System.err.println("Interrupted.");
                e.printStackTrace();
            }
        }
        System.out.println("Good bye.");
    }


    public static void main(String[] args)
    {
        if(args == null || args.length == 0)
        {
            throw new IllegalArgumentException("No code specified.");
        }
        try
        {
            int port = Integer.parseInt(args[2]);
            start(args[0], args[1], port);
        }
        catch(NumberFormatException e)
        {
            System.err.println("Invalid port specified (" + args[2] + "): " + e);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.err.println("Invalid amount of command line parameters: " + e);
        }
    }


    private static void start(String tenant, String host, int port)
    {
        try
        {
            Registry registry = LocateRegistry.getRegistry(host, port);
            RemoteDrain logC = (RemoteDrain)registry.lookup(RemoteDrain.SERVICE_NAME);
            ProcessExecutorServer engine = new ProcessExecutorServer(logC);
            RemoteProcessExecutor stub = (RemoteProcessExecutor)UnicastRemoteObject.exportObject((Remote)engine, 0);
            registry.rebind(RemoteProcessExecutor.class.getSimpleName() + "_" + RemoteProcessExecutor.class.getSimpleName(), (Remote)stub);
            System.out.println("OK");
        }
        catch(Exception e)
        {
            System.err.println("Failed to start process executor server.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
