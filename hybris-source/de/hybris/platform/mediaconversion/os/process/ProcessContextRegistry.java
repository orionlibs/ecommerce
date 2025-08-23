package de.hybris.platform.mediaconversion.os.process;

import de.hybris.platform.mediaconversion.os.ProcessContext;

public interface ProcessContextRegistry
{
    int register(ProcessContext paramProcessContext);


    void unregister(int paramInt);


    ProcessContext retrieve(int paramInt);
}
