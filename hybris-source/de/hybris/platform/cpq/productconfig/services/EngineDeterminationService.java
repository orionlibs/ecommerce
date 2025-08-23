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
 * Determines the configuration engine we use
 */
public interface EngineDeterminationService
{
    /**
     * @return Are we using the mock configuration engine?
     */
    boolean isMockEngineActive();
}
