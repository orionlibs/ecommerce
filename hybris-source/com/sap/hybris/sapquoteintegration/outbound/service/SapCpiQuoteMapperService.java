/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.sapquoteintegration.outbound.service;

import com.sap.hybris.sapquoteintegration.model.SAPCpiOutboundQuoteModel;
import de.hybris.platform.core.model.order.QuoteModel;

/**
 * Provides mapping from {@link QuoteModel} to {@link SAPCpiOutboundQuoteModel}.
 *
 * @param <SOURCE> the parameter of the interface
 * @param <TARGET> the parameter of the interface
 */
public interface SapCpiQuoteMapperService<SOURCE extends QuoteModel, TARGET extends SAPCpiOutboundQuoteModel>
{
    /**
     * Performs mapping from source to target.
     *
     * @param source
     *           Quote Model
     * @param target
     *           SAP CPI Outbound Quote Model
     */
    void map(SOURCE source, TARGET target);
}