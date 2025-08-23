/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import java.util.Set;

/**
 * Base strategy of resetting configuration to defaults. Strategy is used to check whether to execute automated
 * configuration reset triggered on specific event (i.e. user login) or not.
 */
public interface ResetConfigurationStrategy
{
    Set<String> getResetScopes(String trigger);


    boolean isTriggerConfigured(String trigger);
}
