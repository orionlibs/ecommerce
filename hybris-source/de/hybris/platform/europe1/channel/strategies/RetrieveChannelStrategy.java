package de.hybris.platform.europe1.channel.strategies;

import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.jalo.SessionContext;
import java.util.List;

public interface RetrieveChannelStrategy
{
    PriceRowChannel getChannel(SessionContext paramSessionContext);


    List<PriceRowChannel> getAllChannels();
}
