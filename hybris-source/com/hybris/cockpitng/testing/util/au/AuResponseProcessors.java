/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util.au;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import org.zkoss.zk.au.out.AuEcho;
import org.zkoss.zk.ui.Component;

/**
 * Factory for most common {@link org.zkoss.zk.au.AuResponse} processors.
 */
public class AuResponseProcessors
{
    private AuResponseProcessors()
    {
    }


    /**
     * Processor that instantly simulates an event passed to {@link AuEcho}.
     *
     * @see CockpitTestUtil#simulateEvent(Component, String, Object)
     */
    public static AuResponseProcessor<AuEcho> echoImmediate()
    {
        return new AuEchoImmediateProcessor();
    }
}
