package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.strategies.UpdateUserSegmentStrategy;
import org.apache.log4j.Logger;

public class EmptyUpdateUserSegmentStrategy implements UpdateUserSegmentStrategy
{
    private static final Logger LOG = Logger.getLogger(EmptyUpdateUserSegmentStrategy.class);


    public void updateUserSegments(UserModel user)
    {
        LOG.debug("User segments are not updated because there is no UdateUserSegmentStrategy configured.");
    }


    public void updateUserSegments(UserModel user, CxCalculationContext context)
    {
        LOG.debug("User segments are not updated because there is no UdateUserSegmentStrategy configured.");
    }
}
