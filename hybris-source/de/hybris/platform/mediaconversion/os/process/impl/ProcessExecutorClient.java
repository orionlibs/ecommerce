package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.ProcessContext;
import de.hybris.platform.mediaconversion.os.process.AbstractProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.ProcessContextRegistry;
import de.hybris.platform.mediaconversion.os.process.RMIRegistryService;
import de.hybris.platform.mediaconversion.os.process.rmi.RemoteProcessContext;
import de.hybris.platform.mediaconversion.os.process.rmi.RemoteProcessExecutor;
import java.io.File;
import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

public class ProcessExecutorClient extends AbstractProcessExecutor
{
    private static final Logger LOG = Logger.getLogger(ProcessExecutorClient.class);
    public static final String EMMA_COVERAGE_ANNOUNCE = "EMMA: collecting runtime coverage data ...";
    private final RemoteProcessExecutor rmiStub;
    private final ProcessContextRegistry processContextRegistry;


    public ProcessExecutorClient(Configuration conf, RMIRegistryService rmiReg, ProcessContextRegistry contextReg, String tenantName) throws IOException, NotBoundException
    {
        this.processContextRegistry = contextReg;
        if(this.processContextRegistry == null)
        {
            throw new IllegalArgumentException("ProcessContextRegistry is required.");
        }
        ServerSpawn.spawnServer(conf
                        .getString("os.rmi.processexecutor.java", System.getProperty("java.home") + System.getProperty("java.home") + "bin" + File.separatorChar + "java"), conf
                        .getString("os.rmi.processexecutor.classpath", ServerSpawn.buildClasspath()), conf
                        .getStringArray("os.rmi.processexecutor.javaopts"), rmiReg.getLoopbackAddress(), rmiReg.getPort(), tenantName);
        this.rmiStub = (RemoteProcessExecutor)rmiReg.getRegistry().lookup(RemoteProcessExecutor.class
                        .getSimpleName() + "_" + RemoteProcessExecutor.class.getSimpleName());
    }


    public void quit() throws IOException
    {
        try
        {
            this.rmiStub.quit();
        }
        catch(ConnectException e)
        {
            LOG.warn("Failed to quit sub process. Child process already terminated.");
            LOG.debug("Failed to quit sub process.", e);
        }
    }


    public int execute(ProcessContext ctx) throws IOException
    {
        LogUtil.log(LOG, ctx);
        int pid = getProcessContextRegistry().register(ctx);
        LOG.debug("Internal remote pid: " + pid);
        try
        {
            RemoteProcessContext remoteCtx = new RemoteProcessContext(ctx, pid);
            return this.rmiStub.execute(remoteCtx);
        }
        finally
        {
            getProcessContextRegistry().unregister(pid);
        }
    }


    private ProcessContextRegistry getProcessContextRegistry()
    {
        return this.processContextRegistry;
    }
}
