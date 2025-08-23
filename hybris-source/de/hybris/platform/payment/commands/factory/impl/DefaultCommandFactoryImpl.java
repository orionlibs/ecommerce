package de.hybris.platform.payment.commands.factory.impl;

import de.hybris.platform.payment.commands.Command;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.CommandNotSupportedException;
import java.util.Map;

public class DefaultCommandFactoryImpl implements CommandFactory
{
    private Map<Class<Command>, Command> commands;
    private String paymentProvider;


    public <T extends Command> T createCommand(Class<T> commandInterface) throws CommandNotSupportedException
    {
        Command command = this.commands.get(commandInterface);
        if(command == null)
        {
            throw new CommandNotSupportedException("Command not implemented: " + commandInterface.getCanonicalName());
        }
        return (T)command;
    }


    public void setCommands(Map<Class<Command>, Command> commands)
    {
        this.commands = commands;
    }


    public String getPaymentProvider()
    {
        return this.paymentProvider;
    }


    public void setPaymentProvider(String paymentProvider)
    {
        this.paymentProvider = paymentProvider;
    }
}
