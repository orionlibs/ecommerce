/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

/**
 * Simple service to select the proper strategy based on the current active engine.
 *
 * @param <T>
 *           Strategy interface
 */
public interface StrategyDeterminationService<T>
{
    /**
     * @return strategy for the current active engine
     */
    T get();
}
