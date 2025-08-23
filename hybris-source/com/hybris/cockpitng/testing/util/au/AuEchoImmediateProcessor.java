/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util.au;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.Component;

/**
 * Processor that instantly simulates an event passed to {@link AuEcho}.
 *
 * @see CockpitTestUtil#simulateEvent(Component, String, Object)
 */
public class AuEchoImmediateProcessor extends AuResponseProcessor<AuEcho>
{
    @Override
    public void process(final AuEcho response)
    {
        final Component component = (Component)response.getDepends();
        final String eventName = (String)response.getRawData()[1];
        final Integer dataKey = response.getRawData().length > 2 ? (Integer)response.getRawData()[2] : null;
        final Object data = dataKey != null ? AuEcho.getData(component, dataKey) : null;
        simulateEvent(component, eventName, data);
    }


    protected void simulateEvent(final Component component, final String eventName, final Object data)
    {
        CockpitTestUtil.simulateEvent(component, eventName, data);
    }
}
