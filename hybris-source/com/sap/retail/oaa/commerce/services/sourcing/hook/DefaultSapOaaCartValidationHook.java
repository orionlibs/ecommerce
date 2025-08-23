/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.sap.retail.oaa.commerce.services.sourcing.hook;

import com.sap.retail.oaa.commerce.services.sourcing.strategy.SourcingStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.strategies.hooks.CartValidationHook;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;

/**
 * Cart validation hook to do order source during checkout process.
 */
public class DefaultSapOaaCartValidationHook implements CartValidationHook
{
    private static final String SOURCING_INVALID = "ordersourcinginvalid";
    private SourcingStrategy sourcingStrategy;


    @Override
    public void beforeValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications)
    {
        //Nothing to do
    }


    /**
     * Do sourcing for cart
     *
     * @param parameter
     *           the information for validation
     * @param modifications
     *           list containing the validation results
     */
    @Override
    public void afterValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications)
    {
        if(parameter == null)
        {
            return;
        }
        ServicesUtil.validateParameterNotNullStandardMessage("cart", parameter.getCart());
        if(!getSourcingStrategy().doSourcing(parameter.getCart()))
        {
            final CommerceCartModification modification = new CommerceCartModification();
            modification.setStatusCode(SOURCING_INVALID);
            modifications.add(modification);
        }
    }


    public SourcingStrategy getSourcingStrategy()
    {
        return sourcingStrategy;
    }


    public void setSourcingStrategy(final SourcingStrategy sourcingStrategy)
    {
        this.sourcingStrategy = sourcingStrategy;
    }
}
