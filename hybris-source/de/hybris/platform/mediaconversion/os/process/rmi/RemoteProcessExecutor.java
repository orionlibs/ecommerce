package de.hybris.platform.mediaconversion.os.process.rmi;

import java.io.IOException;
import java.rmi.Remote;

public interface RemoteProcessExecutor extends Remote
{
    int execute(RemoteProcessContext paramRemoteProcessContext) throws IOException;


    void quit() throws IOException;
}
