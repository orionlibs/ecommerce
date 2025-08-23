/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.decorator.impl;

import static de.hybris.platform.sap.saps4omservices.constants.Saps4omservicesConstants.SAP_PASSPORT_HEADER_NAME;

import de.hybris.platform.sap.saps4omservices.decorator.SAPS4OMOutboundRequestDecorator;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMPassportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class DefaultSapS4OMPassportRequestDecorator implements SAPS4OMOutboundRequestDecorator
{
    private SapS4OMPassportService sapS4OMPassportService;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSapS4OMPassportRequestDecorator.class);


    @Override
    public void decorate(final HttpHeaders header, String action)
    {
        final String sapPassport = getSapS4OMPassportService().generate(action);
        LOG.debug("Method call from decorate and sapPassport {}", sapPassport);
        header.add(SAP_PASSPORT_HEADER_NAME, sapPassport);
    }


    public SapS4OMPassportService getSapS4OMPassportService()
    {
        return sapS4OMPassportService;
    }


    public void setSapS4OMPassportService(SapS4OMPassportService sapS4OMPassportService)
    {
        this.sapS4OMPassportService = sapS4OMPassportService;
    }
}
