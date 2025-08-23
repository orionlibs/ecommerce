/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import org.apache.velocity.app.VelocityEngine;

/**
 * Provides velocity engine
 */
public class VelocityEngineProvider
{
    private VelocityEngineProvider()
    {
        throw new AssertionError("Creating instances of this class is prohibited");
    }


    public static VelocityEngine provideVelocityEngine()
    {
        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("runtime.strict_mode.enable", Boolean.TRUE);
        velocityEngine.setProperty("resource.default_encoding", "UTF-8");
        velocityEngine.setProperty("output.encoding", "UTF-8");
        velocityEngine.init();
        return velocityEngine;
    }
}
