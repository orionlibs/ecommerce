package de.hybris.platform.servicelayer.user.search.preprocessor;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.preprocessor.QueryPreprocessor;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class UserQueryPreprocessor implements QueryPreprocessor
{
    private static final Logger LOG = Logger.getLogger(UserQueryPreprocessor.class);
    private UserService userService;


    public void process(FlexibleSearchQuery query)
    {
        if(query.getUser() != null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Storing user from query object: " + query.getUser() + " into user session.");
            }
            this.userService.setCurrentUser(query.getUser());
        }
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
