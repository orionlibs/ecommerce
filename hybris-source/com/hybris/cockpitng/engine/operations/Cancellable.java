/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

public interface Cancellable
{
    /**
     * Cancels the operation.
     */
    void cancel();
}
