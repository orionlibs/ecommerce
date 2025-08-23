/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.contextpopulator;

import com.hybris.backoffice.widgets.refineby.RefineByController;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class ContextPopulatorController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(RefineByController.class);
    public static final String SOCKET_IN_DATA = "data";
    public static final String SOCKET_OUT_CONTEXT = "context";
    public static final String SETTING_POPULATOR_NAME = "contextPopulator";


    @SocketEvent(socketId = SOCKET_IN_DATA)
    public void populate(final Object data)
    {
        final String populatorName = getWidgetInstanceManager().getWidgetSettings().getString(SETTING_POPULATOR_NAME);
        if(StringUtils.isNotBlank(populatorName))
        {
            final ContextPopulator populator = (ContextPopulator)SpringUtil.getBean(populatorName, ContextPopulator.class);
            if(populator != null)
            {
                final Map<String, Object> populatedContext = populator.populate(data);
                sendOutput(SOCKET_OUT_CONTEXT, populatedContext);
            }
            else
            {
                LOG.warn("Could not resolve context populator bean by bean id: [{}]", populatorName);
            }
        }
    }
}
