/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

/**
 * Interface for action call back.
 *
 * @param <O> output type of the action
 */
public interface ActionListener<O>
{
    void actionPerformed(ActionResult<O> result);
}
