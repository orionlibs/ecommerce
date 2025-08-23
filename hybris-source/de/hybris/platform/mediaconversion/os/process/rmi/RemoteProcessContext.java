package de.hybris.platform.mediaconversion.os.process.rmi;

import de.hybris.platform.mediaconversion.os.ProcessCommand;
import de.hybris.platform.mediaconversion.os.ProcessContext;
import java.io.Serializable;

public class RemoteProcessContext implements Serializable
{
    private static final long serialVersionUID = -8272622556649970137L;
    private final ProcessCommand context;
    private final int pid;


    public RemoteProcessContext(ProcessContext ctx, int pid)
    {
        this(ctx.getContext(), pid);
    }


    public RemoteProcessContext(ProcessCommand ctx, int pid)
    {
        this.context = ctx;
        if(this.context == null)
        {
            throw new IllegalArgumentException("ProcessContext must not be null.");
        }
        this.pid = pid;
    }


    public int getPid()
    {
        return this.pid;
    }


    public ProcessCommand getContext()
    {
        return this.context;
    }
}
