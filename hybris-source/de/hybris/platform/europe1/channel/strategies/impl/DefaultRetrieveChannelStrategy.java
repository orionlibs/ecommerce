package de.hybris.platform.europe1.channel.strategies.impl;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.channel.strategies.RetrieveChannelStrategy;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRetrieveChannelStrategy implements RetrieveChannelStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultRetrieveChannelStrategy.class);
    protected static final String CHANNEL = "channel";
    protected static final String DETECTED_UI_EXPERIENCE_LEVEL = "UiExperienceService-Detected-Level";
    private EnumerationService enumerationService;


    public PriceRowChannel getChannel(SessionContext ctx)
    {
        LOG.debug("Inside ChannelRetrievalStrategy.");
        PriceRowChannel priceRowChannel = null;
        if(ctx == null || ctx.getAttribute("UiExperienceService-Detected-Level") == null)
        {
            return priceRowChannel;
        }
        priceRowChannel = (PriceRowChannel)ctx.getAttribute("channel");
        if(priceRowChannel == null)
        {
            EnumerationValue enumUIExpLevel = (EnumerationValue)ctx.getAttribute("UiExperienceService-Detected-Level");
            priceRowChannel = getEnumValueForCode(enumUIExpLevel.getCode().toLowerCase());
            if(priceRowChannel != null)
            {
                ctx.setAttribute("channel", priceRowChannel);
            }
        }
        return priceRowChannel;
    }


    public List<PriceRowChannel> getAllChannels()
    {
        return this.enumerationService.getEnumerationValues("PriceRowChannel");
    }


    private PriceRowChannel getEnumValueForCode(String channel)
    {
        PriceRowChannel channelFromDb = null;
        try
        {
            channelFromDb = (PriceRowChannel)this.enumerationService.getEnumerationValue("PriceRowChannel", channel);
        }
        catch(UnknownIdentifierException unknownIdentifierException)
        {
            LOG.debug("This Enum is not setup in PriceRowChannel dynamic enum");
        }
        return channelFromDb;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }
}
