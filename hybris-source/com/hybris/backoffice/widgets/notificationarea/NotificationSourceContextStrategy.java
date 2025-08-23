/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.notificationarea;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationSourceContextStrategy implements CockpitConfigurationContextStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(NotificationSourceContextStrategy.class);


    @Override
    public List<String> getParentContexts(final String context)
    {
        switch(context)
        {
            case NotificationEvent.EVENT_SOURCE_UNKNOWN:
                return Collections.singletonList(StringUtils.EMPTY);
            case StringUtils.EMPTY:
                return Collections.emptyList();
            default:
                return Collections.singletonList(NotificationEvent.EVENT_SOURCE_UNKNOWN);
        }
    }


    @Override
    public boolean valueMatches(final String contextValue, final String value)
    {
        boolean result = false;
        if(StringUtils.equals(contextValue, value))
        {
            result = true;
        }
        else
        {
            try
            {
                result = value != null && contextValue != null && value.matches(contextValue);
            }
            catch(final PatternSyntaxException pse)
            {
                LOG.debug("Given expression could not be evaluated as a regex", pse);
            }
        }
        return result;
    }
}
