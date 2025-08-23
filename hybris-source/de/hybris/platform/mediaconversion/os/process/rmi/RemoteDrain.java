package de.hybris.platform.mediaconversion.os.process.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteDrain extends Remote
{
    public static final String SERVICE_NAME = RemoteDrain.class.getSimpleName();


    void drain(int paramInt, DrainType paramDrainType, String paramString) throws RemoteException;
}
