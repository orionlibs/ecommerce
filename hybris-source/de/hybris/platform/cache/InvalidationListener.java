package de.hybris.platform.cache;

public interface InvalidationListener
{
    void keyInvalidated(Object[] paramArrayOfObject, int paramInt, InvalidationTarget paramInvalidationTarget, RemoteInvalidationSource paramRemoteInvalidationSource);
}
