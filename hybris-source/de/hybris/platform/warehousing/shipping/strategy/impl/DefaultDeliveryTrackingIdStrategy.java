package de.hybris.platform.warehousing.shipping.strategy.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.shipping.strategy.DeliveryTrackingIdStrategy;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDeliveryTrackingIdStrategy implements DeliveryTrackingIdStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDeliveryTrackingIdStrategy.class);


    public String generateTrackingId(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        try
        {
            Random random = SecureRandom.getInstanceStrong();
            long trackingId = random.nextLong();
            LOGGER.info("Tracking ID generated ==> {}", Long.valueOf(trackingId));
            return Long.toString((trackingId > 0L) ? trackingId : (trackingId * -1L));
        }
        catch(NoSuchAlgorithmException e)
        {
            LOGGER.error("No random algorithm is available: {}", e.getMessage());
            return null;
        }
    }
}
