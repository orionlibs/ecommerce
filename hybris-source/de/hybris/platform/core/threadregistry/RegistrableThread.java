package de.hybris.platform.core.threadregistry;

import java.util.Objects;
import javax.annotation.Nonnull;

public class RegistrableThread extends Thread
{
    private final OperationInfo parentThreadInfo = OperationInfo.builder()
                    .withParentThreadId(
                                    Long.valueOf(Thread.currentThread().getId()))
                    .withParentThreadName(Thread.currentThread().getName())
                    .build();
    private volatile OperationInfo initialInfo = OperationInfo.empty();
    private volatile ThreadRegistry threadRegistry;


    public RegistrableThread()
    {
    }


    public RegistrableThread(Runnable target)
    {
        super(target);
    }


    public RegistrableThread(ThreadGroup group, Runnable target)
    {
        super(group, target);
    }


    public RegistrableThread(String name)
    {
        super(name);
    }


    public RegistrableThread(ThreadGroup group, String name)
    {
        super(group, name);
    }


    public RegistrableThread(Runnable target, String name)
    {
        super(target, name);
    }


    public RegistrableThread(ThreadGroup group, Runnable target, String name)
    {
        super(group, target, name);
    }


    public RegistrableThread(ThreadGroup group, Runnable target, String name, long stackSize)
    {
        super(group, target, name, stackSize);
    }


    public RegistrableThread withInitialInfo(OperationInfo initialInfo)
    {
        Objects.requireNonNull(initialInfo, "initialInfo mustn't be null");
        this.initialInfo = initialInfo;
        return this;
    }


    RegistrableThread usingThreadRegistry(ThreadRegistry threadRegistry)
    {
        this.threadRegistry = Objects.<ThreadRegistry>requireNonNull(threadRegistry);
        return this;
    }


    public static void registerThread(@Nonnull OperationInfo operationInfo)
    {
        Objects.requireNonNull(operationInfo, "operationInfo mustn't be null");
        registerThread(operationInfo, null);
    }


    static void registerThread(OperationInfo operationInfo, ThreadRegistry threadRegistry)
    {
        ThreadRegistry registry = (threadRegistry == null) ? SuspendResumeServices.getInstance().getThreadRegistry() : threadRegistry;
        registry.register(operationInfo);
    }


    public static void unregisterThread()
    {
        unregisterThread(null);
    }


    static void unregisterThread(ThreadRegistry threadRegistry)
    {
        ThreadRegistry registry = (threadRegistry == null) ? SuspendResumeServices.getInstance().getThreadRegistry() : threadRegistry;
        registry.unregister();
    }


    public final void run()
    {
        ThreadRegistry registry = this.threadRegistry;
        registerThread(this.initialInfo.merge(this.parentThreadInfo), registry);
        try
        {
            internalRun();
        }
        finally
        {
            unregisterThread(registry);
        }
    }


    protected void internalRun()
    {
        super.run();
    }
}
