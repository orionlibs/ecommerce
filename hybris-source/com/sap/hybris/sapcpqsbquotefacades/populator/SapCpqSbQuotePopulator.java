/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapcpqsbquotefacades.populator;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.quote.data.QuoteData;
import de.hybris.platform.core.model.order.QuoteModel;
import org.springframework.util.Assert;

public class SapCpqSbQuotePopulator extends AbstractOrderPopulator<QuoteModel, QuoteData>
{
    @Override
    public void populate(QuoteModel source, QuoteData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setCpqSbProposalDocumentUrl(source.getCpqSbQuoteProposalDocument());
        target.setEnableDiscount(true);
    }
}