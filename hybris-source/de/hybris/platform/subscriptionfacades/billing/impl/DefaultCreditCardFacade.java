/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.billing.impl;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.subscriptionfacades.billing.CreditCardFacade;
import java.util.Collection;

/**
 * Facade for converting credit card codes.
 */
public class DefaultCreditCardFacade implements CreditCardFacade
{
    @Override
    public boolean mappingStrategy(final Collection<CardTypeData> creditCards)
    {
        return false;
    }
}
