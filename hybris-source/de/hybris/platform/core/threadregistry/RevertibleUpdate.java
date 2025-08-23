package de.hybris.platform.core.threadregistry;

import java.util.Objects;

public class RevertibleUpdate implements AutoCloseable
{
    private final OperationInfo infoToRestore;
    private final ThreadRegistry threadRegistry;


    RevertibleUpdate(OperationInfo infoToRestore, ThreadRegistry threadRegistry)
    {
        this.infoToRestore = infoToRestore;
        this.threadRegistry = Objects.<ThreadRegistry>requireNonNull(threadRegistry);
    }


    public void close()
    {
        revert();
    }


    public void revert()
    {
        if(this.infoToRestore != null)
        {
            this.threadRegistry.set(this.infoToRestore);
        }
    }
}
