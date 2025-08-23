/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.delegate;

import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;

/**
 * Contains delegated part of the Configurable Flow controller logic.
 */
public interface ConfigurableFlowControllerDelegate
{
    /**
     * Sets Configurable Flow controller
     * @param controller Configurable Flow controller used in delegate
     */
    void setController(final ConfigurableFlowController controller);
}
