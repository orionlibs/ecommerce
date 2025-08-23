/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import java.util.UUID;

/**
 * Generates IDs of components rendered in Workflow Designer
 */
public class DefaultNetworkEntityKeyGenerator implements KeyGenerator
{
    @Override
    public Object generate()
    {
        return UUID.randomUUID();
    }


    @Override
    public Object generateFor(final Object object)
    {
        return generate();
    }


    @Override
    public void reset()
    {
        throw new UnsupportedOperationException("Key can not be reset");
    }
}
