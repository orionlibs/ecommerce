package de.hybris.platform.payment.commands.factory;

public interface CommandFactory
{
    <T extends de.hybris.platform.payment.commands.Command> T createCommand(Class<T> paramClass) throws CommandNotSupportedException;


    String getPaymentProvider();
}
