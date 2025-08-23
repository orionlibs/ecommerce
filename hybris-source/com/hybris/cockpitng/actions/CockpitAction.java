/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

/**
 * Interface of all cockpit action classes.
 *
 * @param <I> input type of the action
 * @param <O> output type of the action
 */
public interface CockpitAction<I, O>
{
    /**
     * Performs the action.
     *
     * @param ctx context containing the data and other information
     * @return the action result
     */
    ActionResult<O> perform(ActionContext<I> ctx);


    /**
     * Returns true if the action can be performed for the given context, false otherwise.
     *
     * @param ctx context containing the data and other information
     * @return true if the action can be performed for the given context, false otherwise
     */
    default boolean canPerform(final ActionContext<I> ctx)
    {
        return true;
    }


    /**
     * Returns true if the action should be confirmed by user before being performed, false otherwise.
     * This is evaluated by the action renderer.
     *
     * @param ctx context containing the data and other information
     * @return true if the action should be confirmed by user before being performed, false otherwise
     */
    default boolean needsConfirmation(final ActionContext<I> ctx)
    {
        return false;
    }


    /**
     * Returns the confirmation message to be shown to the user in the confirmation dialog if confirmation is needed
     * (see {@link #needsConfirmation(ActionContext)}).
     *
     * @param ctx context containing the data and other information
     * @return the confirmation message to be shown to the user in the confirmation dialog
     * @see #needsConfirmation(ActionContext)
     */
    default String getConfirmationMessage(final ActionContext<I> ctx)
    {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
