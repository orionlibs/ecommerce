package de.hybris.platform.tx;

public interface AfterSaveListenerRegistry
{
    void publishChanges(byte[][] paramArrayOfbyte);
}
