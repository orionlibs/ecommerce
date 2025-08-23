/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades;

import de.hybris.platform.cpq.productconfig.facades.data.CommerceParameters;

/**
 * Commerce parameters for CPQ
 */
public interface CommerceParametersFacade
{
    /**
     * @return commerce parameters
     */
    CommerceParameters prepareCommerceParameters();
}
