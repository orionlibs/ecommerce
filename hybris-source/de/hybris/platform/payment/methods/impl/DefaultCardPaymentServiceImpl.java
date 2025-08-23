package de.hybris.platform.payment.methods.impl;

import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.AuthorizationCommand;
import de.hybris.platform.payment.commands.CaptureCommand;
import de.hybris.platform.payment.commands.CreateSubscriptionCommand;
import de.hybris.platform.payment.commands.DeleteSubscriptionCommand;
import de.hybris.platform.payment.commands.EnrollmentCheckCommand;
import de.hybris.platform.payment.commands.FollowOnRefundCommand;
import de.hybris.platform.payment.commands.GetSubscriptionDataCommand;
import de.hybris.platform.payment.commands.PartialCaptureCommand;
import de.hybris.platform.payment.commands.StandaloneRefundCommand;
import de.hybris.platform.payment.commands.SubscriptionAuthorizationCommand;
import de.hybris.platform.payment.commands.UpdateSubscriptionCommand;
import de.hybris.platform.payment.commands.VoidCommand;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.CommandFactoryRegistry;
import de.hybris.platform.payment.commands.factory.CommandNotSupportedException;
import de.hybris.platform.payment.commands.request.AbstractRequest;
import de.hybris.platform.payment.commands.request.AuthorizationRequest;
import de.hybris.platform.payment.commands.request.CaptureRequest;
import de.hybris.platform.payment.commands.request.CreateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.DeleteSubscriptionRequest;
import de.hybris.platform.payment.commands.request.EnrollmentCheckRequest;
import de.hybris.platform.payment.commands.request.FollowOnRefundRequest;
import de.hybris.platform.payment.commands.request.PartialCaptureRequest;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.commands.request.SubscriptionDataRequest;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.request.VoidRequest;
import de.hybris.platform.payment.commands.result.AuthorizationResult;
import de.hybris.platform.payment.commands.result.CaptureResult;
import de.hybris.platform.payment.commands.result.EnrollmentCheckResult;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.commands.result.SubscriptionDataResult;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.commands.result.VoidResult;
import de.hybris.platform.payment.dto.BasicCardInfo;
import de.hybris.platform.payment.methods.CardPaymentService;

public class DefaultCardPaymentServiceImpl implements CardPaymentService
{
    private CommandFactoryRegistry commandFactoryRegistry;


    public AuthorizationResult authorize(AuthorizationRequest request)
    {
        try
        {
            CommandFactory commandFactory = this.commandFactoryRegistry.getFactory((BasicCardInfo)request.getCard(), false);
            AuthorizationCommand command = (AuthorizationCommand)commandFactory.createCommand(AuthorizationCommand.class);
            AuthorizationResult result = (AuthorizationResult)command.perform(request);
            result.setPaymentProvider(commandFactory.getPaymentProvider());
            return result;
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public AuthorizationResult authorize(SubscriptionAuthorizationRequest request)
    {
        try
        {
            CommandFactory commandFactory;
            if(request.getPaymentProvider() == null)
            {
                commandFactory = this.commandFactoryRegistry.getFactory(null, false);
            }
            else
            {
                commandFactory = this.commandFactoryRegistry.getFactory(request.getPaymentProvider());
            }
            SubscriptionAuthorizationCommand command = (SubscriptionAuthorizationCommand)commandFactory.createCommand(SubscriptionAuthorizationCommand.class);
            AuthorizationResult result = (AuthorizationResult)command.perform(request);
            result.setPaymentProvider(commandFactory.getPaymentProvider());
            return result;
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public CaptureResult capture(CaptureRequest request)
    {
        try
        {
            CaptureCommand command = (CaptureCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(CaptureCommand.class);
            return (CaptureResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public CaptureResult partialCapture(PartialCaptureRequest request)
    {
        try
        {
            PartialCaptureCommand command = (PartialCaptureCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(PartialCaptureCommand.class);
            return (CaptureResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public EnrollmentCheckResult enrollmentCheck(EnrollmentCheckRequest request)
    {
        CommandFactory commandFactory = this.commandFactoryRegistry.getFactory(request.getCard(), true);
        try
        {
            EnrollmentCheckCommand command = (EnrollmentCheckCommand)commandFactory.createCommand(EnrollmentCheckCommand.class);
            EnrollmentCheckResult result = (EnrollmentCheckResult)command.perform(request);
            result.setPaymentProvider(commandFactory.getPaymentProvider());
            return result;
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public RefundResult refundFollowOn(FollowOnRefundRequest request)
    {
        try
        {
            FollowOnRefundCommand command = (FollowOnRefundCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(FollowOnRefundCommand.class);
            return command.perform((AbstractRequest)request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public RefundResult refundStandalone(StandaloneRefundRequest request)
    {
        try
        {
            CommandFactory commandFactory;
            if(request.getPaymentProvider() == null)
            {
                commandFactory = this.commandFactoryRegistry.getFactory((BasicCardInfo)request.getCard(), false);
            }
            else
            {
                commandFactory = this.commandFactoryRegistry.getFactory(request.getPaymentProvider());
            }
            StandaloneRefundCommand command = (StandaloneRefundCommand)commandFactory.createCommand(StandaloneRefundCommand.class);
            RefundResult result = command.perform((AbstractRequest)request);
            result.setPaymentProvider(commandFactory.getPaymentProvider());
            return result;
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public VoidResult voidCreditOrCapture(VoidRequest request)
    {
        try
        {
            VoidCommand command = (VoidCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(VoidCommand.class);
            return (VoidResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public SubscriptionResult createSubscription(CreateSubscriptionRequest request)
    {
        try
        {
            CreateSubscriptionCommand command = (CreateSubscriptionCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(CreateSubscriptionCommand.class);
            return (SubscriptionResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public SubscriptionResult updateSubscription(UpdateSubscriptionRequest request)
    {
        try
        {
            UpdateSubscriptionCommand command = (UpdateSubscriptionCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(UpdateSubscriptionCommand.class);
            return (SubscriptionResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public SubscriptionDataResult getSubscriptionData(SubscriptionDataRequest request)
    {
        try
        {
            GetSubscriptionDataCommand command = (GetSubscriptionDataCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(GetSubscriptionDataCommand.class);
            return (SubscriptionDataResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public SubscriptionResult deleteSubscription(DeleteSubscriptionRequest request)
    {
        try
        {
            DeleteSubscriptionCommand command = (DeleteSubscriptionCommand)this.commandFactoryRegistry.getFactory(request.getPaymentProvider()).createCommand(DeleteSubscriptionCommand.class);
            return (SubscriptionResult)command.perform(request);
        }
        catch(CommandNotSupportedException e)
        {
            throw new AdapterException(e.getMessage(), e);
        }
    }


    public CommandFactoryRegistry getCommandFactoryRegistry()
    {
        return this.commandFactoryRegistry;
    }


    public void setCommandFactoryRegistry(CommandFactoryRegistry commandFactoryRegistry)
    {
        this.commandFactoryRegistry = commandFactoryRegistry;
    }
}
