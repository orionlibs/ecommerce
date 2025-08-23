/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.platform.sapcustomerlookupservice.actions;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

public class CustomerMasterLookupCheckEmailVerificationForCustomer extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel>
{
    @Override
    public AbstractSimpleDecisionAction.Transition executeAction(final StoreFrontCustomerProcessModel businessProcessModel)
    {
        final CustomerModel customerModel = businessProcessModel.getCustomer();
        validateParameterNotNullStandardMessage("customerModel", customerModel);
        if(customerModel.getCmsEmailVerificationTimestamp() != null || !businessProcessModel.getStore().isCmsEmailVerificationEnabled())
        {
            return Transition.OK;
        }
        else
        {
            return Transition.NOK;
        }
    }
}
