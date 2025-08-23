package de.hybris.platform.tx;

public interface TransactionBody
{
    <T> T execute() throws Exception;
}
