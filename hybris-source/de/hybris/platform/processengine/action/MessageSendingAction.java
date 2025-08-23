package de.hybris.platform.processengine.action;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.event.events.MessageSendingException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

public class MessageSendingAction extends AbstractProceduralAction
{
    private MessageChannel channel;


    public void executeAction(BusinessProcessModel process)
    {
        Message<BusinessProcessModel> message = MessageBuilder.withPayload(process).build();
        boolean sent = this.channel.send(message);
        if(!sent)
        {
            throw new MessageSendingException("Message of type " + message.getClass() + " could not be sent");
        }
    }


    public void setChannel(MessageChannel channel)
    {
        this.channel = channel;
    }
}
