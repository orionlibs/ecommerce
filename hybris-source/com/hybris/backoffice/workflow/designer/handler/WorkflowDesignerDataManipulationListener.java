/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

/**
 * Listens on events that modify Workflow Designer model, that is events that change the yet-unsaved data.
 */
public interface WorkflowDesignerDataManipulationListener
{
    /**
     * Notified when model is loaded from scratch or freshly from the database
     *
     * @param model
     *           workflow designer model
     */
    default void onNew(final Object model)
    {
    }


    /**
     * Notified when user interacts with Workflow Designer in a way that changed its underlying model, meaning the user has
     * modified the Workflow and now has unsaved data in model.
     *
     * @param model
     *           workflow designer model
     */
    default void onChange(final Object model)
    {
    }
}
