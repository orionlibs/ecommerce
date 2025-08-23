/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

/**
 * An interface that defines contract for performing the cancel action logic for the configurable flow widget.
 */
public interface FlowCancelActionHandler
{
    /**
     * Method that is invoked when the wizard close in the configurable flow widget.
     */
    void perform();
}
