package de.hybris.platform.servicelayer.event;

public interface TransactionAwareEvent
{
    boolean publishOnCommitOnly();


    Object getId();
}
