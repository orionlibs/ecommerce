package de.hybris.platform.payment.commands.factory.impl;

import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.IsApplicableCommand;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.CommandFactoryRegistry;
import de.hybris.platform.payment.commands.factory.CommandNotSupportedException;
import de.hybris.platform.payment.commands.request.IsApplicableCommandReqest;
import de.hybris.platform.payment.commands.result.IsApplicableCommandResult;
import de.hybris.platform.payment.dto.BasicCardInfo;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultCommandFactoryRegistryImpl implements CommandFactoryRegistry, ApplicationContextAware, InitializingBean
{
    private static final Logger LOG = Logger.getLogger(DefaultCommandFactoryRegistryImpl.class.getName());
    private ApplicationContext applicationContext;
    private Collection<CommandFactory> commandFactoryList;


    public CommandFactory getFactory(String paymentProvider)
    {
        for(CommandFactory commandFactory : this.commandFactoryList)
        {
            if(commandFactory.getPaymentProvider().equals(paymentProvider))
            {
                return commandFactory;
            }
        }
        throw new AdapterException("Card can not be served!");
    }


    public CommandFactory getFactory(BasicCardInfo card, boolean threeD)
    {
        for(CommandFactory commandFactory : this.commandFactoryList)
        {
            if(isApplicable(card, threeD, commandFactory))
            {
                return commandFactory;
            }
        }
        throw new AdapterException("Card can not be served!");
    }


    private boolean isApplicable(BasicCardInfo card, boolean threeD, CommandFactory commandFactory)
    {
        IsApplicableCommand command;
        try
        {
            command = (IsApplicableCommand)commandFactory.createCommand(IsApplicableCommand.class);
        }
        catch(CommandNotSupportedException e)
        {
            LOG.info("command not supported", (Throwable)e);
            return false;
        }
        return ((IsApplicableCommandResult)command.perform(new IsApplicableCommandReqest(card, threeD))).isApplicable();
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public void afterPropertiesSet()
    {
        this.commandFactoryList = this.applicationContext.getBeansOfType(CommandFactory.class).values();
        if(this.commandFactoryList == null || this.commandFactoryList.isEmpty())
        {
            LOG.warn("Missing command factory! At least one command factory bean should be bound to the current spring application context");
            this.commandFactoryList = Collections.emptyList();
        }
    }


    public void setCommandFactoryList(Collection<CommandFactory> commandFactoryList)
    {
        this.commandFactoryList = commandFactoryList;
    }
}
