/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type;

/**
 * Strategy for loading type information.
 */
public interface TypeFacadeStrategy extends TypeFacade
{
    boolean canHandle(String typeCode);
}
