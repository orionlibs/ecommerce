package de.hybris.platform.cluster;

public interface BroadcastMessageListener
{
    boolean processMessage(RawMessage paramRawMessage);
}
