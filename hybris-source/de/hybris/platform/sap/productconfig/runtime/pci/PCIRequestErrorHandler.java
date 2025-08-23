/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.pci;

import com.hybris.charon.exp.HttpException;
import de.hybris.platform.sap.productconfig.runtime.interf.analytics.model.AnalyticsDocument;

/**
 * Exception Handler for PCI.
 */
public interface PCIRequestErrorHandler
{
    /**
     * Handles any Sever-Side HTTP-Exception when creating an anlytics document.
     *
     * @param ex
     *           HttpException
     * @param analyticsDocumentInput
     * @return analytics document
     */
    AnalyticsDocument processCreateAnalyticsDocumentHttpError(HttpException ex, AnalyticsDocument analyticsDocumentInput);


    /**
     * Processes runtime exceptions like timeout or non-existing server. Default behavior: Timeout exceptions are handled
     * gracefully (just logged) while other runtime exceptions are re-thrown.
     *
     * @param ex
     *           Runtime exception that wraps the actual cause
     * @param analyticsDocumentInput
     * @return analytics
     */
    AnalyticsDocument processCreateAnalyticsDocumentRuntimeException(RuntimeException ex,
                    AnalyticsDocument analyticsDocumentInput);
}
