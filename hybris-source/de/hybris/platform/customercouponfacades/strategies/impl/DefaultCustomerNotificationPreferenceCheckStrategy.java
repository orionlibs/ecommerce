package de.hybris.platform.customercouponfacades.strategies.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.strategies.CustomerNotificationPreferenceCheckStrategy;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

public class DefaultCustomerNotificationPreferenceCheckStrategy implements CustomerNotificationPreferenceCheckStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultCustomerNotificationPreferenceCheckStrategy.class);
    private UserService userService;


    public Boolean checkCustomerNotificationPreference()
    {
        CustomerModel customer = (CustomerModel)getUserService().getCurrentUser();
        if(CollectionUtils.isEmpty(customer.getNotificationChannels()))
        {
            LOG.warn("You haven't chosen any channel in Notification Preference, so no notification would be sent.");
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
