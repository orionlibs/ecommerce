package de.hybris.platform.util;

import com.hybris.charon.netty.NettyClient;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.util.logging.log4j2.HybrisLoggerContext;
import org.apache.logging.log4j.LogManager;

public class RedeployUtilities
{
    public static final boolean USE_SHUTDOWN_HOOKS = true;
    public static boolean runningWithOwnClassLoader = false;
    private static volatile boolean shutdownInProgress = false;
    private static final Thread shutdownHook = (Thread)(new RegistrableThread(RedeployUtilities::shutdown, "ShutdownHookThread"))
                    .withInitialInfo(OperationInfo.builder()
                                    .withCategory(OperationInfo.Category.SYSTEM)
                                    .withStatusInfo("Shutting down system")
                                    .asSuspendableOperation(true).build());

    static
    {
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public static void startup()
    {
        try
        {
            Registry.activateMasterTenant();
            Registry.startup();
        }
        catch(Exception e)
        {
            System.out.println("error starting Registry: " + e.getMessage());
        }
    }


    public static void shutdown()
    {
        System.out.println("shutting down hybris registry..");
        shutdownInProgress = true;
        try
        {
            Registry.destroy(true);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
        shutdownLog4j2();
        shutdownNettyClient();
    }


    private static void shutdownNettyClient()
    {
        NettyClient.shutdown();
    }


    private static void shutdownLog4j2()
    {
        ((HybrisLoggerContext)LogManager.getContext()).shutdown();
    }


    public static boolean isShutdownInProgress()
    {
        return shutdownInProgress;
    }


    public static void noteAboutStartup()
    {
        runningWithOwnClassLoader = true;
    }


    public static boolean isRunningInHybrisServerMode()
    {
        return runningWithOwnClassLoader;
    }
}
